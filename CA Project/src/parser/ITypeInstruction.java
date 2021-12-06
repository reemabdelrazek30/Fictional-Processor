package parser;

import components.DataMemory;
import components.PC;
import components.Register;
import components.RegisterFile;

public class ITypeInstruction extends Instruction 
{
	
    int immediate;
	public int getImmediate() {
		return immediate;
	}

	public void setImmediate(int immediate) {
		this.immediate = immediate;
	}

	public ITypeInstruction(int opcode, int r1 , int immediate,RegisterFile rf) 
	{
		super(opcode, r1,rf);
		this.immediate = immediate;
	}
	
	public void assignFlags(int integerResult , byte byteResult, boolean[] requiredFlags) {
		Register StatusRegister = rf.getStatusRegister();
		boolean C,V,N,S,Z;
		C = V = N = S = Z = false;
		
		boolean bothNegative = (Math.signum(byteResult) == -1 & Math.signum(integerResult) == -1);
		boolean bothPositive = (Math.signum(byteResult) == 1 & Math.signum(integerResult) == 1);
		
		// Carry Flag (C) check
		if (requiredFlags[0])
			if ((bothNegative && integerResult < Byte.MIN_VALUE) || (bothPositive && integerResult > Byte.MAX_VALUE))
				C = true;
			else if (integerResult < 0 && byteResult > 0)
				C = true;
		
		// Overflow Flag (V) check
		if (requiredFlags[1])
			if (!bothNegative && !bothPositive)
				V = true;
		
		// Negative Flag (N) check
		if (requiredFlags[2])
			if (byteResult < 0)
				N = true;
		
		// Zero Flag (Z) check
		if (requiredFlags[3])
			if (byteResult == 0)
				Z = true;
		
		// Sign Flag (S) check
		if (requiredFlags[4]) 
			S = N ^ V;
		
		// get old status and reallocate removed zeros
		String oldStatus = Integer.toBinaryString(StatusRegister.getQ());
		while (oldStatus.length() != 5)
			oldStatus = "0" + oldStatus;
		
		String newStatus = "000";
		
		if (requiredFlags[0]) newStatus += (C?'1':'0');
		else				  newStatus += (oldStatus.charAt(0));
		
		if (requiredFlags[1]) newStatus += (V?'1':'0');
		else				  newStatus += (oldStatus.charAt(1));
		
		if (requiredFlags[2]) newStatus += (N?'1':'0');
		else				  newStatus += (oldStatus.charAt(2));
		
		if (requiredFlags[3]) newStatus += (S?'1':'0');
		else				  newStatus += (oldStatus.charAt(3));
		
		
		if (requiredFlags[4]) newStatus += (Z?'1':'0');
		else				  newStatus += (oldStatus.charAt(4));
		
		Byte status = Byte.parseByte(newStatus,2);
		StatusRegister.setD(status); 
	}
	
    public void andImmediate()
    {
    	Register [] registers = rf.getRegisters();
        int value = registers[getR1()].getQ();
    	registers[getR1()].setD((byte)(value & immediate));
    	assignFlags(value & immediate,(byte)(value & immediate),new boolean[]{false,false,true,false,true}); 

        
    }
    
    
    public void moveImmediate() {
		Register[] registers = rf.getRegisters();
		registers[getR1()].setD((byte) immediate);
		byte immediateByte = (byte) immediate;	// unsafe cast, but assignFlags() checks what happened
		registers[getR1()].setLoad(true);
		registers[getR1()].setD(immediateByte);
		
		
    }
    
    // Maps to BEQZ - Branch if Equal Zero
    public void branchOnZero() {
    	Register[] registers = rf.getRegisters();
    	int r1Value = registers[getR1()].getQ();
    	
    	if (r1Value == 0) {
    		int pcCurrentValue = (byte) PC.getPC().getInternalRegister().getQ();
    		PC.getPC().getInternalRegister().setD((byte) (pcCurrentValue + immediate));
    	}
    }
    
   public void shiftArithmeticLeft() {
	   	Register[] registers = rf.getRegisters();
	   	registers[getR1()].setD((byte) (registers[getR1()].getQ() << immediate));
   		assignFlags(registers[getR1()].getQ() << immediate,(byte) (registers[getR1()].getQ() << immediate),new boolean[]{false,false,true,false,true}); 

   }
   
   public void shiftArithmeticRight() {
	   Register[] registers =rf.getRegisters();
	   registers[getR1()].setD((byte) (registers[getR1()].getQ() >> immediate));
  		assignFlags(registers[getR1()].getQ() >> immediate,(byte) (registers[getR1()].getQ() >> immediate),new boolean[]{false,false,true,false,true}); 

   }
   
   public void loadToRegister( DataMemory memory) {
	   byte targetAddress;
	   try {
		   targetAddress = memory.getMemory()[immediate];
	   } catch (ArrayIndexOutOfBoundsException e) {
		   System.out.println("Array Index out of Bounds");
		   return;
	   }
	   
	   Register[] registers = rf.getRegisters();
	   registers[getR1()].setD(targetAddress);
   }
   
   public void storeToRegister(DataMemory memory) {
	   try {
		   @SuppressWarnings("unused")
		   byte targetAddress = memory.getMemory()[immediate];
	   } catch (ArrayIndexOutOfBoundsException e) {
		   System.out.println("Array Index out of Bounds");
		   return;
	   }
	   
	   
	   Register checkedR1;
	   Register[] registers = rf.getRegisters();
	   checkedR1 = registers[getR1()];
	   if (checkedR1 == null) {
		   System.out.println("Null value!!");
		   return;
	   }
	   memory.getMemory()[immediate] = checkedR1.getQ();
   }
}

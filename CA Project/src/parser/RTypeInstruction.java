package parser;

import components.PC;
import components.Register;
import components.RegisterFile;

public class RTypeInstruction extends Instruction {
	private int r2;

	public RTypeInstruction(int opcode , int r1 , int r2,RegisterFile rf)
	{
		super(opcode,r1,rf);
		this.setR2(r2);
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
	
	/*public void assignFlags(int result , byte resultB)
	{
		Register StatusRegister = rf.getStatusRegister();
		boolean C,V,N,S,Z;
		C = V = N = S = Z = false;
		if (Math.signum(resultB) * Math.signum(result) == 1.0 )
		{
			if ((Math.signum(resultB) == 1.0 && result > resultB) || (Math.signum(resultB) == -1.0 && result < resultB ))
				C = true;
		}
		else if (Math.signum(resultB) == 1.0 && Math.signum(result) == -1.0)
			C = true;
		if (Math.signum(resultB) == -1 && Math.signum(result) == 1.0)
			V = true;
		N = (Math.signum(resultB) == -1.0);
		S = N ^ V;
		if (Math.signum(resultB) == 0.0 )
			Z = true;
		String newStatus = "000";
		
		newStatus += (C?'1':'0');
		newStatus += (V?'1':'0');
		newStatus += (N?'1':'0');
		newStatus += (S?'1':'0');
		newStatus += (Z?'1':'0');
		Byte status = Byte.parseByte(newStatus);
		StatusRegister.setD(status); 
		
	}*/
	public void add()
	{
		Register[] registers = rf.getRegisters();
		int result = registers[getR1()].getQ() + registers[getR2()].getQ();
		
		byte resultB = (byte) (registers[getR1()].getQ() + registers[getR2()].getQ());
		
		assignFlags(result,resultB,new boolean[]{true,true,true,true,true});
		registers[getR1()].setLoad(true);
		registers[getR1()].setD(resultB);
		
	}
	public void subtract()
	{
		Register[] registers = rf.getRegisters();
		int result = registers[getR1()].getQ() - registers[getR2()].getQ();
		byte resultB = (byte) (registers[getR1()].getQ() - registers[getR2()].getQ());
		assignFlags(result,resultB,new boolean[]{true,true,true,true,true});
		registers[getR1()].setLoad(true);
		registers[getR1()].setD(resultB);
		
	}

	public void multiply()
	{
		Register[] registers = rf.getRegisters();
		int result = registers[getR1()].getQ() *  registers[getR2()].getQ();
		byte resultB = (byte) (registers[getR1()].getQ() * registers[getR2()].getQ());
		assignFlags(result,resultB,new boolean[]{true,false,true,false,true});
		registers[getR1()].setLoad(true);
		registers[getR1()].setD(resultB);
		
	}
	public void exclusiveOr()
	{
		Register [] registers = rf.getRegisters();
		int result = registers[getR1()].getQ() ^ registers[getR2()].getQ();
		byte resultB = (byte) (registers[getR1()].getQ() ^ registers[getR2()].getQ()); 
		registers[getR1()].setLoad(true); 
		registers[getR1()].setD(resultB);
		assignFlags(result,resultB,new boolean[]{false,false,true,false,true});
	}
	public void branch() 
	{
		Register [] registers = rf.getRegisters();
		short conactenatedAddress = Short.parseShort((Integer.toBinaryString(registers[getR1()].getQ()) + Integer.toBinaryString(registers[getR2()].getQ())),2);
		PC pc = PC.getPC();
		pc.getInternalRegister().setD(conactenatedAddress);
		pc.getInternalRegister().setLoad(true);
		pc.getInternalRegister().update();
		
		
	}
	public int getR2() {
		return r2;
	}
	public void setR2(int r2) {
		this.r2 = r2;
	}
	
	
}



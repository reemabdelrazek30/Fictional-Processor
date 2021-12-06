package dataPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import components.DataMemory;
import components.InstructionMemory;
import components.PC;
import components.Register;
import components.RegisterFile;
import parser.ITypeInstruction;
import parser.Instruction;
import parser.RTypeInstruction;

public class CPU {
	// Class to run;
	InstructionMemory Imemory;
	RegisterFile rf;
	DataMemory dm;
	static int cycles = 0;

	public CPU() {
		Imemory = new InstructionMemory();
		rf = new RegisterFile();
		dm = new DataMemory();
	}

	public short fetch() {
		PC x = PC.getPC();
		short address = x.getInternalRegister().getQ();
		System.out.println("Fetching instruction from address "+address+" with value"+Imemory.readFromMemory(address));
		return Imemory.readFromMemory(address);
	}

	public void parse(String fileName) throws IOException {
		try {
			File file = new File("src/dataPath/" + fileName + ".txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String data;
			Vector<String> components = new Vector<String>();
			components.add(fileName);
			while ((data = br.readLine()) != null) {
				if (!data.equals("")) {
					components.add(data);
				}
			}
			br.close();
			short opcode;
			short totalInst = 0;
			for (int i = 0; i < components.size(); i++) {
				String[] line = components.get(i).split(" ");
				if (line[0].equals("ADD")) {
					opcode = 0;
					short r1 = Short.parseShort(line[1].substring(1));
					short r2 = Short.parseShort(line[2].substring(1));
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | r2);
				}
				if (line[0].equals("SUB")) {
					opcode = 0b0001000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short r2 = Short.parseShort(line[2].substring(1));
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | r2);
				}
				if (line[0].equals("MUL")) {
					opcode = 0b0010000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short r2 = Short.parseShort(line[2].substring(1));
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | r2);
				}
				if (line[0].equals("MOVI")) {
					opcode = 0b0011000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short imm = (short) (Short.parseShort(line[2]) & 0b0000000000111111);
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | imm);

				}
				if (line[0].equals("BEQZ")) {
					opcode = 0b0100000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short imm = (short) (Short.parseShort(line[2]) & 0b0000000000111111);
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | imm);
				}
				if (line[0].equals("ANDI")) {
					opcode = 0b0101000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short imm = (short) (Short.parseShort(line[2]) & 0b0000000000111111);
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | imm);
				}
				if (line[0].equals("EOR")) {
					opcode = 0b0110000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short r2 = Short.parseShort(line[2].substring(1));
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | r2);
				}
				if (line[0].equals("BR")) {
					opcode = 0b0111000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short r2 = Short.parseShort(line[2].substring(1));
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | r2);
				}
				if (line[0].equals("SAL")) {
					opcode = (short)0b1000000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short imm = (short) (Short.parseShort(line[2])& 0b0000000000111111);
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | imm);
				}
				if (line[0].equals("SAR")) {
					opcode = (short)0b1001000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short imm = (short) (Short.parseShort(line[2]) & 0b0000000000111111);
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | imm);
				}
				if (line[0].equals("LDR")) {
					opcode = (short)0b1010000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short imm = (short) (Short.parseShort(line[2]) & 0b0000000000111111);
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | imm);
				}
				if (line[0].equals("STR")) {
					opcode = (short)0b1011000000000000;
					short r1 = Short.parseShort(line[1].substring(1));
					short imm = (short) (Short.parseShort(line[2]) & 0b0000000000111111);
					r1 = (short) (r1 << 6);
					totalInst = (short) (opcode | r1 | imm);
				}
				Imemory.addInstruction(totalInst);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public Instruction decode(short instruction) {
		short opcode = (short) (instruction & 0b1111000000000000);
		opcode = (short) (opcode >> 12);

		if ((0 <= opcode && opcode <= 2) || (opcode == 6 || opcode == 7)) {
			short r1 = (short) (instruction & 0b0000111111000000);
			r1 = (short) (r1 >> 6);
			short r2 = (short) (instruction & 0b0000000000111111);
			RTypeInstruction toBeReturned = new RTypeInstruction(opcode, r1, r2, rf);
			System.out.println("Decoded "+instruction+" into"
					+ "an instruction with opcode = "+opcode+", r1 = "+r1+
					", r2 = "+r2);
			return toBeReturned;
		} else {
			short r1 = (short) (instruction & 0b0000111111000000);
			r1 = (short) (r1 >> 6);
			short imm = (short) (instruction & 0b0000000000111111);
			if(imm>31) {
				imm = (short) (imm-64);
			}
			ITypeInstruction toBeReturned = new ITypeInstruction(opcode, r1, imm, rf);
			System.out.println("Decoded "+instruction+" into"
					+ "an instruction with opcode = "+opcode+", r1 = "+r1+
					", imm = "+imm);
			return toBeReturned;
		}

	}

	public void execute(Instruction toBeExecuted) {
		if (toBeExecuted instanceof ITypeInstruction) {
			short opcode = (short) toBeExecuted.getOpcode();
			switch (opcode) {
			case 3:
				((ITypeInstruction) toBeExecuted).moveImmediate();
				System.out.println("Executing instruction Move Immediate to "+toBeExecuted.getR1()+"with value "+((ITypeInstruction) toBeExecuted).getImmediate());
				break;
			case 4:
				((ITypeInstruction) toBeExecuted).branchOnZero();
				System.out.println("Executing instruction Branch if equal zero to "+toBeExecuted.getR1()+"with value "+((ITypeInstruction) toBeExecuted).getImmediate());
				break;
			case 5:
				((ITypeInstruction) toBeExecuted).andImmediate();
				System.out.println("Executing instruction And Immediate to "+toBeExecuted.getR1()+"with value "+((ITypeInstruction) toBeExecuted).getImmediate());
				break;
			case -8:
				((ITypeInstruction) toBeExecuted).shiftArithmeticLeft();
				System.out.println("Executing instruction Shift Arithmatic Left to "+toBeExecuted.getR1()+"with value "+((ITypeInstruction) toBeExecuted).getImmediate());
				break;
			case -7:
				((ITypeInstruction) toBeExecuted).shiftArithmeticRight();
				System.out.println("Executing instruction Shift Arithmatic Right to "+toBeExecuted.getR1()+"with value "+((ITypeInstruction) toBeExecuted).getImmediate());
				break;
			case -6:
				((ITypeInstruction) toBeExecuted).loadToRegister(dm);
				System.out.println("Executing instruction Load To Register to "+toBeExecuted.getR1()+"with value "+((ITypeInstruction) toBeExecuted).getImmediate());
				break;
			case -5:
				((ITypeInstruction) toBeExecuted).storeToRegister(dm);
				System.out.println("Executing instruction Store To Register to "+toBeExecuted.getR1()+"with value "+((ITypeInstruction) toBeExecuted).getImmediate());
				break;
			}
		} else {

			short opcode = (short) toBeExecuted.getOpcode();
			switch (opcode) {
			case 0:
				((RTypeInstruction) toBeExecuted).add();
				System.out.println("Executing instruction Add to "+toBeExecuted.getR1()+"and to "+((RTypeInstruction) toBeExecuted).getR2());
				break;
			case 1:
				((RTypeInstruction) toBeExecuted).subtract();
				System.out.println("Executing instruction Subtract to "+toBeExecuted.getR1()+"and to "+((RTypeInstruction) toBeExecuted).getR2());
				break;
			case 2:
				((RTypeInstruction) toBeExecuted).multiply();
				System.out.println("Executing instruction Multiply to "+toBeExecuted.getR1()+"and to "+((RTypeInstruction) toBeExecuted).getR2());
				break;
			case 6:
				((RTypeInstruction) toBeExecuted).exclusiveOr();
				System.out.println("Executing instruction XOR to "+toBeExecuted.getR1()+"and to "+((RTypeInstruction) toBeExecuted).getR2());
				break;
			case 7:
				((RTypeInstruction) toBeExecuted).branch();
				System.out.println("Executing instruction Branch Register to "+toBeExecuted.getR1()+"and to "+((RTypeInstruction) toBeExecuted).getR2());
			}

		}
	}

	public void clock() {
		PC.getPC().getInternalRegister().setD((byte) (PC.getPC().getInternalRegister().getQ() + 1));
		PC.getPC().getInternalRegister().setLoad(true);
		PC.getPC().getInternalRegister().update();
		rf.getStatusRegister().update();
		for (int i = 0; i < rf.getRegisters().length; i++) {
			rf.getRegisters()[i].update();
		}
		System.out.println("Clock cycle"+cycles);
		cycles++;
	}

	public void Run() {
		short fetched = 0;
		Instruction decoded = null;
		for (int i = 0; i < Imemory.getMemBoundary(); i++) {
			if (i > 1) {
				execute(decoded);
			}
			if (i > 0) {
				decoded = decode(fetched);
			}
			fetched = fetch();
			clock();

		}
		if (Imemory.getMemBoundary() != 1) {
			execute(decoded);
		}
		decoded = decode(fetched);
		clock();
		execute(decoded);
		clock();

	}

	public static void main(String[] args) {
		CPU x = new CPU();
		try {
			x.parse("stuff");
			x.Run();
			System.out.println("Final Registers:");
			for(int i = 0; i < x.rf.getRegisters().length; i++) {
				System.out.println(x.rf.getRegisters()[i].getQ());
			}
			System.out.println("Data Memory:");
			for(int i = 0; i < x.dm.getMemory().length; i++) {
				System.out.println(x.dm.getMemory()[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}

package components;

import java.util.Arrays;

public class RegisterFile {
	private Register [] registers; // I changed this
	private Register statusRegister;
	//pc programcounter ;
	public RegisterFile()
	{	
		registers = new Register [64];
		//Arrays.fill(registers,new Register());
		for(int i = 0; i < registers.length; i++) {
			registers[i] = new Register();
		}
		statusRegister = new Register();
		PC.getPC();
	}
	
	/*
	 * public void setProgramcounter(pc programcounter) { this.programcounter =
	 * programcounter; }
	 */
	public Register[] getRegisters() {   //I changed it to be static 
		return registers;
	}
	public void setRegisters(Register[] registers) { 
		this.registers = registers;
	}
	public Register getStatusRegister() {
		return statusRegister;
	}
	public void setStatusRegister(Register statusRegister) {
		this.statusRegister = statusRegister;
	}
}

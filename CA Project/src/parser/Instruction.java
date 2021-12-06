package parser;

import components.RegisterFile;

public class Instruction {
      int opcode;
      private int r1;
      RegisterFile rf;
 public int getOpcode() {
		return opcode;
	}
	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
public Instruction(int opcode , int r1,RegisterFile rf)
 {	 this.rf = rf;
	 this.opcode = opcode;
	 this.setR1(r1);
	 
 }
public int getR1() {
	return r1;
}
public void setR1(int r1) {
	this.r1 = r1;
}
 
}

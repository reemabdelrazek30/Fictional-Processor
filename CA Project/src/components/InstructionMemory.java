package components;

public class InstructionMemory {
	int memBoundary;
	short[] memory ;
	public int getMemBoundary() {
		return memBoundary;
	}
	public void setMemBoundary(int memBoundary) {
		this.memBoundary = memBoundary;
	}
	public InstructionMemory() {
		memory=new short[1024];
		memBoundary = 0;
	}
	public short[] getMemory() {
		return memory;
	}
	public void setMemory(short[] memory) {
		this.memory = memory;
	}
	public void writeToMemory(short x,int address) {
		memory[address]=x;
		return;
	}
		
	public short readFromMemory(int address) {
		return memory[address];
	}
	public void addInstruction(short instruction) {
		memory[memBoundary]=instruction;
		memBoundary+=1;
	}
}

package components;

public class DataMemory {
	byte [] memory;
	
	public DataMemory() {
		memory = new byte[2048];	
	}
	
	public byte[] getMemory() {
		return memory;
	}
	
	public void setMemory(byte[] memory) {
		this.memory = memory;
	}
	
	public void writeToMemory(byte x,int address) {
		
		memory[address]=x;
		System.out.println("Wrote to memory "+x+" at address "+address);
		return;
	}
		
	public byte readFromMemory(int address) {
		System.out.println("Read from address "+address+" value "+memory[address]);
		return memory[address];
	}
}

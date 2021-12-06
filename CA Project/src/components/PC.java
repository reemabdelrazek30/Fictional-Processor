package components;
// We need this to be a singleton class because we have one PC - Reem

/* TDE Change List:
 * Changed internalRegister to private and created respective setters and getters
 * Setter provides a deep copy, so it's safe.
 * */
public class PC {
	private static PC programCounter = null;
	private Register16 internalRegister;
	private PC() 
	{
		internalRegister = new Register16();
	}
	public static PC getPC()
	{
		if (programCounter == null)
			programCounter = new PC();
		return programCounter;
	}
	public static void setPC(Register16 input)
	{
		//programCounter.internalRegister.setLoad(true);
	    programCounter.internalRegister = input;
	    
	}
	
	public Register16 getInternalRegister() { return internalRegister; }
	public void setInternalRegister(Register reg) {
		internalRegister.setD(reg.getD());
		internalRegister.setQ(reg.getQ());
	}
}

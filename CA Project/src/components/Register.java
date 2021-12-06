package components;

public class Register {
	boolean load;
	byte D=0;
	byte q=0;
	
	public Register() {
		
	}
	public Register(boolean load,byte D) {
		this.load=load;
		this.D = D;
	}
	public void update() {
		if(load==true) {
			if(q!=D)
				System.out.println("Register value changed from "+D+" to "+q);
			q=D;
			
		}
	}

	public byte getQ() 		 { return q;   }
	public void setQ(byte q) { this.q = q; }
	
	public boolean isLoad() {
		return load;
	}

	public void setLoad(boolean load) {
		this.load = load;
		
	}

	public byte getD() {
		return D;
	}

	public void setD(byte d) {
		D = d;
	}

}

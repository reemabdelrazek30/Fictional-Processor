package components;

public class Register16 {
	boolean load;
	short D = 0;
	short q = 0;
	
	public Register16() {
		
	}
	public Register16(boolean load,byte D) {
		this.load=load;
		this.D = D;
	}
	public void update() {
		if(load==true) {
			q=D;
		}
	}

	public short getQ() 		 { return q;   }
	public void setQ(short q) { this.q = q; }
	
	public boolean isLoad() {
		return load;
	}

	public void setLoad(boolean load) {
		this.load = load;
		update();
		
	}

	public short getD() {
		return D;
	}

	public void setD(short d) {
		D = d;
		update();
	}

}

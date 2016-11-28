class Register
{
	String regName="";
	int regNo=-1;
	boolean isRob;
	boolean regReady;

	public Register(String regName, int regNo)
	{
		this.regName = regName;
		this.regNo = regNo;
	}

	public void updateValues(String regName, int regNo, boolean isRob)
	{
		this.regName = regName;
		this.regNo = regNo;
		this.isRob = isRob;
	}
}

class Instruction
{
	String pcValue="";
	Register dst;
	Register src1;
	Register src2;
	int opType;
	int ipc=0; 
	int cycleDetails[][];

	public Instruction()
	{		
	}

	public Instruction(String pcValue, Register dst, Register src1, Register src2, int opType)
	{
		this.pcValue = pcValue;
		this.dst = dst;
		this.src1 = src1;
		this.src2 = src2;
		this.opType = opType;
		cycleDetails = new int[Constants.NO_OF_STAGES][2];
	}

	public void updateEntryPoint(int key , int value)
	{
		cycleDetails[key][0] = value;
	}

	public void updateDuration(int key, int value)
	{		
		cycleDetails[key][1] = value - cycleDetails[key][0]; 
		ipc+= cycleDetails[key][1];
	}
}
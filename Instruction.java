class Register
{
	String regName="";
	int regNo;
	boolean isRob;

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
	public Instruction(String pcValue, Register dst, Register src1, Register src2, int opType)
	{
		this.pcValue = pcValue;
		this.dst = dst;
		this.src1 = src1;
		this.src2 = src2;
		this.opType = opType;
	}
}
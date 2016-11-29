class Register
{
	String regName="";
	int regNo=-1;
	boolean isRob;
	boolean regReady=true;

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
	public Register copy()
	{
		return new Register(this.regName, this.regNo);
	}
}

class Instruction
{
	String pcValue="";
	Register dst;
	Register src1;
	Register src2;
	Register orgSrc1;
	Register orgSrc2;
	Register orgDst;
	int opType;
	int ipc=0; 
	int cycleDetails[][];
	int instructionNo;

	public Instruction(String pcValue, Register dst, Register src1, Register src2, int opType)
	{
		this.pcValue = pcValue;
		this.dst = dst;
		this.src1 = src1;
		this.src2 = src2;
		this.orgDst = dst.copy();
		this.orgSrc1 = src1.copy();
		this.orgSrc2 = src2.copy();
		this.opType = opType;
		cycleDetails = new int[Constants.NO_OF_STAGES][2];
		for(int i=0;i<Constants.NO_OF_STAGES;i++) {
			cycleDetails[i][0] = -1;
			cycleDetails[i][1] = -1;
		}
	}

	public void updateEntryPoint(int key , int value)
	{
		if(cycleDetails[key][0]==-1)
			cycleDetails[key][0] = value;
	}

	public void updateDuration(int key, int value)
	{		
		cycleDetails[key][1] = value - cycleDetails[key][0];
		ipc+= cycleDetails[key][1];
	}

	public void printStagesInfo()
	{
		String stages[] = { "FE","DE","RN","RR","DI","IS","EX","WB","RT"};
		System.out.print(""+(instructionNo-1)+" fu{"+opType+"} src{"+orgSrc1.regNo+","+orgSrc2.regNo+"} dst{"+orgDst.regNo+"} ");
		for(int i=0;i<stages.length;i++)
		{
			if(i==0)
				System.out.print(stages[i]+"{"+(cycleDetails[1][0]-1)+","+"1} ");
			else
				System.out.print(stages[i]+"{"+cycleDetails[i][0]+","+cycleDetails[i][1]+"} ");
		}
		System.out.println();
	}
	public void printInfo()
	{
		System.out.println((instructionNo-1)+" "+pcValue+" "+dst.regName+" "+src1.regName+"("+src1.regReady+"), "+src2.regName+"("+src2.regReady+") "+opType);
	}
}
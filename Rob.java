class RobEntry
{
	boolean valueSet;
	String destReg;
	int destRegNo;
	boolean ready;
	int instructionNo;
	String pcValue;
}

class Rob
{
	int head = 3;
	int tail = 3;
	RobEntry buffer[];
	static int instructionNo=1;

	public Rob(int robSize)
	{
		this.buffer = new RobEntry[robSize];
		for(int i=0;i<buffer.length;i++)
			buffer[i] = new RobEntry();
	}

	public boolean robNotFull(ArrayList<Instruction> bundle)
	{		
		int counter = 0;
		counter = bundle.size();

		/* for(Instruction instr:bundle)
		{
			if(instr.destReg.regNo!=-1)
				counter++;
		} */
		
		int space =  if(tail>head)?(buffer.length - tail + head):(head-tail);
		
		if(space>counter)
			return true;

		return false;
	}

	public void tagRegisters(ArrayList<Instruction> bundle, int renameTable[])
	{
		
		for(Instruction instr:bundle)
		{
			//check for -1 case once
			buffer[tail].destReg = bundle.regName;
			buffer[tail].destRegNo = bundle.regNo;
			buffer[tail].pcValue = bundle.pcValue;
			buffer[tail].instructionNo = Rob.instructionNo;

			renameTable[instr.regNo] = tail;

			instr.isRob = true;
			instr.regNo = tail;
			instr.regName = "rob"+tail;			
			
			Rob.instructionNo++;
			tail++;
		}
	}

}
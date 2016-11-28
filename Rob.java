import java.util.ArrayList;

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
		
		int space =  (tail>head)?(buffer.length - tail + head):(head-tail);
		
		if(space>counter || tail==head)
			return true;

		return false;
	}

	public void tagRegisters(ArrayList<Instruction> bundle, int renameTable[])
	{
		
		for(Instruction instr:bundle)
		{
			//check for -1 case once
			buffer[tail].destReg = instr.dst.regName;
			buffer[tail].destRegNo = instr.dst.regNo;
			buffer[tail].pcValue = instr.pcValue;
			buffer[tail].instructionNo = Rob.instructionNo;

			if( instr.dst.regNo!=-1 )
				renameTable[instr.dst.regNo] = tail;

			instr.dst.isRob = true;
			instr.dst.regNo = tail;
			instr.dst.regName = "rob"+tail;
			instr.dst.regReady = false;
			instr.instructionNo = Rob.instructionNo;
			
			Rob.instructionNo++;
			tail =  (tail+1)%buffer.length;
		}
	}

	public int retire(int width,ArrayList<Instruction> rt)
	{
		int counter=0;
		for(int i=0;i<width && head!=tail && buffer[head].ready;i++)
		{
			Instruction instr = rt.get(0);
			if(instr.instructionNo!=buffer[head].instructionNo)
				break;
			counter++;
			head =  (head+1)%buffer.length;
			instr.printStagesInfo();
			rt.remove(0);
		}
		return counter;
	}

}
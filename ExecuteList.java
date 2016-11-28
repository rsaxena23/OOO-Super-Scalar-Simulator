import java.util.ArrayList;
class ExecEntry
{
	int execCycles;
	Instruction instruction;
	public ExecEntry(Instruction instruction, int execCycles)
	{
		this.instruction = instruction;
		this.execCycles = execCycles;
	}
}

class ExecuteList
{
	ArrayList<ExecEntry> entries;
	int maxSize;

	public ExecuteList(int maxSize)
	{
		entries = new ArrayList<ExecEntry>();
		this.maxSize = maxSize;
	}

	public boolean notFull()
	{
		if(entries.size()<maxSize)
			return true;
		return false;
	}

	public int space()
	{
		return maxSize - entries.size();
	}

	public void insertBundle(ArrayList<Instruction> bundle)
	{
		for(Instruction instr:bundle)
		{
			int execCycles;
			if(instr.opType==Constants.OP_ZERO)
				execCycles = 1;
			else if( instr.opType==Constants.OP_ONE )
				execCycles = 2;
			else if( instr.opType==Constants.OP_TWO )
				execCycles = 5;
			else
			{
				System.out.println("Operation Type issue");
			}

			entries.append( new ExecEntry(instr,execCycles)  );
		}
	}

	public boolean notEmpty()
	{
		return entries.size()>0;
	}

	public ArrayList<Instruction> runInstructions()
	{
		ArrayList<Instruction> finishedBundle = new ArrayList<Instruction>();
		for(Instruction instr:entries)
		{
			instr.execCycles-=1;
			if(instr.execCycles==-1)
				finishedBundle.add(instr);
		}
		return finishedBundle;
	}
	
}
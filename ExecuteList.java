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
				execCycles=0;
				System.out.println("Operation Type issue");
			}

			entries.add( new ExecEntry(instr,execCycles)  );
		}
	}

	public boolean notEmpty()
	{
		return entries.size()>0;
	}

	public ArrayList<Instruction> runInstructions()
	{
		int index=0;
		ArrayList<Instruction> finishedBundle = new ArrayList<Instruction>();
		while(index<entries.size())
		{
			ExecEntry execEntry = entries.get(index);
			execEntry.execCycles-=1;
			if(execEntry.execCycles==0) {
				finishedBundle.add(execEntry.instruction);
				entries.remove(index);
			}
			else
				index++;
		}
		return finishedBundle;
	}
	public void printInfo()
	{
		System.out.println("\nExecute List");
		for(ExecEntry execEntry:entries)
		{
			System.out.print("cycles:"+execEntry.execCycles+"  instr:");
			execEntry.instruction.printInfo();
		}
	}
	
}
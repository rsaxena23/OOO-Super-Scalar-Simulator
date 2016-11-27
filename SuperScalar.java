import java.util.HashMap;

class SuperScalar
{
	int width;
	IssueQueue iq;
	Rob rob;
	HashMap renameTable;
	static int cycleNumber=0;

	/*
	Data Structures for cycle phases
	*/
	Instruction de[], rn[], rr[], di[], ex[], wb[], rt[];

	public SuperScalar(int width, int iqSize,int robSize)
	{
		this.width = width;
		this.iq = new IssueQueue(iqSize);
		this.rob = new Rob(robSize);
		renameTable = new HashMap();		
	}

	public boolean fetch(ArrayList<Instruction> bundle)
	{
		return false;
	}

	public boolean decode()
	{
		return false;
	}

	public boolean rename()
	{
		return false;
	}

	public boolean regRead()
	{
		return false;
	}

	public boolean dispatch()
	{
		return false;
	}

	public boolean issue()
	{
		return false;
	}

	public boolean execute()
	{
		return false;
	}

	public boolean writeback()
	{
		return false;
	}

	public boolean retire()
	{
		return false;
	}
}
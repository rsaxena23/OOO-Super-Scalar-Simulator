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
		de = new Instruction[width];
		rn = new Instruction[width];
		rr = new Instruction[width];
		di = new Instruction[width];
		ex = new Instruction[width][5];
		wb = new Instruction[width][5];
		rt = new Instruction[width];
	}
}
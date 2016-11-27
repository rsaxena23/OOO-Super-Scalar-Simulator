import java.util.ArrayList;
import java.util.PriorityQueue;

class IQEntry
{
	boolean src1Rdy;
	boolean src2Rdy;
	Instruction instruction;
}

public class IssueQueue
{
	ArrayList<IQEntry> entries;
	int maxSize;
	public IssueQueue(int size)
	{
		this.entries = new ArrayList<IQEntry>();
		this.maxSize = size;
	}
}
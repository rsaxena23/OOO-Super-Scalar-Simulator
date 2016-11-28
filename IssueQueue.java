import java.util.ArrayList;
import java.util.PriorityQueue;

class IQEntry
{
	Register src1;
	boolean src1Rdy;
	Register src2;
	boolean src2Rdy;
	Instruction instruction;
	public IQEntry(Instruction instruction, boolean src1Rdy, boolean src2Rdy)
	{
		this.src1 = instruction.src1;
		this.src2 = instruction.src2;

		this.src1Rdy = src1Rdy;
		this.src2Rdy = src2Rdy;
		this.instruction = instruction;
	}
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

	public boolean canInsertBundle(ArrayList<Instruction> bundle)
	{
		if( bundle.size()<(maxSize - entries.size()) )
			return true;
		return false;
	}

	public void insertBundle(ArrayList<Instruction> bundle, Rob rob)
	{
		for(Instruction instr:bundle)
		{
			boolean src1Rdy = true, src2Rdy = true;

			if(instr.src1.isRob)
				src1Rdy = rob[instr.src1.regNo].ready
			if(instr.src2.isRob)
				src2Rdy = rob[instr.src2.regNo].ready

			IQEntry newEntry = new IQEntry(instr, src1Rdy, src2Rdy);
			entries.append(newEntry);
		}
	}


}
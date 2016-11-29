import java.lang.reflect.Array;
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
	boolean full = false;

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
		
		if(space>=counter || (tail==head && !full ) )
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
			buffer[tail].ready = false;

			if( instr.dst.regNo!=-1 )
			{
				//System.out.println(Rob.instructionNo+" RN dst:"+instr.dst.regNo+"="+tail);
				renameTable[instr.dst.regNo] = tail;
			}
			else
				buffer[tail].ready = true;

			instr.dst.isRob = true;
			instr.dst.regNo = tail;
			instr.dst.regName = "rob"+tail;
			instr.dst.regReady = false;
			instr.instructionNo = Rob.instructionNo;
			
			Rob.instructionNo++;
			tail =  (tail+1)%buffer.length;
			if(tail==head)
				full=true;

		}
	}

	public int retire(int width,ArrayList<Instruction> rt, int cycleNumber, int renameTable[])
	{
		int counter=0;
		for(int i=0;i<width && (head!=tail || full ) && buffer[head].ready && i<rt.size();i++)
		{
			Instruction instr = rt.get(0);
			if(instr.instructionNo!=buffer[head].instructionNo) {
			/*	System.out.print("\nCan't Retire: "+(buffer[head].instructionNo-1)+" h:"+head+" t:"+tail+" s:");
				instr.printInfo();
				printRow(head);
				printRow(head-1);
				printRow(head+1); */
				break;
			}

			//System.out.println("Head: "+head+" Tail:"+tail);

			counter++;

			if( instr.orgDst.regNo!=-1 &&  renameTable[ instr.orgDst.regNo ] == head )
				renameTable[ instr.orgDst.regNo ]=-1;
			//resetHead();
			head =  (head+1)%buffer.length;
			instr.updateDuration(Constants.RT,cycleNumber);

			instr.printStagesInfo();
			rt.remove(0);
			if(head!=tail)
				full=false;
		}
		return counter;
	}

	public void lookup(ArrayList<Instruction> bundle)
	{
		for(Instruction instr:bundle)
		{
			if(instr.src1.isRob)
			{
				instr.src1.regReady = buffer[instr.src1.regNo].ready;
			}
			if(instr.src1.isRob)
			{
				instr.src1.regReady = buffer[instr.src1.regNo].ready;
			}
		}
	}

	public void resetHead()
	{
		buffer[head] = new RobEntry();
	}

	public void printRow(int rowNo)
	{
		System.out.println(rowNo+": dst"+buffer[rowNo].destReg+" rdy:"+buffer[rowNo].ready+" i:"+buffer[rowNo].instructionNo);
	}

}
import java.util.HashMap;
import java.util.ArrayList;

class SuperScalar
{
	int width;
	IssueQueue iq;
	Rob rob;
	int renameTable[];
	static int cycleNumber=0;
	ExecuteList ex;

	/*
	Data Structures for cycle phases
	*/

	ArrayList<Instruction> de, rn, rr, di, wb, rt;

	public SuperScalar(int width, int iqSize,int robSize)
	{
		this.width = width;
		this.iq = new IssueQueue(iqSize);
		this.rob = new Rob(robSize);
		ex = new ExecuteList(width*5);
		renameTable = new int[67];

		for(int i=0;i<renameTable.length;i++)
			renameTable[i]=-1;	
		wb = new ArrayList<Instruction>();
		rt = new ArrayList<Instruction>();
	}

	public boolean fetch(ArrayList<Instruction> bundle)
	{
		if(de==null && bundle!=null)
		{
			de = bundle;
			updateBundle(bundle, Constants.FE, Constants.FE);
			return true;
		}

		return false;
	}

	public boolean decode()
	{
		if(rn==null && de!=null)
		{
			rn = de;
			updateBundle(de, Constants.FE, Constants.DE);
			de=null;
			return true;
		}

		return false;
	}

	public boolean rename()
	{
		if( rn!=null )
		{
			if(rr==null && rob.robNotFull(rn))
			{
				rob.tagRegisters(rn,renameTable);
				modifySource(rn);
				updateBundle(rn, Constants.DE, Constants.RN);
				rr=rn;
				rn=null;
				return true;
			}
		}
		return false;
	}

	public boolean regRead()
	{
		if(rr!=null && di==null)
		{
			di = rr;
			updateBundle(rr, Constants.RN, Constants.RR);
			rr= null;
			return true;
		}
		return false;
	}

	public boolean dispatch()
	{
		if(di!=null)
		{
			if(iq.canInsertBundle(di))
			{
				iq.insertBundle(di,rob);
				updateBundle(di, Constants.RR, Constants.DI);
				return true;
			}
		}
		return false;
	}

	public boolean issue()
	{
		if( !iq.empty() && ex.notFull() )
		{
			int space = ex.space();
			space = (space>width)?width:space;
			ArrayList<Instruction> bundle = iq.selectBundle(space);
			ex.insertBundle(bundle);
			updateBundle(bundle, Constants.DI, Constants.IS);
			return true;
		}
		return false;
	}

	public boolean execute()
	{
		if(ex.notEmpty())
		{
			ArrayList<Instruction> finishedBundle = ex.runInstructions();
			updateStages(finishedBundle);
			
			for(Instruction instr:finishedBundle)			
				wb.add(instr);			

			return true;
		}
		return false;
	}

	public boolean writeback()
	{
		int index=0;
		while(index<wb.size())
		{
			Instruction instr = wb.get(index);
			rob.buffer[instr.dst.regNo].ready = true;
			if(rt.size()<width)
			{
				rt.add(instr);
				wb.remove(index);
			}
			else
				index++;

			if(index==wb.size())
				return true;
		}

		return false;
	}

	public boolean retire()
	{
		if(rob.head==rob.tail)			
			return false;

		rob.retire(width);
		return true;
	}

	public void nextCycle()
	{
		cycleNumber++;
	}

	public void updateBundle(ArrayList<Instruction> bundle, int prevCycle, int currCycle)
	{
		for(Instruction temp:bundle)
		{
			temp.updateEntryPoint(currCycle,cycleNumber);
			temp.updateDuration(prevCycle,cycleNumber);
		}
	}

	public void modifySource(ArrayList<Instruction> bundle)
	{
		for(Instruction instr:bundle)
		{			
			if(instr.src1.regNo>=0 && renameTable[instr.src1.regNo]!=-1)
			{
				instr.src1.regName = "rob"+renameTable[instr.src1.regNo];
				instr.src1.regNo = renameTable[instr.src1.regNo];
				instr.src1.isRob = true;
			}

			if(instr.src2.regNo>=0 && renameTable[instr.src2.regNo]!=-1)
			{
				instr.src2.regName = "rob"+renameTable[instr.src2.regNo];
				instr.src2.regNo = renameTable[instr.src2.regNo];
				instr.src2.isRob = true;
			}
		}
	}

	public void updateStages(ArrayList<Instruction> bundle)
	{
		for(Instruction instr:bundle)
		{
			Register dst = instr.dst;
			//Update IQ
			updateDS(iq.entries,dst);
			//Update DI
			updateDS(di,dst);
			//Update RR
			updateDS(rr,dst);
			//Way to update Duration
		//	instr.cycleDetails[Constants.EX][0] = instr.cycleDetails[Constants.IS][0] 
		//										+ instr.cycleDetails[Constants.IS][1];
		//	instr.cycleDetails[Constants.EX][1] = instr.
		}
	}

	public void updateDS(ArrayList<Instruction> entries, Register dst)
	{
		for(Instruction instr:entries)
		{
			if(instr.src1.regNo==dst.regNo && instr.src1.regName.equals(dst.regName))
				instr.src1.regReady = true;

			if(instr.src2.regNo==dst.regNo && instr.src2.regName.equals(dst.regName))
				instr.src2.regReady = true;
		}
	}
	
}
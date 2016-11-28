import java.util.HashMap;

class SuperScalar
{
	int width;
	IssueQueue iq;
	Rob rob;
	int renameTable[];
	static int cycleNumber=0;

	/*
	Data Structures for cycle phases
	*/

	ArrayList<Instruction> de, rn, rr, di, ex, wb, rt;

	public SuperScalar(int width, int iqSize,int robSize)
	{
		this.width = width;
		this.iq = new IssueQueue(iqSize);
		this.rob = new Rob(robSize);
		renameTable = new int[67];

		for(int i=0;i<renameTable.length;i++)
			renameTable[i]=-1;		
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
			updateBundle(bundle, Constants.FE, Constants.DE);
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
				updateBundle(bundle, Constants.DE, Constants.RN);
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
			updateBundle(bundle, Constants.RN, Constants.RR);
			rr= null;
			return true;
		}
		return false;
	}

	public boolean dispatch()
	{
		if(di!=null)
		{
			if(iq.canInsertBundle(bundle))
			{
				iq.insertBundle(bundle,rob);
				return true;
			}
		}
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
			if(instr.src1.regNo>=0 && renameTable[instr.src1.regNo]!=null)
			{
				instr.src1.regName = "rob"+renameTable[instr.src1.regNo];
				instr.src1.regNo = renameTable[instr.src1.regNo];
				instr.src1.isRob = true;
			}

			if(instr.src1.regNo>=0 && renameTable[instr.src2.regNo]!=null)
			{
				instr.src2.regName = "rob"+renameTable[instr.src2.regNo];
				instr.src2.regNo = renameTable[instr.src2.regNo];
				instr.src2.isRob = true;
			}
		}
	}
}
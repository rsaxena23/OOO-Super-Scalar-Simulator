import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Comparator;
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

	public SuperScalar(int width, int robSize,int iqSize)
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
		if(bundle!=null)
		{
			if(de==null ) {
				de = bundle;
				updateBundle(bundle, Constants.FE, Constants.DE);
				return true;
			}
			else
			{
				updateBundle(bundle, -1, Constants.FE);
			}
		}

		return false;
	}

	public boolean decode()
	{
		if(de!=null)
		{
			if(rn==null)
			{
				rn = de;
				updateBundle(de, Constants.DE, Constants.RN);
				de=null;
			}
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
				modifySource(rn);
				rob.tagRegisters(rn,renameTable);				
				updateBundle(rn, Constants.RN, Constants.RR);
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
			rob.lookup(rr);
			di = rr;
			updateBundle(rr, Constants.RR, Constants.DI);
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
			//	System.out.println("Cycle:!"+cycleNumber);
			//	di.get(0).printInfo();
				updateBundle(di, Constants.DI, Constants.IS);
				iq.insertBundle(di,rob);
				di=null;
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
//			System.out.println("Space:"+space+" , iqselectbatch:"+bundle.size()+" "+iq.entries.size());
			iq.printInfo();
			ex.insertBundle(bundle);
			updateBundle(bundle, Constants.IS, Constants.EX);
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
			//System.out.println((finishedBundle.size()>0)?finishedBundle.get(0).instructionNo:-1);
			for(Instruction instr:finishedBundle)			
				wb.add(instr);
			ex.printInfo();

			return true;
		}
		return false;
	}

	public boolean writeback()
	{
		int index=0;
		ArrayList<Instruction> tempBundle = new ArrayList<Instruction>();
		System.out.println("\nWriteBack:");
		while(index<wb.size())
		{
			Instruction instr = wb.get(index);
			instr.printInfo();
			rob.buffer[instr.dst.regNo].ready = true;
			if(rt.size()<width*5)
			{
				rt.add(instr);
				tempBundle.add(instr);
				wb.remove(index);
			}
			else
				index++;

			if(index==wb.size()) {
				updateBundle(tempBundle, Constants.WB,Constants.RT);
				return true;
			}
		}

		return false;
	}

	public boolean retire()
	{
		System.out.println("\n-------Cycle Number:"+cycleNumber+"-----------");
		if(rob.head==rob.tail || rt.size()==0)
			return false;
		Collections.sort(rt, instructionSort());
		//rt.get(0).printInfo();
		//rob.printRow(rob.head);
		rob.retire(width,rt,cycleNumber,renameTable);
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
			if(currCycle!=-1)
				temp.updateEntryPoint(currCycle,cycleNumber);

			if(prevCycle!=-1)
				temp.updateDuration(prevCycle,cycleNumber);
		}
	}

	public void modifySource(ArrayList<Instruction> bundle)
	{
		for(Instruction instr:bundle)
		{
			//System.out.println("Cycle:"+cycleNumber);
			//System.out.print("\nBefore RN:");
			//instr.printInfo();
			if(instr.src1.regNo>=0 && renameTable[instr.src1.regNo]!=-1)
			{
				instr.src1.regName = "rob"+renameTable[instr.src1.regNo];
				instr.src1.regNo = renameTable[instr.src1.regNo];
				instr.src1.isRob = true;
				instr.src1.regReady = rob.buffer[ instr.src1.regNo ].ready;
			}

			if(instr.src2.regNo>=0 && renameTable[instr.src2.regNo]!=-1)
			{
				instr.src2.regName = "rob"+renameTable[instr.src2.regNo];
				instr.src2.regNo = renameTable[instr.src2.regNo];
				instr.src2.isRob = true;
				instr.src2.regReady = rob.buffer[ instr.src2.regNo  ].ready;
			}
			//System.out.print("\nAfter RN:");
			//instr.printInfo();
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
			updateBundle(bundle,Constants.EX,Constants.WB);
		}
	}

	public void updateDS(ArrayList<Instruction> entries, Register dst)
	{
		if(entries==null)
			return;
		for(Instruction instr:entries)
		{
			if(instr.src1.regNo==dst.regNo && instr.src1.regName.equals(dst.regName))
				instr.src1.regReady = true;

			if(instr.src2.regNo==dst.regNo && instr.src2.regName.equals(dst.regName))
				instr.src2.regReady = true;
		}
	}

	public static Comparator<Instruction> instructionSort()
	{
		Comparator comp = new Comparator<Instruction>() {
			@Override
			public int compare(Instruction x, Instruction y) {
				if(x.instructionNo>y.instructionNo)
					return 1;
				else
					return -1;
			}
		};
		return comp;
	}

	public void printBundle()
	{
		
	}	
	
}

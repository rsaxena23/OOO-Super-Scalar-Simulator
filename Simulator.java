import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Simulator
{
	SuperScalar superScalar;
	String tracefile;

	public Simulator(SuperScalar superScalar, String tracefile)
	{
		this.superScalar = superScalar;
		this.tracefile = tracefile;
	}

	public void startSimulation()
	{
		try
		{
			BufferedReader br = new BufferedReader( new FileReader(tracefile));
			boolean simStatus = true, traceRead = true;
			ArrayList<Instruction> bundle = null;
			int counter = 0;
			boolean rt;
			while(simStatus)
			{
				superScalar.nextCycle();
				//System.out.println("");
				simStatus = superScalar.retire();
				//System.out.print(simStatus+":RT ");
				simStatus = simStatus | superScalar.writeback();
				//System.out.print(simStatus+":WB ");
				simStatus = simStatus | superScalar.execute();
				//System.out.print(simStatus+":ex ");
				simStatus = simStatus | superScalar.issue();
				//System.out.print(simStatus+":is ");
				simStatus = simStatus | superScalar.dispatch();
				//System.out.print(simStatus+":di ");
				simStatus = simStatus | superScalar.regRead();
				//System.out.print(simStatus+":rr ");
				simStatus = simStatus | superScalar.rename();
				//System.out.print(simStatus+":rn ");
				simStatus = simStatus | superScalar.decode();
				//System.out.print(simStatus+":de ");
								

				/*simStatus = superScalar.retire() | superScalar.writeback() | superScalar.execute()
						  | superScalar.issue() | superScalar.dispatch() | superScalar.regRead()
						  | superScalar.rename() | superScalar.decode();*/

				//System.out.println(simStatus+"");

				if(bundle==null && traceRead)
				{
					/*
					remember to consider end case whether null or 0 length Arraylist
					*/
					bundle = getBundle(br, superScalar.width);
					if(bundle.size()==0) {
						traceRead = false;
						bundle=null;
					}
					else
					{
						superScalar.updateBundle(bundle, -1, Constants.FE);
					}
				}

				boolean fetchStatus = superScalar.fetch(bundle);

				if(fetchStatus)
					bundle = null;

				simStatus= simStatus | fetchStatus;
				//System.out.print(simStatus+":fe ");
				//if(counter==80)
				//	break;
								
				counter++;
				//if(!traceRead)
				//	break;
			}
			superScalar.printStats(tracefile);

			//System.out.println("Number of cycles:"+superScalar.cycleNumber+" "+superScalar.ex.entries.size());

		}catch(Exception e)
		{
			System.out.println("Problem:"+e.getMessage());
			e.printStackTrace();
		}
	}

	public ArrayList<Instruction> getBundle(BufferedReader br, int width)
	{
		ArrayList<Instruction> bundle = new ArrayList<Instruction>();
		String line="";
		try
		{
			
			for(int i=0; i<width && (line=br.readLine())!=null; i++)
			{
				//System.out.println(line);
				String lineValues[] = line.split(" ");
				String pcValue = lineValues[0];
				int opType = Integer.parseInt(lineValues[1]);
				Register dst = new Register( "r"+lineValues[2], Integer.parseInt( lineValues[2] ) );
				Register src1 = new Register( "r"+lineValues[3], Integer.parseInt( lineValues[3] ) );
				Register src2 = new Register( "r"+lineValues[4], Integer.parseInt( lineValues[4] ) );
				bundle.add( new Instruction(pcValue, dst, src1, src2, opType) );
			}
			/*
			remember to consider end case whether null or 0 length Arraylist
			*/
		}
		catch(Exception e)
		{
			System.out.println("getBundle Problem:"+e.getMessage());

		}
		
		return bundle;

	}
}
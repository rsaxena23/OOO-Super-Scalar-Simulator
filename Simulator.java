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

	public void startSimulation(SuperScalar superScalar, String traceFile)
	{
		try
		{
			BufferedReader br = new BufferedReader( new FileReader(traceFile));
			boolean simStatus = true, traceRead = false;
			ArrayList<Instruction> bundle = null;

			while(simStatus)
			{
				/*
				superScalar.retire();
				superScalar.writeback();
				superScalar.execute();
				superScalar.issue();
				superScalar.dispatch();
				superScalar.regRead();
				superScalar.rename();
				superScalar.decode();
				*/
				superScalar.nextCycle();

				simStatus = superScalar.retire() | superScalar.writeback() | superScalar.execute()
						  | superScalar.issue() | superScalar.dispatch() | superScalar.regRead()
						  | superScalar.rename() | superScalar.decode();


				if(bundle==null && traceRead)
				{
					/*
					remember to consider end case whether null or 0 length Arraylist
					*/
					bundle = getBundle(br, superScalar.width);
					if(bundle==null)
						traceRead=false;
				}

				boolean fetchStatus = superScalar.fetch(bundle);

				if(fetchStatus)
					bundle = null;

				simStatus= simStatus | fetchStatus;


			}

		}catch(Exception e)
		{
			System.out.println("Problem:"+e.getMessage());
			e.printStackTrace();
		}
	}

	public ArrayList<Instruction> getBundle(BufferedReader br, int width)
	{
		ArrayList<Instruction> bundle = new ArrayList<Instruction>();
		String line;
		
		for(int i=0; i<width && (line=br.readLine())!=null; i++)
		{
			lineValues = line.split();
			String pcValue = lineValues[0];
			int opType = Integer.parseInt(lineValues[1]);
			Register dst = new Register( "r"+lineValues[2], Integer.parseInt( lineValues[2] ) );
			Register src1 = new Register( "r"+lineValues[3], Integer.parseInt( lineValues[3] ) );
			Register src2 = new Register( "r"+lineValues[4], Integer.parseInt( lineValues[4] ) );
			bundle.append( new Instruction(pcValue, dst, src1, src2, opType) );
		}
		/*
		remember to consider end case whether null or 0 length Arraylist
		*/
		return bundle;

	}
}
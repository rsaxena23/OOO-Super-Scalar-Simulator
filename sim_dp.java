import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


/*
SuperScalar, IQ, Rob

*/




public class sim_dp
{
		public void startSimulation(SuperScalar superScalar, String traceFile)
		{
			try
			{
				BufferedReader br = new BufferedReader( new FileReader(traceFile));
				String line;
				while((line = br.readLine())!=null)
				{

				}
			}catch(Exception e)
			{
				System.out.println("Problem:"+e.getMessage());
				e.printStackTrace();
			}

		}


		public static void main(String args[])
		{
			int robSize, iqSize, width, cacheSize,p;
			String traceFile="";

			if(args.length<6)
			{
				System.out.println("Not enough arguments");
				System.exit(0);
			}

			robSize = Integer.parseInt(args[0]);
			iqSize = Integer.parseInt(args[1]);
			width = Integer.parseInt(args[2]);
			cacheSize = Integer.parseInt(args[3]);
			p = Integer.parseInt(args[4]);
			traceFile = args[5];
			
			SuperScalar superScalar = new SuperScalar(width, robSize, iqSize);
			new sim_dp().startSimulation(superScalar,traceFile);
			
		}
}
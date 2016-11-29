/*
SuperScalar, IQ, Rob

*/

public class sim_ds
{
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
			
			SuperScalar superScalar = new SuperScalar(width, robSize, iqSize, cacheSize, p);
			new Simulator(superScalar,traceFile).startSimulation();			
		}
}
class RobEntry
{
	int value;
	String destReg;
	boolean ready;
}

class Rob
{

	RobEntry buffer[];
	public Rob(int robSize)
	{
		this.buffer = new RobEntry[robSize];
	}
}
class RobEntry
{
	boolean valueSet;
	String destReg;
	boolean ready;
}

class Rob
{
	int head = 3;
	int tail = 3;
	RobEntry buffer[];
	public Rob(int robSize)
	{
		this.buffer = new RobEntry[robSize];
	}
}
class SuperScalar
{
	int width;
	IssueQueue iq;
	Rob rob;
	HashMap renameTable;
	public SuperScalar(int width, int iqSize,int robSize)
	{
		this.width = width;
		this.iq = new IssueQueue(iqSize);
		this.rob = new Rob(robSize);
		renameTable = new HashMap();
	}
}
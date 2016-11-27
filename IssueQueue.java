class IQEntry
{

}

public class IssueQueue
{
	IQEntry entries[];
	public IssueQueue(int size)
	{
		entries = new IQEntry[size];
	}
}
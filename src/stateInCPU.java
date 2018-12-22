
public class stateInCPU {
	public String processName;
	public int stopAt;
	public stateInCPU(String n,int t)
	{
		processName = n;
		stopAt = t;
	}
	public String toString()
	{
		return "<- "+ processName + " ->" + stopAt;
	}
}

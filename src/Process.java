
public class Process {
	public String name;
	public int arrivalTime, burstTime, turnAroundTime, waitingTime;
	private int doneTime=0;
	public Process(String n, int at,int burst)
	{
		name = n;
		arrivalTime = at;
		burstTime = burst;
		doneTime = 0;
	}
	public String toString()
	{
		return "\nProcess Name: "+ name + "\nWaiting Time: " + waitingTime + "\nTurnaroundTime: " + turnAroundTime +"\n";
	}
	public void doItForOneTime()
	{
		if(doneTime >= burstTime)
			return;
		doneTime++;
		
	}
	
	public Boolean isFinished()
	{
		return doneTime >= burstTime;
	}
	public void finishedAt(int time)
	{
		turnAroundTime = time - arrivalTime;
		waitingTime = turnAroundTime - burstTime;
	}
	
}

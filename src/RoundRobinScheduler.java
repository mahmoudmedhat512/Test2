import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RoundRobinScheduler {
	public static int  quantum, contextSwitching;
	public static ArrayList<Process> toComeProcesses = new ArrayList<>();
	public static ArrayList<Process> allProcesses = new ArrayList<>();
	public static Queue<Process> requestQueue = new LinkedList<>(); 
	public static ArrayList<stateInCPU> ganttChart = new ArrayList<>();
	private static int firstAT=0;
	
	
	public static void main(String[] args)
	{
		readInput();
		simulate();
		System.out.println("\n\nProcesses execution order:");
		System.out.println(ganttToString());
		System.out.println("\nAverage Waiting Time: " + averageWaitingTime());
		System.out.println("Average Turnaround Time: " + averageTurnaroundTime());
		System.out.print("\nWaiting Time and Turnaround Time for each process:");
		System.out.println(allProcessesToString());
		
	}
	
	public static void simulate()
	{
		int time = firstAT;
		
		// load the first processes which arrive at first arrival time
		whoComesNow(time);
		Process runningProcess = requestQueue.remove(), oldProcess=null;
		
		
		while(!requestQueue.isEmpty() || !toComeProcesses.isEmpty() || runningProcess!= null)
		{
			
			// do this process until finished or quantum reached
			for(int i=0;i<quantum;i++)
			{
				time++;
				// which process arrives at this time
				whoComesNow(time);
				runningProcess.doItForOneTime();
				if(runningProcess.isFinished())
				{
					runningProcess.finishedAt(time);
					break;
				}
			}

			// save this state in the gantt chart
			saveState(runningProcess.name, time);
	
			// saves the old process to check for the context switching
			if(!runningProcess.isFinished())
				oldProcess = runningProcess;
			else
				oldProcess = null;

			// old process not finished then add it to the requestQueue
			if(oldProcess != null)
				requestQueue.add(oldProcess);
			
			// to be sure that there's a process in the request queue at this time
			while(requestQueue.isEmpty() && !toComeProcesses.isEmpty()) 
			{
				time++;
				whoComesNow(time);
			}
			if(requestQueue.isEmpty())
				break;
			// which process has the right to enter ? FIFO
			runningProcess = requestQueue.remove();

			// if it's not the same as the last one then there's a context switching time here
			if(oldProcess != runningProcess)
			{
				// add the context switching time
				time+=contextSwitching;
				
				
				// save the state of the context switching
				saveState("Context switching", time);
				
			}
		}
	}
	
	public static void whoComesNow(int time)
	{
		while(!toComeProcesses.isEmpty() && toComeProcesses.get(0).arrivalTime <= time)
		{
			requestQueue.add(toComeProcesses.get(0));
			toComeProcesses.remove(0);
		}
	}
	
	public static void saveState(String name, int time)
	{
		ganttChart.add(new stateInCPU(name, time));
	}
	
	public static void readInput()
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Number Of Processes: ");
		int n = sc.nextInt();
		System.out.print("Round robin Time Quantum: ");
		quantum  = sc.nextInt();
		System.out.print("Context switching: ");
		contextSwitching  = sc.nextInt();
		String name;
		int AT, BT;
		Process pTemp =null ;
		while(n-- > 0)
		{
			System.out.println();
			System.out.print("Process Name: ");
			name = sc.next();
			System.out.print("Process Arrivale Time: ");
			AT = sc.nextInt();
			System.out.print("Process Burst Time: ");
			BT = sc.nextInt();
			pTemp = new Process(name, AT, BT);
			toComeProcesses.add(pTemp);
			allProcesses.add(pTemp);
		}
		sortComeProcessesOnAT();
		firstAT = toComeProcesses.get(0).arrivalTime;
		sc.close();
	}
	
	public static void sortComeProcessesOnAT()
	{
        int n = toComeProcesses.size(); 
        for (int i = 0; i < n-1; i++) 
            for (int j = 0; j < n-i-1; j++) 
                if (toComeProcesses.get(i).arrivalTime > toComeProcesses.get(i+1).arrivalTime) 
                { 
                    Process temp = toComeProcesses.get(i); 
                    toComeProcesses.set(i,toComeProcesses.get(i+1));
                    toComeProcesses.set(i+1,temp);
                } 
	}
	
	public static String ganttToString()
	{
		String chart="";
		chart += firstAT;
		for(stateInCPU state : ganttChart)
			chart += state.toString();
		return chart;
	}
	public static String allProcessesToString()
	{
		String result ="";
		for(Process p : allProcesses)
			result += p;
		return result;
	}
	
	public static double averageWaitingTime()
	{
		double result=0;
		for(Process p : allProcesses)
			result += p.waitingTime;
		return result/allProcesses.size();
	}
	public static double averageTurnaroundTime()
	{
		double result=0;
		for(Process p : allProcesses)
			result += p.turnAroundTime;
		return result/allProcesses.size();
	}
}

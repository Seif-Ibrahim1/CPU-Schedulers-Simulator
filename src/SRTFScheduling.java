import java.util.ArrayList;

/**
 * SRTFScheduling class implementing the Shortest Remaining Time First scheduling algorithm (preemptive algorithm).
 */
public class SRTFScheduling extends Scheduler implements StarvationHandler{
    SRTFScheduling(ArrayList<Process> processes) {
        super(processes);
    }

    public boolean checkForStravation(Process process) {
        return false;
    }

    public void run() {
        int currentTime = 0;
        int completedProcesses = 0;
        int shortestRT = Integer.MAX_VALUE; //2^31 - 1
        int shortestRTIndx = 0;
        int[] remainingTime = new int[processes.size()];
        ArrayList<String> executionOrder = new ArrayList<>();

        //fill the remainingTime array with burst time
        for (int j = 0; j < processes.size(); j++) {
            remainingTime[j] = processes.get(j).getBurstTime();
        }

        while(completedProcesses < processes.size()){
            for(int i = 0; i < processes.size(); i++) {
                //find the process with the shortest remaining time
                if (processes.get(i).getArrivalTime() <= currentTime && remainingTime[i] < shortestRT && remainingTime[i] > 0) {
                    shortestRT = remainingTime[i];
                    shortestRTIndx = i;
                }
            }
            //need to increase the current
            if(shortestRT == Integer.MAX_VALUE){
                currentTime++;
                continue;
            }

            executionOrder.add(processes.get(shortestRTIndx).getName());

            //reduce the remaining time of the process by 1
            remainingTime[shortestRTIndx]--;

            //if the remaining time of the process is 0
            if(remainingTime[shortestRTIndx] == 0) {
                completedProcesses++;

                //update the shortestRT value
                shortestRT = Integer.MAX_VALUE;

                //completion time of current process = current time + 1 //+1 as finish time begins from 0
                processes.get(shortestRTIndx).setFinishedTime(currentTime + 1);

                //calculate turnaround time for each completed process = finish time - arrival time
                processes.get(shortestRTIndx).setTurnaroundTime(processes.get(shortestRTIndx).calculateTurnaroundTime());

                //calculate waiting time for each completed process = completion time - arrival time - burst time
                processes.get(shortestRTIndx).setWaitingTime(processes.get(shortestRTIndx).calculateWaitingTime());

                //if the waiting time is negative, set it to 0
                if(processes.get(shortestRTIndx).getWaitingTime() < 0){
                    processes.get(shortestRTIndx).setWaitingTime(0);
                }
            }
            currentTime++;
        }
        printExecutionOrder(executionOrder);
    }

    /**
     * Prints the execution order of the processes
     * @param executionOrder the execution order list of the processes to be printed
     */
    public void printExecutionOrder(ArrayList<String> executionOrder){
        System.out.println("Execution Order: ");
        for (int i = 0 ; i < executionOrder.size() ; i++){
            //print the distinct process names
            if(i == 0){
                System.out.print(executionOrder.get(i));
            }
            else if(!executionOrder.get(i).equals(executionOrder.get(i-1))){
                System.out.print(" -> " + executionOrder.get(i));
            }
        }
        System.out.println();
    }
}

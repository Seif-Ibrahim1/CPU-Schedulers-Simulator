import java.util.ArrayList;

/**
 * SRTFScheduling class implementing the Shortest Remaining Time First scheduling algorithm (preemptive algorithm).
 */
public class SRTFScheduling extends Scheduler implements StarvationHandler {
    SRTFScheduling(ArrayList<Process> processes) {
        super(processes);
    }

    public boolean checkForStravation(Process process) {
        if(process.getWaitingTime() > 15){
            return true;
        }
        return false;
    }

    public void run() {
        int currentTime = 0;
        int completedProcesses = 0;
        int shortestRT = Integer.MAX_VALUE; //2^31 - 1
        int shortestRTIndx = -1;
        ArrayList<String> executionOrder = new ArrayList<>();

        //fill the remainingTime array with burst time
        for (int j = 0; j < processes.size(); j++) {
            processes.get(j).setRemainingBurstTime(processes.get(j).getBurstTime());
        }

        while(completedProcesses < processes.size()){
            for(int i = 0; i < processes.size(); i++) {

                //check for starvation
                if(checkForStravation(processes.get(i))){
                    executionOrder.add(processes.get(i).getName());

                    System.out.println("Process name: " + processes.get(i).getName() + " : " + currentTime + " -> " + (currentTime + processes.get(i).getRemainingBurstTime()));
                    currentTime += processes.get(i).getRemainingBurstTime();
                    processes.get(i).setFinishedTime(currentTime);
                    processes.get(i).setTurnaroundTime(processes.get(i).getFinishedTime() - processes.get(i).getArrivalTime());
                    int x = processes.get(i).getTurnaroundTime() - processes.get(i).getRemainingBurstTime();
                    processes.get(i).setWaitingTime(processes.get(i).getTurnaroundTime() - processes.get(i).getRemainingBurstTime());
                    processes.get(i).setRemainingBurstTime(processes.get(i).getRemainingBurstTime() - processes.get(i).getRemainingBurstTime());
                    if(processes.get(i).getRemainingBurstTime() < 0){
                        processes.get(i).setRemainingBurstTime(0);
                    }

                    completedProcesses++;
                    //update the waiting time for all the processes except this one
                    for(int j = 0; j < processes.size(); j++) {
                        if (j != i && processes.get(j).getArrivalTime() <= currentTime && processes.get(i).getRemainingBurstTime() > 0) {
                            processes.get(j).setWaitingTime(processes.get(j).getWaitingTime() + processes.get(i).getRemainingBurstTime());
                        }
                    }
                    continue;
                }

                //find the process with the shortest remaining time
                if (processes.get(i).getArrivalTime() <= currentTime && processes.get(i).getRemainingBurstTime() < shortestRT &&processes.get(i).getRemainingBurstTime() > 0) {
                    shortestRT = processes.get(i).getRemainingBurstTime();
                    shortestRTIndx = i;
                }
            }
            //need to increase the current
            if(shortestRT == Integer.MAX_VALUE){
                currentTime++;
                continue;
            }

            executionOrder.add(processes.get(shortestRTIndx).getName());

            //reduce the remaining time of the process by 1\
            processes.get(shortestRTIndx).setRemainingBurstTime(processes.get(shortestRTIndx).getRemainingBurstTime() - 1);
            //update waiting time for all the processes except the one with the shortest remaining time
            for(int i = 0; i < processes.size(); i++) {
                if (i != shortestRTIndx && processes.get(i).getArrivalTime() <= currentTime && processes.get(i).getRemainingBurstTime() > 0) {
                    processes.get(i).setWaitingTime(processes.get(i).getWaitingTime() + 1);
                }
            }

            System.out.println("Process name: " + processes.get(shortestRTIndx).getName() + " : " + currentTime + " -> " + (currentTime + 1));
            currentTime++;


            //if the remaining time of the process is 0
            if(processes.get(shortestRTIndx).getRemainingBurstTime() == 0) {
                completedProcesses++;

                //update the shortestRT value
                shortestRT = Integer.MAX_VALUE;

                //completion time of current process = current time
                processes.get(shortestRTIndx).setFinishedTime(currentTime);

                //calculate turnaround time for each completed process = finish time - arrival time
                processes.get(shortestRTIndx).setTurnaroundTime(processes.get(shortestRTIndx).getFinishedTime() - processes.get(shortestRTIndx).getArrivalTime());

                //calculate waiting time for each completed process = completion time - arrival time - burst time
                processes.get(shortestRTIndx).setWaitingTime(processes.get(shortestRTIndx).getTurnaroundTime() - processes.get(shortestRTIndx).getBurstTime());

                //if the waiting time is negative, set it to 0
                if(processes.get(shortestRTIndx).getWaitingTime() < 0){
                    processes.get(shortestRTIndx).setWaitingTime(0);
                }
            }
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
            //print all processes
            System.out.print(executionOrder.get(i) + " ");
        }
        System.out.println();
    }
}
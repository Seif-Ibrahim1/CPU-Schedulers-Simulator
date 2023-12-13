import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SRTFScheduling class implementing the Shortest Remaining Time First scheduling algorithm (preemptive algorithm).
 */
public class SRTFScheduling extends Scheduler implements StarvationHandler {
    SRTFScheduling(ArrayList<Process> processes) {
        super(processes);
    }

    /**
     * Checks if the process is starved or not.
     * @param process the process to be checked
     * @return true if the process is starved, false otherwise
     */
    public boolean checkForStravation(Process process) {
        return process.getWaitingTime() > 15;
    }


    public void run() {
        int currentTime = 0;
        int completedProcesses = 0;
        int shortestRT = Integer.MAX_VALUE; //2^31 - 1
        int shortestRTIndx = -1;
        ArrayList<String> executionOrder = new ArrayList<>();

        //fill the remainingTime array with burst time
        for (Process process : processes) {
            process.setRemainingBurstTime(process.getBurstTime());
        }

        while(completedProcesses < processes.size()){
            for(int i = 0; i < processes.size(); i++) {

                //check for starvation
                if(checkForStravation(processes.get(i))){
                    executionOrder.add(processes.get(i).getName());

                    //set the start and finish time of the process into the time list
                    processes.get(i).setTime(List.of(Map.entry(currentTime, currentTime + processes.get(i).getRemainingBurstTime())));

//                    System.out.println("Process name: " + processes.get(i).getName() + " : " + currentTime + " -> " + (currentTime + processes.get(i).getRemainingBurstTime()));
                    currentTime += processes.get(i).getRemainingBurstTime();
                    processes.get(i).setFinishedTime(currentTime);
                    processes.get(i).setTurnaroundTime(processes.get(i).getFinishedTime() - processes.get(i).getArrivalTime());
                    processes.get(i).setWaitingTime(processes.get(i).getTurnaroundTime() - processes.get(i).getRemainingBurstTime());

                    processes.get(i).setRemainingBurstTime(0);
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
                if (processes.get(i).getArrivalTime() <= currentTime && processes.get(i).getRemainingBurstTime() < shortestRT && processes.get(i).getRemainingBurstTime() > 0) {
                    shortestRT = processes.get(i).getRemainingBurstTime();
                    shortestRTIndx = i;
                }
            }
            //if there is no process with the shortest remaining time
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

//            System.out.println("Process name: " + processes.get(shortestRTIndx).getName() + " : " + currentTime + " -> " + (currentTime + 1));

            //set the start and finish time of the process into the time list
            List<Map.Entry<Integer, Integer>> timeList = processes.get(shortestRTIndx).getTime();
//            timeList.add(Map.entry(currentTime, currentTime + 1));
            if(!timeList.isEmpty()){
                Map.Entry<Integer, Integer> lastEntry = timeList.get(timeList.size() - 1);
                if(lastEntry.getValue() == currentTime){
                    timeList.remove(timeList.size() - 1);
                    timeList.add(Map.entry(lastEntry.getKey(), lastEntry.getValue() + 1));
                }
                else{
                    timeList.add(Map.entry(currentTime, currentTime + 1));
                }
            }
            else{
                timeList.add(Map.entry(currentTime, currentTime + 1));
            }


            //increment the current time
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
            //print the distinct processes
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
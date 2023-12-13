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
        int[] remainingTime = new int[processes.size()];
        ArrayList<String> executionOrder = new ArrayList<>();

        //fill the remainingTime array with burst time
        for (int j = 0; j < processes.size(); j++) {
            remainingTime[j] = processes.get(j).getBurstTime();
        }

        while(completedProcesses < processes.size()){
            for(int i = 0; i < processes.size(); i++) {

                //check for starvation
                if(checkForStravation(processes.get(i))){
                    executionOrder.add(processes.get(i).getName());

                    System.out.println("Process name: " + processes.get(i).getName() + " : " + currentTime + " -> " + (currentTime + remainingTime[i]));
                    currentTime += remainingTime[i];
                    processes.get(i).setFinishedTime(currentTime);
                    processes.get(i).setTurnaroundTime(processes.get(i).getFinishedTime() - processes.get(i).getArrivalTime());
                    int x = processes.get(i).getTurnaroundTime() - remainingTime[i];
                    processes.get(i).setWaitingTime(processes.get(i).getTurnaroundTime() - remainingTime[i]);
                    remainingTime[i] -= remainingTime[i];
                    if(remainingTime[i] < 0){
                        remainingTime[i] = 0;
                    }

                    completedProcesses++;
                    //update the waiting time for all the processes except this one
                    for(int j = 0; j < processes.size(); j++) {
                        if (j != i && processes.get(j).getArrivalTime() <= currentTime && remainingTime[j] > 0) {
                            processes.get(j).setWaitingTime(processes.get(j).getWaitingTime() + remainingTime[i]);
                        }
                    }
                    continue;
                }

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
            //update waiting time for all the processes except the one with the shortest remaining time
            for(int i = 0; i < processes.size(); i++) {
                if (i != shortestRTIndx && processes.get(i).getArrivalTime() <= currentTime && remainingTime[i] > 0) {
                    processes.get(i).setWaitingTime(processes.get(i).getWaitingTime() + 1);
                }
            }

            System.out.println("Process name: " + processes.get(shortestRTIndx).getName() + " : " + currentTime + " -> " + (currentTime + 1));
            currentTime++;


            //if the remaining time of the process is 0
            if(remainingTime[shortestRTIndx] == 0) {
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
/*Process name: p1 : 0 -> 1
Process name: p1 : 1 -> 2
Process name: p1 : 2 -> 3
Process name: p1 : 3 -> 4
Process name: p3 : 4 -> 5
Process name: p3 : 5 -> 6
Process name: p4 : 6 -> 7
Process name: p4 : 7 -> 8
Process name: p1 : 8 -> 9
Process name: p1 : 9 -> 10
Process name: p1 : 10 -> 11
Process name: p6 : 11 -> 12
Process name: p6 : 12 -> 13
Process name: p6 : 13 -> 14
Process name: p5 : 14 -> 15
Process name: p5 : 15 -> 16
Process name: p5 : 16 -> 17
Process name: p5 : 17 -> 18
Process name: p2 : 18 -> 31
Process name: p5 : 31 -> 32
==========================================================================
Process p1 turnaround time: 11
Process p2 turnaround time: 29
Process p3 turnaround time: 2
Process p4 turnaround time: 3
Process p5 turnaround time: 25
Process p6 turnaround time: 5
==========================================================================
Process p1 waiting time: 4
Process p2 waiting time: 16
Process p3 waiting time: 0
Process p4 waiting time: 1
Process p5 waiting time: 20
Process p6 waiting time: 2
==========================================================================
Average Waiting Time : 7.166666666666667
==========================================================================
Average Turnaround Time : 12.5

Process p1 waiting time: 4
Process p2 waiting time: 16
Process p3 waiting time: 0
Process p4 waiting time: 1
Process p5 waiting time: 20
Process p6 waiting time: 2

Process p1 turnaround time: 11
Process p2 turnaround time: 29
Process p3 turnaround time: 2
Process p4 turnaround time: 3
Process p5 turnaround time: 25
Process p6 turnaround time: 5

Average Turnaround Time = 12.5
Average Waiting Time = 7.166666666666667
*/
/////////////////////
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//public class SRTFScheduling extends Scheduler implements StarvationHandler {
//
//     SRTFScheduling(ArrayList<Process> processes) {
//         super(processes);
//     }
//
//     @Override
//     public boolean checkForStravation(Process process) {
//        return process.getWaitingTime() > 15;
//     }
//
//     @Override
//     public void run() {
//        ArrayList<Process> completedProcesses = new ArrayList<>();
//         ArrayList<Process> remainingProcesses = new ArrayList<>(processes);
//         int currentTime = 0;
//
//
//         while (completedProcesses.size() != processes.size()) {
//             boolean isStravation = false;
//             ArrayList<Process> arrivalProcesses = new ArrayList<>();
//             for (Process p : remainingProcesses) {
//                if (p.getArrivalTime() <= currentTime && p.getRemainingBurstTime() > 0) {
//                   arrivalProcesses.add(p);
//                 }
//             }
//
//             for (Process p : arrivalProcesses) {
//                 if (checkForStravation(p)) {
//                     p.setFinishedTime(currentTime + p.getRemainingBurstTime());
//                     p.setTurnaroundTime(p.getFinishedTime() - p.getArrivalTime());
//                    p.setWaitingTime(p.getTurnaroundTime() - p.getRemainingBurstTime());
//
//                     p.setRemainingBurstTime(0);
//                     completedProcesses.add(p);
//                     remainingProcesses.remove(p);
//                     arrivalProcesses.remove(p);
//                     System.out.println("Process name: " + p.getName() + " : " + currentTime + " -> " + p.getFinishedTime());
//                   currentTime = p.getFinishedTime();
//                     for(Process p1 : remainingProcesses){
//                         if(p1 != p && p1.getArrivalTime() <= currentTime){
//                             p1.setWaitingTime(p1.getWaitingTime() + p.getRemainingBurstTime());
//                         }
//                     }
//                     isStravation = true;
//                     break;
//                 }
//            }
//
//             if (!arrivalProcesses.isEmpty() && !isStravation) {
//                 Collections.sort(arrivalProcesses, Comparator.comparingInt(Process::getRemainingBurstTime));
//                 Process process = arrivalProcesses.get(0);
//                 process.setRemainingBurstTime(process.getRemainingBurstTime() - 1);
//                 System.out.println("Process name: " + process.getName() + " : " + currentTime + " -> " + (currentTime + 1));
//
//                 if (process.getRemainingBurstTime() == 0) {
//                     process.setFinishedTime(currentTime + 1);
//                     process.setTurnaroundTime(process.getFinishedTime() - process.getArrivalTime());
//                     process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
//                     completedProcesses.add(process);
//                   remainingProcesses.remove(process);
//                 }
//                 for(Process p : remainingProcesses){
//                     if(p != process && p.getArrivalTime() <= currentTime){
//                         p.setWaitingTime(p.getWaitingTime() + 1);
//                     }
//               }
//                 currentTime++;
//             }
//             if(arrivalProcesses.isEmpty())
//                 currentTime++;
//
//
//         }
//         // Display Average Waiting Time and Average Turnaround Time after all processes are executed
//         System.out.println("==========================================================================");
//         printTurnAroundTime();
//
//         System.out.println("==========================================================================");
//         printWaitingTime();
//
//         System.out.println("==========================================================================");
//        System.out.println("Average Waiting Time : " + getAverageWaitingTime());
//         System.out.println("==========================================================================");
//         System.out.println("Average Turnaround Time : " + getAverageTurnAroundTime());
//     }
// }

////////////////////
//if there is a starvation, execute the process
//                if (checkForStravation(processes.get(i)) && remainingTime[i] > 0) {
//                    executionOrder.add(processes.get(i).getName());
//                    remainingTime[i] -= processes.get(i).getBurstTime();
//                    currentTime += processes.get(i).getBurstTime();
//                    processes.get(i).setFinishedTime(currentTime +1);
//                    processes.get(i).setTurnaroundTime(processes.get(i).calculateTurnaroundTime());
//                    processes.get(i).setWaitingTime(processes.get(i).calculateWaitingTime());
//                    completedProcesses++;
//                }
//
//
//        int[] waiting = new int[processes.size()];
//        for (int i = 0; i < processes.size(); i++) {
//            waiting[i] = 0;
//        }
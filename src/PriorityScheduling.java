import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PriorityScheduling extends Scheduler implements StarvationHandler {

    private final ArrayList<Process> ReadyQueue ;
    Process currentProcess;

    //==================================================================================================================

    /**
     * constructor
     * @param processes
     */
    public PriorityScheduling(ArrayList<Process> processes) {
        super(processes);
        ReadyQueue = new ArrayList<>();
    }

    //==================================================================================================================

    /**
     * check for starvation
     * @param process
     * @return
     */
    public boolean checkForStravation(Process process) {return false;}

    //==================================================================================================================


    /**
     * get the index of the process with the lowest priority( the highest priority value) to increase its priority( decrease its priority value)
     * if there are two processes with the same priority
     * the one with the earliest arrival time will be chosen
     * if there are two processes with the same priority and arrival time
     * the one with the shortest burst time will be chosen
     * @return
     */
    public int getLowestPriority(){

        Process minPriority = ReadyQueue.get(0);
        int min = 0;

         for (int i = 1 ; i<ReadyQueue.size() ; i++){

             if (ReadyQueue.get(i).getPriority() > minPriority.getPriority()){// lower priority

                 minPriority = ReadyQueue.get(i);
                 min = i;

             }else if(ReadyQueue.get(i).getPriority() == minPriority.getPriority()) { // same priority

                  // check for arrival time
                  if(ReadyQueue.get(i).getArrivalTime() < minPriority.getArrivalTime()){// earlier arrival time

                      minPriority = ReadyQueue.get(i);
                      min = i;

                  }else if (ReadyQueue.get(i).getArrivalTime() == minPriority.getArrivalTime()) { // same arrival time

                      // check for burst time
                      if(ReadyQueue.get(i).getBurstTime() < minPriority.getBurstTime()){// shorter burst time

                          minPriority = ReadyQueue.get(i);
                          min = i;
                      }
                  }
              }
         }
            return min;
    }

    //==================================================================================================================

    /**
     * get the index of the process with the highest priority(the least priority value) to be executed
     * if there are two processes with the same priority
     * the one with the earliest arrival time will be chosen
     * if there are two processes with the same priority and arrival time
     * the one with the shortest burst time will be chosen
     * @return
     */
    public int getHighestPriority(){

        Process maxPriority = ReadyQueue.get(0);
        int max = 0;

        for (int i = 1 ; i<ReadyQueue.size() ; i++){

            if (ReadyQueue.get(i).getPriority() < maxPriority.getPriority()){ // higher priority

                maxPriority = ReadyQueue.get(i);
                max = i;

            }else if(ReadyQueue.get(i).getPriority() == maxPriority.getPriority()) { // same priority

                // check for arrival time
                if(ReadyQueue.get(i).getArrivalTime() < maxPriority.getArrivalTime()){ // earlier arrival time

                    maxPriority = ReadyQueue.get(i);
                    max = i;

                }else if (ReadyQueue.get(i).getArrivalTime() == maxPriority.getArrivalTime()) { // same arrival time

                    // check for burst time
                    if(ReadyQueue.get(i).getBurstTime() < maxPriority.getBurstTime()){ // shorter burst time

                        maxPriority = ReadyQueue.get(i);
                        max = i;
                    }
                }
            }
        }
        return max;
    }

    //==================================================================================================================

    /**
     * run the processes
     */

    public void run() {

        int currentTime = 0;

        /**
         * check if there are no processes to schedule
         */
        if (processes.isEmpty()) {
            System.out.println("No processes to schedule");
            return;
        }
//        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int i = 0;
        System.out.println("Process"+'\t'+'\t'+"Arrival Time"+'\t'+'\t'+"Burst Time"+'\t'+'\t'+"OldPriority"+'\t'+'\t'+"NewPriority"+
                '\t'+'\t'+ "Start Time"+'\t'+'\t'+"Finish Time"+'\t'+'\t'+"Turnaround Time"+'\t'+'\t'+"Waiting Time");


        /**
         * loop until all processes are executed
         * or until the ready queue is empty
         */
        while (i < processes.size() || !ReadyQueue.isEmpty()) {

            /**
             * add processes to the ready queue if their arrival time is less than or equal to the current time
             */
            while (i < processes.size() && processes.get(i).getArrivalTime() <= currentTime) {
                ReadyQueue.add(processes.get(i));
                i++;
            }

            /**
             * check if the ready queue is empty
             */
           if (!ReadyQueue.isEmpty()) {

               /**
                * handle starvation
                */

                  // get the index of the process with the lowest priority to increase its priority (decrease its priority value)

                   int low = getLowestPriority();
                   ReadyQueue.get(low).setPriority(ReadyQueue.get(low).getPriority()-1);


                   // get the index of the process with the highest priority to be executed

                   int max=getHighestPriority();

                   currentProcess=ReadyQueue.get(max);

                   ReadyQueue.remove(currentProcess);

                   // set the start time, finish time, turnaround time and waiting time for the current process

                   currentProcess.setStartTime(currentTime);

                   currentProcess.setFinishedTime(currentTime + currentProcess.getBurstTime());

                   currentProcess.setTime(List.of(Map.entry(currentProcess.getStartTime(), currentProcess.getFinishedTime())));

                   currentProcess.setTurnaroundTime(currentProcess.getFinishedTime() - currentProcess.getArrivalTime());

                   currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

                   // display the current process

                   System.out.println(" "+currentProcess.getName()+'\t'+'\t'+'\t'+'\t'+currentProcess.getArrivalTime()+'\t'+'\t'+'\t'+'\t'+'\t'+currentProcess.getBurstTime()+'\t'+'\t'+'\t'+'\t'+currentProcess.getOldPriority()+'\t'+'\t'+'\t'+'\t'+currentProcess.getPriority()+'\t'+'\t'+'\t'+'\t'+currentProcess.getStartTime()+'\t'+'\t'+'\t'+'\t'+currentProcess.getFinishedTime()+'\t'+'\t'+'\t'+'\t'+" "+currentProcess.getTurnaroundTime()+'\t'+'\t'+'\t'+'\t'+'\t'+currentProcess.getWaitingTime());

                   // update the current time

                   currentTime = currentProcess.getFinishedTime();

                   /**
                    * add processes to the ready queue if their arrival time is less than or equal to the current time
                    */
                   while (i < processes.size() && processes.get(i).getArrivalTime() <= currentTime) {
                        ReadyQueue.add(processes.get(i));
                        i++;
                    }
            } else { // if the ready queue is empty
                currentTime++;
            }
        }

        // Display Average Waiting Time and Average Turnaround Time after all processes are executed

        System.out.println("=".repeat(145));
        System.out.println("Average Turnaround Time : " + getAverageTurnAroundTime());
        System.out.println("=".repeat(145));
        System.out.println("Average Waiting Time : " + getAverageWaitingTime());
        System.out.println("=".repeat(145));
    }
}


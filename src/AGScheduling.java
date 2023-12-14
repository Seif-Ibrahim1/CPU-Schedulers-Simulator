import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  AGScheduling class implementing the AG scheduling algorithm.
 */
public class AGScheduling extends Scheduler {
    private int RRTimeQuantum;
    private HashMap<Integer, ArrayList<Integer>> quantumHistory;
    private HashMap<Integer, Integer> currentTimeQuantams; 
    private ArrayList<Process> reeadyQueue;
    private HashMap<Integer, Integer> AGFactors;

    private ArrayList<Process> dieList;

    //==================================================================================================================

    /**
     *
     * constructor for AGScheduling class.
     *
     * @param processes       ArrayList of processes to be scheduled.
     * @param RRTimeQuantum   static time to execute.
     */
    public AGScheduling(ArrayList<Process> processes, int RRTimeQuantum)
    {
        super(processes);
        this.RRTimeQuantum = RRTimeQuantum;

        // for each process store the updated quantum time in quantumHistory
        // each process starts with the input quantum time
        quantumHistory = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < processes.size(); i++)
        {
            quantumHistory.put(processes.get(i).getId(), new ArrayList<Integer>());
            quantumHistory.get(processes.get(i).getId()).add(RRTimeQuantum);
        }

        // store teh current quantum time for each process
        currentTimeQuantams = new HashMap<Integer, Integer>();


        reeadyQueue = new ArrayList<Process>();
        dieList = new ArrayList<Process>();
        AGFactors = new HashMap<Integer, Integer>();

        // Assign AG Factor to each process

        for (int i = 0; i < processes.size(); i++)
        {

            // test case of the assignment
//             if(i==0)
//             {
//                 AGFactors.put(processes.get(i).getId(), 20);
//
//
//             }
//             else if (i==1)
//             {
//                 AGFactors.put(processes.get(i).getId(), 17);
//
//             }
//             else if (i==2)
//             {
//                 AGFactors.put(processes.get(i).getId(), 16);
//
//             }
//             else if(i==3)
//             {
//                 AGFactors.put(processes.get(i).getId(), 43);
//
//             }

            // get AG Factor for each process and store in AGFactor ArrayList
            AGFactors.put(processes.get(i).getId(), calcAGFactor(processes.get(i)));

            // print the AG Factor for each process
            System.out.println("process name : " + processes.get(i).getName() + " AG factor : " + AGFactors.get(processes.get(i).getId()));
        }
        
        
    }

    //==================================================================================================================

    public void setStartAndFinishTime(Process runningProcess, int startedTime, int time)
    {
        runningProcess.getTime().add(Map.entry(startedTime, time));
        
    
    }

    public HashMap<Integer, ArrayList<Integer>> getQuantumHistory() {
        return quantumHistory;
    }


    /**
     * Print all history update of quantum time for each process.
     */
    public void printQuantumHistory()
    {
        for (Map.Entry<Integer, ArrayList<Integer>> entry : quantumHistory.entrySet())
        {
            Integer key = entry.getKey();
            ArrayList<Integer> values = entry.getValue();
            String name = getProcessNameById(key);
            System.out.print("process name: " + name + ", Values: ");
            for (Integer value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    //==================================================================================================================

    /**
     *
     * used to check if the AG will be preemptive or not.
     *
     * @param timeQuantum for the process.
     * @return the ceil of 50% of this quantum time.
     */
    public int convertToPreemptive(int timeQuantum)
    {
        return (int) Math.ceil(0.5 * timeQuantum);
    }

    //==================================================================================================================

    /**
     * calculate the mean of quantum time of the appeared processes.
     *
     * @return the mean if there is appeared processes and zero if not.
     */
    public double getMean()
    {
        double sum = 0;
        for (int value : currentTimeQuantams.values())
        {
            sum += value;
        }
        return currentTimeQuantams.size() > 0 ? sum / currentTimeQuantams.size() : 0;
    }


    //==================================================================================================================

    /**
     * calculate the AG Factor
     *
     * @param process that we want to calculate the AG Factor for it.
     *
     * @return the AG Factor.
     */
    public int calcAGFactor(Process process)
    {
        int rand = (int)(Math.random() * 20);
        if(rand < 10)
        {
            return process.getBurstTime() + process.getArrivalTime() + rand;
        } else if (rand > 10)
        {
            return process.getBurstTime() + process.getArrivalTime() + 10;
        } else
        {
            return process.getBurstTime() + process.getArrivalTime() + process.getPriority();
        }
        
    }

    //==================================================================================================================

    /**
     * search in the ready queue for the process that has lower AG Factor.
     * if equal select the process with lower Arrival Time.
     * if equal select the process with lower Remaining Burst Time.
     * if equal select random.
     *
     *
     * @return the position of the selected process in ready queue.
     */

    public int getProcessWithLowerAGFactor()
    {
        Process p = reeadyQueue.get(0) ;
        int remove = 0;
        for(int i=1 ; i<reeadyQueue.size() ; i++)
        {
            // lower AG factor
            if(AGFactors.get(reeadyQueue.get(i).getId())< AGFactors.get(p.getId()))
            {
                p = reeadyQueue.get(i);
                remove=i;
            }
            // AG factors are equals check arrival time
            if(AGFactors.get(reeadyQueue.get(i).getId())== AGFactors.get(p.getId()))
            {
                if(reeadyQueue.get(i).getArrivalTime() < p.getArrivalTime())
                {

                    p = reeadyQueue.get(i);
                    remove=i;
                }
                // arrival time equal check remaining time first then if equal select random
                if(reeadyQueue.get(i).getArrivalTime() == p.getArrivalTime())
                {
                    if(reeadyQueue.get(i).getRemainingBurstTime() < p.getRemainingBurstTime())
                    {
                        p = reeadyQueue.get(i);
                        remove=i;
                    }

                }
            }


        }
        return remove;
    }

    //==================================================================================================================



    /**
     *
     * finish execution of process and return.
     *
     *
     * @param runningProcess that running now.
     * @param time           is the finished time for the running process.
     * @return true if there is process in ready queue to execute next.
     */
    boolean finishProcess(Process runningProcess, int time)
    {
        currentTimeQuantams.remove(runningProcess.getId());
        quantumHistory.get(runningProcess.getId()).add(0);
        dieList.add(runningProcess);
        runningProcess.setFinishedTime(time);
        int TurnaroundTime = runningProcess.calculateTurnaroundTime();
        modifyTurnaroundTime(runningProcess.getId(), TurnaroundTime);
        int waitingTime = runningProcess.calculateWaitingTime();
        modifyWaitingTime(runningProcess.getId(),waitingTime);

        return (reeadyQueue.size()!=0);
    }

    //==================================================================================================================

    /**
     *  Executes the AG scheduling algorithm.
     */
    public void run()
    {

        int time = 0;
        int nextProcess = 0; // process will arrive next
        int startedTime = 0;
        boolean isThereRunningProcess = false;
        Process runningProcess = null; // process that running at cpu

        while(true)
        {

            // All processes finished its job
            if(dieList.size() == processes.size())
            {
                break;
            }


            // check if the process is arrived or not
            while(nextProcess < processes.size() && time == processes.get(nextProcess).getArrivalTime())
            {
                reeadyQueue.add(processes.get(nextProcess));
                currentTimeQuantams.put(processes.get(nextProcess).getId(), RRTimeQuantum);

                nextProcess++;
            }

            // if there is no process in cpu and no process in ready queue
            if(!isThereRunningProcess && reeadyQueue.size()==0)
            {
                time++;
                continue;
            }

            // if there is no process in cpu and there is process in ready queue
            if(!isThereRunningProcess)
            {
                int position = getProcessWithLowerAGFactor();
                runningProcess = reeadyQueue.get(position);
                reeadyQueue.remove(position);
                isThereRunningProcess = true;
                startedTime = time;
            }

            // process finished its quantum time
            if((time - startedTime) == currentTimeQuantams.get(runningProcess.getId()))
            {

                System.out.println("process name : " + runningProcess.getName() + " : " + startedTime + " -> " + time);
                setStartAndFinishTime(runningProcess, startedTime, time);
                startedTime = time;

                // process still has job to do (scenario 1)

                if(runningProcess.getRemainingBurstTime()!=0)
                {

                    //update quantum time
                    int increasedQuantum = (int) Math.ceil(0.1 * getMean());
                    int newQuantum = currentTimeQuantams.get(runningProcess.getId()) + increasedQuantum;
                    currentTimeQuantams.put(runningProcess.getId(), newQuantum );
                    quantumHistory.get(runningProcess.getId()).add(newQuantum);

                    // there is process in ready queue
                    if(reeadyQueue.size()!=0)
                    {
                        reeadyQueue.add(runningProcess);
                        runningProcess = reeadyQueue.get(0);
                        reeadyQueue.remove(0);
                    }

                    continue;


                }

                // process finished its job with it's quantum(scenario 3)
                else
                {
                    
                    // execute the process and check if there is process in ready queue to execute next
                    // if there is no process in ready queue
                    if(!finishProcess(runningProcess, time))
                    {
                        time++;
                        startedTime = time;
                        isThereRunningProcess = false;
                    }

                    // if there is process in ready queue
                    else
                    {
                        runningProcess = reeadyQueue.get(0);
                        reeadyQueue.remove(0);
                    }
                    
                    continue;
                }
            }

            // process finished its job before quantum time (scenario 3)

            if(runningProcess.getRemainingBurstTime()==0)
            {
                System.out.println("process name : " + runningProcess.getName() + " : " + startedTime + " -> " + time);
                setStartAndFinishTime(runningProcess, startedTime, time);
                startedTime = time;
                // timeList.add(Map.entry(lastEntry.getKey(), lastEntry.getValue() + 1));

                // execute the process and check if there is process in ready queue to execute next
                if(!finishProcess(runningProcess, time))
                {
                    time++;
                    startedTime = time;
                    isThereRunningProcess = false;
                }

                // if there is process in ready queue

                else
                {
                    runningProcess = reeadyQueue.get(0);
                    reeadyQueue.remove(0);
                }

                continue;

            }



            // non-preemptive AG
            if( (time - startedTime) < convertToPreemptive(currentTimeQuantams.get(runningProcess.getId())) )
            {
                time++;
                runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-1);
                continue;
            }

            // preemptive AG
            else
            {

                // no process in ready queue
                if(reeadyQueue.size()==0)
                {
                    time++;
                    runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-1);
                    continue;
                }

                // there are processes in ready queue
                else
                {
                    int position = getProcessWithLowerAGFactor();

                    // priority for the running process if the lower ag factor process in the ready queue is equal to the running process
                    if(AGFactors.get(runningProcess.getId()) <= AGFactors.get(reeadyQueue.get(position).getId()))
                    {
                        time++;
                        runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-1);
                        continue;
                    }

                    // process will get out from cpu and there is unused quantum time for this process(scenario 2)
                    else
                    {
                        System.out.println("process name : " + runningProcess.getName() + " : " + startedTime + " -> " + time);
                        setStartAndFinishTime(runningProcess, startedTime, time);
                        int unusedQuantum = currentTimeQuantams.get(runningProcess.getId()) - (time - startedTime);
                        int newQuantum = currentTimeQuantams.get(runningProcess.getId()) + unusedQuantum;
                        currentTimeQuantams.put(runningProcess.getId(), newQuantum );
                        quantumHistory.get(runningProcess.getId()).add(newQuantum);
                        startedTime = time;
                        reeadyQueue.add(runningProcess);
                        runningProcess = reeadyQueue.get(position);
                        reeadyQueue.remove(position);

                    }

                }

            }

        }

        //==================================================================================================================

        System.out.println("=".repeat(75));
        printTurnAroundTime();
        System.out.println("=".repeat(75));
        printWaitingTime();
        System.out.println("=".repeat(75));

        double AVGWaitingTime = getAverageWaitingTime();
        System.out.println("Average Waiting Time : " + AVGWaitingTime);
        System.out.println("=".repeat(75));

        double AVGTurnaroundTime = getAverageTurnAroundTime();
        System.out.println("Average Turnaround Time : " + AVGTurnaroundTime);
        System.out.println("=".repeat(75));
        System.out.println("History of quantum time of all processes");
        printQuantumHistory();
        System.out.println("=".repeat(75));

        //==================================================================================================================



    }

    //==================================================================================================================


}

//==================================================================================================================


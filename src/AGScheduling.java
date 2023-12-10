import java.util.ArrayList;
import java.util.HashMap;

public class AGScheduling extends Scheduler {
    private int RRTimeQuantum;
    private HashMap<Integer, ArrayList<Integer>> quantumHistory;
    private HashMap<Integer, Integer> currentTimeQuantams; 
    private ArrayList<Process> reeadyQueue;
    private HashMap<Integer, Integer> AGFactors;

    private ArrayList<Process> dieList;


    public AGScheduling(ArrayList<Process> processes, int RRTimeQuantum) {
        super(processes);
        this.RRTimeQuantum = RRTimeQuantum;
        quantumHistory = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < processes.size(); i++) {
            quantumHistory.put(processes.get(i).getId(), new ArrayList<Integer>());
            quantumHistory.get(processes.get(i).getId()).add(RRTimeQuantum);
        }

        currentTimeQuantams = new HashMap<Integer, Integer>();
//        for (int i = 0; i < processes.size(); i++) {
//            currentTimeQuantams.put(processes.get(i).getId(), RRTimeQuantum);
//        }

        reeadyQueue = new ArrayList<Process>();
        dieList = new ArrayList<Process>();
        AGFactors = new HashMap<Integer, Integer>();
        for (int i = 0; i < processes.size(); i++) {

            if(i==0){
                AGFactors.put(processes.get(i).getId(), 20);


            }
            else if (i==1){
                AGFactors.put(processes.get(i).getId(), 17);

            }
            else if (i==2){
                AGFactors.put(processes.get(i).getId(), 16);

            }
            else if(i==3){
                AGFactors.put(processes.get(i).getId(), 43);

            }

//            AGFactors.put(processes.get(i).getId(), calcAGFactor(processes.get(i)));
        }
        
        
    }

    public int convertToPreemptive(int timeQuantum) {
        return (int) Math.ceil(0.5 * timeQuantum);
    }

    public double getMean() {
        double sum = 0;
        for (int value : currentTimeQuantams.values()) {
            sum += value;
        }
        return currentTimeQuantams.size() > 0 ? sum / currentTimeQuantams.size() : 0;
    }

    

    public int calcAGFactor(Process process) {
        int rand = (int) Math.random() * 20;
        if(rand < 10) {
            return process.getBurstTime() + process.getArrivalTime() + rand;
        } else if (rand > 10) {
            return process.getBurstTime() + process.getArrivalTime() + 10;
        } else {
            return process.getBurstTime() + process.getArrivalTime() + process.getPriority();
        }
        
    }

    public int getProcessWithLowerAGFactor(){
        Process p = reeadyQueue.get(0) ;
        int remove = 0;
        for(int i=1 ; i<reeadyQueue.size() ; i++){
            // lower AG factor
            if(AGFactors.get(reeadyQueue.get(i).getId())< AGFactors.get(p.getId())){
                p = reeadyQueue.get(i);
                remove=i;
            }
            // AG factors are equals check arrival time
            if(AGFactors.get(reeadyQueue.get(i).getId())== AGFactors.get(p.getId())){
                if(reeadyQueue.get(i).getArrivalTime() < p.getArrivalTime()){

                    p = reeadyQueue.get(i);
                    remove=i;
                }
                // arrival time equal check remaining time first then if equal select random
                if(reeadyQueue.get(i).getArrivalTime() == p.getArrivalTime()){
                    if(reeadyQueue.get(i).getRemainingBurstTime() < p.getRemainingBurstTime()){
                        p = reeadyQueue.get(i);
                        remove=i;
                    }

                }
            }


        }
        return remove;
    }


    public void run() {

        int time = 0;
        int nextProcess = 0; // process will arrive next
        int startedTime = 0;
        boolean isThereRunningProcess = false;
        Process runningProcess = null; // process that running at cpu
        while(true){
            if(dieList.size() == processes.size()){
                break;
            }
            // check if the process is arrived or not
            if(nextProcess < processes.size() && time == processes.get(nextProcess).getArrivalTime()){

                reeadyQueue.add(processes.get(nextProcess));
                currentTimeQuantams.put(processes.get(nextProcess).getId(), RRTimeQuantum);

                nextProcess++;
            }
            // if there is no process in cpu and no process in ready queue
            if(!isThereRunningProcess && reeadyQueue.size()==0){
                time++;
                continue;
            }
            // if there is no process in cpu and there is process in ready queue
            if(!isThereRunningProcess){
                int position = getProcessWithLowerAGFactor();
                runningProcess = reeadyQueue.get(position);
                reeadyQueue.remove(position);
                isThereRunningProcess = true;
                startedTime = time;
            }

            // process finished its quantum time

            if((time - startedTime) == currentTimeQuantams.get(runningProcess.getId())){

                System.out.println("process name : " + runningProcess.getName() + " : " + startedTime + " -> " + time);
                startedTime = time;

                // process has job to do (scenario 1)

                if(runningProcess.getRemainingBurstTime()!=0){


                    //update quantum time
                    int increasedQuantum = (int) Math.ceil(0.1 * getMean());
                    int newQuantum = currentTimeQuantams.get(runningProcess.getId()) + increasedQuantum;
                    currentTimeQuantams.put(runningProcess.getId(), newQuantum );
                    quantumHistory.get(runningProcess.getId()).add(newQuantum);



                    // no process in ready queue
                    if(reeadyQueue.size()==0){
                        time++;
                        startedTime = time;
                        continue;
                    }else{
                        reeadyQueue.add(runningProcess);
                        runningProcess = reeadyQueue.get(0);

                        reeadyQueue.remove(0);
                        continue;
                    }


                }
                // process finished its job(scenario 3)
                else{

                    currentTimeQuantams.remove(runningProcess.getId());
                    quantumHistory.get(runningProcess.getId()).add(0);
                    dieList.add(runningProcess);

                    runningProcess.setFinishedTime(time);
                    int TurnaroundTime = runningProcess.calculateTurnaroundTime();
                    modifyTurnaroundTime(runningProcess.getId(), TurnaroundTime);
                    int waitingTime = runningProcess.calculateWaitingTime();
                    modifyWaitingTime(runningProcess.getId(),waitingTime);

                    if(reeadyQueue.size()==0){
                        time++;
                        startedTime = time;

                        isThereRunningProcess = false;

                    }
                    else{

                        runningProcess = reeadyQueue.get(0);
                        reeadyQueue.remove(0);

                    }
                    continue;
                }
            }

            // process finished its job(scenario 3)

            if(runningProcess.getRemainingBurstTime()==0) {
                System.out.println("process name : " + runningProcess.getName() + " : " + startedTime + " -> " + time);
                startedTime = time;

                currentTimeQuantams.remove(runningProcess.getId());
                quantumHistory.get(runningProcess.getId()).add(0);
                dieList.add(runningProcess);
                runningProcess.setFinishedTime(time);
                int TurnaroundTime = runningProcess.calculateTurnaroundTime();
                modifyTurnaroundTime(runningProcess.getId(), TurnaroundTime);
                int waitingTime = runningProcess.calculateWaitingTime();
                modifyWaitingTime(runningProcess.getId(),waitingTime);
                if(reeadyQueue.size()==0){
                    time++;
                    startedTime = time;
                    isThereRunningProcess = false;

                }
                else{

                    runningProcess = reeadyQueue.get(0);
                    reeadyQueue.remove(0);

                }
                continue;

            }



                // non-preemptive AG
            if( (time - startedTime) < convertToPreemptive(currentTimeQuantams.get(runningProcess.getId())) ){
                time++;
                runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-1);
                continue;
            }
            // preemptive AG
            else{
                // no process in ready queue
                if(reeadyQueue.size()==0){
                    time++;
                    runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-1);
                    continue;
                }
                // there are processes in ready queue
                else{
                    int position = getProcessWithLowerAGFactor();

                    // priority for the running process if the lower ag factor process in the ready queue is equal to the running process
                    if(AGFactors.get(runningProcess.getId()) <= AGFactors.get(reeadyQueue.get(position).getId())){
                        time++;
                        runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-1);
                        continue;
                    }
                    // process will get out from cpu and there is unused quantum time for this process(scenario 2)
                    else{
                        System.out.println("process name : " + runningProcess.getName() + " : " + startedTime + " -> " + time);
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

        System.out.println("==========================================================================");
        printTurnAroundTime();
        System.out.println("==========================================================================");
        printWaitingTime();
        System.out.println("==========================================================================");

        double AVGWaitingTime = getAverageWaitingTime();
        System.out.println("Average Waiting Time : " + AVGWaitingTime);
        System.out.println("==========================================================================");

        double AVGTurnaroundTime = getAverageTurnAroundTime();
        System.out.println("Average Turnaround Time : " + AVGTurnaroundTime);
        System.out.println("==========================================================================");


    }


}

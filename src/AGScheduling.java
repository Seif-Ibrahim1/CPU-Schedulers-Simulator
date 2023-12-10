import java.util.ArrayList;
import java.util.HashMap;

public class AGScheduling extends Scheduler {
    private int RRTimeQuantum;
    private ArrayList<ArrayList<Integer>> quantumHistory;
    private ArrayList<Integer> currentTimeQuantams; 
    private ArrayList<Process> reeadyQueue;
    private HashMap<Integer, Integer> AGFactors;
    
    public AGScheduling(ArrayList<Process> processes, int RRTimeQuantum) {
        super(processes);
        this.RRTimeQuantum = RRTimeQuantum;
        quantumHistory = new ArrayList<ArrayList<Integer>>(processes.size());
        for (int i = 0; i < processes.size(); i++) {
            quantumHistory.add(new ArrayList<Integer>());
            quantumHistory.get(i).add(RRTimeQuantum);
        }

        currentTimeQuantams = new ArrayList<Integer>(processes.size());
        for (int i = 0; i < processes.size(); i++) {
            currentTimeQuantams.add(RRTimeQuantum);
        }

        reeadyQueue = new ArrayList<Process>();
        AGFactors = new HashMap<Integer, Integer>();
        
        
    }

    public int convertToPreemptive(int timeQuantum) {
        return (int) Math.ceil(0.5 * timeQuantum);
    }

    public double getMean() {
        double sum = 0;
        for (int i = 0; i < currentTimeQuantams.size(); i++) {
            sum += currentTimeQuantams.get(i);
        }
        return sum / currentTimeQuantams.size();
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


    public void run() {
        
    }


}

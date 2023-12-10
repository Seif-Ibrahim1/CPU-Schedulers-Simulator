import java.util.ArrayList;

public class AGScheduling extends Scheduler {
    private int RRTimeQuantum;
    
    AGScheduling(ArrayList<Process> processes, int RRTimeQuantum) {
        super(processes);
        this.RRTimeQuantum = RRTimeQuantum;
    }

    public void run() {
        
    }
}

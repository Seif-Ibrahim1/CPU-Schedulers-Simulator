import java.util.ArrayList;

public class SJFScheduling extends Scheduler {
    private int contextSwitchTime;

    public SJFScheduling(ArrayList<Process> processes, int contextSwitchTime) {
        super(processes);
        this.contextSwitchTime = contextSwitchTime;
    }

    public void run() {
        
    }
    
}

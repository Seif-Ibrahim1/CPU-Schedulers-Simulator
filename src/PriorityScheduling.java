import java.util.ArrayList;

public class PriorityScheduling extends Scheduler implements StarvationHandler {
    public PriorityScheduling(ArrayList<Process> processes) {
        super(processes);
    }

    public boolean checkForStravation(Process process) {
        return false;
    }
}
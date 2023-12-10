import java.util.ArrayList;

public class SRTFScheduling extends Scheduler implements StarvationHandler{
    SRTFScheduling(ArrayList<Process> processes) {
        super(processes);
    }

    public boolean checkForStravation(Process process) {
        return false;
    }

}

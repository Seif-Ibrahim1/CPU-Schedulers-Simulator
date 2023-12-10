import java.util.ArrayList;

public abstract class Scheduler {
    protected ArrayList<Process> processes;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
    }
}

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * SJFScheduling class implementing the Shortest Job First (SJF) scheduling algorithm.
 */
public class SJFScheduling extends Scheduler {
    private int contextSwitchTime;

    /**
     * Constructor for SJFScheduling class.
     *
     * @param processes         ArrayList of processes to be scheduled
     * @param contextSwitchTime time taken for context switching
     */
    public SJFScheduling(ArrayList<Process> processes, int contextSwitchTime) {
        super(processes);
        this.contextSwitchTime = contextSwitchTime;
    }

    /**
     * Executes the Shortest Job First (SJF) scheduling algorithm.
     * Processes are sorted by arrival time and then executed in a manner
     * where the process with the shortest burst time is chosen next.
     */
    public void run() {
        int currentTime = 0;

        // Sort processes by arrival time
        Collections.sort(processes, Comparator.comparingInt(Process::getArrivalTime));

        int i = 0;
        while (i < processes.size()) {
            // Create a list of processes that have arrived by the current time
            ArrayList<Process> arrivalProcesses = new ArrayList<>();

            while (i < processes.size() && processes.get(i).getArrivalTime() <= currentTime) {
                arrivalProcesses.add(processes.get(i));
                i++;
            }

            if (!arrivalProcesses.isEmpty()) {
                // Sort the arrived processes by their burst times
                Collections.sort(arrivalProcesses, Comparator.comparingInt(Process::getBurstTime));

                for (Process process : arrivalProcesses) {
                    // Execute each process, calculating turnaround and waiting times
                    process.setFinishedTime(currentTime + process.getBurstTime() + contextSwitchTime);

                    process.setTurnaroundTime(process.getFinishedTime() - process.getArrivalTime());

                    process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());

                    // Display process execution information
                    System.out.println("Process name : " + process.getName() + " : " + currentTime + " -> " + (currentTime + process.getBurstTime()));
                    currentTime += process.getBurstTime() + contextSwitchTime;
                }
            } else {
                // If no processes arrived, move to the next unit of time
                currentTime++;
            }
        }

        // Display Average Waiting Time and Average Turnaround Time after all processes are executed
        System.out.println("==========================================================================");
        printTurnAroundTime();

        System.out.println("==========================================================================");
        printWaitingTime();

        System.out.println("==========================================================================");
        System.out.println("Average Waiting Time : " + getAverageWaitingTime());

        System.out.println("==========================================================================");
        System.out.println("Average Turnaround Time : " + getAverageTurnAroundTime());

    }
}

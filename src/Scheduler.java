import java.util.ArrayList;
import java.util.Collections;

/**
 * Scheduler abstract class that contains the common attributes and methods between all schedules.
 */
public abstract class Scheduler {
    protected ArrayList<Process> processes;

    //==================================================================================================================

    /**
     * Constructor for Scheduler abstract class.
     * sort the processes according to the Arrival Time.
     *
     * @param processes entered from the user.
     */
    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
        Collections.sort(this.processes);
    }

    //==================================================================================================================

    /**
     * abstract method to run the algorithm for each subclass.
     */

    public abstract void run();

    //==================================================================================================================

    /**
     * calculate the Average of Turnaround Time.
     *
     * @return the Average of Turnaround Time.
     */
    protected double getAverageTurnAroundTime() {
        double totalTurnAroundTime = 0;
        for (Process process : processes) {
            totalTurnAroundTime += process.getTurnaroundTime();
        }
        return totalTurnAroundTime / processes.size();
    }

    //==================================================================================================================

    /**
     * calculate the Average of waiting Time.
     *
     * @return the Average of waiting Time.
     */
    protected double getAverageWaitingTime() {
        double totalWaitingTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
        }
        return totalWaitingTime / processes.size();
    }

    //==================================================================================================================

    /**
     * set the waiting time for the process in the ArrayList.
     *
     * @param processId      of the process that it want to set the waiting time.
     * @param newWaitingTime that will be given to the process.
     */
    protected void modifyWaitingTime(int processId, int newWaitingTime) {
        for (Process process : processes) {
            if (process.getId() == processId) {
                process.setWaitingTime(newWaitingTime);
                return;
            }
        }
    }

    //==================================================================================================================

    /**
     * set the turnaround time for the process in the ArrayList.
     *
     * @param processId          of the process that it want to set the turnaround time.
     * @param newTurnaroundTime  that will be given to the process.
     */
    protected void modifyTurnaroundTime(int processId, int newTurnaroundTime) {
        for (Process process : processes) {
            if (process.getId() == processId) {
                process.setTurnaroundTime(newTurnaroundTime);
                return;
            }
        }
    }

    //==================================================================================================================

    /**
     *
     * search for the process in the ArrayList by ID.
     *
     * @param id  of the process that we want to get it.
     * @return the name of the process if exist and null if it doesn't exist.
     */
    protected String getProcessNameById(int id) {
        for (Process process : processes) {
            if (process.getId() == id) {
                return process.getName();
            }
        }
        //
        return null;
    }

    //==================================================================================================================


    /**
     * print the waiting time for each process.
     */
    protected void printWaitingTime() {
        for (Process process : processes) {
            System.out.println("Process " + process.getName() + " waiting time: " + process.getWaitingTime());
        }
    }

    //==================================================================================================================

    /**
     * print the Turnaround time for each process.
     */
    protected void printTurnAroundTime() {
        for (Process process : processes) {
            System.out.println("Process " + process.getName() + " turnaround time: " + process.getTurnaroundTime());
        }
    }
    //==================================================================================================================

}

//==================================================================================================================

import java.util.ArrayList;
import java.util.Collections;

public abstract class Scheduler {
    protected ArrayList<Process> processes;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
        Collections.sort(this.processes);
    }

    public abstract void run();

    protected double getAverageTurnAroundTime() {
        double totalTurnAroundTime = 0;
        for (Process process : processes) {
            totalTurnAroundTime += process.getTurnaroundTime();
        }
        return totalTurnAroundTime / processes.size();
    }
    protected void modifyWaitingTime(int processId, int newWaitingTime) {
        for (Process process : processes) {
            if (process.getId() == processId) {
                process.setWaitingTime(newWaitingTime);
                return;
            }
        }
    }
    protected String getProcessNameById(int id) {
        for (Process process : processes) {
            if (process.getId() == id) {
                return process.getName();
            }
        }
        //
        return null;
    }

    protected void modifyTurnaroundTime(int processId, int newTurnaroundTime) {
        for (Process process : processes) {
            if (process.getId() == processId) {
                process.setTurnaroundTime(newTurnaroundTime);
                return;
            }
        }
    }

    protected double getAverageWaitingTime() {
        double totalWaitingTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
        }
        return totalWaitingTime / processes.size();
    }

    protected void printWaitingTime() {
        for (Process process : processes) {
            System.out.println("Process " + process.getName() + " waiting time: " + process.getWaitingTime());
        }
    }

    protected void printTurnAroundTime() {
        for (Process process : processes) {
            System.out.println("Process " + process.getName() + " turnaround time: " + process.getTurnaroundTime());
        }
    }

}

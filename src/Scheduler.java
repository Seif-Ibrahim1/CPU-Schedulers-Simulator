import java.util.ArrayList;

public abstract class Scheduler {
    protected ArrayList<Process> processes;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
    }

    public abstract void run();

    public double getAverageTurnAroundTime() {
        double totalTurnAroundTime = 0;
        for (Process process : processes) {
            totalTurnAroundTime += process.getTurnaroundTime();
        }
        return totalTurnAroundTime / processes.size();
    }

    public double getAverageWaitingTime() {
        double totalWaitingTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
        }
        return totalWaitingTime / processes.size();
    }

    void printWaitingTime() {
        for (Process process : processes) {
            System.out.println("Process " + process.getName() + " waiting time: " + process.getWaitingTime());
        }
    }

    void printTurnAroundTime() {
        for (Process process : processes) {
            System.out.println("Process " + process.getName() + " turnaround time: " + process.getTurnaroundTime());
        }
    }

}

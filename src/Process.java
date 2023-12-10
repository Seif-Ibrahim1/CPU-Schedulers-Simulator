public class Process {
    public String name;
    public int id; // maybe we don't need this but just in case
    public int arrivalTime;
    public int burstTime;
    public int priority;
    public int turnaroundTime;
    public int waitingTime;
    public int remainingBurstTime;
    public int finishedTime;

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
        this.remainingBurstTime = burstTime;
        this.finishedTime = 0;
    }
}

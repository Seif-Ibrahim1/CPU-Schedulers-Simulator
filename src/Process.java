import java.awt.*;
import java.util.Comparator;

public class Process implements Comparable<Process> {
    private String name;
    private int id;
    private Color color;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int turnaroundTime;
    private int waitingTime;
    private int remainingBurstTime;
    private int finishedTime;
    private int startTime;
    
    private int oldPriority;

    public Process(String name, int arrivalTime, int burstTime, int priority, int id, Color color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.oldPriority = priority;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
        this.remainingBurstTime = burstTime;
        this.finishedTime = 0;
        this.id = id;
        this.color = color;
        this.startTime = 0;
    }

    //==================================================================================================================

    public int getOldPriority() {
        return oldPriority;
    }

    //==================================================================================================================


    public String getName() {
        return name;
    }

    //==================================================================================================================


    public int getArrivalTime() {
        return arrivalTime;
    }

    //==================================================================================================================



    public int getBurstTime() {
        return burstTime;
    }

    //==================================================================================================================



    public int getPriority() {
        return priority;
    }

    //==================================================================================================================

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    //==================================================================================================================

    public int getWaitingTime() {
        return waitingTime;
    }

    //==================================================================================================================

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public int getFinishedTime() {
        return finishedTime;
    }

    public int getId() {
        return id;
    }
    public Color getColor() {return color;}
    public int getStartTime() {return startTime;}
    public void setStartTime(int startTime) {this.startTime = startTime;}
    public void setColor(Color color) {this.color = color;}

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
    }

    public void setFinishedTime(int finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    //==================================================================================================================

    /**
     * calculate Turnaround Time.
     *
     * @return Turnaround Time.
     */
    public int calculateTurnaroundTime() {
        return finishedTime - arrivalTime;
    }

    //==================================================================================================================

    /**
     * calculate Waiting Time.
     *
     * @return Waiting Time.
     */
    public int calculateWaitingTime() {
        return turnaroundTime - burstTime;
    }
    //==================================================================================================================

    /**
     * for sorting processes by arrival time
     *
     * @param process the object to be compared.
     * @return zero if equal or 1 if greater or -1 if smaller
     */
    @Override
    public int compareTo(Process process) {
        return Integer.compare(this.arrivalTime, process.arrivalTime);
    }}

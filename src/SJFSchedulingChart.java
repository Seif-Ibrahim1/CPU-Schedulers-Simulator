import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * SJFSchedulingChart class extends GUI to display a Gantt chart representing
 * the Shortest Job First (SJF) scheduling algorithm.
 */
public class SJFSchedulingChart extends GUI {
    private int contextSwitchingTime;

    /**
     * Constructor for SJFSchedulingChart class.
     *
     * @param processes              ArrayList of processes to be scheduled
     * @param averageWaitingTime     Average waiting time of the schedule
     * @param averageTurnaroundTime  Average turnaround time of the schedule
     * @param contextSwitchingTime   Time taken for context switching
     */
    public SJFSchedulingChart(ArrayList<Process> processes, double averageWaitingTime,
                              double averageTurnaroundTime, int contextSwitchingTime) {
        super(processes, averageWaitingTime, averageTurnaroundTime);
        this.contextSwitchingTime = contextSwitchingTime;
    }

    /**
     * Draws the Gantt chart representing the scheduling algorithm.
     *
     * @param g Graphics object used for drawing
     */
    protected void drawChart(Graphics g) {
        // Drawing the title
        Font titleFont = new Font("Arial", Font.BOLD, 23);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("CPU Scheduling Graph", 50, 30);

        int y = 50;
        int totalExecutionTime = calculateMaxTime();

        // Find the maximum execution time among processes
        for (Process process : processes) {
            totalExecutionTime = Math.max(totalExecutionTime, process.getFinishedTime());
        }

        // Draw bars for each process
        for (Process process : processes) {
            int executionStart = process.getStartTime();
            int executionEnd = process.getFinishedTime() - contextSwitchingTime;
            int barStart = (int) (((double) executionStart / totalExecutionTime) * 500);
            int barLength = (int) (((double) (executionEnd - executionStart) / totalExecutionTime) * 500);
            int contextSwitchingBarLength = (int) (((double) contextSwitchingTime / totalExecutionTime) * 500);

            g.setColor(Color.BLACK);
            g.drawString(process.getName(), 10, y + 20);

            g.setColor(process.getColor());
            g.fillRect(50 + barStart, y, barLength, 30);

            // Fill the gap between the end of the process and the start of the next process with black color
            g.setColor(Color.BLACK);
            g.fillRect(50 + barStart + barLength, y, contextSwitchingBarLength, 30);

            y += 50;
        }
        displayProcessInfo(g); // Display process information
        displayStatistics(g, "SJFSchedule"); // Display scheduling statistics
    }

    /**
     * Method to display process information including context switching time.
     *
     * @param g Graphics object used for drawing
     */
    protected void displayProcessInfo(Graphics g) {
        super.displayProcessInfo(g); // Invoke the superclass method
        g.drawString("Context Switching Time: " + contextSwitchingTime, 700, y + 15);
        g.setColor(Color.BLACK);
        g.fillRect(900, y, 20, 20);
    }
}

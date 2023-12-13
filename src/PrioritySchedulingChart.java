import java.awt.*;
import java.util.ArrayList;

/**
 * PrioritySchedulingChart class extends GUI to display a Gantt chart representing
 * the Priority scheduling algorithm.
 */
public class PrioritySchedulingChart extends GUI {
    /**
     * Constructor for PrioritySchedulingChart class.
     *
     * @param processes              ArrayList of processes to be scheduled
     * @param averageWaitingTime     Average waiting time of the schedule
     * @param averageTurnaroundTime  Average turnaround time of the schedule
     */
    public PrioritySchedulingChart(ArrayList<Process> processes, double averageWaitingTime, double averageTurnaroundTime) {
        super(processes, averageWaitingTime, averageTurnaroundTime);
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
            int executionEnd = process.getFinishedTime();
            int barStart = (int) (((double) executionStart / totalExecutionTime) * 500);
            int barLength = (int) (((double) (executionEnd - executionStart) / totalExecutionTime) * 500);

            g.setColor(Color.BLACK);
            g.drawString(process.getName(), 10, y + 20);

            g.setColor(process.getColor());
            g.fillRect(50 + barStart, y, barLength, 30);

            y += 50;
        }
        displayProcessInfo(g); // Display process information
        displayStatistics(g, "PrioritySchedule"); // Display scheduling statistics
    }
}

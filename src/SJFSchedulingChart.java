import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * SJFSchedulingChart class extends JFrame to display a Gantt chart representing the Shortest Job First (SJF) scheduling algorithm.
 */
public class SJFSchedulingChart extends JFrame {

    private ArrayList<Process> processes;
    private double averageWaitingTime;
    private double averageTurnaroundTime;
    private int contextSwitchingTime;

    /**
     * Constructor for SJFSchedulingChart class.
     *
     * @param processes              ArrayList of processes to be scheduled
     * @param averageWaitingTime     Average waiting time of the schedule
     * @param averageTurnaroundTime  Average turnaround time of the schedule
     * @param contextSwitchingTime   Time taken for context switching
     */
    public SJFSchedulingChart(ArrayList<Process> processes, double averageWaitingTime, double averageTurnaroundTime, int contextSwitchingTime) {
        this.processes = processes;
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.contextSwitchingTime = contextSwitchingTime;
        setTitle("Gantt Chart");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };

        panel.setBackground(new Color(0x9AC0C0)); // Set background color
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setVisible(true);
    }

    /**
     * Draws the Gantt chart representing the scheduling algorithm.
     *
     * @param g Graphics object used for drawing
     */
    private void drawChart(Graphics g) {
        // Drawing the title
        Font titleFont = new Font("Arial", Font.BOLD, 23);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("CPU Scheduling Graph", 50, 30);

        int y = 50;
        int totalExecutionTime = calculateMaxTime();

        for (Process process : processes) {
            totalExecutionTime = Math.max(totalExecutionTime, process.getFinishedTime());
        }

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

            // Filling the gap between the end of the process and the start of the next process with black color
            g.setColor(Color.BLACK);
            g.fillRect(50 + barStart + barLength, y, contextSwitchingBarLength, 30);

            y += 50;
        }
        displayProcessInfo(g);
        displayStatistics(g);
    }

    /**
     * Displays process information on the chart.
     *
     * @param g Graphics object used for drawing
     */
    private void displayProcessInfo(Graphics g) {
        Font titleFont = new Font("Arial", Font.BOLD, 23);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("Processes Information", 700, 30);

        int y = 50;
        Font infoFont = new Font("Arial", Font.BOLD, 15);
        g.setFont(infoFont);

        for (Process process : processes) {
            g.setColor(Color.BLACK);
            g.drawString("Process: " + process.getName(), 700, y + 15);
            g.setColor(process.getColor());
            g.fillRect(800, y, 20, 20); // Display color in a square
            g.setColor(Color.BLACK);
            g.drawString("ID: " + process.getId(), 830, y + 15);
            g.drawString("Priority: " + process.getPriority(), 890, y + 15);

            y += 50;
        }
        // Displaying context switching time information
        g.drawString("Context Switching Time: " + contextSwitchingTime, 700, y + 15);
        g.setColor(Color.BLACK);
        g.fillRect(900, y, 20, 20);
    }

    /**
     * Displays statistics related to the scheduling algorithm.
     *
     * @param g Graphics object used for drawing
     */
    private void displayStatistics(Graphics g) {
        Font titleFont = new Font("Arial", Font.BOLD, 18);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("Statistics", 50, 400);
        g.setColor(Color.BLACK);
        g.drawString("Schedule Name: SJFSchedule", 50, 450);
        g.setColor(Color.BLACK);
        g.drawString("Average Waiting Time: " + averageWaitingTime, 50, 500);
        g.setColor(Color.BLACK);
        g.drawString("Average Turnaround Time: " + averageTurnaroundTime, 50, 550);
    }

    /**
     * Calculates the maximum time among all processes.
     *
     * @return Maximum time among all processes
     */
    private int calculateMaxTime() {
        int maxTime = 0;
        for (Process process : processes) {
            if (process.getBurstTime() > maxTime) {
                maxTime = process.getBurstTime();
            }
        }
        return maxTime;
    }
}

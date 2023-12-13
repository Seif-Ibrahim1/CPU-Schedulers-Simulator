import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Abstract class defining a GUI for displaying scheduling information.
 * Extends JFrame to create a window.
 */
public class GUI extends JFrame {

    protected ArrayList<Process> processes;

    protected double averageWaitingTime;

    protected double averageTurnaroundTime;

    // Static variable for vertical positioning
    public static int y = 50;

    /**
     * Constructor to initialize GUI with process list and statistics.
     *
     * @param processes            List of processes to display
     * @param averageWaitingTime   Average waiting time for processes
     * @param averageTurnaroundTime Average turnaround time for processes
     */
    public GUI(ArrayList<Process> processes, double averageWaitingTime, double averageTurnaroundTime , String scheduleName) {
        this.processes = processes;
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;

        setTitle("Gantt Chart"); // Set title of the window
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Set window location to center

        // Panel to draw the chart
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g , scheduleName); // Invoke method to draw the chart
            }
        };

        panel.setBackground(new Color(0x9AC0C0)); // Set background color
        JScrollPane scrollPane = new JScrollPane(panel); // Add scrolling functionality
        add(scrollPane); // Add panel to the frame

        setVisible(true); // Set the frame visible
    }

    /**
     * Abstract method to draw the chart, implemented in subclasses.
     *
     * @param g Graphics object used for drawing
     */
    /**
     * Draws the Gantt chart representing the scheduling algorithm.
     *
     * @param g Graphics object used for drawing
     */
    protected void drawChart(Graphics g , String scheduleName) {
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
            //loop over the time list of the process
            int executionStart = 0;
            int executionEnd = 0;
            for (int i = 0; i < process.getTime().size(); i++) {
                executionStart = process.getTime().get(i).getKey();
                executionEnd = process.getTime().get(i).getValue();
                int barStart = (int) (((double) executionStart / totalExecutionTime) * 500);
                int barLength = (int) (((double) (executionEnd - executionStart) / totalExecutionTime) * 500);

                g.setColor(Color.BLACK);
                g.drawString(process.getName(), 10, y + 20);

                g.setColor(process.getColor());
                g.fillRect(50 + barStart, y, barLength, 30);

                y += 50;
            }
        }
        displayProcessInfo(g); // Display process information
        displayStatistics(g,scheduleName); // Display scheduling statistics
    }

    /**
     * Method to display process information.
     *
     * @param g Graphics object used for drawing
     */
    protected void displayProcessInfo(Graphics g) {
        Font titleFont = new Font("Arial", Font.BOLD, 23);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("Processes Information", 700, 30); // Title for process information

        y = 50;
        Font infoFont = new Font("Arial", Font.BOLD, 15);
        g.setFont(infoFont);

        // Loop through processes to display their information
        for (Process process : processes) {
            g.setColor(Color.BLACK);
            g.drawString("Process: " + process.getName(), 700, y + 15);
            g.setColor(process.getColor());
            g.fillRect(800, y, 20, 20); // Display color in a square
            g.setColor(Color.BLACK);
            g.drawString("ID: " + process.getId(), 830, y + 15);
            g.drawString("Priority: " + process.getOldPriority(), 890, y + 15);

            y += 50; // Increment y position for next process
        }
    }

    /**
     * Method to display statistics.
     *
     * @param g            Graphics object used for drawing
     * @param scheduleName Name of the scheduling algorithm
     */
    protected void displayStatistics(Graphics g, String scheduleName) {
        Font titleFont = new Font("Arial", Font.BOLD, 18);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("Statistics", 50, 450); // Title for statistics
        g.setColor(Color.BLACK);
        g.drawString("Schedule Name: " + scheduleName, 50, 470); // Display schedule name
        g.setColor(Color.BLACK);
        g.drawString("Average Waiting Time: " + averageWaitingTime, 50, 500); // Display average waiting time
        g.setColor(Color.BLACK);
        g.drawString("Average Turnaround Time: " + averageTurnaroundTime, 50, 530); // Display average turnaround time
    }

    /**
     * Method to calculate the maximum time among processes.
     *
     * @return Maximum time among processes
     */
    protected int calculateMaxTime() {
        int maxTime = 0;
        // Loop through processes to find the maximum burst time
        for (Process process : processes) {
            if (process.getBurstTime() > maxTime) {
                maxTime = process.getBurstTime();
            }
        }
        return maxTime; // Return the maximum time
    }
}

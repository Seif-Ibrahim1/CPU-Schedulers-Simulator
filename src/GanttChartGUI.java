import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GanttChartGUI extends JFrame {

    private ArrayList<Process> processes;
    private double averageWaitingTime;
    private double averageTurnaroundTime;

    public GanttChartGUI(ArrayList<Process> processes, double averageWaitingTime, double averageTurnaroundTime) {
        this.processes = processes;
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
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

    private void drawChart(Graphics g) {

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
            int executionEnd = process.getFinishedTime() ;
            int barStart = (int) (((double) executionStart / totalExecutionTime) * 500);
            int barLength = (int) (((double) (executionEnd - executionStart) / totalExecutionTime) * 500);


            g.setColor(Color.BLACK);
            g.drawString( process.getName(), 10, y + 20);

            g.setColor(process.getColor());
            g.fillRect(50 + barStart, y, barLength, 30);

            y += 50;
        }
        displayProcessInfo(g);
        displayStatistics( g);
    }
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
    }
    private void displayStatistics(Graphics g){
        Font titleFont = new Font("Arial", Font.BOLD, 20);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("Statistics", 50, 400);
        g.setColor(Color.BLACK);
        g.drawString("Average Waiting Time: " +averageWaitingTime, 50, 450);
        g.setColor(Color.BLACK);
        g.drawString("Average Turnaround Time: " +averageTurnaroundTime, 50, 500);
    }
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

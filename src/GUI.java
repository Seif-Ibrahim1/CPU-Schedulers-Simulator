import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class GUI extends JFrame{
    protected ArrayList<Process> processes;
    protected double averageWaitingTime;
    protected double averageTurnaroundTime;
    public static int y = 50;
    public GUI(ArrayList<Process> processes, double averageWaitingTime, double averageTurnaroundTime) {
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
    protected abstract void drawChart(Graphics g);
    protected void displayProcessInfo(Graphics g) {
        Font titleFont = new Font("Arial", Font.BOLD, 23);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("Processes Information", 700, 30);

        y = 50;
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
    protected void displayStatistics(Graphics g, String scheduleName) {
        Font titleFont = new Font("Arial", Font.BOLD, 18);
        g.setColor(Color.red);
        g.setFont(titleFont);
        g.drawString("Statistics", 50, 400);
        g.setColor(Color.BLACK);
        g.drawString("Schedule Name: "+scheduleName, 50, 450);
        g.setColor(Color.BLACK);
        g.drawString("Average Waiting Time: " + averageWaitingTime, 50, 500);
        g.setColor(Color.BLACK);
        g.drawString("Average Turnaround Time: " + averageTurnaroundTime, 50, 550);
    }
    protected int calculateMaxTime() {
        int maxTime = 0;
        for (Process process : processes) {
            if (process.getBurstTime() > maxTime) {
                maxTime = process.getBurstTime();
            }
        }
        return maxTime;
    }

}

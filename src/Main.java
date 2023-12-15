import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * Ahmed Saad          20210020
 * Seif Ibrahim        20210164
 * Shahd Osama         20211052
 * Shahd Mostafa       20211054
 * Maryam Osama        20211090
 *
 * Group : IT S1&S2
 *
 */
public class Main {

    enum SupportedColor {
        RED, GREEN, BLUE, YELLOW, PINK, GRAY, CYAN, ORANGE;
    }

    public Color getColor(String colorName) {
        try {
            SupportedColor supportedColor = SupportedColor.valueOf(colorName.toUpperCase());
            return getColorFromEnum(supportedColor);
        } catch (IllegalArgumentException e) {
            
            System.out.println("Color not supported: " + colorName + " Changed to Dark Gray");
            return Color.DARK_GRAY; // You might want to return a default color in case of an error
        }
    }

    private Color getColorFromEnum(SupportedColor supportedColor) {
        // Use a switch statement to map the enum constant to a Color object
        switch (supportedColor) {
            case RED:
                return Color.RED;
            case GREEN:
                return Color.GREEN;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case PINK:
                return Color.PINK;
            case GRAY:
                return Color.GRAY;
            case CYAN:
                return Color.CYAN;
            case ORANGE:
                return Color.ORANGE;
            default:
                throw new UnsupportedOperationException("Unsupported color: " + supportedColor);
        }
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of processes :");
        int numberOfProcesses = input.nextInt();
        System.out.println("Enter time quantum :");
        int timeQuantum = input.nextInt();
        System.out.println("Enter context switching time :");
        int contextSwitchingTime = input.nextInt();
        ArrayList<Process> sjfProcesses = new ArrayList<>(numberOfProcesses);
        ArrayList<Process> srtfProcesses = new ArrayList<>(numberOfProcesses);
        ArrayList<Process> priorityProcesses = new ArrayList<>(numberOfProcesses);
        ArrayList<Process> agProcesses = new ArrayList<>(numberOfProcesses);


        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("Enter process name :");
            String processName = input.next();
            System.out.println("Enter process color :");
            System.out.println("Supported colors are :");
            for (SupportedColor color : SupportedColor.values()) {
                System.out.print(color + " ");
            }
            System.out.println();
            Color processColor = new Main().getColor(input.next());
            System.out.println("Enter process arrival time:");
            int arrivalTime = input.nextInt();
            System.out.println("Enter process burst time:");
            int burstTime = input.nextInt();
            System.out.println("Enter process priority number:");
            int priorityNumber = input.nextInt();
            sjfProcesses.add(new Process(processName, arrivalTime, burstTime, priorityNumber, i, processColor));
            srtfProcesses.add(new Process(processName, arrivalTime, burstTime, priorityNumber, i, processColor));
            priorityProcesses.add(new Process(processName, arrivalTime, burstTime, priorityNumber, i, processColor));
            agProcesses.add(new Process(processName, arrivalTime, burstTime, priorityNumber, i, processColor));
        }

        System.out.println();
        System.out.println();
        System.out.println("SJF SCHEUDLING OUTPUT: "); 

        SJFScheduling sjfScheduler = new SJFScheduling(sjfProcesses, contextSwitchingTime);
        sjfScheduler.run();
        double sjfAverageWaitingTime = sjfScheduler.getAverageWaitingTime();
        double sjfAverageTurnaroundTime = sjfScheduler.getAverageTurnAroundTime();
        SwingUtilities.invokeLater(() -> {
            GUI chart = new GUI(sjfProcesses, sjfAverageWaitingTime, sjfAverageTurnaroundTime, "SJF", contextSwitchingTime);
            chart.setVisible(true);
        });

        System.out.println("=".repeat(100));
        
        System.out.println();
        System.out.println();
        System.out.println("SRTF SCHEUDLING OUTPUT: ");

        SRTFScheduling srtfScheduler = new SRTFScheduling(srtfProcesses);
        srtfScheduler.run();
        double srtfAverageWaitingTime = srtfScheduler.getAverageWaitingTime();
        double srtfAverageTurnaroundTime = srtfScheduler.getAverageTurnAroundTime();
        SwingUtilities.invokeLater(() -> {
            GUI chart2 = new GUI(srtfProcesses, srtfAverageWaitingTime, srtfAverageTurnaroundTime, "SRTF");
            chart2.setVisible(true);
        });

        System.out.println("=".repeat(100));

        System.out.println();
        System.out.println();
        System.out.println("Priority SCHEUDLING OUTPUT: ");
        

        PriorityScheduling priorityScheduler = new PriorityScheduling(priorityProcesses);
        priorityScheduler.run();
        double priorityAverageWaitingTime = priorityScheduler.getAverageWaitingTime();
        double priorityAverageTurnaroundTime = priorityScheduler.getAverageTurnAroundTime();
        SwingUtilities.invokeLater(() -> {
            GUI chart3 = new GUI(priorityProcesses, priorityAverageWaitingTime, priorityAverageTurnaroundTime, "Priority");
            chart3.setVisible(true);
        });

        System.out.println("=".repeat(100));

        System.out.println();
        System.out.println();
        System.out.println("AG SCHEUDLING OUTPUT: ");

        AGScheduling agScheduler = new AGScheduling(agProcesses, timeQuantum);
        agScheduler.run();
        double agAverageWaitingTime = agScheduler.getAverageWaitingTime();
        double agAverageTurnaroundTime = agScheduler.getAverageTurnAroundTime();
        SwingUtilities.invokeLater(() -> {
            GUI chart4 = new GUI(agProcesses, agAverageWaitingTime, agAverageTurnaroundTime, "AG", agScheduler.getQuantumHistory());
            chart4.setVisible(true);
        });

        System.out.println("=".repeat(100));

        // ArrayList<Process> processes = new ArrayList<>();
        // processes.add(new Process("P1", 0, 10, 1, 1,Color.gray));

        // processes.add(new Process("P2", 2, 3, 2, 2,Color.PINK));

        // processes.add(new Process("P3", 4, 1, 3, 3,Color.yellow));



        // Collections.sort(processes, Comparator.comparingInt(Process::getArrivalTime));

        // SJFScheduling scheduler = new SJFScheduling(processes, 1);

        // scheduler.run();
        // double averageWaitingTime = scheduler.getAverageWaitingTime();
        // double averageTurnaroundTime = scheduler.getAverageTurnAroundTime();
        // int contextSwitchingTime = 0;
        // SwingUtilities.invokeLater(() -> {
        //     GUI chart = new GUI(processes, averageWaitingTime, averageTurnaroundTime, "SRTF", 1);
        //     chart.setVisible(true);
        // });
    }
}



import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Scanner input = new Scanner(System.in);
        // int numberOfProcesses = input.nextInt();
        // int timeQuantum = input.nextInt();
        // int contextSwitchingTime = input.nextInt();
        // ArrayList<Process> processes = new ArrayList<>(numberOfProcesses);

        // for (int i = 0; i < numberOfProcesses; i++) {
        //     String processName = input.next();
        //     Color processColor = Color.getColor(input.next());
        //     int arrivalTime = input.nextInt();
        //     int burstTime = input.nextInt();
        //     int priorityNumber = input.nextInt();
        //     processes.add(new Process(processName, arrivalTime, burstTime, priorityNumber, i, processColor));
        // }

        // SJFScheduling sjfScheduler = new SJFScheduling(processes, contextSwitchingTime);
        // sjfScheduler.run();
        // double sjfAverageWaitingTime = sjfScheduler.getAverageWaitingTime();
        // double sjfAverageTurnaroundTime = sjfScheduler.getAverageTurnAroundTime();
        // SwingUtilities.invokeLater(() -> {
        //     GUI chart = new GUI(processes, sjfAverageWaitingTime, sjfAverageTurnaroundTime, "SJF", contextSwitchingTime);
        //     chart.setVisible(true);
        // });

        // SRTFScheduling srtfScheduler = new SRTFScheduling(processes);
        // srtfScheduler.run();
        // double srtfAverageWaitingTime = srtfScheduler.getAverageWaitingTime();
        // double srtfAverageTurnaroundTime = srtfScheduler.getAverageTurnAroundTime();
        // SwingUtilities.invokeLater(() -> {
        //     GUI chart2 = new GUI(processes, srtfAverageWaitingTime, srtfAverageTurnaroundTime, "SRTF");
        //     chart2.setVisible(true);
        // });

        // PriorityScheduling priorityScheduler = new PriorityScheduling(processes);
        // priorityScheduler.run();
        // double priorityAverageWaitingTime = priorityScheduler.getAverageWaitingTime();
        // double priorityAverageTurnaroundTime = priorityScheduler.getAverageTurnAroundTime();
        // SwingUtilities.invokeLater(() -> {
        //     GUI chart3 = new GUI(processes, priorityAverageWaitingTime, priorityAverageTurnaroundTime, "Priority");
        //     chart3.setVisible(true);
        // });

        // AGScheduling agScheduler = new AGScheduling(processes, timeQuantum);
        // agScheduler.run();
        // double agAverageWaitingTime = agScheduler.getAverageWaitingTime();
        // double agAverageTurnaroundTime = agScheduler.getAverageTurnAroundTime();
        // SwingUtilities.invokeLater(() -> {
        //     GUI chart4 = new GUI(processes, agAverageWaitingTime, agAverageTurnaroundTime, "AG", agScheduler.getQuantumHistory());
        //     chart4.setVisible(true);
        // });

        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 10, 1, 1,Color.gray));

        processes.add(new Process("P2", 2, 3, 2, 2,Color.PINK));

        processes.add(new Process("P3", 4, 1, 3, 3,Color.yellow));



        Collections.sort(processes, Comparator.comparingInt(Process::getArrivalTime));

        SJFScheduling scheduler = new SJFScheduling(processes, 1);

        scheduler.run();
        double averageWaitingTime = scheduler.getAverageWaitingTime();
        double averageTurnaroundTime = scheduler.getAverageTurnAroundTime();
        int contextSwitchingTime = 0;
        SwingUtilities.invokeLater(() -> {
            GUI chart = new GUI(processes, averageWaitingTime, averageTurnaroundTime, "SRTF", 1);
            chart.setVisible(true);
        });
    }
}


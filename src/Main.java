import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 4, 1, 1,Color.gray));

        processes.add(new Process("P2", 2, 3, 2, 2,Color.PINK));

        processes.add(new Process("P3", 4, 1, 3, 3,Color.yellow));



        Collections.sort(processes, Comparator.comparingInt(Process::getArrivalTime));

        SJFScheduling scheduler = new SJFScheduling(processes, 1);

        scheduler.run();
        double averageWaitingTime = scheduler.getAverageWaitingTime();
        double averageTurnaroundTime = scheduler.getAverageTurnAroundTime();
        int contextSwitchingTime = scheduler.contextSwitchTime;
        SwingUtilities.invokeLater(() -> {
            SJFSchedulingChart chart = new SJFSchedulingChart(processes, averageWaitingTime, averageTurnaroundTime, contextSwitchingTime);
            chart.setVisible(true);
        });
    }
}


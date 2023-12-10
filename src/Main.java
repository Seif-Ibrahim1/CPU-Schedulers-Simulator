import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scheduler scheduler;

        Scanner scanner = new Scanner(System.in);

        // Get the number of processes

        // Create an ArrayList to store the processes
        ArrayList<Process> processList = new ArrayList<>();



            // Create a new Process object and add it to the ArrayList
        Process process = new Process("p1", 0, 17, 4, 1);
        processList.add(process);

        Process process1 = new Process("p2", 3, 6, 9, 2);
        processList.add(process1);

        Process process2 = new Process("p3", 4, 10, 2, 3);
        processList.add(process2);

        Process process3 = new Process("p4", 29, 4, 8, 4);

        processList.add(process3);


        scheduler = new AGScheduling(processList , 4);
//        for (int i = 0 ; i<processList.size() ; i++) {
//            System.out.println("Process " + processList.get(i).getId() + ": " +
//                    "Name=" + processList.get(i).getName() +
//                    ", Arrival Time=" + processList.get(i).getArrivalTime() +
//                    ", Burst Time=" + processList.get(i).getBurstTime() +
//                    ", Priority=" + processList.get(i).getPriority());
//        }
        scheduler.run();

    }
}


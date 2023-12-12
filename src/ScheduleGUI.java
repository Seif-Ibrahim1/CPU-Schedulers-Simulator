import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ScheduleGUI {

    private JFrame window;
    private JPanel scheduleGraph;

    private JPanel statistics;

    private JPanel processesInformation;

    public ScheduleGUI() {
        initialize();

    }


    private void initialize(){
        window = new JFrame();
        window.setTitle("Process Display");
        window.setSize(1200, 700);  // size of the window
        window.setBackground(Color.LIGHT_GRAY);    // background color is Light gray
        window.getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // padding around the window


        window.setResizable(false); // the size of the window can not change while running
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when close exit from the compiler

        // Assuming you have an array of process names
        String[] processNames = {"Process1", "Process2", "Process3", "Process4", "Process5"};

        // Create a panel to hold the process rows
        scheduleGraph = new JPanel(new BorderLayout(5, 5));
        scheduleGraph.setPreferredSize(new Dimension(700 , 500)); // size of the section

        // create label for the CPU Scheduling Graph text

        JLabel scheduleLabel = new JLabel("CPU Scheduling Graph");
        scheduleLabel.setForeground(Color.RED); // text color
        scheduleLabel.setFont(scheduleLabel.getFont().deriveFont(Font.PLAIN, 16)); //font size
        scheduleLabel.setHorizontalAlignment(SwingConstants.LEFT); // put in the left

        // Create an empty border with right padding to add the next statement
        Border rightPaddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 500);
        scheduleLabel.setBorder(rightPaddingBorder);

        // Create the label for the Processes Information text

        JLabel processesInfoLabel = new JLabel("Processes Information");
        processesInfoLabel.setForeground(Color.RED); // text color
        processesInfoLabel.setFont(processesInfoLabel.getFont().deriveFont(Font.PLAIN, 16)); // font size
        processesInfoLabel.setHorizontalAlignment(SwingConstants.LEFT); //put in the left

        // Create a panel for both labels
        JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelsPanel.add(scheduleLabel);
        labelsPanel.add(processesInfoLabel);

        // Add the panel with labels to the NORTH region
        scheduleGraph.add(labelsPanel, BorderLayout.NORTH);



        // Create a panel for the process rows
        JPanel processRowsPanel = new JPanel(new GridLayout(processNames.length, 1, 0, 6));
        processRowsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // padding

        // Add rows for each process
        for (String processName : processNames) {
            JPanel processRow = createProcessRow(processName);
            processRowsPanel.add(processRow);
        }

        // Create a panel for the south section (statistics)
        statistics = new JPanel();
        statistics.setLayout(new BoxLayout(statistics, BoxLayout.Y_AXIS)); // BoxLayout to put each statement in separated line
        statistics.setPreferredSize(new Dimension(window.getWidth(), 100)); // Set height to 100
        statistics.setBackground(Color.LIGHT_GRAY); // Set background color

        // Add individual JLabel components for each line in the southPanel (statistics)
        JLabel statisticsLabel = new JLabel("<html><u>Statistics : </u></html>"); // to put underline
        statisticsLabel.setForeground(Color.RED); // text color
        statisticsLabel.setFont(statisticsLabel.getFont().deriveFont(Font.PLAIN, 16)); // font size

        // Add the custom label to the southPanel
        statistics.add(statisticsLabel);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 0, 10, 5); // padding
        statisticsLabel.setBorder(BorderFactory.createCompoundBorder(statisticsLabel.getBorder(), paddingBorder));

        // this information will change later

        statistics.add(new JLabel("AWT: 50", SwingConstants.LEFT));
        statistics.add(new JLabel("ATAT: 80", SwingConstants.LEFT));


        // Create a panel for the east section(processesInformation)
        processesInformation = new JPanel(new BorderLayout());
        processesInformation.setPreferredSize(new Dimension(500, window.getHeight())); // Set width to 500
        processesInformation.setBackground(Color.LIGHT_GRAY); // Set background color
        processesInformation.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding



        // Create a table to display process information
        String[] columnNames = {"PROCESS", "COLOR", "NAME", "PID", "PRIORITY"};

        // this information will change later
        String[][] obj = {{"1" , "red" , "p1" , "3018" , "120"}, {"2" , "red" , "p2" , "3050" , "120"} , {"3" , "red" , "p3" , "3044" , "120"}};



        // table to store the information of the all processes
        JTable processTable = new JTable(obj, columnNames);
        processTable.setBackground(Color.LIGHT_GRAY);

        // to show the table as there is no table

        processTable.setShowGrid(false);
        processTable.setIntercellSpacing(new Dimension(0, 0));

        processTable.setPreferredSize(new Dimension(500 , 462)); // size of the table

        // put the entry cell in the center of the cell
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        processTable.setDefaultRenderer(Object.class, centerRenderer);

        // to handle the header to show as there is no table

        JTableHeader tableHeader = processTable.getTableHeader();
        tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY)); // Border between column names
                label.setBackground(Color.LIGHT_GRAY);
                return label;
            }
        });


        // avoid reordering of the columns while running
        processTable.getTableHeader().setReorderingAllowed(false);


        // to allow the user to scroll
        processesInformation.add(new JScrollPane(processTable), BorderLayout.CENTER);

        // to allow the user to scroll

        scheduleGraph.add(new JScrollPane(processRowsPanel), BorderLayout.CENTER);
        // Add the panels to the main panel

        scheduleGraph.add(statistics, BorderLayout.SOUTH);
        scheduleGraph.add(processesInformation, BorderLayout.EAST);

        // Add the main panel to the frame
        window.add(scheduleGraph);

        window.setLocationRelativeTo(null); // Center the frame

    }
    private JPanel createProcessRow(String processName) {
        JPanel rowPanel = new JPanel(new BorderLayout());

        // Set the width of the row to 38 as the last time of the cpu
        rowPanel.setPreferredSize(new Dimension(380, 30));

        // Add the process name
        JLabel nameLabel = new JLabel(processName);
        rowPanel.add(nameLabel, BorderLayout.WEST);

        // Add a separator line between the process name and the rest of the row
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        rowPanel.add(separator, BorderLayout.CENTER);

        // Create a panel for alternating blocks of red color and this colors will change later
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 38, 0, 3)); // GridLayout for the colored blocks
        for (int i = 0; i < 38; i++) {
            JPanel block = new JPanel();
            if (i % 3 == 0) {
                block.setBackground(Color.RED);
            } else {
                block.setBackground(Color.GRAY);
            }
            block.setPreferredSize(new Dimension(10, 30)); // Adjust the width of each block as needed
            colorPanel.add(block);
        }

        // Add the panel with colored blocks to the center section
        rowPanel.add(colorPanel, BorderLayout.CENTER);

        return rowPanel;
    }


    public void show(){
        window.setVisible(true);
    }




}

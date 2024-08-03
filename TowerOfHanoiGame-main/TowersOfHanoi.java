package towersofhanoi;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Stack;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;

/*
 * TowersOfHanoi class represents the graphical user interface for solving the Tower of Hanoi problem.
 * It extends JFrame and implements ActionListener and Runnable interfaces.
 * The class incorporates both iterative and recursive algorithms to solve the Tower of Hanoi problem.
 */
public class TowersOfHanoi extends JFrame implements ActionListener,Runnable {

    /*
     * The main method of the TowersOfHanoi program.
     * It prompts the user to enter the number of disks,
     * creates an instance of the TowersOfHanoi class, and displays the graphical interface.
     */
    public static void main(String[] args) {
        // Prompt the user to enter the number of disks
        System.out.print("Please enter the number of disks you want: ");

        // Create an instance of the TowersOfHanoi class
        TowersOfHanoi toh = new TowersOfHanoi();

        // Make the graphical interface visible
        toh.setVisible(true);

    }

    /*
     * JComboBox used for selecting the algorithm type.
     * It allows the user to choose between "Iterative" and "Recursive" algorithms.
     */
    private JComboBox<String> algorithmSelector = new JComboBox<>(new String[]{"Iterative", "Recursive"});






    Scanner in = new Scanner(System.in);
    int n = in.nextInt();

    // GUI components and variables
    int frameWidth = 1800;
    int frameHeight = 1000;

    JButton start=new JButton("Start");
    JButton exit=new JButton("EXit");
    Rectangle []peg=new Rectangle[3];
    Rectangle []disk=new Rectangle[n];
    JLabel numberofMoves =new JLabel("Number of Moves : ");
    JLabel presentMove =new JLabel("Present Move : ");
    JLabel executionTimeLabel = new JLabel("Execution Time: ");
    JTextArea title=new JTextArea();


    private long startTime;
    private long endTime;

    int [][]peg_capacity=new int[3][n];
    int []h=new int[3];
    int num,count=1;

    /*
     * Moves a disk from the source peg to the destination peg.
     * - Maintains the number of disks in each peg.
     * - Calculates the horizontal displacement based on the source and destination pegs.
     * - Updates the position of the disk and repaints the GUI to reflect the updated state.
     * - Updates labels to display the move information.
     * - Delays the execution for better visualization using Thread.sleep.
     */
    private void moveDisk(int from, int dest) {
        try {
            // Keep track of the quantity of disks on each peg.
            peg_capacity[dest - 1][h[dest - 1]] = peg_capacity[from - 1][--h[from - 1]];

            int hor_displacement = 260;
            if ((from == 1 && dest == 2) || (from == 3 && dest == 2))
                hor_displacement = hor_displacement;
            else if ((from == 1 && dest == 3) || (from == 2 && dest == 3))
                hor_displacement = hor_displacement * 2;
            else if ((from == 3 && dest == 1) || (from == 2 && dest == 1))
                hor_displacement = 0;

            int num = peg_capacity[dest - 1][h[dest - 1]++];

            // Delay for better visualization
            Thread.sleep(1000);

            // Update the position of the disk
            disk[num].setLocation(150 + num * 12 + hor_displacement, 475 - (h[dest - 1] - 1) * 25);

            // Repaint the GUI to reflect the updated state
            repaint();

            // Update the labels to display the move information
            numberofMoves.setText("Number of Moves: " + (count++));
            presentMove.setText("Present Move: Disk " + (num + 1) + " moved from " + (char) (from + 64) + " --> " + (char) (dest + 64));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class to store information for each iteration in iterativeHanoi
    private static class IterationInfo {
        int diskCount;
        int from;
        int dest;
        int by;

        public IterationInfo(int diskCount, int from, int dest, int by) {
            this.diskCount = diskCount;
            this.from = from;
            this.dest = dest;
            this.by = by;
        }
    }

    // Thread to run the iterative Hanoi algorithm
    Thread t=new Thread(this);

    // Iterative algorithm for solving Tower of Hanoi
    public void iterativeHanoi(int diskCount, int from, int dest, int by) {
        Stack<IterationInfo> stack = new Stack<>();

        stack.push(new IterationInfo(diskCount, from, dest, by));

        while (!stack.isEmpty()) {
            IterationInfo current = stack.pop();

            if (current.diskCount == 1) {
                moveDisk(current.from, current.dest);
            } else {
                stack.push(new IterationInfo(current.diskCount - 1, current.by, current.dest, current.from));
                stack.push(new IterationInfo(1, current.from, current.dest, current.by));
                stack.push(new IterationInfo(current.diskCount - 1, current.from, current.by, current.dest));
            }
        }
    }

    // Run method is responsible for starting the iterative solution
    // This method is part of the Runnable interface and is implemented to execute the Tower of Hanoi algorithm in a separate thread.
    // In this specific implementation, it calls the iterativeHanoi method with initial parameters (n, 1, 3, 2) to solve the Tower of Hanoi iteratively.
    @Override
    public void run() {
        iterativeHanoi(n, 1, 3, 2);
    }

    private JTextField numberOfDisksInput = new JTextField();

    /*
     * Constructor for the TowersOfHanoi class.
     * Initializes peg heights, sets layout and frame size, and adds various components to the frame.
     * - Initializes peg heights based on user input.
     * - Sets layout to null and sets frame size using predefined constants.
     * - Sets the title of the frame to "Towers Of Hanoi".
     * - Adds an action listener to the "Start" button and the "Exit" button.
     * - Calculates and adjusts the size of each disk based on the number of disks.
     * - Creates a Rectangle object for each disk and pushes disk numbers into the first peg.
     * - Initializes pegs with Rectangle objects.
     */
    public TowersOfHanoi() {


        // Initialize peg heights
        h[0]=n;
        h[1]=0;
        h[2]=0;

        // Set layout and frame size
        setLayout(null);
        setSize(frameWidth, frameHeight );
        setTitle("Towers Of Hanoi ");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set bounds for execution time label
        executionTimeLabel.setBounds(100, 80, 300, 25);
        add(executionTimeLabel);

        // Set bounds for algorithm selector, start button, exit button, and labels
        algorithmSelector.setBounds(450, 520, 100, 25);
        start.setBounds(300, 520,100,25);
        exit.setBounds(600, 520,100,25);
        numberofMoves.setBounds(100,50,300,25);
        presentMove.setBounds(300,50,300,25);

        // Add components to the frame
        add(start);
        add(exit);
        add(numberofMoves);
        add(presentMove);
        add(algorithmSelector);






        // Adjust the size calculation for each disk
        for (int i = 0; i < n; i++) {
            // Calculate width and height based on the number of disks
            int width = 200 - i * (200 / n);
            int height = 25;

            // Calculate x-coordinate to center the disk
            int x = 150 + (i * 12) + ((200 - width) / 2);

            // Calculate y-coordinate to center the disk vertically
            int y = 475 - i * 25 + (25 - height) / 2;

            // Create a Rectangle object for each disk
            disk[i] = new Rectangle(x, y, width, height);
            peg_capacity[0][i] = i; // Pushing disk numbers into the first peg
        }










        // Peg initialization
        peg[0]=new Rectangle(250,200,15,300);
        peg[1]=new Rectangle(510,200,15,300);
        peg[2]=new Rectangle(760,200,15,300);

        // Add action listeners to buttons
        start.addActionListener(this);
        exit.addActionListener(this);

    }
    // Helper method to calculate the y-coordinate
    public int dy(int x){
        return frameHeight -x;
    }






    /*
     * Responds to user actions, particularly button clicks.
     * - If the "Start" button is clicked:
     *   - Attempts to execute the selected algorithm based on user input.
     *   - Handles potential NumberFormatException if an invalid input is provided.
     *   - Executes the chosen algorithm in a separate thread to prevent freezing the GUI.
     *   - Measures the performance of the algorithm and updates the GUI on the Event Dispatch Thread.
     * - If the "Exit" button is clicked:
     *   - Terminates the application.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // Check if the "Start" button is clicked
        if (ae.getSource() == start) {
            try {
                // No specific actions are taken before executing the selected algorithm
            } catch (NumberFormatException e) {
                // Handle the case where an invalid input is provided for the number of disks
                System.out.println("Invalid input. Please enter a valid number for the number of disks.");
                return;
            }
            // Get the selected algorithm from the JComboBox
            String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();

            // Execute the chosen algorithm in a separate thread
            if (selectedAlgorithm.equals("Iterative")) {
                new Thread(() -> {
                    // Measure the performance of the iterative Hanoi algorithm
                    measurePerformance(() -> iterativeHanoi(n, 1, 3, 2));
                    // Update the GUI on the Event Dispatch Thread after the algorithm completes
                    SwingUtilities.invokeLater(() -> repaint());
                }).start();
            } else if (selectedAlgorithm.equals("Recursive")) {
                new Thread(() -> {
                    // Measure the performance of the recursive Hanoi algorithm
                    measurePerformance(() -> recursiveHanoi(n, 1, 3, 2));
                    // Update the GUI on the Event Dispatch Thread after the algorithm completes
                    SwingUtilities.invokeLater(() -> repaint());
                }).start();
            }
        } else if (ae.getSource() == exit) {
            // Terminate the application if the "Exit" button is clicked
            System.exit(0);
        }
    }

    // Helper method to measure algorithm performance
    private void measurePerformance(Runnable algorithm) {
        // Reset move count
        count = 1;

        // Record start time
        startTime = System.nanoTime();

        // Run the selected algorithm
        algorithm.run();

        // Record end time
        endTime = System.nanoTime();

        // Calculate execution time in milliseconds
        long executionTime = (endTime - startTime) / 1_000_000;

        // Display results
        System.out.println("Execution Time: " + executionTime + " ms");
        System.out.println("Number of Moves: " + (count - 1)); // Subtract 1 to exclude the initial count


        // Set the text of the executionTimeLabel
        executionTimeLabel.setText("Execution Time: " + executionTime + " ms");

        // Additional analysis or comparisons can be done here
    }

    // Recursive algorithm
    public void recursiveHanoi(int diskCount,int from, int dest, int by)
    {
        if (diskCount==1)
        {   int hor_displacement=260;
            try
            {t.sleep(1000);

                //maintains number of disks in each peg
                peg_capacity[dest-1][h[dest-1]] =peg_capacity[from-1][--h[from-1]];

                if((from==1 && dest==2)|| (from==3 && dest==2))
                    hor_displacement=hor_displacement;
                else if((from==1 && dest==3)|| (from==2 && dest==3))
                    hor_displacement=hor_displacement*2;
                else if((from==3 && dest==1)||(from==2 && dest==1))
                    hor_displacement=0;


                num= peg_capacity[dest-1][h[dest-1]++];


                disk[num].setLocation(150+num*12+hor_displacement,475-(h[dest-1]-1)*25);

                repaint();
                numberofMoves.setText("Number of Moves :"+(count++));
                presentMove.setText("Present Move : Disk "+(num+1)+ " moved from "+(char)(from+64)+" --> "+(char)(dest+64));

            }
            catch(Exception e) {}


        }

        else
        {
            recursiveHanoi(diskCount -1, from, by, dest);
            recursiveHanoi(1, from, dest, by);
            recursiveHanoi(diskCount -1, by, dest, from);
        }
    }

    // Paint method is responsible for rendering the Towers of Hanoi game.
    // It draws the towers, labels them (A, B, C), draws a baseline for the towers, and represents the disks on the towers with round rectangles.
    // Each disk is labeled with a number indicating its size.
    public void paint(Graphics g){
        // Call the superclass paint method to ensure proper rendering
        super.paint(g);
        /// Set the color for the towers
        g.setColor(Color.DARK_GRAY);

        // Draw the three towers
        for(int i=0;i<3;i++)
            // Fill a rectangle to represent the tower
        {   g.fillRect(peg[i].x,peg[i].y,peg[i].width,peg[i].height);
            // Draw a label on top of each tower (A, B, C)
            g.drawString(""+(char)(i+65),peg[i].x+5,peg[i].y-10);
        }

        // Draw a base line for the towers
        g.drawLine(100,500, 850,500);



        // Drawing the disks
        for(int i=0;i<n;i++)
            // Set the color for the disks
        {   g.setColor(Color.CYAN);
            // Fill a round rectangle to represent the disk
            g.fillRoundRect(disk[i].x,disk[i].y,disk[i].width,disk[i].height,10,10);
            // Set color for the disk borders
            g.setColor(Color.BLACK);
            // Draw the borders of the round rectangle
            g.drawRoundRect(disk[i].x,disk[i].y,disk[i].width,disk[i].height,10,10);
            // Set color for the number on the disk
            g.setColor(Color.black);
            // Draw the number on the disk at its center
            g.drawString(Integer.toString(i + 1), disk[i].x + disk[i].width / 2 - 5, disk[i].y + disk[i].height / 2 + 5);


        }

    }








}

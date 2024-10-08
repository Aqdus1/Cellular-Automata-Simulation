import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellularAutomaton extends JFrame {

    private static final int GRID_SIZE = 80;
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
    private int[][] grid1 = new int[GRID_SIZE][GRID_SIZE];
    private int ruleNumber, base, updates;
    private int[][] initialMatrix;
    private GridPanel gridPanel;
    private int currentUpdate = 0;
    private Timer timer;

    public CellularAutomaton() {
        setTitle("Cellular Automaton");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2));

        JLabel ruleLabel = new JLabel("Enter the Rule:");
        JTextField ruleField = new JTextField();
        inputPanel.add(ruleLabel);
        inputPanel.add(ruleField);

        JLabel baseLabel = new JLabel("Enter the base or Z:");
        JTextField baseField = new JTextField();
        inputPanel.add(baseLabel);
        inputPanel.add(baseField);

        JLabel rowsLabel = new JLabel("Enter the number of rows:");
        JTextField rowsField = new JTextField();
        inputPanel.add(rowsLabel);
        inputPanel.add(rowsField);

        JLabel colsLabel = new JLabel("Enter the number of columns:");
        JTextField colsField = new JTextField();
        inputPanel.add(colsLabel);
        inputPanel.add(colsField);

        JLabel updatesLabel = new JLabel("Enter the number of updates:");
        JTextField updatesField = new JTextField();
        inputPanel.add(updatesLabel);
        inputPanel.add(updatesField);

        JButton enterButton = new JButton("Enter");
        inputPanel.add(enterButton);

        add(inputPanel, BorderLayout.NORTH);

        // Grid panel
        gridPanel = new GridPanel();
        add(gridPanel, BorderLayout.CENTER);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ruleNumber = Integer.parseInt(ruleField.getText());
                base = Integer.parseInt(baseField.getText());
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                updates = Integer.parseInt(updatesField.getText());
                initialMatrix = new int[rows][cols];

                // Prompt user to enter the elements of the matrix
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        initialMatrix[i][j] = Integer.parseInt(JOptionPane.showInputDialog(
                                "Enter element for [" + i + "][" + j + "]:"));
                    }
                }

                initializeGrid(rows, cols);
                startUpdates();
            }
        });
    }

    private void initializeGrid(int rows, int cols) {
        // Initialize the grid with zeros
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = 0;
                grid1[i][j] = 0;
            }
        }

        // Place the matrix in the central region of the grid
        int startRow = (GRID_SIZE - rows) / 2;
        int startCol = (GRID_SIZE - cols) / 2;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[startRow + i][startCol + j] = initialMatrix[i][j];
            }
        }
    }

    private int convert(int number, int base) {
        int num = number;
        int sum = 0;
        int rev = 0;
        while (num > 0) {
            int d = num % base;
            sum = sum * 10 + d;
            num = num / base;
        }
        while (sum > 0) {
            int r = sum % 10;
            rev = rev * 10 + r;
            sum = sum / 10;
        }
        return rev;
    }

    private void startUpdates() {
        int a = convert(ruleNumber, base);

        timer = new Timer(1000, new ActionListener() { // Adjust delay as needed
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentUpdate < updates) {
                    for (int i = 1; i < GRID_SIZE - 1; i++) {
                        for (int j = 1; j < GRID_SIZE - 1; j++) {
                            int c = 0;
                            int sum = 0;
                            int a1 = a;
                            while (a1 > 0) {
                                int r = a1 % 10;
                                if (c == 0) {
                                    sum += r * grid[i][j];
                                }
                                if (c == 1) {
                                    sum += r * grid[i][j + 1];
                                }
                                if (c == 2) {
                                    sum += r * grid[i + 1][j + 1];
                                }
                                if (c == 3) {
                                    sum += r * grid[i + 1][j];
                                }
                                if (c == 4) {
                                    sum += r * grid[i + 1][j - 1];
                                }
                                if (c == 5) {
                                    sum += r * grid[i][j - 1];
                                }
                                if (c == 6) {
                                    sum += r * grid[i - 1][j - 1];
                                }
                                if (c == 7) {
                                    sum += r * grid[i - 1][j];
                                }
                                if (c == 8) {
                                    sum += r * grid[i - 1][j + 1];
                                }
                                c++;
                                a1 = a1 / 10;
                            }
                            grid1[i][j] = sum % base;
                        }
                    }

                    // Update the original grid with the values from grid1
                    for (int i = 0; i < GRID_SIZE; i++) {
                        for (int j = 0; j < GRID_SIZE; j++) {
                            grid[i][j] = grid1[i][j];
                        }
                    }

                    // Repaint the grid panel to show the updates
                    gridPanel.repaint();

                    currentUpdate++;
                } else {
                    timer.stop();
                }
            }
        });

        timer.start();
    }

    class GridPanel extends JPanel {
        private JLabel stepLabel;

        public GridPanel() {
            setLayout(new BorderLayout());
            stepLabel = new JLabel("Starting", SwingConstants.CENTER);
            stepLabel.setFont(new Font("Serif", Font.BOLD, 24));
            stepLabel.setForeground(Color.WHITE);
            add(stepLabel, BorderLayout.NORTH);
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            stepLabel.setText("Step: " + currentUpdate);
            int cellSize = getWidth() / GRID_SIZE;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    switch (grid[i][j]) {
                        case 0:
                            g.setColor(Color.BLACK);
                            break;
                        case 1:
                            g.setColor(Color.WHITE);
                            break;
                        case 2:
                            g.setColor(Color.RED);
                            break;
                        case 3:
                            g.setColor(Color.BLUE);
                            break;
                        case 4:
                            g.setColor(Color.GREEN);
                            break;
                        case 5: 
                            g.setColor(Color.YELLOW);
                            break;
                        case 6:
                            g.setColor(Color.CYAN);
                            break;
                        case 7:
                            g.setColor(Color.PINK);
                            break;
                        case 8:
                            g.setColor(Color.ORANGE);
                            break;
                        case 9:
                            g.setColor(Color.LIGHT_GRAY);
                            break;
                        case 10:
                            g.setColor(Color.DARK_GRAY);
                            break;


                        default:
                            g.setColor(Color.MAGENTA);
                            break;
                    }
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CellularAutomaton automaton = new CellularAutomaton();
            automaton.setVisible(true);
        });
}
}
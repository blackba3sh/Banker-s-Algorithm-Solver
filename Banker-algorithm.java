import javax.swing.JOptionPane;

public class BankerAlgorithm {
    private int[][] allocation;
    private int[][] max;
    private int[][] need;
    private int[] available;
    private int numProcesses;
    private int numResources;

    public BankerAlgorithm(int[][] allocation, int[][] max, int[] available) {
        this.allocation = allocation;
        this.max = max;
        this.available = available;
        this.numProcesses = allocation.length;
        this.numResources = available.length;
        this.need = new int[numProcesses][numResources];

        // calculate need matrix
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                this.need[i][j] = this.max[i][j] - this.allocation[i][j];
            }
        }
    }

    public boolean isSafeState() {
        boolean[] finish = new boolean[numProcesses];
        int[] work = new int[numResources];
        System.arraycopy(available, 0, work, 0, numResources);

        int count = 0;
        while (count < numProcesses) {
            boolean found = false;
            for (int i = 0; i < numProcesses; i++) {
                if (!finish[i]) {
                    int j;
                    for (j = 0; j < numResources; j++) {
                        if (this.need[i][j] > work[j])
                            break;
                    }

                    if (j == numResources) {
                        for (int k = 0; k < numResources; k++)
                            work[k] += allocation[i][k];
                        finish[i] = true;
                        found = true;
                        count++;
                    }
                }
            }

            if (!found)
                return false;
        }

        return true;
    }

    public void requestResource(int processId, int[] request) {
        for (int j = 0; j < numResources; j++) {
            if (request[j] > this.need[processId][j]) {
                JOptionPane.showMessageDialog(null, "Error: Request exceeds maximum claim.");
                return;
            }
            if (request[j] > this.available[j]) {
                JOptionPane.showMessageDialog(null, "Error: Not enough resources available.");
                return;
            }
        }

        int[] tempAvailable = new int[numResources];
        System.arraycopy(this.available, 0, tempAvailable, 0, numResources);
        int[][] tempAllocation = new int[numProcesses][numResources];
        System.arraycopy(this.allocation, 0, tempAllocation, 0, numProcesses);
        int[][] tempNeed = new int[numProcesses][numResources];
        System.arraycopy(this.need, 0, tempNeed, 0, numProcesses);

        for (int j = 0; j < numResources; j++) {
            tempAvailable[j] -= request[j];
            tempAllocation[processId][j] += request[j];
            tempNeed[processId][j] -= request[j];
        }

        BankerAlgorithm tempBanker = new BankerAlgorithm(tempAllocation, this.max, tempAvailable);
        if (tempBanker.isSafeState()) {
            
            System.arraycopy(tempAvailable, 0, this.available, 0, numResources);
            System.arraycopy(tempAllocation, 0, this.allocation, 0, numProcesses);
            System.arraycopy(tempNeed, 0, this.need, 0, numProcesses);
            JOptionPane.showMessageDialog(null, "Success: Request granted.");
        } else {
            JOptionPane.showMessageDialog(null, "Error: Request would cause unsafe state (Deadlock). Request denied.");
        }
    }

    public static void main(String[] args) {
        int numProcesses = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of processes:"));
        int numResources = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of resources:"));

        int[][] allocation = new int[numProcesses][numResources];
        int[][] max = new int[numProcesses][numResources];
        int[] available = new int[numResources];

        for (int i = 0; i < numProcesses; i++) {
            String[] line = JOptionPane.showInputDialog(null, "Enter current allocation for process " + i).split("\\s+");
            for (int j = 0; j < numResources; j++)
                allocation[i][j] = Integer.parseInt(line[j]);
        }
    
        for (int i = 0; i < numProcesses; i++) {
            String[] line = JOptionPane.showInputDialog(null, "Enter maximum need for process " + i).split("\\s+");
            for (int j = 0; j <numResources ;j++)
                max[i][j] = Integer.parseInt(line[j]);
        }
        String[] line = JOptionPane.showInputDialog(null, "Enter available resources:").split("\\s+");
        for (int j = 0; j < numResources; j++)
            available[j] = Integer.parseInt(line[j]);

        BankerAlgorithm banker = new BankerAlgorithm(allocation, max, available);

        
        String menu = "Banker's Algorithm\n\n" +
                "1. Check if the system is in a safe state\n" +
                "2. Request resources for a process\n" +
                "3. Exit\n";

        boolean exit = false;
        while (!exit) {
            int choice = Integer.parseInt(JOptionPane.showInputDialog(null, menu));

            switch (choice) {
                case 1:
                    boolean isSafe = banker.isSafeState();
                    if (isSafe) {
                        JOptionPane.showMessageDialog(null, "The system is in a safe state.");
                    } else {
                        JOptionPane.showMessageDialog(null, "The system is in an unsafe state.");
                    }
                    break;
                case 2:
                    int processId = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the process ID:"));
                    line = JOptionPane.showInputDialog(null, "Enter the resource request:").split("\\s+");
                    int[] request = new int[numResources];
                    for (int j = 0; j < numResources; j++)
                        request[j] = Integer.parseInt(line[j]);

                    banker.requestResource(processId, request);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
                    break;
            }
        }
    }
}

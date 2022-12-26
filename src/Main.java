import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        // Open the input file
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\bey-s\\IdeaProjects\\c20170808047\\src\\jobs.txt"));

        // Create a list to hold the lines from the input file
        List<String> lines = new ArrayList<>();

        // Read each line from the input file and add it to the list
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        // Close the reader
        reader.close();

        // Create a LinkedHashMap to hold the data
        LinkedHashMap<Integer, Bursts> data = new LinkedHashMap<>();

        // Iterate through the lines in the list
        for (String s : lines) {
            // Use a regular expression to extract the first number and the tuples from each line
            Pattern p = Pattern.compile("(\\d+):(.*)");
            Matcher m = p.matcher(s);

            if (m.find()) {
                // Extract the first number (process ID)
                int processId = Integer.parseInt(m.group(1));

                // Extract the tuples
                String[] tuples = m.group(2).split(";");

                // Create a new Bursts object to hold the CPU burst and I/O burst lengths for this process
                Bursts bursts = new Bursts();

                // Iterate through the tuples
                for (String tuple : tuples) {
                    // Remove the leading and trailing parentheses from the tuple
                    tuple = tuple.replace("(", "").replace(")", "");

                    // Split the tuple into the CPU burst and I/O burst lengths
                    String[] lengths = tuple.split(",");
                    int cpuBurst = Integer.parseInt(lengths[0]);
                    int ioBurst = Integer.parseInt(lengths[1]);

                    // Add the CPU burst and I/O burst lengths to the Bursts object
                    bursts.addBurst(cpuBurst, ioBurst);
                }

                // Add the process ID and burst lengths to the data LinkedHashMap
                data.put(processId, bursts);
            }
        }

        // Print the data
        for (Integer processId : data.keySet()) {
            System.out.println("Process ID: " + processId);
            System.out.println("CPU Bursts: " + data.get(processId).getCpuBursts());
            System.out.println("I/O Bursts: " + data.get(processId).getIoBursts());
            System.out.println();
        }

//        // returns processID's
//        System.out.println("\n" + data.keySet());
//
//        // returns the first processID in the list
//        System.out.println("\n" + data.keySet().toArray()[0]);
//
//        // returns the first processes first CPU burst
//        System.out.println("\n" + data.get(346).getCpuBursts().get(0));
//
//        // returns the first processes first I/O burst
//        System.out.println("\n" + data.get(346).getIoBursts().get(0));
//
//        // returns cpuBursts for processID 346
//        System.out.println(data.get(346).getCpuBursts());
//        System.out.println(data.get(346).getIoBursts());
//
//        // To add the first processes first CPU burst and I/O burst.
//        System.out.println(data.get(346).getCpuBursts().get(0) + data.get(346).getIoBursts().get(0));
//
//        Queue<Integer> queue = new LinkedList<>();
//        int removedCpuBurst = data.get(346).getCpuBursts().remove(0);
//        int removedCpuBurst2 = data.get(2547).getCpuBursts().remove(0);
//        int removedCpuBurst3 = data.get(49).getCpuBursts().remove(0);
//        queue.add(removedCpuBurst);
//        System.out.println(queue);
//        queue.add(removedCpuBurst2);
//        queue.add(removedCpuBurst3);
//        System.out.println(queue);
//        queue.remove(15);
//        System.out.println(queue);

//
//        // Print data size
//        System.out.println(data.size());

        //runProcesses(data);
    }

    public static void runProcesses(LinkedHashMap<Integer, Bursts> data) {
        // Initialize variables to keep track of statistics
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        int idleCount = 0;
        int numOfProcesses = data.size();

        // Initialize a list of ready processes
        List<Integer> readyProcesses = new ArrayList<>();

        // Initialize the current time to 0
        int current = 0;

        // Initialize the I/O device queue
        Queue<Integer> ioQueue = new LinkedList<>();

        // Initialize the IDLE process
        int idleProcess = 0;

        // Keep running until the data LinkedHashMap is empty
        while (!data.isEmpty()) {
            // Add any processes that have arrived to the ready processes list
            for (Integer processId : data.keySet()) {
                if (data.get(processId).getArrivalTime() == current) {
                    readyProcesses.add(processId);
                }
            }

            // If there are no ready processes, add the IDLE process to the ready processes list
            if (readyProcesses.isEmpty()) {
                readyProcesses.add(idleProcess);
            }

            // Get the next process from the ready processes list
            int processId = readyProcesses.remove(0);

            // If the process is the IDLE process, increment the idle count
            if (processId == idleProcess) {
                idleCount++;
            } else {
                // Get the next CPU burst for the process
                int cpuBurst = data.get(processId).getCpuBursts().remove(0);

                // Get the next I/O burst for the process
                int ioBurst = data.get(processId).getIoBursts().remove(0);

                // Add the I/O burst to the I/O device queue
                ioQueue.add(ioBurst);

                // Increment the current time by the CPU burst
                current += cpuBurst;

                // If the process has no more CPU bursts, remove it from the data LinkedHashMap
                if (data.get(processId).getCpuBursts().isEmpty()) {
                    data.remove(processId);
                } else {
                    // Add the process back to the ready processes list
                    readyProcesses.add(processId);
                }
            }

            // Increment the current time by the I/O burst
            current += ioQueue.remove();
        }
    }
}

class Bursts {
    private List<Integer> cpuBursts;
    private List<Integer> ioBursts;

    public Bursts() {
        this.cpuBursts = new ArrayList<>();
        this.ioBursts = new ArrayList<>();
    }

    public void addBurst(int cpuBurst, int ioBurst) {
        this.cpuBursts.add(cpuBurst);
        this.ioBursts.add(ioBurst);
    }

    public int getNthBurstsAddition(int n) {
        return cpuBursts.get(n) + ioBursts.get(n);
    }

    public List<Integer> getCpuBursts() {
        return this.cpuBursts;
    }

    public List<Integer> getIoBursts() {
        return this.ioBursts;
    }
}

class Process{
    private int processId;
    private int arrivalTime;
    private Bursts bursts;

    public Process(int processId, int arrivalTime, int cpuBurst, int ioBurst) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
    }

    public int getProcessId() {
        return processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
}

//    public static void runProcesses(LinkedHashMap<Integer, Bursts> data) {
//        // Initialize variables to keep track of statistics
//        int totalTurnaroundTime = 0;
//        int totalWaitingTime = 0;
//        int idleCount = 0;
//        int numOfProcesses = data.size();
//
//        // Initialize a list of ready processes
//        List<Integer> readyProcesses = new ArrayList<>();
//
//        // Initialize the current time to 0
//        int current = 0;
//
//        // Initialize the I/O device queue
//        Queue<Integer> ioQueue = new LinkedList<>();
//
//        // Initialize the IDLE process
//        int idleProcess = 0;
//
//        // Keep running until all processes have completed
//        while (!data.isEmpty()) {
//            // Add all processes that have arrived at the current time to the list of ready processes
//            for (int processId : data.keySet()) {
//                Bursts bursts = data.get(processId);
//                if (bursts.getCpuBursts().get(0) == current) {
//                    readyProcesses.add(processId);
//                }
//            }
//
//            // Print the ready processes
//            System.out.println("Ready Processes: " + readyProcesses.get(0).toString());
//
//            // Check if the readyProcesses list is empty
//            if (readyProcesses.isEmpty()) {
//                // If the list is empty, execute the IDLE process and increment the idle count
//                readyProcesses.add(idleProcess);
//                idleCount++;
//            } else {
//                // Select the next process to run based on the FCFS algorithm (first process in the list of ready processes)
//                int currentProcess = readyProcesses.remove(0);
//
//                // Increase the current by the length of the current CPU burst
//                int currentCpuBurst = data.get(currentProcess).getCpuBursts().remove(0);
//                current += currentCpuBurst;
//
//                // If the current I/O burst is not -1, add the current process to the I/O device queue and set the process state to WAITING
//                int currentIoBurst = data.get(currentProcess).getIoBursts().remove(0);
//                if (currentIoBurst != -1) {
//                    ioQueue.add(currentProcess);
//                }
//
//                // If the I/O device queue is not empty, remove the next process from the queue and set its state to READY
//                if (!ioQueue.isEmpty()) {
//                    int nextProcess = ioQueue.remove();
//                    readyProcesses.add(nextProcess);
//                } else if (readyProcesses.isEmpty()) {
//                    // If the I/O device queue is empty and there are no other processes ready to run, execute the IDLE process and increment the idle count
//                    readyProcesses.add(idleProcess);
//                    idleCount++;
//                }
//
//                // If the current process has completed all of its CPU bursts and I/O bursts, set its state to TERMINATED and calculate its turnaround current,
//                // waiting current.
//                if (data.get(currentProcess).getCpuBursts().isEmpty() && data.get(currentProcess).getIoBursts().isEmpty()) {
//                    data.remove(currentProcess);
//
//                    int turnaroundTime = current - currentCpuBurst;
//                    int waitingTime = turnaroundTime - currentCpuBurst;
//                    int responseTime = waitingTime;
//
//                    totalTurnaroundTime += turnaroundTime;
//                    totalWaitingTime += waitingTime;
//                }
//            }
//        }
//
//        // Calculate the average turnaround current, average waiting current, and number of times the IDLE process was executed
//        double avgTurnaroundTime = (double) totalTurnaroundTime / numOfProcesses;
//        double avgWaitingTime = (double) totalWaitingTime / numOfProcesses;
//
//        // Print the results
//        System.out.println("Average turnaround current: " + avgTurnaroundTime);
//        System.out.println("Average waiting current: " + avgWaitingTime);
//        System.out.println("Number of times IDLE was executed: " + idleCount);
//        System.out.println("HALT");
//    }


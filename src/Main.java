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
//        for (Integer processId : data.keySet()) {
//            System.out.println("Process ID: " + processId);
//            System.out.println("CPU Bursts: " + data.get(processId).getCpuBursts());
//            System.out.println("I/O Bursts: " + data.get(processId).getIoBursts());
//            System.out.println();
//        }

        //data.get(346).returnTime = data.get(346).getCpuBursts().get(0);
        //System.out.println("Return time of the process " + data.keySet().toArray()[0] + "'s first cpu burst is " + data.get(346).returnTime);

        //System.out.println("Process " + data.keySet().toArray()[0] + "'s return time will be " + data.get(346).getNthBurstsAddition(0));

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

        System.out.println("\nTable:");
        System.out.println("Current " + "\t\t\t" + " PID " + "\t\t\t" + " Tuple " + "\t\t\t" + " Return ");
        runProcesses(data);
    }

    public static void runProcesses(LinkedHashMap<Integer, Bursts> data) {

        // Initialize variables to keep track of statistics
        double totalTerminateTime = 0;
        double totalCpuBurstTime = 0;
        int idleCount = 0;
        int numOfProcesses = data.size();

        // Initialize a queue of ready processes

        // Initialize a queue of processes that are currently running
        Queue<Integer> runningQueue = new LinkedList<>();

        // Initialize a list of processes that have terminated
        List<Integer> terminatedList = new ArrayList<>();

        // Initialize the current time to 0
        int current = 0;

        // Add all processes to the ready queue at time 0
        Queue<Integer> readyQueue = new LinkedList<>(data.keySet());

        int smallestReturnTime = Integer.MAX_VALUE;

        // Keep running until the data LinkedHashMap is empty
        while (data.size() != terminatedList.size()) {

            // If there are no processes running and the ready queue is not empty, run the next process
            while (!readyQueue.isEmpty()){
                // Set the ready process's return time to its first CPU burst length plus first I/O burst length
                if (data.get(readyQueue.peek()).getIoBursts().get(0) == -1){
                    data.get(readyQueue.peek()).returnTime = current + data.get(readyQueue.peek()).getCpuBursts().get(0);
                    totalTerminateTime += data.get(readyQueue.peek()).returnTime;
                    //System.out.println("Process " + readyQueue.peek() + " terminated at time " + data.get(readyQueue.peek()).returnTime);
                    //System.out.println("totalTerminateTime: " + totalTerminateTime);

                    // Print the current time, process ID, tuple, and return time
                    System.out.println(current + "\t\t\t\t\t  " + readyQueue.peek() + "\t\t\t\t" + "  " + data.get(readyQueue.peek()).getCpuBursts().get(0) + "," + data.get(readyQueue.peek()).getIoBursts().get(0) + "            " + data.get(readyQueue.peek()).returnTime);

                    // Add running process's cpu burst length to the current time
                    current += data.get(readyQueue.peek()).getCpuBursts().get(0);
                    totalCpuBurstTime += data.get(readyQueue.peek()).getCpuBursts().get(0);

                    // Remove the running process's first CPU burst length and I/O burst length from the list
                    data.get(readyQueue.peek()).getCpuBursts().remove(0);
                    data.get(readyQueue.peek()).getIoBursts().remove(0);

                    terminatedList.add(readyQueue.remove());
                    continue;
                } else {
                    data.get(readyQueue.peek()).returnTime = current + data.get(readyQueue.peek()).getNthBurstsAddition(0);

                    // Print the current time, process ID, tuple, and return time
                    System.out.println(current + "\t\t\t\t\t  " + readyQueue.peek() + "\t\t\t\t" + "  " + data.get(readyQueue.peek()).getCpuBursts().get(0) + "," + data.get(readyQueue.peek()).getIoBursts().get(0) + "            " + data.get(readyQueue.peek()).returnTime);

                }

                // Add running process's cpu burst length to the current time
                current += data.get(readyQueue.peek()).getCpuBursts().get(0);
                totalCpuBurstTime += data.get(readyQueue.peek()).getCpuBursts().get(0);

                // Remove the running process's first CPU burst length and I/O burst length from the list
                data.get(readyQueue.peek()).getCpuBursts().remove(0);
                data.get(readyQueue.peek()).getIoBursts().remove(0);

                runningQueue.add(readyQueue.remove());
            }

            // If the return time of the processes in the running queue is less than or equal to the current time,
            // add them to the ready queue and remove them from the running queue
            Iterator<Integer> iterator = runningQueue.iterator();
            while (iterator.hasNext()) {
                Integer processId = iterator.next();
                if (data.get(processId).returnTime <= current) {
                    readyQueue.add(processId);
                    iterator.remove();
                }
            }

            // If the running queue is not empty and the ready queue is empty
            if(!runningQueue.isEmpty() && readyQueue.isEmpty()) {

                // Find the process with the smallest return time inside the running queue
                for (Integer processId : runningQueue) {
                    if (data.get(processId).returnTime < smallestReturnTime) {
                        // And set the smallest return time to that process's return time
                        smallestReturnTime = data.get(processId).returnTime;
                    }
                }
                // Increase the idle count by 1
                idleCount++;

                System.out.println(current + "\t\t\t\t\t  " + "IDLE" + "\t\t\t" + "      " + (smallestReturnTime - current) + ",0" + "            " + smallestReturnTime);
                // Set the current time to the smallest return time
                current = smallestReturnTime;
            }
            smallestReturnTime = Integer.MAX_VALUE;
        }

        System.out.println("\nAverage turnaround time: " + (totalTerminateTime / numOfProcesses));
        System.out.println("Average waiting time: " + ((totalTerminateTime - totalCpuBurstTime) / numOfProcesses));
        System.out.println("Number of times that the IDLE process runs: " + idleCount);
        System.out.println("HALT");
    }
}

class Bursts {
    public int returnTime = 0;
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


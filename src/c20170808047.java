import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class c20170808047 {
    public static void main(String[] args) throws Exception {

        String fileName = args[0];

        // Get the path to the file that is given as input
        String path = c20170808047.class.getResource
        (fileName).getPath();

        // Open the input file
        BufferedReader reader = new BufferedReader(new FileReader(path));

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

        // Sort the data LinkedHashMap by process ID
        LinkedHashMap<Integer, Bursts> sortedData = new LinkedHashMap<>();
        data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedData.put(x.getKey(), x.getValue()));

        System.out.println("\nTable:");
        System.out.println("Current " + "\t\t\t" + " PID " + "\t\t\t" + " Tuple " + "\t\t\t" + " Return ");
        runProcesses(sortedData);
    }

    // Method to run the processes in the FCFS algorithm with I/O bursts
    public static void runProcesses(LinkedHashMap<Integer, Bursts> data) {

        // Initialize variables to keep track of statistics
        double totalTerminateTime = 0;
        double totalCpuBurstTime = 0;
        int idleCount = 0;
        int numOfProcesses = data.size();

        // Initialize a queue of processes that are currently running
        Queue<Integer> runningQueue = new LinkedList<>();

        // Initialize a list of processes that have terminated
        List<Integer> terminatedList = new ArrayList<>();

        // Initialize the current time to 0
        int current = 0;

        // Add all processes to the ready queue at time 0
        Queue<Integer> readyQueue = new LinkedList<>(data.keySet());

        int smallestReturnTime = Integer.MAX_VALUE;

        // Keep running until all processes have terminated
        while (data.size() != terminatedList.size()) {

            // Transfer processes from the ready queue to the running queue
            while (!readyQueue.isEmpty()){
                // If the process is going to run its last Bursts, remove it from the ready queue and add it to the terminated list
                if (data.get(readyQueue.peek()).getIoBursts().get(0) == -1){
                    // Set the return time of the process
                    data.get(readyQueue.peek()).returnTime = current + data.get(readyQueue.peek()).getCpuBursts().get(0);
                    totalTerminateTime += data.get(readyQueue.peek()).returnTime;

                    // Print the current time, process ID, tuple, and return time
                    System.out.println(current + "\t\t\t\t\t  " + readyQueue.peek() + "\t\t\t\t" + "  " + data.get(readyQueue.peek()).getCpuBursts().get(0)
                            + "," + data.get(readyQueue.peek()).getIoBursts().get(0) + "            " + data.get(readyQueue.peek()).returnTime);

                    // Add running process's cpu burst length to the current time
                    current += data.get(readyQueue.peek()).getCpuBursts().get(0);
                    // Add the process's cpu burst length to the total cpu burst time
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

                current += data.get(readyQueue.peek()).getCpuBursts().get(0);
                totalCpuBurstTime += data.get(readyQueue.peek()).getCpuBursts().get(0);

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

                // Print the current time, IDLE process, tuple, and return time
                System.out.println(current + "\t\t\t\t\t  " + "IDLE" + "\t\t\t" + "      " + (smallestReturnTime - current) + ",0" + "            " + smallestReturnTime);
                // Set the current time to the smallest return time
                current = smallestReturnTime;
            }
            // Reset the smallest return time to the max value
            smallestReturnTime = Integer.MAX_VALUE;
        }

        // Print the statistics
        System.out.println("\nAverage turnaround time: " + (totalTerminateTime / numOfProcesses));
        System.out.println("Average waiting time: " + ((totalTerminateTime - totalCpuBurstTime) / numOfProcesses));
        System.out.println("Number of times that the IDLE process runs: " + idleCount);
        System.out.println("HALT");
    }
}

// Class to hold the CPU and I/O burst lengths and the return time of a process
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


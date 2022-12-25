import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        // Read the input file
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

        // Iterate through the lines in the list
        LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> data = new LinkedHashMap<>();

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

                // Create a new LinkedHashMap to hold the CPU burst and I/O burst lengths for this process
                LinkedHashMap<Integer, Integer> bursts = new LinkedHashMap<>();

                // Iterate through the tuples
                for (String tuple : tuples) {
                    // Remove the leading and trailing parentheses from the tuple
                    tuple = tuple.replace("(", "").replace(")", "");

                    // Split the tuple into the CPU burst and I/O burst lengths
                    String[] lengths = tuple.split(",");
                    int cpuBurst = Integer.parseInt(lengths[0]);
                    int ioBurst = Integer.parseInt(lengths[1]);

                    // Add the CPU burst and I/O burst lengths to the LinkedHashMap
                    bursts.put(cpuBurst, ioBurst);
                }

                // Add the process ID and burst lengths to the data LinkedHashMap
                data.put(processId, bursts);
            }
        }

        // Print the contents of the data LinkedHashMap
        for (int i = 0; i < data.size(); i++) {
            System.out.println("Process ID: " + data.keySet().toArray()[i]);
            System.out.println("CPU Burst Lengths: " + Arrays.toString(data.get(data.keySet().toArray()[i]).keySet().toArray()));
            System.out.println("I/O Burst Lengths: " + Arrays.toString(data.get(data.keySet().toArray()[i]).values().toArray()));
            System.out.println();
        }

        System.out.println("Process ID: " + data.keySet().toArray()[0]);
        System.out.println("CPU Burst Lengths: " + data.get(data.keySet().toArray()[0]).keySet().toArray()[0]);
        System.out.println("I/O Burst Lengths: " + data.get(data.keySet().toArray()[0]).values().toArray()[0]);

        ArrayList<Integer> processIds = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            processIds.add((Integer) data.keySet().toArray()[i]);
        }

        ArrayList<Integer> cpuBursts = new ArrayList<>();
        for (int i = 0; i < data.get(data.keySet().toArray()[0]).keySet().toArray().length; i++) {
            cpuBursts.add((Integer) data.get(data.keySet().toArray()[0]).keySet().toArray()[i]);
        }

        ArrayList<Integer> ioBursts = new ArrayList<>();
        for (int i = 0; i < data.get(data.keySet().toArray()[0]).values().toArray().length; i++) {
            ioBursts.add((Integer) data.get(data.keySet().toArray()[0]).values().toArray()[i]);
        }

        System.out.println(processIds);
        System.out.println(cpuBursts);
        System.out.println(ioBursts);

        FCFSwithIO(data);
    }

    public static void FCFSwithIO(LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> data) {

    }


}

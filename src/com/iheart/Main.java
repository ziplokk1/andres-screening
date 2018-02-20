package com.iheart;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    /**
     * The source directory containing all csv files.
     */
    private final String source;

    /**
     * Constructor.
     *
     * @param source The source directory containing all csv files.
     */
    public Main(String source) {
        this.source = source;
    }

    /**
     * Solutions to the screening problem.
     *
     * @throws IOException
     */
    private void run() throws IOException {
        List<String> csvFilePaths = new ArrayList<String>();
        // populate csvFilePaths
        findCsvFiles(this.source, csvFilePaths);

        // Solve average number of columns
        int averageColumns = this.averageColumns(csvFilePaths);
        System.out.print("Average Number Of Fields Across All CSV Files: ");
        System.out.println(averageColumns);

        // Solve total number of rows
        int numberOfRows = this.numberOfRows(csvFilePaths);
        System.out.print("Total Number Of Rows Across All CSV Files: ");
        System.out.println(numberOfRows);

        Map<String, Integer> wordCounts = this.wordCount(csvFilePaths);
        System.out.println("Frequency Of Words Across All CSV Files: ");
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            System.out.print(entry.getKey());
            System.out.print(",");
            System.out.println(entry.getValue());
        }
    }

    private Map<String, Integer> wordCount(List<String> csvFilePaths) throws IOException {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String path : csvFilePaths) {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[\"]", "");
                String[] values = line.split("[\\s+,]");
                for (String value : values) {
                    int count = map.containsKey(value) ? map.get(value) : 0;
                    map.put(value, count+1);
                }
            }
            br.close();
        }
        return map;
    }

    /**
     * Get the total number of rows from all csv files.
     *
     * @param csvFilePaths An array list containing absolute paths to the target csv files.
     * @return An integer of the total number of rows in all csv files in the provided list.
     * @throws IOException
     */
    private int numberOfRows(List<String> csvFilePaths) throws IOException {
        int rowSum = 0;
        for (String path : csvFilePaths) {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = br.readLine()) != null) {
                rowSum += 1;
            }
            br.close();
        }
        return rowSum;
    }

    /**
     * Get the average number of columns across all csv files.
     *
     * @param csvFilePaths An array list containing absolute paths to the target csv files.
     * @return An integer of the average number of columns across all csv files in the provided list.
     * @throws IOException
     */
    private int averageColumns(List<String> csvFilePaths) throws IOException {
        int columnSum = 0;
        int numFiles = csvFilePaths.size();

        for (String path : csvFilePaths) {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = br.readLine()) != null) {
                columnSum += line.split(",").length;
                break;
            }
            br.close();
        }
        return columnSum / numFiles;
    }

    /**
     * Recursively search through a directory and append all csv files to a list.
     *
     * @param dir    The directory to recursively search for csv files.
     * @param output The list to append paths to the csv files in the directory.
     */
    private void findCsvFiles(String dir, List<String> output) {
        File folder = new File(dir);
        File[] files = folder.listFiles();
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (file.isFile() && path.endsWith(".csv")) {
                output.add(path);
            }
            if (file.isDirectory()) {
                findCsvFiles(path, output);
            }
        }
    }

    /**
     * Main
     *
     * @param args       Cmd line arguments. args[0] is the target folder containing all csv files.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String source;
        try {
            source = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Exception("Argument 0 must be the source folder containing all csv files.");
        }
        Main main = new Main(source);
        main.run();
    }
}
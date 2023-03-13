package filemergesort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    static String sortType = "-a";

    public static void main(String[] args) {

        if (args.length < 3) {
            System.err.println("Not enough arguments!");
            System.exit(0);
        }

        switch (args[0]) {
            case "-a" -> {
                if (args[1].equals("-s")) {
                    stringMergeSort(args[2], Arrays.copyOfRange(args, 3, args.length));
                } else if (args[1].equals("-i")) {
                    intMergeSort(args[2], Arrays.copyOfRange(args, 3, args.length));
                }
            }
            case "-d" -> {
                sortType = "-d";
                if (args[1].equals("-s")) {
                    stringMergeSort(args[2], Arrays.copyOfRange(args, 3, args.length));
                } else if (args[1].equals("-i")) {
                    intMergeSort(args[2], Arrays.copyOfRange(args, 3, args.length));
                }
            }
            case "-s" -> stringMergeSort(args[1], Arrays.copyOfRange(args, 2, args.length));

            case "-i" -> intMergeSort(args[1], Arrays.copyOfRange(args, 2, args.length));

            default -> {
                System.err.println("Invalid arguments!");
                System.exit(0);
            }
        }
    }

    public static void fileWrite(String outputFile, int[] array) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));

            for (int currentNum : array) {
                writer.write(Integer.toString(currentNum));
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Unable to write output file");
        }

    }

    public static void fileWrite(String outputFile, String[] array) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            for (String currentString : array) {
                writer.write(currentString);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Unable to write output file");
        }

    }

    private static void intMergeSort(String outputFile, String[] inputFiles) {
        ArrayList<Integer> intsList = new ArrayList<>();

        for (String inputFile : inputFiles) {
            Path path = Paths.get(inputFile);

            BufferedReader reader = null;
            try {
                reader = Files.newBufferedReader(path);

                String line = reader.readLine();
                while (line != null && line.matches("\\d+")) {
                    intsList.add(Integer.parseInt(line));
                    line = reader.readLine();
                    if (line != null && !line.matches("\\d+"))
                        System.err.println("Invalid input string '" + line + "'. File type may be mismatched or file is corrupted");
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("File " + inputFile + " not found");
            }
        }

        int[] ints = intsList.stream().mapToInt(i -> i).toArray();
        mergeSort(ints);
        fileWrite(outputFile, ints);
    }

    private static void stringMergeSort(String outputFile, String[] inputFiles) {
        ArrayList<String> stringsList = new ArrayList<>();
        for (String inputFile : inputFiles) {
            Path path = Paths.get(inputFile);

            BufferedReader reader = null;
            try {
                reader = Files.newBufferedReader(path);

                String line = reader.readLine();
                while (line != null) {
                    stringsList.add(line);
                    line = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
                System.err.println("File " + inputFile + " not found");
            }
        }

        String[] strings = stringsList.toArray(new String[0]);

        mergeSort(strings);
        fileWrite(outputFile, strings);
    }

    private static void mergeSort(String[] array) {
        if (array.length < 2) return;

        int middle = array.length / 2;
        String[] left;
        String[] right;

        left = Arrays.copyOfRange(array, 0, middle);
        right = Arrays.copyOfRange(array, middle, array.length);

        mergeSort(left);
        mergeSort(right);

        merge(array, left, right);
    }

    private static void mergeSort(int[] array) {
        if (array.length < 2) return;

        int middle = array.length / 2;
        int[] left;
        int[] right;

        left = Arrays.copyOfRange(array, 0, middle);
        right = Arrays.copyOfRange(array, middle, array.length);

        mergeSort(left);
        mergeSort(right);

        merge(array, left, right);
    }

    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0;
        int j = 0;
        int k = 0;

        if (sortType.equals("-a")) {         //Ascending ints sort
            while (i < left.length && j < right.length) {
                if (left[i] <= right[j])
                    array[k++] = left[i++];
                else
                    array[k++] = right[j++];
            }
        } else if (sortType.equals("-d")) {         //Descending ints sort
            while (i < left.length && j < right.length) {
                if (left[i] > right[j])
                    array[k++] = left[i++];
                else
                    array[k++] = right[j++];
            }
        }

        while (i < left.length)
            array[k++] = left[i++];

        while (j < right.length)
            array[k++] = right[j++];
    }

    private static void merge(String[] array, String[] left, String[] right) {
        int i = 0;
        int j = 0;
        int k = 0;

        if (sortType.equals("-a")) {        //Ascending string sort
            while (i < left.length && j < right.length) {
                if (left[i].compareTo(right[j]) <= 0)
                    array[k++] = left[i++];
                else
                    array[k++] = right[j++];
            }
        } else if (sortType.equals("-d")) {         //Descending string sort
            while (i < left.length && j < right.length) {
                if (left[i].compareTo(right[j]) > 0)
                    array[k++] = left[i++];
                else
                    array[k++] = right[j++];
            }
        }

        while (i < left.length)
            array[k++] = left[i++];

        while (j < right.length)
            array[k++] = right[j++];
    }
}

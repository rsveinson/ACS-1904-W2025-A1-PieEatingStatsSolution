import java.io.*;
import java.util.*;

public class A1W2025ChatGPTCheatCode {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Constants for array sizes
        final int PIE_TIMES = 10;
        final int PIE_VOLUMES = 3;
        
        // Data storage
        List<String> names = new ArrayList<>();
        List<Integer> bibNumbers = new ArrayList<>();
        List<int[]> pieTimes = new ArrayList<>();
        List<int[]> pieVolumes = new ArrayList<>();
        
        // Load data from file
        readData("A1Data.txt", names, bibNumbers, pieTimes, pieVolumes);
        
        // Calculate statistics
        double[] avgTimes = calculateAverageTime(pieTimes);
        double[] avgVolumes = calculateAverageVolume(pieVolumes);
        double[] weightedScores = calculateWeightedScore(avgTimes, avgVolumes);
        int[] ranks = calculateRank(weightedScores);
        
        // Display summary
        System.out.println(displaySummary(names, bibNumbers, avgTimes, pieVolumes, avgVolumes, weightedScores, ranks));
        
        // User input loop
        while (true) {
            System.out.println("Choose an option:\nA - Add a score\nB - Quit");
            String choice = scanner.nextLine().trim().toUpperCase();
            
            if (choice.equals("B")) {
                System.out.println("End of program.");
                break;
            } else if (choice.equals("A")) {
                addScore(scanner, bibNumbers, pieTimes, pieVolumes, avgTimes, avgVolumes, weightedScores, ranks);
                System.out.println(displaySummary(names, bibNumbers, avgTimes, pieVolumes, avgVolumes, weightedScores, ranks));
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    public static void readData(String filename, List<String> names, List<Integer> bibNumbers, List<int[]> pieTimes, List<int[]> pieVolumes) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split("\s+");
                
                names.add(data[0]);
                bibNumbers.add(Integer.parseInt(data[1]));
                
                int[] times = new int[10];
                for (int i = 0; i < 10; i++) {
                    times[i] = Integer.parseInt(data[i + 2]);
                }
                pieTimes.add(times);
                
                int[] volumes = new int[3];
                for (int i = 0; i < 3; i++) {
                    volumes[i] = Integer.parseInt(data[i + 12]);
                }
                pieVolumes.add(volumes);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }
    
    public static double[] calculateAverageTime(List<int[]> pieTimes) {
        double[] avgTimes = new double[pieTimes.size()];
        for (int i = 0; i < pieTimes.size(); i++) {
            int[] times = pieTimes.get(i);
            Arrays.sort(times);
            double sum = 0;
            for (int j = 1; j < times.length - 1; j++) {
                sum += times[j];
            }
            avgTimes[i] = sum / (times.length - 2);
        }
        return avgTimes;
    }
    
    public static double[] calculateAverageVolume(List<int[]> pieVolumes) {
        double[] avgVolumes = new double[pieVolumes.size()];
        for (int i = 0; i < pieVolumes.size(); i++) {
            int sum = Arrays.stream(pieVolumes.get(i)).sum();
            avgVolumes[i] = sum / 3.0;
        }
        return avgVolumes;
    }
    
    public static double[] calculateWeightedScore(double[] avgTimes, double[] avgVolumes) {
        double[] scores = new double[avgTimes.length];
        for (int i = 0; i < avgTimes.length; i++) {
            scores[i] = (30 - avgTimes[i]) + (avgVolumes[i] / 35);
        }
        return scores;
    }
    
    public static int[] calculateRank(double[] scores) {
        int[] ranks = new int[scores.length];
        double[] sortedScores = scores.clone();
        Arrays.sort(sortedScores);
        for (int i = 0; i < scores.length; i++) {
            ranks[i] = scores.length - Arrays.binarySearch(sortedScores, scores[i]);
        }
        return ranks;
    }
    
    public static String displaySummary(List<String> names, List<Integer> bibNumbers, double[] avgTimes, List<int[]> pieVolumes, double[] avgVolumes, double[] scores, int[] ranks) {
        StringBuilder sb = new StringBuilder();
        sb.append("PPEA Individual Statistics\n*********************************\n");
        sb.append("Name  Bib# Avg Time  V1  V2  V3  Avg Vol  Score  Rank\n");
        for (int i = 0; i < names.size(); i++) {
            sb.append(String.format("%-6s %-4d %.2f  %3d  %3d  %3d  %.2f  %.2f  %d\n",
                    names.get(i), bibNumbers.get(i), avgTimes[i], pieVolumes.get(i)[0], pieVolumes.get(i)[1], pieVolumes.get(i)[2], avgVolumes[i], scores[i], ranks[i]));
        }
        return sb.toString();
    }
    
    public static void addScore(Scanner scanner, List<Integer> bibNumbers, List<int[]> pieTimes, List<int[]> pieVolumes, double[] avgTimes, double[] avgVolumes, double[] scores, int[] ranks) {
        System.out.println("Enter the bib number of the competitor to edit:");
        int bib;
        while (true) {
            bib = scanner.nextInt();
            if (bibNumbers.contains(bib)) break;
            System.out.println("Number not found. Enter again:");
        }
        
        int index = bibNumbers.indexOf(bib);
        System.out.println("Select:\nT: add a time\nV: add a volume");
        String choice = scanner.next().trim().toUpperCase();
        
        System.out.println("Enter new score:");
        int newScore = scanner.nextInt();
        
        if (choice.equals("T")) {
            int[] times = pieTimes.get(index);
            System.arraycopy(times, 1, times, 0, times.length - 1);
            times[times.length - 1] = newScore;
        } else if (choice.equals("V")) {
            int[] volumes = pieVolumes.get(index);
            System.arraycopy(volumes, 1, volumes, 0, volumes.length - 1);
            volumes[volumes.length - 1] = newScore;
        }
    }
}

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

/** 
 * ACS-1904 Assignment 1
 * @Sveinson 
*/

public class A1DataGenerator{
    public static void main(String[] args)throws IOException {
        BufferedReader fin = null;
        PrintWriter fout = null;
        Scanner scanner = new Scanner(System.in);
        Random r = new Random();
        fout = new PrintWriter(new BufferedWriter(new FileWriter("A1Data.txt")));
        
        String[] names = {"Luke", "Leia", "Yoda", "Han", "Boba", "Jabba"};
        int[] bib = {777, 107, 345, 211, 171, 472};
        int[][] times = new int[6][10];
        int[][] volumes = new int[6][3];
        
        loadTable(times, 6, 10, 10, 20);
        //printTable(times, 6, 10);
        
        loadTable(volumes, 6, 3, 400, 700);
        //printTable(volumes, 6, 3);
        
        makeDataFile(names, bib, times, volumes, fout);

        fout.close();
        System.out.println("end of program");
    }// end main
    public static void makeDataFile(String[] n, int[] b, int[][] times, 
                                        int[][]v, PrintWriter fout){
                   
        for(int i = 0; i < n.length; i++){
            fout.print(n[i] + " ");
            fout.print(b[i] + " ");
            
            
            // times
            for(int j = 0; j < times[i].length; j++){
               System.out.print(times[i][j] + " "); 
               fout.print(times[i][j] + " ");
            }// end j times
            
            // volumes
            for(int j = 0; j < v[i].length; j++){
               System.out.print(v[i][j] + " ");
               fout.print(v[i][j] + " ");
            }// end j times
            
            System.out.println();
            fout.println();
        }//end i
        
    }// end make data file
                                        
    public static void loadTable(int[][] t, int rows, int columns, int lowBound, int highBound){
        Random r = new Random();
        
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                t[i][j] = r.nextInt(lowBound) + (highBound - lowBound + 1);
            }// end j
        }// end for i
    }// end load+ 1
    
    public static void printTable(int[][] t, int rows, int columns){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(t[i][j] + " ");
            }// end j
            
            System.out.println();
        }// end for i
    }// end print
        
    
    
}

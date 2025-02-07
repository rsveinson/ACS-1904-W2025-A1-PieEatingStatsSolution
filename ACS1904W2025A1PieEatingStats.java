import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/** 
 * ACS-1904 Assignment 1
 * @Sveinson 
 */

public class ACS1904W2025A1PieEatingStats{
    public static void main(String[] args)throws FileNotFoundException {
        // ** constants **

        final int SCOREWEIGHTFACTOR = 35;   // the factor used to calculate the weighted socre

        // For declaring dimensions of arrays and tables
        final int MAXRECORDS = 6;
        final int MAXTIMES = 10;
        final int MAXVOLUMES = 3;

        // for adding an offset to the index when parsing data from the input record
        final int TIMESOFFSET = 2;
        final int VOLUMESOFFSET = 12;

        // need scanners for file IO and for change/add operations
        Scanner scanner = new Scanner(System.in);
        Scanner fRead = new Scanner(new File("A1Data.txt"));

        // file IO stuff
        String strin;
        String tokens[] = null;     // input string split into separate items
        String delim = "[ ]+";      // regular expression setting delimiter to one or more spaces
        int n = 0;                  // count and index records read from the file

        // Paralel arrays
        String[] names = new String[MAXRECORDS];
        int[] bib = new int[MAXRECORDS];

        int[][] times = new int[MAXRECORDS][MAXTIMES];
        int[][] volumes = new int[MAXRECORDS][MAXVOLUMES];

        double[] averageTime = new double[MAXRECORDS];
        double[] averageVolume = new double[MAXRECORDS];
        double[] weightedScore = new double[MAXRECORDS];
        int[] rank = new int[MAXRECORDS];

        // miscellaneous variables for performing change and add operations
        int count;          // for counting and indexing input records
        char editOption;    // change, add, or quit
        int bibNumber = 0;  // the person whose stat will be editted
        char scoreType;     // the type of score to edit
        int newScore = 0;   // a new editted score
        int scoreNumber = 0;    // the position/number of the score to edit
        int indexToEdit = 0;    // the index of the record to be edited

        /*************************
         * Step one: get the data from the file into the
         * appropriate arrays and tables
         * Tempted to put all of this into a static
         * method just to tidy up the main code
         * we'll see
         ***********************************************/

        count = 0;         // make sure we start counting and indexing at 0
        while(fRead.hasNext()){
            strin = fRead.nextLine();
            //System.out.println(strin);
            /* now that i've tested the file io
             * stuff and made sure all of the
             * data is being read in corrctly i can move on
             */

            // split the input record into tokens
            tokens = strin.split(delim);
            //System.out.println(tokens.length); // should be 15

            // now CARFULLY add each token to the appropriate array or table
            names[count] = tokens[0];                  // first token is a name
            bib[count] = Integer.parseInt(tokens[1]);  // second token is bib number

            // read the next 10 tokens into the times table
            for(int i = 0; i < MAXTIMES; i++){
                times[count][i] = Integer.parseInt(tokens[i + TIMESOFFSET]);
            }//end for i read times

            // now the next three are the volume scores
            for(int i = 0; i < MAXVOLUMES; i++){
                volumes[count][i] = Integer.parseInt(tokens[i + VOLUMESOFFSET]);
            }//end for i read times

            count ++;      // parse the next record/row
        }// end eof loop

        // use print table to test and verify that each table is loaded correctly
        //printTable(volumes, MAXRECORDS, MAXVOLUMES);
        // post loop stuff
        fRead.close();

        // ******** calculate the averages, weighted score and rank ************
        for(int i = 0; i < MAXRECORDS; i++){
            averageTime[i] = calculateAverageTime(times[i], MAXTIMES);
            averageVolume[i] = calculateAverageVolume(volumes[i], MAXVOLUMES);
            weightedScore[i] = calclateWeightedScore(averageTime[i], averageVolume[i], SCOREWEIGHTFACTOR);

        }// end i calculate averages and rank

        // calculate the rank
        calculateRank(weightedScore, rank, MAXRECORDS);
        // printList(weightedScore, MAXRECORDS);
        // System.out.println();
        // printList(rank, MAXRECORDS);

        // print the first summary
        System.out.println("PPEA Individual Statistics\n");
        System.out.println("**************************************************************************\n");

        printSummaryHeadings();
        System.out.println(displaySummary(names, bib, averageTime, volumes, averageVolume, weightedScore, rank));

        /* get option
         * A. change a score
         * B. add a score
         * C. Quit
         */
        System.out.println();
        System.out.println("******* Edit Records *******\n");
        System.out.println(getOptionPrompt());

        editOption = Character.toUpperCase(scanner.next().charAt(0));
        //System.out.println(editOption);

        while(editOption != 'B'){
            do{
                // get the bib number
                System.out.println("Enter the bib numer of the competitor to edit.");
                bibNumber = scanner.nextInt();

                // get the index of the record to edit
                // continue to prompt for a bib number
                // until a valid number is entered

                indexToEdit = getIndexToEdit(bib, bibNumber);
                //System.out.println(indexToEdit);
                System.out.println(indexToEdit < 0 ? "Number not found.": "");
            }
            while(indexToEdit < 0);

            //get the type of score to be added
            System.out.println("Select:");
            System.out.println("T: add a time");
            System.out.println("V: add a volume");
            //System.out.println(getOptionPrompt());
            scoreType = Character.toUpperCase(scanner.next().charAt(0));

            // get the new score
            System.out.println("Enter the new score value.");
            newScore = scanner.nextInt();
            //System.out.println(scoreType + " " + newScore);

            /* note that i use the same method to 
             * add a new score to times or volumes
             */
            if(scoreType == 'T'){
                //printList(times[indexToEdit], 10);
                //System.out.println();
                addScore(times[indexToEdit], newScore);
                //printList(times[indexToEdit], 10);
                //System.out.println("add time: " + newScore);
            }// end add a time
            else{
                addScore(volumes[indexToEdit], newScore);
                //System.out.println("add volume: " + newScore);
            }// end add a volume

            // confirm and print new summary
            System.out.println("\nScore Added.\n");

            // calcualte new averages and weighted score
            // ******** calculate the averages, weighted score and rank ************
            // for(int i = 0; i < MAXRECORDS; i++){
            // averageTime[i] = calculateAverageTime(times[i], MAXTIMES);
            // averageVolume[i] = calculateAverageVolume(volumes[i], MAXVOLUMES);
            // weightedScore[i] = calclateWeightedScore(averageTime[i], averageVolume[i], SCOREWEIGHTFACTOR);

            // }// end i calculate averages and rank

            averageTime[indexToEdit] = calculateAverageTime(times[indexToEdit], MAXTIMES);
            averageVolume[indexToEdit] = calculateAverageVolume(volumes[indexToEdit], MAXVOLUMES);
            weightedScore[indexToEdit] = calclateWeightedScore(averageTime[indexToEdit], averageVolume[indexToEdit], SCOREWEIGHTFACTOR);

            // calculate the rank
            calculateRank(weightedScore, rank, MAXRECORDS);

            // now re-print the summary
            printSummaryHeadings();
            System.out.println(displaySummary(names, bib, averageTime, volumes, averageVolume, weightedScore, rank));            System.out.println("****************************************************\n");

            System.out.println(getOptionPrompt());
            editOption = Character.toUpperCase(scanner.next().charAt(0));
        }// end while

        System.out.println("\nend of program");
    }// end main

    /* shift each element in an array one index to the
     * left (one less) to make room for a new
     * score added to the end of the array
     * notice it's private! it's only ever called from 
     * withing addScore()
     */
    private static void shift(int[] l, int length){
        //System.out.println("shifting");
        for(int i = 0; i < length - 1; i++){
            l[i] = l[i + 1];
        }// end shift loop
    }// end shift

    /* pass in only the necessary row
     * so this means that times[i] will be passed in
     */
    public static void addScore(int[] t, int newScore){
        shift(t, t.length);
        t[t.length -1] = newScore;
    }// end add a score

    /* linear search for the index of the matching bib number
     * return -1 if not found. I should have made a 'not found'
     * routine part of the assignment specification, oh well, mental 
     * note for next time
     */
    public static int getIndexToEdit(int[] l, int key){
        int index = -1;

        for(int i = 0; i < l.length; i++){
            if(l[i] == key)
                index = i;
        }// end for
        return index;
    }// end get index to edit

    /* pretty obvious,
     * this is really just to keep things tidy
     * in main
     */
    public static String getOptionPrompt(){
        StringBuilder sb = new StringBuilder();
        sb.append("Choose an option:\n");
        sb.append("A - Add a score\n");
        sb.append("B - Quit\n");

        return sb.toString();
    }// end getOptionPrompt

    /* Display summary is a method that is uniquely taylored for use
     * with this application. I don't plan or hope to ever use it again.
     * I will use it a couple of times in this program so it makes sense
     * to put these functions into a method
     */
    public static String displaySummary(String [] n, int[] b, double[] at, int[][] volumes, double [] av, double[] as, int[] r){

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < n.length; i++){
            sb.append(n[i] + "\t" + b[i] + "  ");
            sb.append(String.format("%.2f \t",at[i]));

            for(int j = 0; j < volumes[i].length; j++){
                sb.append(volumes[i][j] + "    "); 
            }// end j

            sb.append(String.format("%.2f \t",av[i]));
            sb.append(String.format("%.2f \t",as[i]));
            sb.append(r[i]);

            sb.append("\n");
        }// end for
        return sb.toString();
    }// end display summary

    /* there's probably lots of ways to 
     * do this.
     */
    public static void calculateRank(double[] ws, int[] rank, int n){
        double[] util = new double[n];
        double largest;
        int bigIndex = -1;

        // 1 copy list to util
        for(int i = 0; i < n; i++)
            util[i] = ws[i];

        // find the largest and rank it 1
        for(int i = 0; i < util.length; i++){

            largest = Integer.MIN_VALUE;
            for(int j = 0; j < n; j++){
                if(util[j] > largest){
                    largest = util[j];
                    bigIndex = j;
                }// end if
            }//end for

            //System.out.println("biggest is " + ws[bigIndex]);
            /* set the value of the biggest score to -1
             * it's not the biggest any more
             */
            util[bigIndex] = -1;
            rank[bigIndex] = i + 1;
            //printList(list, list.length);
        }// end for
    }// end rank scores

    /* not much to see here
     * I could/should have declared a constant for 30
     * careless
     */
    public static double calclateWeightedScore(double avgTime, double avgVolume, int factor){
        double weightedScore = 0.0;

        weightedScore = (30 - avgTime) + (avgVolume / factor);
        //System.out.println(weightedScore);
        return weightedScore;
    }

    /* simple, staight forward method
     * key features: calculate average after the loop
     * cast so the result is in fact a double
     */
    public static double calculateAverageVolume(int[] t, int columns){
        double avg = 0.0;
        int sum;

        sum = 0;            // make sure we'restarting at 0
        for(int i = 0; i < columns; i++){
            sum += t[i];
        }// end i
        //System.out.println(sum);
        avg = (double)sum / columns;

        return avg;
    }// end get average volume

    /* use a utility array that can be sorted so that the 
     * best and worst times can be disregared in the 
     * average calculation
     */
    public static double calculateAverageTime(int[] t, int columns){
        double avgTime = 0.0;          // the average time for one contestant
        int sum;

        // first make a copy of the list of times
        int[] util = new int[columns];
        for(int i = 0; i < columns; i++){
            util[i] = t[i];
        }// end i
        // printList(t, columns);
        // System.out.println();
        // printList(util, columns);
        // System.out.println();
        // sort util
        // printList(util, columns);
        // System.out.println();        
        Arrays.sort(util);        
        // printList(util, columns);
        // System.out.println();

        // now find the sum of the middle 8 times
        sum = 0;            // make sure we'restarting at 0
        for(int i = 1; i < columns - 1; i++){
            sum += util[i];
        }// end i
        // System.out.println(sum);
        avgTime = (double)sum / (columns - 2);
        //System.out.println(avgTime);
        return avgTime;
    }// end getAverage teim

    /* some utility methods i used for development and testing
     * note the overloaded method, pretty cool
     */
    public static void printTable(int[][] t, int rows, int columns){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(t[i][j] + " ");
            }//end j
            System.out.println();
        }// end i
    }// end print table

    public static void printList(int[] l, int n){
        for(int i = 0; i < n; i++){        
            System.out.print(l[i] + " ");
        }// end i
    }// end print list

    public static void printList(double[] l, int n){
        for(int i = 0; i < n; i++){        
            System.out.print(String.format("%.2f  ", l[i]));
        }// end i
    }// end print table

    /* could easily been done in main
     * but since it is printed/coded a couple of times 
     * this makes sense
     * NOTE: it's not part of printSummary
     */
    public static void printSummaryHeadings(){
        System.out.print("Name" + "\t" + "Bib#" + " ");
        System.out.print("Avg Time \t");

        for(int j = 0; j < 3; j++){
            System.out.print("V" + (j + 1) + "     "); 
        }// end j

        System.out.print("Avg Vol \t");
        System.out.print("Score \t");
        System.out.print("Rank");

        System.out.println();

    }// end display summary
}

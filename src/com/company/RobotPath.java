package com.company;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class RobotPath {


        File f;
        Scanner scan;
        String [][] grid;
        int nrows;
        int ncols;
        int [] start = new int[2];
        int [] dest = new int[2];


        public RobotPath(){

        }


        public void readInput(String Filename) throws IOException {

            try {
                f = new File(Filename);
                scan = new Scanner(f);
            } catch (IOException e) {
                System.out.println("file not found");
            } finally {
                System.out.println("File read successfully.");
            }

            scan.nextLine();
            nrows = scan.nextInt();
            ncols = scan.nextInt();
            grid = new String[nrows][ncols];
            scan.nextLine();
            start[0] = scan.nextInt();
            start[1] = scan.nextInt();
            grid[start[0]][start[1]] = "S";
            scan.nextLine();
            dest[0] = scan.nextInt();
            dest[1] = scan.nextInt();
            grid[dest[0]][dest[1]] = "D";

            scan.nextLine();
            while(scan.nextLine() != null){

            }

        }

        public void planShortest(){

        }

        public void quickPlan(){

        }

        public void output(){

        }
}

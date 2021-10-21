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
            }

            String tempLine = scan.nextLine();
            Scanner temp = new Scanner(tempLine);
            //get dimension data
            temp.next();
            nrows = temp.nextInt();
            temp.next();
            ncols = temp.nextInt();
            grid = new String[nrows][ncols];
            for(int i = 0; i < nrows; i++){
                for(int j = 0; j < ncols; j++){
                    grid[i][j] = "0";
                }
            }
            temp.close();

            //get start location
            tempLine = scan.nextLine();
            temp = new Scanner(tempLine);
            temp.next();
            start[0] = temp.nextInt();
            start[1] = temp.nextInt();
            grid[start[0]][start[1]] = "S";
            temp.close();


            //get destination location
            tempLine = scan.nextLine();
            temp = new Scanner(tempLine);
            temp.next();
            dest[0] = temp.nextInt();
            dest[1] = temp.nextInt();
            grid[dest[0]][dest[1]] = "D";
            temp.close();

            //get obstacle locations
            scan.nextLine();
            while(scan.hasNextLine()){
                tempLine = scan.nextLine();
                System.out.println(tempLine);

                temp = new Scanner(tempLine);
                grid[temp.nextInt()][temp.nextInt()] = "*";
                temp.close();

            }

        }

        public void planShortest(){

        }

        public void quickPlan(){

        }

        public void output(){
            for(int i = 0; i < nrows; i++){
                for(int j = 0; j < ncols; j++){
                    System.out.print(grid[i][j] + "    ");
                }
                System.out.print("\n");
            }
        }
}

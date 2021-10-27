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
                temp = new Scanner(tempLine);
                grid[temp.nextInt()][temp.nextInt()] = "*";
                temp.close();

            }

        }

        public void planShortest(){

        }

        public void quickPlan(){
            int curr_row = start[0];
            int curr_col = start[1];
            boolean northPossible;
            boolean southPossible;
            boolean westPossible;
            boolean eastPossible;

            while(curr_row != dest[0] && curr_col != dest[1]){
                int [] north = {curr_row+1, curr_col};
                int [] south = {curr_row-1, curr_col};
                int [] west = {curr_row, curr_col-1};
                int [] east = {curr_row+1, curr_col+1};
                northPossible = true;
                southPossible = true;
                westPossible = true;
                eastPossible = true;

                if((north[0] < 0 || north[0] > nrows-1) || grid[north[0]][north[1]] == "*"){
                    northPossible = false;
                }
                if((south[0] < 0 || south[0] > nrows-1) || grid[south[0]][south[1]] == "*"){
                    southPossible = false;
                }
                if((west[1] < 0 || west[1] > ncols-1) || grid[west[0]][west[1]] == "*"){
                    northPossible = false;
                }
                if((east[1] < 0 || east[1] > ncols-1) || grid[east[0]][east[1]] == "*"){
                    northPossible = false;
                }

                double northDist = Math.sqrt( Math.pow(curr_row - north[0],2) + Math.pow(curr_col - north[1],2));
                double southDist = Math.sqrt( Math.pow(curr_row - south[0],2) + Math.pow(curr_col - south[1],2));
                double westDist = Math.sqrt( Math.pow(curr_row - west[0],2) + Math.pow(curr_col - west[1],2));
                double eastDist = Math.sqrt( Math.pow(curr_row - east[0],2) + Math.pow(curr_col - east[1],2));

                String nextMove = "";
                double shortestDist = Double.MAX_VALUE;

                if(northPossible){ //as long as moving north is possible, north is the default move
                    if(shortestDist > northDist){
                        shortestDist = northDist;
                        nextMove = "north";
                    }
                }
                if(southPossible){
                    if(shortestDist > southDist){
                        shortestDist = southDist;
                        nextMove = "south";
                    }else if(shortestDist == southDist){
                        if(Math.min(north[0], south[0]) == south[0]){
                            shortestDist = southDist;
                            nextMove = "south";
                        }else if(north[0] == south[0]){
                            if(Math.min(north[1],south[1]) == south[1]){
                                shortestDist = southDist;
                                nextMove = "south";
                            }
                        }
                    }
                }
                if(westPossible){
                    if(shortestDist > westDist){
                        shortestDist = westDist;
                        nextMove = "west";
                    }else if(shortestDist == westDist){
                        //do the same as above, but for different cases,
                        //when nextMove = "North" and nextMove = "South"

                        if(north[0] > west[0]){
                            shortestDist = southDist;
                            nextMove = "south";
                        }else if(north[0] == west[0]){
                            if(north[1] > west[1]){
                                shortestDist = southDist;
                                nextMove = "south";
                            }
                        }


                    }
                }
                if(eastPossible){
                    if(shortestDist > eastDist){
                        shortestDist = westDist;
                        nextMove = "east";
                    }
                }

                if(nextMove == "north"){
                    grid[curr_row][curr_col] = "n";
                    curr_row = north[0];
                    curr_col = north[1];
                }else if(nextMove == "south"){
                    curr_row = south[0];
                    curr_col = south[1];
                }else if(nextMove == "west"){
                    curr_row = west[0];
                    curr_col = west[1];
                }else{
                    curr_row = east[0];
                    curr_col = east[1];
                }


            }
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

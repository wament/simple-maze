package com.company;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class RobotPath {


        File f;
        Scanner scan;
        Node [][] grid;
        int nrows;
        int ncols;
        int [] start = new int[2];
        int [] dest = new int[2];
        ArrayList<int[]> obstacles = new ArrayList<int[]>();
        Stack<Node> quickpath = new Stack();


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
            grid = new Node[nrows][ncols];

            temp.close();

            //get start location
            tempLine = scan.nextLine();
            temp = new Scanner(tempLine);
            temp.next();
            start[0] = temp.nextInt();
            start[1] = temp.nextInt();

            temp.close();


            //get destination location
            tempLine = scan.nextLine();
            temp = new Scanner(tempLine);
            temp.next();
            dest[0] = temp.nextInt();
            dest[1] = temp.nextInt();

            temp.close();

            //get obstacle locations
            scan.nextLine();
            while(scan.hasNextLine()){
                tempLine = scan.nextLine();
                temp = new Scanner(tempLine);
                int [] pair = {temp.nextInt(), temp.nextInt()};
                obstacles.add(pair);
                temp.close();
            }
            initGrid();
        }
        public void initGrid(){
            for(int i = 0; i < nrows; i++){
                for(int j = 0; j < ncols; j++){
                    grid[i][j] = new Node(i, j, "0");
                }
            }
            for (int[] ob:obstacles) {
                grid[ob[0]][ob[1]].setValue("*");
            }
            grid[start[0]][start[1]].setValue("S");
            grid[dest[0]][dest[1]].setValue("D");
        }

        public void planShortest(){

        }

        public void quickPlan(){
            grid[start[0]][start[1]].visit();
            quickpath.push(grid[start[0]][start[1]]);
            int curr_row = quickpath.peek().getRow();
            int curr_col = quickpath.peek().getCol();
            boolean northPossible;
            boolean southPossible;
            boolean westPossible;
            boolean eastPossible;

            while(curr_row != dest[0] || curr_col != dest[1]){
                curr_row = quickpath.peek().getRow();
                curr_col = quickpath.peek().getCol();
                System.out.println("(" + curr_row + ", "+curr_col+")");
//                if(curr_row == dest[0] && curr_col == dest[1]){
//                    break;
//                }


                northPossible = true;
                southPossible = true;
                westPossible = true;
                eastPossible = true;

                int [] north = {curr_row-1, curr_col};
                int [] south = {curr_row+1, curr_col};
                int [] west = {curr_row, curr_col-1};
                int [] east = {curr_row, curr_col+1};
                if(north[0] == dest[0] && north[1] == dest[1]){
                    break;
                }else if(south[0] == dest[0] && south[1] == dest[1]){
                    break;
                }else if(west[0] == dest[0] && west[1] == dest[1]){
                    break;
                }else if(east[0] == dest[0] && east[1] == dest[1]){
                    break;
                }



                if((north[0] < 0 || north[0] > nrows-1) || grid[north[0]][north[1]].getValue() == "*" || grid[north[0]][north[1]].isVisited()){
                    northPossible = false;
                }
                if((south[0] < 0 || south[0] > nrows-1) || grid[south[0]][south[1]].getValue() == "*" || grid[south[0]][south[1]].isVisited()){
                    southPossible = false;
                }
                if((west[1] < 0 || west[1] > ncols-1) || grid[west[0]][west[1]].getValue() == "*" || grid[west[0]][west[1]].isVisited()){
                    westPossible = false;
                }
                if((east[1] < 0 || east[1] > ncols-1) || grid[east[0]][east[1]].getValue() == "*" || grid[east[0]][east[1]].isVisited()){
                    eastPossible = false;
                }


                double northDist = Math.sqrt( Math.pow(dest[0] - north[0],2) + Math.pow(dest[1] - north[1],2));
                double southDist = Math.sqrt( Math.pow(dest[0] - south[0],2) + Math.pow(dest[1] - south[1],2));
                double westDist = Math.sqrt( Math.pow(dest[0] - west[0],2) + Math.pow(dest[1] - west[1],2));
                double eastDist = Math.sqrt( Math.pow(dest[1] - east[0],2) + Math.pow(dest[1] - east[1],2));


                String nextMove = null;
                double shortestDist = Double.MAX_VALUE;

                if(southPossible){
                    if(shortestDist > southDist){
                        shortestDist = southDist;
                        nextMove = "south";
                    }
                }
                if(northPossible){
                    if(shortestDist > northDist){
                        shortestDist = northDist;
                        nextMove = "north";
                    }else if(shortestDist == northDist){
                        if(Math.min(south[0], north[0]) == north[0]){
                            shortestDist = northDist;
                            nextMove = "north";
                        }else if(south[0] == north[0]){
                            if(Math.min(south[1],north[1]) == north[1]){
                                shortestDist = northDist;
                                nextMove = "north";
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
                        if(nextMove == "north"){
                            if(Math.min(north[0], west[0]) == west[0]){
                                shortestDist = westDist;
                                nextMove = "west";
                            }else if(north[0] == west[0]){
                                if(Math.min(north[1],west[1]) == west[1]){
                                    shortestDist = westDist;
                                    nextMove = "west";
                                }
                            }
                        }else if(nextMove == "south"){
                            if(Math.min(south[0], west[0]) == west[0]){
                                shortestDist = westDist;
                                nextMove = "west";
                            }else if(south[0] == west[0]){
                                if(Math.min(south[1],west[1]) == west[1]){
                                    shortestDist = westDist;
                                    nextMove = "west";
                                }
                            }
                        }

                    }
                }

                if(eastPossible){
                    if(shortestDist > eastDist){
                        shortestDist = eastDist;
                        nextMove = "east";
                    }else if(shortestDist == eastDist){
                        if(nextMove == "north"){
                            if(Math.min(north[0], east[0]) == east[0]){
                                shortestDist = eastDist;
                                nextMove = "east";
                            }else if(north[0] == east[0]){
                                if(Math.min(north[1],east[1]) == east[1]){
                                    shortestDist = eastDist;
                                    nextMove = "east";
                                }
                            }
                        }else if(nextMove == "south"){
                            if(Math.min(south[0], east[0]) == east[0]){
                                shortestDist = eastDist;
                                nextMove = "east";
                            }else if(south[0] == east[0]){
                                if(Math.min(south[1],east[1]) == east[1]){
                                    shortestDist = eastDist;
                                    nextMove = "east";
                                }
                            }
                        }else if(nextMove == "west"){
                            if(Math.min(west[0], east[0]) == east[0]){
                                shortestDist = eastDist;
                                nextMove = "east";
                            }else if(west[0] == east[0]){
                                if(Math.min(west[1],east[1]) == east[1]){
                                    shortestDist = eastDist;
                                    nextMove = "east";
                                }
                            }
                        }

                    }
                }


                if(nextMove == "north"){
                    grid[north[0]][north[1]].setValue("n");
                    grid[north[0]][north[1]].visit();
                    quickpath.push(grid[north[0]][north[1]]);
                }else if(nextMove == "south"){
                    grid[south[0]][south[1]].setValue("s");
                    grid[south[0]][south[1]].visit();
                    quickpath.push(grid[south[0]][south[1]]);
                }else if(nextMove == "west"){
                    grid[west[0]][west[1]].setValue("w");
                    grid[west[0]][west[1]].visit();
                    quickpath.push(grid[west[0]][west[1]]);
                }else if (nextMove == "east"){
                    grid[east[0]][east[1]].setValue("e");
                    grid[east[0]][east[1]].visit();
                    quickpath.push(grid[east[0]][east[1]]);
                }else{
                    quickpath.pop();
                }


            }

        }

        public void output(){
            while(!quickpath.empty()){
                Node n = quickpath.pop();
                System.out.println("(" + n.getRow() + ", "+n.getCol()+")");
                grid[n.getRow()][n.getCol()].setValue(n.getValue());
            }
            for(int i = 0; i < nrows; i++){
                for(int j = 0; j < ncols; j++){
                    System.out.print(grid[i][j].getValue() + "    ");
                }
                System.out.print("\n");
            }
            initGrid();
        }
}

class Node{
    private int row;
    private int col;
    private String value;
    private int layer;
    private boolean visited;
    private Node parent;

    public Node(int row, int col, String value){
        this.row = row;
        this.col = col;
        this.value = value;
        this.layer = -1;
        this.visited = false;
        parent = null;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }
    public void setValue(String newVal){
        this.value = newVal;
    }
    public String getValue(){
        return this.value;
    }
    public int getRow(){ return this.row;}
    public int getCol(){ return this.col;}
    public int getLayer(){ return this.layer;}
    public void setLayer(int layer){ this.layer = layer;}
    public void visit(){ this.visited = true;}
    public boolean isVisited(){ return this.visited;}
}
package com.company;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RobotPath {
    File f; //for input
    Scanner scan;
    Node[][] grid; //2-D array to hold the nodes
    int nrows; //number of rows
    int ncols; //number of columns
    int[] start = new int[2]; //"pair" of start coordinates
    int[] dest = new int[2]; //"pair" of goal coordinates
    ArrayList<int[]> obstacles = new ArrayList<>(); //list of "pairs" of obstacles
    Stack<Node> quickpath = new Stack<>(); //holds the path for quickPlan()
    Queue<Node> bfsData = new LinkedList<>(); //holds the layer data from calling BFS on grid
    ArrayList<ArrayList<Node>> paths = new ArrayList<>(); //holds a collection of shortest paths, filled within planShortest()
    Map<Node, ArrayList<Node>> children = new HashMap(); //holds the parent-child relationship of all nodes in the graph

    public void readInput(String Filename) throws IOException {
        try {
            f = new File(Filename);
            scan = new Scanner(f);
        } catch (IOException e) { //error handling
            System.out.println("file not found");
            //e.printStackTrace();
            throw e;
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
        while (scan.hasNextLine()) {
            tempLine = scan.nextLine();
            temp = new Scanner(tempLine);
            int[] pair = {temp.nextInt(), temp.nextInt()};
            obstacles.add(pair);
            temp.close();
        }

        initGrid(); //initalize the grid using the store inputs we just read
    }

    public void quickPlan() {
        initGrid(); //reset the grid before each pathfinder
        grid[start[0]][start[1]].visit(); //visit starting node
        quickpath.push(grid[start[0]][start[1]]); //push starting node onto path stack
        int curr_row = quickpath.peek().getRow();
        int curr_col = quickpath.peek().getCol();
        //booleans for checking if n,s,w,e are legal moves
        boolean northPossible;
        boolean southPossible;
        boolean westPossible;
        boolean eastPossible;

        while (curr_row != dest[0] || curr_col != dest[1]) { //while current node is not destination node
            if(!quickpath.empty()){ //check if the stack is empty
                curr_row = quickpath.peek().getRow();
                curr_col = quickpath.peek().getCol();
            }else{
                //if it is empty, then all nodes have been popped from the stack
                // which means there is no path to D
                break;
            }

            //all moves are possible by default
            northPossible = true;
            southPossible = true;
            westPossible = true;
            eastPossible = true;

            //move directives
            int[] north = {curr_row - 1, curr_col};
            int[] south = {curr_row + 1, curr_col};
            int[] west = {curr_row, curr_col - 1};
            int[] east = {curr_row, curr_col + 1};

            //when the last move will bring us to the destination, break out of the while loop
            if(curr_row == dest[0] && curr_col == dest[1]){
                break;
            }

            // check for index out of bounds exception, check for obstacle collision, check for backpaddling
            if ((north[0] < 0 || north[0] > nrows - 1) || grid[north[0]][north[1]].getValue().equals("*") || grid[north[0]][north[1]].isVisited()) {
                northPossible = false;
            }
            if ((south[0] < 0 || south[0] > nrows - 1) || grid[south[0]][south[1]].getValue().equals("*") || grid[south[0]][south[1]].isVisited()) {
                southPossible = false;
            }
            if ((west[1] < 0 || west[1] > ncols - 1) || grid[west[0]][west[1]].getValue().equals("*") || grid[west[0]][west[1]].isVisited()) {
                westPossible = false;
            }
            if ((east[1] < 0 || east[1] > ncols - 1) || grid[east[0]][east[1]].getValue().equals("*") || grid[east[0]][east[1]].isVisited()) {
                eastPossible = false;
            }

            // compute distance from destination for each move directive
            double northDist = Math.sqrt(Math.pow(dest[0] - north[0], 2) + Math.pow(dest[1] - north[1], 2));
            double southDist = Math.sqrt(Math.pow(dest[0] - south[0], 2) + Math.pow(dest[1] - south[1], 2));
            double westDist = Math.sqrt(Math.pow(dest[0] - west[0], 2) + Math.pow(dest[1] - west[1], 2));
            double eastDist = Math.sqrt(Math.pow(dest[0] - east[0], 2) + Math.pow(dest[1] - east[1], 2));


            String nextMove = ""; //keep track of the next move
            double shortestDist = Double.MAX_VALUE; //make shortest distance max_val

            //determine which move is the shortest distance
            if (southPossible) { //if south is a legal move
                if (shortestDist > southDist) { //if south distance is less than shortest distance
                    shortestDist = southDist;
                    nextMove = "south"; //set next move to south
                }
            }
            if (northPossible) {
                if (shortestDist > northDist) {
                    shortestDist = northDist;
                    nextMove = "north";
                } else if (shortestDist == northDist) { //if shortest distances are the same
                    if (Math.min(south[0], north[0]) == north[0]) { //compare row values
                        shortestDist = northDist;
                        nextMove = "north";
                    }
                    //no need to compare column values since north and south moves are always in the same column
                }
            }

            if (westPossible) {
                if (shortestDist > westDist) {
                    shortestDist = westDist;
                    nextMove = "west";
                } else if (shortestDist == westDist) { //if west dist and short dist are the same
                    if (nextMove.equals("north")) { //if next move is north
                        if (Math.min(north[0], west[0]) == west[0]) { //compare north and west row values
                            shortestDist = westDist;
                            nextMove = "west";
                        } else if (north[0] == west[0]) { //if north and west row values are the same
                            if (Math.min(north[1], west[1]) == west[1]) { //compare north and west column values
                                shortestDist = westDist;
                                nextMove = "west";
                            }
                        }
                    } else if (nextMove.equals("south")) { //if next move is south
                        if (Math.min(south[0], west[0]) == west[0]) { //also compare row values
                            shortestDist = westDist;
                            nextMove = "west";
                        } else if (south[0] == west[0]) { //if south and west row values are the same
                            if (Math.min(south[1], west[1]) == west[1]) { //also compare col values
                                shortestDist = westDist;
                                nextMove = "west";
                            }
                        }
                    }

                }
            }

            if (eastPossible) {
                if (shortestDist > eastDist) {
                    nextMove = "east";
                } else if (shortestDist == eastDist) {
                    if (nextMove.equals("north")) {
                        if (Math.min(north[0], east[0]) == east[0]) {
                            nextMove = "east";
                        } else if (north[0] == east[0]) {
                            if (Math.min(north[1], east[1]) == east[1]) {
                                nextMove = "east";
                            }
                        }
                    } else if (nextMove.equals("south")) {
                        if (Math.min(south[0], east[0]) == east[0]) {
                            nextMove = "east";
                        } else if (south[0] == east[0]) {
                            if (Math.min(south[1], east[1]) == east[1]) {
                                nextMove = "east";
                            }
                        }
                    } else if (nextMove.equals("west")) {
//                      //no need to compare row values since west and east moves are always in the same row
                        if (Math.min(west[1], east[1]) == east[1]) {
                            nextMove = "east";
                        }

                    }

                }
            }

            //using nextMove variable, set the appropriate fields for the next move
            if (nextMove.equals("north")) {
                grid[curr_row][curr_col].setValue("n"); //set value to north
                grid[north[0]][north[1]].visit(); //visit  node
                quickpath.push(grid[north[0]][north[1]]); //push node to path stack
            } else if (nextMove.equals("south")) {
                grid[curr_row][curr_col].setValue("s");
                grid[south[0]][south[1]].visit();
                quickpath.push(grid[south[0]][south[1]]);
            } else if (nextMove.equals("west")) {
                grid[curr_row][curr_col].setValue("w");
                grid[west[0]][west[1]].visit();
                quickpath.push(grid[west[0]][west[1]]);
            } else if (nextMove.equals("east")) {
                grid[curr_row][curr_col].setValue("e");
                grid[east[0]][east[1]].visit();
                quickpath.push(grid[east[0]][east[1]]);
            } else {
                Node r = quickpath.pop(); //if no moves are selected (none of the moves are possible) then pop the top node (continue down a new branch of the path)
                grid[r.getRow()][r.getCol()].setValue("0"); //reset the value of the node
            }
        }
        grid[start[0]][start[1]].setValue("S"); //reset starting point value (would be a move directive otherwise)
    }


    public void planShortest() {
        initGrid(); //reset the grid before each pathfinder
        bfs(grid[start[0]][start[1]]); //run bfs call
        findShortestPaths(paths, new ArrayList<>(), grid[start[0]][start[1]]); //find the shortest paths
        writeShortestPlans(paths); //set the value of the grid, according to the path directives
    }


    public void findShortestPaths(ArrayList<ArrayList<Node>> paths, ArrayList<Node> path, Node curr) {
        if (curr.getValue().equals("D")) { //we have reached the end of the path
            paths.add(new ArrayList<>(path)); //add this path to the paths list
            return;
        }

        for (Node e : children.get(curr)) { //look at the children of the current node
            if (e.getLayer() > curr.getLayer()) {
                path.add(e); //add the node to the path
                findShortestPaths(paths, path,  e); //recursive call on node e
                path.remove(path.size() - 1); //remove the destination from the path
            }
        }
    }


    public void bfs(Node root) {
        root.visit(); //visit root node
        root.setLayer(0); //set root layer to 0
        bfsData.add(root); //add root to bfs queue

        while (bfsData.peek() != null) { //while list not empty
            Node n = bfsData.remove(); //extract head node
            ArrayList<Node> neighbors = new ArrayList<>();
            int curr_row = n.getRow();
            int curr_col = n.getCol();

            //move directives
            int[] north = {curr_row - 1, curr_col};
            int[] south = {curr_row + 1, curr_col};
            int[] west = {curr_row, curr_col - 1};
            int[] east = {curr_row, curr_col + 1};

            if (!(north[0] < 0 || north[0] > nrows - 1)) { //index out of bounds check
                if (!grid[north[0]][north[1]].getValue().equals("*")) { //legal move check
                    neighbors.add(grid[north[0]][north[1]]); //add legal neighbor
                }
            }
            if (!(south[0] < 0 || south[0] > nrows - 1)) { //index out of bounds check
                if (!grid[south[0]][south[1]].getValue().equals("*")){ //legal move check
                    neighbors.add(grid[south[0]][south[1]]); //add legal neighbor
                }
            }
            if (!(west[1] < 0 || west[1] > ncols - 1)) { //index out of bounds check
                if (!grid[west[0]][west[1]].getValue().equals("*")){ //legal move check
                    neighbors.add(grid[west[0]][west[1]]); //add legal neighbor
                }
            }
            if (!(east[1] < 0 || east[1] > ncols - 1)) { //index out of bounds check
                if (!grid[east[0]][east[1]].getValue().equals("*")){ //legal move check
                    neighbors.add(grid[east[0]][east[1]]); //add legal neighbor
                }
            }

            children.put(n, neighbors); //store neighbor data

            for (Node u : neighbors) {
                if (!u.isVisited()) {
                    u.visit(); //visit node u
                    u.setLayer(n.getLayer() + 1); //increment node u layer value
                    bfsData.add(u); //recursive bfs call for node u
                }
            }
        }
    }

    //using the shortest paths we found earlier, traverse the arraylist and modify the values of grid based on the respective move directives
    public void writeShortestPlans(ArrayList<ArrayList<Node>> plans) {
        for (int i = 0; i < plans.size(); i++) {
            for (int j = 0; j < plans.get(i).size() - 1; j++) {
                int rowDiff = plans.get(i).get(j + 1).getRow() - plans.get(i).get(j).getRow(); //difference in row value between next node and current node
                int colDiff = plans.get(i).get(j + 1).getCol() - plans.get(i).get(j).getCol(); //difference in column value between next node and current node

                String nextMove = "";

                //move directives based off of rowDiff/colDiff
                if (rowDiff == -1) {
                    nextMove = "north";
                } else if (rowDiff == 1) {
                    nextMove = "south";
                } else if (colDiff == -1) {
                    nextMove = "west";
                } else if (colDiff == 1) {
                    nextMove = "east";
                }

                //all plans contain only valid nodes (no obstacles or out of bounds)
                //if a node has a value that is not "0", then it must already have a move directive associated with it
                if (!plans.get(i).get(j).getValue().equals("0")) {
                    String gridVal = grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].getValue();

                    //apply move precedence rules (s,n,w,e)
                    if (nextMove.equals("north")) {
                        if (gridVal.equals("s")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sn");
                        } else if (gridVal.equals("w")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("nw");
                        } else if (gridVal.equals("e")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("ne");
                        }

                    } else if (nextMove.equals("south")) {
                        if (gridVal.equals("n")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sn");
                        } else if (gridVal.equals("w")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sw");
                        } else if (gridVal.equals("e")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("se");
                        }

                    } else if (nextMove.equals("west")) {
                        if (gridVal.equals("s")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sw");
                        } else if (gridVal.equals("n")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("nw");
                        } else if (gridVal.equals("e")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("we");
                        }

                    } else if (nextMove.equals("east")) {
                        if (gridVal.equals("s")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("se");
                        } else if (gridVal.equals("n")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("ne");
                        } else if (gridVal.equals("w")) {
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("we");
                        }

                    }
                } else {
                    //node value is "0", so simply set it to the respective move directive
                    if (nextMove.equals("north")) {
                        grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("n");
                    } else if (nextMove.equals("south")) {
                        grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("s");
                    } else if (nextMove.equals("west")) {
                        grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("w");
                    } else if (nextMove.equals("east")) {
                        grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("e");
                    }
                }
            }
        }
    }

    public void output() { //output the grid
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                System.out.printf("%5s", grid[i][j].getValue());
            }
            System.out.print("\n");
        }
        System.out.println("\n");
    }

    public void initGrid() { //used to reset the grid to an inital state after running quickPlan() or planShortest()
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                grid[i][j] = new Node(i, j, "0");
            }
        }
        for (int[] ob : obstacles) {
            grid[ob[0]][ob[1]].setValue("*");
        }
        grid[start[0]][start[1]].setValue("S");
        grid[dest[0]][dest[1]].setValue("D");
    }

    public void outputBFS() { //helper method for viewing BFS layer data
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                System.out.printf("5%s", grid[i][j].getLayer());
            }
            System.out.print("\n");
        }
    }

    public String pathToString(ArrayList<Node> path) { //helper method for viewing a shortest path
        String output = "";
        for (Node n : path) {
            output += n.getCoords() + " -> ";
        }
        return output;
    }
}

class Node { //helper class to store node data
    private final int row;
    private final int col;
    private String value;
    private int layer;
    private boolean visited;

    //initalize a node with row, col, and default value
    //always set layer to -1 and visited to false
    public Node(int row, int col, String value) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.layer = -1;
        this.visited = false;
    }

    //collection of getter and setter methods for variables
    public void setValue(String newVal) {
        this.value = newVal;
    }

    public String getValue() {
        return this.value;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void visit() {
        this.visited = true;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public String getCoords() { return "(" + this.getRow() + ", " + this.getCol() + ")"; }

    public String toString() { return this.value; }
}
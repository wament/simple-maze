package com.company;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class RobotPath {


        File f;
        Scanner scan;
        Node [][] grid;
        int nrows;
        int ncols;
        int [] start = new int[2];
        int [] dest = new int[2];
        ArrayList<int[]> obstacles = new ArrayList<>();
        Stack<Node> quickpath = new Stack<>();
        Queue<Node> bfsData = new LinkedList<>();
        ArrayList<ArrayList<Node>> paths = new ArrayList<>();
        Map<Node, ArrayList<Node>> children = new HashMap();


        public RobotPath(){

        }


        public void readInput(String Filename) throws IOException {

            try {
                f = new File(Filename);
                scan = new Scanner(f);
            } catch (IOException e) {
                System.out.println("file not found");
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
                    grid[i][j] = new Node(i, j, " 0");
                }
            }
            for (int[] ob:obstacles) {
                grid[ob[0]][ob[1]].setValue(" *");
            }
            grid[start[0]][start[1]].setValue(" S");
            grid[dest[0]][dest[1]].setValue(" D");
        }


        public void planShortest(){
            bfs();

            findShortestPaths(paths, new ArrayList<>(), children, grid[start[0]][start[1]]);
//            for(ArrayList<Node> p: paths){
//                System.out.println(pathToString(p));
//            }
            writeShortestPlans(paths);
        }


        public void findShortestPaths(ArrayList<ArrayList<Node>> paths, ArrayList<Node> path, Map<Node, ArrayList<Node>> parents, Node curr){

            if(curr.getValue().equals(" D")){
                paths.add(new ArrayList<>(path));
                return;
            }

            for(Node e: children.get(curr)){
                if(e.getLayer() > curr.getLayer()){
                    path.add(e);
                    findShortestPaths(paths, path, children, e);
                    path.remove(path.size()-1);
                }
            }

        }


        public void bfs(){
            grid[start[0]][start[1]].visit();
            grid[start[0]][start[1]].setLayer(0);
            bfsData.add(grid[start[0]][start[1]]);


            while(bfsData.peek() != null) {
                Node n = bfsData.remove();
                ArrayList<Node> neighbors = new ArrayList<>();
                int curr_row = n.getRow();
                int curr_col = n.getCol();
                int[] north = {curr_row - 1, curr_col};
                int[] south = {curr_row + 1, curr_col};
                int[] west = {curr_row, curr_col - 1};
                int[] east = {curr_row, curr_col + 1};

                if(!(north[0] < 0 || north[0] > nrows-1)){
                    if(!grid[north[0]][north[1]].getValue().equals(" *") && !grid[north[0]][north[1]].getValue().equals(null)){
                        neighbors.add(grid[north[0]][north[1]]);
                    }

                }
                if(!(south[0] < 0 || south[0] > nrows-1)){
                    if(!grid[south[0]][south[1]].getValue().equals(" *") && !grid[south[0]][south[1]].getValue().equals(null)){
                        neighbors.add(grid[south[0]][south[1]]);
                    }
                }
                if(!(west[1] < 0 || west[1] > ncols-1)){
                    if(!grid[west[0]][west[1]].getValue().equals(" *") && !grid[west[0]][west[1]].getValue().equals(null)){
                        neighbors.add(grid[west[0]][west[1]]);
                    }
                }
                if(!(east[1] < 0 || east[1] > ncols-1)){
                    if(!grid[east[0]][east[1]].getValue().equals(" *") && !grid[east[0]][east[1]].getValue().equals(null)){
                        neighbors.add(grid[east[0]][east[1]]);
                    }
                }

//                System.out.print(n.getCoords() + " == ");
//                for(Node e: neighbors) {
//                    System.out.print(e.getCoords() + " ");
//                }
//                System.out.println("");

                children.put(n, neighbors);

                for (Node u: neighbors) {
                    if(u.isVisited() == false){
                        u.visit();
                        u.setLayer(n.getLayer()+1);
                        u.setParent(n);
                        bfsData.add(u);

                    }
                }
            }
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
                System.out.println(quickpath.peek().getCoords());
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



                if((north[0] < 0 || north[0] > nrows-1) || grid[north[0]][north[1]].getValue().equals("*") || grid[north[0]][north[1]].isVisited()){
                    northPossible = false;
                }
                if((south[0] < 0 || south[0] > nrows-1) || grid[south[0]][south[1]].getValue().equals("*") || grid[south[0]][south[1]].isVisited()){
                    southPossible = false;
                }
                if((west[1] < 0 || west[1] > ncols-1) || grid[west[0]][west[1]].getValue().equals("*") || grid[west[0]][west[1]].isVisited()){
                    westPossible = false;
                }
                if((east[1] < 0 || east[1] > ncols-1) || grid[east[0]][east[1]].getValue().equals("*") || grid[east[0]][east[1]].isVisited()){
                    eastPossible = false;
                }


                double northDist = Math.sqrt( Math.pow(dest[0] - north[0],2) + Math.pow(dest[1] - north[1],2));
                double southDist = Math.sqrt( Math.pow(dest[0] - south[0],2) + Math.pow(dest[1] - south[1],2));
                double westDist = Math.sqrt( Math.pow(dest[0] - west[0],2) + Math.pow(dest[1] - west[1],2));
                double eastDist = Math.sqrt( Math.pow(dest[1] - east[0],2) + Math.pow(dest[1] - east[1],2));


                String nextMove = "";
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
                        if(Math.min(south[0], north[0]) == north[0]) {
                            shortestDist = northDist;
                            nextMove = "north";
                        }
//                        }else if(south[0] == north[0]){
//                            if(Math.min(south[1],north[1]) == north[1]){
//                                shortestDist = northDist;
//                                nextMove = "north";
//                            }
//                        }
                    }
                }

                if(westPossible){
                    if(shortestDist > westDist){
                        shortestDist = westDist;
                        nextMove = "west";
                    }else if(shortestDist == westDist){
                        //do the same as above, but for different cases,
                        //when nextMove = "North" and nextMove = "South"
                        if(nextMove.equals("north")){
                            if(Math.min(north[0], west[0]) == west[0]){
                                shortestDist = westDist;
                                nextMove = "west";
                            }else if(north[0] == west[0]){
                                if(Math.min(north[1],west[1]) == west[1]){
                                    shortestDist = westDist;
                                    nextMove = "west";
                                }
                            }
                        }else if(nextMove.equals("south")){
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
                        nextMove = "east";
                    }else if(shortestDist == eastDist){
                        if(nextMove.equals("north")){
                            if(Math.min(north[0], east[0]) == east[0]){
                                nextMove = "east";
                            }else if(north[0] == east[0]){
                                if(Math.min(north[1],east[1]) == east[1]){
                                    nextMove = "east";
                                }
                            }
                        }else if(nextMove.equals("south")){
                            if(Math.min(south[0], east[0]) == east[0]){
                                nextMove = "east";
                            }else if(south[0] == east[0]){
                                if(Math.min(south[1],east[1]) == east[1]){
                                    nextMove = "east";
                                }
                            }
                        }else if(nextMove.equals("west")){
//                            if(Math.min(west[0], east[0]) == east[0]){
//                                nextMove = "east";
//                            }else if(west[0] == east[0]){
                                if(Math.min(west[1],east[1]) == east[1]){
                                    nextMove = "east";
                                }
                            //}
                        }

                    }
                }


                if(nextMove.equals("north")){
                    grid[north[0]][north[1]].setValue("n");
                    grid[north[0]][north[1]].visit();
                    quickpath.push(grid[north[0]][north[1]]);
                }else if(nextMove.equals("south")){
                    grid[south[0]][south[1]].setValue("s");
                    grid[south[0]][south[1]].visit();
                    quickpath.push(grid[south[0]][south[1]]);
                }else if(nextMove.equals("west")){
                    grid[west[0]][west[1]].setValue("w");
                    grid[west[0]][west[1]].visit();
                    quickpath.push(grid[west[0]][west[1]]);
                }else if (nextMove.equals("east")){
                    grid[east[0]][east[1]].setValue("e");
                    grid[east[0]][east[1]].visit();
                    quickpath.push(grid[east[0]][east[1]]);
                }else{
                    quickpath.pop();
                }


            }
            writeQuickPlan();
        }

        public void writeShortestPlans(ArrayList<ArrayList<Node>> plans){
            for(int i = 0; i < plans.size(); i++){
                for(int j = 0; j < plans.get(i).size()-1; j++){
                    int rowDiff = plans.get(i).get(j+1).getRow() - plans.get(i).get(j).getRow();
                    int colDiff = plans.get(i).get(j+1).getCol() - plans.get(i).get(j).getCol();

                    String nextMove = "";

                    if(rowDiff == -1){
                        nextMove = "north";
                    }else if (rowDiff == 1){
                        nextMove = "south";
                    }else if (colDiff == -1){
                        nextMove = "west";
                    }else if (colDiff == 1){
                        nextMove = "east";
                    }

                    if(!plans.get(i).get(j).getValue().equals(" 0")){
                        String gridVal = grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].getValue();
                        if(nextMove.equals("north")){
                            if(gridVal.equals(" s")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sn");
                            }else if(gridVal.equals(" w")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("nw");
                            }else if(gridVal.equals(" e")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("ne");
                            }

                        }else if(nextMove.equals("south")){
                            if(gridVal.equals(" n")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sn");
                            }else if(gridVal.equals(" w")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sw");
                            }else if(gridVal.equals(" e")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("se");
                            }

                        }else if(nextMove.equals("west")){
                            if(gridVal.equals(" s")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("sw");
                            }else if(gridVal.equals(" n")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("nw");
                            }else if(gridVal.equals(" e")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("we");
                            }

                        }else if(nextMove.equals("east")){
                            if(gridVal.equals(" s")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("se");
                            }else if(gridVal.equals(" n")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("ne");
                            }else if(gridVal.equals(" w")){
                                grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue("we");
                            }

                        }
                    }else{
                        if(nextMove.equals("north")){
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue(" n");
                        }else if(nextMove.equals("south")){
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue(" s");
                        }else if(nextMove.equals("west")){
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue(" w");
                        }else if(nextMove.equals("east")){
                            grid[plans.get(i).get(j).getRow()][plans.get(i).get(j).getCol()].setValue(" e");
                        }
                    }
                }
            }
        }

        public void writeQuickPlan(){
            while(!quickpath.empty()){
                Node n = quickpath.pop();
                System.out.println(n.getCoords());
                grid[n.getRow()][n.getCol()].setValue(n.getValue());
            }
        }

        public void output(){
            for(int i = 0; i < nrows; i++){
                for(int j = 0; j < ncols; j++){
                    System.out.print(grid[i][j].getValue() + "    ");
                }
                System.out.print("\n");
            }
            initGrid(); //reset the grid after each output. always output after running a plan
        }

    public void outputBFS(){
        for(int i = 0; i < nrows; i++){
            for(int j = 0; j < ncols; j++){
                System.out.print(grid[i][j].getLayer() + "    ");
            }
            System.out.print("\n");
        }
        initGrid(); //reset the grid after each output. always output after running a plan
    }

    public String pathToString(ArrayList<Node> path){
        String output = "";
        for(Node n : path){
            output+= n.getCoords() +" -> ";
        }
        return output;
    }
}

class Node{
    private final int row;
    private final int col;
    private String value = null;
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

    public Node getParent() { return this.parent; }
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
    public String getCoords(){
        return "("+ this.getRow()+", "+this.getCol()+")";
    }
    public String toString(){
        return this.value;
    }
}
package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        RobotPath rob = new RobotPath();
        rob.readInput("C:\\Users\\William\\Documents\\Algo311\\311hw4\\src\\com\\company\\input.txt");
        //rob.readInput("C:\\Users\\will\\Documents\\Algo311\\hw4\\src\\com\\company\\input.txt");
        rob.output();

    }
}

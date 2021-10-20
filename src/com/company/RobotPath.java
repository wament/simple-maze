package com.company;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class RobotPath {


        File f;
        Scanner scan;
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
                System.out.println(scan.nextLine());

            }
        }
}

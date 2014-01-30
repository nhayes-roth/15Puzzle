/*
 * Play.java
 * ------------
 * Name: Nathan Hayes-Roth
 * UNI: nbh2113
 * Project 2: Comparison of Alternative Search Algorithms
 *            (using The 15 Puzzle)
 * ------------
 * Administers the actual testing
 */

package puzzle;

import java.awt.Point;
import java.util.Vector;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;

public class Play{
    
    public static Node start = new Node();
    public static Node goal = new Node();

    private static int dfsBound = 10;
    
    /*
     * intro()
     * chooses one of the two possible goal states
     */
    public static String intro(){
        System.out.println("Welcome to Project 2 - Comparisons of Alternative Search Algorithms");
        System.out.println("Please select a goal state:");
        System.out.println("\n\tGoal State A: ( (0 1 2 3) (4 5 6 7) (8 9 10 11) (12 13 14 15) (0 0) )");
        System.out.println("\tGoal State B: ( (1 2 3 4) (8 7 6 5) (9 10 11 12) (0 15 14 13) (3 0) )");
        String choice;
        while(true){
            System.out.println("\nMake your selection by entering either A or B.");
            Scanner in = new Scanner(System.in);
            choice = in.next();
            if (choice.equals("A")){
                goal.arrayToState(Node.gA);
                goal.setSpace(goal.findSpace());
                return choice;
            }
            if (choice.equals("B")){
                goal.arrayToState(Node.gB);
                goal.setSpace(goal.findSpace());
                return choice;
            }
        }
    }
    
    /*
     * setStartState()
     * prompts the user to input the start state
     * modifies the start node
     */
    public static void setStartState(){
        int i = 0;
        int j = 0;
        int count = 0;
        System.out.println("Please enter the start state (any format):");
        String str = "";
        while(count<=Node.maxValue){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                str = br.readLine();
                br.close();
            } catch (Exception e) {
                System.out.println("Exception: "+e);
            }
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(str); 
            while ((count<=Node.maxValue) && (m.find())){
                start.setState(i, j, Integer.parseInt(m.group()));
                count++;
                if(j == 3){
                    i++;
                    j = 0;
                }
                else
                    j++;
            }
            
        }
        start.setSpace(start.findSpace());
    }
    
    /*
     * main function
     * administers the program
     */
    public static void main(String [] args){
        String goalChoice = intro();
        while(true){
            setStartState();
            if(start.checkState())
                break;
            else
                System.out.println("checkstate() failed");
        }

        System.out.println();
        System.out.println(Search.dfs(start, goal, dfsBound));
        System.out.println(Search.bfs(start, goal));
        System.out.println(Search.ufcs(start, goal));
        System.out.println(Search.idfs(start, goal));
        System.out.println(Search.greedyMT(start,goal));
        System.out.println(Search.greedyMO(start, goal));
        System.out.println(Search.aStarMT(start, goal));
        System.out.println(Search.aStarMO(start, goal));
        
        // reset the nodes to prevent validMoves Vectors from holding incorrect values
        start = new Node();
        goal = new Node();
        
    }
}


        
        
    
             
    
    
    
    
    

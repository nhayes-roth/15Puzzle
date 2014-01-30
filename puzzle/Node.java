/*
 * Node.java
 * ------------
 * Name: Nathan Hayes-Roth
 * UNI: nbh2113
 * Project 2: Comparison of Alternative Search Algorithms
 *            (using The 15 Puzzle)
 * ------------
 * Handles specific states of the board, which will be passed
 * through the other programs.
 */

package puzzle;

import java.awt.Point;
import java.util.Vector;
import java.util.Iterator;
import java.lang.Math;

public class Node implements Comparable{
    
    public static int puzzleSize = 4;
    public static int maxValue = puzzleSize*puzzleSize - 1;
    
    public static int[] gA = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    public static int[] gB = {1, 2, 3, 4, 8, 7, 6, 5, 9, 10, 11, 12, 0, 15, 14, 13};
    
    public Node parent;
    private int[][] state;
    public int depth;
    private int distance;
    private Point space;
    private Vector<Point> validMoves;
    
    /*
     * initial constructor
     */
    public Node(){
        this.parent = null;
        this.state = new int[puzzleSize][puzzleSize];
        this.depth = 0;
        this.distance = 0;
        this.space = new Point();
        this.validMoves = new Vector<Point>();
    }
    
    /* 
     * general constructor
     */
    public Node(int[][] state, Node parent, Point space){
        this.state = state;
        this.parent = parent;
        this.depth = parent.depth + 1;
        this.distance = 0;
        this.space = space;
        this.validMoves = this.getValidMoves();
    }
    
    /*
     * buildNextNode(Point newSpace)
     * builds a child node 
     */
    public static Node buildNextNode(Node Parent, Point newSpace){
        //start with a blank node
        Node child = new Node();
        
        //point its parent to the root node
        child.parent = Parent;
        
        //copy the parent's state
        for (int i = 0; i<puzzleSize; i++){
            for (int j = 0; j<puzzleSize; j++)
                child.state[i][j] = Parent.state[i][j];
        }
        
        //add 1 to the depth
        child.depth = Parent.depth + 1;
        
        //set the space to the argument provided
        child.setSpace(newSpace);
        
        //find value in position of newSpace, replace with zero
        int x = (int)newSpace.getX();
        int y = (int)newSpace.getY();
        int val = child.getState()[x][y];
        child.setState(x, y ,0);
        
        // find previous space, replace with value
        Point oldSpace = Parent.getSpace();
        int i = (int)oldSpace.getX();
        int j = (int)oldSpace.getY();
        child.setState(i, j, val);
        
        return child;
    }
    

    
    /* 
     * getState() 
     * returns this node's state
     */
    public int[][] getState(){
        return this.state;
    }

    /*
     * getDepth()
     * returns this node's depth
     */
    public int getDepth(){
        return this.depth;
    }
    
    /* 
     * setState()
     * assigns a value to a specific state cell
     */
    public void setState(int i, int j, int val){
        this.state[i][j] = val;
    }
    
    /*
     * setSpace(Point that)
     * sets a Node's space to the coordinates given
     * also sets the vector of valid moves
     */
    public void setSpace(Point that){
        this.space = that;
        this.setValidMoves();
    }
    
    /*
     * setValidMoves()
     * sets the validMoves() Vector with appropriate values;
     */
    public void setValidMoves(){
        int row = (int)this.space.getX();
        int column = (int)this.space.getY();
        int max = puzzleSize-1;
        if (row < max)
            this.validMoves.add(new Point(row+1, column));
        if (row > 0)
            this.validMoves.add(new Point(row-1, column));
        if (column < max)
            this.validMoves.add(new Point(row, column+1));
        if (column > 0)
            this.validMoves.add(new Point(row, column-1));
    }
    
    /*
     * getValidMoves()
     * returns a Vector<Point> of valid next spaces
     */
    public Vector<Point> getValidMoves(){
        return this.validMoves;
    }
    
    /* 
     * compareTo()
     * returns 0 if two nodes' states are identical
     * returns 1 otherwise
     */
    public int compareTo(Object other){
        int[][] thisState = this.getState();
        int[][] thatState = ((Node)other).getState();
        int length = this.getState().length;
        for (int i = 0; i<length; i++){
            for (int j = 0; j<length; j++){
                if (thisState[i][j] != thatState[i][j])
                    return 1;
            }
        }
        return 0;
    }

     /*
     * equals(Object o)
     * overrides the equals() method used in searching stacks
     */
    public boolean equals(Object o){
        if (this.compareTo(o)==0)
            return true;
        else
            return false;
    }
    
    /*
     * checkState()
     * returns true if (1) all values are in the acceptable range
     *                 (2) there are no repeats
     *                 (3) there is one zero
     * returns false otherwise
     */
    public boolean checkState(){
        boolean checkZero = false;
        for (int i = 0; i<puzzleSize; i++){
            for (int j = 0; j<puzzleSize; j++){
                if (this.state[i][j] == 0)
                    checkZero = true;
                if ( (this.state[i][j]<0) || (this.state[i][j]>maxValue)){
                    System.out.println("Value greater than " + maxValue + " found.");
                    return false;
                }
                for (int k = 0; k<puzzleSize; k++){
                    for (int l = 0; l<puzzleSize; l++){
                        if ( (this.state[i][j] == this.state[k][l])&&( (i!=k) || (j!=l))){
                            System.out.println("State contained duplicate value(s).");
                            return false;
                        }
                    }
                }
            }
        }
        if (!checkZero)
            System.out.println("State did not contain an empty space. Denote an empty space with a 0.");
        return checkZero;
    }
    
    /*
     * findSpace()
     * returns the point coordinates of a state's zero
     * returns (-1,-1) if zero is not found
     */
    public Point findSpace(){
        Point toReturn = new Point(-1, -1);
        for (int i = 0; i<puzzleSize; i++){
            for (int j = 0; j<puzzleSize; j++){
                if (this.state[i][j] == 0)
                    toReturn.setLocation(i,j);
            }
        }
        return toReturn;
    }
    
    /*
     * returns the Point variable space from a Node
     */
    public Point getSpace(){
        return this.space;
    }
    
    /*
     * printNode()
     * prints a node's state and space in an appealing way
     */
    public void printNode(){
        // make an appropriately lengthed array of formatting
        // to separate rows/space coordinates
        String[] separators = new String[puzzleSize + 2];
        separators[0] = "\n((";
        for (int i=0; i<puzzleSize; i++)
            separators[i+1] = ") (";
        separators[puzzleSize+1] = "))";
        // print the rows
        for (int i = 0; i<puzzleSize; i++){
            System.out.print(separators[i]);
            for (int j = 0; j<puzzleSize; j++){
                System.out.print(this.state[i][j]);
                if (j<puzzleSize-1)
                    System.out.print(" ");
            }
        }
        // print the space's coordinates
        int x = (int)this.space.getX();
        int y = (int)this.space.getY();
        System.out.print(separators[puzzleSize] + x + " " + y + separators[puzzleSize+1]);
    }
    
    /*
     * arrayToState(int[])
     * fills a node's state from an array
     */
    public void arrayToState(int[] gX){
        int i = 0;
        int j = 0;
        int count = 0;
        while(count < gX.length){
            this.setState(i, j, gX[count]);
            count++;
            if(j == 3){
                i++;
                j = 0;
            }
            else
                j++;
        }
    }
    
    /*
     * printValidMoves()
     * prints an aesthetically pleasing representation of a node's possible next spaces
     */
    public void printValidMoves(){
        Iterator itr = this.validMoves.iterator();
        System.out.print("Next Spaces: ");
        while(itr.hasNext()){
            Point next = (Point)itr.next();
            System.out.print("(" + (int)next.getX() + " " + (int)next.getY()+")");
        }
    }

    /*
     * getDirection(Node A, Node B)
     * compares two nodes and returns a string describing the direction the space was moved
     */
    public static String getDirection(Node A, Node B){
        if ((A == null)||(B == null))
                return "";
        Point first = A.getSpace();
        Point second = B.getSpace();
        if ((int)first.getX()>(int)second.getX())
            return "N";
        if ((int)first.getX()<(int)second.getX())
            return "S";
        if ((int)first.getY()>(int)second.getY())
            return "W";
        else
            return "E";
    }

    /*
     * misplacedTiles(Node A, Node B)
     * class heuristic
     * returns the number of misplaced tiles in Node A's state, compared to Node B's
     */
    public static int misplacedTiles(Node A, Node B){
        int toReturn = 0;
        for (int i = 0; i<puzzleSize; i++){
            for (int j = 0; j<puzzleSize; j++){
                if (A.state[i][j]!=B.state[i][j])
                    toReturn++;
            }
        }
        return toReturn;
    }
    
    /*
     * compareMT(Node A, Node B, Node goal)
     * compares the nodes for order using their misplacedTiles
     * returns -1, 0, or 1
     * if the first argument is less than, equal to, or greater than the second
     */
   public static int compareMT(Node A, Node B, Node goal){
       int mtA = misplacedTiles(A, goal);
       int mtB = misplacedTiles(B, goal);
       if (mtA<mtB)
           return -1;
       if (mtA>mtB)
           return 1;
       else
           return 0;
   }

    /*
     * myOwn(Node A, Node B)
     * my heuristic
     * subtracts 1 for each tile in correct spot, adds 1 for each in wrong
     */
    public static int myOwn(Node A, Node B){
        int toReturn = 0;
        for (int i = 0; i<puzzleSize; i++){
            for (int j = 0; j<puzzleSize; j++){
                if (A.state[i][j]==B.state[i][j])
                    toReturn--;
                else
                    toReturn++;
            }
        }
        return toReturn;
    }

    /*
     * compareMA(Node A, Node B, Node goal)
     * compares the nodes for order using their movesAway
     * returns -1, 0, or 1
     * if the first argument is less than, equal to, or greater than the second
     */
   public static int compareMO(Node A, Node B, Node goal){
       int mtA = myOwn(A, goal);
       int mtB = myOwn(B, goal);
       if (mtA<mtB)
           return -1;
       if (mtA>mtB)
           return 1;
       else
           return 0;
   }

    
}

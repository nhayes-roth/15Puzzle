/*
 * Search.java
 * ------------
 * Name: Nathan Hayes-Roth
 * UNI: nbh2113
 * Project 2: Comparison of Alternative Search Algorithms
 *            (using The 15 Puzzle)
 * ------------
 * Holds the search functions to be called by Play.java
 */

package puzzle;

import java.awt.Point;
import java.util.Vector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;


public class Search{

    private static LinkedList<Node> solution = new LinkedList<Node>();
    private static int Open = 0;
    private static int Ignored = 0;
    private static int Closed = 0;

    /*
     * printStats(Node current, String searchName)
     * prints all the required information after a test has been run
     */
    public static void printStats(Node current, String searchName){
        System.out.println(searchName + ":");
        int depth = current.getDepth();
        while(current.parent!=null){    
            solution.addFirst(current);
            current = current.parent;
        }
        solution.addFirst(Play.start);
        System.out.print("((( ");
        //print series of moves
        while(!solution.isEmpty()){
            Node first = solution.removeFirst();
            System.out.print(Node.getDirection(first, solution.peek())+ " ");
        //print number of moves
        }
        System.out.print(") " + depth + " ) " + 
                        (Open+Ignored+Closed) + 
                        " " + Ignored + 
                        " " + Open + 
                        " " + Closed + " )\n");
    }

           
    /*
     * dfs(Node start, Node goal)
     * runs depth first search on the start/end states
     * had to implemented iteratively, due to stack overflow errors 
     * in the recursive version
     */
    public static String dfs(Node start, Node goal, int depth){
        Stack<Node> open = new Stack<Node>();
        Stack<Node> closed = new Stack<Node>();
        Vector<Point> spaces = new Vector<Point>();
        
        //add the start state to the open stack
        open.push(start);
        
        //loop until no more open nodes exist
        while(!open.empty()){
            
            //check the current open node
            Node current = open.pop();
            if (current.compareTo(goal) == 0){
                // Count the number of open nodes
                Iterator itr = open.iterator();
                while (itr.hasNext()){
                    Node first = (Node) itr.next();
                    Open++;
                }
                printStats(current, "DFS");
                return "";
            }
            else{
                if (current.depth>=depth){
                }
                else{
                
                    //move the current node to closed stack
                    closed.push(current);
                    Closed++;
                    //for each potential next space
                    Iterator itr = current.getValidMoves().iterator();
                    while(itr.hasNext()){
                        Point space = (Point)itr.next();
                        Node child = Node.buildNextNode(current, space);
                        // search open and closed stack for potential child
                        if ((open.search(child) == -1) && 
                             (closed.search(child) == -1)){
                            //add those not in either stack to the vector toAdd
                            open.push(child);
                        }
                        else
                            Ignored++;
                    }   
                }    
            }
        }
        return ("Failed. Try increasing the search boundary.");
    }

    /*
     * bfs(Node start, Node goal)
     * runs breadth first search on the start/end states
     */
    public static String  bfs(Node start, Node goal){
        Queue<Node> open = new LinkedList<Node>();
        Queue<Node> closed = new LinkedList<Node>();
        Vector<Point> spaces = new Vector<Point>();
        
        //add the start state to the open
        open.add(start);
        
        // loop while open is not empty
        while(!open.isEmpty()){
            
            //check the current open node
            Node current = open.remove();
            if (current.compareTo(goal) == 0){
                // Count the number of open nodes
                while (!open.isEmpty()){
                    Node removed = open.remove();
                    Open++;
                }
                printStats(current, "BFS");
                return "";
            }

            else{

                //move the current node to closed
                closed.add(current);
                Closed++;
                
                //for each potential next space
                Iterator itr = current.getValidMoves().iterator();
                while(itr.hasNext()){
                    Point space = (Point)itr.next();
                    Node child = Node.buildNextNode(current, space);
                    // search open and closed for potential child
                    if ((!open.contains(child)) && 
                         (!closed.contains(child))){
                        //add those not in either to open
                        open.add(child);                    }
                    else
                        Ignored++;
                }      
            }
        }
        return ("Failed. The start state and goal state are not compatible");
    }

    /*
     * ufcs(start, goal)
     * runs uniform cost search on the start and goal states
     * implementation is similar to bfs(start, goal), except
     * if a child node has the same state as an open node, but
     * the child has a smaller depth, the child node replaces
     * the existing node
     */
    public static String ufcs(Node start, Node goal){
        LinkedList<Node> open = new LinkedList<Node>();
        LinkedList<Node> closed = new LinkedList<Node>();
                
        //add the start state to the open
        open.add(start);
        
        // loop while open is not empty
        while(!open.isEmpty()){
            
            //check the current open node
            Node current = open.remove();
            if (current.compareTo(goal) == 0){
                // Count the number of open nodes
                while (!open.isEmpty()){
                    Node removed = open.remove();
                    Open++;
                }
                printStats(current, "Uniform Cost Search");
                return "";
            }

            else{

                //move the current node to closed stack
                closed.add(current);
                Closed++;
                
                //for each potential next space
                Iterator itr = current.getValidMoves().iterator();
                while(itr.hasNext()){
                    Point space = (Point)itr.next();
                    Node child = Node.buildNextNode(current, space);
                    // search open and closed stack for potential child
                    if (!closed.contains(child)){
                        if (!open.contains(child)){
                            //add those not in either list to open
                            open.add(child);
                        }
                        else if(open.contains(child)){
                            //replace existing node with the newer, shallower node
                            int index = open.indexOf(child);
                            if (open.get(index).depth > child.depth){
                                Node deleted = open.remove(index);
                                int size = open.size();
                                for(int i=0; i<size; i++){
                                    if (child.depth<open.get(i).depth){
                                        //insert the node into the open list
                                        open.add(i, child);
                                        break;
                                    }
                                }
                            }
                            Ignored++;
                        }
                    }
                    else
                        Ignored++;
                }      
            }
        }
        return ("Failed. The start state and goal state are not compatible.");
    }

    /*
     * idfs(Node start, Node goal)
     * calls dfs(start, goal, 0)
     * if that fails, it increases depth by 1, ad infinitum, until it works
     */
    public static String idfs(Node start, Node goal){
        System.out.print("Iterative ");
        int depth = 0;
        while (true){
            String result = dfs(start, goal, depth);
            if (result == "")
                return "";
            depth++;
        }
    }

    /*
     * greedyMT(Node start, Node goal)
     * performs the greedy-best-first search on the start and goal states
     * uses the class heuristic misplacedTiles(Node A, Node B)
     */
    public static String greedyMT(Node start, Node goal){
        LinkedList<Node> open = new LinkedList<Node>();
        LinkedList<Node> closed = new LinkedList<Node>();
                
        //add the start state to the open stack
        open.add(start);
        
        // loop while open is not empty
        while(!open.isEmpty()){
            
            //check the current open node
            Node current = open.remove();
            if (current.compareTo(goal) == 0){
                // Count the number of open nodes
                while (!open.isEmpty()){
                    Node removed = open.remove();
                    Open++;
                }
                printStats(current, "Greedy (Class Heuristic - Misplaced Tiles)");
                return "";
            }

            else{

                //move the current node to closed list
                closed.add(current);
                Closed++;
                
                //for each potential next space
                Iterator itr = current.getValidMoves().iterator();
                while(itr.hasNext()){
                    Point space = (Point)itr.next();
                    Node child = Node.buildNextNode(current, space);
                    // search open and closed list for potential child
                    if (!closed.contains(child)){
                        if (!open.contains(child)){
                            //find the appropriate index to insert
                            int size = open.size();
                            if (size == 0)
                                open.add(child);
                            else{
                                for(int i=0; i<size; i++){
                                    if (Node.compareMT(child, open.get(i), goal) <= 0){
                                        //insert the node into the open list
                                        open.add(i, child);
                                        break;
                                    }
                                }
                            }
                        }
                        else if(open.contains(child)){
                            //replace existing node with the newer, shallower node
                            int index = open.indexOf(child);
                            if (open.get(index).depth<child.depth){
                                open.set(index, child);
                                Ignored++;
                            }
                        }
                    }
                    else
                        Ignored++;
                }      
            }
        }
        return ("Greedy (Class Heuristic) Failed");
    }
    /*
     * greedyMO(Node start, Node goal)
     * performs the greedy-best-first search on the start and goal states
     * uses my own heuristic movesAway(Node A, Node B)
     */
    public static String greedyMO(Node start, Node goal){
        LinkedList<Node> open = new LinkedList<Node>();
        LinkedList<Node> closed = new LinkedList<Node>();
                
        //add the start state to the open stack
        open.add(start);
        
        // loop while open is not empty
        while(!open.isEmpty()){
            
            //check the current open node
            Node current = open.remove();
            if (current.compareTo(goal) == 0){
                // Count the number of open nodes
                while (!open.isEmpty()){
                    Node removed = open.remove();
                    Open++;
                }
                printStats(current, "Greedy (My Heuristic)");
                return "";
            }

            else{

                //move the current node to closed list
                closed.add(current);
                Closed++;
                
                //for each potential next space
                Iterator itr = current.getValidMoves().iterator();
                while(itr.hasNext()){
                    Point space = (Point)itr.next();
                    Node child = Node.buildNextNode(current, space);
                    // search open and closed list for potential child
                    if (!closed.contains(child)){
                        if (!open.contains(child)){
                            //find the appropriate index to insert
                            int size = open.size();
                            if (size == 0)
                                open.add(child);
                            else{
                                for(int i=0; i<size; i++){
                                    if (Node.compareMO(child, open.get(i), goal) <= 0){
                                        //insert the node into the open list
                                        open.add(i, child);
                                        break;
                                    }
                                }
                            }
                        }
                        else if(open.contains(child)){
                            //replace existing node with the newer, shallower node
                            int index = open.indexOf(child);
                            if (open.get(index).depth<child.depth){
                                open.set(index, child);
                                Ignored++;
                            }
                        }
                    }
                    else
                        Ignored++;
                }      
            }
        }
        return ("Greedy (My Heuristic) Failed");
    }
    /*
     * aStar(Node start, Node goal)
     * performs the A* search on the start and goal states
     * uses the class heuristic misplacedTiles(Node A, Node B)
     */
    public static String aStarMT(Node start, Node goal){
        LinkedList<Node> open = new LinkedList<Node>();
        LinkedList<Node> closed = new LinkedList<Node>();
                
        //add the start state to the open stack
        open.add(start);
        
        // loop while open is not empty
        while(!open.isEmpty()){
            
            //check the current open node
            Node current = open.remove();
            if (current.compareTo(goal) == 0){
                // Count the number of open nodes
                while (!open.isEmpty()){
                    Node removed = open.remove();
                    Open++;
                }
                printStats(current, "A* (Class)");
                return "";
            }

            else{

                //move the current node to closed list
                closed.add(current);
                Closed++;
                
                //for each potential next space
                Iterator itr = current.getValidMoves().iterator();
                while(itr.hasNext()){
                    Point space = (Point)itr.next();
                    Node child = Node.buildNextNode(current, space);
                    // search open and closed list for potential child
                    if (!closed.contains(child)){
                        if (!open.contains(child)){
                            //find the appropriate index to insert
                            int size = open.size();
                            if (size == 0)
                                open.add(child);
                            else{
                                for(int i=0; i<size; i++){
                                    if ((Node.misplacedTiles(child,goal)+child.depth)<=(Node.misplacedTiles(open.get(i),goal)+open.get(i).depth)){
                                        //insert the node into the open list
                                        open.add(i, child);
                                        break;
                                    }
                                }
                            }
                        }
                        else if(open.contains(child)){
                            //replace existing node with the newer, shallower node
                            int index = open.indexOf(child);
                            if (open.get(index).depth<child.depth){
                                open.set(index, child);
                                Ignored++;
                            }
                        }
                    }
                    else
                        Ignored++;
                }      
            }
        }
        return ("A* (Class) Failed");
    }
    /*
     * aStar(Node start, Node goal)
     * performs the A* search on the start and goal states
     * uses my heuristic
     */
    public static String aStarMO(Node start, Node goal){
        LinkedList<Node> open = new LinkedList<Node>();
        LinkedList<Node> closed = new LinkedList<Node>();
                
        //add the start state to the open stack
        open.add(start);
        
        // loop while open is not empty
        while(!open.isEmpty()){
            
            //check the current open node
            Node current = open.remove();
            if (current.compareTo(goal) == 0){
                // Count the number of open nodes
                while (!open.isEmpty()){
                    Node removed = open.remove();
                    Open++;
                }
                printStats(current, "A* (My Heuristic)");
                return "";
            }

            else{

                //move the current node to closed list
                closed.add(current);
                Closed++;
                
                //for each potential next space
                Iterator itr = current.getValidMoves().iterator();
                while(itr.hasNext()){
                    Point space = (Point)itr.next();
                    Node child = Node.buildNextNode(current, space);
                    // search open and closed list for potential child
                    if (!closed.contains(child)){
                        if (!open.contains(child)){
                            //find the appropriate index to insert
                            int size = open.size();
                            if (size == 0)
                                open.add(child);
                            else{
                                for(int i=0; i<size; i++){
                                    if ((Node.myOwn(child,goal)+child.depth)<=(Node.myOwn(open.get(i),goal)+open.get(i).depth)){
                                        //insert the node into the open list
                                        open.add(i, child);
                                        break;
                                    }
                                }
                            }
                        }
                        else if(open.contains(child)){
                            //replace existing node with the newer, shallower node
                            int index = open.indexOf(child);
                            if (open.get(index).depth<child.depth){
                                open.set(index, child);
                                Ignored++;
                            }
                        }
                    }
                    else
                        Ignored++;
                }      
            }
        }
        return ("A* (My Heuristic) Failed");
    }

}

package Assignment1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import com.sun.corba.se.spi.orbutil.fsm.Action;

/**
 * @author abhanshu 
 * This class is a template for implementation of 
 * HW1 for CS540 section 2
 */
/**
 * Data structure to store each node.
 */
class Location {
	private int x;
	private int y;
	private Location parent;

	public Location(int x, int y, Location parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Location getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Location) {
			Location loc = (Location) obj;
			return loc.x == x && loc.y == y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * (hash + x);
		hash = 31 * (hash + y);
		return hash;
	}
}

public class KingsKnightmare {
	// represents the map/board
	private static boolean[][] board;
	// represents the goal node
	private static Location king;
	// represents the start node
	private static Location knight;
	// y dimension of board
	private static int n;
	// x dimension of the board
	private static int m;
	// positions that knight can move to
	private static int[][] actions = { { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }, { -2, -1 }, { -1, -2 }, { 1, -2 },
			{ 2, -1 } };

	// enum defining different algo types
	enum SearchAlgo {
		BFS, DFS, ASTAR;
	}

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			// loads the input file and populates the data variables
			SearchAlgo algo = loadFile(args[0]);
			if (algo != null) {
				switch (algo) {
				case DFS:
					executeDFS();
					break;
				case BFS:
					executeBFS();
					break;
				case ASTAR:
					executeAStar();
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * Implementation of Astar algorithm for the problem
	 */
	private static void executeAStar() {
		// TODO: Implement A* algorithm in this method
		PriorityQ<Location> frontier = new PriorityQ<Location>();
		boolean[][] explored = new boolean[n][m];
		frontier.add(knight, 0);
		frontier.add(king, -999);
		Location endState = king;
		searchLoop:
			while(!frontier.isEmpty()) {			
				Location currentState = frontier.poll().getKey();
				if(currentState.equals(king)) {
					endState = currentState;
					break searchLoop;
				}else {
					explored[currentState.getY()][currentState.getX()]=true;
					ArrayList<Location> successors = successors(currentState);
					for (Location s :successors) {
						//Location s = successors.get(i);
						boolean isExplored = explored[s.getY()][s.getX()];
						boolean isFrontier = false;
						if(frontier.exists(s)) {
							isFrontier = true;
							if(frontier.getPriorityScore(s)>function(s)) {
								frontier.modifyEntry(s, function(s));					
							}
						}
						if (!isExplored && !isFrontier) {
							frontier.add(s, function(s));
						}
					}
				}
			}
		printPath(endState, explored);
	}
	/**
	 * Implementation of BFS algorithm
	 */
	private static void executeBFS() {
		// TODO: Implement bfs algorithm in this method
		Queue<Location> frontier = new LinkedList<Location>();
		boolean[][] explored = new boolean[n][m];
		frontier.add(knight);
		Location endState = king;
		searchLoop:
			while(!frontier.isEmpty()){
				Location currentState = frontier.poll();
				explored[currentState.getY()][currentState.getX()]=true;			
				if (currentState.equals(king)) {
					endState = currentState;
					break searchLoop;
				} else {
					ArrayList<Location> successors = successors(currentState);
					// add successors to frontier if not explored and not in frontier
					for (Location s : successors) {
						if(s.equals(king)) {
							endState=s;
							break searchLoop;
						}
						boolean isExplored = explored[s.getY()][s.getX()];
						boolean isFrontier = false;
						for(Location f: frontier) {
							if(s.equals(f)) {
								isFrontier = true;
							}
						}
						if (!isExplored && !isFrontier) {
							frontier.add(s);
						}
					}
				}
			}
		printPath(endState, explored);
	}

	/**
	 * Implemention of DFS algorithm
	 */
	private static void executeDFS() {
		// TODO: Implement dfs algorithm in this method
		Stack<Location> frontier = new Stack<Location>();
		boolean[][] explored = new boolean[n][m];
		frontier.push(knight);
		Location endState = king;		
		searchLoop:
			while (!frontier.isEmpty()) {
				Location currentState = frontier.pop();
				explored[currentState.getY()][currentState.getX()]=true;
				if (currentState.equals(king)) {
					endState = currentState;
					break searchLoop;
				} else {
					ArrayList<Location> successors = successors(currentState);
					// add successors to frontier if not explored and not in frontier
					for (Location s : successors) {
						if(s.equals(king)) {
							endState=s;
							break searchLoop;
						}
						boolean isExplored = explored[s.getY()][s.getX()];
						boolean isFrontier = false;
						for(Location f: frontier) {
							if(s.equals(f)) {
								isFrontier = true;
							}
						}
						if (!isExplored && !isFrontier) {
							frontier.push(s);
						}
					}
				}
			}
		printPath(endState, explored);
	}
	/**
	 * This methods calculates f(n)
	 * @param currentState current node
	 * @return integer f(n)
	 */
	private static int function(Location currentState) {
		return calcH(currentState)+calcG(currentState);
	}
	private static int calcH(Location currentState) {
		int h = Math.abs(currentState.getX()-king.getX())+Math.abs(currentState.getY()-king.getY());
		return h;
	}
	private static int calcG(Location currentState) {
		int g = 0;
		while(currentState.getParent()!=null) {
			g+=3;
			currentState = currentState.getParent();
		}
		return g;
	}
	/**
	 * This method prints all output info needed
	 * @param endState the last state of search
	 * @param explored
	 */
	private static void printPath(Location endState, boolean[][] explored) {
		if (endState.getParent() == null) {
			System.out.println("NOT REACHABLE");
		} else {
			Stack<String> positions = new Stack<String>();
			Location state = endState;
			while(state!=null) {
				positions.push(state.toString());
				state = state.getParent();
			};
			while(!positions.empty()) {
				System.out.println(positions.pop());
			}
		}
		System.out.println("Expanded Nodes: " + countExplored(explored));
	}
	/**
	 * This method count how many nodes are explored
	 * @param explored
	 * @return count number as an integer 
	 */
	private static int countExplored(boolean[][] explored) {
		int count = 0;
		for(boolean[] y:explored) {
			for(boolean x: y) {
				if(x) {
					count++;
				}
			}
		}
		return count;
	}
	/**
	 * This method generate successors
	 * 
	 * @param currentState
	 *            current location
	 * @return ArrayList of Location objects; set of successor locations
	 */
	private static ArrayList<Location> successors(Location currentState) {
		ArrayList<Location> successors = new ArrayList<Location>();
		int x = currentState.getX();
		int y = currentState.getY();
		// for all available moves, check if it is successor
		for (int i = 0; i < 8; i++) {
			int newX = x + actions[i][0];
			int newY = y + actions[i][1];
			boolean isValid = isValid(newX, newY);
			if (isValid) {
				successors.add(new Location(newX, newY, currentState));
			}
		}
		return successors;
	}

	/**
	 * This method check if a location is valid
	 * 
	 * @param x
	 *            X location
	 * @param y
	 *            Y location
	 * @return boolean
	 * 
	 */
	private static boolean isValid(int x, int y) {
		// check if location is out of the board
		if (x < 0 || x > board[0].length - 1) {
			return false;
		}
		if (y < 0 || y > board.length - 1) {
			return false;
		}
		// check if location has obstacles
		if (board[y][x]) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param filename
	 * @return Algo type This method reads the input file and populates all the data
	 *         variables for further processing
	 */
	private static SearchAlgo loadFile(String filename) {
		File file = new File(filename);
		try {
			Scanner sc = new Scanner(file);
			SearchAlgo algo = SearchAlgo.valueOf(sc.nextLine().trim().toUpperCase());
			n = sc.nextInt();
			m = sc.nextInt();
			sc.nextLine();
			board = new boolean[n][m];
			for (int i = 0; i < n; i++) {
				String line = sc.nextLine();
				for (int j = 0; j < m; j++) {
					if (line.charAt(j) == '1') {
						board[i][j] = true;
					} else if (line.charAt(j) == 'S') {
						knight = new Location(j, i, null);
					} else if (line.charAt(j) == 'G') {
						king = new Location(j, i, null);
					}
				}
			}
			sc.close();
			return algo;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

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
	//represents the map/board
	private static boolean[][] board;
	//represents the goal node
	private static Location king;
	//represents the start node
	private static Location knight;
	//y dimension of board
	private static int n;
	//x dimension of the board
	private static int m;
	//enum defining different algo types
	enum SearchAlgo{
		BFS, DFS, ASTAR;
	}

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			//loads the input file and populates the data variables
			SearchAlgo algo = loadFile(args[0]);
			if (algo != null) {
				switch (algo) {
					case DFS :
						executeDFS();
						break;
					case BFS :
						executeBFS();
						break;
					case ASTAR :
						executeAStar();
						break;
					default :
						break;
				}
			}
		}
	}

	/**
	 * Implementation of Astar algorithm for the problem
	 */
	private static void executeAStar() {
		//TODO: Implement A* algorithm in this method
	}

	/**
	 * Implementation of BFS algorithm
	 */
	private static void executeBFS() {
		//TODO: Implement bfs algorithm in this method
	}
	
	/**
	 * Implemention of DFS algorithm
	 */
	private static void executeDFS() {
		//TODO: Implement dfs algorithm in this method
	}
	
	/**
	 * 
	 * @param filename
	 * @return Algo type
	 * This method reads the input file and populates all the 
	 * data variables for further processing
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

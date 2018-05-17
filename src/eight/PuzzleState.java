

import java.util.ArrayList;
import java.util.Arrays;

public class PuzzleState {

	public final int PUZZLE_SIZE = 9;
	public String lastMove;
	public int outOfPlace = 0;

	public int manDist = 0;

	public final int[] GOAL = new int[]{ 1, 2, 3, 8, 0, 4, 7, 6, 5 };
	public int[] curBoard;

	public PuzzleState(int[] board, String m) {
		curBoard = board;
		lastMove = m;
		setOutOfPlace();
		setManDist();
	}

	/**
	 * How much it costs to come to this state
	 */
	public double findCost() {
		return 1;
	}

	/*
	 * Set the 'tiles out of place' distance for the current board
	 */
	public void setOutOfPlace() {
		for (int i = 0; i < curBoard.length; i++) {
			if (curBoard[i] != GOAL[i]) {
				outOfPlace++;
			}
		}
	}

	/*
	 * Set the Manhattan Distance for the current board
	 */
	public void setManDist() {
		// linearly search the array independent of the nested for's below
		int index = -1;

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				index++;
				int rl = 4;
				//the real position
				if (curBoard[index] == 1) {
					rl = 0;
				} else if (curBoard[index] == 2) {
					rl = 1;
				} else if (curBoard[index] == 3) {
					rl = 2;
				} else if (curBoard[index] == 4) {
					rl = 5;
				} else if (curBoard[index] == 5) {
					rl = 8;
				} else if (curBoard[index] == 6) {
					rl = 7;
				} else if (curBoard[index] == 7) {
					rl = 6;
				} else if (curBoard[index] == 8) {
					rl = 3;
				}

				if (rl != 4) {
					// Horizontal offset, mod the tile value by the horizontal
					// dimension
					int horiz = rl % 3;
					// Vertical offset, divide the tile value by the vertical
					// dimension
					int vert = rl / 3;
					manDist += Math.abs(vert - (y)) + Math.abs(horiz - (x));
				}
			}
		}
	}

	/*
	 * Attempt to locate the "0" spot on the current board
	 * 
	 * @return the index of the "hole" (or 0 spot)
	 */
	public int Hole() {
		// The "hole" should always exist
		int holeIndex = -1;

		for (int i = 0; i < PUZZLE_SIZE; i++) {
			if (curBoard[i] == 0) {
				holeIndex = i;
			}
		}
		return holeIndex;
	}

	
	

	/**
	* Getter for last move
	* 
	* @return LastMove
	**/
	public String LastMove() {
		return lastMove;
	}

	
	

	/**
	 * Generate the possible next states of current states
	 * 
	 * @return an ArrayList containing all of the successors for that state
	 */
	public ArrayList<PuzzleState> genSuccessors() {
		ArrayList<PuzzleState> successors = new ArrayList<PuzzleState>();
		int hole = Hole();

		// slide left
		if (hole != 0 && hole != 3 && hole != 6) {
			NextState(hole - 1, hole, successors, "left");
		}

		// slide down
		if (hole != 6 && hole != 7 && hole != 8) {
			NextState(hole + 3, hole, successors, "down");
		}

		// slide up
		if (hole != 0 && hole != 1 && hole != 2) {
			NextState(hole - 3, hole, successors, "up");
		}
		// slide right
		if (hole != 2 && hole != 5 && hole != 8) {
			NextState(hole + 1, hole, successors, "right");
		}

		return successors;
	}

	public void NextState(int i1, int i2, ArrayList<PuzzleState> s, String move) {
		int[] cpy = Arrays.copyOf(curBoard, PUZZLE_SIZE);
		int temp = cpy[i1];
		cpy[i1] = cpy[i2];
		cpy[i2] = temp;
		s.add((new PuzzleState(cpy, move)));
	}

	/**
	 * Check to see if the current state is the goal state.
	 * 
	 * @return - true or false, depending on whether the current state matches
	 *         the goal
	 */
	public boolean isGoal() {
		return Arrays.equals(curBoard, GOAL);
	}

	/**
	 * Method to print out the current state. Prints the puzzle board.
	 */
	public void printState() {
		System.out.println(curBoard[0] + " | " + curBoard[1] + " | "
				+ curBoard[2]);
		System.out.println("---------");
		System.out.println(curBoard[3] + " | " + curBoard[4] + " | "
				+ curBoard[5]);
		System.out.println("---------");
		System.out.println(curBoard[6] + " | " + curBoard[7] + " | "
				+ curBoard[8]);

	}

	/**
	 * Overloaded equals method to compare two states.
	 * 
	 * @return true or false, depending on whether the states are equal
	 */
	public boolean equals(PuzzleState s) {
		return Arrays.equals(curBoard, ((PuzzleState) s).getCurBoard());
	}

	/**
	 * Getter to return the current board array
	 * 
	 * @return the curState
	 */
	public int[] getCurBoard() {
		return curBoard;
	}
}

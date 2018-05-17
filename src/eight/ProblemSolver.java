

import java.util.*;

public class ProblemSolver
{

	public static void main(String[] args) {
		int inputType = 0;
		int puzzleStart = 1;
		final int[] GOAL = new int[]{ 1, 2, 3, 8, 0, 4, 7, 6, 5 };
		// Print out correct usage and end the program if there aren't any
		// parameters
		if (args.length < 1) {
			printUsage();
		}

		System.out.println("Search Type: "
					+ args[inputType].toUpperCase());
		

		String type = args[inputType];//.toLowerCase();
		System.out.println(args.length);
		if (args.length > 2) {
			int[] startingStateBoard = dispatchInput(args,
					puzzleStart);
			
			if (Arrays.equals(startingStateBoard, GOAL)) {
				System.out.println("Input is the final goal!");
				return;
			}
			
			if (type.equals("dfs")) {
				// Use DFSearch.java
				DFSearch ds = new DFSearch();
				ds.search(startingStateBoard, -1);
			} else if (type.equals("ids")) {
				DFSearch ds = new DFSearch();
				ds.search(startingStateBoard, 1);
			} else if (type.equals("bfs")) {
				// Use BFSearch.java
				BFSearch bs = new BFSearch();
				bs.search(startingStateBoard);
			} else if (type.equals("A1")) {
				// Use A* Search.java h1=number of tiles out-of-place
				
				AStarSearch as = new AStarSearch();
				as.search(startingStateBoard, 'o');}
				
				else if (type.equals("A2")) {
					// Use A* Search.java h1=number of tiles out-of-place
					AStarSearch as = new AStarSearch();
					as.search(startingStateBoard, 'm');
					
			} else if (type.equals("IDA1")) {
				// Use IDAStarSearch  
				IDAStar is = new IDAStar();
				is.search(startingStateBoard, 'o');
			} 
			else if (type.equals("IDA2")) {
				// Use IDAStarSearch  
				IDAStar is = new IDAStar();
				is.search(startingStateBoard, 'm');
			} 
			else if (type.equals("greedy")) {
		
				GBFSearch gs = new GBFSearch();
				gs.search(startingStateBoard);
			} else {
				//invalid pattern
				printUsage();
			}
		} else {
			printUsage();
		}
	}

	// Helper method to print the correct usage and end the program
	private static void printUsage() {
		System.out.println("Usage: java ProblemSolver <searchType> [Initial Puzzle State]");
		System.exit(-1);
	}

	private static int[] dispatchInput(String[] a, int d) {
		int[] initState = new int[9];
		for (int i = d; i < a.length; i++) {
			initState[i - d] = Integer.parseInt(a[i]);
		}
		return initState;
	}
}
//BFSearch  It starts at the tree root and explores the neighbor nodes first, 
//before moving to the next level neighbors.
class BFSearch {

	public void search(int[] board) {
		SearchNode root = new SearchNode(new PuzzleState(board, ""));
		Queue<SearchNode> queue = new LinkedList<SearchNode>();

		queue.add(root);

		performSearch(queue);
	}

	/*
	 * Helper method to check to see if a SearchNode has already been evaluated.
	 * Returns true if it has, false if it hasn't.
	 */
	private boolean checkRepeats(SearchNode n) {
		boolean retValue = false;
		SearchNode checkNode = n;

		// While n's parent isn't null, check to see if it's equal to the node
		// we're looking for.
		while (n.parent!= null && !retValue) {
			if (n.parent.curState.equals(checkNode.curState)) {
				retValue = true;
			}
			n = n.parent;
		}

		return retValue;
	}

	/**
	 * Performs a BFSearch using q as the search space
	 */
	public void performSearch(Queue<SearchNode> q) {
		int searchCount = 1; // counter for number of iterations

		while (!q.isEmpty()) {
			SearchNode tempNode = (SearchNode) q.poll();

			if (!tempNode.curState.isGoal()) { 
				// if tempNode is not the goal
				ArrayList<PuzzleState> tempSuccessors = tempNode.curState.genSuccessors();
				/*
				 * Loop through the possible next state
				 */
				for (int i = 0; i < tempSuccessors.size(); i++) {
					SearchNode newNode = new SearchNode(tempNode,
							tempSuccessors.get(i), tempNode.gCost
									+ tempSuccessors.get(i).findCost(), 0);

					if (!checkRepeats(newNode)) {
						q.add(newNode);
					}
				}
				searchCount++;
			} else {
				// goal state
				Stack<SearchNode> solutionPath = new Stack<SearchNode>();
				Stack<String> totalMove = new Stack<String>();
				solutionPath.push(tempNode);
				totalMove.push(tempNode.curState.lastMove);
				tempNode = tempNode.parent;

				while (tempNode.parent!= null) {
					solutionPath.push(tempNode);
					totalMove.push(tempNode.curState.lastMove);
					tempNode = tempNode.parent;
				}
				solutionPath.push(tempNode);

				int loopSize = solutionPath.size();

				for (int i = 0; i < loopSize; i++)
				{
					tempNode = solutionPath.pop();
					tempNode.curState.printState();
					System.out.println();
					System.out.println();
				}

				//reverse the move
				int moveSize = totalMove.size();
				ArrayList<String> moveList = new ArrayList<String>();
				for (int i = 0; i < moveSize; i++) {
					moveList.add(totalMove.pop());
				}
				//print the move
				String printMove = "";
				for (int i = 0; i < moveList.size(); i++) {
					printMove += moveList.get(i) + " ";
				}
				System.out.println(printMove + " \n");

				System.out.println("The cost was: " + tempNode.gCost);
				System.out.println("The number of nodes visited: "+ searchCount);

				System.exit(0);
			}
		}

		// This should never happen with our current puzzles.
		System.out.println("Error! No solution found!");
	}
}

//DFSearch:One starts at the root and explores as far as possible 
//along each branch before backtracking. 
class DFSearch {
	
	public void search(int[] board, int depth) {

		SearchNode root = new SearchNode(new PuzzleState(board, ""));
		Stack<SearchNode> stack = new Stack<SearchNode>();

		stack.add(root);

		performSearch(stack, depth);
	}

	/*
	 * Helper method to check to see if a SearchNode has already been evaluated.
	 * Returns true if it has, false if it hasn't.
	 */
	private boolean checkRepeats(SearchNode n) {
		boolean retValue = false;
		SearchNode checkNode = n;

		// While n's parent isn't null, check to see if it's equal to the node
		// we're looking for.
		while (n.parent != null && !retValue) {
			if (n.parent.curState.equals(checkNode.curState)) {
				retValue = true;
			}
			n = n.parent;
		}

		return retValue;
	}

	/**
	 * Performs a DFSearch using q as the search space
	 */
	public void performSearch(Stack<SearchNode> s, int depth) {
		
		int searchCount = 1; // counter for number of iterations
		SearchNode root = s.peek();

		while (!s.isEmpty()) {
			SearchNode tempNode = (SearchNode) s.pop();

			
			if (!tempNode.curState.isGoal()) {
				// if tempNode is not the goal state
				if (depth == -1 || searchCount < depth) {
					ArrayList<PuzzleState> tempSuccessors = tempNode.curState.genSuccessors();

					for (int i = 0; i < tempSuccessors.size(); i++) {
						SearchNode newNode = new SearchNode(tempNode,
								tempSuccessors.get(i), tempNode.gCost
										+ tempSuccessors.get(i).findCost(), 0);

						if (!checkRepeats(newNode)) {
							s.add(newNode);
						}
					}
				} else if (!s.isEmpty()) {
					tempNode = (SearchNode) s.peek();
					if (tempNode.depth == depth) {
						continue;
					} else {
						searchCount = tempNode.depth - 1;
				// DFS ,  when depth=-1, we can say that depth is infinite,so it is the DFS
					}
				} else {
					continue;
				}
				searchCount++;
			} else {
				//goal state
				Stack<SearchNode> solutionPath = new Stack<SearchNode>();
				Stack<String> totalMove = new Stack<String>();
				solutionPath.push(tempNode);
				totalMove.push(tempNode.curState.lastMove);
				tempNode = tempNode.parent;

				while (tempNode.parent != null) {
					solutionPath.push(tempNode);
					totalMove.push(tempNode.curState.lastMove);
					tempNode = tempNode.parent;
				}
				solutionPath.push(tempNode);

				int loopSize = solutionPath.size();
				for (int i = 0; i < loopSize; i++) {
					tempNode = solutionPath.pop();
					tempNode.curState.printState();
					System.out.println();
					System.out.println();
				}

				ArrayList<String> moveList = new ArrayList<String>();

				while (!totalMove.isEmpty()) {
					moveList.add(totalMove.pop());
				}
				for (int i = 0; i < moveList.size(); i++) {
					System.out.print(moveList.get(i) + " ");
				}
				System.out.println("\n");
				System.out.println("The cost was: " + tempNode.gCost);
				System.out.println("The number of nodes visited: " + searchCount);

				System.exit(0);
			}
		}
		if (depth != -1) {
			//IDS a state space/graph search strategy in which a depth-limited version 
			//of depth-first search is run repeatedly with increasing depth limits until the goal is found
			s.push(root);
			performSearch(s, depth + 1);
		} else {
			// This should never happen with our current puzzles.
			System.out.println("Error! No solution found!");
	}
	}
}

//Greedy search use the gCost= number of tiles out-of-place as the compared way.

class GBFSearch {
	public void search(int[] board) {
		SearchNode root = new SearchNode(new PuzzleState(board, ""));

		performSearch(root);
	}

	private boolean checkRepeats(SearchNode n) {
		boolean retValue = false;
		SearchNode checkNode = n;

		// While n's parent isn't null, check to see if it's equal to the node
		// we're looking for.
		while (n.parent != null && !retValue) {
			if (n.parent.curState.equals(checkNode.curState)) {
				retValue = true;
			}
			n = n.parent;
		}

		return retValue;
	}

	private void performSearch(SearchNode node) {
		int searchCount = 1;
		Stack<SearchNode> stack = new Stack<SearchNode>();

		while (!node.curState.isGoal()) {
			
			ArrayList<PuzzleState> tempSuccessors = node.curState.genSuccessors();
			
			//sort the successor list in min heuristic cost way
			Collections.sort(tempSuccessors, new Comparator<PuzzleState>(){
				public int compare(PuzzleState s1, PuzzleState s2) {
					return s1.manDist - s2.manDist;
				}
			});

			//check the right way to go
			for (PuzzleState st : tempSuccessors) {
				SearchNode newNode = new SearchNode(node, st, node.gCost + st.findCost(), 0);
				if (!checkRepeats(newNode)) {
					stack.push(newNode);
					node = newNode;
					break;
				}
			}
			
			searchCount++;
		}
		
		//the goal state will be found after the loop
		Stack<SearchNode> solutionPath = new Stack<SearchNode>();
		int loopSize = stack.size();
		String totalMove = "";
		SearchNode tempNode = null;
		for (int i = 0; i < loopSize; i++) {
			tempNode = stack.pop();
			// tempNode.getCurState().printState();
			// System.out.println();
			// System.out.println();
			solutionPath.push(tempNode);
			totalMove = tempNode.curState.lastMove + " " + totalMove;
		}

		for (int i = 0; i < loopSize; i++) {
			tempNode = solutionPath.pop();
			tempNode.curState.printState();
			System.out.println();
			System.out.println();
		}
		System.out.println(totalMove + "\n");

		System.out.println("The cost was: " + tempNode.gCost);
		System.out.println("The number of nodes visited: "+ searchCount);

		System.exit(0);
	}
}

//AStar use the fCost as the compared way,fCost=gCost+hCost.

 class AStarSearch {

	public void search(int[] board, char heuristic) {

		SearchNode root = new SearchNode(new PuzzleState(board, ""));
		Queue<SearchNode> q = new LinkedList<SearchNode>();
		q.add(root);

		int searchCount = 1; // counter for number of iterations

		while (!q.isEmpty()) {

			SearchNode tempNode = (SearchNode) q.poll();

			if (!tempNode.curState.isGoal()) {
				//tempNode is not goal state
				ArrayList<PuzzleState> tempSuccessors = tempNode.curState.genSuccessors();
				ArrayList<SearchNode> nodeSuccessors = new ArrayList<SearchNode>();

				for (int i = 0; i < tempSuccessors.size(); i++) {
					SearchNode checkedNode;
					// make the node
					if (heuristic == 'o') {
						/*
						 * Create a new SearchNode, with tempNode as the parent,
						 * tempNode's cost + the new cost (1) for this state,
						 * and the Out of Place h(n) value
						 */
						checkedNode = new SearchNode(tempNode,
								tempSuccessors.get(i), tempNode.gCost
										+ tempSuccessors.get(i).findCost(),
								((PuzzleState) tempSuccessors.get(i))
										.outOfPlace);
					} else {
						// See previous comment
						checkedNode = new SearchNode(tempNode,
								tempSuccessors.get(i), tempNode.gCost
										+ tempSuccessors.get(i).findCost(),
								((PuzzleState) tempSuccessors.get(i))
										.manDist);
					}

					// Check for repeats
					if (!checkRepeats(checkedNode)) {
						nodeSuccessors.add(checkedNode);
					}
				}

				// Check to see if nodeSuccessors is empty
				if (nodeSuccessors.size() == 0) {
					continue;
				}

				SearchNode lowestNode = nodeSuccessors.get(0);

				/*
				 * This loop finds the lowest f(n) in a node, and then sets that
				 * node as the lowest.
				 */
				for (int i = 0; i < nodeSuccessors.size(); i++) {
					if (lowestNode.fCost > nodeSuccessors.get(i).fCost) {
						lowestNode = nodeSuccessors.get(i);
					}
				}

				int lowestValue = (int) lowestNode.fCost;

				// Adds any nodes that have that same lowest value.
				for (int i = 0; i < nodeSuccessors.size(); i++) {
					if (nodeSuccessors.get(i).fCost == lowestValue) {
						q.add(nodeSuccessors.get(i));
					}
				}

				searchCount++;
			} else {
				// goal state
				Stack<SearchNode> solutionPath = new Stack<SearchNode>();
				Stack<String> totalMove = new Stack<String>();
				solutionPath.push(tempNode);
				totalMove.push(tempNode.curState.lastMove);
				tempNode = tempNode.parent;

				while (tempNode.parent != null) {
					solutionPath.push(tempNode);
					totalMove.push(tempNode.curState.lastMove);
					tempNode = tempNode.parent;
				}
				solutionPath.push(tempNode);

				// The size of the stack before looping through and emptying it.
				int loopSize = solutionPath.size();

				for (int i = 0; i < loopSize; i++) {
					tempNode = solutionPath.pop();
					tempNode.curState.printState();
					System.out.println();
					System.out.println();
				}

				ArrayList<String> moveList = new ArrayList<String>();
				while (!totalMove.isEmpty()) {
					moveList.add(totalMove.pop());
				}
				for (String mv : moveList) {
					System.out.print(mv + " ");
				}
				System.out.println("\n");
				System.out.println("The cost was: " + tempNode.gCost);
				System.out.println("The number of nodes visited: " + searchCount);

				System.exit(0);
			}
		}

		// This should never happen with our current puzzles.
		System.out.println("Error! No solution found!");

	}

	/*
	 * Helper method to check to see if a SearchNode has already been evaluated.
	 * Returns true if it has, false if it hasn't.
	 */
	private boolean checkRepeats(SearchNode n) {
		boolean retValue = false;
		SearchNode checkNode = n;

		// While n's parent isn't null, check to see if it's equal to the node
		// we're looking for.
		while (n.parent != null && !retValue) {
			if (n.parent.curState.equals(checkNode.curState)) {
				retValue = true;
			}
			n = n.parent;
		}

		return retValue;
	} 
}

  class IDAStar {

 	public void search(int[] board, char heuristic) {

 		SearchNode root = new SearchNode(new PuzzleState(board, ""));
 		Queue<SearchNode> q = new LinkedList<SearchNode>();
 		q.add(root);

 		int fLimit = heuristic == 'o' ? root.curState.outOfPlace : root.curState.manDist;
 		int searchCount = 1; // counter for number of iterations
 		fLimit += 1;

 		while (!q.isEmpty()) {

 			SearchNode tempNode = (SearchNode) q.poll();
 			fLimit += 1;

 			if (!tempNode.curState.isGoal()) {
 				//tempNode is not goal state
 				ArrayList<PuzzleState> tempSuccessors = tempNode.curState.genSuccessors();
 				ArrayList<SearchNode> nodeSuccessors = new ArrayList<SearchNode>();

 				for (int i = 0; i < tempSuccessors.size(); i++) {
 					SearchNode checkedNode;
 					// make the node
 					if (heuristic == 'o') {
 						/*
 						 * Create a new SearchNode, with tempNode as the parent,
 						 * tempNode's cost + the new cost (1) for this state,
 						 * and the Out of Place h(n) value
 						 */
 						checkedNode = new SearchNode(tempNode,
 								tempSuccessors.get(i), tempNode.gCost
 										+ tempSuccessors.get(i).findCost(),
 								((PuzzleState) tempSuccessors.get(i))
 										.outOfPlace);
 					} else {
 						// See previous comment
 						checkedNode = new SearchNode(tempNode,
 								tempSuccessors.get(i), tempNode.gCost
 										+ tempSuccessors.get(i).findCost(),
 								((PuzzleState) tempSuccessors.get(i))
 										.manDist);
 					}
 					if (checkedNode.fCost> fLimit) {
 						continue;
 					}

 					// Check for repeats
 					if (!checkRepeats(checkedNode)) {
 						nodeSuccessors.add(checkedNode);
 					}
 				}

 				// Check to see if nodeSuccessors is empty
 				if (nodeSuccessors.size() == 0) {
 					continue;
 				}

 				SearchNode lowestNode = nodeSuccessors.get(0);

 				/*
 				 * This loop finds the lowest f(n) in a node, and then sets that
 				 * node as the lowest.
 				 */
 				for (int i = 0; i < nodeSuccessors.size(); i++) {
 					if (lowestNode.fCost > nodeSuccessors.get(i).fCost) {
 						lowestNode = nodeSuccessors.get(i);
 					}
 				}

 				int lowestValue = (int) lowestNode.fCost;
 				// Adds any nodes that have that same lowest value.
 				for (int i = 0; i < nodeSuccessors.size(); i++) {
 					if (nodeSuccessors.get(i).fCost == lowestValue) {
 						q.add(nodeSuccessors.get(i));
 					}
 				}

 				searchCount++;
 			} else {
 				// goal state
 				Stack<SearchNode> solutionPath = new Stack<SearchNode>();
 				Stack<String> totalMove = new Stack<String>();
 				solutionPath.push(tempNode);
 				totalMove.push(tempNode.curState.lastMove);
 				tempNode = tempNode.parent;

 				while (tempNode.parent != null) {
 					solutionPath.push(tempNode);
 					totalMove.push(tempNode.curState.lastMove);
 					tempNode = tempNode.parent;
 				}
 				solutionPath.push(tempNode);

 				// The size of the stack before looping through and emptying it.
 				int loopSize = solutionPath.size();

 				for (int i = 0; i < loopSize; i++) {
 					tempNode = solutionPath.pop();
 					tempNode.curState.printState();
 					System.out.println();
 					System.out.println();
 				}

 				ArrayList<String> moveList = new ArrayList<String>();
 				while (!totalMove.isEmpty()) {
 					moveList.add(totalMove.pop());
 				}
 				for (String mv : moveList) {
 					System.out.print(mv + " ");
 				}
 				System.out.println("\n");
 				System.out.println("The cost was: " + tempNode.gCost);
 				System.out.println("The number of nodes visited: " + searchCount);

 				System.exit(0);
 			}
 		}

 		// This should never happen with our current puzzles.
 		System.out.println("Error! No solution found!");

 	}

 	/*
 	 * Helper method to check to see if a SearchNode has already been evaluated.
 	 * Returns true if it has, false if it hasn't.
 	 */
 	private boolean checkRepeats(SearchNode n) {
 		boolean retValue = false;
 		SearchNode checkNode = n;

 		// While n's parent isn't null, check to see if it's equal to the node
 		// we're looking for.
 		while (n.parent != null && !retValue) {
 			if (n.parent.curState.equals(checkNode.curState)) {
 				retValue = true;
 			}
 			n = n.parent;
 		}

 		return retValue;
 	} 
 }

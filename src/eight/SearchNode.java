

public class SearchNode {

	public PuzzleState curState;
	public SearchNode parent;
	public int depth;
	public double gCost; // cost to get to this state
	public double hCost; // heuristic cost
	public double fCost; // f(n) cost

	public SearchNode(PuzzleState s) {
		curState = s;
		parent = null;
		depth = 1;
		gCost = 0;
		hCost = 0;
		fCost = 0;
	}

	public SearchNode(SearchNode prev, PuzzleState s, double c, double h) {
		parent = prev;
		curState = s;
		depth = prev.depth + 1;
		gCost = c;
		hCost = h;
		fCost = gCost + hCost;
	}

}


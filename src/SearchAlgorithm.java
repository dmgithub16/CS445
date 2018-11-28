import java.util.List;
import java.util.Queue;

import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonalGrid;


public abstract class SearchAlgorithm {
	
	protected HexagonalGrid<HexData> hexagonalGrid = null;
	protected final List<Hexagon<HexData>> path = null;
	
	protected Hexagon<HexData> root;
	protected Hexagon<HexData> goal;
	
	protected final int RET_END = 1;
	protected final int RET_FAIL = 2;
	protected final int RET_NONE = 3;
	
	public boolean foundGoal = false;
	public boolean hasSetup = false;
	
	public int steps = 0;
	
	public void getPathData(){
		hexagonalGrid = HexPrimary.hexagonalGrid;
		root = HexPrimary.root;
		goal = HexPrimary.goal;
	}
	
	public abstract boolean search();
	public abstract int step();
	public abstract void setup();
}

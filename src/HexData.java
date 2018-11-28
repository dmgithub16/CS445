

import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.defaults.DefaultSatelliteData;

public class HexData extends DefaultSatelliteData {

    private static final long serialVersionUID = 1335166038345783576L;

    private boolean isSelected = false;
    
	private boolean isPath = false;
	private boolean isRoot = false;
	private boolean isGoal = false;
	private boolean isWall = false;
	private boolean visited = false;
	private double priority = Double.MAX_VALUE;
	private double distance = Double.MAX_VALUE;
	private Hexagon<HexData> parent = null;
	
	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isGoal() {
		return isGoal;
	}

	public void setGoal(boolean isGoal) {
		this.isGoal = isGoal;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	private double weight = 0.0;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}
	
	public double getPriority(){
		return this.priority;
	}
	
	public void setPriority( double priority ){
		this.priority = priority;
	}
	
	public double getDistance(){
		return this.distance;
	}
	
	public void setDistance( double distance ){
		this.distance = distance;
	}
	
	public Hexagon<HexData> getParent(){
		return this.parent;
	}
	
	public void setParent( Hexagon<HexData> parent ){
		this.parent = parent;
	}

}

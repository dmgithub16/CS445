
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.codetome.hexameter.core.api.Hexagon;




public class BreadthFirstSearch extends SearchAlgorithm {

	//The open or frontier hexagons
	PriorityQueue<Hexagon<HexData>> openList;

	//The hexagons we've already looked at
	ArrayList<Hexagon<HexData>> closeList;

	public boolean hasSetup = false;

	BreadthFirstSearch(){

		setupLists();

	}

	public void setupLists(){
		//Comparator for the priority queue
		Comparator<Hexagon<HexData>> comparator = new Comparator<Hexagon<HexData>>() {	// Comparator to find the closest node to the finish 

			//Compares the priority of two hexagons and returns the one with the highest priority
			public int compare(Hexagon<HexData> hex1, Hexagon<HexData> hex2) {

				HexData hex1Data = hex1.getSatelliteData().get();
				HexData hex2Data = hex2.getSatelliteData().get();

				if ( hex1Data.getPriority() < hex2Data.getPriority() ){
					return -1;
				}
				if ( hex1Data.getPriority() > hex2Data.getPriority() ){
					return 1;
				}
				return 0;
			}
		};

		openList = new PriorityQueue<Hexagon<HexData>>( comparator );
		closeList = new ArrayList<Hexagon<HexData>>();
	}

	public void setup(){

		//Update our path data
		getPathData();

		//Ensure our lists are empty
		setupLists();

		hasSetup = true;

		HexData rootData = root.getSatelliteData().get();

		//Our root node does not have a parent
		rootData.setParent( null );

		//Our root node has a distance of 0
		rootData.setDistance( 0 );

		closeList.add( root );
		openList.add( root );	// A* keeps pulling nodes from openlist until it is empty, so adding start to the list is required

	}

	@Override
	public int step(){
		
		steps++;

		//Make sure we've set up for the pathfinding
		if( !hasSetup ){
			setup();
		}

		//Get the next hexagon
		Hexagon<HexData> current = openList.poll();
		HexData currentData = current.getSatelliteData().get();

		//If we have found the goal, stop here
		if( current.equals( goal ) ){

			System.out.println( "Breadth First found goal in: " + steps + " steps" );
			
			//Ensure we don't try to path again
			foundGoal = true;

			//Start from the end and work upwards to the root
			Hexagon<HexData> curHex = goal;

			while( true ){	// This loop looks at curNode's parent and adds that node to the final path, then makes curNode the parent

				HexData curData = curHex.getSatelliteData().get();
				curHex = curData.getParent();

				curData.setPath( true );

				//If we've gotten to the end of the path, stop
				if( curHex.equals( root ) ){
					break;
				}

			}

			HexPrimary.hexPanel.repaint();


			return RET_END;
		}

		//Loop through all of this hexagon's neighbors
		for(Hexagon<HexData> next : hexagonalGrid.getNeighborsOf( current ) ){
			HexData nextData = next.getSatelliteData().get();

			//If we hit a wall, pretend it doesn't exist because we should never path through a wall hexagon
			if( nextData.isWall() ){
				continue;
			}

			//Calculate the cost to the potential next hexagon
			int distanceToNext = HexPrimary.hexagonalGridCalculator.calculateDistanceBetween( current,  next );
			double newCost = currentData.getDistance() + distanceToNext;

			//If we haven't already seen this hexagon, or if the cost to it is lower than it's distance
			if( !closeList.contains(next) || newCost < nextData.getDistance() ){ 
				//If either of those checks pass, read just the current node and other node to be connected
				nextData.setDistance( newCost );	

				//Mark down that we've seen this node already
				closeList.add(next);

				//Set the next hexagon's data
				nextData.setPriority( newCost );

				//Add the next hexagon to the frontier list
				openList.add(next);
				nextData.setVisited( true );
				nextData.setParent( current );

				//Set this hexagon's data after we've changed it
				next.setSatelliteData( nextData );
			}
		}

		HexPrimary.hexPanel.repaint();

		return RET_NONE;
	}

	@Override
	public boolean search() {

		if( !hasSetup ){
			setup();
		}

		//While there are still things in the frontier
		while( !openList.isEmpty() ){
			int outcome = step();

			//If we stepped without ending the search, continue
			if( outcome == RET_NONE ){
				continue;
			}

			//If we found the goal, stop
			if( outcome == RET_END ){
				return true;
			}

			//If we cannot find the goal, stop
			if( outcome == RET_FAIL ){
				return false;
			}

		}

		return false;

	}
}

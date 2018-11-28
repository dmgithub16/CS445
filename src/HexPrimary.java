import static org.codetome.hexameter.core.api.HexagonOrientation.*;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonOrientation;
import org.codetome.hexameter.core.api.HexagonalGrid;
import org.codetome.hexameter.core.api.HexagonalGridBuilder;
import org.codetome.hexameter.core.api.HexagonalGridCalculator;
import org.codetome.hexameter.core.api.HexagonalGridLayout;
import org.codetome.hexameter.core.backport.Optional;


public class HexPrimary {

	//Config variables

	//Grid defaults
	private static final int DEFAULT_GRID_WIDTH = 51;
	private static final int DEFAULT_GRID_HEIGHT = 51;
	private static final int DEFAULT_RADIUS = 10;
	private static final HexagonOrientation DEFAULT_ORIENTATION = POINTY_TOP;
	private static final HexagonalGridLayout DEFAULT_GRID_LAYOUT = RECTANGULAR;

	//Grid config
	public static HexagonalGrid<HexData> hexagonalGrid;
	private static int gridWidth = DEFAULT_GRID_WIDTH;
	private static int gridHeight = DEFAULT_GRID_HEIGHT;
	private static int radius = DEFAULT_RADIUS;
	private static HexagonOrientation orientation = DEFAULT_ORIENTATION;
	private static HexagonalGridLayout hexagonGridLayout = DEFAULT_GRID_LAYOUT;
	public static SearchAlgorithm currentSearch;
	
	//Calculator for distance checks and the like
	public static HexagonalGridCalculator<HexData> hexagonalGridCalculator;

	//JFrame variables
	public static JPanel hexPanel;
	
	//Colors
	private static final Color RED = null;

	//Pathing variables
	static Hexagon<HexData> root;
	static Hexagon<HexData> goal;
	
	//Click modes
	public static final int MODE_ROOT = 1;
	public static final int MODE_GOAL = 2;
	public static final int MODE_WALL = 3;
	
	public static int currentMode = MODE_ROOT;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException{

		//Instantiate our testing search method
		currentSearch = new AStarSearch();
		
		//Set up frame
		final JFrame frame = new JFrame();
		frame.setTitle( "Hexagon Pathfinding" );
		frame.setSize( 1145, 800 );
		frame.setResizable( false );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setLayout( new BorderLayout() );

		//Add hex grid drawing panel
		Container contentPane = frame.getContentPane();
		hexPanel = new DrawingPanel();

		//Options panel
		OptionsPanel optionsPanel = new OptionsPanel();
		
		//Splitting the frame
		contentPane.add( hexPanel, BorderLayout.WEST );
		contentPane.add( optionsPanel, BorderLayout.EAST );
		
		//Setup the grid
		setupGrid();

		//Add a mouse listener
		hexPanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent event) {

				Hexagon<HexData> hex = null;

				hex = hexagonalGrid.getByPixelCoordinate( event.getX(), event.getY() ).get();

				//If we've actually selected a hexagon
				if (hex != null) {

					//The hexagon's data
					HexData data;

					//If we don't have existing satellite data, create a new one for this hex
					if( !hex.getSatelliteData().isPresent() ){
						data = new HexData();
					}else{
						//Otherwise, just get the existing one
						data = hex.getSatelliteData().get();
					}

					//Swap the hexagon's selected state
					data.setSelected(!data.isSelected());

					//Performing mouse actions based on the currently selected mode
					switch( currentMode ){
					
						//If we're in root mode
						case MODE_ROOT:
							//Reset the current root to be normal
							if( root != null && root.getSatelliteData().isPresent() ){
								HexData rootData = root.getSatelliteData().get();
								
								rootData.setRoot( false );
							}
							
							//Set the new root to be the root
							//If we just clicked the root again, we want to unselect it so don't do the selection process
							if( hex.equals( root ) ){
								root = null;
							}else{
								data.setRoot( true );
								
								//Set the root to be the new root
								root = hex;
							}
							break;
							
						//If we're in goal mode
						case MODE_GOAL:
							//Reset the current goal to be normal
							if( goal != null && goal.getSatelliteData().isPresent() ){
								HexData goalData = goal.getSatelliteData().get();
								
								goalData.setGoal( false );
							}
							
							//Set the new goal to be the goal
							//If we just clicked the goal again, we want to unselect it so don't do the selection process
							if( hex.equals( goal ) ){
								goal = null;
							}else{
								data.setGoal( true );
								
								//Set the root to be the new root
								goal = hex;
							}
							break;
						
						//If we're in wall mode
						case MODE_WALL:
							//Toggle this hex's wall-ness
							data.setWall( !data.isWall() );
							break;
							
					}

					//Repaint the panel with the new values
					hexPanel.repaint();
				}
			}
		});
		
		//Finalize JFrame and display
		frame.validate();
		frame.setVisible( true );
	}
	
	//Resets paths and visited
	public static void clearNonWalls(){
		Iterable<Hexagon<HexData>> hexagons = hexagonalGrid.getHexagons();
		for (Hexagon<HexData> hexagon : hexagons) {
			if( hexagon.getSatelliteData().isPresent() ){
				HexData data = hexagon.getSatelliteData().get();
				
				if( data.isPath() ){
					data.setPath( false );
				}
				
				if( data.isVisited() ){
					data.setVisited( false );
				}
			}
		}
		
		hexPanel.repaint();
	}
	
	//Sets up the grid
	public static void setupGrid() throws InstantiationException, IllegalAccessException{
		
		//Set up grid
		hexagonalGrid = null;
		HexagonalGridBuilder<HexData> builder = new HexagonalGridBuilder<HexData>();
		builder.setGridWidth(gridWidth);
		builder.setGridHeight(gridHeight);
		builder.setRadius(radius);
		builder.setOrientation(orientation);
		builder.setGridLayout(hexagonGridLayout);
		hexagonalGrid = builder.build();
		hexagonalGridCalculator = builder.buildCalculatorFor(hexagonalGrid);
		
		//Set up root and goal variables
		root = null;
		goal = null;
		
		currentSearch = currentSearch.getClass().newInstance();
		
		hexPanel.repaint();
	}

}

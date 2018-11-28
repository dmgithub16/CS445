
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

import javax.swing.JPanel;

import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.Point;


public class DrawingPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public DrawingPanel(){
		setPreferredSize( new Dimension( 895, 800 ) );
	}
	
	//Draws the grid
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Draw each hexagon in the grid
		for (Hexagon<HexData> hexagon : HexPrimary.hexagonalGrid.getHexagons()) {

			//X and Y position arrays for the points of this hexagon
			int[] xs = new int[6];
			int[] ys = new int[6];
			List<Point> points = hexagon.getPoints();

			//Initialize the polygon that we will eventually be drawing
			Polygon p = new Polygon();

			//Add the points of the hexagon to its arrays
			for (int i = 0; i < 6; i++) {
				xs[i] = (int) points.get(i).getCoordinateX();
				ys[i] = (int) points.get(i).getCoordinateY();
			}
			
			//Configure the polygon with the points
			p.npoints = 6;
			p.xpoints = xs;
			p.ypoints = ys;
			
			//Set our default color
			g.setColor( Color.LIGHT_GRAY );

			//If this hexagon has satellite data
			if(hexagon.getSatelliteData().isPresent()){

				//If this hexagon is part of the search
				if(hexagon.getSatelliteData().get().isVisited()) {
					//Draw it as Yellow
					g.setColor(Color.YELLOW);
				}
				
				//If this hexagon is part of an unweighted search
				if(hexagon.getSatelliteData().get().isOpaque()) {
					//Draw it as green
					g.setColor(Color.BLUE);
				}
				
				//If this hexagon is part of a path
				if(hexagon.getSatelliteData().get().isPath()) {
					//Draw it as green
					g.setColor(Color.ORANGE);
				}
				
				if(hexagon.getSatelliteData().get().isRoot()){
					g.setColor(Color.RED);
				}
				
				if(hexagon.getSatelliteData().get().isGoal()){
					g.setColor(Color.GREEN);
				}
				
				if(hexagon.getSatelliteData().get().isWall()){
					g.setColor(Color.DARK_GRAY);
				}
			}else{
				hexagon.setSatelliteData( new HexData() );
			}

			//Actually draw the hexagon
			g.fillPolygon( p );
			
			//Draw outlines of the hexagons
			g.setColor( Color.WHITE );
			g.drawPolygon( p );
		}
	}
}

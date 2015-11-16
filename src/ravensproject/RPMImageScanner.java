package ravensproject;


import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;


public class RPMImageScanner {

	private HashMap<Point, Integer> totalPxMap = new HashMap<Point, Integer>();
	private HashMap<Point, Integer> blackPxMap = new HashMap<Point, Integer>();
	private HashMap<Point, Integer> curPxMap = new HashMap<Point, Integer>();
	private ArrayList<HashMap<Point, Integer>> contiguousBodies = new ArrayList<HashMap<Point, Integer>>();
	private BufferedImage curImage;
	
	public RPMImageScanner()
	{
		
	}
	
public void processFigure(RavensFigure rf) {
	
	try { // Required by Java for ImageIO.read      

		//RavensFigure figureA = problem.getFigures().get("A");
		
		this.curImage = ImageIO.read(new File(rf.getVisual()));
		
			//get total pixels, black pixels, and contiguous regions of black pixels
			for(int i = 0 ; i < this.curImage.getWidth() ; i++) 
			{
				//boolean leftMostPx = false;
			    for(int j = 0 ; j < this.curImage.getHeight() ; j++) 
			    {
			    	Point curPoint = new Point(i,j);
			    	int curPixel = this.curImage.getRGB(i,j);
			    	this.getTotalPxMap().put(curPoint, curPixel);
			    	
			    	if(isBlack(curPixel))
			    	{
			    		this.blackPxMap.put(curPoint, curPixel);
			    		//TODO ADD INCREMENT TOTAL BLACK PIXELS
				    	if(!isMappedContiguous(curPoint))
				    	{
				    		//this.getCurPxMap().clear();
				    		mapContiguousToCurPxMap(curPoint, curPixel);
				    		this.getContiguousBodies().add(this.getCurPxMap());
				    		this.getCurPxMap().clear();
				    	}
			    	}
			    }
			}
			
			//scan contiguous regions
			for(HashMap<Point, Integer> cm : this.getContiguousBodies())
			{
				//get leftmost pixels for classification
				
				//classify shape
				
				//if unclassifiable, further decompose region...
				
				//classify fill
					//if pixel exists in a contiguous region but not the current contiguous region
						//not a fill pixle in spite of isBlack() == true;
				
				//get contig WHITE pixels inside black pixels
					//use these to classify !fill if shape white == shape black
				
				
				//stablish midpoints & intervals for position classification
				
			}

			//left-of
			
			//above
			
			//inside
			
			//overlap
			
		} catch(Exception ex) 
	{
			System.out.println("CLOWNS DONE CLOWNED AROUND!!!");
	}

	
}

private void mapContiguousToCurPxMap(Point curPoint, int curPixel) {
	// TODO Auto-generated method stub
	this.curPxMap.put(curPoint, curPixel);
	for(int i = curPoint.x-1; i <= curPoint.x+1; i++)
	{
		for(int j = curPoint.y-1; i <= curPoint.y+1; i++)
		{
			int nextPx = this.curImage.getRGB(i, j);
			Point nextPt = new Point(i, j);
			if(isBlack(curPixel) && !this.curPxMap.containsKey(nextPt))
			{
				mapContiguousToCurPxMap(nextPt, curPixel);
			}
		}	
	}
	
}

private boolean isMappedContiguous(Point curPoint) {
	for(HashMap<Point, Integer> cBody : this.getContiguousBodies())
	{
		if(cBody.containsKey(curPoint))
		{
			return true;
		}
	}
	return false;
}

private static boolean isBlack(int curPixel) {
	// TODO Auto-generated method stub
	return false;
}

public HashMap<Point, Integer> getTotalPxMap() {
	return totalPxMap;
}

public void setTotalPxMap(HashMap<Point, Integer> totalPxMap) {
	this.totalPxMap = totalPxMap;
}

public HashMap<Point, Integer> getCurPxMap() {
	return curPxMap;
}

public void setCurPxMap(HashMap<Point, Integer> curPxMap) {
	this.curPxMap = curPxMap;
}

public ArrayList<HashMap<Point, Integer>> getContiguousBodies() {
	return contiguousBodies;
}

public void setContiguousBodies(ArrayList<HashMap<Point, Integer>> contiguousBodies) {
	this.contiguousBodies = contiguousBodies;
}

}

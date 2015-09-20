package kbai;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.RavensProblem;

public class SemanticNetState {
	private String name;
	//initialize these to avoid ClassNotLoaded exception...
    private HashMap<String, SemanticNetNode> nodes = new HashMap<String, SemanticNetNode>();
    private String[][] xyNodeSpace;
    private String[] xNodeSpace;
    private String[] yNodeSpace;
    private String[] iNodeSpace;
    private HashMap<String, SemanticNetRelationship> sourceRelationships = new HashMap<String, SemanticNetRelationship>();
    private HashMap<String, SemanticNetRelationship> destinationRelationships = new HashMap<String, SemanticNetRelationship>();
    private boolean left_of;
    private boolean above;
    private boolean overlap;
    private boolean inside;

    public SemanticNetState(RavensFigure rf) 
    {
    	for (RavensObject ro : rf.getObjects().values())
	    {
    		//Verify that getName() uniqueness works as expected.
    		SemanticNetNode snn = new SemanticNetNode(ro);
	    	this.nodes.put(ro.getName(), snn);
	    }
    	setName(rf.getName());
    	
    	//determine if positioning is relevant...
		this.left_of = false;
		this.above = false;
		this.overlap = false;
		this.inside = false;
    	for(SemanticNetNode n : this.nodes.values())
    	{
    		if(n.getAttributes().containsKey("left-of"))
    		{
    			this.left_of = true;
    		}
    		if(n.getAttributes().containsKey("above"))
    		{
    			this.above = true;
    		}
    		if(n.getAttributes().containsKey("overlap"))
    		{
    			this.overlap = true;
    		}
    		if(n.getAttributes().containsKey("inside"))
    		{
    			this.inside = true;
    		}
    	}

		int x;
		int y;
    	if(this.left_of && this.above)
    	{
        	this.xyNodeSpace = new String[nodes.size()][nodes.size()];

        	for(SemanticNetNode n : this.nodes.values())
        	{
        		if(n.getAttributes().containsKey("left-of"))
        		{
        			x = (nodes.size() -  n.getAttributes().get("left-of").split(",").length);
        		}
        		else
        		{
        			x = nodes.size();
        		}
        		if(n.getAttributes().containsKey("above"))
        		{
        			y = (nodes.size() -  n.getAttributes().get("above").split(",").length);
        		}
        		else
        		{
        			y = nodes.size();
        		}
        		this.xyNodeSpace[x][y] = n.getName();
        		n.setxCoordinate(x);
        		n.setyCoordinate(y);
        	}
    	}
    	else if (this.left_of ^ this.above) 
    	{
    		if(this.left_of)
    		{
            	this.xyNodeSpace = new String[nodes.size()][0];
            	
            	for(SemanticNetNode n : this.nodes.values())
            	{
            		if(n.getAttributes().containsKey("left-of"))
            		{
            			x = (nodes.size() -  n.getAttributes().get("left-of").split(",").length);
            		}
            		else
            		{
            			x = nodes.size();
            		}
            		this.xyNodeSpace[x][0] = n.getName();
            		n.setxCoordinate(x);
            		n.setyCoordinate(0);
            	}
    		}
    		else
    		{
            	this.xyNodeSpace = new String[0][nodes.size()];

            	for(SemanticNetNode n : this.nodes.values())
            	{
            		if(n.getAttributes().containsKey("left-of"))
            		{
            			y = (nodes.size() -  n.getAttributes().get("left-of").split(",").length);
            		}
            		else
            		{
            			y = nodes.size();
            		}
            		this.xyNodeSpace[0][y] = n.getName();
            		n.setxCoordinate(0);
            		n.setyCoordinate(y);
            	}
    		}
    	}

    	//'inside' logic may need to be revised to check for more than 1 series of nodes with 'inside' attribue per state.
    	
		//check if the current node has other nodes which refer to it
			//if yes, it can be modeled in an inside array. if no, it should be excluded from inside array
    		//use map to associate node names with inside attr?
		int i;
    	if(this.inside)
    	{
        	this.iNodeSpace = new String[nodes.size()];
        	
        	for(SemanticNetNode n : this.nodes.values())
        	{
        		if(n.getAttributes().containsKey("inside"))
        		{
        			//position in arr is a measure of how many 'n' nested a particular node is...
        			i = (nodes.size() -  n.getAttributes().get("inside").split(",").length);
        		}
        		else
        		{
        			//check if the current has other nodes which refer to it
        				//if yes, it can be modeled in an inside array. if no, it should be excluded from inside array
        			i = nodes.size();
        		}
        		this.iNodeSpace[i] = n.getName();
        		n.setiCoordinate(i);
        		n.setyCoordinate(0);
        	}
    	}
    	
    	//If displacment increased, all nodes surprassed by the displaced node should now be above/left-of displaced node.
    		//for each node traversed, remove that node from the displaced node attr and add it to the traversed node attr.
    	//if displacement decreased, all nodes surpassed by the displaced node should be contained in the displaced node's attr.
    		//for each node traversed, remove the displaced node from travdNode attr and add travdNode to displaced node attr.
    	
    	/*for(SemanticNetNode n : this.nodes.values())
    	{
    		for(Entry<String, String> e : n.getAttributes().entrySet())
    		{
    			if()
    			{
    				
    			}
    		}
    	}*/
    	
    	
    }

	public HashMap<String, SemanticNetNode> getNodes() {
		return nodes;
	}

	public void setNodes(HashMap<String, SemanticNetNode> nodes) {
		this.nodes = nodes;
	}
	

	public void inferStateRelationships() {
		//this.nodes = nodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, SemanticNetRelationship> getSourceRelationships() {
		return sourceRelationships;
	}

	public void setSourceRelationships(HashMap<String, SemanticNetRelationship> sourceRelationships) {
		this.sourceRelationships = sourceRelationships;
	}

	public HashMap<String, SemanticNetRelationship> getDestinationRelationships() {
		return destinationRelationships;
	}

	public void setDestinationRelationships(
			HashMap<String, SemanticNetRelationship> destinationRelationships) {
		this.destinationRelationships = destinationRelationships;
	}

	/*for (Map.Entry<String, String> attributeEntry : o.getAttributes().entrySet())
    {
    	
    }*/
}

class StateComparator implements Comparator<SemanticNetState> {
    public int compare(SemanticNetState s1, SemanticNetState s2) {
        return s1.getName().compareTo(s2.getName());
    }
}

class NamePredicate implements Predicate<SemanticNetState>{  
	String name;  
	  public boolean test(SemanticNetState s1){  
	  if(name.equals(s1.getName())){  
	   return true;  
	  }  
	  return false;  
	  }  
	} 

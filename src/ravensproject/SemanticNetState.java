package ravensproject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class SemanticNetState {
	private String name;
	private int sequence;
	//initialize these to avoid ClassNotLoaded exception...
    private HashMap<String, SemanticNetNode> nodes = new HashMap<String, SemanticNetNode>();
    private String[][] xyNodeSpace;
    private String[] xNodeSpace;
    private String[] yNodeSpace;
    private String[] xAlignNodeSpace;
    private String[] yAlignNodeSpace;
    private String[] iNodeSpace;
    private String[] oNodeSpace;
    private HashSet<String> xNodeValues = new HashSet<String>();
    private HashSet<String> yNodeValues = new HashSet<String>();
    private HashSet<String> iNodeValues = new HashSet<String>();
    private HashSet<String> oNodeValues = new HashSet<String>();
    private HashMap<SemanticNetNode, SemanticNetNode> snnMappings = new HashMap<SemanticNetNode, SemanticNetNode>();
    private HashMap<String, SemanticNetRelationship> relativeRelationships = new HashMap<String, SemanticNetRelationship>();
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
    		snn.setStateName(this.getName());
	    	this.nodes.put(ro.getName(), snn);
	    }
    	this.extrapolateRelativeValues();
    	if(Utilities.isNumeric(rf.getName()))
    	{
    		this.setSequence(Integer.parseInt(rf.getName()));
    	}
    	else
    	{
    		//System.out.println(rf.getName());
        	this.setSequence(KnowledgeBase.name2SequenceMap.get(rf.getName()));
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
    			this.xNodeValues.add(n.getAttributes().get("left-of"));
    		}
    		if(n.getAttributes().containsKey("above"))
    		{
    			this.above = true;
    			this.yNodeValues.add(n.getAttributes().get("above"));
    		}
    		if(n.getAttributes().containsKey("overlap"))
    		{
    			this.overlap = true;
    			this.oNodeValues.add(n.getAttributes().get("overlap"));
    		}
    		if(n.getAttributes().containsKey("inside"))
    		{
    			this.inside = true;
    			this.iNodeValues.add(n.getAttributes().get("inside"));
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
        			x = ((nodes.size()-1) -  n.getAttributes().get("left-of").split(",").length);
        		}
        		else
        		{
        			x = nodes.size()-1;
        		}
        		if(n.getAttributes().containsKey("above"))
        		{
        			y = ((nodes.size()-1) -  n.getAttributes().get("above").split(",").length);
        		}
        		else
        		{
        			y = nodes.size()-1;
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
            	this.xyNodeSpace = new String[nodes.size()][1];
            	
            	for(SemanticNetNode n : this.nodes.values())
            	{
            		if(n.getAttributes().containsKey("left-of"))
            		{
            			x = ((nodes.size()-1) -  n.getAttributes().get("left-of").split(",").length);
            		}
            		else
            		{
            			x = nodes.size()-1;
            		}
            		this.xyNodeSpace[x][0] = n.getName();
            		n.setxCoordinate(x);
            		n.setyCoordinate(0);
            	}
    		}
    		else
    		{
            	this.xyNodeSpace = new String[1][nodes.size()];

            	for(SemanticNetNode n : this.nodes.values())
            	{
            		if(n.getAttributes().containsKey("left-of"))
            		{
            			y = ((nodes.size()-1) -  n.getAttributes().get("left-of").split(",").length);
            		}
            		else
            		{
            			y = nodes.size()-1;
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
        			i = ((nodes.size()-1) -  n.getAttributes().get("inside").split(",").length);
            		this.iNodeSpace[i] = n.getName();
            		n.setiCoordinate(i);
            		n.setyCoordinate(0);
        		}
        		else
        		{
        		//	boolean nodeInvolved = false;
        			for(String s : this.iNodeValues)
        			{
        				HashSet<String> valSet = new HashSet<String>(Arrays.asList(s.split(",")));
        				if(valSet.contains(n.getName()))
        				{
        					
                			i = nodes.size()-1;
                    		this.iNodeSpace[i] = n.getName();
                    		n.setiCoordinate(i);
        				}
        			}
        			//check if the current has other nodes which refer to it
        				//if yes, it can be modeled in an inside array. if no, it should be excluded from inside array
        		}
        	}
    	}
    	
		int o;
    	if(this.overlap)
    	{
        	this.oNodeSpace = new String[nodes.size()];
        	
        	for(SemanticNetNode n : this.nodes.values())
        	{
        		if(n.getAttributes().containsKey("overlap"))
        		{
        			//position in arr is a measure of how many 'n' nested a particular node is...
        			o = ((nodes.size()-1) -  n.getAttributes().get("inside").split(",").length);
        		}
        		else
        		{
        		//	boolean nodeInvolved = false;
        			for(String s : this.oNodeValues)
        			{
        				HashSet<String> valSet = new HashSet<String>(Arrays.asList(s.split(",")));
        				if(valSet.contains(n.getName()))
        				{
        					
                			o = nodes.size()-1;
                    		this.oNodeSpace[o] = n.getName();
                    		n.setoCoordinate(o);
        				}
        			}
        			//check if the current has other nodes which refer to it
        				//if yes, it can be modeled in an inside array. if no, it should be excluded from inside array
        		}
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

	private void extrapolateRelativeValues() {
    	for (SemanticNetNode n1 : this.nodes.values())
	    {
        	for (SemanticNetNode n2 : this.nodes.values())
    	    {
        		if(!n1.getName().equals(n2.getName()))
        		{
            		//if((n1.getRelativeAttributes().containsKey("left-of") && n2.getRelativeAttributes().containsKey("left-of")) ||
            		//		(!(n1.getRelativeAttributes().containsKey("left-of") && n2.getRelativeAttributes().containsKey("left-of"))))
            		//{
            			//if(!(n1.getRelativeAttributes().containsKey("left-of")))
            			//{
            				
            			//}
                		if(n1.getRelativeValue("left-of").equals(n2.getRelativeValue("left-of")))
                		{
                			n1.addRelativeAttribute("y-aligned", n2.getName());
                			//n2.addRelativeAttribute("x-aligned", n1.getName());
                		}
            		//}
            		//if(n1.getRelativeAttributes().containsKey("above") && n2.getRelativeAttributes().containsKey("above"))
            		//{
                		if(n1.getRelativeValue("above").equals(n2.getRelativeValue("above")))
                		{
                			n1.addRelativeAttribute("x-aligned", n2.getName());
                			//n2.addRelativeAttribute("y-aligned", n1.getName());
                		}
            		//}
        		}
    	    }
	    }
		
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

	public int calculateAnalogicalSimilarityForState_Naive(SemanticNetState s2) {
		//HashMap<SemanticNetRelationship, SemanticNetRelationship> snrPairs = new HashMap<SemanticNetRelationship, SemanticNetRelationship>();
		
		int stateAnalogicalSimilarity = 0;
		//String stateAnalogicalFitnessKey = this.getName()+"_"+s2.getName();
		//HashMap<String, Integer> stateAnalogicalSimilarity = new HashMap<String, Integer>();
		//stateAnalogicalSimilarity.put(this.getName()+"_"+s2.getName(), 0);
		HashMap<String, Integer> snrPairsSimilarity = new HashMap<String, Integer>();

		//HashMap<SemanticNetState, SemanticNetState> statePairs = new HashMap<SemanticNetState, SemanticNetState>();
		//HashMap<HashMap<SemanticNetState, SemanticNetState>, Integer> statePairsAnalogFitnessMap = new HashMap<HashMap<SemanticNetState, SemanticNetState>, Integer>();
	
		for(SemanticNetRelationship s1SNR : this.getDestinationRelationships().values())
		{
			for(SemanticNetRelationship s2SNR : s2.getDestinationRelationships().values())
			{
				if(s1SNR.getAttribute().equals(s2SNR.getAttribute()))
				{
					int similarity = s1SNR.calculateSNRSimilarity(s2SNR);
					snrPairsSimilarity.put(s1SNR.getSourceNodeName()+"_"+s2SNR.getSourceNodeName()+"_"+s1SNR.getAttribute(), similarity);
				}
			}
			String maxSNRSimilarityKey = Utilities.getKeyForMax(snrPairsSimilarity);
			int maxSNRSimilarityValue = snrPairsSimilarity.get(maxSNRSimilarityKey);
			//int curAnalogicalSimilarity = stateAnalogicalSimilarity.get(stateAnalogicalFitnessKey);
			//stateAnalogicalSimilarity.put(stateAnalogicalFitnessKey, curAnalogicalSimilarity += maxSNRSimilarityValue);
			stateAnalogicalSimilarity += maxSNRSimilarityValue;
			
		}

		return stateAnalogicalSimilarity;
	}

	/*public int convertRelationshipsToSet(SemanticNetState s) {
		s.getDestinationRelationships()
		return 0;
	}*/
	public String getName() {
		return name;
	}
	public SemanticNetNode getNodeByName(String nodeName) {
		return this.getNodes().get(nodeName);
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public HashMap<String, SemanticNetRelationship> getRelativeRelationships() {
		return relativeRelationships;
	}

	public void setRelativeRelationships(HashMap<String, SemanticNetRelationship> relativeRelationships) {
		this.relativeRelationships = relativeRelationships;
	}

	public HashMap<SemanticNetNode, SemanticNetNode> getSnnMappings() {
		return snnMappings;
	}

	public void setSnnMappings(HashMap<SemanticNetNode, SemanticNetNode> snnMappings) {
		this.snnMappings = snnMappings;
	}

	public SemanticNetNode getNodeByPositionSignature(String destinationPositionSignature) {
			for(SemanticNetNode snn : this.nodes.values())
			{
				if(snn.getRelativePositionSignature().equals(destinationPositionSignature))
				{
					return snn;
				}
			}
		return new SemanticNetNode("null");
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

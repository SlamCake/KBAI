package kbai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;

public class RelationshipCBR {

	private HashMap<String, String[]> attrParameters = new HashMap<String, String[]>();

    public RelationshipCBR() 
    {
       // attrParameters.put("angle",new String[] {"type","logic"});
    //	attrParameters.put("chemisty",new String[] {"ions","electrons"});
    //	attrParameters.put("biology",new String[] {"life","bacteria"});
       // for(String s:subjects.get("biology")){
         //   System.out.println(s);
    	
    	//CW, CCW, XAx, YAx, XAx+YAx
    	// |x-y|+360, |x-y|, 360 - x, 180 - x, 180 + x
    	// Learning through proofs and atomic mathematical information?
    	
    	// Sameness needs to factor. Isolate diff and factor in similarities. "NO CHANGE" should be a transformation...
    	
    	//problem B - 03, what is an angle here? Please check this problem in piazia
    	
    	//break down alignment into components (btm-left)
    	
    	//problem B - 05 ... transform tri to squ vs delete tri & spawn squ???
    	
    	//fill vs non fill (pattern?)
    	
    	//problem B - 08 ... break down fill into components (right-half), transform or delete & spawn?
    	
    	
    	// THIS ONE IS TRICKY
    	//problem B - 10 ... not all attributes line up and shape is different? consider transform vs delete & spawn?
    	// We need a way to associate above: x with the attributes of node x so that the transformation "same" resolves
    	// If unclear on how to proceed after identifying transformation...consider KEY ATTRIBUTES and matching nodes
    	// between figures A and C or vertically / diagonally....
    	
    	//review 10 - 12...
    	
    	//zero change from figure to figure (special case) ... choose zero change from C
    	
    	//a large circle with no fill 'a' does not change
    	//a small plus with fill 'b' inside 'a' does not change
    	//zero change special case
    	
    	//General To Specific
    	//3 only makes sense if rotation is CCW... How do we determine CW or CCW? How we we classify rotation? Sample pixels?
    	//Assume an increas in angle is CW and decrease is CCW?
    	//Node is rotated x_1 -/+ y = x_2 degrees
    	//two candidates fit
    	//Node is large ... 2 left ... node is triangle ... 2 left ... node is fill ... 1 left
    	
    	//angle only difference, all others same (speical case)
    	//angle difference classified as a possible reflection OR rotation, how to choose one over the other?
    	//2 left ... choose reflection? How do we learn reflection is better than rotation?
    	
    	//choose align horozontal or shape transform / D&S as starting attribute
    	//2 or 3 left ... choose other aspect ... 1 left
    	
    	//when calculating the similarity differences of an above (x) attribute, we need to consider similarity of x to x_2
    	
    	//B 06 only transformation difference is fill...
    	//Choose answers that qualify transformations (A fill is applied [one or more])
    		//if all nodes same && no new nodes, apply general-similarity.
    	//Choose answers that quantify transformations (x fills are applied)
    	//Choose answers that quantify subjects of transformations (A fill is applied to 1 node)
    	//Choose answers that describe subject (A fill is applied to 1 node with attributes x,y,z
    	//Choose answers that describe distinctions of subject from other subjects with same attrs (A fill is applied to 1 node 
    		//with attributes x,y,z where x is unique to node and q is absent from node.
    		//What makes this node identifiable? unique attr, absense of attr? etc...
    	//Choose answers that qualify similarities (absence of relationship assumess no change in attribute)
    		//Apply node-level difference/similarit metrics here, determine cost for presence/absence/rotation/rflection/morph/fill/etc

    	//Choose answers that qualify anti-transformations (and all else is the same [consider how to same elements introduced in C])
    	// 2+ left? ... choose where fill has same # attributes as prior ... or choose a non fill candidate ... or use 'above' attribute
    	
    	//C 01 demands a meta reasoning to identify 'same' transformation or 'no' transformation pattern.
    		//general anti-transformation of entire figure vs per node anti-transformation
    	
    	
            //int d = Math.min(Math.abs(1 - 4) % 360, Math.abs(1 + 4) % 360);
    }
	
	 static Map<String, SemanticNetRelationship> reasonRelationshipsWithinStates(SemanticNetNode nSource, SemanticNetNode nDest, String attr) 
	 {
		HashMap<String, SemanticNetRelationship> intraStateRelationships = new HashMap<String, SemanticNetRelationship>();
		
		
		
		//Loop through combinations of pairs of nodes within the state
   	while (ci.hasNext())
	    {
   		ArrayList<SemanticNetNode> pair = (ArrayList<SemanticNetNode>) ci.next();
   		
   		//loop through each node in the pair
       	for (int i = 0; i < pair.size(); i++)
   		{

	    		//loop through the nodes in the pair and process all but the ith node
	    		for (int j = 0; i < pair.size(); j++)
	    		{
	    			if(j!=i)
	    			{
	    				intraStateRelationships.putAll(compareNodes(pair.get(i), pair.get(j)));
	    			}
	    		}
       		// Were getting each unique pair so we want to get both directions of relationships between nodes...
   			Iterator<String> ai = pair.get(i).getAttributes().keySet().iterator();
   			
   			//loop through each attribute of the ith node
   	    	while (ai.hasNext())
   		    {
   	    		String attrName = ai.next();
   	    		
   	    		
   		    }
   		}
	    }
		return intraStateRelationships;
	}
	
}

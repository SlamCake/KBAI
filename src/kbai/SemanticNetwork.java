package kbai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.RavensProblem;

public class SemanticNetwork {


    private String name;
    //Upgrade this to accomadate 2x2 and 3x3...
    //private HashMap<Integer, HashMap<String, HashMap<String,SemanticNetRelationship>>> transformationsByRow = new HashMap<Integer, HashMap<String, HashMap<String,SemanticNetRelationship>>>();
    private HashMap<String, HashMap<String,SemanticNetRelationship>> transformationsByState = new HashMap<String, HashMap<String,SemanticNetRelationship>>();
    private HashMap<String,SemanticNetRelationship> transformationsQA = new HashMap<String,SemanticNetRelationship>();
    //private HashMap<String, SemanticNetRelationship> transformationsQQ = new HashMap<String, SemanticNetRelationship>();
    //private HashMap<String, SemanticNetRelationship> transformationsQA = new HashMap<String, SemanticNetRelationship>();
    private ArrayList<SemanticNetState> states = new ArrayList<SemanticNetState>();
    private ArrayList<SemanticNetState> answerStates = new ArrayList<SemanticNetState>();
    private SemanticNetState finalQuestionState;
    private ArrayList<SemanticNetState> questionStates = new ArrayList<SemanticNetState>();
    public SemanticNetwork(RavensProblem problem) {
    
    	/*RavensFigure rfTemp;
    	RavensObject roTemp;
    	RavensFigure attrMapTemp;
    	SemanticNetNode snnTemp;
    	SemanticNetRelationship snrTemp;*/

    	name = problem.getName();
        for (RavensFigure rf: problem.getFigures().values())
        {
        	states.add(new SemanticNetState(rf));
        }
        int rows = Character.getNumericValue(problem.getProblemType().charAt(0));
        int columns = Character.getNumericValue(problem.getProblemType().charAt(2));
        
        answerStates = (ArrayList<SemanticNetState>) states.subList((columns*rows)-1, states.size());
        		
        finalQuestionState = states.get((columns*rows)-1);
        //if(problem.getProblemType().equals("2x2"))
        //{
        	//add logic to iterate through answers and identify transformations where possible...prune generic -> specific
        for(int i = 0; i < (rows-1) ; i++)
        {
        	if(i!=(rows-1))
        	{
            	transformationsByState.putAll(RelationshipReasoner.reasonTransformationRelationships(states.subList(columns*i, columns*(i+1))));
            	//transformationsByRow.put(i, transformationsByState);
        	}
        	else
        	{
        		if(columns > 2)
        		{
                	transformationsByState.putAll(RelationshipReasoner.reasonTransformationRelationships(states.subList(columns*i, ((columns*(i+1))-1))));
                	//transformationsByRow.put(i, transformationsByState);
        		}
        	}
        	//Verify this gathers the rest of the nodes appropriately.
        	//for(SemanticNetState sns : states.subList(3, states.size()))
        	//{
        		
        	//}  	
        }
        
        for(SemanticNetState as : answerStates)
        {
        	transformationsQA.putAll(RelationshipReasoner.reasonTransformationRelationships(finalQuestionState, as));
        }
        //}
        
        /*
        for (Map.Entry<String, RavensFigure> rfEntry : problem.getFigures().entrySet())
        {
            for (Map.Entry<String, RavensObject> roEntry : rfEntry.getValue().getObjects().entrySet())
            {
            	nodes.put(rfEntry.getKey()+roEntry.getKey(), new SemanticNetNode(roEntry.getValue()));
            }
        }
         for (RavensObject ro : rf.getObjects().values())
            {
            	nodes.put(rf.getName()+ro.getName(), new SemanticNetNode(ro));
            }
        */
        
      /*  for (Iterator<Map.Entry<String, RavensFigure>> rf = problem.getFigures().entrySet().iterator(); rf.hasNext(); )
        {
        	rfTemp = rf.next();
            for (Iterator<Map.Entry<String, RavensObject>> ro = rf.next().getValue().getObjects().entrySet().iterator(); ro.hasNext(); )
            {
                for (Iterator<Map.Entry<String, String>> attrMap = ro.next().getValue().getAttributes().entrySet().iterator(); attrMap.hasNext(); )
                {

                	ro.next().
                }
            }
        }*/
            	
            	
            	
            	
    }
    /*
    public addNode() {
        //this.nodes.add();
    }
    public addRelationship() {
        //this.nodes.add();
    }
    public removeNode() {
        //this.nodes.add();
    }
    public removeRelationship() {
        //this.nodes.add();
    }
    public inferIntraNetworkRelationships() {
        //this.nodes.add();
    }
    public inferTransformations() {
        //this.nodes.add();
    }
    */
}

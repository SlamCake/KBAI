package kbai;

import java.util.HashMap;
import java.util.Map;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;
import ravensproject.RavensProblem;

public class SemanticNetState {
	private String name;
	//initialize these to avoid ClassNotLoaded exception...
    private HashMap<String, SemanticNetNode> nodes = new HashMap<String, SemanticNetNode>();
    private HashMap<String, SemanticNetRelationship> stateRelationships = new HashMap<String, SemanticNetRelationship>();

    public SemanticNetState(RavensFigure rf) 
    {
    	for (RavensObject ro : rf.getObjects().values())
	    {
    		//Verify that getName() uniqueness works as expected.
    		SemanticNetNode snn = new SemanticNetNode(ro);
	    	this.nodes.put(ro.getName(), snn);
	    }
    	setName(rf.getName());
    	
    	
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

	public HashMap<String, SemanticNetRelationship> getStateRelationships() {
		return stateRelationships;
	}

	public void setStateRelationships(HashMap<String, SemanticNetRelationship> stateRelationships) {
		this.stateRelationships = stateRelationships;
	}

	/*for (Map.Entry<String, String> attributeEntry : o.getAttributes().entrySet())
    {
    	
    }*/
}

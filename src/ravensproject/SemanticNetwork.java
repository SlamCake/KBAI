package ravensproject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SemanticNetwork {


	private HashMap<SemanticNetNode, NodeSimilarityRecord> NSRMap = new HashMap<SemanticNetNode, NodeSimilarityRecord>();
    private String name;
    private String RPMType;
    private int rows;
    private int columns;
    //Upgrade this to accomadate 2x2 and 3x3...
    //private HashMap<Integer, HashMap<String, HashMap<String,SemanticNetRelationship>>> transformationsByRow = new HashMap<Integer, HashMap<String, HashMap<String,SemanticNetRelationship>>>();
    private HashMap<String, HashMap<String,SemanticNetRelationship>> transformationsByState = new HashMap<String, HashMap<String,SemanticNetRelationship>>();
    public String getName() {
		return name;
	}

	public List<SemanticNetState> getQuestionStatesByRow(int row) {
		return this.questionStates.subList((row-1)*(this.columns), ((row-1)*(this.columns))+(this.columns));
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SemanticNetState> getStates() {
		return states;
	}
	
	public SemanticNetState getStateByName(String stateName) {
		return stateMap.get(stateName);
	}
	public SemanticNetState getStateBySequence(int sequence) {
		return stateMap.get(this.getStateByName(KnowledgeBase.sequence2NameMap.get(sequence)));
	}
	public SemanticNetState getStateByPosition(int row, int column) {
		return stateMap.get(this.getStateByName(KnowledgeBase.sequence2NameMap.get(row*column+column)));
	}
	
	public SemanticNetNode getNodeByName(String stateName, String nodeName) {
		
		return this.getStateByName(stateName).getNodes().get(nodeName);
	}

	public void setStates(ArrayList<SemanticNetState> states) {
		this.states = states;
	}

	public ArrayList<SemanticNetState> getAnswerStates() {
		return answerStates;
	}

	public void setAnswerStates(ArrayList<SemanticNetState> answerStates) {
		this.answerStates = answerStates;
	}

	public ArrayList<SemanticNetState> getQuestionStates() {
		return questionStates;
	}

	public void setQuestionStates(ArrayList<SemanticNetState> questionStates) {
		this.questionStates = questionStates;
	}

	public HashMap<String, HashMap<String, SemanticNetRelationship>> getTransformationsByState() {
		return transformationsByState;
	}
	
	public HashMap<String, SemanticNetRelationship> getTransformationsBySequence(int s) {
		return transformationsByState.get(KnowledgeBase.sequence2NameMap.get(s));
	}

	public void setTransformationsByState(
			HashMap<String, HashMap<String, SemanticNetRelationship>> transformationsByState) {
		this.transformationsByState = transformationsByState;
	}

	public HashMap<String, HashMap<String, SemanticNetRelationship>> getTransformationsQA() {
		return transformationsQA;
	}

	public void setTransformationsQA(
			HashMap<String, HashMap<String, SemanticNetRelationship>> transformationsQA) {
		this.transformationsQA = transformationsQA;
	}

	private HashMap<String, HashMap<String,SemanticNetRelationship>> transformationsQA = new HashMap<String, HashMap<String,SemanticNetRelationship>>();
    //private HashMap<String, SemanticNetRelationship> transformationsQQ = new HashMap<String, SemanticNetRelationship>();
    //private HashMap<String, SemanticNetRelationship> transformationsQA = new HashMap<String, SemanticNetRelationship>();
    private ArrayList<SemanticNetState> states = new ArrayList<SemanticNetState>();
    private HashMap<String, SemanticNetState> stateMap = new HashMap<String, SemanticNetState>();
    private ArrayList<SemanticNetState> answerStates = new ArrayList<SemanticNetState>();
    private ArrayList<SemanticNetState> questionStates = new ArrayList<SemanticNetState>();
    
    public SemanticNetwork(RavensProblem problem) {
    
    	/*RavensFigure rfTemp;
    	RavensObject roTemp;
    	RavensFigure attrMapTemp;
    	SemanticNetNode snnTemp;
    	SemanticNetRelationship snrTemp;*/
    	this.RPMType = problem.getProblemType();
    	this.name = problem.getName();
    	this.setRows(Character.getNumericValue(problem.getProblemType().charAt(0)));
    	this.setColumns(Character.getNumericValue(problem.getProblemType().charAt(2)));
        for (RavensFigure rf: problem.getFigures().values())
        {
        	SemanticNetState tempState = new SemanticNetState(rf);
        	states.add(tempState);
        	stateMap.put(tempState.getName(), tempState);
        }
        //int rows = Character.getNumericValue(problem.getProblemType().charAt(0));
        //int columns = Character.getNumericValue(problem.getProblemType().charAt(2));
        
        for(SemanticNetState s : states)
        {
        	if(Utilities.isNumeric(s.getName()))
        	{
                answerStates.add(s);
        	}
        	else
        	{
        		questionStates.add(s);
        	}
        }
        Collections.sort(answerStates, new StateComparator());
        Collections.sort(questionStates, new StateComparator());
        
        RelationshipReasoner rr = new RelationshipReasoner(this);
        		
        //finalQuestionState = states.get((columns*rows)-1);
        //if(problem.getProblemType().equals("2x2"))
        //{
        	//add logic to iterate through answers and identify transformations where possible...prune generic -> specific
        for(SemanticNetState s : questionStates)
        {
        	int index = questionStates.indexOf(s);
        	if((index+1) % (rows) != 0 && (index+1) != questionStates.size())
        	{
            	transformationsByState.put(s.getName(), rr.reasonTransformationRelationships(s, questionStates.get(index+1)));
        	}
        }
        /*for(int i = 0; i < (rows) ; i++)
        {
            	if(i!=(rows))
            	{
                	for(int j = 0; j < (columns-1); j++)
                	{
                	transformationsByState.putAll(questionStates.get(i)rr.reasonTransformationRelationships(questionStates.get((j*i)), questionStates.get((j*i)+1)));
                	//transformationsByRow.put(i, transformationsByState);
                	}
            	}
            	else
            	{
                	for(int j = 0; j < (columns-2); j++)
                	{
                	transformationsByState.putAll(rr.reasonTransformationRelationships(questionStates.get((j*i)), questionStates.get((j*i)+1)));
                	//transformationsByRow.put(i, transformationsByState);
                	}
            		/*if(columns > 2)
            		{
                    	transformationsByState.putAll(RelationshipReasoner.reasonTransformationRelationships(questionStates.subList(columns*i, ((columns*(i+1))-1))));
                    	//transformationsByRow.put(i, transformationsByState);
            		}
            	}
        	
        	//Verify this gathers the rest of the nodes appropriately.
        	//for(SemanticNetState sns : states.subList(3, states.size()))
        	//{
        		
        	//}  	
        }*/
        
        //Get Transformations between final question figure and all answer candidates...
        for(SemanticNetState as : answerStates)
        {
        	HashMap<String, SemanticNetRelationship> tqa = rr.reasonTransformationRelationships(questionStates.get(questionStates.size()-1), as);
        	transformationsQA.put(as.getName(), tqa);
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

	public String getRPMType() {
		return RPMType;
	}

	public ArrayList<String> getTransformationRelationshipSpecs(int row, int column) {
		ArrayList<String> specs = new ArrayList<String>();
		for(SemanticNetRelationship snr : this.getTransformationRelationships(row, column))
		{
			specs.add(snr.getTransformationSpecification());
		}
		return specs;
	}
	
	public ArrayList<String[]> getTransformationRelationshipSpecs(int rowStart, int rowEnd, int columnStart, int columnEnd) {
		ArrayList<String[]> tempTRSpecs = new ArrayList<String[]>();
		for(Collection<SemanticNetRelationship> snrCol : this.getTransformationRelationships(rowStart, rowEnd, columnStart, columnEnd))
		{
			String[] specs = new String[snrCol.size()];
			int index = 0;
			for(SemanticNetRelationship snr : snrCol)
			{
				specs[index] = snr.getTransformationSpecification();
				index++;
			}
			tempTRSpecs.add(specs);
		}
		return tempTRSpecs;
	}
	
	public ArrayList<Collection<SemanticNetRelationship>> getTransformationRelationships(int rowStart, int rowEnd, int columnStart, int columnEnd) {
		ArrayList<Collection<SemanticNetRelationship>> tempRelationships = new ArrayList<Collection<SemanticNetRelationship>>();
		for(int i = rowStart; i< rowEnd; i++)
		{
			for(int j = columnStart; j < columnEnd; j++)
			{
				tempRelationships.add(this.getTransformationRelationships(i, j));	
			}
		}
		return tempRelationships;
	}
	public Collection<SemanticNetRelationship> getTransformationRelationships(int row, int column) {
		return this.getQuestionState(row, column).getDestinationRelationships().values();
	}
	public SemanticNetState getQuestionState(int row, int column) {
		return this.getQuestionStates().get(((row-1)*this.getColumns()+column));
	}

	public void setRPMType(String rPMType) {
		RPMType = rPMType;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public HashMap<SemanticNetNode, NodeSimilarityRecord> getNSRMap() {
		return NSRMap;
	}

	public void setNSRMap(HashMap<SemanticNetNode, NodeSimilarityRecord> nSRMap) {
		NSRMap = nSRMap;
	}

}

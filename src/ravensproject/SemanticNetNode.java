
package ravensproject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

public class SemanticNetNode {
	
	private String name;
	private String stateName;
	private String type;
	private String mapsTo;
	private String mapsFrom;
	private String analogousTo;
	private String relativePosSignature;
	private int xCoordinate;
	private int yCoordinate;
	private int iCoordinate;
	private int oCoordinate;
	
    private HashMap<String, String> attributes = new HashMap<String, String>();
    private HashMap<String, String> relativeAttributes = new HashMap<String, String>();
    private HashMap<String, String> nonRelativeAttributes = new HashMap<String, String>();
    
    private HashMap<String, SemanticNetRelationship> relativeRelationships = new HashMap<String, SemanticNetRelationship>();
    private HashMap<String, SemanticNetRelationship> sourceRelationships = new HashMap<String, SemanticNetRelationship>();
    private HashMap<String, SemanticNetRelationship> destinationRelationships = new HashMap<String, SemanticNetRelationship>();

    public SemanticNetNode(RavensObject ro) 
    {
    	setName(ro.getName());
    	//setAttributes(ro.getAttributes());
    	setAttributes(ro.getAttributes());
    	this.setType("normal");
    	for(Entry<String, String> attrEntry : ro.getAttributes().entrySet())
    	{
    		if(KnowledgeBase.attributeMetaDataMap.get(attrEntry.getKey()).isRelative()) 
    		{ 
    			this.relativeAttributes.put(attrEntry.getKey(), attrEntry.getValue());
    		}
    		else
    		{
    			this.nonRelativeAttributes.put(attrEntry.getKey(), attrEntry.getValue());
    		}
    	}
    	generateRelativePositionSignature();
    	//populateNullRelativeValues();
    	//this.setRelativeAttributes(ro.getAttributes().entrySet().);
    	
    	/*if(KBAILogging.stats == true)
    	{
    		KBAILogging.mergeNodeAttributesIntoDAV(ro.getAttributes());
    		KBAILogging.logDistinctAttributeValues();
    	}*/
    	
    	
    }

	/*private void populateNullRelativeValues() {
		if(!this.getAttributes().containsKey("left-of"))
		{
			
		}
		if(!this.getAttributes().containsKey("above"))
		{
			
		}
		if(!this.getAttributes().containsKey("left-of"))
		{
			
		}
		if(!this.getAttributes().containsKey("left-of"))
		{
			
		}
		
	}*/

	public String getRelativeValue(String attr) {
		String result = "";
		if(this.getRelativeAttributes().containsKey(attr))
		{
			result = this.getRelativeAttributes().get(attr);
		}
		return result;
	}
	public String getRelativePositionSignature() {
		return this.relativePosSignature;
	}
	private void generateRelativePositionSignature() {
		String result = "";
		//for(String attr : KnowledgeBase.relativeAttributes)
		//{
			if(this.getRelativeAttributes().containsKey("left-of"))
			{
				result += String.valueOf(this.getRelativeAttributes().get("left-of").split(",").length)+"_";
			}
			else
			{
				result+="0"+"_";
			}

			if(this.getRelativeAttributes().containsKey("above"))
			{
				result += String.valueOf(this.getRelativeAttributes().get("above").split(",").length)+"_";
			}
			else
			{
				result+="0"+"_";
			}
			
			if(this.getRelativeAttributes().containsKey("overlaps"))
			{
				result += String.valueOf(this.getRelativeAttributes().get("overlaps").split(",").length)+"_";
			}
			else
			{
				result+="0"+"_";
			}
			if(this.getRelativeAttributes().containsKey("inside"))
			{
				result += String.valueOf(this.getRelativeAttributes().get("inside").split(",").length)+"_";
			}
			else
			{
				result+="0"+"_";
			}
			if(this.getRelativeAttributes().containsKey("x-aligned"))
			{
				result += String.valueOf(this.getRelativeAttributes().get("x-aligned").split(",").length)+"_";
			}
			else
			{
				result+="0"+"_";
			}
			if(this.getRelativeAttributes().containsKey("y-aligned"))
			{
				result += String.valueOf(this.getRelativeAttributes().get("y-aligned").split(",").length)+"_";
			}
			else
			{
				result+="0"+"_";
			}
		//}
		this.relativePosSignature = result.substring(0, result.length()-1);
	}
	public int calculateRelativePositionSimilarity(String RPSig) {
		// TODO system of allowances given prior displacements, creates, and deletes...
		int similarity = 0;
		
		ArrayList<String> CurRPSigAL = new ArrayList<String>(Arrays.asList(this.relativePosSignature.split("_")));
		//HashSet<String> NextRPSigAL = new HashSet<String>(Arrays.asList(RPSig.split("_")));
		ArrayList<String> NextRPSigAL = new ArrayList<String>(Arrays.asList(RPSig.split("_")));
		for(int i = 0; i < CurRPSigAL.size(); i++)
		{
			if(CurRPSigAL.get(i).equals(NextRPSigAL.get(i)))
			{
				//consider not accounting for zeroes?
				similarity += 1;
				similarity += Integer.valueOf(CurRPSigAL.get(i));
			}
			else
			{
				similarity += Math.min(Integer.valueOf(NextRPSigAL.get(i)), Integer.valueOf(CurRPSigAL.get(i)));
				similarity -= Math.abs(Integer.valueOf(NextRPSigAL.get(i)) - Integer.valueOf(CurRPSigAL.get(i)));
			}
		}
		/*for(int i = 0; i < RPSig.length(); i++)
		{
			if(RPSig.charAt(i) == this.relativePosSignature.charAt(i))
			{
				similarity += 1;
			}
			else
			{
				similarity -= 1;
			}
		}*/
		return similarity;
	}
	public int calculateRelativePositionSimilarity(String RPSig1, String RPSig2) {
		// TODO system of allowances given prior displacements, creates, and deletes...
		int similarity = 0;
		for(int i = 0; i < RPSig1.length(); i++)
		{
			if(RPSig1.charAt(i) == RPSig2.charAt(i))
			{
				similarity += 1;
			}
			else
			{
				similarity -= 1;
			}
		}
		return similarity;
	}
	public String calculateRelativePositionDiference(String RPSig) {
		// TODO system of allowances given prior displacements, creates, and deletes...
		String difference = "";
		for(int i = 0; i < RPSig.length(); i++)
		{
			difference += String.valueOf(Character.getNumericValue(RPSig.charAt(i)) 
							-Character.getNumericValue(this.relativePosSignature.charAt(i)));
		}
		return difference;
	}
	
	public String calculateRelativePositionDiference(String RPSig1, String RPSig2) {
		// TODO system of allowances given prior displacements, creates, and deletes...
		String difference = "";
		for(int i = 0; i < RPSig1.length(); i++)
		{
			difference += String.valueOf(Character.getNumericValue(RPSig2.charAt(i)) 
							-Character.getNumericValue(RPSig1.charAt(i)));
		}
		return difference;
	}

	public SemanticNetNode(String type) {
		this.setType(type);
		this.setName("null");
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return this.getName();
	}

	public boolean calculateAttributeMatch(SemanticNetNode n) {
		boolean match = true;
	    
	    Set<String> nNonRelValues = new HashSet<String>(n.getNonRelativeAttributes().values());
	    Set<String> nNonRelAttributes = new HashSet<String>(n.getNonRelativeAttributes().keySet());
	    
	    Set<String> similarities;
	    Set<String> differences;
	   // Set<String> similarRelAttributes = new HashSet<String>(this.getRelativeAttributes().keySet());
	    
	   // Set<String> similaronRelValues = new HashSet<String>(this.getNonRelativeAttributes().values());
	   // Set<String> nonRelAttributes = new HashSet<String>(this.getNonRelativeAttributes().keySet());
	    
	    //Set<String> differences = new HashSet<String>();
	    //Set<String> similarities = new HashSet<String>();

	    //compare non relative attributes
	    similarities = new HashSet<String>(this.getNonRelativeAttributes().keySet());
	    differences = new HashSet<String>(this.getNonRelativeAttributes().keySet());
	    similarities.retainAll(nNonRelAttributes);
	    differences.removeAll(nNonRelAttributes);
	    
	    if(!differences.isEmpty())
	    {
	    	match = false;
	    }
	    
	    //compare non relative values
	    similarities = new HashSet<String>(this.getNonRelativeAttributes().values());
	    differences = new HashSet<String>(this.getNonRelativeAttributes().values());
	    similarities.retainAll(nNonRelValues);
	    differences.removeAll(nNonRelValues);
	    
	    if(!differences.isEmpty())
	    {
	    	match = false;
	    }
		
		return match;
	}
	public int calculateNonRelativeSimilarity(SemanticNetNode n) {
		int similarity = 0;
	    
	    Set<String> nNonRelValues = new HashSet<String>(n.getNonRelativeAttributes().values());
	    Set<String> nNonRelAttributes = new HashSet<String>(n.getNonRelativeAttributes().keySet());
	    
	    Set<String> similarities;
	    Set<String> differences;
	   // Set<String> similarRelAttributes = new HashSet<String>(this.getRelativeAttributes().keySet());
	    
	   // Set<String> similaronRelValues = new HashSet<String>(this.getNonRelativeAttributes().values());
	   // Set<String> nonRelAttributes = new HashSet<String>(this.getNonRelativeAttributes().keySet());
	    
	    //Set<String> differences = new HashSet<String>();
	    //Set<String> similarities = new HashSet<String>();

	    //compare non relative attributes
	    similarities = new HashSet<String>(this.getNonRelativeAttributes().keySet());
	    differences = new HashSet<String>(this.getNonRelativeAttributes().keySet());
	    
	    similarities = Utilities.intersection(similarities, nNonRelAttributes);
	    differences = Utilities.symDifference(differences, nNonRelAttributes);
	    similarity += similarities.size() - differences.size();
	    
	    //compare non relative values
	    similarities = new HashSet<String>(this.getNonRelativeAttributes().values());
	    differences = new HashSet<String>(this.getNonRelativeAttributes().values());
	    //similarities.retainAll(nNonRelValues);
	    //differences.removeAll(nNonRelValues);
	    similarities = Utilities.intersection(similarities, nNonRelValues);
	    differences = Utilities.difference(differences, nNonRelValues);
	    similarity += similarities.size() - differences.size();
	    
	    //extra point for perfect matches, prioritize node attributes over position...
	   /* if(differences.size() == 0)
	    {
	    	similarity++;
	    }*/
		
		return similarity;
	}
	public int calculateRelativeSimilarity(SemanticNetNode n) {
		int similarity = 0;
		
	    Set<String> nRelValues = new HashSet<String>(n.getRelativeAttributes().values());
	    Set<String> nRelAttributes = new HashSet<String>(n.getRelativeAttributes().keySet());
	    
	    Set<String> similarities;
	    Set<String> differences;
	    Set<String> relValues1;
	    Set<String> relValues2;
	    
	    //compare relative attributes
	    similarities = new HashSet<String>(this.getRelativeAttributes().keySet());
	    differences = new HashSet<String>(this.getRelativeAttributes().keySet());
	    //similarities.retainAll(nRelAttributes);
	    //differences.removeAll(nRelAttributes);
	    similarities = Utilities.intersection(similarities, nRelAttributes);
	    differences = Utilities.symDifference(differences, nRelAttributes);
	    similarity += similarities.size() - differences.size();

	    //compare relative values
	    //this will need to be handled separately
	    /*
	    similarities = new HashSet<String>(this.getRelativeAttributes().values());
	    differences = new HashSet<String>(this.getRelativeAttributes().values());
	    similarities.retainAll(nRelValues);
	    differences.removeAll(nRelValues);
	    similarity += similarities.size() - differences.size();
	    */
	    
	    
	    //left 2 right, top 2 bottom....
	    //build coordinate 2d matrix based on above/left of attrs...
	    //test if prior position in 2d map is the same as posterior position...
	    //creation v deletion v exchange v displacement...all should impact the 2d matrix differently...
	    
	    //must know if a displacement/exchange/creation/deletion has occoured PRIOR to evaluating
	    //node fitness for relative values.
	    for(Entry<String, String> e : n.getRelativeAttributes().entrySet())
	    {
    		String[] n2RelVal = e.getValue().split(",");
	    	if(this.getRelativeAttributes().containsKey(e.getKey()))
	    	{
	    		//Search through NSRMap to quickly identify these without considering other relative attrs...
	    		String[] n1RelVal = this.getRelativeAttributes().get(e.getKey()).split(",");
	    		similarity += n1RelVal.length - Math.abs((n2RelVal.length - n1RelVal.length));
	    	}
	    	else
	    	{
	    		similarity -= n2RelVal.length;
	    	}
	    }
		
		return similarity;
	}

	public HashMap<String, String> getNonRelativeAttributes() {
		return nonRelativeAttributes;
	}

	public void setNonRelativeAttributes(HashMap<String, String> nonRelativeAttributes) {
		this.nonRelativeAttributes = nonRelativeAttributes;
	}
	
	public HashMap<String, String> getRelativeAttributes() {
		return relativeAttributes;
	}

	public void setRelativeAttributes(HashMap<String, String> relativeAttributes) {
		this.relativeAttributes = relativeAttributes;
	}

	public String getMapsTo() {
		return mapsTo;
	}

	public void setMapsTo(String mapsTo) {
		this.mapsTo = mapsTo;
	}

	public String getMapsFrom() {
		return mapsFrom;
	}

	public void setMapsFrom(String mapsFrom) {
		this.mapsFrom = mapsFrom;
	}

	public String getAnalogousTo() {
		return analogousTo;
	}

	public void setAnalogousTo(String analogousTo) {
		this.analogousTo = analogousTo;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public int getiCoordinate() {
		return iCoordinate;
	}

	public void setiCoordinate(int iCoordinate) {
		this.iCoordinate = iCoordinate;
	}

	public void setoCoordinate(int oCoordinate) {
		this.oCoordinate = oCoordinate;
	}

	public HashMap<String, SemanticNetRelationship> getRelativeRelationships() {
		return relativeRelationships;
	}

	public void setRelativeRelationships(HashMap<String, SemanticNetRelationship> relativeRelationships) {
		this.relativeRelationships = relativeRelationships;
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

	public void setDestinationRelationships(HashMap<String, SemanticNetRelationship> destinationRelationships) {
		this.destinationRelationships = destinationRelationships;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void addRelativeAttribute(String attr, String val) {
		if(this.getRelativeAttributes().containsKey(attr))
		{
			
			if(!this.getRelativeAttributes().get(attr).isEmpty())
			{
				val += this.getRelativeAttributes().get(attr)+","+val;
			}
			//else
			//{
				//this.getRelativeAttributes().put(attr, val);
				//this.getAttributes().put(attr, ","val);
			//}
			this.getRelativeAttributes().put(attr, val);
			this.getAttributes().put(attr, val);
		}
		else
		{
			this.getRelativeAttributes().put(attr, val);
			this.getAttributes().put(attr, val);
		}
		
	}


	/*for (Map.Entry<String, String> attributeEntry : o.getAttributes().entrySet())
    {
    	
    }*/
}

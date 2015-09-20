
package kbai;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import ravensproject.RavensObject;

public class SemanticNetNode {
	
	private String name;
	private String type;
	private String mapsTo;
	private String mapsFrom;
	private String analogousTo;
	private int xCoordinate;
	private int yCoordinate;
	private int iCoordinate;
    private HashMap<String, String> attributes;
    private HashMap<String, String> relativeAttributes;
    private HashMap<String, String> nonRelativeAttributes;

    public SemanticNetNode(RavensObject ro) 
    {
    	setName(ro.getName());
    	setAttributes(ro.getAttributes());
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
    	//this.setRelativeAttributes(ro.getAttributes().entrySet().);
    	
    	/*if(KBAILogging.stats == true)
    	{
    		KBAILogging.mergeNodeAttributesIntoDAV(ro.getAttributes());
    		KBAILogging.logDistinctAttributeValues();
    	}*/
    	
    	
    }

	public SemanticNetNode(String type) {
		this.setType(type);
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
	
	public int calculateSimilarity(SemanticNetNode n) {
		int similarity = 0;
		
	    Set<String> nRelValues = new HashSet<String>(n.getRelativeAttributes().values());
	    Set<String> nRelAttributes = new HashSet<String>(n.getRelativeAttributes().keySet());
	    
	    Set<String> nNonRelValues = new HashSet<String>(n.getNonRelativeAttributes().values());
	    Set<String> nNonRelAttributes = new HashSet<String>(n.getNonRelativeAttributes().keySet());
	    
	    Set<String> similarities;
	    Set<String> differences;
	    Set<String> relValues1;
	    Set<String> relValues2;
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
	    similarity += similarities.size() - differences.size();
	    
	    //compare non relative values
	    similarities = new HashSet<String>(this.getNonRelativeAttributes().values());
	    differences = new HashSet<String>(this.getNonRelativeAttributes().values());
	    similarities.retainAll(nNonRelValues);
	    differences.removeAll(nNonRelValues);
	    similarity += similarities.size() - differences.size();

	    //compare relative attributes
	    similarities = new HashSet<String>(this.getRelativeAttributes().keySet());
	    differences = new HashSet<String>(this.getRelativeAttributes().keySet());
	    similarities.retainAll(nRelAttributes);
	    differences.removeAll(nRelAttributes);
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
	    for(Entry<String, String> e : n.getRelativeAttributes().entrySet())
	    {
	    	if(this.getRelativeAttributes().containsKey(e.getKey()))
	    	{
	    		//Search through NSRMap to quickly identify these without considering other relative attrs...
	    		String[] relVal1 = this.getRelativeAttributes().get(e.getKey()).split(",");
	    		String[] relVal2 = this.getRelativeAttributes().get(e.getKey()).split(",");
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

	/*for (Map.Entry<String, String> attributeEntry : o.getAttributes().entrySet())
    {
    	
    }*/
}

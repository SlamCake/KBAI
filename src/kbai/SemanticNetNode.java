
package kbai;
import java.util.HashMap;

import ravensproject.RavensObject;

public class SemanticNetNode {
	
	private String name;
    private HashMap<String, String> attributes;

    public SemanticNetNode(RavensObject ro) 
    {
    	setName(ro.getName());
    	setAttributes(ro.getAttributes());
    	
    	if(KBAILogging.stats == true)
    	{
    		KBAILogging.mergeNodeAttributesIntoDAV(ro.getAttributes());
    		KBAILogging.logDistinctAttributeValues();
    	}
    	
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

	/*for (Map.Entry<String, String> attributeEntry : o.getAttributes().entrySet())
    {
    	
    }*/
}

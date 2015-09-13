package kbai;

import java.util.ArrayList;

public class SemanticNetRelationship {
	private String sourceNodeName;
	private String destinationNodeName;
	private String sourceNodeLabel;
	private String destinationNodeLabel;
	private Object sourceNodeValue;
	private Object destinationNodeValue;
	private String attribute;
	private String pruningDirectives;
	//private ArrayList<String> postConditions;
	//private ArrayList<String> postConditions;
	
	public SemanticNetRelationship(String node1, String node2, String attr) {
		sourceNodeName = node1;
		destinationNodeName = node2;
	}
	public SemanticNetRelationship() {
		// TODO Auto-generated constructor stub
	}
	public String getSourceNodeName() {
		return sourceNodeName;
	}
	public void setSourceNodeName(String sourceNodeName) {
		this.sourceNodeName = sourceNodeName;
	}
	public String getDestinationNodeName() {
		return destinationNodeName;
	}
	public void setDestinationNodeName(String destinationNodeName) {
		this.destinationNodeName = destinationNodeName;
	}
	/*public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}*/
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Object getSourceNodeValue() {
		return sourceNodeValue;
	}
	public void setSourceNodeValue(Object sourceNodeValue) {
		this.sourceNodeValue = sourceNodeValue;
	}
	public Object getDestinationNodeValue() {
		return destinationNodeValue;
	}
	public void setDestinationNodeValue(Object destinationNodeValue) {
		this.destinationNodeValue = destinationNodeValue;
	}
	public String getSourceNodeLabel() {
		return sourceNodeLabel;
	}
	public void setSourceNodeLabel(String sourceNodeLabel) {
		this.sourceNodeLabel = sourceNodeLabel;
	}
	public String getDestinationNodeLabel() {
		return destinationNodeLabel;
	}
	public void setDestinationNodeLabel(String destinationNodeLabel) {
		this.destinationNodeLabel = destinationNodeLabel;
	}
	public String getPruningDirective() {
		return pruningDirectives;
	}
	public void setPruningDirective(String pruningDirectives) {
		this.pruningDirectives = pruningDirectives;
	}

}

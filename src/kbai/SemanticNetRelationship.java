package kbai;

import java.util.ArrayList;

public class SemanticNetRelationship {
	//private SemanticNetNode sourceNode;
	//private SemanticNetNode destinationNode;
	private String sourceStateName;
	private String destinationStateName;
	private String sourceNodeName;
	private String destinationNodeName;
	private String sourceNodeLabel;
	private String destinationNodeLabel;
	private Object sourceNodeValue;
	private Object destinationNodeValue;
	private String attribute;
	private String transformationSpecification;
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
	public String getTransformationSpecification() {
		return transformationSpecification;
	}
	public void setTransformationSpecification(String transformationSpecification) {
		this.transformationSpecification = transformationSpecification;
	}
	public String getDestinationStateName() {
		return destinationStateName;
	}
	public void setDestinationStateName(String destinationStateName) {
		this.destinationStateName = destinationStateName;
	}
	public String getSourceStateName() {
		return sourceStateName;
	}
	public String toString() {
		String snrString = this.getSourceStateName()+"\t"+this.getSourceNodeName()+"\t"+this.getSourceNodeLabel()+"\t"+this.getSourceNodeValue()+"\t"+this.getDestinationStateName()+"\t"+this.getDestinationNodeLabel()+"\t"+this.getDestinationNodeName()+"\t"+this.getDestinationNodeValue()+"\t"+this.getAttribute()+"\t"+this.getTransformationSpecification();
		return snrString;
	}
	public void setSourceStateName(String sourceStateName) {
		this.sourceStateName = sourceStateName;
	}
	/*public SemanticNetNode getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(SemanticNetNode sourceNode) {
		this.sourceNode = sourceNode;
	}
	public SemanticNetNode getDestinationNode() {
		return destinationNode;
	}
	public void setDestinationNode(SemanticNetNode destinationNode) {
		this.destinationNode = destinationNode;
	}*/

}

package ravensproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class SemanticNetRelationship {
	//private SemanticNetNode sourceNode;
	//private SemanticNetNode destinationNode;
	private HashMap<String,Integer> sourcePositionAttrsEnum = new HashMap<String,Integer>();
	private HashMap<String,Integer> destinationPositionAttrsEnum = new HashMap<String,Integer>();
	private HashMap<String,String> sourcePositionAttrs = new HashMap<String,String>();
	private HashMap<String,String> destinationPositionAttrs = new HashMap<String,String>();
	private String sourcePositionSignature;
	private String snrIndex;
	private String destinationPositionSignature;
	private String sourceStateName;
	private String destinationStateName;
	private String sourceNodeName;
	private String destinationNodeName;
	private String sourceNodeLabel;
	private String destinationNodeLabel;
	private Object sourceNodeValue;
	private Object destinationNodeValue;
	private String attribute;
	private boolean isNull;
	private String nullType;
	private int cost;
	private String transformationSpecification;
	//private ArrayList<String> postConditions;
	//private ArrayList<String> postConditions;
	
	public SemanticNetRelationship(String node1, String node2, String attr) {
		sourceNodeName = node1;
		destinationNodeName = node2;
		this.setNull(false);
	}
	public SemanticNetRelationship(String nullType) {
		this.setNull(true);
		this.setNullType(nullType);
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
	
	public int calculateSNRSimilarity(SemanticNetRelationship snr)
	{
		int transformationSimilarity = 0;
			    
		Set<String> thisTransSpecSet = new HashSet<String>(Arrays.asList(this.getTransformationSpecification()));
		Set<String> snrTransSpecSet = new HashSet<String>(Arrays.asList(snr.getTransformationSpecification()));
		Set<String> similaritiesTransSpecSet = new HashSet<String>(thisTransSpecSet);
		Set<String> differencesTransSpecSet = new HashSet<String>(thisTransSpecSet);
		similaritiesTransSpecSet.retainAll(snrTransSpecSet);
		differencesTransSpecSet.removeAll(snrTransSpecSet);
		transformationSimilarity += similaritiesTransSpecSet.size() - differencesTransSpecSet.size();

		return transformationSimilarity;
	}

	public static HashMap<SemanticNetRelationship, SemanticNetRelationship> getSnrMappingsFromSets(
			HashSet<SemanticNetRelationship> sSnrSet,
			HashSet<SemanticNetRelationship> dSnrSet) 
	{
		//HashSet<SemanticNetRelationship> aSnrSet = new HashSet<SemanticNetRelationship>(answerTransSet.getValue().values());
		ArrayList<SNRSimilarityRecord> snrSimilarityRecords = SemanticNetRelationship.getSnrSimilarityRecords(sSnrSet, dSnrSet);
		HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings = new HashMap<SemanticNetRelationship, SemanticNetRelationship>();
		Collections.sort(snrSimilarityRecords, new SNRComparator());
		snrMappings = SemanticNetRelationship.mapSimilarRelationships(snrSimilarityRecords);
		snrMappings = SemanticNetRelationship.populateNullSNRs(sSnrSet, dSnrSet, snrMappings);
		
		return snrMappings;
	}
	public static ArrayList<SNRSimilarityRecord> getSnrSimilarityRecords(
			HashSet<SemanticNetRelationship> sSnrSet,
			HashSet<SemanticNetRelationship> dSnrSet) 
	{
		ArrayList<SNRSimilarityRecord> snrSimilarityRecords = new ArrayList<SNRSimilarityRecord>();
		for(SemanticNetRelationship ssnr : sSnrSet)
		{
			for(SemanticNetRelationship dsnr : dSnrSet)
			{
				snrSimilarityRecords.add(new SNRSimilarityRecord(ssnr, dsnr));
			}
		}
		return snrSimilarityRecords;
	}
	public static HashMap<SemanticNetRelationship, SemanticNetRelationship> populateNullSNRs(
			HashSet<SemanticNetRelationship> sSnrSet,
			HashSet<SemanticNetRelationship> dSnrSet,
			HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings) 
			{

		// populate null-prior SNRs
		
		for(SemanticNetRelationship snr : sSnrSet)
		{
			if(!snrMappings.containsKey(snr))
			{
				snrMappings.put(snr, new SemanticNetRelationship("posterior"));
			}
		}
		// populate null-posterior SNRs
		for(SemanticNetRelationship snr : dSnrSet)
		{
			if(!snrMappings.containsValue(snr))
			{
				snrMappings.put(new SemanticNetRelationship("prior"), snr);
			}
		}
		return snrMappings;
	}

	public static HashMap<SemanticNetRelationship, SemanticNetRelationship> mapSimilarRelationships(ArrayList<SNRSimilarityRecord> SNRSList) {
		// Find node with highest similarity value
		HashMap<SemanticNetRelationship, SemanticNetRelationship> pairings = new HashMap<SemanticNetRelationship, SemanticNetRelationship>();
		HashMap<SemanticNetRelationship, SNRSimilarityRecord> s_snr_2_snr_sr_map = new HashMap<SemanticNetRelationship, SNRSimilarityRecord>();
		HashMap<SemanticNetRelationship, SNRSimilarityRecord>  d_snr_2_snr_sr_map = new HashMap<SemanticNetRelationship, SNRSimilarityRecord>();
		//HashMap<String, String> S2DMap = new HashMap<String, String>();
		//HashMap<String, String> D2SMap = new HashMap<String, String>();
		// SemanticNetNode maxSimilarityNode = null;
	
		for (SNRSimilarityRecord snr_sr : SNRSList) 
		{
			//SemanticNetNode sNode = s1.getNodeByName(nsr.getSourceNodeName());
			//SemanticNetNode dNode = s2.getNodeByName(nsr.getDestinationNodeName());
			if(!pairings.containsKey(snr_sr.getSourceSNR()) && !pairings.containsValue(snr_sr.getDestinationSNR()))
			{
				pairings.put(snr_sr.getSourceSNR(), snr_sr.getDestinationSNR());
				s_snr_2_snr_sr_map.put(snr_sr.getSourceSNR(), snr_sr);
				d_snr_2_snr_sr_map.put(snr_sr.getDestinationSNR(), snr_sr);
				//S2DMap.put(nsr.getSourceNodeName(), nsr.getDesinationNodeName());
				//D2SMap.put(nsr.getDesinationNodeName(), nsr.getSourceNodeName());
			}
			else if (pairings.containsKey(snr_sr.getSourceSNR())) // risk of redundantly mapping same source node
			{
				
				//pairings.put(new SemanticNetRelationship("prior"), snr.getDestinationSNR());
				SNRSimilarityRecord prior_snr_sr = s_snr_2_snr_sr_map.get(snr_sr.getSourceSNR());
				//SemanticNetRelationship priorDSNR = pairings.get(snr.getDestinationSNR());
				//SemanticNetRelationship priorSSNR = pairings.get(snr.getSourceSNR());
				//SemanticNetNode priorDNode = s2.getNodeByName(priorNsr.getDestinationNodeName());
				if(snr_sr.similarity > prior_snr_sr.similarity)
				{
					pairings.remove(prior_snr_sr.getSourceSNR());
					s_snr_2_snr_sr_map.remove(prior_snr_sr.getSourceSNR());
					d_snr_2_snr_sr_map.remove(prior_snr_sr.getDestinationSNR());
					
					pairings.put(snr_sr.getSourceSNR(), snr_sr.getDestinationSNR());
					s_snr_2_snr_sr_map.put(snr_sr.getSourceSNR(), snr_sr);
					d_snr_2_snr_sr_map.put(snr_sr.getDestinationSNR(), snr_sr);
					/*if(sNSRMap.size() == s1.getNodes().size())
					{
						pairings.put((, value)
					}*/
				}
				else if(snr_sr.similarity > prior_snr_sr.similarity)
				{
					//Log These Cases...
				}
				
			}
			else // risk of redundantly mapping same destination node
			{
				//pairings.put( snr.getSourceSNR(), new SemanticNetRelationship("posterior"));
			
				SNRSimilarityRecord prior_snr_sr = d_snr_2_snr_sr_map.get(snr_sr.getDestinationSNR());
				if(snr_sr.similarity > prior_snr_sr.similarity)
				{
					pairings.remove(prior_snr_sr.getSourceSNR());
					s_snr_2_snr_sr_map.remove(prior_snr_sr.getSourceSNR());
					d_snr_2_snr_sr_map.remove(prior_snr_sr.getDestinationSNR());
					
					pairings.put(snr_sr.getSourceSNR(), snr_sr.getDestinationSNR());
					s_snr_2_snr_sr_map.put(snr_sr.getSourceSNR(), snr_sr);
					d_snr_2_snr_sr_map.put(snr_sr.getDestinationSNR(), snr_sr);
				}
				else if(snr_sr.similarity > prior_snr_sr.similarity)
				{
					//Log These Cases...
				}
			}
		}
		return pairings;
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
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getNullType() {
		return nullType;
	}
	private void setNullType(String nullType) {
		this.nullType = nullType;
	}
	public boolean isNull() {
		return isNull;
	}
	private void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	public String getSourcePositionSignature() {
		return sourcePositionSignature;
	}
	public void setSourcePositionSignature(String sourcePositionSignature) {
		this.sourcePositionSignature = sourcePositionSignature;
	}
	public String getDestinationPositionSignature() {
		return destinationPositionSignature;
	}
	public void setDestinationPositionSignature(
			String destinationPositionSignature) {
		this.destinationPositionSignature = destinationPositionSignature;
	}
	public HashMap<String,String> getDestinationPositionAttrs() {
		return destinationPositionAttrs;
	}
	public void setDestinationPositionAttrs(HashMap<String,String> destinationPositionAttrs) {
		this.destinationPositionAttrs = destinationPositionAttrs;
		for(Entry<String,String> e : destinationPositionAttrs.entrySet())
		{
			this.destinationPositionAttrsEnum.put(e.getKey(), e.getValue().split("_").length);
		}
	}
	public HashMap<String, String> getSourcePositionAttrs() {
		return sourcePositionAttrs;
	}
	public void setSourcePositionAttrs(HashMap<String,String> sourcePositionAttrs) {
		this.sourcePositionAttrs = sourcePositionAttrs;
		for(Entry<String,String> e : destinationPositionAttrs.entrySet())
		{
			this.sourcePositionAttrsEnum.put(e.getKey(), e.getValue().split("_").length);
		}
	}
	public HashMap<String, Integer> getSourcePositionAttrsEnum() {
		return sourcePositionAttrsEnum;
	}
	public HashMap<String, Integer> getDestinationPositionAttrsEnum() {
		return destinationPositionAttrsEnum;
	}
	public HashSet<String> getSourcePositionAttrsEnumAsSet() {
		HashSet<String> returnableSet = new HashSet<String>();
		for(Entry<String, Integer> e : sourcePositionAttrsEnum.entrySet())
		{
			returnableSet.add(e.getKey()+"_"+String.valueOf(e.getValue()));
		}
		return returnableSet;
	}
	public HashSet<String> getDestinationPositionAttrsEnumAsSet() {
		HashSet<String> returnableSet = new HashSet<String>();
		for(Entry<String, Integer> e : destinationPositionAttrsEnum.entrySet())
		{
			returnableSet.add(e.getKey()+"_"+String.valueOf(e.getValue()));
		}
		return returnableSet;
	}

}

class SNRSimilarityRecord {
	int quality;
	int similarity;
	int difference;
	HashSet<String> similarities;
	HashSet<String> differences;
	private SemanticNetRelationship sSNR;
	private SemanticNetRelationship dSNR;

	public SNRSimilarityRecord(SemanticNetRelationship sSNR, SemanticNetRelationship dSNR) {
		this.sSNR = sSNR;
		this.dSNR = dSNR;
		this.similarity = this.calculateSimilarity(sSNR, dSNR);
	}

	public int getSimilarity() {
		return similarity;
	}
	public int getQuality() {
		return quality;
	}
	public int getNumDifference() {
		return difference;
	}
	public SemanticNetRelationship getSourceSNR() {
		return this.sSNR;
	}
	public SemanticNetRelationship getDestinationSNR() {
		return this.dSNR;
	}
	public int calculateSimilarity(SemanticNetRelationship sSNR2, SemanticNetRelationship dSNR2) {
		int similarity = 0;

	    Set<String> sTransSpecSet = new HashSet<String>(Arrays.asList(this.sSNR.getTransformationSpecification()));
	    Set<String> dTransSpecSet = new HashSet<String>(Arrays.asList(this.dSNR.getTransformationSpecification()));
	    Set<String> similaritiesTransSpecSet = new HashSet<String>(sTransSpecSet);
	    Set<String> differencesTransSpecSet = new HashSet<String>(sTransSpecSet);
	    similaritiesTransSpecSet.retainAll(dTransSpecSet);
	    differencesTransSpecSet.removeAll(dTransSpecSet);
	    similarity += similaritiesTransSpecSet.size() - differencesTransSpecSet.size();
	    
	    this.difference = differencesTransSpecSet.size();
	    this.quality = 1*(similaritiesTransSpecSet.size() / sTransSpecSet.size());
	    this.similarities = new HashSet<String>(similaritiesTransSpecSet);
	    this.differences = new HashSet<String>(differencesTransSpecSet);
	    
		return similarity;
	}
	
	public String toString() {
		return this.sSNR.getTransformationSpecification()+" : "+this.dSNR.getTransformationSpecification()+" "+this.similarity;
	}
}

class SNRComparator implements Comparator<SNRSimilarityRecord>{
    @Override
    public int compare(SNRSimilarityRecord snr1, SNRSimilarityRecord snr2) {

        if(snr1.getSimilarity() < snr2.getSimilarity()){
            return 1;
        } else if(snr1.getSimilarity() < snr2.getSimilarity()){
            return -1;
        }
        else
        {
        	return 0;
        }
    }
}






package kbai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import ravensproject.RavensObject;

public class PatternReasoner {
	HashMap<Integer, String> generalizedRowBehaviors = new HashMap <Integer, String>();
	HashMap<Integer, String> generalizedTransitionBehaviors = new HashMap <Integer, String>();
	String generalizedMatrixBehavior;
	private SemanticNetwork sn;
	
	public PatternReasoner(SemanticNetwork sn)
	{
		this.sn = sn;
	}
	
	//get constants
	//get ...
	//for all transforamtions from first state
		//if present in next state
			//find most similar transformation
				//take intersection of the two
					//try to generalized differences as 'behavior'
	//What attributes stay the same? What attributes are distinct in the figure from node to node?
	//what happens? a thing is created...a fill is applied...etc...
	public String ReasonConstantPatterns(int row)
	{
		//GetEntityPresencePattern();
		//sn.getStateBySequence(i);
		HashSet<SemanticNetRelationship> sSnrSet = new HashSet<SemanticNetRelationship>();
		HashSet<SemanticNetRelationship> dSnrSet = new HashSet<SemanticNetRelationship>();
		//HashMap<SemanticNetRelationship, SemanticNetRelationship> snrSimilarityMap = new HashMap<SemanticNetRelationshipSemanticNetRelationship>();
		ArrayList<SNRSimilarityRecord> snrSimilarityRecords = SemanticNetRelationship.getSnrSimilarityRecords(sSnrSet, dSnrSet);
		HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings = new HashMap<SemanticNetRelationship, SemanticNetRelationship>();
		String behavior = "";
		
		
		Collections.sort(snrSimilarityRecords, new SNRComparator());
		snrMappings = SemanticNetRelationship.mapSimilarRelationships(snrSimilarityRecords);
		snrMappings = SemanticNetRelationship.populateNullSNRs(sSnrSet, dSnrSet, snrMappings);
		//!!!!!!!!!!!
		//may need prior/posterior SemNetRelas to act as placeholders for pattern induction...!!!!!!!!!!!!!!!!
		//focus on PRIOR then POSTERIOR transofrmationsDescript, how does PRIOR inform POSTERIOR???
		//How can be POSTERIAOR be described in terms of PRIOR???
		//POSTERIOR is PRIOR plus/sans/increase/decrease BLAH....!!!!
		//!!!!!!!!!!!
		
			/*for(int j = 0; j < sn.getColumns()-2; j++)
			{
				sSnrSet.addAll(sn.getTransformationsBySequence(j).values());
				dSnrSet.addAll(sn.getTransformationsBySequence(j+1).values());
			}
			for(SemanticNetRelationship snr : sSnrSet)
			{
				String[] spec = specs1.split("_");
			}*/
		return behavior;
	}

	private void GetEntityPresencePattern() {
		/*ArrayList<ArrayList<Integer>> entityPresence = new ArrayList<ArrayList<Integer>>();
		int offset = 0;
		int minDifference = 99;
		for(int i = 1; i < sn.getRows()-1; i++)
		{
			entityPresence.add(new ArrayList<Integer>());
			for(int j = 1; i < sn.getColumns(); i++)
			{
				entityPresence.get(i-1).add(sn.getStateByPosition(i, j).getNodes().size());
			}
		}

		//int[][] differenceMatrix = new int[entityPresence.size()-1][entity];
		for(int i = 0; i < entityPresence.size()-1; i++) 
		{
			int[] differenceMatrix = new int[entityPresence.get(i).size()];
			for(int j = 0; j < entityPresence.get(i).size(); j++)
			{
				for(int k = 0; j < entityPresence.get(i).size(); k++)
				{
					entityPresence.get(i).get(j) - entityPresence.get(i+1).get(k));
				}
			}
		}*/
	}

	private String GeneralizeBehaviorFromSpecs(String behavior,
			ArrayList<String> tSpecs) {
		if (behavior.isEmpty() && tSpecs.size() > 0)
		{
			behavior = tSpecs.remove(0);
		}
		
		for(String s : tSpecs)
		{
			behavior = GeneralizeSpecification(behavior, s);
		}
		
		return null;
	}
	private String GeneralizeSpecification(String behavior, String s) {
		// TODO Auto-generated method stub
		return null;
	}
	/*public GeneralizeTransitionBehavior()
	{
		this.sn = sn;
	}
	public GeneralizeMatrixBehavior()
	{
		this.sn = sn;
	}*/

}


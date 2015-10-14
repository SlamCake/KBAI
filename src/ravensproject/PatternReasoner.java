package ravensproject;

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

public class PatternReasoner {
	HashMap<Integer, String> generalizedRowBehaviors = new HashMap <Integer, String>();
	HashMap<Integer, String> generalizedTransitionBehaviors = new HashMap <Integer, String>();
	String generalizedMatrixBehavior;
	ProgressionConcept pc;
	private SemanticNetwork sn;
	
	public PatternReasoner(SemanticNetwork sn)
	{
		this.sn = sn;
		this.pc = new ProgressionConcept(sn);
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
		
		//TODO determine lowest deltas, if multiple low deltas exist then choose the delta with the lowest variance
		
		int[][] entityPresenceMatrix = new int[sn.getRows()][sn.getColumns()];
		int[][] entityPresenceDeltaMatrix = new int[sn.getRows()][sn.getColumns()];
		HashMap<String, Integer> entityDeltaVarianceMap = new HashMap<String, Integer>();
		for(int i = 1; i < sn.getRows(); i++)
		{
			for(int j = 1; j < sn.getColumns(); j++)
			{
				if(i!=sn.getRows() && j!=sn.getRows())
				{
					entityPresenceMatrix[i][j] = sn.getStateByPosition(i, j).getNodes().size();
				}
			}
		}

		/*for(int i = 1; i < entityPresenceMatrix.length; i++)
		{
			for(int j = 1; j < entityPresenceMatrix[i].length; j++)
			{
				for(int k = 0; i < entityPresenceMatrix[i+1].length; i++) // loop through cells in posterior row
				{
					if(i!=sn.getRows() && j!=sn.getRows())
					{
						entityPresenceDeltaMatrix[i][j] = sn.getStateByPosition(i, j).getNodes().size();
					}
				}
			}
		}*/

		
		//TODO IF CONDITION TO CATCH CASE WHERE SETS OF ENTITY VALUES DO NOT DIFFER
		
		//TODO SELECT FOR MINIMAL DELTAS, THEN FOR UNIFORM DELTAS
		
		//LOOP THROUGH PRESENCE TO DETERMINE DELTA POSSIBILITIES
		for(int i = 0; i < entityPresenceMatrix.length-2; i++)// loop through rows
		{
			for(int j = 0; i < entityPresenceMatrix[i].length; i++) // loop through cells in prior row
			{
				//int min_delta_ij_ip1k = 99;
				//ArrayList<Integer> deltas = new ArrayList<Integer>();
				for(int k = 0; i < entityPresenceMatrix[i+1].length; i++) // loop through cells in posterior row
				{
					entityPresenceDeltaMatrix[j][k] = Math.abs((entityPresenceMatrix[i+1][k] - entityPresenceMatrix[i][j]));
					//deltas.add(Math.abs((entityPresenceMatrix[i+1][k] - entityPresenceMatrix[i][j])));
				}
				//entityPresenceDeltaMatrix.put(String.valueOf(i)+","+String.valueOf(j), deltas);
				//patternSequencePairs.put(key, value)
				//for(Entry<String, ArrayList<Integer> e : entityPresenceDeltaMap.entrySet())
				//{
					
				//}
			}
			
		}
		
		// LOOP THROUGH DELTA MATRIX TO GATHER PAIRINGS WITH MINIMAL VARIANCE
		for(int c1 = 0; c1 < entityPresenceDeltaMatrix[0].length-1; c1++)// loop through path candidates (corresponds to first deltas of 
		{
			int minVariance = 0;
			String sequenceMapping = "1,"+String.valueOf(c1);
			HashSet<Integer> c_prior  = new HashSet<Integer>();
			int startVal = entityPresenceDeltaMatrix[0][c1];
			c_prior.add(c1);
			//HashSet<Integer> prior_r  = new HashSet<Integer>();
			for(int r = 1; r < entityPresenceDeltaMatrix.length; r++) // loop through rows (mapping to i+1, k)
			{
				int minDelta = 99;
				String nextMapping = "";
				int c_traversed = -1;
				for(int c2 = 0; c2 < entityPresenceDeltaMatrix.length; c2++) // loop through columns in (i+1, k) 
				{
					if(!c_prior.contains(c2))	// if column has not been selected for minimal variances from the prior value.
					{
						if(minDelta < entityPresenceDeltaMatrix[r][c2] - startVal)
						{
							c_traversed = c2;
							nextMapping += "_"+String.valueOf(r)+","+String.valueOf(c2);
							minDelta = Math.abs(entityPresenceDeltaMatrix[r][c2] - startVal);
						}
					}
				}
				c_prior.add(c_traversed);
				minVariance += minDelta;
				sequenceMapping += nextMapping;
			}
			entityDeltaVarianceMap.put(sequenceMapping, minVariance);
		}
		ArrayList<Entry<String, Integer>> varianceMappingList = Utilities.mapToStortedList(entityDeltaVarianceMap);
		
		//TODO ADD SORT MAP BY VALUE TO UTILITIES...
		
		
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

	public void defineRowConcept(
			HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappingsFromSets) {
		// TODO Auto-generated method stub
		for(Entry<SemanticNetRelationship, SemanticNetRelationship> e : snrMappingsFromSets.entrySet())
		{
			String[] s1 = e.getKey().getTransformationSpecification().split("_");
			String[] s2 = e.getKey().getTransformationSpecification().split("_");
		}
		
	}

	public ProgressionConcept defineProgressionConcept(
			HashSet<SemanticNetRelationship> priorTrans,
			HashSet<SemanticNetRelationship> postTrans) {
			pc.analyzeRowInstance(priorTrans, postTrans);
			
		
		return null;
	}

}


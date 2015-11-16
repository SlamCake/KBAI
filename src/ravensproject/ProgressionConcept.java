package ravensproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class ProgressionConcept {

	//private SemanticNetNode sourceNode;
	//private SemanticNetNode destinationNode;

	private SemanticNetwork sn;
	
	private HashMap<String, Integer> transSeqOrder = new HashMap<String, Integer>();
	
	private int rowOffset;
	
	private int numTotalTrans = 0;
	private ArrayList<Integer> numRowTrans = new ArrayList<Integer>();
	private ArrayList<Integer> numPriorTrans = new ArrayList<Integer>();
	private ArrayList<Integer> numPostTrans = new ArrayList<Integer>();

	private ArrayList<HashSet<String>> priorTransTypes = new ArrayList<HashSet<String>>();
	private ArrayList<HashSet<String>> postTransTypes = new ArrayList<HashSet<String>>();
	
	private ArrayList<HashSet<SemanticNetRelationship>> priorTrans = new ArrayList<HashSet<SemanticNetRelationship>>();
	private ArrayList<HashSet<SemanticNetRelationship>> postTrans= new ArrayList<HashSet<SemanticNetRelationship>>();
	
	private ArrayList<HashSet<String>> priorTransCompositions = new ArrayList<HashSet<String>>();
	private ArrayList<HashSet<String>> postTransCompositions = new ArrayList<HashSet<String>>();
	
	private ArrayList<ArrayList<HashSet<String>>> priorTransPositions = new ArrayList<ArrayList<HashSet<String>>>();
	private ArrayList<ArrayList<HashSet<String>>> postTransPositions = new ArrayList<ArrayList<HashSet<String>>>();
	
	private ArrayList<HashSet<String>> priorTransPositionDeltas = new ArrayList<HashSet<String>>();
	private ArrayList<HashSet<String>> postTransPositionDeltas = new ArrayList<HashSet<String>>();
	
	private ArrayList<HashSet<String>> priorTransPositionConst = new ArrayList<HashSet<String>>();
	private ArrayList<HashSet<String>> postTransPositionConst = new ArrayList<HashSet<String>>();
	
	private ArrayList<HashSet<String>> priorTransPositionAbs = new ArrayList<HashSet<String>>();
	private ArrayList<HashSet<String>> postTransPositionAbs = new ArrayList<HashSet<String>>();
	
	private ArrayList<HashSet<String>> col1_PosConfig = new ArrayList<HashSet<String>>();
	private ArrayList<HashSet<String>> col2_PosConfig = new ArrayList<HashSet<String>>();
	private ArrayList<HashSet<String>> col3_PosConfig = new ArrayList<HashSet<String>>();
	
	//private HashMap<String, HashSet<String>> transDistinctType = new HashMap<String, HashSet<String>>();
	private HashMap<String, HashSet<String>> transSetDistinctAttr = new HashMap<String, HashSet<String>>();
	//private HashSet<SemanticNetRelationship> predicted
	private HashMap<String, HashSet<String>> constraints = new HashMap<String, HashSet<String>>();
	private HashSet<SemanticNetRelationship> predictedTransformations = new HashSet<SemanticNetRelationship>();
	private HashMap<String, String> predictedParameters = new HashMap<String,String>();
	private HashSet<SemanticNetRelationship> redundantTransformations = new HashSet<SemanticNetRelationship>();
	private String[][] predictedPositionMatrix;
	//private ArrayList<String> postConditions;
	//private ArrayList<String> postConditions;
	
	public ProgressionConcept(SemanticNetwork sn) {
		this.sn = sn;
	}

	public void getProgressionDataFromPriorTransformationSet(HashSet<SemanticNetRelationship> priorTrans) {

		numTotalTrans += (priorTrans.size());
		this.priorTrans.add(priorTrans);
		numPriorTrans.add(priorTrans.size());
		HashSet<String> tempHSComp = new HashSet<String>();
		HashSet<String> tempHSType = new HashSet<String>();
		ArrayList<HashSet<String>> tempHSPos = new ArrayList<HashSet<String>>();
		for(SemanticNetRelationship snr : priorTrans)
		{
			tempHSComp.add(snr.getAttribute());
			//tempHSPos.add(snr.getAttribute()+"_"+snr.getDestinationPositionSignature());
			tempHSPos.add(snr.getDestinationPositionAttrsEnumAsSet());
			if(!tempHSType.contains(snr.getAttribute()))
			{
				tempHSType.add(snr.getAttribute());
			}
			
		}
		
		priorTransCompositions.add(tempHSComp);
		priorTransPositions.add(tempHSPos);
		priorTransTypes.add(tempHSType);
		
		//tempHSComp.clear();
		//tempHSType.clear();
		//tempHSPos.clear();
	}
	
	public void getProgressionDataFromPosteriorTransformationSet(HashSet<SemanticNetRelationship> postTrans) {
		numTotalTrans += (postTrans.size());
		this.postTrans.add(postTrans);
		numPostTrans.add(postTrans.size());
		HashSet<String> tempHSComp = new HashSet<String>();
		HashSet<String> tempHSType = new HashSet<String>();
		ArrayList<HashSet<String>> tempHSPos = new ArrayList<HashSet<String>>();
		for(SemanticNetRelationship snr : postTrans)
		{
			tempHSComp.add(snr.getAttribute());
			//tempHSPos.add(snr.getAttribute()+"_"+snr.getDestinationPositionSignature());
			tempHSPos.add(snr.getDestinationPositionAttrsEnumAsSet());
			if(!tempHSType.contains(snr.getAttribute()))
			{
				tempHSType.add(snr.getAttribute());
			}
		}
		
		//if no transformations involve positions
		/*if(tempHSPos.size() == 0)
		{
			
		}*/
		postTransCompositions.add(tempHSComp);
		postTransPositions.add(tempHSPos);
		postTransTypes.add(tempHSType);
		
		//tempHSComp.clear();
		//tempHSType.clear();
		//tempHSPos.clear();
	}
	
	public void getProgressionDataFromRowTransformations(HashSet<SemanticNetRelationship> priorTrans, HashSet<SemanticNetRelationship> postTrans) {
		
		
		numRowTrans.add(priorTrans.size()+postTrans.size());
		
		getProgressionDataFromPriorTransformationSet(priorTrans);	
		getProgressionDataFromPosteriorTransformationSet(postTrans);
		
		
		
		
		//code to check if numRowTrans > 1, check if prior 1 transSet == posterior 2 transSet, assert offset.
		
		//if  create || delete || relPosDeltaTrans...check resulting position configuration...
		//if all prior/post position affecting transSet result in the same position-configuration commonalities 
		// (i.e. if all prior/post sets share 1+ position delta)...
		// set constraint that says 'transformations must result in position configuration agreement with prior row instances'
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// put differently, if left-of etc position array in STATE has the same outcome for each column of frames...
		// ensure that the query frame ends up in the same positional configuration....
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		// !!!!!
		// build logic to identify what is distinct, then say 'x pos changed s.t.: DIAMOND left of: 1 && circle left of: 0
		// && total left of positions : changes like 2 -> 1 -> 2...
		// !!!
		
		// Add logic to specify the number of nodes??????
	}


	public void generateConstraints() {

		predictTransformationCount();
		predictTransformationComposition();
		predictTransformationTypeByIntersection();
		predictTransformationPositionByIntersection();
		classifyTransformationCompositionBehavior();
		substituteTransformationsByComposition();
		predictRedundantTransformations();
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		//generateConstraints for number of transformations...row by row...
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		
		
		//GET POSSIBLE (currently avail) AND REQUIRED TRANSFORMATION TYPES
		
		/*
		else if(priorTransNumDeltas.get(0) == priorTransNumDeltas.get(1) && postTransNumDeltas.get(0) && priorTransNumDeltas.get(0))
		{
			predictedParameters.put("TransNum", priorTransNumDeltas);
		}*/
		/*
		int numPosteriorTransVal = numPostTrans.get(0);
		boolean sameVal = true;
		for(int i = 0; i < numPostTrans.size(); i++)
		{
			if(numPosteriorTransVal != numPostTrans.get(i))
			{
				sameVal = false;
			}
		}
		
		if(sameVal)
		{
			//constant value
			
			predictedParameters.put("numPostTrans", String.valueOf(numPosteriorTransVal));
		}
		else
		{

			ArrayList<Integer> deltas = new ArrayList<Integer>();
			for(int i = 0; i < numPostTrans.size()-1; i++)
			{
				deltas.add(numPostTrans.get(i+1) - numPostTrans.get(i));
			}
			if(deltas.get(0) == deltas.get(1))
			{
				//constant increase/decrease change

				predictedParameters.put("numRowTrans", String.valueOf(numPostTrans.get()));
				
			}
			else if(deltas.get(0) < 0) // row 2 has fewer transformations than row 1
			{
				if(deltas.get(0) > deltas.get(1)) // row 2 has fewer transformations than row 3
				{
					
				}
				else if(deltas.get(0) < deltas.get(1)) // row 2 has more transformations than row 3
				{
					
				}
			}
			else if(deltas.get(0) > 0) // row 2 has more transformations than row 1
			{
				if(deltas.get(0) > deltas.get(1)) // row 2 has fewer transformations than row 3
				{
					
				}
				else if(deltas.get(0) < deltas.get(1)) // row 2 has more transformations than row 3
				{
					
				}
			}
		}*/
		
		//!!!!!!
		//Gen constraints for transformation compositions
		//We may not care about composition if variance is too high or if one set does not subsume the other
		//We may want to test for an intersection presence of some kind...
		//!!!!!!
		/*
		ArrayList<HashSet<String>> differenceListPri2Post = new ArrayList<HashSet<String>>();
		ArrayList<HashSet<String>> differenceListPri2Pri = new ArrayList<HashSet<String>>();
		ArrayList<HashSet<String>> differenceListPost2Post = new ArrayList<HashSet<String>>();
		ArrayList<HashSet<String>> differenceListRow2Row = new ArrayList<HashSet<String>>();
		
		HashSet<String> differenceSet = new HashSet<String>();
		for(int i = 0; i < postTransCompositions.size()-1; i++)
		{
			differenceSet.addAll(Utilities.difference(priorTransCompositions.get(i), priorTransCompositions.get(i+1)));
			differenceListPri2Pri.add(differenceSet);
			differenceSet.clear();
			
			if(i < postTransCompositions.size()-2)
			{
				differenceSet.addAll(Utilities.difference(priorTransCompositions.get(i), postTransCompositions.get(i)));
				differenceListPri2Post.add(differenceSet);
				differenceSet.clear();

				differenceSet.addAll(Utilities.difference(postTransCompositions.get(i), postTransCompositions.get(i+1)));
				differenceListPost2Post.add(differenceSet);
				differenceSet.clear();

				HashSet<String> tempRowComp_1 = priorTransCompositions.get(i);
				tempRowComp_1.addAll(postTransCompositions.get(i));
				
				HashSet<String> tempRowComp_2 = priorTransCompositions.get(i+1);
				tempRowComp_2.addAll(postTransCompositions.get(i+1));
				
				differenceSet.addAll(Utilities.difference(tempRowComp_1, tempRowComp_2));
				differenceListRow2Row.add(differenceSet);
				differenceSet.clear();
			}
		}*/
		
		//differenceSet = differenceListPri2Pri.get(0);
		/*
		boolean sameVal = true;
		for(int i = 0; i < differenceListPri2Pri.size()-1; i++)
		{
			if(!Utilities.difference(differenceListPri2Pri.get(0), differenceListPri2Pri.get(i)).isEmpty())
			{
			
				sameVal = false;
				//no change in priors
			}		
			
			if(Utilities.difference(differenceListPri2Pri.get(0), differenceListPri2Pri.get(i)).isEmpty())
			{
				HashSet<String> priorTCCompare_1 = priorTransCompositions.get(i);
				HashSet<String> priorTCCompare_2 = priorTransCompositions.get(i+1);
				for(String s : differenceListPri2Pri.get(i))
				{
					if(priorTCCompare_1.contains(s))
					{
						priorTCCompare_1.remove(s);
						//add transformation to 'remove' list
					}			
					else if(priorTCCompare_2.contains(s))
					{
						priorTCCompare_2.remove(s);
						//add transformation to 'add' list
					}
				}
			}			
			
		}*/

		

		
		
		//!!!!!!
		//Determine offset (is r1 prior == r2 posterior?, assert r2 prior == r3 posterior).
		//We may not care about composition if variance is too high or if one set does not subsume the other
		//We may want to test for an intersection presence of some kind...
		//!!!!!!
		
		//String [] compositionFeatures = constraints.get("TransComp").split("_");
		
		
		
	}


	private void substituteTransformationsByComposition() {
		//all transformations have same composition
		if(constraints.get("TransComp").contains("mConst"))
		{
			//constraints.put("offset", "1");
			predictedTransformations.addAll(this.postTrans.get(1));
		}
		// all rows have same composition
		else if(constraints.get("TransComp").contains("rConst")) 
		{
			predictedTransformations.addAll(this.priorTrans.get(2));
		}
		// all post trans sets have same composition
		else if(constraints.get("TransComp").contains("postConst"))
		{
			predictedTransformations.addAll(this.postTrans.get(1));
		}
		else if(constraints.get("TransComp").contains("offset_1"))
		{
			predictedTransformations.addAll(this.priorTrans.get(1));
		}
		
	}

	private void predictRedundantTransformations() {
		// check if target node already has expected value in prior states, 
		// if so then remove that transformation from predictions.
		// addresses problem C-12
		if(!predictedTransformations.isEmpty())
		{
			HashSet<SemanticNetRelationship> tempPredTrans = new HashSet<SemanticNetRelationship>(predictedTransformations);
			for(SemanticNetRelationship snr : tempPredTrans)
			{
				//SemanticNetRelationship snrCopy = new SemanticNetRelationship(snr);
				snr.getSourceStateName();
				SemanticNetNode prevRowTargetSNN = sn.getNodeByName(snr.getDestinationStateName(), snr.getDestinationNodeName());
				
				//JAVA Is returning a NULL STATE from the HASHMAP...WHY???
				SemanticNetState s_c1 = sn.getStateByPosition(sn.getRows(), sn.getColumns()-2);
				SemanticNetState s_c2 = sn.getStateByPosition(sn.getRows(), sn.getColumns()-1);
				
				String sig = snr.getDestinationPositionSignature();
				SemanticNetNode curRowTargetSNN_c1 = s_c1.getNodeByPositionSignature(sig);
				SemanticNetNode curRowTargetSNN_c2 = s_c2.getNodeByPositionSignature(sig);
				//SemanticNetNode curRowTargetSNN = sn.getStateByPosition(sn.getRows(), sn.getColumns()-1).getNodeByPositionSignature(snr.getDestinationPositionSignature());
				if(prevRowTargetSNN.calculateAttributeMatch(curRowTargetSNN_c1) && prevRowTargetSNN.calculateAttributeMatch(curRowTargetSNN_c2))
				{
					predictedTransformations.remove(snr);
					redundantTransformations.add(snr);
				}
			}
			//predictedTransformations.addAll(this.postTrans.get(1));
		}
		//get position and transformation outcomes...
	}

	private void classifyTransformationCompositionBehavior() {
		// get intersections of all compositions to specify what must exist in the answer set transformations
		HashSet<String> transComp_1_1 = priorTransCompositions.get(0);
		HashSet<String> transComp_1_2 = postTransCompositions.get(0);
		HashSet<String> transComp_2_1 = priorTransCompositions.get(1);
		HashSet<String> transComp_2_2 = postTransCompositions.get(1);
		HashSet<String> transComp_3_1 = priorTransCompositions.get(2);
		
		int m_differences = 0;
		HashSet<String> m_diff = (HashSet<String>) Utilities.symDifference(transComp_1_1, transComp_1_2);
		m_differences += m_diff.size();
		m_diff = (HashSet<String>) Utilities.symDifference
				(transComp_1_1, transComp_2_1);
		m_differences += m_diff.size();
		m_diff = (HashSet<String>) Utilities.symDifference(transComp_1_1, transComp_2_2);
		m_differences += m_diff.size();
		m_diff = (HashSet<String>) Utilities.symDifference(transComp_1_1, transComp_3_1);
		m_differences += m_diff.size();
		
		HashSet<String> r1_diff = (HashSet<String>) Utilities.symDifference(transComp_1_1, transComp_1_2);
		HashSet<String> r2_diff = (HashSet<String>) Utilities.symDifference(transComp_2_1, transComp_2_2);
		
		HashSet<String> post_diff = (HashSet<String>) Utilities.symDifference(transComp_1_2, transComp_2_2);

		HashSet<String> offset_diff = (HashSet<String>) Utilities.symDifference(transComp_1_1, transComp_2_2);
		
		constraints.put("TransComp", new HashSet<String>());
		if(m_differences == 0)
		{
			constraints.get("TransComp").add("mConst");
		}

		if(r1_diff.isEmpty() && r2_diff.isEmpty())
		{
			constraints.get("TransComp").add("rConst");
		}
		
		if(post_diff.isEmpty())
		{
			constraints.get("TransComp").add("postConst");
		}
		
		if(offset_diff.isEmpty())
		{
			constraints.get("TransComp").add("offset_1");
		}

		/*if(Utilities.difference(priorTransCompositions.get(0), postTransCompositions.get(1)).isEmpty() && 
				!constraints.get("TransComp").contains("rConst") && 
				!constraints.get("TransComp").contains("mConst"))
		{
			//constraints.put("offset", "1");
			predictedTransformations.addAll(this.priorTrans.get(1));
		}*/
	}
	
	private void predictTransformationComposition() {
		// get intersections of all compositions to specify what must exist in the answer set transformations
		HashSet<String> transCompIntersection = priorTransCompositions.get(0);
		for(int i = 0; i < priorTransCompositions.size() ; i++)
		{
			//for now don't worry about priorTransCompositions...
			transCompIntersection = (HashSet<String>) Utilities.intersection(transCompIntersection, priorTransCompositions.get(i));
			if(i < postTransCompositions.size())
			{
				transCompIntersection = (HashSet<String>) Utilities.intersection(transCompIntersection, postTransCompositions.get(i));
			}
		}
		
		if(!transCompIntersection.isEmpty())
		{
			constraints.put("transCompositionRequirements", transCompIntersection);
			//predictedTransformations MUST HAVE a transformation of the types in the intersection...
			//generalize transformation between post trans compositions
			//if there is only one instance of a type of transformation...generalize these
			//select the required pairs between posterior transformationst that are most similar.
			
			//predictedTransformations.addAll(transCompIntersection);
		}
		
		//difference of prior compositions
		HashSet<String> transCompPriorDiff_1 = (HashSet<String>) Utilities.difference(priorTransCompositions.get(2), priorTransCompositions.get(0));
		HashSet<String> transCompPriorDiff_2 = (HashSet<String>) Utilities.difference(priorTransCompositions.get(2), priorTransCompositions.get(1));
		
		if(transCompPriorDiff_1.size() == 0 ^ transCompPriorDiff_2.size() == 0)
		{
			if(transCompPriorDiff_1.size() == 0)
			{
				constraints.put("transCompositionPrediction", postTransCompositions.get(0));
			}
			else
			{
				constraints.put("transCompositionPrediction", postTransCompositions.get(1));
			}
			//predictedTransformations MUST HAVE a transformation of the types in the intersection...
			//generalize transformation between post trans compositions
			//if there is only one instance of a type of transformation...generalize these
			//select the required pairs between posterior transformationst that are most similar.
			
			//predictedTransformations.addAll(transCompIntersection);
		}
	}

	private void predictTransformationTypeByIntersection() {
		// get intersections of all transformation types to specify what must exist in the answer set transformations
		HashSet<String> transTypeIntersection = postTransTypes.get(0);
		for(int i = 0; i < priorTransTypes.size() ; i++)
		{
			//for now don't worry about priorTransCompositions...
			transTypeIntersection = (HashSet<String>) Utilities.intersection(transTypeIntersection, priorTransTypes.get(i));
			if(i < postTransTypes.size())
			{
				transTypeIntersection = (HashSet<String>) Utilities.intersection(transTypeIntersection, postTransTypes.get(i));
			}
		}
		if(!transTypeIntersection.isEmpty())
		{
			constraints.put("transTypeRequirements", transTypeIntersection);
		}
	}
	
	private void predictTransformationPositionByIntersection() {
		// get common position distinctons of posterior transformations to specify where the transformations should be applied
		HashSet<String> postTransPosIntersection = postTransPositions.get(0).get(0);
		for(int i = 0; i < postTransPositions.size() ; i++)
		{
			for(int j = 0; j < postTransPositions.get(i).size(); j++)
			{
			postTransPosIntersection = (HashSet<String>) Utilities.intersection(postTransPosIntersection, postTransPositions.get(i).get(j));
			}
		}
		
		if(!postTransPosIntersection.isEmpty())
		{
			constraints.put("transPositionRequirements", postTransPosIntersection);
			//define constraint that predicted posterior transformations should have the intersection of postTransPositions
			
			//predictedTransformations.addAll(transCompIntersection);
		}
		
	}

	private void predictTransformationCount() {
		ArrayList<Integer> priorTransNumDeltas = new ArrayList<Integer>();
		for(int i = 0; i < numPriorTrans.size()-1; i++)
		{
			priorTransNumDeltas.add(numPriorTrans.get(i+1) - numPriorTrans.get(i));
		}
		
		/*ArrayList<Integer> postTransNumDeltas = new ArrayList<Integer>();
		for(int i = 0; i < numPostTrans.size()-1; i++)
		{
			postTransNumDeltas.add(numPostTrans.get(i+1) - numPostTrans.get(i));
		}*/
		
		int row1TransNumDelta = numPostTrans.get(0) - numPriorTrans.get(0);
		int row2TransNumDelta = numPostTrans.get(1) - numPriorTrans.get(1);
		
		if(row1TransNumDelta == 0 && row2TransNumDelta == 0)
		{
			HashSet<String> hs = new HashSet<String>();
			hs.add(String.valueOf(numPriorTrans.get(2)));
			constraints.put("transNumRequirements", hs);
			predictedParameters.put("transNumRequirements", String.valueOf(numPriorTrans.get(2)));
		}
		else if (row1TransNumDelta == row2TransNumDelta)
		{
			HashSet<String> hs = new HashSet<String>();
			hs.add(String.valueOf(numPriorTrans.get(2)+row2TransNumDelta));
			constraints.put("transNumRequirements", hs);
			predictedParameters.put("transNumRequirements", String.valueOf(numPriorTrans.get(2)+row2TransNumDelta));
		}
	}

	//GET CONSTRAINTS AND PREDICTED TRANSFORMATIONS METHODS!!
	public HashSet<SemanticNetRelationship> getPredictedTransformations() {
		return this.predictedTransformations;
	}
	public HashSet<SemanticNetRelationship> getRedundantTransformations() {
		return this.redundantTransformations;
	}
	public HashMap<String, HashSet<String>> getConstraints() {
		return this.constraints;
	}
	public HashMap<String, String> getPredictedParameters() {
		return this.predictedParameters;
	}
	public void getProgressionDataFromStates(SemanticNetState s1, SemanticNetState s2, SemanticNetState s3 ) 
	{
		
		
	}
	
	public void predictConstraints() {
		
	}
	public void predictAssertions() {
		
	}

	public HashSet<String> getCompositionFromTransSet(
			Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashSet<String> getTypesFromTransSet(
			Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

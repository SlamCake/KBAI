package ravensproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ProgressionConcept {

	//private SemanticNetNode sourceNode;
	//private SemanticNetNode destinationNode;

	private SemanticNetwork sn;
	
	private HashMap<String, Integer> transSeqOrder = new HashMap<String, Integer>();
	
	private int rowOffset;
	
	private int numTotalTrans = 0;
	private ArrayList<Integer> numRowTrans;
	private ArrayList<Integer> numPriorTrans;
	private ArrayList<Integer> numPostTrans;

	private HashSet<String> priorTransTypes = new HashSet<String>();
	private HashSet<String> postTransTypes = new HashSet<String>();
	
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
	private HashSet<SemanticNetRelationship> predictedTransformations = new HashSet<SemanticNetRelationship>();;
	private String[][] predictedPositionMatrix;
	//private ArrayList<String> postConditions;
	//private ArrayList<String> postConditions;
	
	public ProgressionConcept(SemanticNetwork sn) {
		this.sn = sn;
	}

	
	public void getProgressionDataFromTransformations(HashSet<SemanticNetRelationship> priorTrans, HashSet<SemanticNetRelationship> postTrans) {
		
		this.priorTrans.add(priorTrans);
		this.postTrans.add(postTrans);
		
		numPriorTrans.add(priorTrans.size());
		numPostTrans.add(postTrans.size());
		numRowTrans.add(priorTrans.size()+postTrans.size());
		numTotalTrans += (priorTrans.size()+postTrans.size());
		
		HashSet<String> tempHSComp = new HashSet<String>();
		HashSet<String> tempHSType = new HashSet<String>();
		ArrayList<HashSet<String>> tempHSPos = new ArrayList<HashSet<String>>();
		
		for(SemanticNetRelationship snr : priorTrans)
		{
			tempHSComp.add(snr.getAttribute());
			//tempHSPos.add(snr.getAttribute()+"_"+snr.getDestinationPositionSignature());
			tempHSPos.add(snr.getDestinationPositionAttrsEnumAsSet());
			if(!priorTransTypes.contains(snr.getAttribute()))
			{
				priorTransTypes.add(snr.getAttribute());
			}
			
		}
		
		priorTransCompositions.add(tempHSComp);
		priorTransPositions.add(tempHSPos);
		
		tempHSComp.clear();
		tempHSType.clear();
		tempHSPos.clear();
		
		for(SemanticNetRelationship snr : postTrans)
		{
			tempHSComp.add(snr.getAttribute());
			//tempHSPos.add(snr.getAttribute()+"_"+snr.getDestinationPositionSignature());
			tempHSPos.add(snr.getDestinationPositionAttrsEnumAsSet());
			if(!postTransTypes.contains(snr.getAttribute()))
			{
				postTransTypes.add(snr.getAttribute());
			}
			
		}
		
		postTransCompositions.add(tempHSComp);
		postTransPositions.add(tempHSPos);
		
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
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		//generateConstraints for number of transformations...row by row...
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		
		int numRowVal = numRowTrans.get(0);
		boolean sameVal = true;
		for(int i = 0; i < numRowTrans.size(); i++)
		{
			if(numRowVal != numRowTrans.get(i))
			{
				sameVal = false;
			}
		}
		
		if(sameVal)
		{
			//constant value
		}
		else
		{

			ArrayList<Integer> deltas = new ArrayList<Integer>();
			for(int i = 0; i < numRowTrans.size()-1; i++)
			{
				deltas.add(numRowTrans.get(i+1) - numRowTrans.get(i));
			}
			if(deltas.get(0) == deltas.get(1))
			{
				//constant increase/decrease change
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
		}
		
		//!!!!!!
		//Gen constraints for transformation compositions
		//We may not care about composition if variance is too high or if one set does not subsume the other
		//We may want to test for an intersection presence of some kind...
		//!!!!!!
		
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
		}
		
		//differenceSet = differenceListPri2Pri.get(0);
		sameVal = true;
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
			
		}

		
		
		
		// get intersections of all compositions to specify what should exist in the answer set transformations
		HashSet<String> transCompIntersection = priorTransCompositions.get(0);
		for(int i = 0; i < priorTransCompositions.size()-1 ; i++)
		{
			transCompIntersection = (HashSet<String>) Utilities.intersection(transCompIntersection, priorTransCompositions.get(i));
			if(i < postTransCompositions.size()-1)
			{
				transCompIntersection = (HashSet<String>) Utilities.intersection(transCompIntersection, postTransCompositions.get(i));
			}
		}
		
		if(!transCompIntersection.isEmpty())
		{
			constraints.put("transComposition", transCompIntersection);
			//predictedTransformations MUST HAVE a transformation of the types in the intersection...
			//generalize transformation between post trans compositions
			//if there is only one instance of a type of transformation...generalize these
			//select the required pairs between posterior transformationst that are most similar.
			
			//predictedTransformations.addAll(transCompIntersection);
		}
		

		// get common position distinctons of posterior transformations to specify where the transformations should be applied
		HashSet<String> postTransPosIntersection = postTransPositions.get(0).get(0);
		for(int i = 0; i < postTransPositions.size()-1 ; i++)
		{
			for(int j = 0; j < postTransPositions.get(i).size()-1; j++)
			{
			postTransPosIntersection = (HashSet<String>) Utilities.intersection(postTransPosIntersection, postTransPositions.get(i).get(j));
			}
		}
		
		if(!postTransPosIntersection.isEmpty())
		{
			constraints.put("transPosition", postTransPosIntersection);
			//define constraint that predicted posterior transformations should have the intersection of postTransPositions
			
			//predictedTransformations.addAll(transCompIntersection);
		}
		
		
		//!!!!!!
		//Determine offset (is r1 prior == r2 posterior?, assert r2 prior == r3 posterior).
		//We may not care about composition if variance is too high or if one set does not subsume the other
		//We may want to test for an intersection presence of some kind...
		//!!!!!!
		
		//String [] compositionFeatures = constraints.get("TransComp").split("_");
		
		if(Utilities.difference(priorTransCompositions.get(0), postTransCompositions.get(1)).isEmpty() && 
				!constraints.get("TransComp").contains("rConst") && 
				!constraints.get("TransComp").contains("mConst"))
		{
			//constraints.put("offset", "1");
			predictedTransformations.addAll(this.priorTrans.get(1));
		}
		
		//all transformations have same composition
		else if(constraints.get("TransComp").contains("mConst"))
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
		
		// check if target node already has expected value in prior states, 
		// if so then remove that transformation from predictions.
		// addresses problem C-12
		if(!predictedTransformations.isEmpty())
		{
			for(SemanticNetRelationship snr : predictedTransformations)
			{
				snr.getSourceStateName();
				SemanticNetNode prevRowTargetSNN = sn.getNodeByName(snr.getDestinationStateName(), snr.getDestinationNodeName());
				SemanticNetNode curRowTargetSNN = sn.getStateByPosition(sn.getRows(), sn.getColumns()-1).getNodeByPositionSignature(snr.getDestinationPositionSignature());
				if(prevRowTargetSNN.calculateAttributeMatch(curRowTargetSNN))
				{
					predictedTransformations.remove(snr);
				}
			}
			//predictedTransformations.addAll(this.postTrans.get(1));
		}
		
		

		//get position and transformation outcomes...
		
	}

	public void getProgressionDataFromStates(SemanticNetState s1, SemanticNetState s2, SemanticNetState s3 ) 
	{
		
		
	}
	
	public void predictConstraints() {
		
	}
	public void predictAssertions() {
		
	}
	
}

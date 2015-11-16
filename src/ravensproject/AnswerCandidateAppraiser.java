package ravensproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class AnswerCandidateAppraiser {
	SemanticNetwork sn;
	public int AppraiseAnswerCandidates(SemanticNetwork sn) {
		this.sn = sn;
		int answerCandidate = -1;
		//Add logic here in the future to deal with the 2x2, 3x3, and NxN RPM questions...
		//if no transformations, we know to choose an answer state that differs least from the final questionState.
		if(sn.getRPMType().equals("2x2"))
		{
			answerCandidate = this.appraiseAnswers_2x2();
		}
		else
		{
			answerCandidate = this.appraiseAnswers_3x3();
		}
		return answerCandidate;
	}
	private ArrayList<SemanticNetState> nullTransformationPrune(ArrayList<SemanticNetState> answerCandidates)
	{
		//If this is a zero change state, all answer states which have some kind of an SNR are not unchanged.
		//Remove states from answer candidates which have SNRs.
		NamePredicate filter = new NamePredicate();
		for(Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet : sn.getTransformationsQA().entrySet()) 
		{
			if(answerTransSet.getValue().size()!=0)
			{
				filter.name = answerTransSet.getKey();
				answerCandidates.removeIf(filter);
			}
		}
		return answerCandidates;
	}
	private int appraiseAnswers_3x3()
	{
		PatternReasoner pr = new PatternReasoner(this.sn);
		int answerCandidate = -1;
		int rows = sn.getRows();
		int columns = sn.getColumns();

		//ArrayList<ArrayList<SemanticNetRelationship>> possibleTransformationsRows = new ArrayList<ArrayList<SemanticNetRelationship>>();
		//ArrayList<ArrayList<SemanticNetRelationship>> possibleTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
		//ArrayList<ArrayList<SemanticNetRelationship>> assertedTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
		//ArrayList<SemanticNetRelationship> possibleTransformations = new ArrayList<SemanticNetRelationship>();
		//ArrayList<SemanticNetRelationship> assertedTransformations = new ArrayList<SemanticNetRelationship>();

		ArrayList<SemanticNetState> answerCandidates = new ArrayList<SemanticNetState>(sn.getAnswerStates());
		HashMap<String, Integer> answerCandidateFitness = new HashMap<String, Integer>();

		//change this pattern to check sequence size...
		int size_1 = sn.getTransformationsByState().get("B").size();
		int size_2 = sn.getTransformationsByState().get("E").size();
		/*if(sn.getTransformationsByState().get("B").size() == 0 && sn.getTransformationsByState().get("E").size() == 0) //no observed transformations!!
		{
			answerCandidates = nullTransformationPrune(answerCandidates);
		}*/
		if(size_1 == 0 && size_2 == 0 )
		{
			answerCandidates = nullTransformationPrune(answerCandidates);
		}
		else
		{


	    	String[] logArgs;
	    	if(KBAILogging.metrics)
	    	{
	        	logArgs = new String[]{"Pattern Determination", "start"};
	        	KBAILogging.updateLog("performance", logArgs);;
	        	KBAILogging.updateLog("memory consumption", logArgs);
	    	}

			ProgressionConcept pc = buildProgressionConcept();

	    	if(KBAILogging.metrics)
	    	{
	        	logArgs = new String[]{"Pattern Determination", "stop"};
	        	KBAILogging.updateLog("performance", logArgs);
	        	KBAILogging.updateLog("memory consumption", logArgs);
	    	}
	    	

			HashMap<String, HashSet<String>> constraints = pc.getConstraints();
			HashSet<SemanticNetRelationship> predictedTransformations = pc.getPredictedTransformations();
			HashSet<SemanticNetRelationship> redundantTransformations = pc.getRedundantTransformations();
			if(predictedTransformations.size() == 0 && redundantTransformations.size() > 0)
			{
				constraints.clear();
			}


			for(Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet : sn.getTransformationsQA().entrySet()) 
			{


				//TODO DEBUG THIS SNR MAPPER!!!!!!!!!!!!!!!!!!!!!!!!!!

				HashSet<SemanticNetRelationship> aSnrSet = new HashSet<SemanticNetRelationship>(answerTransSet.getValue().values());
				HashMap<SemanticNetRelationship, SemanticNetRelationship> predictedSnrMappings = SemanticNetRelationship.getSnrMappingsFromSets(predictedTransformations, aSnrSet);
				//HashMap<SemanticNetRelationship, SemanticNetRelationship> redundantSnrMappings = SemanticNetRelationship.getSnrMappingsFromSets(redundantTransformations, aSnrSet);

				calculateAnswerFitness(predictedSnrMappings, answerCandidateFitness, constraints, answerTransSet.getKey());

				//answerCandidateFitness.put(answerTransSet.getKey(), answerFitness);
			}		
		}

		answerCandidate = selectAnswerByMaxFitness(answerCandidates, answerCandidateFitness);
		return answerCandidate;
	}



	private ProgressionConcept buildProgressionConcept() {
		//ArrayList<ArrayList<SemanticNetRelationship>> possibleTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
		//ArrayList<ArrayList<SemanticNetRelationship>> assertedTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
		ProgressionConcept pc = new ProgressionConcept(sn);
		HashSet<SemanticNetRelationship> priorTrans_1 = new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("A").values());
		HashSet<SemanticNetRelationship> postTrans_1= new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("B").values());

		//HashSet<SemanticNetRelationship> aSnrSet = new HashSet<SemanticNetRelationship>(answerTransSet.getValue().values());
		//HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings = SemanticNetRelationship.getSnrMappingsFromSets(qSnrSet, aSnrSet);


		//pr.defineProgressionConcept(SemanticNetRelationship.getSnrMappingsFromSets(priorTrans_1, postTrans_1));

		HashSet<SemanticNetRelationship> priorTrans_2 = new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("D").values());
		HashSet<SemanticNetRelationship> postTrans_2 = new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("E").values());
		HashSet<SemanticNetRelationship> priorTrans_3 = new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("G").values());


		pc.getProgressionDataFromRowTransformations(priorTrans_1, postTrans_1);
		pc.getProgressionDataFromRowTransformations(priorTrans_2, postTrans_2);
		pc.getProgressionDataFromPriorTransformationSet(priorTrans_3);
		pc.generateConstraints();
		return pc;
	}
	private int appraiseAnswers_2x2()
	{
		int answerCandidate = -1;

		NamePredicate filter = new NamePredicate();
		ArrayList<SemanticNetState> answerCandidates = new ArrayList<SemanticNetState>(sn.getAnswerStates());
		HashMap<String, Integer> answerCandidateFitness = new HashMap<String, Integer>();
		if(sn.getTransformationsByState().get("A").size() == 0) //no observed transformations!!
		{
			//If this is a zero change state, all answer states which have some kind of an SNR are not unchanged.
			//Remove states from answer candidates which have SNRs.
			for(Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet : sn.getTransformationsQA().entrySet()) 
			{
				if(answerTransSet.getValue().size()!=0)
				{
					filter.name = answerTransSet.getKey();
					answerCandidates.removeIf(filter);
				}
			}
			/*for(SemanticNetRelationship snr : sn.getTransformationsQA().values())
			{
				filter.name = snr.getDestinationStateName();
				answerCandidates.removeIf(filter);
			}*/
		}
		else
		{
			//evaluate similarity of transformations w respect to A->B
			//evaluate similarity of node attributes w/ respect to C 

			//mapping has already been done so for now we assume that is sufficent to directly compare transformations
			//and not consider the impact of the other node attributes to 'fitness'

			//may need to analogize the nodes from A->B to C...
			//relative attributes could come in handy for the purposes of analogous mapping

			//does a transformation like this occur? to what specificity?
			//does this transformation apply to a node whose attributes are similar to another node
			//is this posterior node comparable or does it have attribute similarities to the prior?

			//analogical reasoning may benefit from ONLY considering similarities for relative attribute types...

			//entry sets of transformations mapped to answer state names...

			//Construct a list of attributes that are transformed in relevant questionState...
			//ArrayList<String> possibleTransformations = new ArrayList<String>();
			//ArrayList<String> assertedTransformations = new ArrayList<String>();
			HashSet<SemanticNetRelationship> qSnrSet = new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("A").values());
			for(Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet : sn.getTransformationsQA().entrySet()) 
			{


				//TODO DEBUG THIS SNR MAPPER!!!!!!!!!!!!!!!!!!!!!!!!!!

				HashSet<SemanticNetRelationship> aSnrSet = new HashSet<SemanticNetRelationship>(answerTransSet.getValue().values());
				HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings = SemanticNetRelationship.getSnrMappingsFromSets(qSnrSet, aSnrSet);

				/*HashSet<SemanticNetRelationship> aSnrSet = new HashSet<SemanticNetRelationship>(answerTransSet.getValue().values());
				ArrayList<SNRSimilarityRecord> snrSimilarityRecords = SemanticNetRelationship.getSnrSimilarityRecords(qSnrSet, aSnrSet);
				HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings = new HashMap<SemanticNetRelationship, SemanticNetRelationship>();
				Collections.sort(snrSimilarityRecords, new SNRComparator());
				snrMappings = SemanticNetRelationship.mapSimilarRelationships(snrSimilarityRecords);
				snrMappings = SemanticNetRelationship.populateNullSNRs(qSnrSet, aSnrSet, snrMappings);*/

				/*for(Entry<String, SemanticNetRelationship> questionTrans : sn.getTransformationsByState().get("A").entrySet())
				{
					possibleTransformations.add(questionTrans.getValue().getTransformationSpecification());
				}*/
				/*for(Entry<String, SemanticNetRelationship> answerTrans : answerTransSet.getValue().entrySet())
				{
					assertedTransformations.add(answerTrans.getValue().getTransformationSpecification());
				}*/



				calculateAnswerFitness(snrMappings, answerCandidateFitness, answerTransSet.getKey());
			}				
		}



		answerCandidate = selectAnswerByMaxFitness(answerCandidates, answerCandidateFitness);
		return answerCandidate;
	}

	private void calculateAnswerFitness(
			HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings, HashMap<String, Integer> answerCandidateFitness, HashMap<String, HashSet<String>> constraints, String figureName) {
		int answerFitness = 0;
		
		HashSet<SemanticNetRelationship> answerSNRs = new HashSet<SemanticNetRelationship>();
		HashSet<String> answerSNRTypes = new HashSet<String>();
		HashSet<String> answerSNRComposition = new HashSet<String>();
		ArrayList<HashSet<String>> answerSNRPositions = new ArrayList<HashSet<String>>();
		for(Entry<SemanticNetRelationship, SemanticNetRelationship> snrMapping: snrMappings.entrySet())
		{
			
			answerSNRs.add(snrMapping.getValue());
			answerSNRPositions.add(snrMapping.getValue().getDestinationPositionAttrsEnumAsSet());
			answerSNRComposition.add(snrMapping.getValue().getAttribute());
			if(!answerSNRTypes.contains(snrMapping.getValue().getAttribute()))
			{
				answerSNRTypes.add(snrMapping.getValue().getAttribute());
			}
			SNRSimilarityRecord snr = new SNRSimilarityRecord(snrMapping.getKey(), snrMapping.getValue());
			answerFitness += snr.getSimilarity();
			if(KBAILogging.metrics)
			{
				String similarities = (Arrays.toString(snr.differences.toArray()));
				String differences = (Arrays.toString(snr.similarities.toArray()));
				String[] data = new String[]{"match",
						sn.getName(),
						snrMapping.getKey().getSourceStateName(), 
						snrMapping.getKey().getSourceNodeName(),
						snrMapping.getValue().getDestinationStateName(), 
						snrMapping.getValue().getDestinationNodeName(),
						similarities, 
						differences};
				KBAILogging.updateLog("answer fitness", data);
			}
		}

		for(Entry<String, HashSet<String>> constraint: constraints.entrySet())
		{
			switch (constraint.getKey()) {
			case "transNumRequirements":
				if(constraint.getValue().contains(String.valueOf(answerSNRs.size())))
				{
					answerFitness += 1;
				}
				else
				{
					answerFitness -= 1;
				}
				break;
			case "transTypeRequirements": 
				if(answerSNRTypes.containsAll(constraint.getValue()))
				{
					answerFitness += 1;
				}
				else
				{
					answerFitness -= 1;
				}
				break;
			case "transCompositionRequirements":
				if(answerSNRComposition.containsAll(constraint.getValue()))
				{
					answerFitness += 1;
				}
				else
				{
					answerFitness -= 1;
				}
				break;
			case "transCompositionPrediction":
				if(answerSNRComposition.containsAll(constraint.getValue()))
				{
					answerFitness += 1;
				}
				else
				{
					answerFitness -= 1;
				}
				break;
			case "transPositionRequirements":        
				boolean answerSubsumesPositionIntersection = true;
				for(SemanticNetRelationship asnr : answerSNRs)
				{
					if(!asnr.getDestinationPositionAttrsEnumAsSet().containsAll(constraint.getValue()))
					{
						answerSubsumesPositionIntersection = false;
					}
				}
				if(answerSubsumesPositionIntersection)
				{
					answerFitness += 1;
				}
				else
				{
					answerFitness -= 1;
				}
				break;
			default:
				break;
			}
		}
		
		answerCandidateFitness.put(figureName, answerFitness);

		if(KBAILogging.metrics)
		{
			String[] data = new String[]{"fitness",
					sn.getName(),
					figureName, 
					String.valueOf(answerFitness)};
			KBAILogging.updateLog("answer fitness", data);
		}
	}
		
	private void calculateAnswerFitness(
			HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings, HashMap<String, Integer> answerCandidateFitness, String figureName) {
		int answerFitness = 0;
		for(Entry<SemanticNetRelationship, SemanticNetRelationship> snrMapping: snrMappings.entrySet())
		{
			SNRSimilarityRecord snr = new SNRSimilarityRecord(snrMapping.getKey(), snrMapping.getValue());
			answerFitness += snr.getSimilarity();
			if(KBAILogging.metrics)
			{
				String similarities = (Arrays.toString(snr.differences.toArray()));
				String differences = (Arrays.toString(snr.similarities.toArray()));
				String[] data = new String[]{"match",
						sn.getName(),
						snrMapping.getKey().getSourceStateName(), 
						snrMapping.getKey().getSourceNodeName(),
						snrMapping.getValue().getDestinationStateName(), 
						snrMapping.getValue().getDestinationNodeName(),
						similarities, 
						differences};
				KBAILogging.updateLog("answer fitness", data);
			}
		}

		answerCandidateFitness.put(figureName, answerFitness);

		if(KBAILogging.metrics)
		{
			String[] data = new String[]{"fitness",
					sn.getName(),
					figureName, 
					String.valueOf(answerFitness)};
			KBAILogging.updateLog("answer fitness", data);
		}
		
	}
	private int selectAnswerByMaxFitness(ArrayList<SemanticNetState> answerCandidates, HashMap<String, Integer> answerCandidateFitness) {
		int answerCandidate = -1;
		if(answerCandidates.size() != 1)
		{
			int maxFitnessValue = -99;
			int maxFitnessKey = -1;
			for(Entry<String,Integer> e : answerCandidateFitness.entrySet())
			{
				if(maxFitnessValue < e.getValue())
				{
					maxFitnessValue = e.getValue();
					maxFitnessKey = Integer.parseInt(e.getKey());
				}
			}
			answerCandidates = new ArrayList<SemanticNetState>(answerCandidates.subList(maxFitnessKey-1, maxFitnessKey));
		}

		if(answerCandidates.size() == 1)
		{
			answerCandidate = Integer.parseInt(answerCandidates.get(0).getName());
		}
		return answerCandidate;
	}
}

//Construct a list of attributes that are transformed in current answerState...
//for(Entry<String, SemanticNetRelationship> answerTrans : answerTransSet.getValue().entrySet())
//{
//assertedTransformations.clear();
//assertedTransformations.add(answerTrans.getValue().getAttribute());


//for every transformation, pair up the node transformed in QA with the most similar node in QQ...
/*for(Entry<String, SemanticNetRelationship> answerTrans : answerTransSet.getValue().entrySet()) 
				{
					answerTrans.getValue().
				}*/

//individual transformations associated with an answerState
/*for(Entry<String, SemanticNetRelationship> answerTrans : answerTransSet.getValue().entrySet()) 
				{
					answerTrans.getValue().
				}*/

//String[] answerTransSpecArr = answerTrans.getValue().getTransformationSpecification().split("_");

//may need to apply sequencing logic here so that we know where in the sequence the comparison is being made...
//For now we assume all the transformations are associated with state "A"...

//individual transformations associated with the sequence of the final questionState - 
//this will need to be revised to consider more than just "A" state transformations.
/*for(Entry<String, SemanticNetRelationship> questionTrans : sn.getTransformationsByState().get("A").entrySet())
					{
						//int curTransSimilarity = 0;
						String[] patternTransSpecArr = questionTrans.getValue().getTransformationSpecification().split("_");
						//first check to see if the same transformation category is applied...

						//Should break down these things in sets, and then find differences and similarities...
						if(possibleTransformations.contains(answerTransSpecArr[0]))
						{

								if(patternTransSpecArr[0].equals(answerTransSpecArr[0]))
								{
								    Set<String> answerTransSpecSet = new HashSet<String>(Arrays.asList(answerTransSpecArr));
								    Set<String> patternTransSpecSet = new HashSet<String>(Arrays.asList(patternTransSpecArr));
								   // Set<String> similaritiesTransSpecSet = new HashSet<String>(patternTransSpecSet);
								    //Set<String> differencesTransSpecSet = new HashSet<String>(patternTransSpecSet);
								    Set<String> similaritiesTransSpecSet = Utilities.intersection(answerTransSpecSet, patternTransSpecSet);
								    Set<String> differencesTransSpecSet = Utilities.difference(answerTransSpecSet, patternTransSpecSet);
								    //similaritiesTransSpecSet.retainAll(answerTransSpecSet);
								    //differencesTransSpecSet.removeAll(answerTransSpecSet);
								    answerFitness += similaritiesTransSpecSet.size() - differencesTransSpecSet.size();

								    if(KBAILogging.metrics)
								    {
								    	String similarities = (Arrays.toString(similaritiesTransSpecSet.toArray()));
								    	String differences = (Arrays.toString(differencesTransSpecSet.toArray()));
								    	String[] data = new String[]{"match",
								    			sn.getName(),
								    			answerTrans.getValue().getSourceStateName(), 
								    			answerTrans.getValue().getSourceNodeName(),
								    			answerTrans.getValue().getDestinationStateName(), 
								    			answerTrans.getValue().getDestinationNodeName(),
								    			similarities, 
								    			differences};
								    	KBAILogging.updateLog("answer fitness", data);
								    }
									/*for(int i = 0; i < patternTransSpec.length; i++)
									{
										if(i < answerTransSpec.length)
										{

										}
									}*/
//}
//}
/*}
						else
						{
							answerFitness -= answerTransSpecArr.length;
						    if(KBAILogging.metrics)
						    {
						    	String differences = (Arrays.toString(answerTransSpecArr));
						    	String[] data = new String[]{"!match",
						    			sn.getName(),
						    			answerTrans.getValue().getSourceStateName(), 
						    			answerTrans.getValue().getSourceNodeName(),
						    			answerTrans.getValue().getDestinationStateName(), 
						    			answerTrans.getValue().getDestinationNodeName(),
						    			differences};
						    	KBAILogging.updateLog("answer fitness", data);
						    }
						}*/


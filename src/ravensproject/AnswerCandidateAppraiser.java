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
		if(sn.getTransformationsByState().get("B").size() == 0 && sn.getTransformationsByState().get("E").size() == 0) //no observed transformations!!
		{
			answerCandidates = nullTransformationPrune(answerCandidates);
		}
		else
		{

			//ArrayList<ArrayList<SemanticNetRelationship>> possibleTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
			//ArrayList<ArrayList<SemanticNetRelationship>> assertedTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
			HashSet<SemanticNetRelationship> priorTrans_1 = new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("A").values());
			HashSet<SemanticNetRelationship> postTrans_1= new HashSet<SemanticNetRelationship>(sn.getTransformationsByState().get("B").values());

			//HashSet<SemanticNetRelationship> aSnrSet = new HashSet<SemanticNetRelationship>(answerTransSet.getValue().values());
			//HashMap<SemanticNetRelationship, SemanticNetRelationship> snrMappings = SemanticNetRelationship.getSnrMappingsFromSets(qSnrSet, aSnrSet);

			ProgressionConcept pc = pr.defineProgressionConcept(priorTrans_1, postTrans_1);
			
			//pr.defineProgressionConcept(SemanticNetRelationship.getSnrMappingsFromSets(priorTrans_1, postTrans_1));
			
			ArrayList<SemanticNetRelationship> priorTrans_2 = new ArrayList<SemanticNetRelationship>(sn.getTransformationsByState().get("D").values());
			ArrayList<SemanticNetRelationship> postTrans_2 = new ArrayList<SemanticNetRelationship>(sn.getTransformationsByState().get("E").values());
			ArrayList<SemanticNetRelationship> priorTrans_3 = new ArrayList<SemanticNetRelationship>(sn.getTransformationsByState().get("G").values());
			ArrayList<SemanticNetRelationship> possibleTransformations = new ArrayList<SemanticNetRelationship>();
			ArrayList<SemanticNetRelationship> assertedTransformations = new ArrayList<SemanticNetRelationship>();
			
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
			//loop through question rows...
			
			//!!!!!!!
			//PATTERNS TO VERIFY!!!
			//ROW SUBSUMPTION (apply difference to answer 3)
			//ROW SHUFFLING
			//APPLY SUBSUMED TRANSFORMATIONS FIRST
			//APPLY META PATTERN BEHAVIOR LATER
			//
			//...
			//!!!!!!!
			
			//fetch transformations...
			/*possibleTransformations.addAll(s1.getDestinationRelationships().values());
			for(int i = 1; i < sn.getRows()-1; i++)
			{
				//loop through (sorted) question row states...
				ArrayList<SemanticNetState> rowStates = new ArrayList<SemanticNetState>( sn.getQuestionStatesByRow(i));
				
					//loop through transformations in a state...
					for(SemanticNetState s1 : rowStates)
					{
							
					
							//formerly used strings, now we store the entire set of relationships.
							//add each set of dest relationships to PT
					}
					//add each PT to PTS for pattern comparisons...
					//generalize prior patterns...
					//align sequences of patterns if relevant...
					//determine subsumption...
					possibleTransformationsSequence.add(possibleTransformations);
			}*/

			//feed forward comparison for sequencing and subsumption...
			
			/*
			c1 - ma: nothing changes, mi: nothing changes
			c2 - ma: 1 square !inside size increments _1, mi: 1 entity increments +1
			c3 - ma: !1 entity created
						!2 entities created
							either double transforamtion presence
								or create first state in series
									or row_const_presence entities created...
				 mi: 3 entities created...
			c4 - ma: const circles created, all overlap, mi: 3 circles created
			c5 - ma: 1 circle created, mi: circle created
			c6 - ma: const width increment 1, 1 square, mi: width increment
			c7 - ma: 1 circle displacement right, mi: same
			c8 - ma: 2 transformations, right2left row_const_type mi: 2 fills
			c9 - ma: 2 displacements (L->R or L->L?) (if overlaps?), mi: 2 displacements...
			c10 - ma: create overlap & leftof -> break new overlaps... mi: break new overlaps
			c11 - ma: create 1 diamond left-of !above mi: create diamond
			c12 - ma: fill increase max, const above, left of increasing 
				  mi: apply fill... (no transformation possible here!!)*/

			/*for(int i = 1; i < sn.getRows()-2; i++)
			{
				//loop through (sorted) question row states...
				ArrayList<SemanticNetState> rowStates = (ArrayList<SemanticNetState>) sn.getQuestionStatesByRow(i);
				ArrayList<SemanticNetState> nextRowStates = (ArrayList<SemanticNetState>) sn.getQuestionStatesByRow(i+1);
				
					//loop through transformations in a state...
					for(SemanticNetState s1 : rowStates)
					{
						int analogousFitness = 0;
						for(SemanticNetState s2: nextRowStates)
						{
							
						}
							//attempt to find corresponding state in next row...
					}
					//add each PT to PTS for pattern comparisons...
					//generalize prior patterns...
					//align sequences of patterns if relevant...
					//determine subsumption...
					possibleTransformationsSequence.add(possibleTransformations);
			}*/
			
			/*for(int i = 1; i < sn.getRows()-2; i++)
			{
				//loop through (sorted) question row states...
				ArrayList<SemanticNetState> rowStates = (ArrayList<SemanticNetState>) sn.getQuestionStatesByRow(i);
				ArrayList<SemanticNetState> nextRowStates = (ArrayList<SemanticNetState>) sn.getQuestionStatesByRow(i+1);
				
					//loop through transformations in a state...
					for(SemanticNetState s1 : rowStates)
					{
						int analogousFitness = 0;
						for(SemanticNetState s2: nextRowStates)
						{
							
						}
							//attempt to find corresponding state in next row...
					}
					//add each PT to PTS for pattern comparisons...
					//generalize prior patterns...
					//align sequences of patterns if relevant...
					//determine subsumption...
					possibleTransformationsSequence.add(possibleTransformations);
			}*/
			

			
			/*for(ArrayList<SemanticNetRelationship> snrSequence : possibleTransformationsSequence)
			{
				if(possibleTransformationsSequence.indexOf(snrSequence) != possibleTransformationsSequence.size()-1)
				{
					ArrayList<SemanticNetRelationship>
					for(SemanticNetRelationship snr : snrSequence)
					{
					//loop through (sorted) question row states...
					ArrayList<SemanticNetState> rowStates = (ArrayList<SemanticNetState>) sn.getQuestionStatesByRow(i);
						//loop through transformations in a state...
						for(SemanticNetState s : rowStates)
						{
								//formerly used strings, now we store the entire set of relationships.
								//add each set of dest relationships to PT
								possibleTransformations.addAll(s.getDestinationRelationships().values());
						}
						//add each PT to PTS for pattern comparisons...
						//generalize prior patterns...
						//align sequences of patterns if relevant...
						//determine subsumption...
						possibleTransformationsSequence.add(possibleTransformations);
					}
				}
			}*/
		
		
			for(Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet : sn.getTransformationsQA().entrySet()) 
			{
				int answerFitness = 0;
				
				//Construct a list of attributes that are transformed in current answerState...
				for(Entry<String, SemanticNetRelationship> answerTrans : answerTransSet.getValue().entrySet())
				{
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
					
				String[] answerTransSpecArr = answerTrans.getValue().getTransformationSpecification().split("_");
				
				//may need to apply sequencing logic here so that we know where in the sequence the comparison is being made...
				//For now we assume all the transformations are associated with state "A"...
				
				//individual transformations associated with the sequence of the final questionState - 
					//this will need to be revised to consider more than just "A" state transformations.
					for(Entry<String, SemanticNetRelationship> questionTrans : sn.getTransformationsByState().get("A").entrySet())
					{
						//int curTransSimilarity = 0;
						String[] patternTransSpecArr = questionTrans.getValue().getTransformationSpecification().split("_");
						//first check to see if the same transformation category is applied...
						
						//Should break down these things in sets, and then find differences and similarities...
						if(possibleTransformations.contains(answerTransSpecArr[0]))
						{
							//if(Collections.frequency(assertedTransformations, answerTransSpecArr[0]) == Collections.frequency(possibleTransformations, answerTransSpecArr[0]))
							//{
								if(patternTransSpecArr[0].equals(answerTransSpecArr[0]))
								{
								    Set<String> answerTransSpecSet = new HashSet<String>(Arrays.asList(answerTransSpecArr));
								    Set<String> patternTransSpecSet = new HashSet<String>(Arrays.asList(patternTransSpecArr));
								    Set<String> similaritiesTransSpecSet = new HashSet<String>(patternTransSpecSet);
								    Set<String> differencesTransSpecSet = new HashSet<String>(patternTransSpecSet);
								    similaritiesTransSpecSet.retainAll(answerTransSpecSet);
								    differencesTransSpecSet.removeAll(answerTransSpecSet);
								    answerFitness += similaritiesTransSpecSet.size() - differencesTransSpecSet.size();
									/*for(int i = 0; i < patternTransSpec.length; i++)
									{
										if(i < answerTransSpec.length)
										{
											
										}
									}*/
								}
							//}
						}
						else
						{
							answerFitness -= answerTransSpecArr.length;
						}
					}				
				}
				answerCandidateFitness.put(answerTransSet.getKey(), answerFitness);
			}

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

			if(answerCandidates.size() == 1)
			{
				answerCandidate = Integer.parseInt(answerCandidates.get(0).getName());
			}
		}
		return answerCandidate;
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
				
				answerCandidateFitness.put(answerTransSet.getKey(), answerFitness);
				
			    if(KBAILogging.metrics)
			    {
			    	String[] data = new String[]{"fitness",
			    			sn.getName(),
			    			answerTransSet.getKey(), 
			    			String.valueOf(answerFitness)};
			    	KBAILogging.updateLog("answer fitness", data);
			    }

				}				
			}
		
	
		

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


package kbai;

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
			answerCandidate = this.appraiseAnswers_NxN();
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
	private int appraiseAnswers_NxN()
	{
		int answerCandidate = -1;
		int rows = sn.getRows();
		int columns = sn.getColumns();
		
		ArrayList<ArrayList<SemanticNetRelationship>> possibleTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
		ArrayList<ArrayList<SemanticNetRelationship>> assertedTransformationsSequence = new ArrayList<ArrayList<SemanticNetRelationship>>();
		ArrayList<SemanticNetRelationship> possibleTransformations = new ArrayList<SemanticNetRelationship>();
		ArrayList<SemanticNetRelationship> assertedTransformations = new ArrayList<SemanticNetRelationship>();

		ArrayList<SemanticNetState> answerCandidates = new ArrayList<SemanticNetState>(sn.getAnswerStates());
		HashMap<String, Integer> answerCandidateFitness = new HashMap<String, Integer>();
		
		//change this pattern to check sequence size...
		if(sn.getTransformationsByState().get("A").size() == 0) //no observed transformations!!
		{
			answerCandidates = nullTransformationPrune(answerCandidates);
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
			//loop through question rows...
			
			//!!!!!!!
			//PATTERNS TO VERIFY!!!
			//ROW SUBSUMPTION (apply difference to answer 3)
			//ROW SHUFFLING
			//APPLY SUBSUMED TRANSFORMATIONS FIRST
			//APPLY META PATTERN BEHAVIOR LATER
			//...
			//!!!!!!!
			
			for(int i = 1; i < sn.getRows()-1; i++)
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
		
			//use pattern r
		
		
			for(Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet : sn.getTransformationsQA().entrySet()) 
			{
				int answerFitness = 0;
				
				//Construct a list of attributes that are transformed in current answerState...
				for(Entry<String, SemanticNetRelationship> answerTrans : answerTransSet.getValue().entrySet())
				{
					assertedTransformations.add(answerTrans.getValue().getAttribute());
				

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
			ArrayList<String> possibleTransformations = new ArrayList<String>();
			ArrayList<String> assertedTransformations = new ArrayList<String>();
			for(Entry<String, SemanticNetRelationship> questionTrans : sn.getTransformationsByState().get("A").entrySet())
			{
				possibleTransformations.add(questionTrans.getValue().getAttribute());
			}
			for(Entry<String, HashMap<String, SemanticNetRelationship>> answerTransSet : sn.getTransformationsQA().entrySet()) 
			{
				int answerFitness = 0;
				
				//Construct a list of attributes that are transformed in current answerState...
				for(Entry<String, SemanticNetRelationship> answerTrans : answerTransSet.getValue().entrySet())
				{
					assertedTransformations.add(answerTrans.getValue().getAttribute());
				

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
		}
		if(answerCandidates.size() == 1)
		{
			answerCandidate = Integer.parseInt(answerCandidates.get(0).getName());
		}
		return answerCandidate;
	}
}

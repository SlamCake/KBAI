package kbai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ravensproject.RavensObject;

public class RelationshipReasoner {

	 static HashMap<String, SemanticNetRelationship> reasonRelationshipsWithinStates(HashMap<String, SemanticNetNode> nodes) {
		HashMap<String, SemanticNetRelationship> stateRelationships = new HashMap<String, SemanticNetRelationship>();
		CombinationIterator ci = new CombinationIterator(new ArrayList(nodes.values()), 2);
		
		//Loop through combinations of pairs of nodes within the state
    	while (ci.hasNext())
	    {
    		ArrayList<SemanticNetNode> pair = (ArrayList<SemanticNetNode>) ci.next();
    		
    		//loop through each node in the pair
        	for (int i = 0; i < pair.size(); i++)
    		{

	    		//loop through the nodes in the pair and process all but the ith node
	    		for (int j = 0; i < pair.size(); j++)
	    		{
	    			if(j!=i)
	    			{
	    				stateRelationships.putAll(compareNodes(pair.get(i), pair.get(j)));
	    			}
	    		}
        		// Were getting each unique pair so we want to get both directions of relationships between nodes...
    			Iterator<String> ai = pair.get(i).getAttributes().keySet().iterator();
    			
    			//loop through each attribute of the ith node
    	    	while (ai.hasNext())
    		    {
    	    		String attrName = ai.next();
    	    		
    	    		
    		    }
    		}
	    }
		return stateRelationships;
	}

	 //This function only instantiates relationships from node1 to node 2, relationships the other way are handled by
	 //another call to compareNodes in the calling function.
	private static HashMap<String, SemanticNetRelationship> compareNodes(SemanticNetNode node1, SemanticNetNode node2) {
		String attr;
		HashMap<String, SemanticNetRelationship> pairedNodeRelationships = new HashMap<String, SemanticNetRelationship>();
		Iterator<String> a1i = node1.getAttributes().keySet().iterator();
		int i = 0;
		while(a1i.hasNext())
		{
			attr = a1i.next();
			//Check attributes to get insights on what kind of relationship might be reasoned, consider passing a nominal
			//value to reasonRelationship()!
			
			//Both nodes have same attribute
			if(node2.getAttributes().containsKey(attr))
			{
				//There is a value difference, else the values are the same...
				if(!node2.getAttributes().get(attr).equals(node1.getAttributes().get(attr)))
				{
					pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr, reasonRelationship(node1, node2, attr));
					//pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr, reasonRelationship(node2, node1, attr));
				}
				else
				{
					//Do nothing for now
				}
			}
		
			// One node refers to the other
			if(node2.getAttributes().containsValue(node1.getName()))
			{
				pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr, reasonRelationship(node1, node2, attr));
				//pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr, reasonRelationship(node2, node1, attr));
			}
		}
		return pairedNodeRelationships;
	}


	//embedded in the mentioning of the attribute keyword in pruning directives (first word) 
	//is that the value of the attribute changes.
	private static SemanticNetRelationship reasonRelationship(SemanticNetNode node1, SemanticNetNode node2, String attr) 
	{

		/*
		shape:right triangle
		fill:yes
		size:huge
		angle:90
		above:j
		*/
		
		//Node B 06 is a reflection, not a rotation...
		//Cur value - 180 for y axis and cur value - 360 for x axis and cur value +180 for x&y axis
		
		
		//first try to look up the relationship...
		
		
		
		//...then try to reason it (determine if it fits into known relationships)
		
		
		//...then create a case for it..
		//shape: square, pentagon, triangle, pac-man, star, heart, circle, plus, right triangle, octagon, diamond, rectangle,
		//size: very large, small, huge, medium, very small, large,
		//width: large, huge, small,
		//angle: 0, 45, 270, 180, 135, 315, 225, 90,
		//fill: yes, no, right-half, left-half, top-half, bottom-half,
		//alignment: bottom-right, bottom-left, top-right, top-left,
		//height: small, large, huge,
		//left-of
		//above
		//overlaps
		//inside
		
		
		SemanticNetRelationship snr = new SemanticNetRelationship();
		
		snr.setSourceNodeName(node1.getName());
		snr.setDestinationNodeName(node2.getName());
		String value1 = node1.getAttributes().get(attr);
		String value2 = node1.getAttributes().get(attr);
		snr.setSourceNodeValue(value1);
		snr.setDestinationNodeValue(value2);
		String pruningDirective = "noop";
		
		snr.setAttribute(attr);
		
		//If statements establish pruning directives considering only node1 and node2.
		//Pruning directives based on more general (holistic?) criteria may need to be established.
		
		if(attr.equals("shape"))
		{
			pruningDirective = "morph";
		}
		
		if(attr.equals("size")||attr.equals("width")||attr.equals("height"))
		{
			int sourceSize = (int) KnowledgeBase.attributeMap.get(attr).get(value1);
			int destSize = (int) KnowledgeBase.attributeMap.get(attr).get(value2);
			int difference = destSize - sourceSize;
			//growth
			if(difference > 1)
			{
				//snr.getPruningDirectives().add("size_increase");
				pruningDirective = attr+"_increase_"+Integer.toString(difference);
			}
			else // shrink
			{
				//snr.getPruningDirectives().add("size_increase");
				pruningDirective = attr+"_!increase_"+Integer.toString(difference);
			}
		}

		
		/*
		 * 11 - 10 =  0-1	...		10 - 11 = 01
		 * 00	00	  00			00	 00   00	
		 * 
		 * 
		 */
		if(attr.equals("fill"))
		{

			pruningDirective = "fill";
			
			int[][] sourceFill = (int[][]) KnowledgeBase.attributeMap.get(attr).get(value1);
			int[][] destFill = (int[][]) KnowledgeBase.attributeMap.get(attr).get(value2);
			int[][] difference = {{destFill[0][0]-sourceFill[0][0], destFill[0][1]-sourceFill[0][1]},
									{destFill[1][0]-sourceFill[1][0], destFill[1][1]-sourceFill[1][1]}};
			
			//test for flip or displacement
			//test for deletion or expansion
			int zeroPresence = 0;
			int positivePresence = 0;
			int negativePresence = 0;
			for(int i = 0; i < difference.length; i++)
			{
				for(int j = 0; j < difference[i].length; j++)
				{
					if(difference[i][j] == 0)
					{
						zeroPresence++;
					}
					else if(difference[i][j] == 1)
					{
						positivePresence++;
					}
					else
					{
						negativePresence++;
					}
				}
			}
			if(positivePresence == 4)
			{
				pruningDirective = "fill_increase_max";
			}
			else if(negativePresence == 4)
			{
				pruningDirective = "fill_!increase_max";
			}
			else
			{
				if(positivePresence > 0)
				{
					//pruningDirective = "fill_present";
					if(negativePresence > 0)
					{
						pruningDirective = "fill_present_changed";
						if(positivePresence == 2 && negativePresence == 2)
						{
							pruningDirective = "fill_flip";
						}
						else if(zeroPresence == 0)
						{
							pruningDirective = "fill_invert";
						}
						else if(positivePresence > negativePresence)
						{
							pruningDirective = "fill_increase_!max";
						}
						else if(positivePresence < negativePresence)
						{
							pruningDirective = "fill_!increase_!max";
						}
					}
					else
					{
						pruningDirective = "fill_increase_!max";
					}
				}
				else if (negativePresence > 0)
				{
					pruningDirective = "fill_!increase_!max";
				}
				
			}
		}
		
		if(attr.equals("alignment"))
		{
			int[] sourceSize = (int[]) KnowledgeBase.attributeMap.get(attr).get(value1);
			int[] destSize = (int[]) KnowledgeBase.attributeMap.get(attr).get(value2);
			int[] difference = {destSize[0] - sourceSize[0], destSize[1] - sourceSize[1]};
			
			//test for direction of shift
			//test for one or both directions
			if(difference[0] == 1 && difference[1] == 1)
			{
				pruningDirective = "alignment_right&up";
			}
			else if(difference[0] == 1 && difference[1] == -1)
			{
				pruningDirective = "alignment_right&!up";
				
			}
			else if(difference[0] == -1 && difference[1] == 1)
			{
				pruningDirective = "alignment_!right&up";
			}
			else if(difference[0] == -1 && difference[1] == 1)
			{
				pruningDirective = "alignment_!right&!up";
			}
			else if(difference[0] == 1 && difference[1] == 0)
			{
				pruningDirective = "alignment_right";
			}
			else if(difference[0] == -1 && difference[1] == 0)
			{
				pruningDirective = "alignment_!right";
			}
			else if(difference[0] == 0 && difference[1] == 1)
			{
				pruningDirective = "alignment_up";
			}
			else if(difference[0] == 0 && difference[1] == -1)
			{
				pruningDirective = "alignment_!up";
			}
		}
		
		snr.setPruningDirective(pruningDirective);
		return snr;
	}

	public static HashMap<String, HashMap<String, SemanticNetRelationship>> reasonTransformationRelationships(List<SemanticNetState> states) 
	{
		
		//Find node mappings between states. Loop through nodes in each state and estimate which nodes map
		HashMap<String, HashMap<SemanticNetNode, SemanticNetNode>> transformationNodeMappingsByState = new HashMap<String, HashMap<SemanticNetNode, SemanticNetNode>>();
		HashMap<String, HashMap<String, SemanticNetRelationship>> transformationRelationshipsByState = new HashMap<String, HashMap<String, SemanticNetRelationship>>();
		//loop through all states in the row except for the final state.
		for(int i = 0; i < states.size()-1; i++)
		{
			HashMap<SemanticNetNode, SemanticNetNode> transformationNodeMappings = new HashMap<SemanticNetNode, SemanticNetNode>();
			for(SemanticNetNode n : states.get(i).getNodes().values())
			{
				//mapTransformationNodes compares 'n' with all nodes in the i+1 state.
				transformationNodeMappings.put(n, mapTransformationNodes(n,states.get(i+1)));
			}
			transformationNodeMappingsByState.put(states.get(i).getName(), transformationNodeMappings);
		}
		
		
		//For each state, reason relationships between each mapping...
		
		for(Entry<String, HashMap<SemanticNetNode, SemanticNetNode>> e : transformationNodeMappingsByState.entrySet())
		{
			HashMap<String, SemanticNetRelationship> transformationRelationships = new HashMap<String, SemanticNetRelationship>();
			for(SemanticNetNode n: e.getValue().keySet())
			{
				transformationRelationships.putAll(compareNodes(n, e.getValue().get(n)));
			}
			transformationRelationshipsByState.put(e.getKey(), transformationRelationships);
		}
		
		return transformationRelationshipsByState;
	}
	

	public static HashMap<String, SemanticNetRelationship> reasonTransformationRelationships(SemanticNetState s1, SemanticNetState s2) 
	{
		
		//Find node mappings between states. Loop through nodes in each state and estimate which nodes map
		HashMap<SemanticNetNode, SemanticNetNode> transformationNodeMappings = new HashMap<SemanticNetNode, SemanticNetNode>();
		HashMap<String, SemanticNetRelationship> transformationRelationships = new HashMap<String, SemanticNetRelationship>();
		//loop through all states in the row except for the final state.
			for(SemanticNetNode n : s1.getNodes().values())
			{
				//mapTransformationNodes compares 'n' with all nodes in the i+1 state.
				transformationNodeMappings.put(n, mapTransformationNodes(n,s2));
			}
		
		
		//For each state, reason relationships between each mapping...
		
		for(Entry<SemanticNetNode, SemanticNetNode> e : transformationNodeMappings.entrySet())
		{
			transformationRelationships.putAll((compareNodes(e.getKey(), e.getValue())));
		}
		
		return transformationRelationships;
	}

	
	private static SemanticNetNode mapTransformationNodes(SemanticNetNode n1,	SemanticNetState semanticNetState) 
	{
		HashMap<String, Integer> similarity = new HashMap<String, Integer>();
		
		//Compare n1 to every node in semanticNetState, associate a similarity value with each node
		for(SemanticNetNode n2: semanticNetState.getNodes().values())
		{
			//consider attribute, value, and relationships to intra nodes (these should reinforce eachother)
			Iterator<String> attrs1 = n2.getAttributes().keySet().iterator();
			int similarityValue = 0;
			while(attrs1.hasNext())
			{
				String attr = attrs1.next();
				
				//Both nodes have same attribute
				if(n1.getAttributes().containsKey(attr))
				{
					//+1 to similarity, this should be made more sophisticated later...
					similarityValue = similarityValue + 1;

					// same attribute has the same value
					if(n1.getAttributes().get(attr).equals(n2.getAttributes().get(attr)))
					{
						//+1 to similarity, this should be made more sophisticated later...
						similarityValue = similarityValue + 1;
						//pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr, reasonRelationship(node1, node2, attr));
						//pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr, reasonRelationship(node2, node1, attr));
					}
					//pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr, reasonRelationship(node1, node2, attr));
					//pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr, reasonRelationship(node2, node1, attr));
				}
			
			}
			//consider meta reasoning too i.e. problems D-12
			
			//consider planar subtraction to isolate what changes between figures.
			similarity.put(n2.getName(), similarityValue);
		}
		
		//Find node with highest similarity value
		ArrayList<SemanticNetNode> maxSimilarityNodes = new ArrayList<SemanticNetNode>();
		//SemanticNetNode maxSimilarityNode = null;
		int maxSimilarity = 0;
		
		//Fetches highest similarity value to n1...
		for(SemanticNetNode n2: semanticNetState.getNodes().values())
		{
			if(similarity.get(n2.getName()) > maxSimilarity)
			{ 
				maxSimilarity = similarity.get(n2.getName());
				//maxSimilarityNode = n2;
			}
		}
		

		//Find other nodes with similarity == maxSimilarity
		for(SemanticNetNode n2: semanticNetState.getNodes().values())
		{
			if(similarity.get(n2.getName()) == maxSimilarity)
			{ 
				maxSimilarityNodes.add(n2);
			}
		}
		
		SemanticNetNode finalCandidateNode = null;
		//If there is more than one similar node, identify the final candidate
		if(maxSimilarityNodes.size() != 1)
		{
			
		}
		else
		{
			finalCandidateNode = maxSimilarityNodes.get(0);
		}
		
		return finalCandidateNode;
		
	}
}

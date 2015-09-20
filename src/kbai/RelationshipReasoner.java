package kbai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import ravensproject.RavensObject;

public class RelationshipReasoner {
	private HashMap<String, SemanticNetRelationship> pairedNodeRelationships = new HashMap<String, SemanticNetRelationship>();
	private HashMap<SemanticNetNode, NodeSimilarityRecord> NSRMap = new HashMap<SemanticNetNode, NodeSimilarityRecord>();
	private SemanticNetwork sn;
	
	public RelationshipReasoner(SemanticNetwork sn)
	{
		this.sn = sn;
	}
	/*
	 * static HashMap<String, SemanticNetRelationship>
	 * reasonRelationshipsWithinStates(HashMap<String, SemanticNetNode> nodes) {
	 * HashMap<String, SemanticNetRelationship> stateRelationships = new
	 * HashMap<String, SemanticNetRelationship>(); CombinationIterator ci = new
	 * CombinationIterator(new ArrayList(nodes.values()), 2);
	 * 
	 * //Loop through combinations of pairs of nodes within the state while
	 * (ci.hasNext()) { ArrayList<SemanticNetNode> pair =
	 * (ArrayList<SemanticNetNode>) ci.next();
	 * 
	 * //loop through each node in the pair for (int i = 0; i < pair.size();
	 * i++) {
	 * 
	 * //loop through the nodes in the pair and process all but the ith node for
	 * (int j = 0; i < pair.size(); j++) { if(j!=i) {
	 * stateRelationships.putAll(compareNodes(pair.get(i), pair.get(j))); } } //
	 * Were getting each unique pair so we want to get both directions of
	 * relationships between nodes... Iterator<String> ai =
	 * pair.get(i).getAttributes().keySet().iterator();
	 * 
	 * //loop through each attribute of the ith node while (ai.hasNext()) {
	 * String attrName = ai.next();
	 * 
	 * 
	 * } } } return stateRelationships; }
	 */

	// This function only instantiates relationships from node1 to node 2,
	// relationships the other way are handled by
	// another call to compareNodes in the calling function.
	private static HashMap<String, SemanticNetRelationship> compareNodes(
			SemanticNetNode node1, SemanticNetNode node2) {
		String attr;
		HashMap<String, SemanticNetRelationship> pairedNodeRelationships = new HashMap<String, SemanticNetRelationship>();

		// store detected node translations

		// store node presences
		//HashMap<String, String> presencePrior = new HashMap<String, String>();
		//HashMap<String, String> resencePosterior = new HashMap<String, String>();

		// store detected node exchanges
		// HashMap<String, String> exchangePresencePrior = new HashMap<String,
		// String>();
		// HashMap<String, String> exchangePresencePosterior = new
		// HashMap<String, String>();

		if(!node2.getType().equals("delete") && !node1.getType().equals("create"))
		{
			Iterator<String> a1i = node1.getAttributes().keySet().iterator();
			int i = 0;
			while (a1i.hasNext()) {
				attr = a1i.next();
				// Check attributes to get insights on what kind of relationship
				// might be reasoned, consider passing a nominal
				// value to reasonRelationship()!

				// Both nodes have same attribute
				if (node2.getAttributes().containsKey(attr)) {
					String n1Value = node1.getAttributes().get(attr);
					String n2Value = node2.getAttributes().get(attr);
					// There is a value difference, else the values are the same...

					// handle attributes that are distinct relative to other node
					// entities differently from nominal or numerical attributes.
					if (!KnowledgeBase.attributeMetaDataMap.get(attr).isRelative()) {
						if (!n2Value.equals(n1Value)) {
							pairedNodeRelationships.put(node1.getName() + ","
									+ node2.getName() + "," + attr,
									reasonRelationship(node1, node2, attr));
							// pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr,
							// reasonRelationship(node2, node1, attr));
						} else {

							// Do nothing for now
						}
					} else // a relative relationship requires testing presence
							// difference rather than value difference.
					{
						// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						// Consider checking for the presence of a DELETE or CREATE
						// prior to this check!!!
						// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

						int n1ValuePresence = n1Value.split(",").length;
						int n2ValuePresence = n2Value.split(",").length;
						// This check cannot verify node position exchanges...only
						// isolated displacements.
						if (n1ValuePresence != n2ValuePresence) {
							//
						}

						// try to determine relationship, if "noop" relationship
						// will not be added...
						// SemanticNetRelationship snr = reasonRelationship(node1,
						// node2, attr);
						// if(!snr.getTransformationSpecification().equals("noop"))
						// {
						// pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr,
						// snr);
						// }
						// }
					}
				}
			}
		}
		else
		{
			if(node2.getType().equals("delete"))
			{
				attr = "delete";
			}
			else
			{
				attr = "create";
			}
			pairedNodeRelationships.put(node1.getName() + ","
					+ "null" + "," + attr,
					reasonRelationship(node1, node2, attr));
		}
		return pairedNodeRelationships;
	}

	// One node refers to the other
	// Currently being handled by isRelative check above...
	/*
	 * if(node2.getAttributes().containsValue(node1.getName())) {
	 * pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr,
	 * reasonRelationship(node1, node2, attr));
	 * //pairedNodeRelationships.put(node2
	 * .getName()+","+node1.getName()+","+attr, reasonRelationship(node2, node1,
	 * attr)); }
	 */

	// embedded in the mentioning of the attribute keyword in pruning directives
	// (first word)
	// is that the value of the attribute changes.
	private static SemanticNetRelationship reasonRelationship(
			SemanticNetNode node1, SemanticNetNode node2, String attr) {

		/*
		 * shape:right triangle fill:yes size:huge angle:90 above:j
		 */

		// Node B 06 is a reflection, not a rotation...
		// Cur value - 180 for y axis and cur value - 360 for x axis and cur
		// value +180 for x&y axis

		// first try to look up the relationship...

		// ...then try to reason it (determine if it fits into known
		// relationships)

		// ...then create a case for it..
		// shape: square, pentagon, triangle, pac-man, star, heart, circle,
		// plus, right triangle, octagon, diamond, rectangle,
		// size: very large, small, huge, medium, very small, large,
		// width: large, huge, small,
		// angle: 0, 45, 270, 180, 135, 315, 225, 90,
		// fill: yes, no, right-half, left-half, top-half, bottom-half,
		// alignment: bottom-right, bottom-left, top-right, top-left,
		// height: small, large, huge,
		// left-of
		// above
		// overlaps
		// inside

		// write special rules for inside, above, overlaps, left-of
		// number-of presence sould be applied first...

		SemanticNetRelationship snr = new SemanticNetRelationship();
		String transformationSpecification = "noop";
		String value1 = "";
		String value2 = "";
		
		if(!attr.equals("create") && !attr.equals("delete"))
		{

			snr.setSourceNodeName(node1.getName());
			snr.setDestinationNodeName(node2.getName());
			//snr.setSourceNode(node1);
			//snr.setDestinationNode(node2);
			value1 = node1.getAttributes().get(attr);
			value2 = node2.getAttributes().get(attr);
			snr.setSourceNodeValue(value1);
			snr.setDestinationNodeValue(value2);

			snr.setAttribute(attr);
		}
		else
		{
			if(attr.equals(("create")))
			{
				snr.setDestinationNodeName(node2.getName());
			}
			else
			{
				snr.setSourceNodeName(node1.getName());
			}
		}

		snr.setAttribute(attr);

		// If statements establish pruning directives considering only node1 and
		// node2.
		// Pruning directives based on more general (holistic?) criteria may
		// need to be established.

		if (attr.equals("left-of")) {
			transformationSpecification = "left-of";
		}
		
		if (attr.equals("above")) {
			transformationSpecification = "above";
		}
		
		if (attr.equals("overlaps")) {
			transformationSpecification = "overlaps";
		}
		
		if (attr.equals("inside")) {
			transformationSpecification = "inside";
		}
		
		if (attr.equals("delete")) {
			transformationSpecification = "delete";
		}
		
		if (attr.equals("create")) {
			transformationSpecification = "create";
		}
		
		if (attr.equals("shape")) {
			transformationSpecification = "shape_"+node2.getAttributes().get(attr);
		}
		
		if (attr.equals("angle")) {
			int v1 = Integer.parseInt(value1);
			int v2 = Integer.parseInt(value2);
			String difference = String.valueOf(v2 - v1);
			if(((v1 + 180)%360) == v2)
			{
				transformationSpecification = "angle_reflection_x&y";
			}
			else if(((180 - v1)+360)%360 == v2)
			{
				transformationSpecification = "angle_reflection_y";
			}
			else if((360 - v1) == v2)
			{
				transformationSpecification = "angle_reflection_x";
			}
			else
			{
				transformationSpecification = "angle_rotation_"+difference;
			}
		}

		if (attr.equals("size") || attr.equals("width")
				|| attr.equals("height")) {
			int sourceSize = (int) KnowledgeBase.attributeMap.get(attr).get(
					value1);
			int destSize = (int) KnowledgeBase.attributeMap.get(attr).get(
					value2);
			int difference = destSize - sourceSize;
			// growth
			if (difference > 1) {
				// snr.gettransformationSpecifications().add("size_increase");
				transformationSpecification = attr + "_increase_"
						+ Integer.toString(difference);
			} else // shrink
			{
				// snr.gettransformationSpecifications().add("size_increase");
				transformationSpecification = attr + "_!increase_"
						+ Integer.toString(difference);
			}
		}

		/*
		 * 11 - 10 = 0-1 ... 10 - 11 = 01 00 00 00 00 00 00
		 */
		if (attr.equals("fill")) {

			transformationSpecification = "fill";

			int[][] sourceFill = (int[][]) KnowledgeBase.attributeMap.get(attr)
					.get(value1);
			int[][] destFill = (int[][]) KnowledgeBase.attributeMap.get(attr)
					.get(value2);
			int[][] difference = {
					{ destFill[0][0] - sourceFill[0][0],
							destFill[0][1] - sourceFill[0][1] },
					{ destFill[1][0] - sourceFill[1][0],
							destFill[1][1] - sourceFill[1][1] } };

			// test for flip or displacement
			// test for deletion or expansion
			int zeroPresence = 0;
			int positivePresence = 0;
			int negativePresence = 0;
			for (int i = 0; i < difference.length; i++) {
				for (int j = 0; j < difference[i].length; j++) {
					if (difference[i][j] == 0) {
						zeroPresence++;
					} else if (difference[i][j] == 1) {
						positivePresence++;
					} else {
						negativePresence++;
					}
				}
			}
			if (positivePresence == 4) {
				transformationSpecification = "fill_increase_max";
			} else if (negativePresence == 4) {
				transformationSpecification = "fill_!increase_max";
			} else {
				if (positivePresence > 0) {
					// transformationSpecification = "fill_present";
					if (negativePresence > 0) {
						transformationSpecification = "fill_present_changed";
						if (positivePresence == 2 && negativePresence == 2) {
							transformationSpecification = "fill_flip";
						} else if (zeroPresence == 0) {
							transformationSpecification = "fill_invert";
						} else if (positivePresence > negativePresence) {
							transformationSpecification = "fill_increase_!max";
						} else if (positivePresence < negativePresence) {
							transformationSpecification = "fill_!increase_!max";
						}
					} else {
						transformationSpecification = "fill_increase_!max";
					}
				} else if (negativePresence > 0) {
					transformationSpecification = "fill_!increase_!max";
				}

			}
		}

		if (attr.equals("alignment")) {
			int[] sourceSize = (int[]) KnowledgeBase.attributeMap.get(attr)
					.get(value1);
			int[] destSize = (int[]) KnowledgeBase.attributeMap.get(attr).get(
					value2);
			int[] difference = { destSize[0] - sourceSize[0],
					destSize[1] - sourceSize[1] };

			// test for direction of shift
			// test for one or both directions
			if (difference[0] == 1 && difference[1] == 1) {
				transformationSpecification = "alignment_right&up";
			} else if (difference[0] == 1 && difference[1] == -1) {
				transformationSpecification = "alignment_right&!up";

			} else if (difference[0] == -1 && difference[1] == 1) {
				transformationSpecification = "alignment_!right&up";
			} else if (difference[0] == -1 && difference[1] == 1) {
				transformationSpecification = "alignment_!right&!up";
			} else if (difference[0] == 1 && difference[1] == 0) {
				transformationSpecification = "alignment_right";
			} else if (difference[0] == -1 && difference[1] == 0) {
				transformationSpecification = "alignment_!right";
			} else if (difference[0] == 0 && difference[1] == 1) {
				transformationSpecification = "alignment_up";
			} else if (difference[0] == 0 && difference[1] == -1) {
				transformationSpecification = "alignment_!up";
			}
		}

		snr.setTransformationSpecification(transformationSpecification);
		return snr;
	}

	/*public static HashMap<String, HashMap<String, SemanticNetRelationship>> reasonTransformationRelationships(
			List<SemanticNetState> states) {

		// Find node mappings between states. Loop through nodes in each state
		// and estimate which nodes map
		HashMap<String, HashMap<SemanticNetNode, SemanticNetNode>> transformationNodeMappingsByState = new HashMap<String, HashMap<SemanticNetNode, SemanticNetNode>>();
		HashMap<String, HashMap<String, SemanticNetRelationship>> transformationRelationshipsByState = new HashMap<String, HashMap<String, SemanticNetRelationship>>();

		// naively gather mappings
		// loop through all states in the row except for the final state.
		for (int i = 0; i < states.size() - 1; i++) {
			HashMap<SemanticNetNode, SemanticNetNode> transformationNodeMappings = new HashMap<SemanticNetNode, SemanticNetNode>();
			int deletions = states.get(i).getNodes().size()
					- states.get(i + 1).getNodes().size();
			int spawns = states.get(i + 1).getNodes().size()
					- states.get(i).getNodes().size();

			/*for (SemanticNetNode n : states.get(i).getNodes().values()) {
				// mapTransformationNodes compares 'n' with all nodes in the i+1
				// state.
				transformationNodeMappings.put(n,
						naiveMapTransformationNodes(n, states.get(i + 1)));

			}*/
	/*
			transformationNodeMappings.putAll(naiveMapTransformationNodes(states.get(i), states.get(i + 1)));

			// loop through deletions -> nodes
			if (deletions > 0) {
				for (int j = 0; i < deletions; i++) {

				}
			} else if (spawns > 0) {

			}

			// if there is a different number of nodes from state to state, a
			// node was created or deleted...

			for (Entry<SemanticNetNode, SemanticNetNode> e : transformationNodeMappings
					.entrySet()) {

			}
			transformationNodeMappingsByState.put(states.get(i).getName(),
					transformationNodeMappings);
		}
		// check mappings,
		// if two nodes in the prior state map to one node in the posterior
		// state, reconcile this...
		// if there are more nodes in the prior state than the posterior state,
		// identify deletion...
		// if there are more nodes in the posterior state than the prior state,
		// identify creation...
		// results can be used to evaluate transformations involving "relative"
		// attributes...

		/*
		 * for(Entry<>SemanticNetNode n : states.get(i).getNodes().values()) {
		 * //mapTransformationNodes compares 'n' with all nodes in the i+1
		 * state. transformationNodeMappings.put(n,
		 * mapTransformationNodes(n,states.get(i+1))); }
		 * 
		 * for(SemanticNetNode n : states.get(i).getNodes().values()) {
		 * //mapTransformationNodes compares 'n' with all nodes in the i+1
		 * state. transformationNodeMappings.put(n,
		 * naiveMapTransformationNodes(n,states.get(i+1))); }
		 */

		// For each state, reason relationships between each mapping...
/*
		for (Entry<String, HashMap<SemanticNetNode, SemanticNetNode>> e : transformationNodeMappingsByState
				.entrySet()) {
			HashMap<String, SemanticNetRelationship> transformationRelationships = new HashMap<String, SemanticNetRelationship>();
			for (SemanticNetNode n : e.getValue().keySet()) {
				transformationRelationships.putAll(compareNodes(n, e.getValue()
						.get(n), transformationNodeMappingsByState.get(e
						.getKey())));
			}
			transformationRelationshipsByState.put(e.getKey(),
					transformationRelationships);
		}

		return transformationRelationshipsByState;
	}*/


	public HashMap<String, SemanticNetRelationship> reasonTransformationRelationships(
			SemanticNetState s1, SemanticNetState s2) {

		// Find node mappings between states. Loop through nodes in each state
		// and estimate which nodes map
		HashMap<SemanticNetNode, SemanticNetNode> transformationNodeMappings = new HashMap<SemanticNetNode, SemanticNetNode>();
		HashMap<String, SemanticNetRelationship> transformationRelationships = new HashMap<String, SemanticNetRelationship>();
		// loop through all states in the row except for the final state.
		/*for (SemanticNetNode n : s1.getNodes().values()) {
			// mapTransformationNodes compares 'n' with all nodes in the i+1
			// state.
			transformationNodeMappings.put(n,
					naiveMapTransformationNodes(n, s2));
		}*/

		transformationNodeMappings.putAll(naiveMapTransformationNodes(s1, s2));

		// For each state, reason relationships between each mapping...

		for (Entry<SemanticNetNode, SemanticNetNode> e : transformationNodeMappings
				.entrySet()) {
			transformationRelationships.putAll((compareNodes(e.getKey(),
					e.getValue())));
		}

		for (SemanticNetRelationship snr : transformationRelationships.values()) {
			snr.setSourceStateName(s1.getName());
			snr.setDestinationStateName(s2.getName());
		}
		s1.setSourceRelationships(transformationRelationships);
		s2.setDestinationRelationships(transformationRelationships);
		return transformationRelationships;
	}

	/*private static SemanticNetNode naiveMapTransformationNodes(
			SemanticNetNode n1, SemanticNetState semanticNetState) {
		HashMap<String, Integer> similarity = new HashMap<String, Integer>();

		// Compare n1 to every node in semanticNetState, associate a similarity
		// value with each node
		for (SemanticNetNode n2 : semanticNetState.getNodes().values()) {
			// consider attribute, value, and relationships to intra nodes
			// (these should reinforce eachother)
			Iterator<String> attrs1 = n2.getAttributes().keySet().iterator();
			int similarityValue = 0;
			while (attrs1.hasNext()) {
				String attr = attrs1.next();

				// Both nodes have same attribute
				if (n1.getAttributes().containsKey(attr)) {
					// +1 to similarity, this should be made more sophisticated
					// later...
					similarityValue = similarityValue + 1;

					// same attribute has the same value
					if (n1.getAttributes().get(attr)
							.equals(n2.getAttributes().get(attr))) {
						// +1 to similarity, this should be made more
						// sophisticated later...
						similarityValue = similarityValue + 1;
						// pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr,
						// reasonRelationship(node1, node2, attr));
						// pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr,
						// reasonRelationship(node2, node1, attr));
					}
					// pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr,
					// reasonRelationship(node1, node2, attr));
					// pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr,
					// reasonRelationship(node2, node1, attr));
				}

			}
			// consider meta reasoning too i.e. problems D-12

			// consider planar subtraction to isolate what changes between
			// figures.
			similarity.put(n2.getName(), similarityValue);
		}

		// Find node with highest similarity value
		ArrayList<SemanticNetNode> maxSimilarityNodes = new ArrayList<SemanticNetNode>();
		// SemanticNetNode maxSimilarityNode = null;
		int maxSimilarity = 0;

		// Fetches highest similarity value to n1...
		for (SemanticNetNode n2 : semanticNetState.getNodes().values()) {
			if (similarity.get(n2.getName()) > maxSimilarity) {
				maxSimilarity = similarity.get(n2.getName());
				// maxSimilarityNode = n2;
			}
		}

		// Find other nodes with similarity == maxSimilarity
		for (SemanticNetNode n2 : semanticNetState.getNodes().values()) {
			if (similarity.get(n2.getName()) == maxSimilarity) {
				maxSimilarityNodes.add(n2);
			}
		}

		SemanticNetNode finalCandidateNode = null;
		// If there is more than one similar node, identify the final candidate
		if (maxSimilarityNodes.size() != 1) {
			// currently choose first, must revise later...
			finalCandidateNode = maxSimilarityNodes.get(0);
		} else {
			finalCandidateNode = maxSimilarityNodes.get(0);
		}

		return finalCandidateNode;

	}*/

	private HashMap<SemanticNetNode, SemanticNetNode> naiveMapTransformationNodes(
			SemanticNetState semanticNetState1,
			SemanticNetState semanticNetState2) {
		HashMap<SemanticNetNode, SemanticNetNode> pairings = new HashMap<SemanticNetNode, SemanticNetNode>();

		// deletions & spawns will need to be defined more rigorously to
		// determine instances where the pattern is
		// the number of entities present rather than a single one or two
		// entities being deleted or spawned.
		// int deletions = semanticNetState1.getNodes().size() -
		// semanticNetState2.getNodes().size();
		// int spawns = semanticNetState2.getNodes().size() -
		// semanticNetState1.getNodes().size();

		//this.NSRMap = new HashMap<SemanticNetNode, NodeSimilarityRecord>();
		this.NSRMap.putAll((calculateSimilarities(semanticNetState1,
				semanticNetState2)));
		pairings.putAll(mapSimilarNodes(semanticNetState1, semanticNetState2,
				this.NSRMap));
		// "delete-type" node pairings
		if (semanticNetState1.getNodes().size() > semanticNetState2.getNodes()
				.size()) {
			pairings = processDeletedNodes(pairings);
		}
		// "create-type" node pairings
		if (semanticNetState1.getNodes().size() < semanticNetState2.getNodes()
				.size()) {
			pairings = processSpawnedNodes(pairings);
		}

		// logic to deal with removals and creations...
		// HashMap<SemanticNetNode, SemanticNetNode> deletions = new
		// HashMap<SemanticNetNode, SemanticNetNode>();
		// HashMap<SemanticNetNode, SemanticNetNode> spawn = new
		// HashMap<SemanticNetNode, SemanticNetNode>();

		/*
		 * if(deletions > 0) { for(int i = 0; i < deletions; i++) {
		 * ArrayList<SemanticNetNode> for(Entry<SemanticNetNode,
		 * SemanticNetNode> e : pairings.entrySet()) {
		 * 
		 * } } }
		 * 
		 * if (spawns > 0) { for(int i = 0; i < spawns; i++) {
		 * 
		 * }
		 * 
		 * }
		 */
		return pairings;
	}

	private static HashMap<SemanticNetNode, SemanticNetNode> mapSimilarNodes(
			SemanticNetState semanticNetState1,
			SemanticNetState semanticNetState2,
			HashMap<SemanticNetNode, NodeSimilarityRecord> NSRMap) {
		// Find node with highest similarity value
		HashMap<SemanticNetNode, SemanticNetNode> pairings = new HashMap<SemanticNetNode, SemanticNetNode>();
		// SemanticNetNode maxSimilarityNode = null;

		for (SemanticNetNode n1 : semanticNetState1.getNodes().values()) {
			// Fetches highest similarity value to n1...
			int maxSimilarity = 0;
			for (SemanticNetNode n2 : semanticNetState2.getNodes().values()) {
				if (NSRMap.get(n1).getSimilarities().get(n2) > maxSimilarity) {
					maxSimilarity = NSRMap.get(n1).getSimilarities().get(n2);
					// maxSimilarityNode = n2;
				}
			}

			ArrayList<SemanticNetNode> maxSimilarityNodes = new ArrayList<SemanticNetNode>();
			
			// Find other nodes with similarity == maxSimilarity
			for (SemanticNetNode n2 : semanticNetState2.getNodes().values()) {
				if (NSRMap.get(n1).getSimilarities().get(n2) == maxSimilarity) {
					maxSimilarityNodes.add(n2);
				}
			}

			SemanticNetNode finalCandidateNode = null;
			// If there is more than one similar node, identify the final
			// candidate
			if (maxSimilarityNodes.size() != 1) {
				// currently choose first, must revise later...
				finalCandidateNode = maxSimilarityNodes.get(0);
			} else {
				finalCandidateNode = maxSimilarityNodes.get(0);
			}
			pairings.put(n1, finalCandidateNode);
		}
		return pairings;

		/*
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! REFACTOR THIS
		 * CLASS USE STRUCT-LIKE DATA SRUCTURES INSTEAD OF HASH MAPS EXPAND ON
		 * DELETE/CREATE PAIRINGS, CREATE TRANSLATIONS FOR THESE USE THESE TO
		 * RECONCILE RELATIVE ATTRIBUTES
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */

	}

	private static HashMap<SemanticNetNode, NodeSimilarityRecord> calculateSimilarities(
			SemanticNetState semanticNetState1,
			SemanticNetState semanticNetState2) {
		HashMap<SemanticNetNode, NodeSimilarityRecord> NSRMap = new HashMap<SemanticNetNode, NodeSimilarityRecord>();
		//HashMap<SemanticNetNode, HashMap<String, Integer>> similaritiesForNodes = new HashMap<SemanticNetNode, HashMap<String, Integer>>();
		// Compare n1 to every node in semanticNetState, associate a similarity
		// value with each node
		for (SemanticNetNode n1 : semanticNetState1.getNodes().values()) {
			HashMap<SemanticNetNode, Integer> similarity = new HashMap<SemanticNetNode, Integer>();
			for (SemanticNetNode n2 : semanticNetState2.getNodes().values()) {
				// consider attribute, value, and relationships to intra nodes
				// (these should reinforce eachother)
				Iterator<String> attrs1 = n2.getAttributes().keySet()
						.iterator();
				int similarityValue = 0;
				while (attrs1.hasNext()) {
					String attr = attrs1.next();

					// Both nodes have same attribute
					if (n1.getAttributes().containsKey(attr)) {
						// +1 to similarity, this should be made more
						// sophisticated later...
						similarityValue = similarityValue + 1;

						// same attribute has the same value
						// !!!
						// MUST BE REVISED TO HANDLE RELATIVE ATTRIBUTES!!!
						// !!!
						if (n1.getAttributes().get(attr)
								.equals(n2.getAttributes().get(attr))) {
							// +1 to similarity, this should be made more
							// sophisticated later...
							similarityValue = similarityValue + 1;
							// pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr,
							// reasonRelationship(node1, node2, attr));
							// pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr,
							// reasonRelationship(node2, node1, attr));
						} else if (KnowledgeBase.attributeMetaDataMap.get(attr)
								.isRelative()) {
							// For now, assume if node attr/vals have the same
							// number of comma separated values
							// then they are 'similar'

							int numberCSV1 = n1.getAttributes().get(attr)
									.split(",").length;
							int numberCSV2 = n2.getAttributes().get(attr)
									.split(",").length;

							if (numberCSV1 == numberCSV2) {
								similarityValue = similarityValue + 1;
							}
							else
							{
								similarityValue = similarityValue - 1;
							}

						}
						else
						{
							similarityValue = similarityValue - 1;
						}
						// pairedNodeRelationships.put(node1.getName()+","+node2.getName()+","+attr,
						// reasonRelationship(node1, node2, attr));
						// pairedNodeRelationships.put(node2.getName()+","+node1.getName()+","+attr,
						// reasonRelationship(node2, node1, attr));
					}
					else
					{
						similarityValue = similarityValue - 2;
					}

				}
				// consider meta reasoning too i.e. problems D-12

				// consider planar subtraction to isolate what changes between
				// figures.
				similarity.put(n2, similarityValue);
			}
			NSRMap.put(n1, new NodeSimilarityRecord(n1.getName(),
					semanticNetState1.getName(), similarity));
		}
		return NSRMap;

	}

	private HashMap<SemanticNetNode, SemanticNetNode> processDeletedNodes(
			HashMap<SemanticNetNode, SemanticNetNode> pairings) {
		
		HashMap<SemanticNetNode, SemanticNetNode> tempPairings = new HashMap<SemanticNetNode, SemanticNetNode>(pairings);
		for (Entry<SemanticNetNode, SemanticNetNode> e1 : pairings.entrySet()) {
			for (Entry<SemanticNetNode, SemanticNetNode> e2 : pairings
					.entrySet()) {
				//if the prior state node names are different AND posterior node names are the same...
				//determine similarity of both w/ rspt to current node and choose the most similar...
				//if both nodes are equally similar, choose the first node to be a delete node...
				//may need to calculate and map all similarities as one step in the instance of many same nodes...
				if (e1.getKey().getName() != e2.getKey().getName() && e1.getValue().getName() == e2.getValue().getName() && tempPairings.entrySet().contains(e1) && tempPairings.entrySet().contains(e2)) {
					int n1Similarity = this.NSRMap.get(e1.getKey()).getSimilarities().get(e1.getValue());
					int n2Similarity = this.NSRMap.get(e2.getKey()).getSimilarities().get(e2.getValue());
					if(n1Similarity > n2Similarity)
					{
						tempPairings.remove(e2.getKey());
						tempPairings.put(e2.getKey(), new SemanticNetNode("delete"));
					}
					else if(n2Similarity > n1Similarity)
					{
						tempPairings.remove(e1.getKey());
						tempPairings.put(e1.getKey(), new SemanticNetNode("delete"));
					}
					else
					{
						//consider logging message for this case...
						tempPairings.remove(e1.getKey());
						tempPairings.put(e1.getKey(), new SemanticNetNode("delete"));
						
					}
					/*
					 * if(similaritiesForNodes.get(n1Prior).get(finalCandidateNode
					 * ) < similaritiesForNodes.get(n1).get(finalCandidateNode))
					 * { //create "delete-type" pairing for n1Prior and eject
					 * n1Prior from pairings... pairings.remove(n1Prior);
					 * pairings.put(n1Prior, new SemanticNetNode("delete"));
					 * //pairings. } else if
					 * (similaritiesForNodes.get(n1Prior).get
					 * (finalCandidateNode) >
					 * similaritiesForNodes.get(n1).get(finalCandidateNode)) {
					 * //create delete pairing for n1 and do not add n1 to
					 * pairings... pairings.put(n1, new
					 * SemanticNetNode("delete")); } else { //advanced
					 * similarity check between these nodes...? }
					 */
				}
			}
		}
		return tempPairings;

		/*
		 * //"delete-type" pairings SemanticNetNode n1Prior = null;
		 * if(pairings.values().contains(finalCandidateNode)) { //deletions--;
		 * for (Entry<SemanticNetNode, SemanticNetNode> e : pairings.entrySet())
		 * { if (finalCandidateNode.getName().equals(e.getValue().getName())) {
		 * n1Prior = e.getKey(); } }
		 * 
		 * if(similaritiesForNodes.get(n1Prior).get(finalCandidateNode) <
		 * similaritiesForNodes.get(n1).get(finalCandidateNode)) { //create
		 * "delete-type" pairing for n1Prior and eject n1Prior from pairings...
		 * pairings.remove(n1Prior); pairings.put(n1Prior, new
		 * SemanticNetNode("delete")); //pairings. } else if
		 * (similaritiesForNodes.get(n1Prior).get(finalCandidateNode) >
		 * similaritiesForNodes.get(n1).get(finalCandidateNode)) { //create
		 * delete pairing for n1 and do not add n1 to pairings...
		 * pairings.put(n1, new SemanticNetNode("delete")); } else { //advanced
		 * similarity check between these nodes...? } } else { pairings.put(n1,
		 * finalCandidateNode); }
		 */
	}

	private static HashMap<SemanticNetNode, SemanticNetNode> processSpawnedNodes(
			HashMap<SemanticNetNode, SemanticNetNode> pairings) {
		ArrayList<SemanticNetNode> createNodes = new ArrayList<SemanticNetNode>(pairings.values());
		for (Entry<SemanticNetNode, SemanticNetNode> e : pairings.entrySet()) {
			if (createNodes.contains(e.getValue())) {
				createNodes.remove(e.getValue());
			}
		}
		for (SemanticNetNode c : createNodes) {
			pairings.put(new SemanticNetNode("create"), c);
		}
		return pairings;
	}

	/*public HashMap<String, SemanticNetRelationship> getPairedNodeRelationships() {
		return pairedNodeRelationships;
	}

	public void setPairedNodeRelationships(HashMap<String, SemanticNetRelationship> pairedNodeRelationships) {
		this.pairedNodeRelationships = pairedNodeRelationships;
	}*/

}

class NodeSimilarityRecord {
	HashMap<SemanticNetNode, Integer> similarities;
	private String stateName;
	private String nodeName;

	public NodeSimilarityRecord(String nodeName, String stateName,
			HashMap<SemanticNetNode, Integer> similarities) {
		this.similarities = similarities;
		this.stateName = stateName;
		this.nodeName = nodeName;
	}

	public HashMap<SemanticNetNode, Integer> getSimilarities() {
		return similarities;
	}

	public String getStateName() {
		return stateName;
	}

	public String getNodeName() {
		return nodeName;
	}
	
	public String toString() {
		return this.stateName+" : "+this.nodeName;
	}
}

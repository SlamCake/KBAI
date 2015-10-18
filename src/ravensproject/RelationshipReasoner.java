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


public class RelationshipReasoner {
	private HashMap<String, SemanticNetRelationship> pairedNodeRelationships = new HashMap<String, SemanticNetRelationship>();
	private static HashMap<String, NodeSimilarityRecord> NSRMap = new HashMap<String, NodeSimilarityRecord>();
	private static ArrayList<NodeSimilarityRecord> NSRList = new ArrayList<NodeSimilarityRecord>();
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
			HashSet<String> attributes = (HashSet<String>) Utilities.union(node1.getAttributes().keySet(), node2.getAttributes().keySet());
			Iterator<String> a1i = attributes.iterator();
			int i = 0;
			while (a1i.hasNext()) {
				attr = a1i.next();
				// Check attributes to get insights on what kind of relationship
				// might be reasoned, consider passing a nominal
				// value to reasonRelationship()!

				// Both nodes have same attribute
				if (node1.getAttributes().containsKey(attr) && node2.getAttributes().containsKey(attr)) {
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
							pairedNodeRelationships.put(node1.getName() + ","
									+ node2.getName() + "," + attr,
									reasonRelationship(node1, node2, attr));
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
				else if(KnowledgeBase.attributeMetaDataMap.get(attr).isDiscrete() && (node1.getAttributes().containsKey(attr) ^ node2.getAttributes().containsKey(attr)))
				{
					pairedNodeRelationships.put(node1.getName() + ","
							+ node2.getName() + "," + attr,
							reasonRelationship(node1, node2, attr));
				}
				else if(KnowledgeBase.attributeMetaDataMap.get(attr).isRelative() && (node1.getAttributes().containsKey(attr) ^ node2.getAttributes().containsKey(attr)))
				{

					//int n1ValuePresence = node1.getAttributes().containsKey(attr) ? node1.getAttributes().get(attr).split(",").length : 0;
					//int n2ValuePresence = node2.getAttributes().containsKey(attr) ? node2.getAttributes().get(attr).split(",").length : 0;
					// This check cannot verify node position exchanges...only
					// isolated displacements.
					pairedNodeRelationships.put(node1.getName() + ","
							+ node2.getName() + "," + attr,
							reasonRelationship(node1, node2, attr));
				}
			}
		}

		else
		{
			if(node2.getType().equals("delete"))
			{
				attr = "delete";
				pairedNodeRelationships.put(node1.getName() + ","
						+ "null" + "," + attr,
						reasonRelationship(node1, node2, attr));
			}
			else
			{
				attr = "create";
				pairedNodeRelationships.put("null" + ","
						+ node2.getName() + "," + attr,
						reasonRelationship(node1, node2, attr));
			}
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
		String transformationPosition = "noop";
		String transformationSpecification = "noop";
		String value1 = "";
		String value2 = "";
		
		if(!attr.equals("create") && !attr.equals("delete"))
		{

			snr.setSourceNodeName(node1.getName());
			snr.setDestinationNodeName(node2.getName());
			
			snr.setSourcePositionSignature(node1.getRelativePositionSignature());
			snr.setDestinationPositionSignature(node2.getRelativePositionSignature());
			snr.setSourcePositionAttrs(node1.getRelativeAttributes());
			snr.setDestinationPositionSignature(node2.getRelativePositionSignature());
			snr.setDestinationPositionAttrs(node2.getRelativeAttributes());
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
				snr.setDestinationPositionSignature(node2.getRelativePositionSignature());
				snr.setDestinationPositionAttrs(node2.getRelativeAttributes());
			}
			else
			{
				snr.setSourceNodeName(node1.getName());
				snr.setSourcePositionSignature(node1.getRelativePositionSignature());
				snr.setSourcePositionAttrs(node1.getRelativeAttributes());
			}
		}

		snr.setAttribute(attr);

		// If statements establish pruning directives considering only node1 and
		// node2.
		// Pruning directives based on more general (holistic?) criteria may
		// need to be established.

		//check to verify that the attribute did not suddenly appear...
		if(!(node1.getAttributes().containsKey(attr) ^ node2.getAttributes().containsKey(attr)))
		{
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
					transformationSpecification = "angle_reflection_x_y";
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
				if (difference >= 1) {
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
					transformationSpecification = "alignment_right_up";
				} else if (difference[0] == 1 && difference[1] == -1) {
					transformationSpecification = "alignment_right_!up";

				} else if (difference[0] == -1 && difference[1] == 1) {
					transformationSpecification = "alignment_!right_up";
				} else if (difference[0] == -1 && difference[1] == 1) {
					transformationSpecification = "alignment_!right_!up";
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
		}
		else //attr became available or unavailable...
		{
			if(node1.getAttributes().containsKey(attr))
			{
				int difference;
				switch (attr) {
				case "width":
					difference = Math.abs((int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("size")) - (int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("width")));
							if((int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("size")) > (int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("width")))
							{
								transformationSpecification = "width_increase_"+String.valueOf(difference);
							}
							else if((int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("size")) < (int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("width")))
							{
								transformationSpecification = "width_!increase_"+String.valueOf(difference);
							}
					break;
				case "height": 
					difference = Math.abs((int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("size")) - (int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("height")));
							if((int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("size")) > (int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("height")))
							{
								transformationSpecification = "height_increase_"+String.valueOf(difference);
							}
							else if((int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("size")) < (int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("height")))
							{
								transformationSpecification = "height_!increase_"+String.valueOf(difference);
							}
					break;
				default:
					transformationSpecification = "!"+attr;
					break;
				}
			}
			else if(node2.getAttributes().containsKey(attr))
			{
				int difference;
				switch (attr) {
				case "width":
					difference = Math.abs((int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("size")) - (int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("width")));
							if((int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("size")) > (int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("width")))
							{
								transformationSpecification = "width_!increase_"+String.valueOf(difference);
							}
							else if((int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("size")) < (int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("width")))
							{
								transformationSpecification = "width_increase_"+String.valueOf(difference);
							}
					break;
				case "height": 
					difference = Math.abs((int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("size")) - (int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("height")));
							if((int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("size")) > (int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("height")))
							{
								transformationSpecification = "height_!increase_"+String.valueOf(difference);
							}
							else if((int)KnowledgeBase.sizeMap.get(node1.getAttributes().get("size")) < (int)KnowledgeBase.sizeMap.get(node2.getAttributes().get("height")))
							{
								transformationSpecification = "height_increase_"+String.valueOf(difference);
							}
					break;
				default:
					transformationSpecification = attr;
					break;
				}
			}
			else
			{
				transformationSpecification = attr;
			}
		}

		snr.setTransformationSpecification(transformationSpecification);
		snr.setCost(transformationSpecification.split("_").length);
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
		s1.setSnnMappings(transformationNodeMappings);
		// For each state, reason relationships between each mapping...

    	String[] logArgs ;
    	if(KBAILogging.metrics)
    	{
        	logArgs = new String[]{"Evaluating Transformations Between Nodes", "start"};
        	KBAILogging.updateLog("performance", logArgs);
        	KBAILogging.updateLog("memory consumption", logArgs);
    	}
    	
//get key here is overwriting the 'create' entries where the 'key' would have been the 
		for (Entry<SemanticNetNode, SemanticNetNode> e : transformationNodeMappings
				.entrySet()) {
			transformationRelationships.putAll((compareNodes(e.getKey(),
					e.getValue())));
		}
    	

    	if(KBAILogging.metrics)
    	{
        	logArgs = new String[]{"Evaluating Transformations Between Nodes", "stop"};
        	KBAILogging.updateLog("performance", logArgs);
        	KBAILogging.updateLog("memory consumption", logArgs);
    	}
		

		
		for (SemanticNetRelationship snr : transformationRelationships.values()) {
			snr.setSourceStateName(s1.getName());
			snr.setDestinationStateName(s2.getName());
		}
		
		//remove position relationships resulting from creations/deletions...
		HashMap<String, SemanticNetRelationship> tempTransRelationships = new HashMap<String, SemanticNetRelationship>(transformationRelationships);
		HashSet<String> create_delete_node_names = new HashSet<String>();
		for (SemanticNetRelationship snr : tempTransRelationships.values()) {
			if(snr.getAttribute().equals("create"))
			{
				create_delete_node_names.add(snr.getDestinationNodeName());
			}
			else if(snr.getAttribute().equals("delete"))
			{
				create_delete_node_names.add(snr.getSourceNodeName());
			}
		}
		
		for (SemanticNetRelationship snr : tempTransRelationships.values()) {
			if(KnowledgeBase.attributeMetaDataMap.get(snr.getAttribute()).isRelative())
			{
				SemanticNetNode sNode = s1.getNodeByName(snr.getSourceNodeName());
				SemanticNetNode dNode = s2.getNodeByName(snr.getDestinationNodeName());
				HashSet<String> posAttrVals = new HashSet<String>();
				if(sNode.getAttributes().containsKey(snr.getAttribute()))
				{
					posAttrVals.addAll(new HashSet<String>(Arrays.asList(sNode.getAttributes().get(snr.getAttribute()).split(","))));
				}
				if(dNode.getAttributes().containsKey(snr.getAttribute()))
				{
					posAttrVals.addAll(new HashSet<String>(Arrays.asList(dNode.getAttributes().get(snr.getAttribute()).split(","))));
				}
				//HashSet<String> posAttrVals = new HashSet<String>(Arrays.asList(s1.getNodeByName(snr.getSourceNodeName()).getAttributes().get(snr.getAttribute()).split(",")));
				//posAttrVals.addAll(new HashSet<String>(Arrays.asList(s2.getNodeByName(snr.getDestinationNodeName()).getAttributes().get(snr.getAttribute()).split(","))));
				posAttrVals.removeAll(create_delete_node_names);
				
				HashSet<String> unchangedPairedNodeNames = new HashSet<String>();
				for(String s : posAttrVals)
				{
					if(s1.getNodes().containsKey(s))
					{
						sNode = s1.getNodeByName(s);
						if(posAttrVals.contains(s1.getSnnMappings().get(sNode).getName()))
						{
							unchangedPairedNodeNames.add(sNode.getName());
							unchangedPairedNodeNames.add(s1.getSnnMappings().get(sNode).getName());
						}
					}
				}
				
				posAttrVals.removeAll(unchangedPairedNodeNames);
				
				if(posAttrVals.isEmpty())
				{
					transformationRelationships.remove(snr.getSourceNodeName()+","+snr.getDestinationNodeName()+","+snr.getAttribute());
				}
			}
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
		/*this.NSRMap.putAll((calculateSimilarities(semanticNetState1,
				semanticNetState2)));
		pairings.putAll(mapSimilarNodes(semanticNetState1, semanticNetState2,
				this.NSRMap));*/
		
		

    	String[] logArgs ;
    	if(KBAILogging.metrics)
    	{
        	logArgs = new String[]{"Node Similarity Calculation", "start"};
        	KBAILogging.updateLog("performance", logArgs);;
        	KBAILogging.updateLog("memory consumption", logArgs);
    	}

		calculateSimilarities(semanticNetState1, semanticNetState2);
    	

    	if(KBAILogging.metrics)
    	{
        	logArgs = new String[]{"Node Similarity Calculation", "stop"};
        	KBAILogging.updateLog("performance", logArgs);
        	KBAILogging.updateLog("memory consumption", logArgs);
    	}
    	

    	if(KBAILogging.metrics)
    	{
        	logArgs = new String[]{"Node Mapping", "start"};
        	KBAILogging.updateLog("performance", logArgs);;
        	KBAILogging.updateLog("memory consumption", logArgs);
    	}

		pairings.putAll(mapSimilarNodes(semanticNetState1, semanticNetState2));
    	

    	if(KBAILogging.metrics)
    	{
        	logArgs = new String[]{"Node Mapping", "stop"};
        	KBAILogging.updateLog("performance", logArgs);
        	KBAILogging.updateLog("memory consumption", logArgs);
    	}
		
		
		
		
		// "delete-type" node pairings
		/*if (semanticNetState1.getNodes().size() > semanticNetState2.getNodes()
				.size()) {
			pairings = processDeletedNodes(pairings);
		}
		// "create-type" node pairings
		if (semanticNetState1.getNodes().size() < semanticNetState2.getNodes()
				.size()) {
			pairings = processSpawnedNodes(pairings);
		}*/

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
			SemanticNetState s1,
			SemanticNetState s2) {
		// Find node with highest similarity value
		HashMap<SemanticNetNode, SemanticNetNode> pairings = new HashMap<SemanticNetNode, SemanticNetNode>();
		HashMap<String, NodeSimilarityRecord> sNSRMap = new HashMap<String, NodeSimilarityRecord>();
		HashMap<String, NodeSimilarityRecord>  dNSRMap = new HashMap<String, NodeSimilarityRecord>();
		//HashMap<String, String> S2DMap = new HashMap<String, String>();
		//HashMap<String, String> D2SMap = new HashMap<String, String>();
		// SemanticNetNode maxSimilarityNode = null;

		for (NodeSimilarityRecord nsr : NSRList) 
		{
			SemanticNetNode sNode = s1.getNodeByName(nsr.getSourceNodeName());
			SemanticNetNode dNode = s2.getNodeByName(nsr.getDestinationNodeName());
			if(!pairings.containsKey(sNode)	&& !pairings.containsValue(dNode))
			{
				pairings.put(sNode, dNode);
				sNSRMap.put(sNode.getName(), nsr);
				dNSRMap.put(dNode.getName(), nsr);
				//S2DMap.put(nsr.getSourceNodeName(), nsr.getDesinationNodeName());
				//D2SMap.put(nsr.getDesinationNodeName(), nsr.getSourceNodeName());
			}
			else if (pairings.containsKey(s1.getNodeByName(nsr.getSourceNodeName()))) // risk of redundantly mapping same source node
			{
				NodeSimilarityRecord priorNsr = sNSRMap.get(sNode.getName());
				SemanticNetNode priorDNode = s2.getNodeByName(priorNsr.getDestinationNodeName());
				if(nsr.similarity > priorNsr.similarity)
				{
					pairings.remove(sNode);
					sNSRMap.remove(sNode.getName());
					dNSRMap.remove(priorDNode.getName());
					
					pairings.put(sNode, dNode);
					sNSRMap.put(sNode.getName(), nsr);
					dNSRMap.put(dNode.getName(), nsr);
					/*if(sNSRMap.size() == s1.getNodes().size())
					{
						pairings.put((, value)
					}*/
				}
				else if(nsr.similarity == priorNsr.similarity)
				{
					//Log These Cases...
				}
				
			}
			else // risk of redundantly mapping same destination node
			{
				NodeSimilarityRecord priorNsr = dNSRMap.get(dNode.getName());
				SemanticNetNode priorSNode = s1.getNodeByName(priorNsr.getSourceNodeName());
				if(nsr.similarity > priorNsr.similarity)
				{
					pairings.remove(priorNsr.getSourceNodeName());
					sNSRMap.remove(priorSNode.getName());
					dNSRMap.remove(dNode.getName());
					
					pairings.put(sNode, dNode);
					sNSRMap.put(sNode.getName(), nsr);
					dNSRMap.put(dNode.getName(), nsr);
				}
				else if(nsr.similarity == priorNsr.similarity)
				{
					//Log These Cases...
				}
			}
		}
		// populate delete nodes
		for(SemanticNetNode n : s1.getNodes().values())
		{
			if(!pairings.containsKey(n))
			{
				pairings.put(n, new SemanticNetNode("delete"));
			}
		}
		// populate create nodes
		for(SemanticNetNode n : s2.getNodes().values())
		{
			if(!pairings.containsValue(n))
			{
				pairings.put(new SemanticNetNode("create"), n);
			}
		}
		return pairings;
	}

		/*
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! REFACTOR THIS
		 * CLASS USE STRUCT-LIKE DATA SRUCTURES INSTEAD OF HASH MAPS EXPAND ON
		 * DELETE/CREATE PAIRINGS, CREATE TRANSLATIONS FOR THESE USE THESE TO
		 * RECONCILE RELATIVE ATTRIBUTES
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */

	

	private static void calculateSimilarities(
			SemanticNetState semanticNetState1,
			SemanticNetState semanticNetState2) {
		NSRList.clear();
		//NSRMap.clear();
		for (SemanticNetNode n1 : semanticNetState1.getNodes().values()) {
			HashMap<SemanticNetNode, Integer> similarity = new HashMap<SemanticNetNode, Integer>();
			for (SemanticNetNode n2 : semanticNetState2.getNodes().values()) 
			{
				//similarity.put(n2, n1.calculateNonRelativeSimilarity(n2));
				//NodeSimilarityRecord nsr = new NodeSimilarityRecord(n1.getName(), n2.getName(), semanticNetState1.getName(), n1.calculateNonRelativeSimilarity(n2), n1.calculateRelativeSimilarity(n2));
				NodeSimilarityRecord nsr = new NodeSimilarityRecord(n1.getName(), n2.getName(), semanticNetState1.getName(), n1.calculateNonRelativeSimilarity(n2), n1.calculateRelativePositionSimilarity(n2.getRelativePositionSignature()));
				NSRList.add(nsr);
				//NSRMap.put(nsr.getSourceNodeName()+"_"+nsr.getDestinationNodeName(), nsr);
			}
		}
		//System.out.println(semanticNetState1.getName()+"_"+semanticNetState2.getName());
		Collections.sort(NSRList, new NSRComparator());
	}

	/*private HashMap<SemanticNetNode, SemanticNetNode> processDeletedNodes(
			HashMap<SemanticNetNode, SemanticNetNode> pairings) {
		
		HashMap<SemanticNetNode, SemanticNetNode> tempPairings = new HashMap<SemanticNetNode, SemanticNetNode>(pairings);
		for (Entry<SemanticNetNode, SemanticNetNode> e1_1 : pairings.entrySet()) {
			for (Entry<SemanticNetNode, SemanticNetNode> e1_2 : pairings
					.entrySet()) {
				//if the prior state node names are different AND posterior node names are the same...
				//determine similarity of both w/ rspt to current node and choose the most similar...
				//if both nodes are equally similar, choose the first node to be a delete node...
				//may need to calculate and map all similarities as one step in the instance of many same nodes...
				
				//if both entries have DIFFERENT NODE KEYS and THE SAME NODE VALUES and temp node pairings
				//DOES NOT have an entry for EITHER NODE KEY
				if (e1_1.getKey().getName() != e1_2.getKey().getName() 
						&& e1_1.getValue().getName() == e1_2.getValue().getName() 
						&& tempPairings.entrySet().contains(e1_1) 
						&& tempPairings.entrySet().contains(e1_2)) 
				{
					int n1Similarity = this.NSRMap.get(e1_1.getKey()).getSimilarities().get(e1_1.getValue());
					int n2Similarity = this.NSRMap.get(e1_2.getKey()).getSimilarities().get(e1_2.getValue());
					if(n1Similarity > n2Similarity)
					{
						tempPairings.remove(e1_2.getKey());
						tempPairings.put(e1_2.getKey(), new SemanticNetNode("delete"));
					}
					else if(n2Similarity > n1Similarity)
					{
						tempPairings.remove(e1_1.getKey());
						tempPairings.put(e1_1.getKey(), new SemanticNetNode("delete"));
					}
					else // both node pairings are equally similar...
					{
						//log this situaiton...
						
						//determine which pair involves the least costly transformations, select this one...
						HashMap<String, SemanticNetRelationship> e1_1Transformations = compareNodes(e1_1.getKey(), e1_1.getValue());
						HashMap<String, SemanticNetRelationship> e1_2Transformations = compareNodes(e1_2.getKey(), e1_2.getValue());
						int sumOfCosts1 = 0;
						int sumOfCosts2 = 0;
						for(SemanticNetRelationship snr : e1_1Transformations.values()){ sumOfCosts1 += snr.getCost(); }
						for(SemanticNetRelationship snr : e1_2Transformations.values()){ sumOfCosts2 += snr.getCost(); }
						if(sumOfCosts1 > sumOfCosts2)
						{
							tempPairings.remove(e1_1.getKey());
							tempPairings.put(e1_1.getKey(), new SemanticNetNode("delete"));
						}
						else if(sumOfCosts2 > sumOfCosts2)
						{
							tempPairings.remove(e1_2.getKey());
							tempPairings.put(e1_2.getKey(), new SemanticNetNode("delete"));
						}
						else // both node pairings have the same cost of transformations...
						{
							//log this situation...
						}
						
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
		//		}
		//	}
	//	}
	//	return tempPairings;

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
	//}

	/*private static HashMap<SemanticNetNode, SemanticNetNode> processSpawnedNodes(
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
	}*/

	/*public HashMap<String, SemanticNetRelationship> getPairedNodeRelationships() {
		return pairedNodeRelationships;
	}

	public void setPairedNodeRelationships(HashMap<String, SemanticNetRelationship> pairedNodeRelationships) {
		this.pairedNodeRelationships = pairedNodeRelationships;
	}*/

}

class NodeSimilarityRecord {
	int similarity;
	private String stateName;
	private String sourceNodeName;
	private String destinationNodeName;
	private float quality;
	private int rSimilarity;
	private int nrSimilarity;

	public NodeSimilarityRecord(String sourceNodeName, String destinationNodeName, String stateName, int nrSimilarity, int rSimilarity) {
		this.nrSimilarity = nrSimilarity;
		this.rSimilarity = rSimilarity;
		this.stateName = stateName;
		this.sourceNodeName = sourceNodeName;
		this.destinationNodeName = destinationNodeName;
		this.similarity = rSimilarity+nrSimilarity;
	}

	public int getSimilarity() {
		return similarity;
	}
	public String getStateName() {
		return stateName;
	}

	public String getSourceNodeName() {
		return sourceNodeName;
	}

	public String getDestinationNodeName() {
		return destinationNodeName;
	}
	
	public String toString() {
		return this.stateName+" : "+this.sourceNodeName+" "+this.destinationNodeName+" "+this.similarity;
	}
}

class NSRComparator implements Comparator<NodeSimilarityRecord>{
 
    @Override
    public int compare(NodeSimilarityRecord nsr1, NodeSimilarityRecord nsr2) {
        if(nsr1.getSimilarity() < nsr2.getSimilarity()){
            return 1;
        } else if(nsr1.getSimilarity() > nsr2.getSimilarity()){
            return -1;
        }
        else
        {
        	return 0;
        }
    }
}
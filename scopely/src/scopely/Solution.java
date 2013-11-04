package scopely;

import java.util.*;

public class Solution {
	
	static int id = 0;
	static int parentId = 0;
	
	
	private Map<Integer, HashMap<Integer, Node>> mapParentIdToChildId = new HashMap<Integer, HashMap<Integer,Node>>();
	
	public void addToNodeList(List<Node> lstNodes, String parent, List<String> sameLevelNodes){
		
		for (Node n : lstNodes)
			if(n.nodeName.equals(parent))
				parentId = n.id;
		
		for (String sameLevelNode : sameLevelNodes){
			Node newNode = new Node();
			newNode.id = id;
			newNode.nodeName = sameLevelNode;
			newNode.parentId = parentId;
			lstNodes.add(newNode);
			id++;
		}
	}
	
	public void addNodesToTree(List<Node> inputPath){
		
		HashMap<Integer,Node> mapIdtoNode;
		
		for(Node node : inputPath){
			
			if(!mapParentIdToChildId.containsKey(node.parentId)){
				mapIdtoNode = new HashMap<Integer, Node>();	
				mapIdtoNode.put(node.id,node);
			}
			
			else
				mapIdtoNode = mapParentIdToChildId.get(node.parentId);

			mapParentIdToChildId.put(node.parentId, mapIdtoNode);
		}
	}
}

package scopely;

import java.util.*;

public class Driver {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Solution objSolution = new Solution();
		List<Node> lstNodes = new ArrayList<Node>();

		while(sc.hasNextLine()){
			String input = sc.nextLine();
			String[] pathNodes = input.split("/");
			List<String> parentList = new ArrayList<String>();
			
			parentList.add("/");

			for(String node: pathNodes){
				String[] splitNodes = node.split("\\|");
				ArrayList<String> lstCombo = new ArrayList<String>();
				int j = 0;

				if(splitNodes.length > 1){
					for(String snode : splitNodes){
						comboGenerator("", lstCombo, splitNodes, j,  1, splitNodes.length);
						j++;
					}
				}

				else
					lstCombo.add(node);

				for(String parent : parentList)
					objSolution.addToNodeList(lstNodes, parent, lstCombo);

				parentList.clear();
				parentList.addAll(0, lstCombo);
			}

			objSolution.addNodesToTree(lstNodes);	
		}
	}

	public static void comboGenerator(String combo, ArrayList<String> lstCombo, String[] input, int index, int limit, int maxLimit){

		for(int i = index; i< input.length; i++){

			String node = input[i];

			if(combo.equals(node))
				continue;

			if(combo.equals(""))
				combo = node;
			else 
				combo = combo+"-"+node;

			lstCombo.add(combo);
		}
	}

}

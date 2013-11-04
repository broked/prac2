package tsig2;

import java.util.*;
import java.io.*;

public class DataSynchronizer {

	public static void main(String[] args) {

		int numDC = 0;
		int ctr = 1;

		/**
		 * map datasetID to the data centers that contain that dataset
		 */
		Map<Integer,ArrayList<Integer>> mapDataListToData = new HashMap<Integer, ArrayList<Integer>>();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try{
			numDC = Integer.parseInt(br.readLine());
		}

		catch(IOException ex){
			System.out.println(ex.getMessage());
		}

		if(numDC < 0)
			System.exit(0);

		while(ctr <= numDC ){

			try {
				String inputLine = br.readLine();
				StringTokenizer st = new StringTokenizer(inputLine," ");

				while(st.hasMoreElements()){
					int totalDS = Integer.parseInt(st.nextToken());
					int i=0;

					while (i < totalDS){
						int dataSetId = Integer.parseInt(st.nextToken());
						ArrayList<Integer> lst = mapDataListToData.get(dataSetId);

						if(lst != null)
							lst.add(ctr);
						
						else{
							lst = new ArrayList<Integer>();
							lst.add(ctr);
							mapDataListToData.put(dataSetId, lst);
						}
						i++;
					}
				}
				ctr++;
			}
			
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println();
		
		for(Integer key: mapDataListToData.keySet()){
			
			List<Integer> dCList = mapDataListToData.get(key);
			
			for(int i=1;i<=numDC;i++){
				
				if(!dCList.contains(i))
					System.out.println(key+" "+ dCList.get(0)+" "+i);
			}
		}
		
		System.out.println("done");
	}
}
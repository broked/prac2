package tsig1;

import java.util.*;
import java.io.*;

public class JumbleSort {

	public static void main(String[] args) {

		String inputString = "";

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try{
			inputString = br.readLine();

			StringTokenizer st = new StringTokenizer(inputString," ");

			List<String> wordList = new ArrayList<String>();
			List<Integer> numberList = new ArrayList<Integer>();

			while(st.hasMoreTokens()){
				String next = st.nextToken();

				if(isInteger(next)){
					numberList.add(Integer.parseInt(next));
				}

				else{
					wordList.add(next);
				}
			}

			Collections.sort(wordList);
			Collections.sort(numberList);
			
			int idxNum = 0;
			int idxWord = 0;
			
			for(String str : inputString.split(" ")){
				
				if(isInteger(str)){
					System.out.print(numberList.get(idxNum) + " ");
					idxNum++;
				}
				
				else{
					System.out.print(wordList.get(idxWord) + " ");
					idxWord++;
				}
			}
		}
		
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	}

	private static boolean isInteger(String chkString) {
		
		try { 
			Integer.parseInt(chkString); 
		} 
		
		catch(NumberFormatException e) { 
			return false; 
		}

		return true;
	}
}
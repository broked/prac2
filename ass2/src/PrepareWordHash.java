import java.io.*;
import java.util.*;

public class PrepareWordHash {
	
	private long totalWordCount = 0;

	public long getTotalWordCount() {
		return totalWordCount;
	}

	public void setTotalWordCount(long totalWordCount) {
		this.totalWordCount = totalWordCount;
	}

	public HashMap<String,Integer> buildWordCountHash(String fileName){

		HashMap<String,Integer> hashStruct = new HashMap<String,Integer>();
		String readLine;
		String[] readWords;
		int wordCount,eol=1;
		String eolSymbol = "Á", unkSymbol = "ª";

		try{
			BufferedReader scanLine = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "ISO-8859-1"));

			while((readLine = scanLine.readLine()) != null){
				readWords = readLine.split(" ");

				for (String readWord : readWords) {
					if(!hashStruct.containsKey(readWord)){
						hashStruct.put(readWord,1);
					}
					else {
						wordCount = hashStruct.get(readWord);
						hashStruct.remove(readWord);
						hashStruct.put(readWord, ++wordCount);		
					}
					this.totalWordCount++;
				}
				eol++;
			}

			//add end of line marker
			hashStruct.put(eolSymbol,eol);
			//add Unk to the hashset
			hashStruct.put(unkSymbol,0);
			
			this.totalWordCount++;
			this.setTotalWordCount(totalWordCount);
			
			scanLine.close();
		}

		catch(Exception ex){
			System.out.println("Exceptional performance in WordHash!:" + ex.getMessage());
		}	 

		return hashStruct;
	}

	public void printCountOfUniqueWords(HashMap<String, Integer> hashStruct){
		System.out.println("Number of unique words:" + hashStruct.size());
	}
}
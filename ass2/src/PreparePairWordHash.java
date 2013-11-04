import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class PreparePairWordHash {

	private HashMap<StringPair,Integer> hashStruct;
	
	public HashMap<StringPair, Integer> getHashStruct() {
		return hashStruct;
	}

	public void setHashStruct(HashMap<StringPair, Integer> hashStruct) {
		this.hashStruct = hashStruct;
	}

	public PreparePairWordHash(){
		hashStruct = new HashMap<StringPair,Integer>();
	}
	
	public void buildWordPairCountHash(String fileName){
		String readLine, readWord1, readWord2,eolSymb="Á",unkSymb="ª",readWords[];
		int wordCount=0;

		try{
			BufferedReader scanLine = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "ISO-8859-1"));

			while((readLine = scanLine.readLine()) != null){
				readWords = readLine.split(" ");

				for (int i=0;i<readWords.length;i++) {
					StringPair pairOfWords = new StringPair();
					
					if(i == readWords.length-1){
						readWord1 = readWords[i];
						readWord2 = eolSymb;
					}
					else{
					readWord1 = readWords[i];
					readWord2 = readWords[i+1];
					}

					pairOfWords.setStr1(readWord1);
					pairOfWords.setStr2(readWord2);

					if(!hashStruct.containsKey(pairOfWords))
						hashStruct.put(pairOfWords,1);
					else {
						wordCount = hashStruct.get(pairOfWords);
						hashStruct.remove(pairOfWords);
						hashStruct.put(pairOfWords, ++wordCount);		
					}
				}
			}
			
			StringPair pairOfWords = new StringPair();
			pairOfWords.setStr1(unkSymb);
			pairOfWords.setStr2(unkSymb);
			hashStruct.put(pairOfWords,0);
			scanLine.close();
		}

		catch(Exception ex){
			System.out.println("Exceptional performance in PairHash!:" + ex.getMessage());
		}	 
	}

	public long returnExactPairCount(StringPair strPair){
		long count = 0;
		
		if(this.hashStruct.containsKey(strPair))
				count = this.hashStruct.get(strPair);
		return count;
	}
}

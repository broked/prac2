/**
 * Question 2
 * The program reads in a string containing a sentence 
 * Returns a string consisting of number of characters in each word including punctuation
 * 
 * @author anubhav malhotra
 * @version 1.0
 * @LastModified Sept 7 2013
 *
 */

package riverbed;

public class NumOfChars {

	public String countOfCharsPerWord(String sentence){

		String[] words = sentence.split(" ");
		StringBuilder sb = new StringBuilder();

		try{
			for(String word : words){
				sb.append(word.length());
				sb.append(" ");
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}

		return sb.toString();
	}

}
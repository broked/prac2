import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.Math;

public class Unigram {

	//using preparedWordHash class
	private PrepareWordHash objTrainingWordCount,objHeldOutWordCount,objTestWordCount;
	private HashMap<Integer, Double> sentenceLogProb;

	//constant set of values for Golden Search algorithm
	private double goldenPhi = (1 + Math.sqrt(5)) / 2;
	private double resultPhi = 2 - goldenPhi;

	//hash maps to be used
	private HashMap<String,Double> smoothLogThetaValues,smoothThetaValues;

	private HashMap<String, Integer> trainingHash, heldoutHash,testHash;
	
	//constructor for the class
	public Unigram(){
		this.objTrainingWordCount = new PrepareWordHash();
		this.objHeldOutWordCount = new PrepareWordHash();
		this.objTestWordCount = new PrepareWordHash();

		this.smoothLogThetaValues = new HashMap<String, Double>();
		this.smoothThetaValues = new HashMap<String, Double>();
		this.smoothThetaValues = new HashMap<String, Double>();
		this.sentenceLogProb = new HashMap<Integer, Double>();
	}

	//getter & setter for different variables/data structures used
	public HashMap<String, Double> getSmoothThetaValues() {
		return smoothThetaValues;
	}

	public void setSmoothThetaValues(HashMap<String, Double> smoothThetaValues) {
		this.smoothThetaValues = smoothThetaValues;
	}
	
	public HashMap<String, Integer> getTrainingHash() {
		return trainingHash;
	}

	public void setTrainingHash(HashMap<String, Integer> trainingHash) {
		this.trainingHash = trainingHash;
	}

	public HashMap<Integer, Double> getSentenceProb(){
		return this.sentenceLogProb;
	}

	public void setSentenceProb(HashMap<Integer, Double> sentenceProb){
		this.sentenceLogProb = sentenceProb;
	}

	public HashMap<String, Double> getSmoothLogThetaValues(){
		return this.smoothLogThetaValues;
	}

	public double getSmoothThetaValueForString(String str){
		return this.smoothThetaValues.get(str);
	}

	public void setSmoothLogThetaValues(HashMap<String, Double> smoothVal){
		this.smoothLogThetaValues = smoothVal;
	}

	//function to clear the smooth values hash
	public void resetsmoothLogThetaValues(){
		this.smoothLogThetaValues.clear();
	}

	//calculates the Unigram probabilities
	public void calcUnigram(String[] args) {
		//prepare three hashes - each for training, held-out & test set corpus
		this.trainingHash = this.objTrainingWordCount.buildWordCountHash(args[0]);
		this.heldoutHash = this.objHeldOutWordCount.buildWordCountHash(args[1]);
		this.testHash = this.objTestWordCount.buildWordCountHash(args[2]);

		//calculate Unigram model with alpha = 1
		System.out.println("Value of alpha used\t\t\t: 1");
		this.unigramModelLog(this.trainingHash, 1);
		System.out.println("Probability of the test document\t: " + this.calcDocumentLogProb());

		//get optimum value of alpha
		double alpha = goldenSectionSearch(0.0,5,10,0.0001);
		System.out.println("Optimum value of alpha calculated\t: "+ alpha);
		this.unigramModelLog(this.trainingHash, alpha);
		System.out.println("Probability of the test document\t: " + this.calcDocumentLogProb());

		//print the overall success ratio
		this.printSentenceLogProb(args[3]);
	}

	//calculates the Unigram log probabilities
	public void unigramModelLog(HashMap<String, Integer> hashStruct, double alpha){
		long totalCountOfWords = this.objTrainingWordCount.getTotalWordCount();
		HashMap<String, Double> smoothLogVal = this.getSmoothLogThetaValues();
		HashMap<String, Double> smoothVal = this.getSmoothThetaValues();
		long typeOfWords = hashStruct.size(),countOfWord;
		double smoothLogTheta,smoothTheta;

		this.resetsmoothLogThetaValues();

		for(String key:  hashStruct.keySet()){
			countOfWord = hashStruct.get(key);
			smoothLogTheta = Math.log(( countOfWord + alpha)/(totalCountOfWords + alpha*typeOfWords))*-1;
			smoothTheta = (countOfWord + alpha)/(totalCountOfWords + alpha*typeOfWords);
			smoothLogVal.put(key,smoothLogTheta);
			smoothVal.put(key,smoothTheta);
		}

		this.setSmoothLogThetaValues(smoothLogVal);
		this.setSmoothThetaValues(smoothVal);
	}

	//get the value of likelihood function
	public double likelihoodCalc(double alpha){
		double likeliHood=1.0;
		long power = 1;
		double smoothThetaVal;

		this.resetsmoothLogThetaValues();
		this.unigramModelLog(this.trainingHash, alpha);

		try{
			for(String key:  this.heldoutHash.keySet()){
				if (this.trainingHash.get(key) == null){
					power = 0;
					smoothThetaVal = this.smoothLogThetaValues.get("ª");
				}
				else {
					power = this.heldoutHash.get(key);
					smoothThetaVal = this.smoothLogThetaValues.get(key);
				}

				likeliHood += power * smoothThetaVal;
			}
		}

		catch (Exception ex){
			System.out.println(ex.getMessage());
		}


		return likeliHood;
	}

	//module to compute the optimum value of alpha
	
	public double goldenSectionSearch(double lowerBound, double maxVal, double upperBound, double tau) {
		double x,y,z;
		if (upperBound - maxVal > maxVal - lowerBound)
			x = maxVal + resultPhi * (upperBound - maxVal);
		else
			x = maxVal - resultPhi * (maxVal - lowerBound);

		if (Math.abs(upperBound - lowerBound) < tau * (Math.abs(maxVal) + Math.abs(x))) 
			return (upperBound + lowerBound) / 2; 

		y = this.likelihoodCalc(x);
		z = this.likelihoodCalc(maxVal);
		assert(y != z );
		if (y < z) {
			if (upperBound - maxVal > maxVal - lowerBound) return goldenSectionSearch(maxVal, x, upperBound, tau);
			else return goldenSectionSearch(lowerBound, x, maxVal, tau);
		}
		else {

			if (upperBound - maxVal > maxVal - lowerBound) return goldenSectionSearch(lowerBound, maxVal, x, tau);
			else return goldenSectionSearch(x, maxVal, upperBound, tau); 
		}
	}

	
	public double calcDocumentLogProb(){
		HashMap<String, Double> smoothTheta = this.getSmoothLogThetaValues();
		double logProb = 0.0, valNum;

		try{
			for(String str : this.testHash.keySet()){
				if(smoothTheta.containsKey(str))
					valNum = smoothTheta.get(str);
				else 
					valNum = smoothTheta.get("ª");

				logProb += valNum * this.testHash.get(str);
			}
		}
		catch(Exception ex){
			System.out.println("Exceptional performance in Unigram object!:" + ex.getMessage());
		}

		return logProb;

	}

	//print the computed Unigram probabilities
	public void printUnigramProbability(){
		HashMap<String, Double> smoothVal = this.getSmoothLogThetaValues();

		for(String key:  smoothVal.keySet()){
			System.out.println("Theta("+ key + ")\t: " + smoothVal.get(key));
		}
	}
	
	//print success ratio
	
	public void printSentenceLogProb(String fileName){
		HashMap<String, Double> smoothAlpha = this.getSmoothLogThetaValues();
		HashMap<Integer, Double> sentenceProb = this.getSentenceProb();
		double successRatio = 0.0;
		Double totalSentProb = 1.0;
		Integer ctr = 1;
		String readLine, readWords[];

		try{
			BufferedReader scanLine = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "ISO-8859-1"));

			while((readLine = scanLine.readLine()) != null){
				readWords = readLine.split(" ");

				for (String readWord : readWords) {
					if(smoothAlpha.containsKey(readWord))
						totalSentProb *= smoothAlpha.get(readWord);
					else
						totalSentProb *= smoothAlpha.get("ª");
				}
				sentenceProb.put(ctr++, totalSentProb);
				totalSentProb = 1.0;
			}

			this.setSentenceProb(sentenceProb);
			scanLine.close();
		}

		catch(Exception ex){
			System.out.println("Exceptional performance in WordHash!:" + ex.getMessage());
		}

		for(int i=1;i+1<ctr+1;i+=2){
			if(sentenceProb.get(i) >= sentenceProb.get(i+1))
				successRatio++;
		}

		System.out.println("Success ratio of Unigram model\t\t: " + (successRatio/(ctr/2))*100);
	}
}
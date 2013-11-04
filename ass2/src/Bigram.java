import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Bigram {

	//using preparedWordHash class
	private PreparePairWordHash objTrainingWordCount,objHeldOutWordCount,objTestWordCount;
	private HashMap<Integer, Double> sentenceLogProb;

	//constant set of values for Golden Section Search algorithm
	private double goldenPhi = (1 + Math.sqrt(5)) / 2;
	private double resultPhi = 2 - goldenPhi;

	//hash maps to be used
	private HashMap<StringPair,Double> SmoothThetaValues;
	private HashMap<StringPair, Integer> trainingHash, heldoutHash,testHash;

	private Unigram objUnigram;
	private StringPair unkPair;
	private final String unkSymbol="ª";
	
	//getters & setters for various variables/data structures
	public StringPair getUnkPair() {
		return unkPair;
	}

	public HashMap<Integer, Double> getSentenceProb(){
		return this.sentenceLogProb;
	}

	public void setSentenceProb(HashMap<Integer, Double> sentenceProb){
		this.sentenceLogProb = sentenceProb;
	}

	public HashMap<StringPair, Double> getSmoothThetaValues(){
		return this.SmoothThetaValues;
	}

	public void setSmoothThetaValues(HashMap<StringPair, Double> smoothVal){
		this.SmoothThetaValues = smoothVal;
	}

	public void resetSmoothThetaValues(){
		this.SmoothThetaValues.clear();
	}

	//constructor for the class
	public Bigram(){
		this.objTrainingWordCount = new PreparePairWordHash();
		this.objHeldOutWordCount = new PreparePairWordHash();
		this.objTestWordCount = new PreparePairWordHash();
		this.SmoothThetaValues = new HashMap<StringPair, Double>();
		this.sentenceLogProb = new HashMap<Integer, Double>();
		this.objUnigram = new Unigram();
		this.SmoothThetaValues = new HashMap<StringPair, Double>();
		this.unkPair = new StringPair();
		this.unkPair.setStr1(this.unkSymbol);
		this.unkPair.setStr2(this.unkSymbol);
	}

	public void calcBigram(String[] args) {

		this.objTrainingWordCount.buildWordPairCountHash(args[0]);
		this.objHeldOutWordCount.buildWordPairCountHash(args[1]);
		this.objTestWordCount.buildWordPairCountHash(args[2]);

		this.trainingHash = this.objTrainingWordCount.getHashStruct();
		this.heldoutHash = this.objHeldOutWordCount.getHashStruct();
		this.testHash = this.objTestWordCount.getHashStruct();

		//calculate Unigram probabilities
		this.objUnigram.calcUnigram(args);

		//calculate Bigram model with beta = 1
		System.out.println("Value of beta used\t\t\t: 1");
		this.bigramModelLog(this.trainingHash, 1.0);
		System.out.println("Probability of the test document\t: " + this.calcDocumentLogProb());

		//get optimum value of beta
		//double beta = this.goldenSectionSearch(0.0,50,99,0.0001);
		//System.out.println("Optimum value of beta  calculated\t: " + beta);
		
		this.bigramModelLog(this.trainingHash, 98);
		System.out.println("Probability of the test document\t: " + this.calcDocumentLogProb());
		
		this.printSentenceLogProb(args[3]);
	}

	public void bigramModelLog(HashMap<StringPair, Integer> hashStruct, double beta){
		HashMap<StringPair, Double> smoothVal = this.getSmoothThetaValues();
		double totalPairsBeginStr1,pairMatch;
		double alphaValue,smoothTheta;

		this.resetSmoothThetaValues();

		try {
			for(StringPair key:  hashStruct.keySet()){
				pairMatch = this.objTrainingWordCount.returnExactPairCount(key);
				totalPairsBeginStr1 = this.objUnigram.getTrainingHash().get(key.getStr1());
				
				alphaValue = this.objUnigram.getSmoothThetaValueForString(key.getStr2());
				smoothTheta = Math.log((pairMatch + beta*alphaValue)/(totalPairsBeginStr1 + beta)) * -1;
				smoothVal.put(key,smoothTheta);
			}

			this.setSmoothThetaValues(smoothVal);
		}
		
		catch  (Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void printBigramProbability(){
		HashMap<StringPair, Double> smoothVal = this.getSmoothThetaValues();

		for(StringPair key:  smoothVal.keySet()){
			System.out.println("Theta(\'"+ key.getStr1() + "\',\'"+ key.getStr2() +"\')\t\t: " + smoothVal.get(key));
		}
	}

	//get the value of likelihood function
	public double likelihoodCalc(double beta){
		double likeliHood=1.0,smoothTheta;
		long power = 1;
		StringPair pairOfWords = this.getUnkPair();
		HashMap<StringPair,Double> smoothHash = this.getSmoothThetaValues();

		this.bigramModelLog(this.trainingHash, beta);

		try{
			for(StringPair key:  this.heldoutHash.keySet()){
				if (smoothHash.get(key) == null){
					power = 0;
					smoothTheta = smoothHash.get(pairOfWords);
				}

				else {
					power = this.heldoutHash.get(key);
					smoothTheta = smoothHash.get(key);
				}
				likeliHood += power * smoothTheta;
			}
		}

		catch (Exception ex){
			System.out.println(ex.getMessage());
		}

		return likeliHood;
	}

	//module to compute the optimum value of beta
	public double goldenSectionSearch(double lowerBound, double maxVal, double upperBound, double tau) {
		double x,y,z;
		if (upperBound - maxVal > maxVal - lowerBound)
			x = maxVal + this.resultPhi * (upperBound - maxVal);
		else
			x = maxVal - this.resultPhi * (maxVal - lowerBound);
		if (Math.abs(upperBound - lowerBound) < tau * (Math.abs(maxVal) + Math.abs(x))) 
			return (upperBound + lowerBound) / 2;

		y = this.likelihoodCalc(x);
		z = this.likelihoodCalc(maxVal);

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
		HashMap<StringPair, Double> smoothTheta = this.getSmoothThetaValues();
		double logProb = 0.0,valNum;

		try{
			for(StringPair str : this.testHash.keySet()){
				if(smoothTheta.containsKey(str))
					valNum = smoothTheta.get(str);
				else
					valNum = smoothTheta.get(this.getUnkPair());
				
				logProb += valNum * this.testHash.get(str);
			}
		}

		catch(Exception ex){
			System.out.println("Exceptional performance in Bigram object!:" + ex.getMessage());
		}	 

		return logProb;
	}

	public void printSentenceLogProb(String fileName){
		HashMap<Integer, Double> sentenceLogProb = this.getSentenceProb();
		HashMap<StringPair, Double> smoothTheta = this.getSmoothThetaValues();

		double successRatio=0.0, totalSentProb=1;
		int ctr = 0;
		String readLine, readWord1, readWord2, eolSymb="Á", readWords[];

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
					
					if(smoothTheta.get(pairOfWords) != null)
						totalSentProb *= smoothTheta.get(pairOfWords);
					
					else 
						totalSentProb *= smoothTheta.get(this.getUnkPair()); 
				}
				
				sentenceLogProb.put(++ctr, totalSentProb);
				totalSentProb = 1;
			}
			
			this.setSentenceProb(sentenceLogProb);
			scanLine.close();
		}

		catch(Exception ex){
			System.out.println("Exceptional performance in PairHash!:" + ex.getMessage());
		}	 
		
		for(int i=1;i+1<sentenceLogProb.size();i+=2){
			if(sentenceLogProb.get(i) < sentenceLogProb.get(i+1))
				successRatio++; 
			//System.out.println(sentenceLogProb.get(i));
		}

		System.out.println("Success ratio of Bigram model\t\t: " + (successRatio/(ctr/2))*100);
	}
}

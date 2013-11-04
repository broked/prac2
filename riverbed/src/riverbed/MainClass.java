package riverbed;

public class MainClass {

	private static final String testFileName = "";
	private final static String testSentence = "This is a short sentence.";
	
	public static void main(String[] args) {

		try{

			System.out.println("Question 1 output");
			GenerateReports objGenerateReports = new GenerateReports();
			objGenerateReports.readInputFile(testFileName);
			objGenerateReports.generateTotalRevReport();
			System.out.println();
			objGenerateReports.generatePerCustomerTaxReport();
			System.out.println();
			objGenerateReports.generateCategoryReport();
			System.out.println();
			objGenerateReports.generatePerCategoryTaxReport();

			System.out.println("Question 2 output");
			System.out.println();
			NumOfChars objNumOfChars = new NumOfChars();
			String ret = objNumOfChars.countOfCharsPerWord(testSentence);
			System.out.println(ret);
		}
		
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
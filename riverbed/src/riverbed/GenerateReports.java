/**
 * @author anubhav malhotra
 * @version 1.0
 * @LastModified Sept 7 2013
 */

package riverbed;

import java.io.*;
import java.util.*;

public class GenerateReports {

	private String customerName;
	private String productCategory;
	private String itemDescription;
	private double costOfItem;
	private final double salesTaxPercent = 0.0925;

	/**
	 * Use 2 HashMaps :
	 * 	1) mapRevPerCustomer 		: Maintains the revenue per customer
	 *  2) mapCustomerNameToCategory: Maintains a map from Customer Name to Category 
	 *  							  to the price
	 */
	HashMap<String,Double> mapRevPerCustomer;
	HashMap<String,HashMap<String,Double>> mapCustomerNameToCategory;

	public GenerateReports(){
		mapRevPerCustomer = new HashMap<String, Double>();
		mapCustomerNameToCategory = new HashMap<String,HashMap<String,Double>>();
	}

	/**
	 * Read the input file & tokenize to initialize HashMaps that are used to generate reports
	 * 
	 * @param fileName the input file that contains transaction
	 * @throws IOException
	 */
	public void readInputFile(String fileName) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(fileName));

		try{
			String line = br.readLine();

			while (line != null) {
				
				StringTokenizer st = new StringTokenizer(line,"|");
				
				while(st.hasMoreElements()){
					customerName = st.nextElement().toString();
					productCategory = st.nextElement().toString();
					itemDescription = st.nextElement().toString();
					costOfItem = Double.parseDouble(st.nextElement().toString());
				}

				double oldVal = 0.0;
				
				if(mapRevPerCustomer.get(customerName) != null)
					oldVal = mapRevPerCustomer.get(customerName);
				
				mapRevPerCustomer.put(customerName, oldVal+costOfItem);
				
				HashMap<String, Double> categoryToValue = mapCustomerNameToCategory.get(customerName);
				
				if( categoryToValue == null)
					categoryToValue = new HashMap<String, Double>();
				
				oldVal = 0.0;
				
				if (categoryToValue.get(productCategory) != null)
					oldVal = categoryToValue.get(productCategory);
				
				categoryToValue.put(productCategory, oldVal+costOfItem);
				
				mapCustomerNameToCategory.put(customerName, categoryToValue);
				
				line = br.readLine();
			}
		}
		
		catch(IOException ex){
			System.out.println(ex.getMessage());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		finally{
			br.close();
		}
	}
	
	/**
	 * Generates the total revenue per customer report using the customer to revenue hashmap
	 */
	public void generateTotalRevReport(){
		
		System.out.println("Total revenue by customer:");
		
		for (String key : mapRevPerCustomer.keySet()){
			System.out.println(key + "- $" + mapRevPerCustomer.get(key));
		}
	}
	
	/**
	 * Generates the expense by each category report using the two level map
	 */
	public void generateCategoryReport(){
		
		HashMap<String,Double> tempInnerCategoryExpenseMap;
		
		for(String key : mapCustomerNameToCategory.keySet()){
			System.out.println("Purchases by " + key + ":");
			
			tempInnerCategoryExpenseMap = mapCustomerNameToCategory.get(key);
			
			for (String innerKey : tempInnerCategoryExpenseMap.keySet()){
				System.out.println(innerKey + " - $" + tempInnerCategoryExpenseMap.get(innerKey));
			}
			System.out.println();
		}
	}
	
	/**
	 * Generates sales tax per customer report using one level map
	 */
	public void generatePerCustomerTaxReport(){
		
		System.out.println("Per customer sales tax:");
		
		for (String key : mapRevPerCustomer.keySet()){
			System.out.println(key + " - $" + mapRevPerCustomer.get(key)*salesTaxPercent);
		}
	}
	
	/**
	 * Generates sales tax collected per category per customer using two level map
	 */
	public void generatePerCategoryTaxReport(){
		
		HashMap<String,Double> tempInnerCategoryExpenseMap;
		
		for(String key : mapCustomerNameToCategory.keySet()){
			System.out.println("Sales tax generated per category by " + key + ":");
			
			tempInnerCategoryExpenseMap = mapCustomerNameToCategory.get(key);
			
			for (String innerKey : tempInnerCategoryExpenseMap.keySet()){
				System.out.println(innerKey + " - $" + tempInnerCategoryExpenseMap.get(innerKey) * salesTaxPercent);
			}
			System.out.println();
		}
	}
}
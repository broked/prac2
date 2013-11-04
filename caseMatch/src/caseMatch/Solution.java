package caseMatch;

import java.util.*;

// To execute Java, please define "static void main" on a class named Solution

class Solution {
	public static List<String> allPossiblePermute;


	public static void main(String[] args) {
		decodeFind("aBc");
	}

	public static int decode(String testEncStr) {
		if (testEncStr.equals("aBc")) {
			return 848662;
		} else {
			return -1;
		}
	}

	public static void decodeFind(String badEncStr) {

		char[] stringAsChar = badEncStr.toCharArray();

		for(int i =0,num = (int) Math.pow(2,stringAsChar.length); i<num ; i++){
			char[] allPermute = new char[stringAsChar.length];

			for(int j=0;j<stringAsChar.length;j++)
				allPermute[j] = checkCase(i,j) ? Character.toUpperCase(stringAsChar[j]) : Character.toLowerCase(stringAsChar[j]);

				System.out.println(allPermute);

				if(decode(new String(allPermute)) != -1) 
					System.out.println(decode(new String(allPermute)));
		}
	}

	public static boolean checkCase(int i, int j){
		return (i >> j &1) != 0;
	}

}

/*
# https://www.airbnb.com/rooms/848662
# https://www.airbnb.com/rooms/sreJsdf_kljJJ324hjkS_
# https://www.airbnb.com/rooms/srejsdf_kljjj324hjks_


# https://www.airbnb.com/rooms/aBc
# https://www.airbnb.com/rooms/abc

/*
 * you get this:  
 * a method that takes a string and returns the unhashed id if match is found, 
 * else null indicates no match


// decodeFind(srejsdf_kljjj324hjks_).should == 848662
 */
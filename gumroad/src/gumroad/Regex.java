package gumroad;

public class Regex
{
    public static boolean matchStrings(String pattern, String sample)
    {
        if (pattern.isEmpty())
        {
            return false;
        }
        else
        {
            if (pattern.charAt(0) == '*')
            {
                if (pattern.length() == 1)
                {
                    return true;
                }
                else
                {
                    return matchStar(pattern.substring(1), sample);
                }
            }
            
            else if (sample.isEmpty())
            {
                return false;
            }
            
		else if (pattern.charAt(0) == '.' || pattern.charAt(0) == sample.charAt(0))
            {
                
                if (pattern.length() == 1 && sample.length() == 1)
                {
                    return true;
                }
                                else
                {
                    return matchStrings(pattern.substring(1), sample.substring(1));
                }
            }
            else
            {
                return false; 
            }
        }
    }
    
    private static boolean matchStar(String pattern, String sample)
    {
        for (int i = 0; i < sample.length(); i++)
        {
            if (matchStrings(pattern, sample.substring(i)))
            {
                return true;
            }
            else
            {
                continue;
            }
        }

        return false;
    }
    
    public static void main(String[] args){
    	System.out.println(matchStrings("abc","abc"));
    	System.out.println(matchStrings("abc","ab"));
    	System.out.println(matchStrings("ab","abc"));
    	System.out.println(matchStrings("ab.","abc"));
    	System.out.println(matchStrings("ab.","ab"));
    	System.out.println(matchStrings("a.c","abc"));
    	System.out.println(matchStrings("ab*","abc"));
    	System.out.println(matchStrings("abc*","abc"));
    	System.out.println(matchStrings("*c","abc"));
    	System.out.println(matchStrings("a*c","abcdc"));
    }
}
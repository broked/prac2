package gumroad;

import java.util.*;

public class Challenge {

	public int findPivot(List<Integer> inputList){

		int listSum = 0;
		int leftSideSum = 0, rightSideSum = 0;
		int listSize = inputList.size();

		//calculate list sum
		for (int index=0;index<listSize;index++){
			listSum += inputList.get(index);
		}

		//find pivot
		if (listSize > 1){
			for (int index = 1;index < listSize; index++){
				leftSideSum += inputList.get(index-1);
				rightSideSum = listSum - leftSideSum - inputList.get(index);

				if(leftSideSum == rightSideSum)
					return index;
			}
		}

		return -1;
	}

	public static void main(String[] args){
		Challenge aa = new Challenge();
		List<Integer> lst = new ArrayList<Integer>();
		lst.add(1);
		lst.add(4);
		lst.add(1);
		lst.add(3);
		lst.add(2);
		lst.add(1);
		lst.add(3);
		int rt = aa.findPivot(lst);
		System.out.println(rt);
	}

}
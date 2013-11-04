package buildfs;

public class FS {
	
	private Block startNode;

	public FS(int numOfBlocks){
		
		startNode = new Block();
		
		if(numOfBlocks < 0){
			System.out.println("Invalid number");
			System.exit(0);
		}
		
		Block iterate = startNode;
		Block tempNode = startNode;
		
		int ctr = 0;
		while(ctr < numOfBlocks){
			tempNode = iterate.returnRefToPreviousBlock();
			iterate.setNextBlock(tempNode);
			iterate = tempNode;
			ctr++;
		}
	}
	
	public void dump(){
		Block iterateList = startNode;
		Block node;
		
		while(iterateList != null){
			node = iterateList.returnRefToPreviousBlock();
			System.out.println("["+new String(iterateList.getBlockVal()) + "] ");
			iterateList = node;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

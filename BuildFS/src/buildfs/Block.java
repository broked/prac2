package buildfs;


public class Block {
	
	private char blockVal[] = new char[8];
	private Block refNextBlock = new Block();
	
	public void setBlockVal(char[] setVal){
		
		if(setVal.length > 8)
			throw new RuntimeException();
		
		blockVal = setVal;
	}
	
	public char[] getBlockVal(){
		return blockVal;
	}
	
	public void setNextBlock(Block nextBlock){
		this.refNextBlock = nextBlock;
	}
	
	public Block returnRefToPreviousBlock(){
		return this.refNextBlock;
	}
	
}

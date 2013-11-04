package trees;

public class Trees {

	class Node {
		Node left, right;
		int val;
	}
	
	public void inorder(Node root){
		
		if(root == null)
			return;
		
		inorder(root.left);
		System.out.println(root.val);
		inorder(root.right);
	}
	
	public static void main(String[] args) {
		
	}

}

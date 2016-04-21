package backend;
import java.util.ArrayList;

import intermediate.Node;

/**
 * This class walks the binary tree printing each node
 * @author Usha
 *
 */
public class PrintBinaryTree
{
	/**
	 *  Print the Binary tree with proper parenthesis
	 * @param root Root node of the Binary tree 
	 * @param start Variable indicating whether the root node points to new tree or sub-tree
	 */
	public static void printBTree(Node root, boolean start)
	 {
		 if(start)
		 {
			 System.out.println("\n\nPrinting Binary tree from Backend...");
			 System.out.print("(");	
		 }
		 
		 if(root != null)
		  {
			 
			  if(root.left == null && root.right == null)
			  {
				  System.out.print(")");
			  }
			  else if(root.left.data == null)
			  {
				  System.out.print("(");
				  printBTree(root.left, false);	
			  }			  		  
			  else
			  {
				  if(root.left.data.equals("lambda")){
					  System.out.print("symTablerefrence" + root.symTablerefrence);
				  }
				  System.out.print(root.left.data);
				  
				  System.out.print(" ");
			  }
		  
			  printBTree(root.right, false);
		  }
		 else
			 System.out.println();
	  }
	
	
}

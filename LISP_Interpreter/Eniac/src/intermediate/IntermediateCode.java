package intermediate;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

import frontend.Parser;
import backend.PrintBinaryTree;

/**
 * This class builds the Intermediate code(Binary Tree)
 * @author Anumeha,Usha
 *
 */
public class IntermediateCode 
{
	Node root = null, mainRoot = null;
	Node current = null;
	int count = 0;
	Stack<Node> temp = new Stack<Node>();
	public  TreeMap<String, Object> symTableLambda = new TreeMap<String, Object>(); //added by anumeha
	public TreeMap<String, TreeMap<String, Node>> procNameAttribute = new TreeMap<String, TreeMap<String, Node>>();
	TreeMap<String, Object> lambdaEntryAttr = new TreeMap<String, Object>();
	int define_flag = 0;
	int lambda_flag = 0;
	int set_lambda_ref = 0;
	int in_use =1;
	String procedure_name = "";
		
	/**
	 * Getter for root node
	 * @return returns the root node of the tree
	 */
	public Node getRoot()
	{
		return mainRoot;
	}
	
	/**
	 * Create a new node with initial value 'null'
	 * @return returns newly created node
	 */
	public Node buildroot()
	{
		return new Node(null);
	}
	  	  
	/**
	 * Insert the given token in the left child of given node
	 * @param node current node
	 * @param token token to be inserted
	 * @return returns the right child of current node
	 */
	public Node insert(Node node, String token)
	{
		 node.left = new Node(token);
		 node.right = buildroot();
		 
		 return node.right;
	}
	
	
	/**
	 * Insert procedure name to top level stack
	 * @param node current node
	 * @param token token to be inserted
	 * 
	 */
	public void insertTopLevelSymTable(Node node, String token)
	{
		TreeMap<String, Node> proc = procNameAttribute.get(token);
		if(proc == null)
		{
			proc = new TreeMap<String, Node>();
		}
		proc.put("main_root_ref", current);
		procNameAttribute.put(token, proc);
		Parser.symTableTop.put(token, procNameAttribute.get(token));
		
		/*procNameAttribute.put("main_root_ref", current);
  		Parser.symTableTop.put(token, procNameAttribute);*/
  	  
	}
	
	
	/**
	 * Insert identifier name to lambda scope symbol table
	 * @param node current node
	 * @param token token to be inserted
	 * 
	 */
	public void insertLambdaSymTable(String token)
	{
  	  
  	  	//lambdaEntryAttr.put("symTable_ref",symTableLambda );
  	  	//System.out.println("tokenValue =" + token );
  	  	
  	  	symTableLambda.put(token, "");
  		
  	  
	}
	
	/**
	 * Insert identifier name to lambda scope symbol table
	 * @param node current node
	 * @param token token to be inserted
	 * 
	 */
	public void insertLambdaSymTableOnStack()
	{
		symTableLambda.put("level", "3");
		Parser.addSymolTableToStack("lambdaSymTable", symTableLambda);
  		
  	  
	}
	  
	/**
	 * Build the binary tree step-by-step
	 * @param token Token to be inserted into the tree
	 */
	public void buildTree(String token)
	{
		if(token.equals("("))
		{
			  count++;
		      if(count == 1)
			  {
		    	  root = buildroot();
				  current = root;
				  mainRoot = root;
			  }
			  else
			  {
				  temp.push(current);
				  current.left = buildroot();
			      current = current.left;
			  }
		  }
		  else if(token.equals(")"))
	      {	
			  if(!temp.empty())
				{
					current = temp.pop();
					if(current != null)
					{
						current.right = buildroot();
						current = current.right;
					}
				}
	      }
	      else
	      {
	    	  current = insert(current, token);
	    	  
	    	  
		  }
	  }
	
	
	
	
	  
	/**
	 * Reset the variables  
	 */
	public void reset()
	  {
		 root = null;
		 mainRoot = null;
		 current = null;
		 count = 0;
		 symTableLambda = new TreeMap<String, Object>();
	  }	
	
	
	/**
	 * Build the binary tree step-by-step
	 * @param token Token to be inserted into the tree
	 */
	public void anumeha_buildTree(ArrayList<String> tokenList)
	{
		for (String token : tokenList){
			if(token.equals("("))
			{
				  count++;
			      if(count == 1)
				  {
			    	  root = buildroot();
					  current = root;
					  mainRoot = root;
				  }
				  else
				  {
					  temp.push(current);
					  current.left = buildroot();
				      current = current.left;
				  }
			  }
			  else if(token.equals(")"))
		      {	
				  if(!temp.empty())
					{
						current = temp.pop();
						if(current != null)
						{
							current.right = buildroot();
							current = current.right;
						}
					}
		      }
		      else
		      {
		    	  
		    	  current = insert(current, token);
		    	  
		    	  
			  }
			
			//code for building stack tables added by anumeha
			
			 if(token.equals("define")){
				 define_flag = 1;
		  		 set_lambda_ref = 1;
			  		  
		     }
			 else if(define_flag == 1 && !token.equals("define") && !token.equals("(")){
				 procedure_name = token;
		  		 insertTopLevelSymTable(current, procedure_name);
		  		  
		  	 }
			 else if (define_flag == 1 && !token.equals("define") && token.equals("(")){
				 
				 TreeMap<String, Node> new_proc = procNameAttribute.get(procedure_name);
				if(new_proc == null)
				{
					new_proc = new TreeMap<String, Node>();
				}
				 new_proc.put("lambda_root_ref", current);
				 procNameAttribute.put(procedure_name, new_proc);
				 insertLambdaSymTableOnStack();
				 
				 current.symTablerefrence = symTableLambda;	
				 define_flag =0;
		  		  
		  	 }
			
			 if(token.equals("lambda")){
		  		 lambda_flag = 1;
		  		 continue;
		  		  
		  	 }
			 else if((lambda_flag == 1) && !token.equals("(") && !token.equals(")")){
				 if(symTableLambda.containsKey(token))
				 {
					 System.out.println(token + " is already defined.");
				 }
				 insertLambdaSymTable(token);
				
			 }
			 else if((lambda_flag == 1) && token.equals(")")){
				 //System.out.println("Printing final symboltable " + symTableLambda );
				 Parser.removeSymolTablefromStack("lambdaSymTable", symTableLambda);
				 lambda_flag = 0;
			 }
			 
			
		 }
	  }
	
	
}

package intermediate;

import java.util.TreeMap;

/**
 * Node class tells the structure of each node in the Binary Tree
 * @author Usha
 *
 */
public class Node 
{
	public Node left; 
    public Node right; 
    public String data; 
    public TreeMap<String, Object> symTablerefrence = null;
    
    
    /**
     * Constructor of Node class
     * @param newData Value to initialize data part of Node
     */
    public Node(String newData) 
    { 
      left = null; 
      right = null; 
      data = newData; 
      symTablerefrence = null;
      
    }

    /**
     * Constructor of Node class
     * @param newData Value to initialize data part of Node
     */
    public Node(String newData, TreeMap<String, Object> newsymTablerefrence ) 
    { 
      left = null; 
      right = null; 
      data = newData; 
      symTablerefrence = newsymTablerefrence;
    } 
}

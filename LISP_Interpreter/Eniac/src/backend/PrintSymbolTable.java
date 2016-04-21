package backend;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import frontend.Parser;
import intermediate.Node;

/**
 * This class prints the contents of Symbol table
 * @author Usha
 *
 */
public class PrintSymbolTable 
{
	/**
	 * Print the contents of symbol table in alphabetical order
	 * @param symbolTable Table whose contents are to be printed
	 */
	@SuppressWarnings("rawtypes")
	public static void printSTable(TreeMap<String, Object> symbolTable)
	{
		if(symbolTable.isEmpty())
		{
			System.out.println("\n\nNo elements to display in the symbol table...");
		}
		else
		{
			System.out.println("\n\nDisplaying contents of symbol table...");
					
			// Get a set of the entries 
			Set set = symbolTable.entrySet(); 
					
			// Get an iterator 
			Iterator i = set.iterator(); 
			
			// Display elements 
			while(i.hasNext())
			{ 
				Map.Entry me = (Map.Entry)i.next(); 
				System.out.print(me.getKey() + ": "); 
				System.out.println(me.getValue()); 
			} 
			System.out.println();	
		}
	}
	
	public static void printStackOfSymTables(){
		for(Map.Entry<String,Object> entry : Parser.stackOfStackTables.entrySet()){
			
			String key = entry.getKey();
			TreeMap<String, Object> value = (TreeMap<String, Object>) entry.getValue();
			
			if(key.equals("level1Symboltable")){
				for(Map.Entry<String,Object> value_entry : value.entrySet()){
					
					TreeMap<String, Object>value_entry_value = (TreeMap<String, Object>) value_entry.getValue();
					
					
					for(Map.Entry<String,Object> value_entry_entry : value_entry_value.entrySet()){
						
						if(value_entry_entry.getKey().equals("main_root_ref")){
							continue;
						}
						else{
							Node tree_value = (Node) value_entry_entry.getValue();
							boolean start = true;
							//PrintBinaryTree.printBTree(tree_value, start);
						}
					}
				}
			}


		}
	}
}

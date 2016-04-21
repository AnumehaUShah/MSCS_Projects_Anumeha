package frontend;

import backend.Execute;
import backend.PrintBinaryTree;
import backend.PrintSymbolTable;
import backend.Execute;

import java.util.*;

/**
 * The Parser class requests the Scanner to generate tokens and builds the Symbol table and
 * Intermediate code with the received tokens  
 * @author Anumeha, Usha
 */
public class Parser 
{
	frontend.Scanner scanner;
	static intermediate.IntermediateCode interCode = new intermediate.IntermediateCode();
	static intermediate.SymbolTable SymbolTableObj = new intermediate.SymbolTable();
	static TreeMap<String, Object> symTable = SymbolTableObj.createSymbolTable();
	static TreeMap<String, Object> symTableGlobal = SymbolTableObj.createSymbolTable();
	public static TreeMap<String, Object> symTableTop = SymbolTableObj.createSymbolTable();
	public static TreeMap<String, Object> symTableLambda = SymbolTableObj.createSymbolTable();
	public static TreeMap<String, Object> stackOfStackTables = new TreeMap();
	
	
	
	
	
	/**
	 * Constructor for the Parser class to initialize the scanner
	 * @param scanner Scanner from which tokens are received
	 */
	public Parser(frontend.Scanner scanner)
	{
		this.scanner = scanner;
		addGlobalSymbolTable();
	}
	
	public Parser() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Invoke the scanner to generate tokens
	 */
	public void callScanner()
	{
		scanner.generateTokens();
	}
		
	/**
	 * Populate the symbol table
	 * @param identifier Token to be added to Symbol table
	 */
	static void addToSymbolTable(String identifier)
	{
		symTable.put(identifier, null);
	}

	/**
	 * When a token is received from Scanner, put it in the Binary Tree.
	 * @param token Token to be placed in Binary tree
	 */
	static void receiveToken(String token)
	{
		interCode.buildTree(token);
	}
	
	/**
	 * At the end of each top level list display the contents of Binary tree and 
	 * Symbol table and reset them for the next list
	 */
	static void displayTable()
	{
		PrintBinaryTree.printBTree(interCode.getRoot(), true);
		PrintSymbolTable.printSTable(symTable);
		PrintSymbolTable.printSTable(symTableGlobal);
		interCode.reset();
		symTable.clear();
		symTableGlobal.clear();
	}
	
	/**
	 * add data to symbol table to stack of stacks as soon as parser is initialized.
	 */
	static void addGlobalSymbolTable()
	{
		symTableGlobal.put("level?", "0");
		symTableGlobal.put("null?", "");
		symTableGlobal.put("cond", "");
		symTableGlobal.put("#f", "");
		symTableGlobal.put("equal?", "");
		symTableGlobal.put("car", "");
		symTableGlobal.put("#t", "");
		symTableGlobal.put("else", "");
		symTableGlobal.put("cdr", "");
		symTableGlobal.put("'", "");
		symTableGlobal.put("and", "");
		symTableGlobal.put("cons", "");
		symTableGlobal.put("not", "");
		symTableGlobal.put("symbol?", "");
		symTableGlobal.put("integer?", "");
		symTableGlobal.put("boolean?", "");
		symTableGlobal.put("string?", "");
		symTableGlobal.put("pair?", "");
		symTableGlobal.put("cadr", "");
		symTableGlobal.put("or", "");
		symTableGlobal.put("real?", "");
		symTableGlobal.put("char?", "");
		symTableGlobal.put("append", "");
		symTableGlobal.put("list", "");
		symTableGlobal.put("cddr", "");
		symTableGlobal.put("let*", "");
		symTableGlobal.put("let", "");
		symTableGlobal.put(">", "");
		symTableGlobal.put("if", "");
		symTableGlobal.put("+", "");
		
		
		addSymolTableToStack("globalSymbolTable", symTableGlobal);
		addSymolTableToStack("level1Symboltable", symTableTop);
		
		
		
		
		
	}
	
	/**
	 * add symbol table to ArrayList as soon as parser is initialized.
	 */
	public static void addSymolTableToStack(String name, TreeMap<String, Object> symbolTable)
	{
		stackOfStackTables.put(name, symbolTable);
	}
	
	/**
	 * add symbol table to ArrayList as soon as parser is initialized.
	 */
	public static void removeSymolTablefromStack(String name,TreeMap<String, Object> symbolTable)
	{
		stackOfStackTables.remove(name);
	}
	
	/**
	 * add symbol table to ArrayList as soon as parser is initialized.
	 */
	public static void anumeha_processSchemeExpressionList(ArrayList<String> tokenList, int no_list, ArrayList<String> tokenListWithParamSpaces)
	{   
		String procedure_name = tokenList.get(1);
        if(symTableTop.containsKey(procedure_name)){
            Execute.execute(procedure_name, tokenList, stackOfStackTables, tokenListWithParamSpaces);
        }
		
		/*for(String token : tokenList){
			if(symTableTop.containsKey(token)){
				Execute.execute(token, tokenList, stackOfStackTables);
			}
		}*/
		
		//System.out.println(tokenList);
		interCode.anumeha_buildTree(tokenList);
		System.out.println("> ");
		//PrintBinaryTree.printBTree(interCode.getRoot(), true);
		//PrintSymbolTable.printStackOfSymTables();
		interCode.reset();
		//System.out.println("Symbol Table values are" + symTableTop);
		//PrintSymbolTable.printStackOfSymTables();
	}
	
	/**
	 * Print the symbol table stack.
	 */
	public static void anumeha_printSymTableStack()
	{
		
		PrintSymbolTable.printStackOfSymTables();
	
	}
	
	
}



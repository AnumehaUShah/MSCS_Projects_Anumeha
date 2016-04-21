package frontend;

import java.util.*;
import frontend.Parser;


/**
 * The Scanner class reads the input file and generates tokens
 * @author Anumeha, Usha
 *
 */
public class Scanner 
{
	StringBuilder token;
	ArrayList<String> keywords;
	ArrayList<String> specialSymbols;
	ArrayList<String> tokenList = new ArrayList<String>(); //added by anumeha
	ArrayList<String> tokenListWithParamSpaces = new ArrayList<String>(); //added by Umang
	int noOfOpenBrace = 0, noOfCloseBrace = 0;
	String numPattern = "[0-9]+[.]*[0-9]*";
	private java.util.Scanner inFileScan; //added by anumeha
	int no_list = 1;
	
	/**
	 * Constructor for Scanner 
	 */
	public Scanner()
	{
		fillKeywords();
	}
	
	/**
	 * Read the input data and process each input line
	 */
	public void generateTokens()
	{
		inFileScan = new java.util.Scanner(System.in);
		String currentLine;
		
		inFileScan.useDelimiter("\n");
		while(inFileScan.hasNextLine())
		{
			currentLine = inFileScan.nextLine();
			//System.out.println("Line read by Scanner: " + currentLine);
			//currentLine.trim();
			anumeha_process(currentLine);
		}
		
	}
	
	/**
	 * Split the input line into separate tokens
	 * @param line Input line to be processed
	 */
	private void process(String line)
	{
		String tmpInput = line;
		int len = tmpInput.length();
		token = new StringBuilder(len);
		
		for(int i = 0; i < len; i++)
		{
			if(tmpInput.charAt(i) == ';')
				return;
			
			if((tmpInput.charAt(i) == ' ') || (tmpInput.charAt(i) == '\n') 
					|| (tmpInput.charAt(i) == '\t') )
			{
				printToken(token);
				token.delete(0, len);
			}
			else if(tmpInput.charAt(i) == '(')
			{
				printToken(token);
				token.delete(0, len);
				noOfOpenBrace++;
				
				token.append(tmpInput.charAt(i));
				printToken(token);
				token.delete(0, len);
			}
			else if(tmpInput.charAt(i) == ')')
			{
				printToken(token);
				token.delete(0, len);
				noOfCloseBrace++;
				
				token.append(tmpInput.charAt(i));
				printToken(token);
				token.delete(0, len);	
			}
			else
			{
				token.append(tmpInput.charAt(i));
			}
			
			if((noOfOpenBrace == noOfCloseBrace) && (noOfOpenBrace != 0))
			{
				Parser.displayTable();
				noOfOpenBrace = 0;
				noOfCloseBrace = 0;				
			}							
		}
	}
	
	/**
	 * Display each token and its type
	 * @param token Token to be displayed
	 */
	private void printToken(StringBuilder token)
	{		
		if(token.length() > 0)
		{
			String tmpToken = token.toString();
			
			if(tmpToken.matches(numPattern))
				System.out.println(tmpToken + " -> Number");
			else if (keywords.contains(tmpToken))
				System.out.println(tmpToken + " -> Keyword");
			else if (specialSymbols.contains(tmpToken))
				System.out.println(tmpToken + " -> Special Symbol");
			else
			{
				System.out.println(tmpToken + " -> Identifier");
				Parser.addToSymbolTable(tmpToken);
			}		
			Parser.receiveToken(tmpToken);
		}
	}
	
	/**
	 * Initialize Keywords and Special Symbols
	 */
	private void fillKeywords()
	{
		keywords = new ArrayList<String>();
		keywords.add("and");
		keywords.add("begin");
		keywords.add("begin0");
		keywords.add("break-var");
		keywords.add("case");
		keywords.add("cond");
		keywords.add("cons");		
		keywords.add("cycle");		
		keywords.add("define");
		keywords.add("delay");
		keywords.add("delay-list-cons");
		keywords.add("do");
		keywords.add("else");
		keywords.add("extend-syntax");
		keywords.add("for");
		keywords.add("freeze");
		keywords.add("if");
		keywords.add("lambda");
		keywords.add("let");
		keywords.add("letrec");
		keywords.add("let*");
		keywords.add("macro");
		keywords.add("object-maker");
		keywords.add("not");
		keywords.add("or");
		keywords.add("quote");
		keywords.add("repeat");
		keywords.add("safe-letrec");
		keywords.add("set!");
		keywords.add("stream-cons");
		keywords.add("variable-case");
		keywords.add("while");
		keywords.add("wrap");	
		
		specialSymbols = new ArrayList<String>();
		specialSymbols.add("(");
		specialSymbols.add(")");
		specialSymbols.add("[");
		specialSymbols.add("]");
		specialSymbols.add("{");
		specialSymbols.add("}");
		specialSymbols.add(";");
		specialSymbols.add(",");
		specialSymbols.add(".");
		specialSymbols.add("\"");
		specialSymbols.add("'");
		specialSymbols.add("#");
		specialSymbols.add("\\");	
	}
	
	
	/**
	 * Split the input line into separate tokens and create an ArrayList of all the tokens
	 * @param line Input line to be processed
	 */
	private void anumeha_process(String line)
	{
		String tmpInput = line;
		int len = tmpInput.length();
		token = new StringBuilder();
		
		for(int i = 0; i < len; i++)
		{
			Character c= new Character(tmpInput.charAt(i));
			//System.out.println(c);
			
			if((c == ' ') || (c == '\n') || (c == '\t'))
			{   //System.out.println("I am in space condition");
				if(token.length() != 0){
					tokenList.add(token.toString());
					tokenListWithParamSpaces.add(token.toString());
					int token_len = token.length();
					token.delete(0, token_len);
					//System.out.println(token);
				}
				if(noOfOpenBrace == (noOfCloseBrace + 1))
				{
					tokenListWithParamSpaces.add(c.toString());
				}				
			}
			else if(c == '(')
			{
				//System.out.println("I am in ( condition");
				noOfOpenBrace++;
				tokenList.add(c.toString());
				tokenListWithParamSpaces.add(c.toString());
				
				
			}
			else if(c == ')')
			{
				//System.out.println("I am in ) condition");
				if(token.length() != 0){
					tokenList.add(token.toString());
					tokenListWithParamSpaces.add(token.toString());
					int token_len = token.length();
					token.delete(0, token_len);
				}
				noOfCloseBrace++;
				tokenList.add(c.toString());
				tokenListWithParamSpaces.add(c.toString());
			}
			else if(c == '\''){
				//System.out.println("I am in ' condition " + c.toString());
				tokenList.add(c.toString());
				tokenListWithParamSpaces.add(c.toString());
			}
			else if(i == len-1){
				token.append(c);
				tokenList.add(token.toString());
				tokenListWithParamSpaces.add(c.toString());
				int token_len = token.length();
				token.delete(0, token_len);
			}
			
			else
			{
				//System.out.println("I am in append condition" + (int) c);
				token.append(c);
			}
			
			if(c == ')'){
				if((noOfOpenBrace == noOfCloseBrace) && (noOfOpenBrace != 0))
				{	
					Parser.anumeha_processSchemeExpressionList(tokenList,no_list, tokenListWithParamSpaces);
					no_list ++;
					noOfOpenBrace = 0;
					noOfCloseBrace = 0;
					tokenList.clear();
					tokenListWithParamSpaces.clear();
					
				}
			}
		}
	}
}

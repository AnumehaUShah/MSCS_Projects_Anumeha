import frontend.Scanner;
import frontend.Parser;

/**
 * This is the main class to test the interpreter for Scheme 
 * @author Usha
 *
 */
public class CompilerTest {

	public static void main(String[] args) 
	{
		/**
		 * Create new Scanner and Parser objects and invoke the parser
		 */
		try{
		Scanner scannerObj = new Scanner();
		
		Parser parse = new Parser(scannerObj);		
		parse.callScanner();
		}
		catch(Exception ex){
			System.out.println("Please run again provide the input correctly. First the first functions and then press enter");
			System.out.println("or you can input all the function together");
			
		}

	}

}

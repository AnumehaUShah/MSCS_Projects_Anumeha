/****Common Modulus Attack on RSA By*****
******Anumeha Shah
******Neha Rajkumar
*****Veena Reddy
****************************************/

import java.util.Scanner;
import java.math.BigInteger;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;


public class CommonModulusAttack {
	
/*************method to find a and b such that e1*a + e2*b =1 ******************/
	static int[] extendedEuclidean(int e1, int e2) {
	      if (e2 == 0){
	    	 int[] exEucArr = new int[] { e1, 1, 0 };
	         return exEucArr;
	      }
	      
	      //call the extendedEuclidean method recursively 
	      int[] recursionArr = extendedEuclidean(e2, e1 % e2);
	      int d = recursionArr[0];
	      int a = recursionArr[2];
	      int b = recursionArr[1] - (e1 / e2) * recursionArr[2];
	      int[] finalexEucArr = new int[] { d, a, b };
	      return finalexEucArr;
	   }
	
/*************method to convert the plain text into decimal while encrypting ******************/
	static String convertStringToDecimal(String text){
		
		//base 64 encoding
		byte[] B64text=text.getBytes();
		byte[] encodedText=Base64.encodeBase64(B64text);
		String strText=new String(encodedText);
		
		String encryptedBase64 = "Original Message in Base 64 encoded format = \n" + strText;
	 	JOptionPane.showMessageDialog(null, encryptedBase64);
	 	System.out.println(encryptedBase64 +"\n");
		
		//convert string to decimal
		String textToDecimal = "";
	    
		for(int i=0;i<strText.length();i++)
		{
				
	         char cur = strText.charAt(i);
	         int val = (int) cur;
	         if(i == 0){
	        	 textToDecimal = "" + val;
	         }
	         else{
	        	 textToDecimal = "" + textToDecimal + val;
	         }
	       
		}
		
		
		return textToDecimal;
		
	}
	
/************* Method to convert From Decimal to String while decrypting ***************/
static String convertDecimalToString(String decimalToStr)
{
		try{
			int k =0;
		 	String str = "";
		 	//to convert from decimal to char
		 
		 	while(k < decimalToStr.length() ){
		 		
		 		String ch= decimalToStr.charAt(k)+""+decimalToStr.charAt(k+1);
		 		int ini = Integer.parseInt(ch);
		 		String strascInt="";
		 		
		 		if(ini <= 13){
		 			ch= decimalToStr.charAt(k)+""+decimalToStr.charAt(k+1)+""+decimalToStr.charAt(k+2);
		 			ini = Integer.parseInt(ch);
		 			char ascInt = (char)ini;
		 			strascInt = ""+ascInt;
		 			k = k+3;
		 		}
		 		else{
		 			ch= decimalToStr.charAt(k)+""+decimalToStr.charAt(k+1);
		 			ini = Integer.parseInt(ch);
		 			char ascInt = (char)ini;
		 			strascInt = ""+ascInt;
		 			k= k+2;
		 		}
		 		
		 		str = str.concat(strascInt);
		 		
		 		
		 		
		 	}
		 	
		 	String decryptedBase64 = "Decrypted Message After Performing Common Modulus Attack in base 64 encoded Format = \n" + str;
		 	JOptionPane.showMessageDialog(null, decryptedBase64);
		 	System.out.println(decryptedBase64 +"\n");
		 	
		 
		 	//Base64 decoding
		 	byte[] byteArray = Base64.decodeBase64(str.getBytes());
		 	String decodedString = new String(byteArray);			
		 	return decodedString;
		}
		catch(Exception ex){
			return decimalToStr;
		}
	
	
	 
		
}//End convertDecimalToString method

	
/*************method to encrypt using RSA algorithm************************************/
	static BigInteger encryptRsa(String text, BigInteger N, int e){
		BigInteger Cipher = new BigInteger(text);
		String eStr = "" + e;
		BigInteger eBig = new BigInteger(eStr);
		BigInteger C1  = Cipher.modPow(eBig, N);
		return C1;
		
		
	}
	
/*************method to find the original message based on extended euclidean algorithm******/
	static BigInteger decryptRsa(BigInteger Cipher1, BigInteger Cipher2, BigInteger bigA, BigInteger bigB, BigInteger N){
		BigInteger Msg;
		
		BigInteger Ca= Cipher2.modPow(bigB, N);
		BigInteger Cb= Cipher1.modPow(bigA, N);
	    
	    
	    BigInteger C = Ca.multiply(Cb);
	    Msg = C.mod(N) ;
	   
	    
		return Msg;
		
		
	}

/**********************************main method ***************************************/
	 public static void main(String[] args) {
		   
		 	BigInteger N = new BigInteger("402394248802762560784459411647796431108620322919897426002417858465984510150839043308712123310510922610690378085519407742502585978563438101321191019034005392771936629869360205383247721026151449660543966528254014636648532640397857580791648563954248342700568953634713286153354659774351731627683020456167612375777");
		 	BigInteger C1;
		 	BigInteger C2;
		 	int e1;
		 	int e2 ;
		 	int a;
		 	int b;
		 	String ptext;
		 	
		 	String ptextToDecimal;
		 	
		 	//Scanner in = new Scanner(System.in);
		 	ptext =JOptionPane.showInputDialog(null,"Enter the invitation message");
		 	System.out.println("Invitation Message = "+ ptext + "\n");
		 	
		 	e1 = new Integer(JOptionPane.showInputDialog(null,"Enter the value of first exponent e1"));
		 	System.out.println("Exponent e1 = "+ e1 + "\n");
		 	
		 	e2 = new Integer(JOptionPane.showInputDialog(null,"Enter the value of Second exponent e2 such that gcd(e1,e2) = 1 to satisfy the comman modulus attck"));
		 	System.out.println("Exponent e2 = "+ e2+ "\n");
		 	
		 	//convert the plain text to decimal 
		 	ptextToDecimal = convertStringToDecimal(ptext);
		 	
		 	String msgInDecimalFormt  = "Original Message in Decimal format  = " + ptextToDecimal;
		 	JOptionPane.showMessageDialog(null, msgInDecimalFormt);
		 	System.out.println(msgInDecimalFormt+ "\n");
		 
		 	
		 	//Create ciphers based on RSA algorithm using same N and different exponents e1 and e2
		 	C1 = encryptRsa(ptextToDecimal, N, e1);
		 	C2 = encryptRsa(ptextToDecimal, N, e2);
		 	
		 	String Cipher1 = "Value of Cipher1 calculated as M^e1 mod N = \n" + C1;
			String Cipher2 = "Value of Cipher2 calculated as M^e2 mod N = \n" + C2;
			
		 	JOptionPane.showMessageDialog(null, Cipher1);
		 	JOptionPane.showMessageDialog(null, Cipher2);
		 	System.out.println(Cipher1 + "\n");
		 	System.out.println(Cipher2 + "\n");
		 	
		 	
		 	//Performing Common modulus attack
		 	JOptionPane.showMessageDialog(null, "Performing Common Modulus Attack");
			System.out.println("Performing Common Modulus Attack"+ "\n");
		 	
		 	//Using extendedEuclidean method Find a and b such that e1*a + e2*b =1
		 	
		 	int[] exEucArr = extendedEuclidean(e1, e2);
		    a = exEucArr[1];
		    b = exEucArr[2];
		    
		    String val_a_and_b = "Value of a and b such that e1*a + e2*b =1.\n a= " + a + " b = " +b;
		    JOptionPane.showMessageDialog(null, val_a_and_b);
		    System.out.println(val_a_and_b+ "\n");
		    
		    
		    String aToString = "" + a;
		    String bToString = "" + b;
		    
		    BigInteger bigA= new BigInteger(aToString);
		    BigInteger bigB= new BigInteger(bToString);
		 	
		    //find the  message m based on common modulus attck
		 	BigInteger m = decryptRsa(C1, C2, bigA, bigB, N);
		 	
		 	String decodedMessageInDecimal = "Now message calculated based on formula C1^a mod N * C2 ^ b mod N  in decimal  = \n" + m;
		 	System.out.println(decodedMessageInDecimal+ "\n");
		 	JOptionPane.showMessageDialog(null, decodedMessageInDecimal);
		 
		 	
		 	String decimalToStr = ""+ m;
		 	
		 	//method to convert decimal to String 
		 	String decodedMessage = convertDecimalToString(decimalToStr);
		 	String decodedMessageshow = "Decrypted Message After Performing Common Modulus Attack in Decimal = \n" + decimalToStr;
		 	JOptionPane.showMessageDialog(null, decodedMessageshow);
		 	System.out.println(decodedMessageshow +"\n");
		 	
		 	if(decodedMessage.equals(ptext)){
		 		String Result = "Orignal Message matches with the decrypted message \n"+
		 				"Original Message = " + ptext +"\n"+
		 				"Decrypted Message = " + decodedMessage;
		 		JOptionPane.showMessageDialog(null, Result);
		 		System.out.println(Result);
		 		
		 	}
		 	else if(decodedMessage.equals(decimalToStr)){
		 		String Result = "Orignal Message does not matches with the decoded message as gcd("+e1+","+e2+") != 1\n"+
		 				"Original Message in decimal = " + ptextToDecimal +"\n"+
		 				"Decrypted Message in decimal = " + decodedMessage;
		 		JOptionPane.showMessageDialog(null, Result);
		 		System.out.println(Result);
		 		
		 	}
		 	else {
		 		
		 		String Result = "Orignal Message does not matches with the decrypted messageas gcd("+e1+","+e2+") != 1\n"+
		 				"Original Message = " + ptext +"\n"+
		 				"Decrypted Message = " + decodedMessage;
		 		JOptionPane.showMessageDialog(null, Result);
		 		System.out.println(Result);
		 		
		 	}
		 	
		 	
		 	
		 	
		    
		    
		   
	      
	   }

}


/****Cube Root Attack on RSA By*****
 ******Anumeha Shah
 ******Neha Rajkumar
 *****Veena Reddy
 ****************************************/
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.math.*;



public class ChineseRemainderTheorem {
	

	static final BigInteger TWO = new BigInteger("2");
	static final BigInteger THREE = new BigInteger("3");


/*************method to convert the plain text into decimal while encrypting ******************/
static String convertStringToDecimal(String text){
	
	//base 64 encoding
	byte[] B64text=text.getBytes();
	byte[] encodedText=Base64.encodeBase64(B64text);
	String strText=new String(encodedText);
	
	String encryptedBase64 = "Original Message in Base 64 encoded format = \n" + strText;
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

/*************method to encrypt using RSA algorithm************************************/
static BigInteger encryptRsa(String text, BigInteger N, int e){
	BigInteger Cipher = new BigInteger(text);
	String eStr = "" + e;
	BigInteger eBig = new BigInteger(eStr);
	BigInteger C1  = Cipher.modPow(eBig, N);
	return C1;
	
}

//Method to find out decrypt 1+2+3MOD(N1,N2,N3)//
static void ChineseRemainder(BigInteger C1,BigInteger C2,BigInteger C3,BigInteger N1,BigInteger N2,BigInteger N3)
{
	//part1//
	BigInteger p = N2.multiply(N3);
	BigInteger q = p.modInverse(N1);
	BigInteger r = p.multiply(q);
	BigInteger s = C1.multiply(r);
	//part2//
	BigInteger a = N1.multiply(N3);
	BigInteger b = a.modInverse(N2);
	BigInteger c = a.multiply(b);
	BigInteger d = C2.multiply(c);
	//part3//
	BigInteger t = N1.multiply(N2);
	BigInteger u = t.modInverse(N3);
	BigInteger v = t.multiply(u);
	BigInteger w = C3.multiply(v);
	BigInteger o = s.add(d);
	BigInteger l = o.add(w);
	BigInteger x = t.multiply(N3);
	BigInteger y = l.mod(x);
	//cuberoot//
	System.out.println("test:"+test);	String M = cbrt(y).toString();
	System.out.println("");
    System.out.println("DECRYPTED MESSAGE IN DECIMAL FORMAT:"+ M);
    String g = M.toString();
    String decodeMessage = convertDecimalToString(g);
    String output = decodeMessage.toString();
    System.out.println("final output:"+ output);

}
public static BigInteger cbrt(BigInteger n) {
	
    BigInteger guess = n.divide(BigInteger.valueOf((long) n.bitLength() / 3));
    boolean go = true;
    int c = 0;
    BigInteger test = guess;
    while (go) {
        BigInteger numOne = n.divide(guess.multiply(guess));
        BigInteger numTwo = guess.multiply(TWO);
        guess = numOne.add(numTwo).divide(THREE);
        if (numOne.equals(numTwo)) {
            go = false;
        }
        if (guess.mod(TWO).equals(BigInteger.ONE)) {
            guess = guess.add(BigInteger.ONE);
        }
        //System.out.println(guess.toString());
        c++;
        c %= 5;
        if (c == 4 && (test.equals(guess))) {
            return guess;
        }
        if (c == 2) {
            test = guess;
        }
        if (c == 3) {
            guess = guess.add(BigInteger.ONE);
        }
    }

    if ((guess.multiply(guess)).equals(n)) {
        return guess;
    }
    return guess.add(BigInteger.ONE);

}
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
		 	
		 	String decryptedBase64 = "Decrypted Message After Performing n base 64 encoded Format = \n" + str;
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



public static void main(String[] args) throws IOException
{
	BigInteger N1 = new BigInteger("514745167025222387434132377137056715954750729807151447929894289695587285793889099978536904494455862473045694392353612260528582074521711735864082380505874261026769465596315849668245703081452047808798727647904141791488099702631575692170683102622471798376397440600292225038412176681344166204027842724877162681931");
	BigInteger N2 = new BigInteger("332459552799915544356022641605448137617079921391832222557892949808060953028449422328281413629912335051440744955455010851012308918294549765005480121061697711447087615327860789708246235156912421474047484838827777697938563515420810650393553528058831317409340577149233554235346445890238642955390137465511286414033");
	BigInteger N3 = new BigInteger("665701912162243069059653781669230805473457427767514323262762891771122352328706695409103713864384833437438648120217615990765220365745013739246022203593234785338178963805463643869398986119431772931646042972240277833431035018628949924813463553419243108837309078316455504749755062865258063926243606206806549969161");
	BigInteger C1,C2,C3;
	int e=3;
	String ptext;
	System.out.println("Enter the invitation message M\n");
	BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
    ptext = inp.readLine();
	
 	//convert the plain text to decimal 
 	String ptextToDecimal = convertStringToDecimal(ptext);
 	
 	String msgInDecimalFormt  = "Original Message in Decimal format  = " + ptextToDecimal;
 	System.out.println(msgInDecimalFormt+ "\n");
    C1 = encryptRsa(ptextToDecimal, N1, e);
    C2 = encryptRsa(ptextToDecimal, N2, e);
    C3 = encryptRsa(ptextToDecimal, N3, e);
    System.out.println("Encrypted message C1:"+ C1);
    System.out.println("Encrypted message C2:"+ C2);
    System.out.println("Encrypted message C3:"+ C3);
    ChineseRemainder(C1,C2,C3,N1,N2,N3);
   
}
}
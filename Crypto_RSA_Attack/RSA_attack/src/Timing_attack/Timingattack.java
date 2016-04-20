//The program chooses three random cipher text values.....

/****Timing Attack on RSA By*****
 ******Anumeha Shah
 ******Neha Rajkumar
 *****Veena Reddy
 ****************************************/
package Timing_attack;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.lang.NullPointerException;
import java.math.BigInteger;
import java.util.Scanner;

import javax.swing.JOptionPane;

	public class Timingattack {

		private static final BigInteger C1 = null;
		private static long tot;
		private static int j1;
		private static int[] art1=new int[3];

		//The function encrypts the message send by user
		public static BigInteger encrypt(BigInteger M,BigInteger N,BigInteger e)
		{
			BigInteger ciphertext = M.modPow(e, N);
		    return ciphertext;
			
		}
		
		//the function decrypts the message send by user
		public static long decrypt(BigInteger cipher,BigInteger d,BigInteger N)
		{
			BigInteger s;
			BigInteger ss;
			BigInteger sc=null;
			s = cipher;
			int d1=d.intValue();
			int temp,num=d1;
			int i=0;
			int[] arr1 = new int[3];
			int[] arr2 = new int[3]; //binary array for d
			int[] tims=new int[12];
			do
			{			
				temp=num%2;
				arr1[i]=temp;
				num=num/2;
				i=i+1;
			}while(num>0);
			int g3=i-1;
			int k=0;
			while(g3!=-1)
			{
				
		    arr2[k]=arr1[g3];
		    k++;
		    g3=g3-1;
			}
			long nano1;
			nano1=0;
		    nano1=System.nanoTime();
			//Displaying the value of d in binary
			System.out.println("The value of d in binary is:");
			for(int p=0;p<arr2.length;p++)
			{
				System.out.println(arr2[p]);
			}
			
			int i1;
			for(i1=0;i1<arr2.length;i1++)
		    {
			ss= s.multiply(s).mod(N);
			s=ss;
			if (arr1[i1]==1){
			
				
				sc = s.multiply(cipher).mod(N);
				s=sc;
				
			}
		    }
			return System.nanoTime()-nano1;
			}
		
		//the function measures the time difference to check for the 1st bit
		public static void check1(int tims[],int t1) 
		{
		
			art1[j1]=t1-tims[0]; 
			j1++;
			System.out.println(Arrays.toString(art1));
		}
		
		//the function measures the time difference to check for the 2nd bit
		public static void check2(int tims[],int t1)
		{
		
			art1[j1]=t1-(tims[0]+tims[1]); 
			j1++;
			System.out.println(Arrays.toString(art1));
		}
		
		public static void main(String args[]) throws NullPointerException
		{
			Scanner scan=new Scanner(System.in);
			int ch;
			int k1=0;
			int temp;
			j1=0;
			int i;
			int[] arr1 = new int[3];
			int[] arr2 = new int[3];
			int[] tims=new int[12];
			int var2[]=new int[7];
			do
			{
			System.out.print("The user");
			System.out.print("\n");
			String g="";
		    g=JOptionPane.showInputDialog(null,"Enter the invitation");
			System.out.println("Enter the value of N");
			BigInteger N=null;
			N=scan.nextBigInteger();
			System.out.println("Enter the value of e");
			BigInteger e=null;
			e=scan.nextBigInteger();
			BigInteger M=null;
			M=new BigInteger(g);
			BigInteger cipher=null;
			cipher=encrypt(M,N,e);
			BigInteger d=null;
			System.out.println("the cipher text is" +cipher);
			System.out.println("Enter the value of d");
			d=scan.nextBigInteger();
			long t=0;
			t=decrypt(cipher,d,N);//Measuring the decryption time of user 
			System.out.println("The time required for decrypting the ciphertext: " +cipher.toString()+ "for the the user is: " +t);
			//Attackers method
			System.out.println("\n");
			System.out.println("Attacker measuring the time");
			
			int num=0;
			BigInteger C1=null;
			System.out.println("enter the cipher text");
			C1=scan.nextBigInteger();
			int d1=0;
			System.out.println("Enter the value of d");
			d1=scan.nextInt();
			num=d1;
			BigInteger z;
			BigInteger zz;
			BigInteger zC=null;
			z = C1;
			
			i=0;
			
			do
			{			
				temp=num%2;
				arr1[i]=temp;
				num=num/2;
				i=i+1;
			}while(num>0);
			int g3=i-1;
			int k=0;
			while(g3!=-1)
			{
				
		    arr2[k]=arr1[g3];
		    k++;
		    g3=g3-1;
			}
			long nano1;
			
			//Displaying the value of d in binary
			System.out.println("The value of d in binary is:");
			for(int p=0;p<arr2.length;p++)
			{
				System.out.println(arr2[p]);
			}
			
			int i1;
			for(i1=0;i1<arr2.length;i1++)
		    {
				nano1=0;
		    nano1=System.nanoTime(); //to get the time for each iteration
			zz= z.multiply(z).mod(N);
			z=zz;
			if (arr1[i1]==1){
			
				zC = z.multiply(C1).mod(N);
				z=zC;
				
			}
			long tot=System.nanoTime()-nano1;
			
			System.out.println("The time of iteration for the "+i1+":th bit is: " +tot);
		
			int timing=(int)tot;
			tims[i1]=timing;
	       tot=0;
		   }
	       //displaying the timings
	       System.out.print("the time array");
	       System.out.print(Arrays.toString(tims));
		
	       int t1=(int)t;
	       int choice=0;
	       System.out.print("\n");
	       
	       System.out.println("Which bit to check give 1 for bit **1** and 2 for bit **2** ");
	       
	       //For each value of d three cipher texts are selected in random
	       System.out.println("\t");
	       choice=scan.nextInt();
	       if (choice==1)
	       {
		   check1(tims,t1);
	       }
	       else if(choice==2)
	       {
	    	   check2(tims,t1);
	       }
	       System.out.print("do u wish to continue??press 1 to continue");
	       ch=scan.nextInt();
			}while(ch==1);
			System.out.print("Finished...");
			double mean1=0;
			double sum1=0;
						
			//Calculating the mean of array art1
			for (int f=0;f<art1.length;f++)
			{
				sum1=sum1+art1[f];
			}
			mean1=sum1/art1.length;
			System.out.println("checking for mean for the variables...");
			System.out.println(Arrays.toString(art1));
			System.out.println("the mean is "+mean1);
			
			//calculating the variance
			
			double sum2=0;
			double var1 = 0;
			for(int k=0;k<art1.length;k++)
			{
				sum2= sum2+Math.pow((art1[k]-mean1),2);
			}
			System.out.println("the value of sum2 is "+sum2);
			var1=sum2/art1.length;
			System.out.println("checking for variance for the variables...");
			System.out.println(Arrays.toString(art1));
			System.out.println("the variance is: "+var1);
			
		}
	}




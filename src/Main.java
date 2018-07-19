import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 15 July 2018
 * 
 * Cusioning to creating and outputting regatta results. 
 */

public class Main 
{
	public static void main(String[] args)
	{
		Scanner keyboard = new Scanner(System.in);
		Scanner fileInput = null;
		
		// Gathering regatta name
		System.out.println("Please input name of regatta: " );
		String name = keyboard.nextLine().trim();
		
		// Input file specification
		System.out.println("\nPlease enter input file name: ");
		String filename = keyboard.next();
		
		Regatta r = new Regatta(name);
		
		// Reading from specified file.
		try
		{
			fileInput = new Scanner(new FileInputStream(filename));
		}
		catch(FileNotFoundException e) 
		{ 
			System.err.println("File not found. Exiting...");
			keyboard.close();
			System.exit(1);
		}
		
		// Getting wind type.
		String windType = fileInput.nextLine().toLowerCase();
		if(!(windType.equals("low") || windType.equals("high"))) {
			System.err.println("INVALID WIND CONDITION."); }
		
		// Reading in boats from input file.
		while(fileInput.hasNextLine())
		{
			String[] split = fileInput.nextLine().split(":");
			r.addBoat(split[0], split[1] + windType, split[2]);
		}
		fileInput.close();
		
		// Outputting regatta results.
		System.out.println(r.podium());
		
		// File output.
		System.out.println("Would you like to output to file?");
		String fileOutputResponse = keyboard.next();
	
		if(fileOutputResponse.trim().toLowerCase().equals("yes"))
		{
			PrintWriter output = null;
			
			System.out.println("File name?");
			String outputFile = keyboard.next();
			
			try
			{
				// Outputting to file.
				output = new PrintWriter(new FileWriter(outputFile, true));
				
				output.print(r.podium());
				output.close();
			}
			catch(IOException e) 
			{ 
				System.err.println("IOException... Exiting.");
				keyboard.close();
				System.exit(1);
			}
		}
		else
		{
			System.out.println("Alright. Goodbye for now.");
			System.exit(1);
		}
		
		keyboard.close();
		
	}
}

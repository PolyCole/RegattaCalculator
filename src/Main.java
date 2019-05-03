import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 16 March 2019
 * 
 * A command line interface used to calculate regatta results.
 */

public class Main 
{
	private static Scanner fileInput = null;
	private static PrintWriter fileOutput = null;

	private static Scanner keyboard = new Scanner(System.in);

	private static Regatta r = null;

	public static void main(String[] args)
	{
		System.out.println("------------------------------------");
		System.out.println("Welcome to the CW Regatta Calculator");
		System.out.println("Type help to get a list of commands");
		System.out.println("------------------------------------");

		String command = null;

		// Command loop. 
		while(!"exit".equals(command)) {
			System.out.print(">>\t");
			command  = keyboard.nextLine().trim();
			String[] processed = command.split(" ");

			switch(processed[0]) {
			case "help":
				help();
				break;
			case "regatta":
				initializeRegatta(processed);
				break;
			case "podium":
				podium();
				break;
			case "write":
				writeToFile(processed);
				break;
			case "format":
				format();
				break;
			case "types":
				types();
				break;
			case "exit":
				goodbye();
				break;
			case "addtype":
				addType(processed);
				break;
			default:
				System.out.println("\nInvalid command. Use help to list commands.\n");
				break;
			}

		}
	}

	/**
	 * This method takes an existing regatta and writes it to file. 
	 * @param data : The string containing the command "write" and the filename. 
	 */
	private static void writeToFile(String[] data) {
		// Regatta hasn't been calculated.
		if(r == null) {
			System.out.println("\nYou haven't processed a regatta yet! \nUse regatta to begin processing.\n");
			return;
		}
		
		// Just the command was specified, we need to get the filename.
		if(data.length == 1) {
			System.out.println("Oops, looks like you didn't specify an output file.\n");
			initializeOutput(getFilename());
		}

		// Filename was specified but is invalid. Need to get another one.
		if(!isValidFileName(data[1])) {
			System.out.println("Looks like your filename is incorrect.\n");
			initializeOutput(getFilename());
		}

		initializeOutput(data[1]);

		// Write out.
		fileOutput.print(r.podium());
		fileOutput.close();
	}

	/**
	 * This method outputs the adjusted results from the regatta.
	 */
	private static void podium() {
		if(r == null) System.out.println("\nCannot print the podium for a regatta that doesn't exist!");
		else System.out.println("\n" + r.podium());
	}

	/**
	 * This method initializes the regatta by reading everything into memory. 
	 * @param data : The string containing the command. 
	 */
	private static void initializeRegatta(String[] data) {
		// Just command was used, we need input file.
		if(data.length == 1) {
			System.out.print("\nOops, looks like you didn't specify an input file.\n");
			initializeInput(getFilename());
		}
		
		// Input file specified but the file was invalid. Need to get another.
		if(data.length == 2) {
			if(!isValidFileName(data[1])) {
				System.out.println("\nOops, your filename is invalid.");
				initializeInput(getFilename());
			} else {
				initializeInput(data[1]);
			}
		}
		processRegatta(getRegattaName());
	}

	/**
	 * This method takes the read in data and processes it. 
	 * @param name : The name of the regatta.
	 */
	private static void processRegatta(String name) {
		r = new Regatta(name);

		// Getting wind type.
		String windType = fileInput.nextLine().toLowerCase();
		if(!(windType.equals("low") || windType.equals("high"))) {
			throw new IllegalStateException("Invalid wind condition");
		}

		// Reading in boats from input file.
		while(fileInput.hasNextLine()){
			String[] split = fileInput.nextLine().split(":");
			r.addBoat(split[0], split[1] + windType, split[2]);
		}
		
		fileInput.close();
		System.out.println("\nFinished processing regatta. Use podium to see results");
	}

	// Outputs all available commands and their purpose.
	private static void help() {
		System.out.println("\n----------------------------------");
		System.out.println("---Regatta Calculator Commands----");
		System.out.println("----------------------------------");
		System.out.println("addtype -- Adds a boat type and handicap to file. Format: name:lowHandicap:highHandicap");
		System.out.println("format -- Provides a sample format for how input files should be arranged.");
		System.out.println("help -- Lists every command that can be used to process regattas.");
		System.out.println("podium -- Lists the results of the regatta, assuming one has been processed.");
		System.out.println("regatta [inputfile] -- Accepts an input file as a parameter, this processes the regatta results outlined in the file.");
		System.out.println("types -- lists every available boat type.");
		System.out.println("write [outputfile] -- Takes the results of the regatta and writes them to the file passed as a parameter.");
		System.out.println("----------------------------------\n");
	}
	
	// Outputs the format that is expected in input files.
	private static void format() {
		System.out.println("\nThe following is a sample input file.");
		System.out.println("-----------------------------------------");
		System.out.println("low\t\t\t // The first line should be the wind speed (low or high");
		System.out.println("Babe:cscow:32.54\r\n" + 
				"Renegade:420:38.01\r\n" + 
				"Top:xboat:41.20\r\n" + 
				"Jeanie-K:xboat:41.35\r\n" + 
				"JuiceBox:capri:42.40\r\n" + 
				"LadyBug:nj2k:55.40\r\n" + 
				"XQ'sMe:xboat:56.58\r\n" + 
				"Steve:mcscow:57.29\r\n" + 
				"TunaFish:nj2k:60.32\r\n" + 
				"NoName:nj2k:67.20");
		System.out.println("-----------------------------------------");
		System.out.println("Each boat entry follows the pattern of name:type:time");
		System.out.println("Boat times should be formatted as mm.ss");
		System.out.println("\nThe total list of boat types can be seen with the types command.\n");
	}

	// Returns the available boat classes. 
	private static void types() {
		try {
			Scanner handicaps = new Scanner(new FileInputStream("handicaps.txt"));
			System.out.println("\nAvailable Boat Types");
			System.out.println("--------------------");
			while(handicaps.hasNextLine()) {
				System.out.println(handicaps.nextLine().split(":")[0]);
			}
			System.out.println("");
			handicaps.close();
		} catch (FileNotFoundException e) {
			System.err.println("\n\nSEVERE ERROR: CRUCIAL FILE HANDICAPS.TXT NOT FOUND\n\n");
			System.err.println("Attempting to re-create file....");
			generateHandicaps();
			System.err.println("Success!\nTry command again.\n");
		}

	}
	
	/**
	 * This method is used when the generic handicaps file is not found. 
	 */
	private static void generateHandicaps() {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(new File("handicaps.txt"), true));
			// The generic handicap set.
			writer.append("420:90.00:94.00\r\n" + 
					"cscow:80.75:78.1\r\n" + 
					"m16scow:92.4:88.1\r\n" + 
					"mcscow:88.45:87.2\r\n" + 
					"xboat:103:96.5\r\n" + 
					"nj2k:99:103\r\n" + 
					"butterfly:109.75:106.9\r\n" + 
					"miniscow:108:105\r\n" + 
					"capri:100.10:99.20\r\n" + 
					"canoe:79.00:144.00");
			writer.close();
		} catch(IOException e) {
			System.out.println("\n\nSOMETHING HAS GONE TERRIBLY WRONG\n\n");
			goodbye();
		}
	} 

	/**
	 * This method is used to add an additional boat class to the generic handicap file. 
	 * @param data : The boat class to be added.
	 */
	private static void addType(String[] data) {
		// Weird params. 
		if(data.length != 2) {
			System.out.println("Incorrect parameter usage. Consult help command");
			return;
		}
		// Valid handicap, let's proceed. 
		if(isValidHandicap(data[1])) {
			try {
				PrintWriter handicaps = new PrintWriter(new FileWriter("handicaps.txt", true));
				handicaps.append("\n");
				handicaps.append(data[1]);
				handicaps.close();
				System.out.println("Successfully added handicap!");
			} catch (IOException e) {
					// Off chance file just doesn't exist.
					System.err.println("\n\nSEVERE ERROR: CRUCIAL FILE: HANDICAPS.TXT NOT FOUND\n\n");
					System.err.println("Attempting to re-create file....");
					generateHandicaps();
					System.err.println("Success!\nTry command again.\n");
			}
		} else {
			System.out.println("Invalid handicap. Consult help command for usage information");
		}
	}

	/**
	 * This method prompts the user for a regatta name. 
	 * @return : Returns the name of the regatta.
	 */
	private static String getRegattaName() {
		String name = "";

		System.out.println("\nPlease enter a name for the regatta...");
		do {
			System.out.print(">>\t");
			name = keyboard.nextLine().trim();
			if("exit".equals(name)) goodbye();
		} while(!isValidRegattaName(name) || name.length() == 0);

		return name;
	}

	/**
	 * This method prompts the user for a file input name. 
	 * @return : The name of the input file. 
	 */
	private static String getFilename() {
		String name = "";

		System.out.println("Please enter a file name including the extension (results.txt)\n");
		do {
			System.out.print(">>\t");
			name = keyboard.nextLine().trim();
			if("exit".equals(name)) goodbye();
		} while(!isValidFileName(name) || name.length() == 0);

		return name;
	}

	/**
	 * Checks that the file name passed in is in fact valid. 
	 * @param name : The intended file name. 
	 * @return : Boolean representing if the file is valid or not. 
	 */
	private static boolean isValidFileName(String name) {
		if(name.indexOf('.') != -1 && name.length() != 0) return true;
		else System.out.println("\nFilenames must include the file's extension."); return false;
	}

	/**
	 * Checks that the regatta name is valid -- Basically ensuring we don't get passed anything bizarre. 
	 * @param name : The intended name. 
	 * @return : Boolean representing the validity of the regatta name. 
	 */
	private static boolean isValidRegattaName(String name) {
		if(name.length() == 0) {
			System.out.println("\nRegatta names must be atleast one character!"); 
			return false;
		}

		if(name.indexOf('.') == -1 && name.length() != 0) return true;
		else {
			System.out.println("\nRegatta names cannot have \'.\' in them!"); 
			return false;
		}
	}

	/**
	 * Ensures the validity of a handicap.
	 * @param type : The intended handicap.
	 * @return : Boolean representing the validity of the handicap. 
	 */
	private static boolean isValidHandicap(String type) {
		if("exit".equals(type)) goodbye();

		String[] split = type.split(":");

		if(split.length != 3) {
			System.out.println("\nTypes should have three elements, the boat identifier, the low handicap, and the high handicap");
			System.out.println("All items should be separated by a colon.\n");
			return false;
		}

		try {
			Double.parseDouble(split[1]);
			Double.parseDouble(split[2]);
			return true;
		} catch(NumberFormatException e) {
			System.out.println("One of the two handicaps are not numbers!");
			return false; 
		}
	}
	
	/**
	 * Initializing the input file.  
	 * @param filename : The name of the file. 
	 */
	private static void initializeInput(String filename) {
		// Reading from specified file.
		try
		{
			fileInput = new Scanner(new FileInputStream(filename));
		}
		catch(FileNotFoundException e) 
		{ 
			System.err.println("\nFile not found. Please re-enter.");
			initializeInput(getFilename());
		}
	}

	/**
	 * Initializing the output file. 
	 * @param outputFile : The name of the output file. 
	 */
	private static void initializeOutput(String outputFile) {
		try
		{
			// Outputting to file.
			fileOutput = new PrintWriter(new FileWriter(outputFile, true));

			fileOutput.print(r.podium());
			fileOutput.close();
		}
		catch(IOException e) 
		{ 
			System.err.println("IOException... Exiting.");
			keyboard.close();
			System.exit(1);
		}
	}

	// Exit method. 
	private static void goodbye() {
		System.out.println("Exiting. Farewell for now!"); 
		keyboard.close();
		System.exit(0);
	}
}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 15 July 2018
 * 
 * Creates a regatta that the user can add boats to.
 * Calculates adjusted time and ranks boats accordingly.
 * 
 */

public class Regatta 
{
	//Our handicaps and boats.
	public HashMap<String, Double> boatHandicaps = new HashMap<>();
	private HashSet<boat> boats = null;
	
	// Checks if the final results have been calculated before.
	private boolean tabulated;
	
	// Updates array with final results.
	private boat[] ordered = null;
	
	// Regatta name.
	private String name;

	/**
	 * Constructor
	 * @param name : The name of the regatta
	 */
	public Regatta(String name)
	{
		this.name = name;
		initHashMap();
		boats = new HashSet<>();
		tabulated = false;
	}
	
	/**
	 * Adds a boat to the regatta.
	 * @param name : Name of the boat
	 * @param boatClass : Class of the boat
	 * @param time
	 * @return : The status of the add.
	 */
	public boolean addBoat(String name, String boatClass, String time)
	{
		boat b = new boat(name, boatClass, time);
		return boats.add(b);
	}
	
	/**
	 * Outputs the final results of the regatta, nicely.
	 * 
	 */
	public String podium()
	{
		ordered = this.calculateFinalResults();
		
		// Optimized to use a StringBuilder.
		StringBuilder sb = new StringBuilder();
		
		sb.append("********" + name + "********\r\n");
		
		int position = 1;
		
		for(boat b : ordered)
		{
			sb.append(position + ". " + b + "\r\n");
			++position;
		}
		
		return sb.toString();
	}
	
	/**
	 * Calculates the final orderings of the boats in the regatta.
	 * @return : An ordered array of the boats after adjusting.
	 */
	public boat[] calculateFinalResults()
	{
		// Only allows the results to be calculated once. Ensures accuracy.
		if(tabulated) { return ordered;}
		
		boat[] finalResults = new boat[boats.size()];
		
		for(int i = finalResults.length-1; i >= 0; --i)
		{
			boat max = findMax(boats);
			finalResults[i] = max;
			boats.remove(max);
		}
		
		tabulated = true;
		
		return finalResults;
	}
	
	/**
	 * Finds the maximum adjusted time of the boats in a hashset.
	 * @param b : a hashset of boats.
	 * @return : The boat with the longest time in the set.
	 */
	private boat findMax(HashSet<boat> b)
	{
		boat currentMax = null;
		
		for(boat cur : boats)
		{
			if(currentMax == null) { currentMax = cur;}
			
			if(cur.adjustedTime > currentMax.adjustedTime)
			{
				currentMax = cur;
			}
		}
		
		return currentMax;
	}
	
	/**
	 * Initializes hashMap.
	 */
	private void initHashMap()
	{
		Scanner handicapsIn = null;
		
		try 
		{
			handicapsIn = new Scanner(new FileInputStream("handicaps.txt"));
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(handicapsIn.hasNextLine())
		{
			String[] processed = handicapsIn.nextLine().split(":");
			
			boatHandicaps.put(processed[0] + "low", Double.parseDouble(processed[1]));
			boatHandicaps.put(processed[0] + "high", Double.parseDouble(processed[2]));
		}
	}
	
	// Class representing a single boat in the regatta. Includes name, time, and class. 
	class boat
	{
		/**
		 * Hashcode function for boats. 
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(adjustedTime);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((boatClass == null) ? 0 : boatClass.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((time == null) ? 0 : time.hashCode());
			return result;
		}

		/**
		 * Equals method for boats.
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			boat other = (boat) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Double.doubleToLongBits(adjustedTime) != Double.doubleToLongBits(other.adjustedTime))
				return false;
			if (boatClass == null) {
				if (other.boatClass != null)
					return false;
			} else if (!boatClass.equals(other.boatClass))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (time == null) {
				if (other.time != null)
					return false;
			} else if (!time.equals(other.time))
				return false;
			return true;
		}

		private String name;
		private String boatClass;
		private String time;
		
		// Time after the handicap is applied.
		private double adjustedTime;
		
		/**
		 * Boat constructor.
		 * @param name : The name of the boat.
		 * @param boatClass : The class of the boat.
		 * @param time : The time the boat ran the course in. MM.SS
		 */
		public boat(String name, String boatClass, String time)
		{
			this.name = name;
			this.boatClass = boatClass;
			this.time = time;
			
			this.calculate();
		}
		
		public double getAdjustedTime()
		{
			return adjustedTime;
		}
		
		/**
		 * Calculates the adjusted times of the boats.
		 */
		private void calculate()
		{
			int timeInSeconds = calculate(time);
			adjustedTime = (Math.round((timeInSeconds * 100) / boatHandicaps.get(boatClass)*1000))/1000.00;
		}
		
		/**
		 * 
		 * @param input : Inputs the time of the boat in format of mm.ss
		 * @return : Returns the number of seconds the boat took.
		 */
		public int calculate(String input)
		{
			String[] arr = new String[2];

			int splitIndex = input.indexOf(".");

			arr[0] = input.substring(0, splitIndex);
			arr[1] = input.substring(splitIndex + 1);

			int partOne = Integer.parseInt(arr[0]);
			int partTwo = Integer.parseInt(arr[1]);

			return (partOne * 60) + partTwo;
		}
		
		public String toString()
		{
			return name + " " + adjustedTime;
		}

		private Regatta getOuterType() {
			return Regatta.this;
		}
	}
}

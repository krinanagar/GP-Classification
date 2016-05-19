import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
/**
 * 
 * @author Krina Nagar
 *
 */
public class ParseData {
	static List<Instance> instances = new ArrayList<>(); //store each patient
	
	public ParseData(String file) throws IOException{
		parseFile(file);
	}
	
	/**
	 * Parses file passed in. If a attribute is a ? it will be changed to 
	 * -1. Each patient is put into a map and then the map is added to the
	 * list of instances(patients)
	 * @param file
	 * @throws IOException 
	 */
	private void parseFile(String file) throws IOException {
		Scanner sc = new Scanner(new InputStreamReader(ClassLoader.getSystemResourceAsStream(file)));
		//Scanner sc = new Scanner(new File(file));
		HashMap<String, Integer> patient = new HashMap<String, Integer>();
		String line;
		while(sc.hasNextLine()){
			line = sc.nextLine(); //read line
			String[] lineArray = line.split(","); //separate each variable
			int finalarray[] = new int[lineArray.length]; 
			//iterate through the line
			for(int i = 0; i < lineArray.length; i++){
				//check for question marks
				if(lineArray[i].equals("?")){
					lineArray[i] = "-1";
				}
				//add to integer array
				 finalarray[i] = Integer.parseInt(lineArray[i]);
				
			}
		//add attributes to map
		patient.put("ID", finalarray[0]);
		patient.put("clumpThickness", finalarray[1]);
		patient.put("uniformityOfCellSize", finalarray[2]);
		patient.put("uniformityOfCellShape", finalarray[3]);
		patient.put("marginalAdhesion", finalarray[4]);
		patient.put("singleEpithelialCellSize", finalarray[5]);
		patient.put("bareNuclei", finalarray[6]);
		patient.put("blandChromatin", finalarray[7]);
		patient.put("normalNucleoli", finalarray[8]);
		patient.put("mitoses", finalarray[9]);
		patient.put("cancerClass", finalarray[10]);
		//add map to list of patients
		instances.add(new Instance(patient));
		patient.clear();
		}
		
		sc.close();
	}

	public static List<Instance> getInstances() {
		return instances;
	}
	
	 
}

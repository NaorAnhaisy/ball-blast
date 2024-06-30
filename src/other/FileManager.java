package other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	private static String directory = System.getProperty("user.home");  
	private static String fileName = "sample.txt";
	private static String absolutePath = directory + File.separator + fileName;
	
	public static void writeToFile(FileResult fileRes) {
		
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absolutePath))) {  
		    String fileContent = fileRes.getName() + "\n";
		    bufferedWriter.write(fileContent);
		    fileContent = String.valueOf(fileRes.getScore());
		    bufferedWriter.write(fileContent);
		} catch (IOException e) {
		    // exception handling
		}
	}
	
	public static FileResult readFromFile() {
		String name = null;
		int score = 0;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(absolutePath))) {  
		    name = bufferedReader.readLine();
		    score = Integer.parseInt(bufferedReader.readLine());
		} catch (FileNotFoundException e1) {
		    // exception handling
		} catch (IOException e2) {
		    // exception handling
		}
		FileResult fileRes = new FileResult(name, score);
		return fileRes;
	}
}

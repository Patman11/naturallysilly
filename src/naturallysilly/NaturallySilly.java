package naturallysilly;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * COMP 472 - Artificial Intelligence
 *
 * @author Patrick Bednarski - 40002239
 * @author Youssef Akallal - 25988322
 * @author Ali Douch - 27578253
 * @author Anthony Dubois - 26647375
 */
public class NaturallySilly {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> easyStrings = CandyCrisis.parseFile("input1.txt");
        List<String> mediumStrings = CandyCrisis.parseFile("input2.txt");
        List<String> expertStrings = CandyCrisis.parseFile("input3.txt");
        List<String> masterStrings = CandyCrisis.parseFile("input4.txt");
        
        removeOutputFiles();
        
        easyStrings.stream().map((gameString) -> new CandyCrisis(gameString, 1)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
        mediumStrings.stream().map((gameString) -> new CandyCrisis(gameString, 2)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
        expertStrings.stream().map((gameString) -> new CandyCrisis(gameString, 3)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
        masterStrings.stream().map((gameString) -> new CandyCrisis(gameString, 4)).map(AlgorithmA::new).forEachOrdered(AlgorithmA::start);
        
        appendTotalNumberOfMovesToOutput();
        
        System.out.println("Done!");
    }
    
    private static void appendTotalNumberOfMovesToOutput() {
    	File currentDirectory = new File(".");
    	File[] files = currentDirectory.listFiles();
    	for (File file: files) {
    		if (file.getName().startsWith("output")) {
  	
		    	ArrayList<String> list = new ArrayList<String>();
				try (Scanner scanner = new Scanner(file)) {
		
			    	while (scanner.hasNext()){
			    	    list.add(scanner.next());
			    	}
			    	scanner.close();
			    	
			    	int totalNumberOfMoves = 0;
			    	for (int i = 0; i < list.size(); i += 2) {
			    		String line = list.get(i);
			    		totalNumberOfMoves += line.length();
			    	}
			    	
			    	try (PrintWriter output = new PrintWriter(new FileOutputStream(file, true))) {
			            output.println(totalNumberOfMoves);
			        } catch (FileNotFoundException e) {
			        	e.printStackTrace();
			        }
			    	
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
    private static void removeOutputFiles() {
    	File currentDirectory = new File(".");
    	File[] files = currentDirectory.listFiles();
    	
    	for (File file: files) {
    		if (file.getName().startsWith("output")) {
    			file.delete();
    		}
    	}
    }
}

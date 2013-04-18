package my.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyUtil {

	
	public static void printFile(String filename, String content) {
		try {
			 
 
			File file = new File(filename);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			System.out.printf("Done writing at %s \n", filename);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

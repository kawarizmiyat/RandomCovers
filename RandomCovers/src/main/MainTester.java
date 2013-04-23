package main;

import java.io.IOException;

import com.generator.ArbitraryGraphGenerator;
import com.generator.RandomGraphGenerator;
import com.generator.RegionalizedGraphGenerator;
import com.test.RegionTester;

public class MainTester {


	public static void main(String[] args) {
		
//		
//		
	int numberFiles = 25;
	int numReaders = 25; 
	int numTags = 100; 
	int maxX = 30; 
	int maxY = 30; 
	String foldername = "region_dir";
	String filenamePrefix = "region_graph";
//		
//		RandomGraphGenerator.generateRandomUniformCovers(numberFiles, 
//				numReaders, 
//				numTags, 
//				maxX, 
//				maxY, 
//				foldername, 
//				filenamePrefix, 'u', 1, 1);
//		

		
		// RegionalizedGraphGenerator.generateRandomGraphs(numberFiles,
		//		5, 5, maxX, maxY, 30, numTags, numReaders, foldername, filenamePrefix);
		
	
	ArbitraryGraphGenerator.generateRandomGraphs(numberFiles, 
			numReaders, numTags, 0.5, foldername, filenamePrefix);
	
		// new RegionTester().run();
		
		
	}

}

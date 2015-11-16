package ravensproject;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

public class KBAILogging {
	public static boolean debug = false;
	public static boolean stats = false;
	public static boolean metrics = true;
	public static long startingMemory;
    public static Stack priorMemory = new Stack();
   // public static Stack priorMemoryData = new Stack();
    public static Stack priorTime = new Stack();
	//public static long previousMemory = 0;
	//public static String previousMemoryData = "";
    //public static long priorTime = 0;
   // public static long snPopulationStartTime = 0;
	public static HashMap<String, ArrayList<String>> distinctAttributeValues = new HashMap<String, ArrayList<String>>();
	
	//FileWriter fw = new FileWriter("MemoryConsumption.txt");
	public static StringWriter memoryConsumptionLog = new StringWriter();
	public static StringWriter performanceLog = new StringWriter();
	public static StringWriter answerFitnessLog = new StringWriter();
	//sw.write("some content...");
	//fw.write(sw.toString());
	//fw.close();

    public static void initializeMetrics()
    {
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
    	startingMemory = runtime.totalMemory() - runtime.freeMemory();
    }
    public static void updateLog(String logName, String[] data)
    {

        
        switch (logName) {
            case "memory consumption":

                // Get the Java runtime
                Runtime runtime = Runtime.getRuntime();
                // Run the garbage collector
                runtime.gc();
                // Calculate the used memory
                long memory = runtime.totalMemory() - runtime.freeMemory();
            	
                if(data[1].equals("start"))
                {
	                priorMemory.push(memory);
	                //priorMemoryData.push(data[0]);
                }
                else
                {
                    long previousMemory = (long) priorMemory.pop();
                    //String previousMemoryData = (String) priorMemoryData.pop();
                    
                    long priorDifference = memory - previousMemory;
                    //long startingDifference = memory - startingMemory;
                    memoryConsumptionLog.write(data[0]+"\n");
                    memoryConsumptionLog.write("Total used memory in bytes: " + memory+"\n");
                   // memoryConsumptionLog.write("Used memory in megabytes: "+ kbai.Utilities.bytesToMegabytes(memory)+"\n");
                    //memoryConsumptionLog.write("Used memory change from "+previousMemoryData+" : "+priorDifference+" bytes"+"\n");
                    //memoryConsumptionLog.write("Used memory change from "+previousMemoryData+" : "+ kbai.Utilities.bytesToMegabytes(priorDifference)+" megabytes"+"\n");
                    memoryConsumptionLog.write("Total used memory before operation : "+previousMemory+" bytes"+"\n");
                    memoryConsumptionLog.write("Total used memory after operation : "+memory+" bytes"+"\n");
                    memoryConsumptionLog.write("Used memory change after operation : "+priorDifference+" bytes"+"\n\n");
                    //memoryConsumptionLog.write("Used memory change after operation : "+ kbai.Utilities.bytesToMegabytes(priorDifference)+" megabytes"+"\n\n");
                }
                
                
                     break;
            case "performance":  

	                if(data[1].equals("start"))
	                {
	                	/*if(data[0].equals("Semantic Network Population"))
	                	{
	                		snPopulationStartTime = System.nanoTime();
	                	}*/
		                priorTime.push(System.nanoTime());
	                }
	                else
	                {
		                long stopTime = System.nanoTime();
		                long elapsedTime = stopTime - (long)priorTime.pop();
		                
	                	/*if(data[0].equals("Semantic Network Population"))
	                	{
	                		elapsedTime = stopTime - snPopulationStartTime;
	                	}*/
	                	
	            		performanceLog.write(data[0]+" - Elapsed Time : "+elapsedTime+"\n\n");
	                }
	
                     break;
            case "answer fitness":  

                if(data[0].equals("match"))
                {
            		answerFitnessLog.write(data[1]+"\n\t sState name : "+
										    data[2]+"\t sNode name : "+
										    data[3]+"\n\t dState name : "+
										    data[4]+"\t dNode name : "+
										    data[5]+"\n\t similarities : "+
										    data[6]+"\n\t differences : "+
										    data[7]+"\n\n");
                }
                else if(data[0].equals("!match"))
                {
            		answerFitnessLog.write(data[1]+"\n\t sState name : "+
										    data[2]+"\t sNode name : "+
										    data[3]+"\n\t dState name : "+
										    data[4]+"\t dNode name : "+
										    data[5]+"\n\t differences : "+
										    data[6]+"\n\n");
                }
                else if(data[0].equals("fitness"))
                {
            		answerFitnessLog.write(data[1]+" - state name : "+data[2]+"\t state fitness : "+data[3]+"\n\n");
                }

                 break;
        } 
    }
    public static void writeLogFile(String logName)
    {
    	FileWriter fw;
    	String data = "";
		try {
	    	fw = new FileWriter(logName+".txt");
        switch (logName) {
            case "MemoryConsumption":
            		 data = memoryConsumptionLog.toString();
                     break;
            case "Performance":  
            		 data = performanceLog.toString();
                     break;
            case "AnswerFitness":  
		       		 data = answerFitnessLog.toString();
		             break;
        }
	    	fw.write(data);
	    	fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void writeAllMetricLogs()
    {
    	FileWriter fw;
		try {
	    	fw = new FileWriter("MemoryConsumption.txt");
	    	fw.write(memoryConsumptionLog.toString());
	    	fw.close();
	    	fw = new FileWriter("Performance.txt");
	    	fw.write(performanceLog.toString());
	    	fw.close();
	    	fw = new FileWriter("AnswerFitness.txt");
	    	fw.write(answerFitnessLog.toString());
	    	fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void logQATransformations(HashMap<String, SemanticNetRelationship> hashMap)
    {
    	PrintWriter writer;
		try {
			writer = new PrintWriter("QATransformations.txt", "UTF-8");
			
			String data = "";
	    	for(Entry<String, SemanticNetRelationship> e1 : hashMap.entrySet())
	    	{
		    		data = data.concat(e1.getKey()+"\n");
		    		data = data.concat("\t"+e1.getValue().toString()+"\n\n");
	    		writer.println(data);
	    	}
	    	writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void logQQTransformations(HashMap<String, HashMap<String, SemanticNetRelationship>> transformations)
    {
    	PrintWriter writer;
		try {
			writer = new PrintWriter("QQTransformations.txt", "UTF-8");
			String data = "";
	    	for(Entry<String, HashMap<String,SemanticNetRelationship>> e1 : transformations.entrySet())
	    	{
	    		data = data.concat(e1.getKey()+"\n");
	        	for(Entry<String, SemanticNetRelationship> e2 : e1.getValue().entrySet())
	        	{
		    		data = data.concat("\t"+e2.getKey()+"\n");
		    		data = data.concat("\t"+e2.getValue().toString()+"\n\n");
	        	}
	    		writer.println(data);
	    	}
	    	writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void logDistinctAttributeValues()
    {
    	PrintWriter writer;
		try {
			writer = new PrintWriter("DistinctAttributeValues.txt", "UTF-8");
	    	for(Entry<String,ArrayList<String>> e : distinctAttributeValues.entrySet())
	    	{
	    		String line = e.getKey();
	    		line = line.concat(":");
	    		for(String s : e.getValue())
	    		{
	    			line = line.concat(" "+s+",");
	    		}
	    		writer.println(line);
	    	}
	    	writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void mergeNodeAttributesIntoDAV(HashMap<String,String> nodeAttrs)
    {
    	for(Entry<String,String> e : nodeAttrs.entrySet())
    	{
    		if(!distinctAttributeValues.containsKey(e.getKey()))
    		{
    			distinctAttributeValues.put(e.getKey(), new ArrayList<String>());
    			distinctAttributeValues.get(e.getKey()).add(e.getValue());
    		}
    		else
    		{
    			if(!distinctAttributeValues.get(e.getKey()).contains(e.getValue()))
    			{
    				distinctAttributeValues.get(e.getKey()).add(e.getValue());
    			}
    		}
    	}
    }
}

package kbai;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class KBAILogging {
	public static boolean debug = true;
	public static boolean stats = true;
	public static HashMap<String, ArrayList<String>> distinctAttributeValues = new HashMap<String, ArrayList<String>>();
		

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

package ravensproject;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class Utilities {

	private static final long MEGABYTE = 1024L * 1024L;
	  
	public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>(setA);
	    tmp.addAll(setB);
	    return tmp;
	  }

	  public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>();
	    for (T x : setA)
	      if (setB.contains(x))
	        tmp.add(x);
	    return tmp;
	  }

	  public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>(setA);
	    tmp.removeAll(setB);
	    return tmp;
	  }

	  public static <T> Set<T> symDifference(Set<T> setA, Set<T> setB) {
	    Set<T> tmpA;
	    Set<T> tmpB;

	    tmpA = union(setA, setB);
	    tmpB = intersection(setA, setB);
	    return difference(tmpA, tmpB);
	  }

	  public static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
	    return setB.containsAll(setA);
	  }

	  public static <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
	    return setA.containsAll(setB);
	  }
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	public static String getKeyForMax(HashMap<String, Integer> map) {
		int maxVal = -99;
		String maxKey = "";
		//Entry<String, Integer> maxEntry;
		for(Entry<String, Integer> e : map.entrySet())
		{
			if(e.getValue() > maxVal)
			{
				maxVal = e.getValue();
				maxKey = e.getKey();
				//maxEntry = e;;
			}
		}

		return maxKey;
	}

	  public static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	  }
	  public static void logMemoryUsage() {
		    // Get the Java runtime
		    Runtime runtime = Runtime.getRuntime();
		    // Run the garbage collector
		    runtime.gc();
		    // Calculate the used memory
		    long memory = runtime.totalMemory() - runtime.freeMemory();
		    System.out.println("Used memory is bytes: " + memory);
		    System.out.println("Used memory is megabytes: "
		        + bytesToMegabytes(memory));
		  }
	  public static ArrayList<Map.Entry<String, Integer>> mapToStortedList(Map<String, Integer> unsortMap)
	    {
		  ArrayList<Map.Entry<String,Integer>> entries = new ArrayList<Map.Entry<String,Integer>>(
				    unsortMap.entrySet()
				);
				Collections.sort(
				    entries
				,   new Comparator<Map.Entry<String,Integer>>() {
				        public int compare(Map.Entry<String,Integer> a, Map.Entry<String,Integer> b) {
				            return Integer.compare(b.getValue(), a.getValue());
				        }
				    }
				);
	        return entries;
	    }
}

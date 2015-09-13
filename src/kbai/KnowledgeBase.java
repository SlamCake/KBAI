package kbai;

import java.util.HashMap;

public class KnowledgeBase {

	//shape: square, pentagon, triangle, pac-man, star, heart, circle, plus, right triangle, octagon, diamond, rectangle,
	//size: very large, small, huge, medium, very small, large,
	//width: large, huge, small,
	//angle: 0, 45, 270, 180, 135, 315, 225, 90,
	//fill: yes, no, right-half, left-half, top-half, bottom-half,
	//alignment: bottom-right, bottom-left, top-right, top-left,
	//height: small, large, huge,
	//left-of
	//above
	//overlaps
	//inside

    public static HashMap<String, Object> sizeMap = new HashMap<String, Object>();
    public static HashMap<String, Object> widthMap = new HashMap<String, Object>();
    public static HashMap<String, Object> heightMap = new HashMap<String, Object>();
    public static HashMap<String, Object> alignmentMap = new HashMap<String, Object>();
    public static HashMap<String, Object> fillMap = new HashMap<String, Object>();
    
    public static HashMap<String, HashMap<String, Object>> attributeMap = new HashMap<String, HashMap<String,Object>>();

    public static void initialize() {
    	
    	sizeMap.put("huge", (Integer)6);
    	sizeMap.put("very large", (Integer)5);
    	sizeMap.put("large", (Integer)4);
    	sizeMap.put("medium", (Integer)3);
    	sizeMap.put("small", (Integer)2);
    	sizeMap.put("very small", (Integer)1);
    	

    	widthMap.put("huge", (Integer)6);
    	widthMap.put("very large", (Integer)5);
    	widthMap.put("large", (Integer)4);
    	widthMap.put("medium", (Integer)3);
    	widthMap.put("small", (Integer)2);
    	widthMap.put("very small", (Integer)1);
    	

    	heightMap.put("huge", (Integer)6);
    	heightMap.put("very large", (Integer)5);
    	heightMap.put("large", (Integer)4);
    	heightMap.put("medium", (Integer)3);
    	heightMap.put("small", (Integer)2);
    	heightMap.put("very small", (Integer)1);
    	

    	alignmentMap.put("bottom-left", new int[]{0,0});
    	alignmentMap.put("bottom-right", new int[]{1,0});
    	alignmentMap.put("top-left", new int[]{0,1});
    	alignmentMap.put("top-right", new int[]{1,1});
    	
    	fillMap.put("no", new int[][]{{0,0},{0,0}});
    	fillMap.put("yes", new int[][]{{1,1},{1,1}});
    	fillMap.put("right-half", new int[][]{{0,1},{0,1}});
    	fillMap.put("left-half", new int[][]{{1,0},{1,0}});
    	fillMap.put("top-half", new int[][]{{1,1},{0,0}});
    	fillMap.put("bottom-half", new int[][]{{0,0},{1,1}});
    	
    	attributeMap.put("size", sizeMap);
    	attributeMap.put("width", widthMap);
    	attributeMap.put("height", heightMap);
    	attributeMap.put("alignment", alignmentMap);
    	attributeMap.put("fill", fillMap);
	}
        

}

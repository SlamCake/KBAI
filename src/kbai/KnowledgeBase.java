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

    public static HashMap<String, AttributeMetaData> attributeMetaDataMap = new HashMap<String, AttributeMetaData>();
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

    	//may need to update knowledge base for new attributes if they are discovered otherwise it will crash...
    	attributeMetaDataMap.put("shape", new AttributeMetaData(false, true, false));
    	attributeMetaDataMap.put("angle", new AttributeMetaData(false, true, false));
    	attributeMetaDataMap.put("size", new AttributeMetaData(false, true, false));
    	attributeMetaDataMap.put("width", new AttributeMetaData(false, true, false));
    	attributeMetaDataMap.put("height", new AttributeMetaData(false, true, false));
    	attributeMetaDataMap.put("alignment", new AttributeMetaData(false, true, false));
    	attributeMetaDataMap.put("fill", new AttributeMetaData(false, true, false));
    	attributeMetaDataMap.put("left-of", new AttributeMetaData(true, false, false));
    	attributeMetaDataMap.put("above", new AttributeMetaData(true, false, false));
    	attributeMetaDataMap.put("overlaps", new AttributeMetaData(true, false, false));
    	attributeMetaDataMap.put("inside", new AttributeMetaData(true, false, false));
		//left-of
		//above
		//overlaps
		//inside
	}
}
class AttributeMetaData {
	private boolean isRelative;
	private boolean isNominal;
	private boolean isNumerical;
		public AttributeMetaData(boolean isRelative, boolean isNominal, boolean isNumerical)
		{
			this.isRelative = isRelative;
			this.isNominal = isNominal;
			this.isNumerical = isNumerical;
		}
		public boolean isNumerical() {
			return isNumerical;
		}
		public boolean isNominal() {
			return isNominal;
		}
		public boolean isRelative() {
			return isRelative;
		}
}

package API.StringSlicer;

public class StringSlicer {
    public static String sliceStart(String s, int startIndex){
        if (startIndex < 0) startIndex = s.length() + startIndex;
        return s.substring(startIndex);
    }

    public static String sliceEnd(String s, int endIndex){
        if (endIndex < 0) endIndex = s.length() + endIndex;
        return s.substring(0, endIndex);
    }

    public static String sliceRange(String s, int startIndex, int endIndex){
        if (startIndex < 0) startIndex = s.length() + startIndex;
        if (endIndex < 0) endIndex = s.length() + endIndex;
        return s.substring(startIndex, endIndex);
    }
}

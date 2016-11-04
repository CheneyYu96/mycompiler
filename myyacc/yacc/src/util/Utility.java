package util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yuminchen on 16/10/22.
 */
public class Utility {

    public final static String inputFileName = "doc/input.txt";

    public final static String tokenFileName = "doc/tokens.txt";

    public final static String defFileName = "doc/definition.txt";

    private static List<String> keyWords = Arrays.asList("public","protect","private","new","class",
            "void", "static", "int", "char", "float", "double", "if", "else", "switch", "case" ,
            "for","while", "try", "catch");

    public static boolean isKeyWord(String word){
        if(keyWords.contains(word)){
            return true;
        }
        return false;
    }

}

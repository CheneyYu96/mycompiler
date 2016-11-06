package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuminchen on 16/10/22.
 */
public class Utility {

    public final static List<String> inputFileName = Arrays.asList("doc/input1.txt","doc/input2.txt");

    public final static List<String> tokenFileName = Arrays.asList("doc/tokens1.txt","doc/tokens2.txt");

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

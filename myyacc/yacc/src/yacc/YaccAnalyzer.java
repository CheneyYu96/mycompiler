package yacc;

import util.ParsingTable;
import util.Utility;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/11/3.
 */
public class YaccAnalyzer {

    private ParsingTable parsingTable;

    public YaccAnalyzer() {
        parsingTable = new ParsingTable();
    }

    public void readDefinition(){

        boolean isProduction = false;
        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(Utility.defFileName)));
            String line;

            while ((line = br.readLine())!=null){

                if(line.equals("%%")){
                    isProduction = true;
                    continue;
                }

                if(isProduction){
                    parsingTable.addProduction(line);
                }
                else {
                    String[] spl1 = line.split(":");
                    // error condition
                    if(spl1==null||spl1.length<2){
                        System.err.println("definition format wrong");
                        break;
                    }

                    Set<String> set = new HashSet<>();
                    String[] values = spl1[1].split(",");
                    for(String value : values){
                        set.add(value);
                    }
                    // terminal condition
                    if(spl1[0].equals("Vt")){
                        parsingTable.setTerminal(set);
                    }
                    // non-terminal condition
                    else if(spl1[0].equals("Vn")){
                        parsingTable.setNonTerminal(set);
                    }
                    // error
                    else {
                        System.err.println("definition format wrong");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * build parsing table using first and follow
     */
    public ParsingTable buildParsingTable(){
        readDefinition();
        for(String pro: parsingTable.getProductions()){
           Map<String, String> subMap = new HashMap<>();
           // epsilon production, calculate follow
           if(pro.split(":")[1].equals("e")){
               String nonTerm = pro.split(":")[0];

               Set<String> follow = findFollow(nonTerm);
               follow.forEach( o -> subMap.put(o,pro));
           }
           // calculate first
           else {
               Set<String> first = getOneFirst(pro);
               first.forEach( o -> subMap.put(o,pro));
           }
           parsingTable.addTableMap(pro.split(":")[0],subMap);

       }
        return parsingTable;
    }

    /**
     * recession find first of non terminal
     * @param nonterminal
     * @return
     */
    private Set<String> findFirst(String nonterminal){

        List<String> relatedPros = parsingTable
                .getProductions()
                .stream()
                .filter(o -> o.split(":")[0].equals(nonterminal))
                .collect(Collectors.toList());

        Set<String> result = new HashSet<>();
        relatedPros
                .stream()
                .forEach(o -> result.addAll(getOneFirst(o)));

        return result;
    }

    private Set<String> getOneFirst(String production){
        String[] spl = production.split(":");
        Set<String> result = new HashSet<>();
        String right = spl[1];

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i<right.length(); i++){
            sb.append(right.charAt(i));

            if(parsingTable.getTerminal().contains(sb.toString())){
                result.add(sb.toString());
                break;
            }
            else if (parsingTable.getNonTerminal().contains(sb.toString())){
                Set<String> tmp = findFirst(sb.toString());
                // if set contains epsilon, remove epsilon and find next set
                if(tmp.contains("e")){
                    tmp.remove("e");
                    result.addAll(tmp);
                    sb.delete(0, sb.length());
                }
                else {
                    result.addAll(tmp);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * recession find follow of non terminal
     * @param nonterminal
     * @return
     */
    private Set<String> findFollow(String nonterminal){
        List<String> relatedPros = parsingTable
                .getProductions()
                .stream()
                .filter(o -> o.split(":")[1].contains(nonterminal))
                .collect(Collectors.toList());

        Set<String> result = new HashSet<>();
        relatedPros
                .stream()
                .forEach(o -> result.addAll(getOneFollow(nonterminal,o)));
        if(nonterminal.equals("S")){
            result.add("$");
        }

        return result;
    }

    private Set<String> getOneFollow(String nonTerm, String production){
        String left = production.charAt(0)+"";
        String[] spl = production.substring(1).split(nonTerm);
        Set<String> result = new HashSet<>();

        if(spl.length==1){
            //if same with the origin non terminal
            if(left.equals(nonTerm)){
                return result;
            }
            result.addAll(findFollow(left));
        }
        else {
            for(int i = 1; i<spl.length; i++){

                StringBuffer sb = new StringBuffer();
                for(int j = 0; j<spl[i].length(); j++){
                    sb.append(spl[i].charAt(j));

                    if(parsingTable.getTerminal().contains(sb.toString())){
                        result.add(sb.toString());
                        break;
                    }
                    else if (parsingTable.getNonTerminal().contains(sb.toString())){

                        Set<String> tmpFirst = findFirst(sb.toString());
                        // if set contains epsilon, remove epsilon and find next set
                        if(tmpFirst.contains("e")){
                            tmpFirst.remove("e");
                            result.addAll(tmpFirst);
                            Set<String> tmpFollow = findFollow(left);
                            result.addAll(tmpFollow);
                            sb.delete(0, sb.length());
                        }
                        else {
                            result.addAll(tmpFirst);
                            break;
                        }
                    }
                }

            }
        }
        return result;
    }

    public static void main(String[] args) {
        YaccAnalyzer yaccAnalyzer = new YaccAnalyzer();
        yaccAnalyzer.buildParsingTable().printTableMap();

    }
}

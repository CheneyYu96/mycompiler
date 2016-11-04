package util;

import java.util.*;

/**
 * Created by yuminchen on 16/11/3.
 */
public class ParsingTable {


    private List<String> productions;

    private Map<String, Map<String,String>> tableMap;

    private Set<String> terminal;

    private Set<String> nonTerminal;

    public ParsingTable() {
        tableMap = new HashMap<>();
        productions = new ArrayList<>();
    }

    public ParsingTable(Set<String> terminal, Set<String> nonTerminal) {
        this();
        this.terminal = terminal;
        this.nonTerminal = nonTerminal;
    }

    public List<String> getProductions() {
        return productions;
    }

    public void addProduction(String production) {
        productions.add(production);
    }

    public Set<String> getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(Set<String> nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public Set<String> getTerminal() {
        return terminal;
    }

    public void setTerminal(Set<String> terminal) {
        this.terminal = terminal;
    }

    public void addTableMap(String nonterminal, Map<String, String> terminalMap){
        if(tableMap.keySet().contains(nonterminal)){
            Map<String ,String> subMap = tableMap.get(nonterminal);
            subMap.putAll(terminalMap);
        }
        else {
            tableMap.put(nonterminal, terminalMap);
        }
    }

    /**
     * print the table map
     */
    public void printTableMap(){
        for (int i = 0; i<productions.size();i++){
            System.out.println(i + "\t" + productions.get(i));
        }

        List<String> terms = new ArrayList<>(terminal);
        terms.add("$");
        terms.remove("e");
        System.out.print("\t");
        terms.forEach(o -> {
            if (o.equals("while")){
                System.out.print("wh\t");
            }
            else if (o.equals("else")){
                System.out.print("el\t");
            }
            else{
                System.out.print(o+"\t");
            }
        });
        System.out.println();

        for (String nonTerm : tableMap.keySet()){

            Map<String,String> subMap = tableMap.get(nonTerm);
            System.out.print(nonTerm+"\t");

            StringBuffer outputString = new StringBuffer();
            int lastIndex = 0;
            for (int i = 0 ; i < terms.size(); i++){

                if(subMap.keySet().contains(terms.get(i))){
                    for(int j = lastIndex; j< i; j++){
                        outputString.append("\t");
                    }
                    lastIndex = i;
                    outputString.append(productions.indexOf(subMap.get(terms.get(i))));
                }
            }
            System.out.println(outputString.toString());
        }
    }
}

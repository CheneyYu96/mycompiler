package main;

import util.ParsingTable;
import util.Token;
import util.Type;
import util.Utility;
import yacc.YaccAnalyzer;

import java.io.*;
import java.util.*;

/**
 * Created by yuminchen on 16/11/3.
 */
public class Main {
    Stack<String> stack;

    List<Token> tokenList;

    List<String> sequences;

    ParsingTable table;

    /**
     *
     */
    public Main(String fileName) {
        table = new YaccAnalyzer().buildParsingTable();
        stack = new Stack<>();
        tokenList = new ArrayList<>();
        sequences = new ArrayList<>();
        readTokenList(fileName);
    }

    /**
     * read token list
     */
    private void readTokenList(String fileName){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line;

            while ((line = br.readLine())!=null){
                tokenList.add(Token.string2Token(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tokenList.add(new Token("$", Type.END));
    }

    /**
     *
     * @return sequences of production
     */
    public List<String> handle(){
        /*
        initialize the stack value
         */
        stack.push("$");
        stack.push("S");

        Iterator<Token> iterator = tokenList.iterator();
        boolean needMoveReader = true;
        Token token = null;
        while (true){

            String stackValue = stack.pop();
            if(needMoveReader) {
                needMoveReader = false;
                token = iterator.next();
            }

            if(stackValue.equals("$")&&token.isEnd()){
                System.out.println("success");
                break;
            }

            if(table.isNonTerm(stackValue)){
                Map<String, String> subMap = table.getTableMap().get(stackValue);

                String pro = "";

                if(subMap.keySet().contains(token.getValue())){
                    pro = subMap.get(token.getValue());
                }
                // id or num
                else if(subMap.keySet().contains(token.getType().name().toLowerCase())){
                    pro = subMap.get(token.getType().name().toLowerCase());

                }
                else {
                    System.err.println("N:Syntactic error! At line "+token.getLine());
                    break;
                }

                String[] spl = pro.split(":");

                // update stack
                String right = spl[1];
                Stack<String> tmpStack = new Stack<>();
                StringBuffer sb = new StringBuffer();

                for(int i = 0; i < right.length(); i++){
                    sb.append(right.charAt(i));
                    if(table.isTerminal(sb.toString())||table.isNonTerm(sb.toString())){
                        //special case = , ==
                        if(sb.toString().equals("=")&&right.charAt((i+1))=='='){
                            tmpStack.push("==");
                            i++;
                        }
                        // epsilon , else
                        else if(sb.toString().equals("e")){
                            if(right.length()==1){
                                break;
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            tmpStack.push(sb.toString());
                        }
                        sb.delete(0, sb.length());
                    }
                }

                while (!tmpStack.isEmpty()){
                    stack.push(tmpStack.pop());
                }

                sequences.add(pro);
            }
            // terminal symbol
            else if(table.isTerminal(stackValue)){

                if(stackValue.equals(token.getValue())||
                        token.getType().name().equals(stackValue.toUpperCase())){
                    needMoveReader = true;
                    continue;
                }
                else {
                    System.err.println("T:Syntactic error! At line "+token.getLine());
                    break;
                }
            }
            else {
                System.err.println("error!");
                System.exit(0);
            }


        }

        return sequences;
    }



    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        for(String name : Utility.tokenFileName) {
            System.out.println(name);
            Main main = new Main(name);
            List<String> seq = main.handle();
            seq.forEach(o -> System.out.println(o));
            System.out.println();
        }
    }
}

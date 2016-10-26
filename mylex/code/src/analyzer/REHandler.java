package analyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by yuminchen on 16/10/22.
 */
public class REHandler {

    private List<String> initExpr;

    private List<String> usableExpr;


    /**
     * set initial expr
     * @param initExpr
     */
    public REHandler(List<String> initExpr) {
        this.initExpr = initExpr;
        usableExpr = new ArrayList<>();
    }


    public REHandler() {
        initExpr = new ArrayList<>();
        usableExpr = new ArrayList<>();
        getExpr();
    }


    private void getExpr(){

        String fileName = "doc/RE.txt";
        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String re;

            while ((re = br.readLine())!=null){
                initExpr.add(re);
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String addDot(String re){

        String newExpr = "";

        for (int i = 0; i < re.length() - 1; i++) {
            if (isCharacter(re.charAt(i)) && isCharacter(re.charAt(i + 1))) {
                newExpr += re.charAt(i) + ".";

            } else if (isCharacter(re.charAt(i)) && re.charAt(i + 1) == '(') {
                newExpr += re.charAt(i) + ".";

            } else if (re.charAt(i) == ')' && isCharacter(re.charAt(i + 1))) {
                newExpr += re.charAt(i) + ".";

            } else if (re.charAt(i) == '*' && isCharacter(re.charAt(i + 1))) {
                newExpr += re.charAt(i) + ".";

            } else if (re.charAt(i) == '*' && re.charAt(i + 1) == '(') {
                newExpr += re.charAt(i) + ".";

            } else if (re.charAt(i) == ')' && re.charAt(i + 1) == '(') {
                newExpr += re.charAt(i) + ".";

            } else {
                newExpr += re.charAt(i);
            }
        }
        newExpr += re.charAt(re.length() - 1);

        return newExpr;

    }

    private boolean isCharacter(char ch){
        if (ch == 'a'||ch == 'b'||ch == 'c'){
            return true;
        }
        return false;
    }

    /**
     *
     * @param infix
     * @return
     */
    private String infix2suffix(String infix){

        Stack<Character> operatorStack = new Stack<>();
        String suffix = "";

        for (int i = 0; i < infix.length(); i++){

            char ch = infix.charAt(i);
            if (ch =='('){
                operatorStack.push('(');
            }

            else if (ch ==')'){
                while (!operatorStack.isEmpty()&&operatorStack.peek()!='('){
                    suffix = suffix + operatorStack.pop();
                }
                operatorStack.pop();
            }

            else if (ch=='*'||ch=='.'||ch=='|'){
                if(operatorStack.contains('(')){
                    operatorStack.push(ch);
                    continue;
                }
                // if the priority lower than the top value of stack, pop
                while (!operatorStack.isEmpty()&&comparePriority(ch,operatorStack.peek())){
                    suffix = suffix + operatorStack.pop();
                }
                operatorStack.push(ch);
            }

            else {
                suffix = suffix + ch;
            }

        }

        for (;!operatorStack.isEmpty();){
            suffix = suffix + operatorStack.pop();
        }

        return suffix;
    }


    /**
     * if second's priority >= the first, return true
     * else return false
     *
     * '*'>'.'>'|'
     * @param firstOperator
     * @param secondOperator
     * @return
     */
    private boolean comparePriority(char firstOperator, char secondOperator){

        if (firstOperator == '|'){
            return true;
        }
        if (firstOperator == '.' && secondOperator != '|'){
            return true;
        }
        if (firstOperator == '*' && secondOperator == '*'){
            return true;
        }

        return false;
    }


    /**
     * handle the expr
     * @return
     */
    public List<String> handle(){

        for (String re : initExpr){
            String infix = addDot(re);
            usableExpr.add(infix2suffix(infix));
        }

        return usableExpr;
    }


    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        List<String> expr = new REHandler().handle();
        expr
                .stream()
                .forEach(o -> System.out.println(o));
    }

}

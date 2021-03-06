package lex;

import util.Token;
import util.Type;
import util.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuminchen on 16/10/22.
 */
public class Lex {

    private int lineNo;


    private List<Token> tokens;

    public Lex() {
        tokens = new ArrayList<>();
        lineNo = 0;
    }

    public void clear(){
        lineNo = 0;
        tokens = new ArrayList<>();
    }

    public List<Token> handle(String inputFileName, String outputFileName) throws IOException {


        BufferedReader br = null;
        BufferedWriter bw = null;

        File outputFile = new File(outputFileName);
        if (!outputFile.exists()){
            outputFile.createNewFile();
        }

        try {
            br = new BufferedReader(new FileReader(new File(inputFileName)));
            bw = new BufferedWriter(new FileWriter(outputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String line;
        while ((line = br.readLine())!=null){
            lineNo++;
            tokens.addAll(scan(line,lineNo));
        }

        br.close();

        for(Token token : tokens){
            bw.write(token.toString() + "\n");
        }
        bw.flush();
        bw.close();
        return tokens;
    }

    /**
    `* scan input line by line
     */
    private List<Token> scan(String line, int lineNo) {
        List<Token> subTokens = new ArrayList<>();
        char[] preChars = line.toCharArray();
        ArrayList<Character> charArray = new ArrayList<>();

        // filter blank character like ' ', '\t'
        for (int i = 0; i < preChars.length; i++) {
            if (preChars[i] == ' ' || preChars[i] == '\t') {
                continue;
            }
            charArray.add(preChars[i]);
        }

        for (int index = 0; index < charArray.size(); index++) {

            // when it is a symbol
            switch (charArray.get(index)) {
                case '<':
                    if (charArray.get(++index) == '=') {
                        subTokens.add(new Token("<=", Type.NM, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("<", Type.LS, lineNo));
                    }
                    break;

                case '>':
                    if (charArray.get(++index) == '=') {
                        subTokens.add(new Token(">=", Type.NL, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token(">", Type.MR, lineNo));
                    }
                    break;

                case '/':
                    // annotation
                    if (charArray.get(++index) == '*') {
                        subTokens.add(new Token("/*", Type.AN, lineNo));
                    } else if (charArray.get(index) == '/') {
                        subTokens.add(new Token("//", Type.AN, lineNo));
                    } else if (charArray.get(index) == '=') {
                        subTokens.add(new Token("/=", Type.ED, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("/", Type.Div, lineNo));
                    }
                    break;

                case '*':
                    // annotation
                    if (charArray.get(++index) == '/') {
                        subTokens.add(new Token("*/", Type.AN, lineNo));
                    } else if (charArray.get(index) == '=') {
                        subTokens.add(new Token("*=", Type.EMU, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("*", Type.Mul, lineNo));
                    }
                    break;

                case '-':
                    if (charArray.get(++index) == '=') {
                        subTokens.add(new Token("-=", Type.EMI, lineNo));
                    }
                    else {
                        index--;
                        subTokens.add(new Token("-", Type.Minus, lineNo));
                    }
                    break;

                case '+':
                    if (charArray.get(++index) == '=') {
                        subTokens.add(new Token("+=", Type.EA, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("+", Type.Add, lineNo));
                    }
                    break;

                case '=':
                    if (charArray.get(++index) == '=') {
                        subTokens.add(new Token("==", Type.EQ, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("=", Type.Assign, lineNo));
                    }
                    break;

                case '!':
                    if (charArray.get(++index) == '=') {
                        subTokens.add(new Token("!=", Type.UE, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("!", Type.NOT, lineNo));
                    }
                    break;

                case '|':
                    if (charArray.get(++index) == '|') {
                        subTokens.add(new Token("||", Type.DOR, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("|", Type.OR, lineNo));
                    }
                    break;

                case '&':
                    if (charArray.get(++index) == '&') {
                        subTokens.add(new Token("&&", Type.DAND, lineNo));
                    } else {
                        index--;
                        subTokens.add(new Token("&", Type.AND, lineNo));
                    }
                    break;

                case '(':
                    subTokens.add(new Token("(", Type.LC, lineNo));
                    break;

                case ')':
                    subTokens.add(new Token(")", Type.RC, lineNo));
                    break;

                case '{':
                    subTokens.add(new Token("{", Type.LM, lineNo));
                    break;

                case '}':
                    subTokens.add(new Token("}", Type.RM, lineNo));
                    break;

                case '[':
                    subTokens.add(new Token("[", Type.LB, lineNo));
                    break;

                case ']':
                    subTokens.add(new Token("]", Type.RB, lineNo));
                    break;

                case '.':
                    subTokens.add(new Token(".", Type.Dot, lineNo));
                    break;

                default:
                    break;
            }

            // when it is a number
            if (Character.isDigit(charArray.get(index))) {

                int numberIndex = 0;
                boolean isFirstDoc = true;
                StringBuffer number = new StringBuffer();
                for (; numberIndex + index < charArray.size(); numberIndex++) {
                    if (Character.isDigit(charArray.get(index + numberIndex))) {
                        number.append(charArray.get(index + numberIndex));
                        continue;
                    }
                    if (charArray.get(index + numberIndex) == '.' && isFirstDoc) {
                        isFirstDoc = false;
                        number.append(charArray.get(index + numberIndex));
                        continue;
                    }
                    break;
                }

                subTokens.add(new Token(number.toString(), Type.NUM, lineNo));

                // adjust the index
                index += numberIndex;
                index--;
            }

            // when it is a word(keyword or normal word)
            if (Character.isLetter(charArray.get(index))) {

                int wordIndex = 0;
                StringBuffer word = new StringBuffer();
                boolean isKeyword = false;

                for (; wordIndex + index < charArray.size(); wordIndex++) {

                    if (Character.isLetterOrDigit(charArray.get(index + wordIndex))) {
                        word.append(charArray.get(index + wordIndex));

                        if (Utility.isKeyWord(word.toString())) {
                            subTokens.add(new Token(word.toString(),Type.valueOf(word.toString().toUpperCase()),lineNo));
                            isKeyword = true;
                            break;
                        }

                    }
                    else {
                        break;
                    }

                }

                index += wordIndex;
                if(!isKeyword){
                    String normalWord = word.toString();
                    subTokens.add(new Token(normalWord, Type.ID, lineNo));
                    index -- ;
                }

            }

        }

        return subTokens;
    }

    /**
     * test lex
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Lex lex = new Lex();
        for (int i = 0;i < Utility.inputFileName.size(); i++){
            lex.handle(Utility.inputFileName.get(i),Utility.tokenFileName.get(i));
            lex.clear();
        }

    }
}

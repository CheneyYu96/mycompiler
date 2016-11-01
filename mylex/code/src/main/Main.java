package main;
import lex.Lex;
import util.Token;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by yuminchen on 16/10/22.
 */
public class Main {

    private String outFileName = "doc/output.txt";

    /**
     * save tokens to output file
     * @param tokens
     * @throws IOException
     */
    private void saveTokens(List<Token> tokens) throws IOException{

        File file = new File(outFileName);

        if(!file.exists()){
            file.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
        bw.write("value   type   line");
        bw.write("\n");

        for(Token t : tokens){
            System.out.println(t.toString());
            bw.write(t.toString());
            bw.newLine();
        }

        bw.flush();
        bw.close();

    }

    public static void main(String[] args) {

        Main main = new Main();
        try {
            main.saveTokens(new Lex().handle());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

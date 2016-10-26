package util;

/**
 * Created by yuminchen on 16/10/22.
 *
 * token consist of value, type ,line
 * for instants, ("a1",ID,1)
 */
public class Token {

    private String value;

    private Type type;

    private int line;

    @Override
    public String toString(){
        return "( \"" + value + "\", " + type.name() + ", "+line + " )";
    }

    /**
     *
     * @param value
     * @param type
     * @param line
     */
    public Token(String value, Type type, int line) {
        this.value = value;
        this.type = type;
        this.line = line;
    }


}

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
        return  value + "," + type.name() + ","+line;
    }


    public Token(String value, Type type) {
        this.value = value;
        this.type = type;
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

    /**
     *
     * @param value
     * @return
     */
    public static Token string2Token(String value){
        String[] spl = value.split(",");
        return new Token(spl[0],Type.valueOf(spl[1]),Integer.parseInt(spl[2]));
    }

    public Type getType() {
        return type;
    }

    public int getLine() {

        return line;
    }

    public String getValue() {

        return value;
    }

    public boolean isEnd(){
        return type==Type.END;

    }

}

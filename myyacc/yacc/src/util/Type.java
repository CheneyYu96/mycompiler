package util;

/**
 * Created by yuminchen on 16/10/22.
 */
public enum Type {
    /**
     * including (  [  these symbols and so on
     * {
     */
    LM,
    /**
     * }
     */
    RM,
    /**
     *(
     */
    LC,
    /**
     * )
     */
    RC,
    /**
     * [
     */
    LB,
    /**
     * ]
     */
    RB,

    /**
     * annotation // or /*
    */
    AN,

    /**
     * .
     */
    Dot,


    /**
     * operator like + - * / =
     */
    Add,
    EA,
    Minus,
    EMI,
    Mul,
    EMU,
    Div,
    ED,
    Assign,

    /**
     * compare
     */
    MR,
    NM,
    LS,
    NL,
    EQ,
    UE,


    /**
     * or and
     */
    OR,
    DOR,
    AND,
    DAND,
    NOT,


    /**
     * normal words, including function name and variable name
     */
    ID,

    /**
     * number, including integer and double(float)
     */
    NUM,

    /**
     * means reserved word, including public, class, void these kind of word
     * details seen in Utility
     */
    PUBLIC, PROTECT, PRIVATE, NEW, CLASS, VOID, STATIC, INT, CHAR, FLOAT, DOUBLE, IF,
    ELSE, SWITCH, CASE, FOR, WHILE, TRY, CATCH,

    /**
     * end symbol $
     */
    END

}

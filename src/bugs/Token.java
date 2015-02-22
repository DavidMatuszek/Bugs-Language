package bugs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Tokens specific to the Bugs language.
 * @author David Matuszek
 * @version February 2015
 */
public class Token {
    static enum Type { KEYWORD, NAME, NUMBER, SYMBOL, ERROR, EOL, EOF }
    private static final Pattern NAME_REGEX = Pattern.compile("[a-zA-Z_]\\w*");
    private static final Pattern NUMBER_REGEX = Pattern.compile("(\\d+\\.\\d*)|(\\.?\\d+)");
    private static final Pattern SYMBOL_REGEX = Pattern.compile("[^\\w]+");

    private static final String[] KEYWORD_LIST = new String[] {
        "Allbugs", "Bug", "move", "moveto", "turn", "turnto", "line",
        "loop", "exit", "if", "switch", "case", "return", "do", "color",
        "define", "using", "var", "initially", "background",
        "black", "blue", "cyan", "darkGray", "gray", "green", "lightGray",
        "magenta", "orange", "pink", "red", "white", "yellow", "brown",
        "purple", "none" };

    private static final String[] PSEUDO_KEYWORD_LIST = new String[] {
        "assign", "block", "call", "function", "list"
    };

    private static final String[] COLOR_LIST = new String[] {
        "black", "blue", "cyan", "darkGray", "gray", "green", "lightGray",
        "magenta", "orange", "pink", "red", "white", "yellow", "brown",
        "purple", "none" };

    /** The set of strings that are considered to be keywords. */
    public static final Set<String> KEYWORDS =
        new HashSet<>(Arrays.asList(KEYWORD_LIST));

    /** The set of strings that are considered to be pseudo keywords. */
    public static final Set<String> PSEUDO_KEYWORDS =
            new HashSet<>(Arrays.asList(PSEUDO_KEYWORD_LIST));

    /** The set of strings that are considered to be color names. */
    public static final Set<String> COLORS =
        new HashSet<>(Arrays.asList(COLOR_LIST));

    /** The type of this token (name, number, etc.). */
    final Type type;
    /** The characters that make up this token. */
    final String value;

    /**
     * Constructor for Tokens.
     * 
     * @param type The type of the token, chosen from the above list.
     * @param value The characters making up the token.
     */
    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Constructor for Tokens.
     * 
     * @param value The characters making up the token. The type is
     * determined from the token.
     */
    public Token(String value) {
        this.type = typeOf(value);
        this.value = value;
    }

    /**
     * Determine the token type of the given string. A null string
     * is considered to represent the end of file.
     * @param s The string to classify.
     * @return The type of the string.
     */
    public static Token.Type typeOf(String s) {
        if (s == null) return Token.Type.EOF;
        if (s.equals("\n")) return Token.Type.EOL;
        if (SYMBOL_REGEX.matcher(s).matches()) return Token.Type.SYMBOL;
        if (NUMBER_REGEX.matcher(s).matches()) return Token.Type.NUMBER;
        if (NAME_REGEX.matcher(s).matches()) {
            if (Token.KEYWORDS.contains(s)) return Token.Type.KEYWORD;
            if (Token.PSEUDO_KEYWORDS.contains(s)) return Token.Type.KEYWORD;
            return Token.Type.NAME;
        }
        return Token.Type.ERROR;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Token) {
            Token that = (Token) o;
            return this.type == that.type && this.value.equals(that.value);
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return type + ":" + value;
    }

    /**
     * Returns <code>true</code> if the argument is a recognized keyword,
     * <code>false</code> otherwise.
     * @param s The possible keyword.
     * @return <code>true</code> if the argument is a keyword.
     */
    public static boolean isKeyword(String s) {
        return KEYWORDS.contains(s);
    }

    /**
     * Returns <code>true</code> if the argument is a recognized color,
     * <code>false</code> otherwise.
     * @param s The possible color name.
     * @return <code>true</code> if the argument is a keyword.
     */
    public static boolean isColor(String s) {
        return COLORS.contains(s);
    }
}
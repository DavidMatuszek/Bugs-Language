package bugs;

import java.util.StringTokenizer;

import tree.Tree;
/**
 * @author David Matuszek
 * @version February 2015
 */
public class TreeParser {
    
    /**
     * Given a String, construct a Tree of Tokens. The syntax is:<br>
     * &lt;tree&gt; ::= &lt;word&gt; [ "(" &lt;tree&gt; { &lt;tree&gt; } ")" ]
     * @param input The String representation of a tree..
     * @return The tree described by the input.
     */
    Tree<Token> parse(String input) {
        return makeTree(new PushbackStringTokenizer(input));
    }
    
    /**
     * Given a PushbackStringTokenizer (which returns Strings),
     * construct a Tree of Tokens.
     * 
     * @param tokenizer The source of the Tokens.
     * @return The tree corresponding to this sequence of Tokens.
     */
    private Tree<Token> makeTree(PushbackStringTokenizer tokenizer) {
        //  <tree> ::= <word> [ "(" <tree> { <tree> } ")" ]
        Token token;
        Tree<Token> tree;
        // Note: A StringTokenizer returns Strings, not Tokens!
        assert tokenizer.hasNext();
        Tree<Token> root = new Tree<>(nextActualToken(tokenizer));  // <word>
        if (!tokenizer.hasNext()) return root;
        
        token = nextActualToken(tokenizer);             // [ "("
        if (!"(".equals(token.value)) {
            tokenizer.pushBack(token.value);
            return root;
        }
        tree = makeTree(tokenizer);                // <tree>
        root.addChild(tree);
        
        assert tokenizer.hasNext();
        token = nextActualToken(tokenizer);
        while (!token.value.equals(")")) {
            tokenizer.pushBack(token.value);
            tree = makeTree(tokenizer);            // <tree> }
            root.addChild(tree);
            if (!tokenizer.hasNext()) break;
            token = nextActualToken(tokenizer);
        }
        return root;
    }

    
    /**
     * Gets a "token" of type String from the PushbackStringTokenizer
     * and returns a token of type Token.
     * @param tokenizer The input source of string tokens.
     * @return The next string, as a Token.
     */
    private static Token nextActualToken(PushbackStringTokenizer tokenizer) {
        String s = tokenizer.next();
        return new Token(s);
    }

    
    //---------------------------------------------------------------------
    
    /**
     * A Tokenizer that returns one of four things: a left parenthesis, a
     * right parenthesis, a sequence of non-whitespace, non-parenthesis
     * characters, or <code>null</code> if there are no more tokens.
     * 
     * @author David Matuszek
     */
    static class PushbackStringTokenizer {
        private StringTokenizer tokenizer;
        private String pushedValue = null;
        
        /**
         * Constructs a tokenizer for the input that uses whitespace and
         * parentheses as delimiters.
         * 
         * @param input The string to be tokenized.
         */
        PushbackStringTokenizer(String input) {
            tokenizer = new StringTokenizer(input, " \t\n\r\f()", true);
            pushedValue = null;
        }
        
        /**
         * Tests if there are more tokens in the input string.
         * 
         * @return <code>true</code> if there are more tokens,
         *         <code>false</code> otherwise.         
         */
        boolean hasNext() {
            return pushedValue != null || tokenizer.hasMoreTokens();
        }
        
        /**
         * Returns the next token (or a pushed back token, if there is
         * one.) A token may be a left parenthesis, a right parenthesis,
         * or any sequence of other, non-whitespace characters.
         * <p>
         * Unlike most tokenizers, this tokenizer will return
         * <code>null</code> if there are no remaining tokens.
         * 
         * @return The next token, or <code>null</code> if there are no more.
         */
        String next() {
            String temp = pushedValue;
            if (temp == null && tokenizer.hasMoreTokens()) {
                temp = tokenizer.nextToken().trim();
            }
            pushedValue = null;
            // skip whitespace tokens
            if (temp != null && temp.length() == 0) {
                temp = next();
            }
            return temp;
        }
        
        /**
         * Returns a token to this tokenizer so that it will be returned by
         * the next call to the <code>next()</code> method.
         * 
         * @param token The token to be reused.
         */
        void pushBack(String token) {
            pushedValue = token;
        }
    }
}

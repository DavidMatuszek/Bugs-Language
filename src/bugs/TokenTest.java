package bugs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author David Matuszek
 * @version February 2015
 */
public class TokenTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test method for {@link bugs.Token#hashCode()}.
     */
    @Test
    public final void testHashCode() {
        Token t1 = new Token("switch");
        Token t2 = new Token(Token.Type.KEYWORD, "switch");
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    /**
     * Test method for {@link bugs.Token#Token(bugs.Token.Type, java.lang.String)}.
     */
    @Test
    public final void testToken_Type_String() {
        Token t = new Token(Token.Type.KEYWORD, "loop");
        assertEquals(t.type, Token.Type.KEYWORD);
        assertEquals(t.value, "loop");
    }

    /**
     * Test method for {@link bugs.Token#Token(java.lang.String)}.
     */
    @Test
    public final void testToken_String() {
        Token t = new Token("loop");
        assertEquals(Token.Type.KEYWORD, t.type);
        assertEquals("loop", t.value);
        Token t2 = new Token("call");
        assertEquals(Token.Type.KEYWORD, t2.type);
    }

    /**
     * Test method for {@link bugs.Token#Token(java.lang.String)}.
     */
    @Test
    public final void testToken_NumericString() {
        Token t = new Token("5");
        assertEquals(Token.Type.NUMBER, t.type);
        assertEquals("5", t.value);
        t = new Token("5.0");
        assertEquals(Token.Type.NUMBER, t.type);
        assertEquals("5.0", t.value);
    }

    /**
     * Test method for {@link bugs.Token#typeOf(java.lang.String)}.
     */
    @Test
    public final void testTypeOf() {
        assertEquals(Token.Type.NAME, Token.typeOf("abc"));
        assertEquals(Token.Type.NAME, Token.typeOf("Abc_123"));
        
        assertEquals(Token.Type.NUMBER, Token.typeOf("123"));
        assertEquals(Token.Type.NUMBER, Token.typeOf("123.45"));
        assertEquals(Token.Type.NUMBER, Token.typeOf("123."));
        assertEquals(Token.Type.NUMBER, Token.typeOf(".45"));
        
        assertEquals(Token.Type.KEYWORD, Token.typeOf("Allbugs"));
        assertEquals(Token.Type.KEYWORD, Token.typeOf("move"));
        assertEquals(Token.Type.KEYWORD, Token.typeOf("red"));
        assertEquals(Token.Type.KEYWORD, Token.typeOf("list"));
        assertEquals(Token.Type.KEYWORD, Token.typeOf("call"));

        assertEquals(Token.Type.SYMBOL, Token.typeOf("*"));
        assertEquals(Token.Type.SYMBOL, Token.typeOf("?"));
        assertEquals(Token.Type.SYMBOL, Token.typeOf("<="));

        assertEquals(Token.Type.EOL, Token.typeOf("\n"));
        
        assertEquals(Token.Type.EOF, Token.typeOf(null));

        assertEquals(Token.Type.ERROR, Token.typeOf("123abc"));
        assertEquals(Token.Type.ERROR, Token.typeOf("123.45.6"));
        assertEquals(Token.Type.ERROR, Token.typeOf("=7"));
    }

    /**
     * Test method for {@link bugs.Token#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        assertEquals(new Token(Token.Type.NUMBER, "123"),
                new Token(Token.Type.NUMBER, "123"));
        assertEquals(new Token("123"), new Token("123"));
        assertEquals(new Token("123"), new Token(Token.Type.NUMBER, "123"));
        assertTrue(new Token("123").equals(new Token("123")));
    }

    /**
     * Test method for {@link bugs.Token#toString()}.
     */
    @Test
    public final void testToString() {
        Token t = new Token("switch");
        // Sometimes I like to print the type as well as the value,
        // sometimes I don't
        assertTrue(t.toString().contains("switch"));
    }

    /**
     * Test method for {@link bugs.Token#isKeyword(java.lang.String)}.
     */
    @Test
    public final void testIsKeyword() {
        assertTrue(Token.isKeyword("loop"));
        assertFalse(Token.isKeyword("algebra"));
    }

    /**
     * Test method for {@link bugs.Token#isColor(java.lang.String)}.
     */
    @Test
    public final void testIsColor() {
        assertTrue(Token.isColor("red"));
        assertFalse(Token.isColor("algebra"));
    }

}

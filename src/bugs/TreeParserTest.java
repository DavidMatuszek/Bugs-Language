/**
 * 
 */
package bugs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import tree.Tree;

/**
 * @author David Matuszek
 */
public class TreeParserTest {
    TreeParser tp;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        tp = new TreeParser();
    }

    /**
     * Test method for {@link bugs.TreeParser#parse(java.lang.String)}.
     */
    @Test
    public final void testParse() {
        Tree<Token> t1, t2, t3, t4, t5, t6, t7;
        /*      a           t1
         *     / \
         *    /   \
         *   b    loop      t2 & t3
         *  / \    / \
         * c  d  red 5.0    t4 ... t7
         *    
         */
        t4 = new Tree<>(new Token("c"));
        t5 = new Tree<>(new Token("d"));
        t6 = new Tree<>(new Token("red"));
        t7 = new Tree<>(new Token("5"));
        t2 = new Tree<>(new Token("b"));
        t3 = new Tree<>(new Token("loop"));
        t1 = new Tree<>(new Token("a"));
        assertEquals(t4, tp.parse("c"));
        assertEquals(t5, tp.parse("d"));
        assertEquals(t6, tp.parse("red"));
        assertEquals(t7, tp.parse("5"));
        assertEquals(t2, tp.parse("b"));
        assertEquals(t3, tp.parse("loop"));
        assertEquals(t1, tp.parse("a"));
    }

}

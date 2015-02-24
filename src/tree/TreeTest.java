package tree;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class TreeTest {
    Tree<String> one, two, three, four, five, six, seven, eight;
    Tree<String> tree1, tree2;
    
    /**
     * Creates the following Tree:<pre>
     *          one
     *         /   \
     *       two  three
     *            /   \
     *         four   five
     *               / |  \
     *              /  |   \
     *           six seven eight</pre>
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        makeTreesForTesting();
        tree1 = new Tree<String>("one", two, three);
        tree2 = new Tree<String>("one", two, three);
    }

    private void makeTreesForTesting() {
        eight = new Tree<String>("eight");
        seven = new Tree<String>("seven");
        six   = new Tree<String>("six");
        five  = new Tree<String>("five", six, seven, eight);
        four  = new Tree<String>("four");
        three = new Tree<String>("three", four, five);
        two   = new Tree<String>("two");
        one   = new Tree<String>("one", two, three);
    }

    @Test
    public final void testTreeConstructor() {
        assertEquals(two, new Tree<String>("two"));
        assertEquals(five, new Tree<String>("five", six, seven, eight));
    }
    
    @Test
    public final void testTreeWithNonStringValues() {
        Tree<Integer> tree = new Tree(5);
        tree.addChild(0, new Tree<Integer>(2));
        tree.addChildren(new Tree<Integer>(3), new Tree<Integer>(4));
        assertEquals(new Integer(5), tree.getValue());
        tree.setValue(1);
        assertEquals(new Integer(1), tree.getValue());
        assertEquals(3, tree.getNumberOfChildren());
        Iterator<Tree<Integer>> iter = tree.iterator();
        assertTrue(iter.hasNext());
        assertEquals(new Integer(2), iter.next().getValue());
        Tree<Integer> child = new Tree<Integer>(5);
        tree.getChild(2).addChildren(child);
        assertFalse(tree.contains(new Tree<Integer>(2)));
        assertTrue(tree.contains(child));
        assertEquals("1(2 3 4(5))", squeezeOutUnnecessaryBlanks(tree.toString()));
    }

    @Test
    public final void testGetAndSetValue() {
        assertEquals("one", one.getValue());
        one.setValue("New value");
        assertEquals("New value", one.getValue());
        two.setValue(null);
        assertEquals(null, two.getValue());
    }

    @Test
    public final void testAddChildAtIndex() {
        assertChildValues(one, "two", "three");
        one.addChild(0, new Tree<String>("zero"));
        assertChildValues(one, "zero", "two", "three");
        one.addChild(1, new Tree<String>("one"));
        assertChildValues(one, "zero", "one", "two", "three");
        one.addChild(4, new Tree<String>("four"));
        assertChildValues(one, "zero", "one", "two", "three", "four");
    }
    
//    // Not in assignment
//    @Test
//    public final void testAddChild() {
//        one.addChild(new Tree<String>("four"));
//        assertChildValues(one, "two", "three", "four");        
//    }

    @Test(expected=IllegalArgumentException.class)
    public final void testAddChildToMakeCircularTree() {
        seven.addChild(0, three);
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testAddNodeAsChildOfItself() {
        seven.addChild(0, three);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public final void testAddChildAtIllegalLocation() {
        three.addChild(3, two);
    }

    @Test
    public final void testAddChildren() {
        assertChildValues(one, "two", "three");
        Tree<String> x = new Tree<String>("x");
        Tree<String> y = new Tree<String>("y");
        Tree<String> z = new Tree<String>("z");
        one.addChildren(x);
        assertChildValues(one, "two", "three", "x");
        one.addChildren(y, z);
        assertChildValues(one, "two", "three", "x", "y", "z");
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testAddChildrenToMakeCircularTree() {
        seven.addChildren(two, four, one, eight);
    }
    
    @Test
    public final void testGetNumberOfChildren() {
        assertEquals(0, two.getNumberOfChildren());
        assertEquals(3, five.getNumberOfChildren());
        Tree<String> x = new Tree<String>("x");
        Tree<String> y = new Tree<String>("y");
        two.addChildren(x, y);
        assertEquals(2, two.getNumberOfChildren());
    }

    @Test
    public final void testGetChild() {
        assertEquals("two", one.getChild(0).getValue());
        assertEquals("three", one.getChild(1).getValue());
    }

    @Test
    public final void testIterator() {
        Iterator<Tree<String>> iter = two.iterator();
        assertFalse(iter.hasNext());
        iter = five.iterator();
        assertEquals("six", iter.next().getValue());
        assertEquals("seven", iter.next().getValue());
        assertEquals("eight", iter.next().getValue());
        assertFalse(iter.hasNext());
    }

    @Test
    public final void testContains() {
        assertTrue(one.contains(one));
        assertTrue(one.contains(two));
        assertTrue(one.contains(eight));
        
        assertFalse(two.contains(one));
        assertFalse(tree1.contains(tree2));
    }

    @Test
    public final void testParse() {
        Tree<String> tree = Tree.parse("one(two three(four five ( six seven eight)))");
        assertEquals(tree1, tree);

        Tree<String> aTree = new Tree<String>("a");
        Tree<String> bTree = new Tree<String>("b");
        Tree<String> cTree = new Tree<String>("c");
        Tree<String> dTree = new Tree<String>("d");
        Tree<String> eTree = new Tree<String>("e");
        Tree<String> fTree = new Tree<String>("f");
        Tree<String> gTree = new Tree<String>("g");
        aTree.addChildren(bTree, cTree);
        bTree.addChildren(dTree, eTree);
        cTree.addChildren(fTree, gTree);
        assertEquals(aTree, Tree.parse("a ( b(d e) c(f g) )"));
    }
    
    @Test(expected=Exception.class)
    public final void testParseUnbalancedLeftParentheses() {
        Tree.parse("one(two three(four five ( six seven eight))");
    }
    
    @Test(expected=Exception.class)
    public final void testParseUnbalancedRightParentheses() {
        Tree.parse("one(two three(four five ( six seven eight))))");
    }
    
    @Test(expected=Exception.class)
    public final void testParseImproperTree() {
        Tree.parse("one two");
    }
    
    @Test(expected=Exception.class)
    public final void testParseImproperTree2() {
        Tree.parse("one (two) three");
    }
    
    @Test(expected=Exception.class)
    public final void testParseImproperTree3() {
        Tree.parse("(one two)");
    }
    
    @Test(expected=Exception.class)
    public final void testParseImproperUseOfParentheses() {
        Tree.parse("one ((two ))");
    }

    @Test
    public final void testToString() {
        Tree<String> tree = Tree.parse("one(two three(four five ( six seven eight)))");
        assertEquals("one(two three(four five(six seven eight)))",
                     squeezeOutUnnecessaryBlanks(tree.toString()));
    }
    
    /**
     * Tests whether the children of the node <code>actual</code> have
     * the expected values. Because varargs are used, the actual value
     * is the first parameter, contrary to usual JUnit practice.
     * 
     * @param actual The result to be examined.
     * @param expected The desired values of the children of <code>actual</code>.
     */
    private void assertChildValues(Tree<String> actual, String... expected) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual.getChild(i).getValue());
        }
    }
    
    /**
     * Removes blanks before and after parentheses and at the beginning
     * and end of the input string, and reduces other sequences of blanks
     * to a single blank.
     * 
     * @param s The string to be squeezed.
     * @return A string with no unnecessary blanks.
     */
    private String squeezeOutUnnecessaryBlanks(String s) {
        s = s.replaceAll(" +", " ");
        s = s.replaceAll(" *\\( *", "(");
        s = s.replaceAll(" *\\) *", ")");
        return s.trim();
    }

    @Test
    public final void testSqueeze() {
        assertEquals("a(b c)((d)e f)",
                     squeezeOutUnnecessaryBlanks("  a (b c) ( (  d)e f  )  "));
        assertEquals("(a(b c)((d)e f",
                     squeezeOutUnnecessaryBlanks("  ( a (b c) ( (  d)e f    "));
    }
    
    // ----- Do not use the following for testing student programs! -----
    
    @Test
    public void testTokenizer() {
        Tree.PushbackStringTokenizer pst =
            new Tree.PushbackStringTokenizer("a(b cat))");
        assertEquals("a", pst.next());
        assertEquals("(", pst.next());
        assertEquals("b", pst.next());
        assertEquals("cat", pst.next());
        assertEquals(")", pst.next());
        assertEquals(")", pst.next());
        assertEquals(null, pst.next());
    }
    
    @Test
    public void testTokenizerWhitespaceHandling() {
        Tree.PushbackStringTokenizer pst =
            new Tree.PushbackStringTokenizer("  a (\tb\tcat))\n");
        assertEquals("a", pst.next());
        assertEquals("(", pst.next());
        assertEquals("b", pst.next());
        assertEquals("cat", pst.next());
        assertEquals(")", pst.next());
        assertEquals(")", pst.next());
        assertEquals(null, pst.next());
    }
//    
//    @Test
//    public void testMarkParentheses() {
//        String markThis = "a(b(c(d))e))))x((f((((((((((((g)))";
//        String expected = " 1 2 3 32 1-== == -123456789AB BA9";
//        assertEquals(expected, Tree.markParentheses(markThis));
//    }
}

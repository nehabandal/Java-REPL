package cs652.repl;

import org.junit.Test;

/**
 * Created by npbandal on 2/3/17.
 */
public class TestPrint {

    @Test
    public void test1() throws Exception {
        String code = "if (true) { print \"hello\"; }";
        System.out.println(JavaREPL.printParsing(code));
    }

    @Test
    public void test2() throws Exception {
        String code = "if (true) { print \"hello\"; print \"hi\"; }";
        System.out.println(JavaREPL.printParsing(code));
    }

}

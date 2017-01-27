package cs652.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class JavaREPL {
    public static void main(String[] args) throws IOException {
        exec(new InputStreamReader(System.in));
    }

    public static void exec(Reader r) throws IOException {
        BufferedReader stdin = new BufferedReader(r);
        NestedReader reader = new NestedReader(stdin);
        int classNumber = 0;
        System.out.print("> ");
//                Class.forName("Interp_0").newInstance()
        while (true) {

            String java = reader.getNestedString();

            System.out.println(java);
            System.out.print("> ");

            // TODO
        }
    }
}

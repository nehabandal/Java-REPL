package cs652.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by npbandal on 1/25/17.
 */
public class NestedReader {
    StringBuilder buf = new StringBuilder();    // fill this as you process, character by character
    BufferedReader input; // where are we reading from?
    int c; // current character of lookahead; reset upon each getNestedString() call
    String abc;

    public NestedReader(BufferedReader input) {
        this.input = input;
    }

    public String getNestedString() throws IOException {


        while ((c = input.read()) != ';') {
            buf.append((char) c);
        }
        String inputString = buf.toString() + ";";
//                  System.out.println(buf.toString());
//        while((c=input.read())!= -1)
//        {
//            switch (c) {
//                case '{':
//                    consume();
////                case '}':
////                    Character a=retrieve();
//                    default:
//                       // return input.readLine();
//
//            }
//        }

        return inputString;

    }


    void consume() throws IOException {

        Stack<Character> stack = new Stack<>();
        buf.append((char) c);
        c = input.read();
        while (c != '}')
            System.out.println((char) c);
        stack.push((char) c);

    }
}

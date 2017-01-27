package cs652.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by npbandal on 1/25/17.
 */
public class NestedReader {
    StringBuilder buf=new StringBuilder();    // fill this as you process, character by character
    BufferedReader input; // where are we reading from?
    int c; // current character of lookahead; reset upon each getNestedString() call


    public NestedReader(BufferedReader input) {
        this.input = input;
    }

    public String getNestedString() throws IOException {

//        StringBuilder sb=new StringBuilder();
        while ((c = input.read()) != -1) {
            buf.append((char) c);
            System.out.println(buf.toString());
        }
        return buf.toString();

    }


    void consume() throws IOException {

        Stack<Character> stack = new Stack<>();
        buf.append((char) c);
        c = input.read();
        while (c != '}')
            stack.push((char) c);

    }
}
//System.out.println(sb.toString());
//        String resp = buf.toString();®

//        System.out.println(resp);
//        while (true) {
//            System.out.println("c="+(char)c);
//            switch (c) {
//                case '{':
//                    consume();
//                case '}':
//                    Character a=retrieve();
//
//            }
//        }

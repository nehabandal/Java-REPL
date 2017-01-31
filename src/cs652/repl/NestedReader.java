package cs652.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by npbandal on 1/25/17.
 */
public class NestedReader {
    StringBuilder buf = new StringBuilder();    // fill this as you process, character by character
    BufferedReader input; // where are we reading from?
    int c; // current character of lookahead; reset upon each getNestedString() call

    public NestedReader(BufferedReader input) {
        this.input = input;

    }

    public String getNestedString() throws IOException {
        buf.setLength(0);
        String inputString;
        Stack<Character> stack = new Stack<>();
        c = input.read();
        while (true) {
            //                consume();
            if ((c != '\n'))
                switch (c) {
                    case '{':
                        stack.push((char) c);
                        while (!stack.empty()) {
                            consume();
                            if (c == '{' || c == '(' || c == '[')
                                stack.push((char) c);
                            if (c == '}' || c == ')' || c == ']')
                                stack.pop();
                        }
//                    case '(':
//                        stack.push((char) c);
//                        while (!stack.empty()) {
//                            consume();
//                            if (c == '{' || c == '(' || c == '[')
//                                stack.push((char) c);
//                            if (c == '}' || c == ')' || c == ']')
//                                stack.pop();
//                        }
////                    case '[':
//                        stack.push((char) c);
//                        while (!stack.empty()) {
//                            consume();
//                            if (c == '{' || c == '(' || c == '[')
//                                stack.push((char) c);
//                            if (c == '}' || c == ')' || c == ']')
//                                stack.pop();
//                        }
                    default:
                        consume();
                }
            else {
                break;
            }
        }
        System.out.println(input.toString());
//        System.out.println(stack.toString());
        inputString = buf.toString();
        Pattern p = Pattern.compile("(print)[^a-zA-Z](.*);");
        String input = inputString;
        Matcher m = p.matcher(input);
        if (m.find()) {
            // replace first string with System.out.println
            input = m.replaceFirst("System.out.println");  // number 46
            System.out.println(m.group(2));
            inputString = input + "(" + m.group(2) + ");";
        }
        System.out.println(inputString);
        return inputString;

    }

    void consume() throws IOException {
        buf.append((char) c);
        c = input.read();
    }
}

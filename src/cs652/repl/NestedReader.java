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
        while ((c = input.read()) != '\n') {
//            switch (c) {
//                case '{':
//                    while (c != '}') {
//                        consume();
//                        stack.push((char) c);
//                    }
//                    while (!stack.empty()) {
//                        if (stack.peek() == '}' && c != '{') {
//                            buf.append(stack.pop());
//                        }
//                    }
//                    System.out.println(stack.peek());
//                case '}':
//                    if(stack.empty())
//                        return null ;
//                    else if(stack.peek() == '{') {
//                        buf.append((char) c);
//                        stack.pop();
//                    }
//                    else
//                        return null;

//            }
            buf.append((char) c);

        }

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
        c = System.in.read();
//        while (c != '}') {
//            buf.append((char) c);
//            c = System.in.read();
//        }
    }
}
/*
   while (true) {
                c=input.read();
                System.out.println("c=" + (char) c);
            switch (c) {
                case '{':
                    while (c != '}') {
                        consume();
                    }
                    stack.push((char) c);
                case '}':
                    char d = stack.pop();
                    if ((char) c == d && stack.empty()) {
                        return buf.toString();
                    }
                default:
                    consume();


            }
                System.out.println(buf.toString());
            return buf.toString();
        }

 */
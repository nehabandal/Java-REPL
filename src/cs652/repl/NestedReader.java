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
    Stack<Character> stack = new Stack<>();

    public NestedReader(BufferedReader input) {
        this.input = input;
    }

    public String getNestedString() throws IOException {
        buf.setLength(0);
        c = input.read();
        while (true) {
            switch (c) {
                case '{':
                    stack.push('}');
                    consume();
                    break;
                case '(':
                    stack.push(')');
                    consume();
                    break;
                case '[':
                    stack.push(']');
                    consume();
                    break;
                case '}':
                    if (getBracket(stack)) return buf.toString();
                    break;
                case ')':
                    if (getBracket(stack)) return buf.toString();
                    break;
                case ']':
                    if (getBracket(stack)) return buf.toString();
                    break;
                case -1:
                    return null;
                case '\n':
                    if (!stack.empty()) {
                        consume();
                        break;
                    } else
                        return buf.toString();
                default:
                    consume();
            }
        }
    }

    private boolean getBracket(Stack<Character> stack) throws IOException {
        if (c == stack.pop())
            consume();
        else
            return true;
        return false;
    }

    void consume() throws IOException {
        buf.append((char) c);//if(4<5) {
        c = input.read();//){
    }
}

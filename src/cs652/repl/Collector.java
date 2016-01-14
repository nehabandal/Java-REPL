package cs652.repl;

/** Support code used during execution of the generated code. */
public class Collector {
	public static StringBuilder output = new StringBuilder();
	public static StringBuilder errors = new StringBuilder();

	public static void reset() {
		output.setLength(0);
		errors.setLength(0);
	}
	public static void error(String msg) {
		System.err.println(msg);
		errors.append(msg);
	}
	public static void println(Object o) {
		String output = o.toString();
		Collector.output.append(output+"\n");
		System.out.print(output);
	}
	public static String getErrors() { return errors.toString(); }
	public static String getText() { return output.toString(); }
}

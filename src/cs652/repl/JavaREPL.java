package cs652.repl;

public class JavaREPL {
	public static void main(String[] args) throws IOException {
		String[] results = exec(new InputStreamReader(System.in));
		if ( results[1].length()>0 ) {
			System.err.print(results[1]);
		}
		if ( results[0].length()>0 ) {
			System.out.print(results[0]);
		}
	}

	public static String[] exec(Reader r) throws IOException {
		BufferedReader stdin = new BufferedReader(r);
		NestedReader reader = new NestedReader(stdin);
		int classNumber = 0;
		Collector.reset();
		while (true) {
			System.out.print("> ");
			String java = reader.getNestedString();
			// TODO
		}
	}
}

package cs652.repl;

public class JavaREPL {
	public static void main(String[] args) throws IOException {
		exec(new InputStreamReader(System.in));
	}

	public static String[] exec(Reader r) throws IOException {
		BufferedReader stdin = new BufferedReader(r);
		NestedReader reader = new NestedReader(stdin);
		int classNumber = 0;
		while (true) {
			System.out.print("> ");
			String java = reader.getNestedString();
			// TODO
		}
	}
}

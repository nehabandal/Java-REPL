package cs652.repl;

import com.sun.source.util.JavacTask;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaREPL {

    public static final String GEN_SRC_PATH = "/tmp/repl/java/gen";
    public static final String GEN_OUT_PATH = "/tmp/repl/java/out";

    public static void main(String[] args) throws IOException {
        exec(new InputStreamReader(System.in));
    }

    public static void exec(Reader r) throws IOException {
        BufferedReader stdin = new BufferedReader(r);
        NestedReader reader = new NestedReader(stdin);
        int classNumber = 0;

        while (true) {
            try {
                System.out.print("> ");
                String java = reader.getNestedString();
                String srccode = "package test;\n" +
                        "public class Interp_0 {\n" +
                        "    //    public static void f() { System.out.println(i); }\n" +
                        "    public static void exec() {\n" +
                        java +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "\n";

                // Save source in .java file.
                File sourceFile = new File(GEN_SRC_PATH, "Interp_0.java");
                sourceFile.getParentFile().mkdirs();
                new File(GEN_OUT_PATH).mkdirs();
                Files.write(sourceFile.toPath(), srccode.getBytes(StandardCharsets.UTF_8));

                // Compile source file.
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//                compiler.run(null, null, null, sourceFile.getPath());


//                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
                Iterable<? extends JavaFileObject> compilationUnits = fileManager
                        .getJavaFileObjectsFromStrings(Arrays.asList(sourceFile.getAbsolutePath()));
                List<String> options=new ArrayList<String>();
                options.add("-sourcepath");
                options.add(GEN_SRC_PATH);
                options.add("-d");
                options.add(GEN_OUT_PATH);
                JavacTask task = (JavacTask) compiler.getTask(null, fileManager, diagnostics, options,
                        null, compilationUnits);
                boolean success = task.call();
                fileManager.close();
//                System.out.println("Success: " + success);
                if (!success) {
                    for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                        System.err.format("Error on line %d in %s", diagnostics.getDiagnostics(), diagnostics);
                    }
                    throw new IOException("Could not compile project");
                }

                // Load and instantiate compiled class.
                URL tmpURL = new File(GEN_OUT_PATH).toURI().toURL();
                ClassLoader loader = new URLClassLoader(new URL[]{tmpURL});
                Class cl = loader.loadClass("test.Interp_0");
                Method exec = cl.getDeclaredMethod("exec");
                exec.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
class JavaSourceFromString extends SimpleJavaFileObject {
    final String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}


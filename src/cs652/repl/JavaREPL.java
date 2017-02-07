package cs652.repl;

import com.sun.source.util.JavacTask;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;


public class JavaREPL {
    public static final String GEN_SRC_PATH = "/tmp/repl/java/gen";
    public static final String GEN_OUT_PATH = "/tmp/repl/java/out";
    private static int classNumber = 0;

    public static void main(String[] args) throws IOException {
        exec(new InputStreamReader(System.in));
    }

    /**
     * Execution Method includes complete process
     *
     * @param r
     * @throws IOException
     */
    public static void exec(Reader r) throws IOException {
        ClassLoader classLoader = new URLClassLoader(new URL[]{new File(GEN_OUT_PATH).toURI().toURL()});
        BufferedReader stdin = new BufferedReader(r);
        NestedReader reader = new NestedReader(stdin);
        while (true) {
            try {
                System.out.print("> ");
                String code = reader.getNestedString();
                if (code == null)
                    break;
                if (code.equals("^D")) // this is not correct. ^D is the end of file signal which returns -1 from read; as far as I can tell this a dead code. please fix
                    exit(0);
                code = printParsing(code);
                File sourceFile = generateJavaSource(code, null);

                // Compile source file.
                boolean success = compile(sourceFile);
                if (!success) {
                    sourceFile = generateJavaSource(null, code);
                    success = compile(sourceFile);
                    if (!success)
                        sysError(sourceFile);
                }
                // Load and instantiate compiled class.
                if (success) {
                    String classname = getCurrentClassName();
                    classNumber++;
                    execute(classLoader, classname);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Handelling errors
     *
     * @param sourceFile
     * @throws IOException
     */
    private static void sysError(File sourceFile) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromStrings(Arrays.asList(sourceFile.getAbsolutePath()));
        compiler.getTask(null, fileManager, diagnostics, null,
                null, compilationUnits).call();
        fileManager.close();
        for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
            System.err.format("line %d: %s\n", diagnostic.getLineNumber(), diagnostic.getMessage(null));
        }

    }

    /**
     * getting classNames
     *
     * @param n
     * @return
     */
    private static String getClassName(int n) {
        return String.format("Interp_%02d", n);
    }

    /**
     * getting current class name from class number
     *
     * @return
     */
    private static String getCurrentClassName() {
        return getClassName(classNumber);
    }

    /**
     * Generating source Java code for compilation
     *
     * @param def
     * @param stat
     * @return
     * @throws IOException
     */
    private static File generateJavaSource(String def, String stat) throws IOException {

        String className = getCurrentClassName();
        String extendSuper = classNumber != 0 ? getClassName(classNumber - 1) : null;

        String code = getCode(className, extendSuper, def, stat);

        // Save source in .java file.
        File sourceFile = new File(GEN_SRC_PATH, className + ".java");
        sourceFile.getParentFile().mkdirs();
        new File(GEN_OUT_PATH).mkdirs();
        Files.write(sourceFile.toPath(), code.getBytes(StandardCharsets.UTF_8));
        return sourceFile;
    }

    /**
     * Generating Java source code for compilation
     *
     * @param className
     * @param extendSuper
     * @param def
     * @param stat
     * @return
     */
    public static String getCode(String className, String extendSuper, String def, String stat) {
        return String.format("\nimport java.util.*;\n" +
                        "import java.util.*;\n" +
                        "public class %s %s {\n" +
                        "    %s\n" +
                        "    public static void exec() {\n" +
                        "        %s\n" +
                        "    }\n" +
                        "}\n",
                className,
                (extendSuper != null ? "extends " + extendSuper : ""),
                def == null ? "" : "public static " + def,
                stat == null ? "" : stat
        );
    }

    /**
     * Compilation will be performed here with JavaCompiler
     *
     * @param sourceFile
     * @return
     * @throws IOException
     */
    private static boolean compile(File sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromStrings(Arrays.asList(sourceFile.getAbsolutePath()));
        List<String> options = new ArrayList<>();
        options.add("-sourcepath");
        options.add(GEN_SRC_PATH);
        options.add("-d");
        options.add(GEN_OUT_PATH);
        JavacTask task = (JavacTask) compiler.getTask(null, fileManager, diagnostics, options,
                null, compilationUnits);
        final boolean success = task.call();
        fileManager.close();
        return success;
    }

    /**
     * Execution of Java compiled code
     * Java Reflection
     *
     * @param classLoader
     * @param classRef
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */

    private static void execute(ClassLoader classLoader, String classRef)
            throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class cl = classLoader.loadClass(classRef);
        Method exec = cl.getDeclaredMethod("exec");
        exec.invoke(null);
    }

    /**
     * Converting print to System.out.println
     *
     * @param input
     * @return
     */
    static String printParsing(String input) {
        input = input
                .replaceAll("[\\t\\n\\r]", " ")
                .replaceAll("(print)\\s+([^;]+);", "System.out.println($2); ");
        return input;
    }

}


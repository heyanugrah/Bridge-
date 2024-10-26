package org.legacy;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

/**
 * BridgeGenerator generates Java classes based on methods annotated with {@link Bridge}.
 *
 * <p>This class scans the specified package for classes and generates corresponding Java
 * files for each method that is annotated with the {@link Bridge} annotation. The generated
 * files are stored in the specified output package.</p>
 *
 * <p>Additionally, it compiles the generated Java files to create their corresponding
 * native header files.</p>
 *
 * @author heyanugrah
 */
public class BridgeGenerator {

    /**
     * Retrieves all classes in the specified package.
     *
     * @param packageName the name of the package to scan
     * @return a list of classes found in the specified package
     * @throws Exception if the package cannot be found or loaded
     */
    public static List<Class<?>> getClasses(String packageName) throws Exception {
        List<Class<?>> classList = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null) {
            throw new IOException("Package not found: " + packageName);
        }

        File directory = new File(resource.toURI());

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classList.add(Class.forName(className));
            }
        }
        return classList;
    }

    /**
     * Creates Java classes based on methods annotated with {@link Bridge}.
     *
     * @param packageUri the package URI to scan for classes
     * @return a list of paths where the new Java files were created
     * @throws Exception if an error occurs during class generation
     */
    public static List<OutputFilePath> createJavaClasses(String packageUri) throws Exception {
        List<Class<?>> classList = getClasses(packageUri);
        List<OutputFilePath> outputFilePaths = new ArrayList<>();

        for (Class<?> clazz : classList) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Bridge.class)) {
                    Bridge annotation = method.getAnnotation(Bridge.class);
                    System.out.println("Annotated Method Name: " + method.getName());
                    System.out.println("Annotation Value: " + annotation.value());
                    System.out.println("Class Name: " + clazz.getName());

                    String[] classNameParts = clazz.getName().split("\\.");
                    String outputPackageName = "routines";
                    String generatedClassName = capitalizeFirstLetter(classNameParts[2]);
                    String outputFileName = "src/main/java/" + outputPackageName + "/" + generatedClassName + "_" + method.getName() + ".java";
                    String outputFolderPath = "src/main/java/" + outputPackageName + "/";

                    // Ensure the output directory exists
                    new File(outputFolderPath).mkdirs();

                    // Build the content for the new Java file
                    StringBuilder fileContent = new StringBuilder();
                    fileContent.append("package ").append(outputPackageName).append(";\n")
                            .append("public class ").append(generatedClassName).append("_").append(method.getName()).append(" {\n")
                            .append("    public native void ").append(method.getName()).append("();\n")
                            .append("}\n");

                    // Write the content to the new Java file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                        writer.write(fileContent.toString());

                        OutputFilePath outputFilePath = new OutputFilePath();
                        outputFilePath.setInputFileName(outputFileName);
                        outputFilePath.setOutputFolderPath(outputFolderPath);
                        outputFilePaths.add(outputFilePath);

                        System.out.println("File created: " + outputFileName);
                    } catch (IOException e) {
                        System.err.println("An error occurred while creating the file: " + outputFileName);
                        e.printStackTrace();
                    }
                }
            }
        }

        // Compile generated Java files and create header files
        compileJavaFiles(outputFilePaths);

        return outputFilePaths;
    }

    /**
     * Compiles the generated Java files and generates their corresponding header files.
     *
     * @param outputFilePaths the list of output file paths to compile
     */
    private static void compileJavaFiles(List<OutputFilePath> outputFilePaths) {
        // Get the Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("No Java compiler available. Make sure to run this with a JDK.");
            return;
        }

        for (OutputFilePath outputFilePath : outputFilePaths) {
            String javaFilePath = outputFilePath.getInputFileName();
            String headerOutputPath = outputFilePath.getOutputFolderPath();

            // Compile the Java file and generate the header file
            String[] compileOptions = new String[]{
                    "-h", headerOutputPath, javaFilePath
            };

            // Compile
            int result = compiler.run(null, null, null, compileOptions);
            if (result == 0) {
                System.out.println("Header file generated successfully for: " + javaFilePath);
            } else {
                System.err.println("Compilation failed for: " + javaFilePath);
            }
        }
    }

    /**
     * Capitalizes the first letter of a given string.
     *
     * @param str the string to capitalize
     * @return the input string with the first letter capitalized
     */
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}

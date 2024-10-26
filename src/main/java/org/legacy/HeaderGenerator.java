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

public class HeaderGenerator {

    public static List<Class<?>> getClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) {
            throw new IOException("Package not found: " + packageName);
        }
        File directory = new File(resource.toURI());

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }

    public static List<OutputFilePath> createJavaClass() throws Exception {
        List<Class<?>> classes = getClasses("org.legacy");
        List<OutputFilePath> outputFilePathList = new ArrayList<>();

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                // Check if the method is annotated with Bridge
                if (method.isAnnotationPresent(Bridge.class)) {
                    Bridge annotation = method.getAnnotation(Bridge.class);
                    System.out.println("Annotated Method Name: " + method.getName());
                    System.out.println("Annotation Value: " + annotation.value());
                    System.out.println("Class Name: " + clazz.getName());

                    String[] fileNames = clazz.getName().split("\\.");

                    // Define the package name and file name
                    String packageName = "routines";
                    String className = capitalizeFirstLetter(fileNames[2]); // Use the class name
                    String fileName = "src/main/java/" + packageName + "/" + className + "_" + method.getName() + ".java"; // Unique file name for each method
                    String folderPath = "src/main/java/" + packageName + "/";

                    // Ensure the directory exists
                    new File(folderPath).mkdirs();

                    // Define the content of the new class
                    StringBuilder fileContent = new StringBuilder();
                    fileContent.append("package ").append(packageName).append(";\n")
                            .append("public class ").append(className).append("_").append(method.getName()).append(" {\n") // Unique class name
                            .append("    public native void ").append(method.getName()).append("();\n")
                            .append("}\n");

                    // Write the content to the new Java file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                        writer.write(fileContent.toString());

                        OutputFilePath outputFilePath = new OutputFilePath();
                        outputFilePath.setInputFileName(fileName);
                        outputFilePath.setOutputFolderPath(folderPath);
                        outputFilePathList.add(outputFilePath);

                        System.out.println("File created: " + fileName);
                    } catch (IOException e) {
                        System.out.println("An error occurred while creating the file.");
                        e.printStackTrace();
                    }
                }
            }
        }
        return outputFilePathList;
    }

    public static void main(String[] args) throws Exception {
        List<OutputFilePath> javaClass = createJavaClass();

        for (OutputFilePath aClass : javaClass) {
            String javaFilePath = aClass.getInputFileName();
            String headerOutputPath = aClass.getOutputFolderPath();

            // Get the Java compiler
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                System.err.println("No Java compiler available. Make sure to run this with a JDK.");
                return;
            }

            // Compile the Java file and generate the header file
            String[] compileOptions = new String[] {
                    "-h", headerOutputPath, javaFilePath
            };

            // Compile
            int result = compiler.run(null, null, null, compileOptions);
            if (result == 0) {
                System.out.println("Header file generated successfully.");
            } else {
                System.err.println("Compilation failed.");
            }
        }
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}

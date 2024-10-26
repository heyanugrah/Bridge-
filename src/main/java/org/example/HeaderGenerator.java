package org.example;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HeaderGenerator {

    public static List<Class<?>> getClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
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
        List<Class<?>> classes = HeaderGenerator.getClasses("org.example");
        List<OutputFilePath> outputFilePathList = new ArrayList<>();

        for (Class<?> clazz : classes) {
            System.out.println("Class Name: " + clazz.getName());
            String fileNames[] = clazz.getName().split("\\.");

            // Define the package name and file name
            String packageName = "routines";
            String fileName = "src/main/java/" + packageName + "/" + capitalizeFirstLetter(fileNames[1]) + ".java";
            String folderPath = "src/main/java/" + packageName + "/";

            // Ensure the directory exists
            new java.io.File("src/main/java/" + packageName).mkdirs();

            // Define the content of the new class
            String fileContent = "package " + packageName + ";\n\n" +
                    "public class " + capitalizeFirstLetter(fileNames[1]) + "{\n" +
                    "    public native void displayMessage() ;" +
                    "    public static void main(String[] args) {\n" +
                    "        " + capitalizeFirstLetter(fileNames[1]) + " newClass = new " + capitalizeFirstLetter(fileNames[1]) + "();\n" +
                    "        newClass.displayMessage();\n" +
                    "    }\n" +
                    "}\n";

            // Write the content to the new Java file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(fileContent);

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
        return outputFilePathList;
    }

    public static void main(String[] args) throws Exception {

        List<OutputFilePath> javaClass = createJavaClass();

        for (OutputFilePath aClass : javaClass) {
            // Specify the paths
            String javaFilePath = aClass.getInputFileName();
            String headerOutputPath = aClass.getOutputFolderPath();

            // Get the Java compiler
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                System.err.println("No Java compiler available. Make sure to run this with a JDK.");
                return;
            }

            // Compile the Java file and generate the header file
            String[] compileOptions = new String[]{
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
        String[] words = str.split(" ");
        StringBuilder capitalizedWords = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                // Capitalize the first letter and concatenate with the rest of the word
                String capitalizedWord = Character.toUpperCase(word.charAt(0)) + word.substring(1);
                capitalizedWords.append(capitalizedWord).append(" ");
            }
        }

        // Trim the final string to remove the trailing space
        return capitalizedWords.toString().trim();
    }

}

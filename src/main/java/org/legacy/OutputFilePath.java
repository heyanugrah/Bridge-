package org.legacy;

/**
 * Represents the file paths associated with generated output files.
 * <p>
 * This class encapsulates the input file name and the output folder path
 * for easy management and retrieval of file-related information.
 * </p>
 */
public class OutputFilePath {

    private String inputFileName;  // The name of the input file
    private String outputFolderPath; // The path of the output folder

    /**
     * Gets the input file name.
     *
     * @return the name of the input file
     */
    public String getInputFileName() {
        return inputFileName;
    }

    /**
     * Sets the input file name.
     *
     * @param inputFileName the name of the input file to set
     */
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    /**
     * Gets the output folder path.
     *
     * @return the path of the output folder
     */
    public String getOutputFolderPath() {
        return outputFolderPath;
    }

    /**
     * Sets the output folder path.
     *
     * @param outputFolderPath the path of the output folder to set
     */
    public void setOutputFolderPath(String outputFolderPath) {
        this.outputFolderPath = outputFolderPath;
    }
}

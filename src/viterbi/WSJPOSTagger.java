package viterbi;

import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class WSJPOSTagger {

    private static Integer MAX_SUFFIX_LENGTH;
    private static Integer MAX_WORD_FREQUENCY;

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            String errMsg = "Expected [TEST_WORDS_FILENAME] [MAX_SUFFIX_LENGTH] [MAX_WORD_FREQUENCY] [MODEL_FILENAME], got " + args.length + " args.";
            System.err.println(errMsg);
            System.exit(99);
        }

        MAX_SUFFIX_LENGTH = Integer.parseInt(args[1]);
        MAX_WORD_FREQUENCY = Integer.parseInt(args[2]);
        System.out.println("Using a maximum suffix length of " + MAX_SUFFIX_LENGTH);
        System.out.println("Using words with a maximum frequency of " + MAX_WORD_FREQUENCY + " to create suffix tree");

        // Read saved trained model from file: https://docs.oracle.com/javase/8/docs/api/java/io/ObjectInputStream.html
        BigramModel bigramModel = null;
        try (FileInputStream fis = new FileInputStream(args[3]);
              ObjectInputStream ois = new ObjectInputStream(fis);) {
          bigramModel = (BigramModel) ois.readObject();
        } catch (Exception e) {
          System.err.println("Could not read the saved trained model\'s file");
          e.printStackTrace();
          System.exit(90);
        }

        if (bigramModel == null) {
          System.err.println("Model is invalid");
          System.exit(91);
        }

        SuffixTreeBuilder treeBuilder = new SuffixTreeBuilder(bigramModel, MAX_SUFFIX_LENGTH, MAX_WORD_FREQUENCY);
        SuffixTree upperCaseTree = treeBuilder.buildUpperCaseTree();
        SuffixTree lowerCaseTree = treeBuilder.buildLowerCaseTree();

        String testFilename = args[0];
        File testFile = new File(testFilename);
        String[] filenameParts = testFilename.split("/");
        String[] filenameAndExt = filenameParts[filenameParts.length - 1].split("\\.");
        String filename = filenameAndExt[0];
        String outputFilename = filename + ".pos";

        System.out.println("Tagging...");

        EvaluationResult result = bigramModel.evaluate(upperCaseTree, lowerCaseTree, testFile, outputFilename);
        generateOutputFile(outputFilename, result);
        System.out.println("Finished tagging. Check the base directory for the output file, " + outputFilename + ".");
    }

    public static void generateOutputFile(String filename, EvaluationResult result) throws IOException {
        FileWriter writer = new FileWriter(filename);
        List<List<String>> sentences = result.sentences;
        List<List<String>> sentenceTags = result.sentenceTags;

        Integer numSentences = sentences.size();
        for (int i = 0; i < numSentences; i++) {
            Integer sentenceLength = sentences.get(i).size();
            for (int j = 0; j < sentenceLength; j++) {
                writer.write(sentences.get(i).get(j) + "\t" + sentenceTags.get(i).get(j) + "\n");
            }
            writer.write("\n");
        }

        writer.close();
    }

}

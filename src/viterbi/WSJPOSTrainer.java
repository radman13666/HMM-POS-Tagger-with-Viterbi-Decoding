package viterbi;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WSJPOSTrainer {

    private static Integer MAX_SUFFIX_LENGTH;
    private static Integer MAX_WORD_FREQUENCY;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            String errMsg = "Expected [TRAINING_FILENAME] [MAX_SUFFIX_LENGTH] [MAX_WORD_FREQUENCY], got " + args.length + " args.";
            System.err.println(errMsg);
            System.exit(99);
        }

        System.out.println("Using a maximum suffix length of " + MAX_SUFFIX_LENGTH);
        System.out.println("Using words with a maximum frequency of " + MAX_WORD_FREQUENCY + " to create suffix tree");

        System.out.println("Training...");
        MAX_SUFFIX_LENGTH = Integer.parseInt(args[1]);
        MAX_WORD_FREQUENCY = Integer.parseInt(args[2]);

        String trainFilename = args[0];
        File trainFile = new File(trainFilename);
        BigramModel bigramModel = new BigramModel(MAX_SUFFIX_LENGTH);
        bigramModel.train(trainFile);
        System.out.print("Finished training. ");

        // Save the trained model in a file: https://docs.oracle.com/javase/8/docs/api/java/io/ObjectOutputStream.html
        String modelFilename = "trained-luganda-pos-tagger.model";
        try (FileOutputStream fos = new FileOutputStream(modelFilename);
              ObjectOutputStream oos = new ObjectOutputStream(fos);) {
          oos.writeObject(bigramModel);
          System.out.println("Check the base directory for the trained HMM model, " + modelFilename + ".");
        } catch (Exception e) {
          System.err.println("Could not save trained model");
          e.printStackTrace();
          System.exit(90);
        }
    }
}

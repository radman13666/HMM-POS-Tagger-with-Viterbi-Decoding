# HMM POS Tagger with Viterbi Decoding

This is a bigram Hidden Markov Model part of speech tagger that uses the Viterbi algorithm for decoding.
The model is trained on the Wall Street Journal POS corpus and attempts to handle unknown words by performing suffix analysis using suffix trees as described by [(Brants, 2000)](#brants).

## Try out a trained version of the model

[Trained POS tagger](https://www.aaronccwong.com/pos-tagger)

The code for that application can be found [here](https://github.com/AaronCCWong/hmm-pos-tagger-service).

## To Run

This was primarily tested on Java 11.

### Compile

To run from the command line, make sure you are in the root directory `viterbi`. This is the directory that contains `src` and `WSJ_POS_CORPUS_FOR_STUDENTS`.
Run

```bash
javac src/viterbi/*.java
```

### Run

Then to train and evaluate

```bash
java -cp src viterbi.WSJPOSTagger WSJ_POS_CORPUS_FOR_STUDENTS/WSJ_02-21.pos TEST_FILE MAX_SUFFIX_LENGTH MAX_WORD_FREQUENCY
```

where `TEST_FILE` is the file with sentences that you want to tag, `MAX_SUFFIX_LENGTH` is the maximum suffix length to use for the suffix tree and
`MAX_WORD_FREQUENCY` is the maximum word frequency as found in the training set of the words to use for the suffix tree.

**Note**: The sentences in the test file must have the same format as the sentences in `WSJ_POS_CORPUS_FOR_STUDENTS/WSJ_23.words`.

## References

<a id="brants"></a> Brants, T. (2000). TnT: A statistical part-of-speech tagger. In *ANLP 2000*, Seattle, WA, pp. 224–231.

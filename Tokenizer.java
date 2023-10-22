import java.util.Scanner;
import java.io.StringReader;

/** 
 *  Shows use of StreamTokenizer.
 *  @author Nick Howe
 *  @version 26 September 2013
 */
public class Tokenizer {
  System.out.println("Running Tokenizer");

  
  /** Pattern that matches on words */
  public static final String WORD = "[a-zA-Z]*\\b";

  /** Pattern that matches on arithmetic symbols */
  public static final String SYMBOL = "[^\\w]";

  /** Storage of input ot pass to PostFix.java */
  ArrayDeque inputQueue = new ArrayDeque; 

  /** 
   *  Converts the input string to a queue of tokens 
   *  @param input  the string to convert
   *  @return  the queue of tokens
   */
  public static void readTokens(String input) {
    Scanner scanner = new Scanner(new StringReader(input));
    // Below is a complicated regular expression that will split the
    // input string at various boundaries.
    scanner.useDelimiter
      ("(\\s+"                            // whitespace
      +"|(?<=[a-zA-Z])(?=[^a-zA-Z])"      // word->non-word
      +"|(?<=[^a-zA-Z])(?=[a-zA-Z])"      // non-word->word
      +"|(?<=[^0-9\\056])(?=[0-9\\056])"  // non-number->number
      +"|(?<=[0-9\\056])(?=[^0-9\\056])"  // number->non-number
      +"|(?<=[^\\w])(?=[^\\w]))");        // symbol->symbol
    
    while (scanner.hasNext()) {
      if (scanner.hasNextDouble()) {
        inputQueue.addFirst(scanner.nextDouble());
        //System.out.println(scanner.nextDouble());
        
      } else if (scanner.hasNext(WORD)) {
        inputQueue.addFirst(scanner.next(WORD));
        //System.out.println(scanner.next(WORD));

      } else if (scanner.hasNext(SYMBOL)) {
        inputQueue.addFirst(scanner.next(SYMBOL).charAt(0));
        //System.out.println(scanner.next(SYMBOL).charAt(0));

      } else {
        inputQueue.addFirst(scanner.next());
        //System.out.println(scanner.next());
      }
    }

    System.out.println(inputQueue);
    PostFix(inputDeque);
  }

  /** Run short test */
  public static void main(String[] args) {
    if (args.length==0) {
      System.err.println("Usage:  java Tokenizer <expr>");
    } else {
      readTokens(args[0]);
    }
  }
}

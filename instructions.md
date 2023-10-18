# Assignment 5:  Stack-Based Parsing of Arithmetic Expressions

_Note: These instructions contain formatting that does not render nicely in raw text formats (e.g., TextEdit or Notepad).  VSCode offers a markdown viewer window that will format the text for you._

This assignment will give you practice using stacks in a real parsing application: computing the result of arithmetic expressions. The simplest version will read an expression in postfix notation (e.g. `3 2 + 5 *`) and use an `ArrayDeque` as a stack to compute the result.

For full credit, you'll also implement a form of the [Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm) to enable your program to interpret the more familiar infix expressions (e.g. `(3+2) * 5`).  In addition to stacks, we'll also use queues just a little bit for passing tokens around between different parts of our program.

## Specifications

Your first program should read a postfix expression from the command line and compute the result:

    java Postfix "3 2 + 5 *"
    Answer: 25
    
Your second program should read an infix expression from the command line and compute the result:

    java Calculate "(3+2)*5"
    Answer: 25

_Note: The quotation marks are required because the parentheses would otherwise be interpreted by the command line shell rather than being passed to your program as input. In this instance, the program should print out the answer 25._

## Phase 0: Tokenization
Since we will be reading the expression from the command line arguments, it will originally be read in as a `String`. We can convert this to a series of discrete tokens using a `Scanner` that reads its input from a `StringReader` (see the `StringReader`  [documentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/StringReader.html)). 

Once we set the delimiters properly, this `Scanner` will then be able to separate numbers, operators, parentheses, etc., which we can put into a queue to operate on one at a time. You may want to play around with setting up this kind of `Scanner` in `Main.java` to make sure you understand how it works before moving on to Phase 1.

_To see an example of how to use a `Scanner` with a `StringReader`, you may want to revisit the short [Tokenizer](https://replit.com/@nhowe/Tokenizer) demonstration program that we saw in class. If you like, you may copy and use this as the starting point for your own program - just remember to reference it!_

## Phase 1: Postfix Computation
We'll start by writing the first part of the assignment in a new file called `Postfix.java`. The basic version of this assignment (sufficient for a passing grade) will only accept arithmetic expressions in postfix notation. For example, "3 2 + 5 \*" means (3+2)\*5 in our standard infix notation. You would call it like this:

    java Postfix "3 2 + 5 *"

The first step in processing the input expression is to read its tokens and store them in a queue (see Phase 0 above).  Once you have done that, the algorithm below will compute the value of the postfix expression:

* Take a token one at a time from the queue and process it as follows:
  * If the token is a **number**, `push(...)` it onto the stack
  * If the token is an **operator**, `pop()` two numbers off the stack, combine them using the **operator**, and `push(...)` the result back onto the stack
* Once all the tokens have been processed in this way, the stack should contain exactly one element: the result.

If the stack runs out of elements needed for processing an operator, or if there are too many items on the stack at the end, then the starting expression was malformed and you should throw an exception to report the error.

Even if you intend to implement the more complicated algorithm below, you should begin with a postfix calculator program named `Postfix.java`.

## Phase 2: Infix Computation
Handling infix expressions is slightly more complicated, since you have to deal with parentheses. However, it turns out that infix expressions can be converted (parens and all) into postfix ones using a single stack. You can add the converted symbols to a queue as the conversion proceeds.  The symbols in the queue may then be converted to a final value using the postfix expression algorithm you wrote previously.  If you wrote your code for phase 1 correctly, you should be able to just call the appropriate method without any changes.  The outline below is a simplified version (ignoring operator associativity) of the full shunting yard algorithm given on Wikipedia: [Shunting-yard algorithm](http://en.wikipedia.org/w/index.php?title=Shunting-yard_algorithm&oldid=572362024). 

_Note: to follow this implementation, you will need **two** instances of type `ArrayDeque<Object>`: one to use as your stack, and another to use as your queue._

* While there are tokens to be read:
  * Read a token.
  * If the token is a number, then add it to the output queue.
  * If the token is an operator (the "queue operator") then:
    * while there is an operator token at the top of the stack (the "stack operator"), and the stack operator has greater precedence than the queue operator,
      * pop the stack operator off the stack and add it to the output queue;
    * when no more high-precedence stack operators remain, finally push the queue operator onto the stack.
  * If the token is a left parenthesis, then push it onto the stack.
  * If the token is a right parenthesis:
    * Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue.
    * Pop the left parenthesis from the stack, but not onto the output queue.
    * If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
* When there are no more tokens to read:
  * While there are still tokens in the stack:
    * If the token on the top of the stack is a parenthesis, then there are mismatched parentheses.
    * If it is an operator, pop it onto the output queue.
* Exit.

If all goes well, you should be able to send the now-populated output queue directly to your postfix-processing method in `Postfix.java`.

## Hints and Comments

### Using an `ArrayDeque` as a stack or queue
For this assignment, we will use instances of the Java built-in `ArrayDeque` class (see [documentation](https://docs.oracle.com/javase/9/docs/api/java/util/ArrayDeque.html)) to serve as either a stack or a queue. Although we will be using the same underlying implementation for both, we will be using them for different purposes. To help keep things from getting confusing, we recommend naming your variables like this:

 - `ArrayDeque<Object> stack`: we'll make use of the `addFirst(...)` and `removeFirst()` methods to use this one as a **stack**
- `ArrayDeque<Object> queue`: we'll make use of the `addLast(...)` and `removeFirst()` methods to use this one as a **queue**

_**Warning**: Java also has a now-obsolete `Stack` class (see [documentation](https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/Stack.html)) but we do not endorse using it for this assignment, or really anything else. If you want to get into the details of why this is, check out [this article](https://www.baeldung.com/java-deque-vs-stack)._

### The `instanceof` operator
Both the stack and the token queue will need to hold data of heterogeneous types: `Double` for the numbers, and `Character` for the operators (not to mention `String` for function names if you want to attempt the kudos challenge described below). This means that when you take an element off the queue or the stack, you'll need to figure out what type it has: to accomplish this, use the `instanceof` operator.  For example,  `x instanceof Character` has the value `true` if `x` is of type `Character`.  Likewise, `x instanceof Double` has the value `true` if `x` is of type `Double`.

### Left- vs. Right-Associativity
The shunting-yard pseudocode given above will work for the operators `+` `-` `*` and `/` but not `^` (exponentiation). While the first four operators are left-associative, the latter is right-associative and needs slightly different treatment (described in the the full pseudocode under the wikipedia link) -- but it boils down to using less than in the precedence comparison rather than less than or equal to. 

What does left-associative mean? We interpret 1+2+3 as (1+2)+3. On the other hand, 2^1^3 is interpreted as 2^(1^3) because ^ is right-associative. For full credit your program should implement the complete shunting-yard algorithm linked above from Wikipedia, including associativity.  This involves only a small change from the simplified algorithm shown above.

## Kudos

For this assignment, you need only consider the normal arithmetic operators `+`, `-`, `*`, `/`, and `^` (addition, subtraction, multiplication, division, and exponentiation), plus parentheses for grouping. You may ignore the unary minus (e.g., `5+(-3)`). The full shunting-yard algorithm given at the wikipedia page can also handle the unary minus, constants such as `pi`, and functional expressions like `sin(0)` and `max(3,4)`. Can you augment your program to compute expressions that include elements like these? If you do, describe the augmentations in your `readme.md`.
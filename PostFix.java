import java.util.ArrayDeque;

public class PostFix {
    ArrayDeque input = new ArrayDeque;
    ArrayDeque stack = new ArrayDeque;
    
    public PostFix(ArrayDeque inputDeque) {
        input = inputDeque;
    }

    while (input.size() > 0){
        nextElement = input.pop();
        if (nextElement.type() == Double) {
            stack.push(nextElement);
        } else if (nextElement == "+" || nextElement == "-" || nextElement == "*" || nextElement == "/"){
            a = ArrayDeque.pop();
            b = ArrayDeque.pop();

            if (nextElement == "+" ){
                stack.push(a + b);
            } else if (nextElement == "-" ){
                stack.push(a - b);
            } else if (nextElement == "*" ){
                stack.push(a * b);
            } else if (nextElement == "/" ){
                stack.push(a / b);
            }

        }
    }

    System.out.println(stack);

}
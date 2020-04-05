import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class CodingTest {
    // Complete the braces function below.
    static String[] braces(String[] values) {
        //['{}[]()', '{[}]}']

        Stack<Character> stack = new Stack<>();
        // take each string char by char and put each char onto the stack

        String[] result = new String[values.length];

        for (int arrayIndex = 0; arrayIndex < values.length; arrayIndex++) {
            String temp = "YES";
            char c = ' ';
            for (int charIndex = 0; charIndex < values[arrayIndex].length(); charIndex++) {
                c = values[arrayIndex].charAt(charIndex);
                if ((c == '{') || (c == '[') || (c == '('))
                    stack.push(c);
                if ((c == '}') || (c == ']') || (c == ')')) {
                    if (stack.size() == 0)
                        temp = "NO";
                    else if (c == '}') {
                        if (stack.peek() == '{')
                            stack.pop();
                        else
                            temp = "NO";
                    } else if (c == ']') {
                        if (stack.peek() == '[')
                            stack.pop();
                        else
                            temp = "NO";
                    } else if (c == ')') {
                        if (stack.peek() == '(')
                            stack.pop();
                        else
                            temp = "NO";
                    }
                }
            }
            if (!stack.isEmpty())
                temp = "NO";
            result[arrayIndex] = temp;
            stack.clear();
            System.out.println("For string : " + values[arrayIndex] + ", result is : " + temp);
        }
        return result;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int valuesCount = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String[] values = new String[valuesCount];

        for (int i = 0; i < valuesCount; i++) {
            String valuesItem = scanner.nextLine();
            values[i] = valuesItem;
        }

        String[] res = braces(values);

        for (int i = 0; i < res.length; i++) {
            bufferedWriter.write(res[i]);

            if (i != res.length - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}

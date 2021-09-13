package classes;
import static constants.ConstantsForMatcher.*;
import enums.RomanNum;

import java.util.Locale;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Numbers {
    public static boolean isValidString(String s) {
        // The Main regex check on valid. Don't scare if it possibly... The regex usual look like:
        // ^([\\-]|)\\d{1,2}(\\s|)[+\\-*/]?(\\s|)\\d{1,2}$|^[IVXivx]{1,4}(\\s|)[+\\-*/]?(\\s|)[IVXivx]{1,4}$
        String regex = "^([\\-]|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}(\\s|)[+\\-*/]?(\\s|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}$|" +
                    "^[IVXivx]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}(\\s|)[+\\-*/]?(\\s|)[IVXivx]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}$";
        //
        Matcher stringMatcher = Pattern.compile(regex).matcher(s);
        return stringMatcher.find();
    }

    public static void toResolve(String s) {
        String firstNum = "";
        String secondNum = "";
        String symbol = "";
        if (isValidString(s)) {
            // Get data from string to:
            Matcher firstNumMatcher = Pattern.compile("^([\\-]|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}|^[IVXivx]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}").matcher(s);
            Matcher secondNumMatcher = Pattern.compile("\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}$|[IVXivx]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}$").matcher(s);
            Matcher symbolMatcher = Pattern.compile("[+\\-*/]").matcher(s);
            //

            // Fill values
            while(firstNumMatcher.find() && secondNumMatcher.find() && symbolMatcher.find()) {
                firstNum = firstNumMatcher.group().trim();
                secondNum = secondNumMatcher.group().trim();
                symbol = symbolMatcher.group().trim();
            }
            //
                System.out.println(getResult(firstNum, secondNum, symbol));

        } else {
            throw new IllegalArgumentException("Wrong format");
        }
    }

    public static <T> Object getResult(String a, String b, String operator) {
        Integer result = 0;
        BinaryOperator<Integer> binaryOperator = action(operator);

        // Check regex, if string contains integer then we can plus them;
        // else this value is Roman number and we can sum them.
        if (a.matches("^([\\-]|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}") && b.matches("([\\-]|)\\d{"+NUMBERS_D[0]+"," +
                            ""+NUMBERS_D[1]+"}$")) {
            int numOne = Integer.parseInt(a);
            int numTwo = Integer.parseInt(b);

            if (numOne > 10 || numTwo > 10 && !operator.equals("-"))
                throw new IllegalArgumentException("Numbers can't be more than 10");

            result = binaryOperator.apply(numOne, numTwo);
        } else {
            RomanNum numOne = RomanNum.valueOf(a.toUpperCase(Locale.ROOT));
            RomanNum numTwo = RomanNum.valueOf(b.toUpperCase(Locale.ROOT));

            if (numOne.getNum() > 10 || numTwo.getNum() > 10)
                throw new IllegalArgumentException("Numbers can't be more than 10");

            result = binaryOperator.apply(numOne.getNum(), numTwo.getNum());
            for (RomanNum r : RomanNum.values()) {
                if (r.getNum() == result) return r;
            }

            if (result < 1) throw new IllegalArgumentException("Roman number can't be equal or less than 0");
        }
        return result;
    }

    public static BinaryOperator<Integer> action(String operator) {
        switch (operator) {
            case "+": return Integer::sum;
            case "-": return (a, b) -> a - b;
            case "*": return (a, b) -> a * b;
            case "/": return (a, b) -> a / b;
            default: return (a, b) -> 0;
        }
    }
}
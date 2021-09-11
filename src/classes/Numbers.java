package classes;
import static constants.ConstantsForMatcher.*;
import enums.RomanNum;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Numbers {
    public static boolean isValidString(String s) {
        String xz = "^([\\-]|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}(\\s|)[+\\-*/]?(\\s|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}$|" +
                    "^[IVX]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}(\\s|)[+\\-*/]?(\\s|)[IVX]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}$";
        Matcher stringMatcher = Pattern.compile(xz).matcher(s);
        return stringMatcher.find();
    }

    public static void toResolve(String s) {
        String firstNum = "";
        String secondNum = "";
        String symbol = "";
        if (isValidString(s)) {
            Matcher firstNumMatcher = Pattern.compile("^([\\-]|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}|^[IVX]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}").matcher(s);
            Matcher secondNumMatcher = Pattern.compile("\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}$|[IVX]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}$").matcher(s);
            Matcher symbolMatcher = Pattern.compile("[+\\-*/]").matcher(s);
            while(firstNumMatcher.find() && secondNumMatcher.find() && symbolMatcher.find()) {
                firstNum = firstNumMatcher.group().trim();
                secondNum = secondNumMatcher.group().trim();
                symbol = symbolMatcher.group().trim();
            }
            System.out.println(test(firstNum, secondNum, symbol));
        } else {
            System.out.println("Error");
        }
    }

    public static <T> Object test(String a, String b, String operator) {
        Integer result = 0;
        BinaryOperator<Integer> binaryOperator = action(operator);
        if (a.matches("^([\\-]|)\\d{"+NUMBERS_D[0]+","+NUMBERS_D[1]+"}") && b.matches("([\\-]|)\\d{"+NUMBERS_D[0]+"," +
                            ""+NUMBERS_D[1]+"}$") && operator.matches("[+\\-*/]")) {
            result = binaryOperator.apply(Integer.parseInt(a), Integer.parseInt(b));
        } else if (a.matches("^[IVX]{"+NUMBERS_IVX[0]+","+NUMBERS_IVX[1]+"}")){
            RomanNum numOne = RomanNum.valueOf(a);
            RomanNum numTwo = RomanNum.valueOf(b);
            result = binaryOperator.apply(numOne.getNum(), numTwo.getNum());
            for (RomanNum r : RomanNum.values()) {
                if (r.getNum() == result) return r;
            }
        } else {
            System.out.println("Wrong format");
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

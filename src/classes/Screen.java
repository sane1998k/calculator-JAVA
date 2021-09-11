package classes;

import java.util.Scanner;

public class Screen {
   public void toStartScreen() {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String s = in.nextLine();
            Numbers.toResolve(s);
        }
    }
}

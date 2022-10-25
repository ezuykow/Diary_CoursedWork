package menu;

import task.TaskService;

import java.util.Arrays;
import java.util.Scanner;

public class Menu {

    public static void show() {
        System.out.println("#####################################");
        System.out.println("Select an action:");
        int counter = 0;
        for (Items i : Items.values()) {
            System.out.println("    " + i.getNum() + ". " + i.getText());
            counter++;
            if (counter == 2 || counter == 4 || counter == 7)
                System.out.println("-------------------------------------");
        }
        System.out.println("#####################################");
        caseAction(new Scanner(System.in).nextInt());
    }

    public static void caseAction(int label) {
        Items.doAction(Arrays.stream(Items.values()).filter(i -> i.getNum() == label).findAny().orElse(null));
    }
}

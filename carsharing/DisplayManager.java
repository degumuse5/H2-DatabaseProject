package carsharing;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Scanner;

// This class here will add a lot more sense to your understanding the program.
// This class is basically the engine of the program.
// The whole point of having multiple class for menus and inheritance is mainly for this class.
// It contains a stack and map
// Menus are pushed onto the stack and displayed

public class DisplayManager {

    // A stack with a type Menu so that all child menu are pushed
    private final Stack<Menu> menuStack;
    // menuMap to map an enum to a menu
    private final Map<Menu.menus, Menu> menuMap;
    private final Scanner scan;

    // database to use
    private final DataBaseManager database = new DataBaseManager();

    public DisplayManager() { // Set up tools
        menuStack = new Stack<>();
        menuMap = new HashMap<>();
        scan = new Scanner(System.in);
    }

    // Map all enums to the corresponding menu
    // The reason for is to avoid large switch cases and if else condition
    // When the process choice is called on the menus they return an enum
    // so this we have to is plug in the enum in the map which will spit a
    // menu out.
    public void prepareProgram() {
        menuMap.put(Menu.menus.MAINMENU, new MainMenu(database));
        menuMap.put(Menu.menus.COMPANYMENU, new CompanyMenu());
        menuMap.put(Menu.menus.COMPANYLISTMENU, new CompanyListMenu());
        menuMap.put(Menu.menus.CARMENU, new CarMenu());
        menuMap.put(Menu.menus.CARLISTMENU, new CarListMenu());
        menuMap.put(Menu.menus.CUSTOMERMENU, new CustomerMenu());
        menuMap.put(Menu.menus.CUSTOMERLISTMENU, new CustomerListMenu());
        menuStack.push(menuMap.get(Menu.menus.MAINMENU));
    }

    // The MainMenu will be pushed on to the stack and the program
    // will run until the stack is empty.
    // When process choice on the current menu is called the return value will
    // decide which menu should be pushed onto the stack.
    public void start() {
        while (!menuStack.isEmpty()) {
            Menu currentMenu = menuStack.peek(); // Get the current menu
            currentMenu.display(); // display it
            String userInput = obtainUserInput(currentMenu, scan); // block until correct input
            // The current menu will the process choice and return an enum
            Menu.menus nextMenu = currentMenu.processChoice(Integer.parseInt(userInput));
            if (nextMenu != Menu.menus.STAY){
                // pop the current
                if(nextMenu == Menu.menus.BACK){
                    menuStack.pop();
                }
                // pop 2 times
                else if (nextMenu == Menu.menus.BACK2){
                    menuStack.pop();
                    menuStack.pop();
                }
                // From menu map get nextMenu then push it onto the stack
                else {
                    menuStack.push(menuMap.get(nextMenu));
                }
            }
        }
    }

    private String obtainUserInput(Menu currentMenu, Scanner scan) {
        String userInput = scan.nextLine(); // Receive input
        boolean validInput = false;
        while (!validInput) {
            // Since menu selections are always numbers
            // Check if input is integer
            // And check only isValidInput if it's Integer
            // If it's not an integer then it will short circuit
            if (isInteger(userInput) && currentMenu.isValidInput(Integer.parseInt(userInput))) {
                validInput = true;
            } else {
                // Otherwise keep asking for input
                System.out.print("> ");
                userInput = scan.nextLine();
            }
        }
        return userInput;
    }

    private boolean isInteger(String userInput) {
        return userInput.matches("-?(0|[1-9]\\d*)");
    }

    // When the program is done close connection
    // And close the scanner
    public void stop() {
        database.closeConnection();
        scan.close();
    }
}

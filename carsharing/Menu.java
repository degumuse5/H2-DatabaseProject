package carsharing;

// This a class that all the child Menu classes extend

public abstract class Menu {

    // Database that child classes share.
    // Initialized through the main menu.
    protected static DataBaseManager database;


    // When the display manager obtains user input it
    // is run through this method to verity validity depending on the menu.
    // Each child menu has their own validity restriction
    public abstract boolean isValidInput(int choice);

    // List of enums the child menus return after processing input
    public enum menus {
        MAINMENU,

        COMPANYMENU, COMPANYLISTMENU,

        CARMENU, CARLISTMENU,

        CUSTOMERMENU, CUSTOMERLISTMENU,

        STAY, BACK, BACK2 // Back2 indicates to back out 2 times
    }

    // Each child menu display their own content.
    public abstract void display();

    // Each child menu process user choice then return an enum
    // inorder to notify the display manager on what do.
    public abstract menus processChoice (int userChoice);

}

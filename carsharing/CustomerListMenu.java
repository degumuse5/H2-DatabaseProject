package carsharing;

// Customer list menu lists customers when logging
// in as a customer is selected.
// Displays customers names ordered by their id's.
// Only allowed to use this class if MainMenu
// has customer list to collected.
// Only way to get this menu is from the MainMenu

public class CustomerListMenu extends Menu{

    // Store the customer name and customer Id
    protected static String customer_name;
    protected static int customer_id;


    @Override
    public boolean isValidInput(int choice) {
        return (choice-1) < MainMenu.customerNames.size();
    }

    @Override
    public void display() {
        System.out.println();
        showCustomerList();
    }

    @Override
    public menus processChoice(int userChoice) {
        menus nextMenu = menus.STAY;

        if (userChoice == 0) {
            nextMenu = menus.BACK;
        }
        else if ((userChoice-1) < MainMenu.customerNames.size()) {
            customer_id = userChoice;
            customer_name = MainMenu.customerNames.get(userChoice-1);
            nextMenu = menus.CUSTOMERMENU;
        }

        return nextMenu;
    }

    private void showCustomerList() {
        int listNumber = 1;
        System.out.println("Choose the customer: ");
        // List the customer names using the arraylist MainMenu class filled
        for(String nameOfCompany: MainMenu.customerNames) {
            System.out.println((listNumber++) + ". " + nameOfCompany);
        }
        System.out.println("0. Back");
        System.out.print("> ");
    }
}

package carsharing;

// The CarListMenu Lists the available Cars to choose from in a specified company.
// The Menu is used only during the rent a car portion of the program.
// Only way to get to this menu is through the CompanyListMenu


public class CarListMenu extends Menu{
    protected static String car_name; // Store the car name the user choose
    protected static int car_id; // Store the car ID the user choose


    // Display a list of cars
    public void display() {
        System.out.println(); // Empty lines for clarity

        if(CarMenu.getCarList()) { // Get the car list using the CarMenu (not this menu).
            CustomerMenu.user_input_needed = true; // The CustomerMenu needs know is user input is needed.
            showCarList(); // Finally show the list.
        }
        // Otherwise if the list is empty then no input needed.
        else {
            System.out.println("No available cars in the " + CompanyListMenu.company + " company");
            CustomerMenu.user_input_needed = false;
        }
    }

    // Take in a user input then return the next action/Menu for the DisplayManger to operate
    public menus processChoice (int choice) {
        menus nextMenu = menus.STAY; // Stay in the current Menu

        if (choice == 0) { // Back to the previous menu
            nextMenu = menus.BACK;
        }
        else if ((choice-1) < CarMenu.carList.size()) { // Check if user choice less than the car list size
            car_id = choice; // Get the ID
            car_name = CarMenu.carList.get(choice-1); // Store the name
            System.out.println("You rented '"+ car_name +"'"); // Print Status
            String sql = "UPDATE customer "
                        + "SET rented_car_id =" + choice
                        + "WHERE name =" + "('" + CustomerListMenu.customer_name + "')";
            database.insertData(sql); // Update the date base using the Car ID
            nextMenu = menus.BACK2; // Back 2 times in the DisplayManager class
        }
        // Otherwise stay in the menu until correct input is obtained
        return nextMenu;
    }

    // The methods validates raw user input
    @Override
    public boolean isValidInput(int choice) {
        return (choice-1) < CarMenu.carList.size();
    }

    // Display the car List
    private static void showCarList() {
        int listCounter = 1;
        System.out.println("Choose a car:");
        // The car names are stored in an arraylist
        // that is filled by getCarList method in the CarMenu class.
        // This method is only invoked if the getCarList method returns true.
        for(String carName: CarMenu.carList) {
            System.out.println((listCounter++) + ". " + carName);
        }
        System.out.println("0. Back");
        System.out.print("> ");
    }

}

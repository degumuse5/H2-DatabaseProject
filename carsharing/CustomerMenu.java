package carsharing;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

// Customer Menu is a menu where users are allowed to
// rent a car, return a car, display the car rented.
// There is a mini display manager in the
// rent a car method since there two big menus used.


public class CustomerMenu extends Menu {

    protected static boolean user_input_needed;

    // Store thr car name and company name
    private String car_Name = null;
    private String company_Name = null;

    @Override
    public boolean isValidInput(int choice) {
        return  choice == 0 ||  choice == 1
                || choice == 2 || choice == 3;
    }

    @Override
    public void display() {
        System.out.println();
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
        System.out.print("> ");
    }

    @Override
    public menus processChoice(int userChoice) {
        Menu.menus nextMenu = menus.STAY;
        if(userChoice == 0) {
            nextMenu = menus.BACK;
        }
        else if(userChoice == 1) {
            rentCar();
        }
        else if(userChoice == 2) {
            returnACar();
        }
        else if(userChoice == 3) {
            rentedCars();
        }
        return nextMenu;
    }

    // Rent car methods allows user to
    // rent a car using the companyList menu
    // and carList menu.
    // The user is given a list of companies (CompanyListMenu)
    // to choose the car from then prompted to the (CarListMenu)
    // then choose the car to rent.
    private void rentCar() {
        CompanyListMenu.customerMenuActive = true; // Activate the customerMenuActive check
        if(!hasRentedACar()) { // Check if the user has rented a car
            // The code below uses the properties as the displayManager class
            Stack<Menu> rentCarMenus = new Stack<>();
            Map<Menu.menus, Menu> rentCarMap = new HashMap<>();
            Scanner scan = new Scanner(System.in);

            rentCarMap.put(Menu.menus.COMPANYLISTMENU, new CompanyListMenu());
            rentCarMap.put(menus.CARLISTMENU, new CarListMenu());

            rentCarMenus.push(rentCarMap.get(Menu.menus.COMPANYLISTMENU));

            while (!rentCarMenus.isEmpty()) {
                Menu currentMenu = rentCarMenus.peek(); // Access the menu on top
                currentMenu.display();

                // Since the menus used here can either be empty
                // A check is necessary inorder to avoid asking the user
                // no reason.
                if (user_input_needed) {
                    // Get user input
                    String userInput = obtainUserInput(currentMenu,scan);
                    user_input_needed = false; // Reset check for next time

                    Menu.menus nextMenu = currentMenu.processChoice(Integer.parseInt(userInput));
                    if (nextMenu != Menu.menus.STAY) {
                        if (nextMenu == Menu.menus.BACK) {
                            rentCarMenus.pop();
                        }
                        else if (nextMenu == Menu.menus.BACK2){
                            rentCarMenus.pop();
                            rentCarMenus.pop();
                        }
                        else {
                            rentCarMenus.push(rentCarMap.get(nextMenu));
                        }

                    }
                }
                else { // Remove the current menu at the top of the stack
                    // When no user input needed meaning the list were empty
                    rentCarMenus.pop();
                }
            }
        }
        else { // A customer can't rent more than ones.
            System.out.println("You've already rented a car!");
        }
        CompanyListMenu.customerMenuActive = false;
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


    private boolean hasRentedACar() {
        boolean hasRentedACar = true;
        // From the customer check if a customer has rented a car
        // if an entry in the table containing his name and
        // rented_car_id column is not null
        String sql = "SELECT * FROM customer WHERE rented_car_id IS NOT NULL " +
                "AND name IN " + "('" + CustomerListMenu.customer_name + "')";
        try (ResultSet selectedData = database.pullData(sql)) { // Send query
            if (!selectedData.next()) {
                hasRentedACar = false;
            }
            else {
                // Since the customer can only have unique names
                // Only one entry can be pulled at ones
                int car_id = selectedData.getInt("rented_car_id");
                // if so store car id
                // Then find the car info using it's ID
                findCarInfo(car_id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return hasRentedACar;
    }

    private void findCarInfo(int carId) {
        // Pull an entry from table car where id matches the car id
        String sql = "SELECT * FROM car WHERE id = (" + carId +  ")";
        try (ResultSet selectedData = database.pullData(sql)) {
            if (selectedData.next()) {
                // Set the global variables
                car_Name = selectedData.getString("name"); // Get car name
                int company_id = selectedData.getInt("company_id"); // Get company id
                // Use company_id to find company name from the company arraylist
                company_Name = CompanyMenu.listOfCompany.get(company_id-1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Using the global variables set by findCarInfo method
    // Give the User full description of car rented
    private void rentedCars() {
        if(hasRentedACar()) {
            System.out.println("Your rented car:");
            System.out.println(car_Name);
            System.out.println("Company:");
            System.out.println(company_Name);
        }
        else {
            System.out.println("You didn't rent a car!");
        }
    }

    // When returning a car set rented_car_id of
    // the matching customer to NULL
    private void returnACar() {
        if (hasRentedACar()) {
            String insertComNameQuery = "UPDATE customer "
                    + "SET rented_car_id = NULL "
                    + "WHERE name = " + "('" + CustomerListMenu.customer_name + "')";
            database.insertData(insertComNameQuery);
            System.out.print("You've returned a rented car!");
        }
        else {
            System.out.print("You didn't rent a car!");
        }
    }

    // Simple comparison to check if a string is an integer
    private boolean isInteger(String userInput) {
        return userInput.matches("-?(0|[1-9]\\d*)");
    }
}

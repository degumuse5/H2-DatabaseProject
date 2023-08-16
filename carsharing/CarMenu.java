package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

// CarMenu Class is a menu for viewing list of cars and also create cars.
// A company must be chosen to inorder to operate the class or call its methods.
// The only way to get to this menu is from CompanyListMenu.
// Can't go to any other menu except back.
// Car list - ShowCarList()
// Create a car - createCar()
public class CarMenu extends Menu{


    protected static ArrayList<String> carList; // Arraylist that is filled and used by CarListMenu


    // This menu only contains three choices
    @Override
    public boolean isValidInput(int choice) {
        return choice == 0
                ||  choice == 1
                ||  choice == 2;
    }

    @Override
    public void display() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
        System.out.print("> ");
    }


    @Override
    public menus processChoice(int userChoice) {
        menus nextMenu = menus.STAY;
        if (userChoice == 1) {
            if (getCarList()) { // The Car list simply just lists car names
                // It doesn't let you choose.
                showCarList();
            }
            else {
                System.out.println("The car list is empty!");
            }
        } else if (userChoice == 2) {
            createCar();
        } else if(userChoice == 0) {
            nextMenu = menus.BACK2;
        }
        return nextMenu;
    }

    protected static void showCarList() {
        int listCounter = 1;
        System.out.println("Car list:");
        for(String carName: carList) {
            System.out.println((listCounter++) + ". " + carName);
        }
        System.out.println();
    }

    // User enters car name then its inserted into database
    // Inserted into the car table
    private void createCar() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the car name:");
        System.out.print("> ");
        String carName = "'" + scan.nextLine() + "'";
        String company_id = "'" + CompanyListMenu.company_id + "'";
        String insertCarNameQuery = "INSERT INTO car (name, company_id) VALUES " +
                "(" + carName + "," + company_id + ")";
        database.insertData(insertCarNameQuery);
        System.out.println("The car was added!");
        System.out.println();
        scan.close();
    }

    // Pull data out of the car table (names of cars created)
    // Fill the carList ArrayList with car names
    protected static boolean getCarList() {
        carList = new ArrayList<>();
        boolean carListExist = true;
        // Set up query using company ID
        String sql = "SELECT name FROM car WHERE company_id IN (" + CompanyListMenu.company_id +  ")";
        try (ResultSet selectedData = database.pullData(sql)) { // Send the query
            if (!selectedData.next()) { // Check if requested is empty
                carListExist = false;
            } else {
                do { // Since the cursor has been moved to check if the data is empty
                    // No need to move cursor again
                    String nameOfCar = selectedData.getString("name");
                    carList.add(nameOfCar);
                }
                // (Access the data then move cursor)
                while (selectedData.next());
            }
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return carListExist;
    }
}

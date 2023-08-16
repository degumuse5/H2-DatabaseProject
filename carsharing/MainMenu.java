package carsharing;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

// This is the menu that is displayed at beginning of the program.
// Able to access two menus from here.
// Log in as a manager - CompanyMenu
// Log in as a customer - CustomerListMenu if list exist or else create a customer
// Create a customer - Stay


public class MainMenu extends Menu {
    // ArrayList of customerNames for the customerListMenu to use.
    protected static ArrayList<String> customerNames = new ArrayList<>();

    // When the MainMenu is created all the tables
    // required for the program come alive.
    public MainMenu(DataBaseManager dataBaseManager) {
        database = dataBaseManager; // Initialize the parent class field
        database.connectToDB(); // Start a connection
        // Create the tables
        database.createOrDeleteTable("CREATE TABLE IF NOT EXISTS company ( " +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(100) UNIQUE NOT NULL " +
                ")" );
        database.createOrDeleteTable("CREATE TABLE IF NOT EXISTS car ( " +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(100) UNIQUE NOT NULL, " +
                "company_id INT NOT NULL, "+
                "FOREIGN KEY(company_id) REFERENCES company(id)" +
                ")" );
        database.createOrDeleteTable("CREATE TABLE IF NOT EXISTS customer ( " +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(100) UNIQUE NOT NULL, " +
                "rented_car_id INT, " +
                "FOREIGN KEY(rented_car_id) REFERENCES car(id)" +
                ")" );
    }

    public void display() {
        System.out.println();
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
        System.out.print("> ");
    }


    public boolean isValidInput(int choice) {
        return  choice == 0 ||  choice == 1
                || choice == 2 || choice == 3;
    }

    public menus processChoice(int choice) {
        menus nextMenu = menus.STAY;
        if(choice == 1) {
            nextMenu = menus.COMPANYMENU;
        }
        else if(choice == 0) {
            nextMenu = menus.BACK;
        }
        else if(choice == 2 && getCustomerList()) {
            nextMenu = menus.CUSTOMERLISTMENU;
        }
        else if(choice == 3) {
            createCustomer();
        }
        return nextMenu;
    }

    private void createCustomer() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the customer name:");
        System.out.print("> ");
        String companyName = "'" + scan.nextLine() + "'";
        String insertComNameQuery = "INSERT INTO customer (name) VALUES (" + companyName + ")";
        database.insertData(insertComNameQuery);
        System.out.println("The customer was created!");
    }

    // Pull data from the customer table
    private boolean getCustomerList() {
        customerNames = new ArrayList<>();
        boolean customerListExist = true;
        String sql = "SELECT name FROM customer ORDER BY id"; // Set up query
        try (ResultSet selectedData = database.pullData(sql)) { // Send query
            if(!selectedData.next()) {
                System.out.println("The customer list is empty!");
                customerListExist = false;
            }
            else {
                do {
                    String nameOfCompany = selectedData.getString("name");
                    // Fill the customerNames arraylist for the customerList menu to use.
                    customerNames.add(nameOfCompany);
                }
                while (selectedData.next());
            }
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return customerListExist;
    }
}

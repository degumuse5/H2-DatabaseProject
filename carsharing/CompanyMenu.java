package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

// This menu is used to create companies and cars
// Company List option displays companies (companyListMenu) to choose, after choosing a
// company it will switch to car menu where cars are created for that
// specified company.
// Creating a company is required to create a car.
// Only way to get to this menu is from MainMenu.

public class CompanyMenu extends Menu {

    protected static ArrayList<String> listOfCompany;

    public void display() {
        System.out.println();
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        System.out.print("> ");
    }

    public menus processChoice (int userChoice) {
        menus nextMenu = menus.STAY;
        if (userChoice == 1 && getCompanyList()) { // for CompanyLintMenu to be
            // displayed first the list of companies must be collected
            nextMenu = menus.COMPANYLISTMENU;
        }
        else if(userChoice == 2) {
            createCompany();
        }
        else if(userChoice == 0) {
            nextMenu = menus.BACK;
        }
        return nextMenu;
    }


    @Override
    public boolean isValidInput(int choice) {
        return choice == 0 || choice == 1 || choice == 2;
    }

    private void createCompany() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the company name:");
        System.out.print("> ");
        String companyName = "'" + scan.nextLine() + "'";
        String insertComNameQuery = "INSERT INTO company (name) VALUES (" + companyName + ")";
        database.insertData(insertComNameQuery);
        System.out.println("The company was created!");
    }

    // Send a query to the database then retrieve the company names
    // Else print company list is empty!
    protected static boolean getCompanyList() {
        listOfCompany = new ArrayList<>();
        boolean companyListExist = true;
        String sql = "SELECT name FROM company ORDER BY id";
        try (ResultSet selectedData = database.pullData(sql)) {
            if(!selectedData.next()) {
                System.out.println("The company list is empty!");
                companyListExist = false;
            }
            else {
                do {
                    String nameOfCompany = selectedData.getString("name");
                    listOfCompany.add(nameOfCompany);
                }
                while (selectedData.next());
            }
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return companyListExist;
    }
}

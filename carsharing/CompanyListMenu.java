package carsharing;

// The CompanyLists menu lists the available companies to choose from either
// to create a car or to rent a car.
// This menu is used as a gateway to the CarMenu and the rent a car process.
// The only way to get to this menu is through CustomerListMenu and CompanyMenu


public class CompanyListMenu extends Menu {

    // Store the company chosen by the User
    protected static String company;

    // Store the company id chosen by the User
    protected static int company_id;

    // Since there are two different menus to switch to.
    // 1: To goto CarMenu
    // 2: To goto CarListMenu
    // This depends on the customer Menu being active.
    protected static boolean customerMenuActive = false;

    public void display() {
        System.out.println();
        if(CompanyMenu.getCompanyList()) { // Get the car list using the CompanyMenu Class
            CustomerMenu.user_input_needed = true; // The CustomerMenu needs know user input is needed.
            showCompanyList();
        }
        else { // Otherwise if the list is empty then no input needed.
            CustomerMenu.user_input_needed = false;
        }
    }

    public menus processChoice (int choice) {
        menus nextMenu = menus.STAY;

        if (choice == 0) {
            nextMenu = menus.BACK;
        }
        else if ((choice-1) < CompanyMenu.listOfCompany.size()) {
            company_id = choice; // Store the ID
            company = CompanyMenu.listOfCompany.get(choice-1); // Store the name
            if (!customerMenuActive) { // If customerMenuActive is true
                System.out.println();
                System.out.println("'" + CompanyMenu.listOfCompany.get(choice-1)
                        + "'" + " company");
                nextMenu = menus.CARMENU; // Switch to CARMENU
            }
            else { // Otherwise switch to customerMenuActive
                nextMenu = menus.CARLISTMENU;
            }
        }
        return nextMenu;
    }


    // The methods validates raw user input
    @Override
    public boolean isValidInput(int choice) {
        return (choice-1) < CompanyMenu.listOfCompany.size();
    }

    private  void showCompanyList() {
        int listNumber = 1;
        System.out.println("Choose the company: ");
        // The company names are stored in an arraylist
        // that is filled by getCompanyList method in the CompanyMenu class.
        // This method is only invoked if the getCompanyList method returns true.
        for(String nameOfCompany: CompanyMenu.listOfCompany) {
            System.out.println((listNumber++) + ". " + nameOfCompany);
        }
        System.out.println("0. Back");
        System.out.print("> ");
    }
}

# H2-DatabaseProject

This Java project offers hands-on SQL learning, H2 database practice, and advanced Java features like Collections, Data Sturctures and Inheritance ensuring to grasp essential skills for effective database management and streamlined coding.


# What is it?

It's a program that simulates car renting, this is done through the menus that are provided. Create a company then create a car under that comanies name for Ex. Company: Ford, Car: F-150.
Create your desired customer then login using your created customer then rent a car.

# How it works?

It uses JDBC to connect to H2 database using the Java connection class and statements class. Connect to database using connection class then send and recive data using the statements class.

Three entitys in the program: Companies, Cars and Customer. Each have there own tables in database to store there info. 

There are a total of 7 diffrent menus all are the child class of the MENU class this OOP desgin enables Dynamic binding. 
The display-manager class loads the menus onto the stack of type MENU then displays them. When a user chooses back the d-m pops the current menu being displayed to display the one before if. If the user demands a switch to another menu then it loads needed menu to display it.

# Video

https://github.com/degumuse5/H2-DatabaseProject/assets/80492184/a9faf32a-2a7e-4cf4-80bb-3089a58eee48

# DataBase Diagram after

<img width="729" alt="Screenshot 2023-08-22 at 4 17 36 PM" src="https://github.com/degumuse5/H2-DatabaseProject/assets/80492184/8b86b90a-316b-4fad-a143-09e90fb8d3dc">




package UI;

import Domain.Car;
import Domain.Inchiriere;
import Repository.RepositoryException;
import Service.Service;

import java.util.Objects;
import java.util.Scanner;

public class UI {
    Service service;
    Scanner sc;

    public UI(Service service) {
        this.service = service;
        sc = new Scanner(System.in);
    }

    public void runMenu() {
        printMenu();
        String rawOption = sc.nextLine();
        while(true) {
            try {
                if(Objects.equals(rawOption, "x")) break;
                int option = Integer.parseInt(rawOption);
                if (option == 1) addCar();
                else if (option == 2) readCar();
                else if (option == 3) updateCar();
                else if (option == 4) deleteCar();
                else if (option == 5) printAllCars();
                else if (option == 6) addRental();
                else if (option == 7) readRental();
                else if (option == 8) updateRental();
                else if (option == 9) deleteRental();
                else if (option == 10) printAllRentals();
                else if (option == 11) statistics();
                else System.out.println("Command unknown!");
            }
            catch (RepositoryException exception) {
                System.out.println(exception.getMessage());
            }
            printMenu();
            rawOption = sc.nextLine();
        }
        System.out.println("Closing app...");
    }

    private void statistics() {
        System.out.println("\t\t1 - Most rented cars");
        System.out.println("\t\t2 - Number of rentals in each month");
        System.out.println("\t\t3 - Cars with longest rental period");
        String optiune = sc.nextLine();
        if(Objects.equals(optiune, "1"))
            service.getMostRentedCars().forEach(((car, integer) -> System.out.println(car + " rented: " + integer)));
        else if(Objects.equals(optiune, "2"))
            service.getEachMonthNumberOfRentals().forEach(((month, integer) -> System.out.println(month + " no rentals: " + integer)));
        else if(Objects.equals(optiune, "3"))
            service.getCarWithLongestRentalPeriod().forEach(((car, integer) -> System.out.println(car + " days rented: " + integer)));
    }

    private void printAllRentals() {
        for(Inchiriere inchiriere: service.getRepoInchiriere()) {
            System.out.println(inchiriere);
        }
    }

    private void printAllCars() {
        for(Car car: service.getRepoCar()) {
            System.out.println(car);
        }
    }

    private void deleteRental() throws RepositoryException {
        System.out.println("Please enter ID of rental: ");
        String IDRental = sc.nextLine();
        service.deleteInchiriere(IDRental);
    }

    private void updateRental() throws RepositoryException {
        System.out.println("Please enter ID of rental: ");
        String IDRental = sc.nextLine();
        System.out.println("Please enter ID of car: ");
        String IDCar = sc.nextLine();
        System.out.println("Please enter start date: ");
        String startDate = sc.nextLine();
        System.out.println("Please enter end date: ");
        String endDate = sc.nextLine();
        service.updateInchiriere(IDRental, IDCar, startDate, endDate);

    }

    private void readRental() throws RepositoryException {
        System.out.println("Please enter ID: ");
        String ID = sc.nextLine();
        Inchiriere car = service.readInchiriere(ID);
        System.out.println(car);
    }

    private void addRental() throws RepositoryException {
        System.out.println("Please enter ID of rental: ");
        String IDRental = sc.nextLine();
        System.out.println("Please enter ID of car: ");
        String IDCar = sc.nextLine();
        System.out.println("Please enter start date: ");
        String startDate = sc.nextLine();
        System.out.println("Please enter end date: ");
        String endDate = sc.nextLine();
        service.addInchiriere(IDRental, IDCar, startDate, endDate);
    }

    private void deleteCar() throws RepositoryException {
        System.out.println("Please enter ID: ");
        String ID = sc.nextLine();
        service.deleteCar(ID);
    }

    private void updateCar() throws RepositoryException {
        System.out.println("Please enter ID: ");
        String ID = sc.nextLine();
        System.out.println("Please enter manufacturer: ");
        String marca = sc.nextLine();
        System.out.println("Please enter model: ");
        String model = sc.nextLine();
        service.updateCar(ID, marca, model);
    }

    private void readCar() throws RepositoryException {
        System.out.println("Please enter ID: ");
        String ID = sc.nextLine();
        Car car = service.readCar(ID);
        System.out.println(car);
    }

    private void addCar() throws RepositoryException {
        System.out.println("Please enter ID: ");
        String ID = sc.nextLine();
        System.out.println("Please enter manufacturer: ");
        String marca = sc.nextLine();
        System.out.println("Please enter model: ");
        String model = sc.nextLine();
        service.addCar(ID, marca, model);
    }

    private void printMenu() {
        System.out.println("   1 - Add car");
        System.out.println("   2 - Read car");
        System.out.println("   3 - Update car");
        System.out.println("   4 - Delete car");
        System.out.println("   5 - Print all cars");
        System.out.println("   6 - Add rental");
        System.out.println("   7 - Read rental");
        System.out.println("   8 - Update rental");
        System.out.println("   9 - Delete rental");
        System.out.println("   10 - Print all rentals");
        System.out.println("   11 - Statistics");
        System.out.println("   x - Quit session");
    }
}

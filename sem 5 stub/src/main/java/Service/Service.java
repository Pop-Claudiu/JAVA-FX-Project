package Service;

import Domain.Car;
import Domain.Inchiriere;
import Repository.IRepository;
import Repository.RepositoryException;

import java.text.DateFormatSymbols;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Service {
    IRepository<Car> repoCar;
    IRepository<Inchiriere> repoInchiriere;

    AbstractValidator<Car> carValidator;

    InchiriereValidator inchiriereValidator;

    public Service(IRepository<Car> repoCar, IRepository<Inchiriere> repoInchiriere) {
        this.repoCar = repoCar;
        this.repoInchiriere = repoInchiriere;
        this.carValidator = new CarValidator(repoCar);
        this.inchiriereValidator = new InchiriereValidator(repoInchiriere);
    }

    public void addData(){
        Car car1 = new Car(1, "BMW", "E60");
        Car car2 = new Car(2, "BMW", "E46");
        Car car3 = new Car(3, "Porche", "Macan");
        Car car4 = new Car(4, "Merceded", "AMG GT 63");
        Car car5 = new Car(5, "Dacia", "Logan");
        repoCar.addEntity(car1);
        repoCar.addEntity(car2);
        repoCar.addEntity(car3);
        repoCar.addEntity(car4);
        repoCar.addEntity(car5);
        Inchiriere inchiriere1 = new Inchiriere(1, car1,new Date(122, 1, 20), new Date(122, 11, 23));
        Inchiriere inchiriere2 = new Inchiriere(2, car2,new Date(122, 1, 20), new Date(122, 11, 23));
        Inchiriere inchiriere3 = new Inchiriere(3, car3,new Date(122, 1, 20), new Date(122, 11, 23));
        Inchiriere inchiriere4 = new Inchiriere(4, car4,new Date(122, 1, 20), new Date(122, 11, 23));
        Inchiriere inchiriere5 = new Inchiriere(5, car5,new Date(122, 1, 20), new Date(122, 11, 23));
        repoInchiriere.addEntity(inchiriere1);
        repoInchiriere.addEntity(inchiriere2);
        repoInchiriere.addEntity(inchiriere3);
        repoInchiriere.addEntity(inchiriere4);
        repoInchiriere.addEntity(inchiriere5);
    }

    public List<Car> getRepoCar() {
        return repoCar.getAll();
    }

    public List<Inchiriere> getRepoInchiriere() {
        return repoInchiriere.getAll();
    }

    public Iterator<Car> carIterator(){
        return repoCar.iterator();
    }

    public Iterator<Inchiriere> inchiriereIterator(){
        return repoInchiriere.iterator();
    }

    public void addCar(String rawID, String marca, String model) throws RepositoryException {
        int ID = carValidator.validateAddID(rawID);
        repoCar.addEntity(new Car(ID, marca, model));
    }

    public void addInchiriere(String rawID, String rawIDCar, String rawStartDate, String rawEndDate) throws RepositoryException {
        int IDCar = carValidator.existsID(rawIDCar);
        Car car = repoCar.findEntity(IDCar);
        int IDInchiriere = inchiriereValidator.validateAddID(rawID);
        Date startDate = inchiriereValidator.validateDate(rawStartDate);
        Date endDate = inchiriereValidator.validateDate(rawEndDate);
        inchiriereValidator.validateDates(startDate, endDate, car);
        repoInchiriere.addEntity(new Inchiriere(IDInchiriere, car, startDate, endDate));
    }

    public Car readCar(String rawID) throws RepositoryException {
        return repoCar.findEntity(carValidator.validateAddID(rawID));
    }

    public Inchiriere readInchiriere(String rawID) throws RepositoryException {
        int ID = inchiriereValidator.existsID(rawID);
        return repoInchiriere.findEntity(ID);
    }

    public void updateCar(String rawID, String marca, String model) throws RepositoryException {
        int ID = carValidator.existsID(rawID);
        repoCar.update(new Car(ID, marca, model));
    }

    public void updateInchiriere(String rawID, String rawIDCar, String rawStartDate, String rawEndDate) throws RepositoryException {
        int ID = inchiriereValidator.existsID(rawID);
        Inchiriere old = repoInchiriere.findEntity(ID);
        repoInchiriere.deleteEntity(ID); //Stergem din container ca sa putem face validari
        try{
            addInchiriere(rawID, rawIDCar, rawStartDate, rawEndDate);
        }
        catch (RepositoryException exception) {
            repoInchiriere.addEntity(old);
            throw exception;
        }
    }

    public void deleteCar(String rawID) throws RepositoryException {
        int ID = carValidator.existsID(rawID);
        repoCar.deleteEntity(ID);
        for(Inchiriere inchiriere: repoInchiriere.getAll()) {
            if(inchiriere.getCar().getID() == ID)
                repoInchiriere.deleteEntity(inchiriere.getID());
        }
    }

    public void deleteInchiriere(String rawID) throws RepositoryException {
        repoInchiriere.deleteEntity(inchiriereValidator.existsID(rawID));
    }

    public Map<Car, Integer> getMostRentedCars() {
        return repoCar.getAll().stream().
                collect(Collectors.toMap(Function.identity(), this::getNumberRented)).
                entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public Map<String, Integer> getEachMonthNumberOfRentals() {
        return Stream.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "November", "December").
                collect(Collectors.toMap(Function.identity(), this::getRentalsPerMonth)).
                entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public Map<Car, Integer> getCarWithLongestRentalPeriod() {
        return repoCar.getAll().stream().
                collect(Collectors.toMap(Function.identity(), this::getTotalRentalDays)).
                entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private int getTotalRentalDays(Car car) {
        int counter = 0;
        for(Inchiriere rent: repoInchiriere.getAll()) {
            if(rent.getCar().getID() == car.getID()) {
                long diff = rent.getEndDate().getTime() - rent.getStartDate().getTime();
                counter += (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }
        }
        return counter;
    }

    private Integer getRentalsPerMonth(String month) {
        int counter = 0;
        for(Inchiriere inch: repoInchiriere.getAll()) {
            String startMonth = new DateFormatSymbols().getMonths()[inch.getStartDate().getMonth()];
            if(Objects.equals(startMonth, month))
                counter++;
        }
        return counter;
    }

    private int getNumberRented(Car car) {
        int counter = 0;
        for(Inchiriere rent: repoInchiriere.getAll()) {
            if(rent.getCar().getID() == car.getID())
                counter++;
        }
        return counter;
    }
}

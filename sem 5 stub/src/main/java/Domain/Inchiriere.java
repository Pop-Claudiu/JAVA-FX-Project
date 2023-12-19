package Domain;

import java.util.Date;

public class Inchiriere extends Entity {
    private Car car;
    private Date startDate;
    private Date endDate;

    public Inchiriere(int ID, Car car, Date startDate, Date endDate) {
        super(ID);
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Inchiriere: " + "ID = " + ID +
                ", carID = " + car.getID() +
                ", startDate = " + dateFormat(startDate) +
                ", endDate = " + dateFormat(endDate);
    }

    private String dateFormat(Date date){
        return date.getDate() + "-" + (date.getMonth()+1) + "-" + (1900+date.getYear());
    }
    public Car getCar() {
        return car;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}

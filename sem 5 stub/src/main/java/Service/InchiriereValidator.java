package Service;

import Domain.Car;
import Domain.Inchiriere;
import Repository.IRepository;
import Repository.RepositoryException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class InchiriereValidator extends AbstractValidator<Inchiriere> {
    public InchiriereValidator(IRepository<Inchiriere> repoInchiriere) {
        super(repoInchiriere);
    }

    public Date validateDate(String rawDate) throws RepositoryException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try{
            return formatter.parse(rawDate);
        }
        catch (ParseException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    public void validateDates(Date startDate, Date endDate, Car car) throws RepositoryException {
        if(Objects.compare(startDate, endDate, Date::compareTo) > 0)
            throw new RepositoryException("Start date cannot be after end date!");
        for(Inchiriere inchiriere: super.getRepository()){
            if(inchiriere.getCar().getID() == car.getID()) {
                if ((Objects.compare(inchiriere.getEndDate(), startDate, Date::compareTo) >= 0 &&
                        Objects.compare(inchiriere.getEndDate(), endDate, Date::compareTo) <= 0 ) ||
                        (Objects.compare(inchiriere.getStartDate(), startDate, Date::compareTo) >= 0 &&
                                Objects.compare(inchiriere.getStartDate(), endDate, Date::compareTo) <= 0 ))
                    throw new RepositoryException("Car is not free!");
            }
        }
    }
}

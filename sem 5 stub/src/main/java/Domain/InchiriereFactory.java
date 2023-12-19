package Domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InchiriereFactory implements IEntityFactory<Inchiriere>{
    @Override
    public Inchiriere createEntity(String line) {
        try{
            int ID = Integer.parseInt(line.split(",")[0]);
            int IDCar  = Integer.parseInt(line.split(",")[1]);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date startDate = formatter.parse(line.split(",")[2]);
            Date endDate = formatter.parse(line.split(",")[3]);

            return new Inchiriere(ID, new Car(IDCar, "", ""), startDate, endDate);
        }
        catch (Exception ignored){}
        return null;}

    @Override
    public String parseEntity(Inchiriere entity) {
        return entity.getID() + "," + entity.getCar().getID() + "," + entity.getStartDate().getDate() + "-" +
                (entity.getStartDate().getMonth()+1) + "-" + (1900+ entity.getStartDate().getYear()) + "," +
                entity.getEndDate().getDate() + "-" + (entity.getEndDate().getMonth()+1) + "-" + (1900+ entity.getEndDate().getYear()) + "\n";
    }
}

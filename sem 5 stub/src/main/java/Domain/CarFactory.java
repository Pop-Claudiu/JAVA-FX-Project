package Domain;

public class CarFactory implements IEntityFactory<Car>{
    @Override
    public Car createEntity(String line) {
        int ID = Integer.parseInt(line.split(",")[0]);
        String marca = line.split(",")[1];
        String model = line.split(",")[2];

        return new Car(ID, marca, model);
    }

    @Override
    public String parseEntity(Car entity) {
        return entity.getID() + "," + entity.getMarca() + "," + entity.getModel() + "\n";
    }
}

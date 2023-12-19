package Domain;

import java.util.Objects;

public class Car extends Entity {
    private String marca;
    private String model;

    public Car(int ID, String marca, String model) {
        super(ID);
        this.marca = marca;
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(marca, car.marca) && Objects.equals(model, car.model) && this.getID()==((Car) o).getID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(marca, model);}

    public String getMarca() {
        return marca;
    }

    @Override
    public String toString() {
        return "Car: " +
                "marca = " + marca +
                ", model = " + model +
                ", ID = " + ID;
    }

    public String getModel() {
        return model;
    }
}

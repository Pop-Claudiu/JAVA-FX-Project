package Domain;

public interface IEntityFactory <T extends Entity> {
    public T createEntity(String line);
    public String parseEntity(T entity);
}

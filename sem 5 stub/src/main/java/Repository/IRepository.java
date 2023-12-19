package Repository;

import Domain.Entity;

import java.util.Iterator;
import java.util.List;

public interface IRepository<T extends Entity> {
    public void addEntity(T entity);
    public T findEntity(int ID);
    public boolean deleteEntity(int ID);
    public void update(T entity);
    public List<T> getAll();

    Iterator<T> iterator();
}

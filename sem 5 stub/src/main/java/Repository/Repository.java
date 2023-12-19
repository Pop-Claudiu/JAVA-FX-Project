package Repository;

import Domain.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repository<T extends Entity> implements Iterable<T>, IRepository<T>{
    private List<T> container = new ArrayList<>();

    public void addEntity(T entity) {
        container.add(entity);
    }

    public boolean deleteEntity(int ID){
        if(findEntity(ID) != null) {
            container.remove(findEntity(ID));
            return true;
        }
        return false;
    }

    public T findEntity(int ID) {
        for(T entity: container) {
            if (entity.getID() == ID)
                return entity;
        }
        return null;
    }

    public void update(T entity) {
        if(deleteEntity(entity.getID()))
            addEntity(entity);
    }
    @Override
    public Iterator<T> iterator() {
        return container.iterator();
    }

    public List<T> getAll(){
        return new ArrayList<>(container);
    }
}

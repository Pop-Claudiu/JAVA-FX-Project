package Service;

import Domain.Entity;
import Repository.IRepository;
import Repository.RepositoryException;

import java.util.List;

abstract public class AbstractValidator <T extends Entity> {
    IRepository<T> repository;

    public List<T> getRepository() {
        return repository.getAll();
    }

    public AbstractValidator(IRepository<T> repository) {
        this.repository = repository;
    }

    public int validateAddID(String rawID) throws RepositoryException {
        try{
            int ID = Integer.parseInt(rawID);
            if(repository.findEntity(ID) != null) //Mesaj personalizat cu tipul lui T??
                throw new RepositoryException("Entity with specified id already exists!");
            return ID;
        }
        catch (NumberFormatException e){
            throw new RepositoryException("Please enter valid inputs!");
        }
    }

    public int existsID(String rawID) throws RepositoryException{
        try{
            int ID = Integer.parseInt(rawID);
            if(repository.findEntity(ID) == null)
                throw new RepositoryException("Entity with specified id doesn't exist!");
            return ID;
        }
        catch (NumberFormatException e){
            throw new RepositoryException("Please enter valid inputs!");
        }
    }
}

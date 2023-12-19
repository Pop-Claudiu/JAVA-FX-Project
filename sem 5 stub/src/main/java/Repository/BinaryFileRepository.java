package Repository;

import Domain.Entity;

import java.io.*;

public class BinaryFileRepository<T extends Entity> extends Repository<T>{
    String fileName;
    public BinaryFileRepository(String fileName) throws RepositoryException {
        super();
        this.fileName = fileName;
        loadFile();
    }

    private void saveFile() throws RepositoryException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.flush();
            for (Entity entity: super.getAll()) {
                oos.writeObject(entity);
            }
        }
        catch (IOException exception){throw new RepositoryException(exception.getMessage());}
    }

    private void loadFile() throws RepositoryException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            while(true) {
                try{
                    T entity = (T) ois.readObject();
                    super.addEntity(entity);
                }
                catch (EOFException exception){ break;}
            }
        }
        catch (ClassNotFoundException | FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ignoerd) {}
    }

    public void addEntity(T entity) {
        try{
        super.addEntity(entity);
            saveFile();
        }
        catch (RepositoryException exception){throw new RuntimeException(exception.getMessage());}
    }

    public boolean deleteEntity(int ID){
        try{
            boolean bool = super.deleteEntity(ID);
            saveFile();
            return bool;
        } catch (RepositoryException exception){throw new RuntimeException(exception.getMessage());}}

    public void update(T entity){
        try{
            super.update(entity);
            saveFile();
        }
        catch (RepositoryException exception){throw new RuntimeException(exception.getMessage());}
    }
}

package Repository;

import Domain.Entity;
import Domain.IEntityFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TextFileRepository<T extends Entity> extends Repository<T>{
    String fileName;

    IEntityFactory<T> entityFactory;
    public TextFileRepository(String fileName, IEntityFactory<T> entityFactory) throws RepositoryException {
        super();
        this.fileName = fileName;
        this.entityFactory = entityFactory;
        loadFile();
    }

    private void loadFile() throws RepositoryException {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            while(line != null && !line.isEmpty()){
                super.addEntity(entityFactory.createEntity(line));
                line = br.readLine();
            }
        }  catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void addEntity(T entity){
        super.addEntity(entity);
        saveFile();
    }

    public boolean deleteEntity(int ID){
        boolean bool = super.deleteEntity(ID);
        saveFile();
        return bool;
    }

    public void updateEntity(T entity){
        super.update(entity);
        saveFile();}

    private void saveFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.flush();
            for(T entity: this.getAll()) {
                writer.write(entityFactory.parseEntity(entity));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());}
    }
}

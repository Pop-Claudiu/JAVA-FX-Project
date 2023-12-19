package Repository;

import Domain.Entity;

public interface IDBRepository<T extends Entity> extends IRepository<T> {
    void openConnection();
    void closeConnection();
    void createTable();
}

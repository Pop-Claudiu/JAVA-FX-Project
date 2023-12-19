package Repository;

import Domain.Car;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DBCarRepository extends Repository<Car> implements IDBRepository<Car> {

    private String JDBC_URL;
    private Connection connection;
    public DBCarRepository(String JDBC_URL) {
        this.JDBC_URL = JDBC_URL;
        openConnection();
        createTable();
        loadData();
        randomGen();
    }

    public void randomGen() {
        if(!super.getAll().isEmpty()) return;
        Map<String, List<String>> multimap = new HashMap<>();
        multimap.put("BMW", Arrays.asList("E46", "E60", "E90", "3 Series", "7 Series", "4 Series"));
        multimap.put("Mercedes", Arrays.asList("AMG GT 60", "GLE 53", "C 63", "E 220", "GLC 180d", "G wagon"));
        multimap.put("Dacia", Arrays.asList("Solenza", "Logan", "Docker", "MCV", "Sandero", "Spring"));
        multimap.put("Audi", Arrays.asList("A5", "A7", "A3", "TT", "RS6", "Q8"));
        multimap.put("Opel", Arrays.asList("Vectra", "Insignia", "Corsa", "Mokka", "Astra", "Meriva"));
        multimap.put("Volvo", Arrays.asList("XC60", "S90", "V60", "S60", "XC90", "S30"));
        List<String> carManufacturer = Arrays.asList("BMW", "Mercedes", "Dacia", "Audi", "Opel", "Volvo");
        for(int i = 1; i <= 100; i++) {
            int random1 = ThreadLocalRandom.current().nextInt(0, 1000) % 6;

            int random2 = (ThreadLocalRandom.current().nextInt(0, 1000) % 49) % 6;
            addEntity(new Car(i, carManufacturer.get(random1), multimap.get(carManufacturer.get(random1)).get(random2)));
        }
    }

    private void loadData() {
        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * from Cars");
                 ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Car car = new Car(rs.getInt("id"), rs.getString("marca"),
                            rs.getString("model"));
                    super.addEntity(car);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void openConnection() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);
        try {
            if(connection == null || connection.isClosed())
                connection = ds.getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable() {
        try(final Statement stmt = connection.createStatement()) {

            stmt.execute("create table if not exists Cars(id int, marca varchar(50), model varchar(50))");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEntity(Car entity) {
        super.addEntity(entity);
        try(PreparedStatement stmt = connection.prepareStatement("insert into Cars values(?, ?, ?)")){
            stmt.setInt(1, entity.getID());
            stmt.setString(2, entity.getMarca());
            stmt.setString(3, entity.getModel());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteEntity(int ID) {
        boolean local = super.deleteEntity(ID);
        try(PreparedStatement stmt = connection.prepareStatement("delete from Cars where id=?")){
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return local;
    }

    @Override
    public void update(Car entity) {
        super.update(entity);
        try(PreparedStatement stmt = connection.prepareStatement("update Cars set marca = ?, model = ? where id = ?")){
            stmt.setString(1, entity.getMarca());
            stmt.setString(2, entity.getModel());
            stmt.setInt(3, entity.getID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

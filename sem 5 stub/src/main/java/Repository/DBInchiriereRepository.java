package Repository;

import Domain.Car;
import Domain.Inchiriere;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class DBInchiriereRepository extends Repository<Inchiriere> implements IDBRepository<Inchiriere> {

    private String JDBC_URL;
    private Connection connection;
    public DBInchiriereRepository(String JDBC_URL) {
        this.JDBC_URL = JDBC_URL;
        openConnection();
        createTable();
        loadData();
        randomGen();
    }

    public void randomGen() {
        if(!super.getAll().isEmpty()) return;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        for(int i = 1; i <= 100; i++) {
            String source1 = Integer.toString(i%28 + 1) + "-" + Integer.toString(i %12 + 1) + "-" + Integer.toString(2010 + i/12);
            String source2 = Integer.toString(i%28 + 2) + "-" + Integer.toString(i % 12 + 1) + "-" + Integer.toString(2010 + i/12);
            java.util.Date startDate, endDate;
            try {
                startDate = formatter.parse(source1);
                endDate = formatter.parse(source2);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            addEntity(new Inchiriere(i, findCar(ThreadLocalRandom.current().nextInt(1, 100)), startDate, endDate));
        }
    }

    private void loadData() {
        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * from Inchirieri");
                 ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    int idCar = rs.getInt("idCar");
                    Car car = findCar(idCar);
                    Inchiriere inch = new Inchiriere(rs.getInt("id"), car,
                            rs.getDate("startDate"), rs.getDate("endDate"));
                    super.addEntity(inch);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Car findCar(int idCar) {
        try(PreparedStatement statement = connection.prepareStatement("select * from Cars where id=?")){
            statement.setInt(1, idCar);
            ResultSet resultSet = statement.executeQuery();
            return new Car(resultSet.getInt("id"), resultSet.getString("marca"), resultSet.getString("model"));
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
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
            stmt.execute("create table if not exists Inchirieri(id int, idCar int, startDate date, endDate date)");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEntity(Inchiriere entity) {
        super.addEntity(entity);
        try(PreparedStatement stmt = connection.prepareStatement("insert into Inchirieri values(?, ?, ?, ?)")){
            stmt.setInt(1, entity.getID());
            stmt.setInt(2, entity.getCar().getID());
            stmt.setDate(3, new java.sql.Date(entity.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(entity.getEndDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteEntity(int ID) {
        boolean local = super.deleteEntity(ID);
        try(PreparedStatement stmt = connection.prepareStatement("delete from Inchirieri where id=?")){
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return local;
    }

    @Override
    public void update(Inchiriere entity) {
        super.update(entity);
        try(PreparedStatement stmt = connection.prepareStatement("update Inchirieri set idCar = ?, startDate = ?, endDate = ? where id = ?")){
            stmt.setInt(1, entity.getCar().getID());
            stmt.setDate(2, new java.sql.Date(entity.getStartDate().getTime()));
            stmt.setDate(3, new java.sql.Date(entity.getEndDate().getTime()));
            stmt.setInt(4, entity.getID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

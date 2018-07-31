package encyclopedia;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final String URL = "jdbc:derby:animalsDB;create=true";

    Connection conn = null;
    DatabaseMetaData dbmd = null;
    Statement statement = null;

    public DB() {
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (conn != null) {
            try {
                statement = conn.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ResultSet rs = dbmd.getTables(null, "APP", "ANIMALS", null);
            if (!rs.next()) {
                String sql = "create table animals (id INT not null primary key GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), name varchar(30), lifespan varchar(30), color varchar(30), sound varchar(20))";
                statement.execute(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Animal> getAllAnimals() {
        String sql = "select * from animals";
        ArrayList<Animal> list = null;
        try {
            ResultSet rs = statement.executeQuery(sql);
            list = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String id2 = "" + id;
                Animal tmp = new Animal(rs.getString("name"), rs.getString("lifespan"), rs.getString("color"), rs.getString("sound"), id2);
                list.add(tmp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void printAllAnimals() {
        String sql = "select * from animals";
        ResultSet rs;
        try {
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString("name") + " | " + rs.getString("lifespan") + " | " + rs.getString("color") + " | " + rs.getString("sound"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addAnimal(Animal animal) {
        try {
            String sql = "insert into animals (name, lifespan, color, sound) values(?,?,?,?)";
            PreparedStatement ppst = conn.prepareStatement(sql);
            ppst.setString(1, animal.getName());
            ppst.setString(2, animal.getLifespan());
            ppst.setString(3, animal.getColor());
            ppst.setString(4, animal.getSound());
            ppst.execute();
    
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateAnimal(Animal animal) {
        String sql = "update animals set name = ?, lifespan = ?, color = ?, sound = ? where id = ?";
        try {
            PreparedStatement ppst = conn.prepareStatement(sql);
            ppst.setString(1, animal.getName());
            ppst.setString(2, animal.getLifespan());
            ppst.setString(3, animal.getColor());
            ppst.setString(4, animal.getSound());
            ppst.setInt(5, Integer.parseInt(animal.getID()));
            ppst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAnimal(int ID) {
        try {
            String sql = "delete from animals where id = ?";
            PreparedStatement ppst = conn.prepareStatement(sql);
            ppst.setInt(1, ID);
            ppst.execute();
            System.out.println("DELETE ANIMAL");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void insertColumn(String tableName, String columnName, String columnType) {
        try {
            String sql = "alter table ? add ? ?";
            PreparedStatement ppst = conn.prepareStatement(sql);
            ppst.setString(1, tableName);
            ppst.setString(2, columnName);
            ppst.setString(3, columnType);
            ppst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createProcedure(String createProcedure, String as) {
        try {
            String sql = "create procedure ? as ? GO;";
            
            PreparedStatement ppst = conn.prepareStatement(sql);
            ppst.setString(1, createProcedure);
            ppst.setString(2, as);
            ppst.execute();
           
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
//    public void execProcedure(String procedureName) {
//        try {
//            String sql = "exec ?";
//            CallableStatement cst = conn.prepareCall(sql);
//            cst.setString(1, procedureName);
//            cst.execute();
//        } catch (SQLException ex) {
//            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}

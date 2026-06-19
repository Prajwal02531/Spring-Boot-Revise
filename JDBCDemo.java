import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.sql.*;

public class JDBCDemo {
    private static String URL = "jdbc:mysql://localhost:3306/demo_db";
    private static String USER = "root";
    private static String PASSWORD = "root123";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);) {
            System.out.println("Connected to the Database");
            insertStudent(conn,"Alice","alice@gmail.com");

            updateStudent(conn,1,"BOB","aice@gmail.com");
            selectStudents(conn);
            deleteStudent(conn,1);
            selectStudents(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void insertStudent(Connection conn, String name, String email){
        String sql = "INSERT INTO Student(name, email) VALUES ('"+ name +"','"+ email +"')";
        try (Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            System.out.println("Inserted :"+rows);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private static void selectStudents (Connection conn){
        String sql = "Select * From Student";
        try(Statement stmt = conn.createStatement()){
            ResultSet resultSet = stmt.executeQuery(sql);
            System.out.println("Student List: ");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.println(id + " : "+ name + " : " + email + " : ");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private static void updateStudent (Connection conn, int id,String name,String email){
        String sql = "UPDATE student SET name = '" + name + "', email = '" + email + "' WHERE id = " + id;
        // UPDATE student SET name ='Alice',email ='email@gmail.com'
        // where id=10;
        try (Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            System.out.println(" UPDATED "+rows);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void deleteStudent (Connection conn, int id){
        String sql = "DELETE FROM student WHERE id = "+id;
        try (Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            System.out.println(" Deleted "+rows);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}


/*  connection to sql
Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Connected to the Database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                System.out.println("Connection Closed");
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
 */
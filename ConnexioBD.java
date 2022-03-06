import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexioBD {
    private String servidor;
    private String bbdd;
    private String user;
    private String password;

    static Connection connexioBD = null;

    public void connexioBD() {

        servidor = "jdbc:mysql://localhost:3306/";
        bbdd = "db_gimnas";
        user = "root";
        password = "root";

        try {
            connexioBD = DriverManager.getConnection(servidor + bbdd, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desconnexioBD() throws SQLException {
        connexioBD.close();
    }
}





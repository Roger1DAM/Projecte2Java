import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AppGimnas {
    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        Gimnas g = new Gimnas();
        g.gestionarGimnas();
    }
}
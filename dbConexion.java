import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConexion {
    private static Connection con=null;
    public static Connection getConnection(){
       try{
          if( con == null ){
             String driver="com.mysql.cj.jdbc.Driver"; //el driver varia segun la DB que usemos
             String url="jdbc:mysql://localhost/db_gimnas?autoReconnect=true";
             String pwd="root";
             String usr="root";
             Class.forName(driver);
             con = DriverManager.getConnection(url,usr,pwd);
          }
      }
      catch(ClassNotFoundException | SQLException ex){
         ex.printStackTrace();
      }
      return con;
    }
 }
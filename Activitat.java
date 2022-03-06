import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Activitat {
    private String nom;
    private String descripcio;
    private int duradaSessio;
    private int numReserves;
    private double percentatge;

    public ArrayList<Activitat> visualitzarActivitats() throws SQLException {
        ArrayList<Activitat> activitats = new ArrayList<>();
        String sql = "SELECT a.*, count(b.id) AS Reserves, (count(b.id)/(count(b.id) + d.aforament_max) * 100) AS Percentatge FROM activitat a, reserva_lliure b, es_fa c, sala d WHERE a.id = b.id_act AND c.id = a.id AND c.num = d.num group by a.id;";
        Connection c = dbConexion.getConnection();
        PreparedStatement sentencia = c.prepareStatement(sql);
        sentencia.executeQuery();
        ResultSet rs = sentencia.executeQuery();
        
        while (rs.next()) {
            Activitat c1 = new Activitat();
            c1.afegirDadesActivitat(rs);
            activitats.add(c1);
        }
        return activitats;   

    }

    private Activitat afegirDadesActivitat(ResultSet rs) throws SQLException {
        this.setNom (rs.getString("nom"));
        this.setDescripcio (rs.getString("descripcio"));
        this.setDuradaSessio (rs.getInt("durada_sessio"));
        this.setNumReserves (rs.getInt("Reserves"));
        this.setPercentatge (rs.getInt("Percentatge"));  

        return this;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public int getDuradaSessio() {
        return duradaSessio;
    }

    public void setDuradaSessio(int duradaSessio) {
        this.duradaSessio = duradaSessio;
    }

    public int getNumReserves() {
        return numReserves;
    }

    public void setNumReserves(int numReserves) {
        this.numReserves = numReserves;
    }

    public double getPercentatge() {
        return percentatge;
    }

    public void setPercentatge(double percentatge) {
        this.percentatge = percentatge;
    }

    @Override
    public String toString() {
        return "\nNom: " + nom + "\nDescripció: " + descripcio + "\nDurada Sessió: " + duradaSessio + "\nReserves: " + numReserves + "\nPercentatge d'aforament: " + percentatge;
    } 
}

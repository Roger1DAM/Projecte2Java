import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicTreeUI.TreeCancelEditingAction;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Client {

    //DNI, CORREU, TELEFON, COMPTE BANCARI HAN DE SER CLASSES A PART.
    private Dni dni;
    private String nom;
    private String cognom;
    private Telefon telefon;
    private Correu correu;
    private LocalDate data_naix;
    private char sexe;
    private String usuari;
    private String contrasenya;
    private boolean comunicacio_com;
    private CompteBancari compte_bancari;
    private String condicio;
    private int edat;
    private int reserves;


    static ConnexioBD con = new ConnexioBD();
    
    public void consultarClient() throws SQLException {
        Scanner teclat = new Scanner(System.in);
        System.out.println("Introdueix el DNI del client");
        String dni = teclat.nextLine();
        Client c = consultarClientBD(dni);

        if (c != null) {
            System.out.println(c.toString());
        } else {
            System.out.println("No s'ha trobat el client.");
        } 
    }

    public Client consultarClientBD(String dni) throws SQLException {
        
        con.connexioBD();
        String consulta = "SELECT * FROM client WHERE dni = ?";
        PreparedStatement sentencia = con.connexioBD.prepareStatement(consulta);

        sentencia.setString(1, dni);

        sentencia.executeQuery();

        ResultSet rs = sentencia.executeQuery();


        while (rs.next()) {
            afegirDadesClient(rs);
            return this;
        }       

        return null;    
    }

    private Client afegirDadesClient(ResultSet rs) throws SQLException {

        //UTILITZAR SETTERS PER AFEGIR LES DADES.
        //DNI, CORREU I COMPTE_BANCARI S'HAN DE CREAR OBJECTES.
        this.setDni (new Dni(rs.getString("dni")));
        this.setNom (rs.getString("nom"));
        this.setCognom (rs.getString("cognom"));
        this.setTelefon (new Telefon(rs.getString("telefon")));
        this.setCorreu (new Correu(rs.getString("email")));
        this.setData_naix (rs.getDate("data_neixement").toLocalDate());
        this.setSexe (rs.getString("sexe").charAt(0));
        this.setUsuari (rs.getString("usuari"));
        this.setComunicacio_com (rs.getBoolean("comunicacio_comercial"));
        this.setCompte_bancari (new CompteBancari(rs.getString("compte_bancari")));
        this.setCondicio (rs.getString("condicio"));

        calcularEdat();

        return this;
    } 

    public void altaClient() throws SQLException, NoSuchAlgorithmException {
        Scanner teclat = new Scanner(System.in);
        Dni dniObj = new Dni();
        String dni;
        
        do {
            System.out.print("Introdueix el DNI del client que vols afegir: ");
            dni = teclat.next();
            
        } while (!dniObj.validarDNI(dni)); 
        dniObj.setDni(dni);
        setDni(dniObj);

        //També s'ha de comprovar que aquest DNI no està donat d'alta a la BD.
        if (consultarClientBD(dniObj.getDni()) != null) {
            System.out.println("Aquest DNI ja està donat d'alta");
        } else {
        
        System.out.print("Introdueix el nom: ");
        nom = teclat.next();
        System.out.print("Introdueix el cognom: ");
        cognom = teclat.next();

        Telefon telefonObj = new Telefon();
        String telefon;

        do {
            System.out.print("Introdueix el telefon: ");
            telefon = teclat.next();
        } while (!telefonObj.validarTel(telefon));

        telefonObj.setTelf(telefon);    
        setTelefon(telefonObj);    
        
        Correu correuObj = new Correu();
        String correu;

        do {
            System.out.print("Introdueix el correu: ");
            correu = teclat.next();
        } while (!correuObj.validarCorreu(correu));

        correuObj.setCorreu(correu);
        setCorreu(correuObj);
        
        boolean dataCorrecta;

        do {
            dataCorrecta = true;
            System.out.print("Introdueix la data de naixement (AAAA-M-D): ");
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-M-d");
            try {
                this.data_naix = LocalDate.parse(teclat.next(), dateFormat);
            } catch (Exception e) {
                dataCorrecta = false;
            }
        } while (!dataCorrecta);
        
        char sexe;

        do {
            System.out.print("Introdueix el sexe (H/D): ");
            sexe = teclat.next().toUpperCase().charAt(0);
        } while (sexe != 'H' && sexe != 'D');
        setSexe(sexe);
        
        System.out.print("Introdueix l'usuari: ");
        usuari = teclat.next();

        System.out.print("Introdueix la contrasenya: ");
        contrasenya = encriptarContrasenya(teclat.next());
        
        // Console console = System.console();
        // char[] password = console.readPassword("Introdueix la contrasenya: ");  

        CompteBancari cb = new CompteBancari();
        String compte_bancari;

        do {
            System.out.print("Introdueix el teu compte bancari: ");
            compte_bancari = teclat.next();
        } while (!cb.validarCompteBancari(compte_bancari));

        cb.setCompte(compte_bancari);
        setCompte_bancari(cb);

        System.out.print("Introdueix alguna condició física a tenir en compte: ");

        condicio = teclat.nextLine();    
        teclat.nextLine();  

        Character comunicacio = null;
        do {
            System.out.print("Vols rebre comunicació comercial al correu? (S/N) ");
            comunicacio = teclat.next().toUpperCase().charAt(0);
        if (comunicacio.equals('S')) {
            comunicacio_com = true;
        } else if (comunicacio.equals('N')) {
            comunicacio_com = false;
        }
        } while (!comunicacio.equals('S') && !comunicacio.equals('N'));

        altaClientBD();
        }
    }

    private void altaClientBD() throws SQLException {
        //Per posar a una consulta el valor d'un OBJECTE utilitzarem un getter.
        //ps.setString(1, this.dni.getDni());

        try {
            String consulta = "INSERT INTO client VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = con.connexioBD.prepareStatement(consulta);
            sentencia.setString(1, this.dni.getDni());
            sentencia.setString(2, this.getNom());
            sentencia.setString(3, this.getCognom());
            sentencia.setString(4, this.telefon.getTelf());
            sentencia.setString(5, this.correu.getCorreu());
            sentencia.setString(6, String.valueOf(this.getSexe()));
            sentencia.setDate(7, Date.valueOf(this.data_naix));
            sentencia.setString(8, this.getUsuari());
            sentencia.setString(9, this.getContrasenya());
            sentencia.setString(10, this.compte_bancari.getCompte());
            sentencia.setString(11, this.getCondicio());
            sentencia.setBoolean(12, this.getComunicacio_com());

            sentencia.executeUpdate();

            String consulta2 = "INSERT INTO alta VALUES()";
            PreparedStatement sentencia2 = con.connexioBD.prepareStatement(consulta2);
            sentencia2.executeUpdate();

            String consulta3 = "SELECT * FROM alta order by id desc limit 1";
            PreparedStatement sentencia3 = con.connexioBD.prepareStatement(consulta3);
            sentencia3.executeQuery();

            ResultSet rs = sentencia3.executeQuery();
            
            int id_alta = 0;
            while (rs.next()) {
                id_alta = (rs.getInt("id"));
            }

            String consulta4 = "INSERT INTO es_dona(data_alta, dni, id) VALUES(?, ?, ?)";
            PreparedStatement sentencia4 = con.connexioBD.prepareStatement(consulta4);
            LocalDate lt = LocalDate.now();

            sentencia4.setDate(1, Date.valueOf(lt));
            sentencia4.setString(2, this.dni.getDni());
            sentencia4.setInt(3, id_alta);

            sentencia4.executeUpdate();


            System.out.println("S'ha donat d'alta correctament.");
        } catch (Exception e) {
            System.out.println("No s'ha pogut donar d'alta el client.");
            e.printStackTrace();
        }
    }

    

    public ArrayList<Client> ordenarPerCognom() throws SQLException{
        ArrayList<Client> clients = new ArrayList<>();
        String consulta = "SELECT * FROM client order by cognom";
        Connection c = dbConexion.getConnection();
        PreparedStatement sentencia = c.prepareStatement(consulta);
        sentencia.executeQuery();

        ResultSet rs = sentencia.executeQuery();

        while (rs.next()) {
            Client c1 = new Client();
            c1.afegirDadesClient(rs);
            clients.add(c1);
        }

        return clients;
        
    }

    public ArrayList<Client> ordenarPerEdat() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();
        String consulta = "SELECT * FROM client order by data_neixement";
        Connection c = dbConexion.getConnection();
        PreparedStatement sentencia = c.prepareStatement(consulta);
        sentencia.executeQuery();
        ResultSet rs = sentencia.executeQuery();

        while (rs.next()) {
            Client c1 = new Client();
            c1.afegirDadesClient(rs);
            clients.add(c1);
        }       
        return clients;
    }

    public ArrayList<Client> ordenarPerReserves() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();
        con.connexioBD();
        String sql = "SELECT client.*,count(reserva_colectiva.dni) + (SELECT count(reserva_lliure.dni) FROM reserva_lliure WHERE reserva_lliure.dni = client.dni)as Reserves FROM reserva_colectiva, client WHERE reserva_colectiva.dni = client.dni GROUP BY reserva_colectiva.dni  ORDER BY count(reserva_colectiva.dni) + (SELECT count(reserva_lliure.dni) FROM reserva_lliure WHERE reserva_lliure.dni = client.dni) desc";
        Connection c = dbConexion.getConnection();
        PreparedStatement sentencia = c.prepareStatement(sql);
        sentencia.executeQuery();
        ResultSet rs = sentencia.executeQuery();
        
        while (rs.next()) {
            Client c1 = new Client();
            c1.afegirDadesClient(rs);
            c1.setReserves((rs.getInt("Reserves")));
            clients.add(c1);
        }
        return clients;   
    }

    private void calcularEdat() {
        LocalDate ara = LocalDate.now();
        this.edat = Period.between(this.data_naix, ara).getYears();
    }

    @Override
    public String toString() {
        return "\nDni: " + dni + "\nNom: " + nom + "\nCognom: " + cognom + "\nTelefon: " + telefon + "\nCorreu: " + correu + "\nData naixement: " + data_naix + "\nSexe: " + sexe + "\nUsuari: " + usuari + "\nEdat: " + edat + "\nReserves: " + reserves;
    }

    private String encriptarContrasenya(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString(16);
    
    }

    public void baixaClient() throws SQLException {
        Scanner teclat = new Scanner(System.in);
        System.out.print("Introdueix el DNI del client que vols donar de baixa: ");
        String dni = teclat.next();
        LocalDate lt = LocalDate.now();

        String consulta = "UPDATE es_dona SET data_baixa = ? WHERE dni = ?";
        Connection c = dbConexion.getConnection();
        PreparedStatement sentencia = c.prepareStatement(consulta);
        sentencia.setDate(1, Date.valueOf(lt));
        sentencia.setString(2, dni);

        sentencia.executeUpdate();
        System.out.println("El client s'ha donat de baixa correctament.");
    }

    public void modificarClient() throws NoSuchAlgorithmException, SQLException {
        Scanner teclat = new Scanner(System.in);
        Dni dniObj = new Dni();
        String dni;
        do {
            System.out.print("Introdueix el DNI del client que vols modificar: ");
            dni = teclat.next();
            
        } while (!dniObj.validarDNI(dni)); 
        dniObj.setDni(dni);
        setDni(dniObj);

        if (consultarClientBD(dniObj.getDni()) == null) {
            System.out.println("Aquest DNI no està donat d'alta");
        } else {
        
        Telefon telefonObj = new Telefon();
        String telefon;

        do {
            System.out.print("Introdueix el telefon: ");
            telefon = teclat.next();
        } while (!telefonObj.validarTel(telefon));

        telefonObj.setTelf(telefon);    
        setTelefon(telefonObj);    
        
        Correu correuObj = new Correu();
        String correu;

        do {
            System.out.print("Introdueix el correu: ");
            correu = teclat.next();
        } while (!correuObj.validarCorreu(correu));

        correuObj.setCorreu(correu);
        setCorreu(correuObj);
                
        char sexe;

        do {
            System.out.print("Introdueix el sexe (H/D): ");
            sexe = teclat.next().toUpperCase().charAt(0);
        } while (sexe != 'H' && sexe != 'D');
        setSexe(sexe);
        
        System.out.print("Introdueix l'usuari: ");
        usuari = teclat.next();

        System.out.print("Introdueix la contrasenya: ");
        contrasenya = encriptarContrasenya(teclat.next());

        System.out.print("Introdueix alguna condició física a tenir en compte: ");

        condicio = teclat.nextLine();    
        teclat.nextLine();  

        Character comunicacio = null;
        do {
            System.out.print("Vols rebre comunicació comercial al correu? (S/N) ");
            comunicacio = teclat.next().toUpperCase().charAt(0);
        if (comunicacio.equals('S')) {
            comunicacio_com = true;
        } else if (comunicacio.equals('N')) {
            comunicacio_com = false;
        }
        } while (!comunicacio.equals('S') && !comunicacio.equals('N'));
    
        modificarClientBD();
        }
    }

    private void modificarClientBD() {
        try {
            String consulta = "UPDATE client SET telefon = ?, email = ?, sexe = ?, usuari = ?, contrasenya = ?, comunicacio_comercial = ? WHERE dni = ?";
            PreparedStatement sentencia = con.connexioBD.prepareStatement(consulta);
            sentencia.setString(1, this.telefon.getTelf());
            sentencia.setString(2, this.correu.getCorreu());
            sentencia.setString(3, String.valueOf(this.getSexe()));
            sentencia.setString(4, this.getUsuari());
            sentencia.setString(5, this.getContrasenya());
            sentencia.setBoolean(6, this.getComunicacio_com());
            sentencia.setString(7, this.dni.getDni());

            sentencia.executeUpdate();

            System.out.println("S'ha donat modificat correctament.");
        } catch (Exception e) {
            System.out.println("No s'ha pogut modificar el client.");
            e.printStackTrace();
        }
    }
    
    public Dni getDni() {
        return dni;
    }

    public void setDni(Dni dni) {
        this.dni = dni;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public Telefon getTelefon() {
        return telefon;
    }

    public void setTelefon(Telefon telefon) {
        this.telefon = telefon;
    }

    public Correu getCorreu() {
        return correu;
    }

    public void setCorreu(Correu correu) {
        this.correu = correu;
    }

    public LocalDate getData_naix() {
        return data_naix;
    }

    public void setData_naix(LocalDate data_naix) {
        this.data_naix = data_naix;
    }

    public char getSexe() {
        return sexe;
    }

    public void setSexe(char sexe) {
        this.sexe = sexe;
    }

    public String getUsuari() {
        return usuari;
    }

    public void setUsuari(String usuari) {
        this.usuari = usuari;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public boolean getComunicacio_com() {
        return comunicacio_com;
    }

    public void setComunicacio_com(boolean comunicacio_com) {
        this.comunicacio_com = comunicacio_com;
    }

    public CompteBancari getCompte_bancari() {
        return compte_bancari;
    }

    public void setCompte_bancari(CompteBancari compte_bancari) {
        this.compte_bancari = compte_bancari;
    }

    public String getCondicio() {
        return condicio;
    }

    public void setCondicio(String condicio) {
        this.condicio = condicio;
    }

    public static ConnexioBD getCon() {
        return con;
    }

    public static void setCon(ConnexioBD con) {
        Client.con = con;
    }

    public int getEdat() {
        return edat;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    public int getReserves() {
        return reserves;
    }

    public void setReserves(int reserves) {
        this.reserves = reserves;
    }
    
    
}

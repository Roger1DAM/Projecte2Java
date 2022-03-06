public class Dni {
    private String dni;
    private int numeros;
    private char lletra;

    public boolean validarDNI(String dni) {

        if ( dni.length() != 9 ) return false;
        
        char ultPos = dni.charAt(8);
        if (!(Character.isLetter(ultPos))) return false;

        char majuscula = Character.toUpperCase(ultPos);
        String dniNum = dni.substring(0,8);
        char[] lletresDni = {'T', 'R', 'W', 'A', 'G', 'M', 'Y' ,'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        int dniNums = 0;

        try {
            dniNums = Integer.parseInt(dniNum);            
        } catch (Exception e) {
            return false;
        }

        int reste = dniNums % 23;
        
        if (majuscula == lletresDni[reste]) {
            return true;
        }
        return false;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

    public Dni(String dni) {
        this.dni = dni;
    }

    public Dni() {
    }

    @Override
    public String toString() {
        return "Dni [dni=" + dni + "]";
    }
    
}

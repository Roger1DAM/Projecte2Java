import java.util.regex.Pattern;

public class Correu {
    private String correu;
    private String usuari;
    private String domini;

    public boolean validarCorreu(String correu) {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        java.util.regex.Matcher mather = pattern.matcher(correu);
 
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }

    public Correu(String correu) {
        this.correu = correu;
    }

    public Correu() {
        this.correu = correu;
    }


    @Override
    public String toString() {
        return "Correu [correu=" + correu + "]";
    }

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }
}

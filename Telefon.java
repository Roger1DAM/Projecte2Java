public class Telefon {
    private String telf;
    
    public boolean validarTel(String telefon) {
        if (telefon == null) return false;
        if (telefon.length() != 9) return false;
        
        for (int i = 0; i < telefon.length(); i++) {
            char c = telefon.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public Telefon(String telf) {
        this.telf = telf;
    }

    public Telefon() {
        this.telf = telf;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    @Override
    public String toString() {
        return "Telefon [telf=" + telf + "]";
    }
}

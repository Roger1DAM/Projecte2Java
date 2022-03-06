import java.math.BigInteger;

public class CompteBancari {
    private String compte;
    private String numeros;
    private char lletra;
    
    public boolean validarCompteBancari(String cb) {
        boolean esValido = false;
		int i = 2;
		int caracterASCII = 0; 
		int resto = 0;
		int dc = 0;
		String cadenaDc = "";
		BigInteger cbNumero = new BigInteger("0"); 
		BigInteger modo = new BigInteger("97");

		if(cb.length() == 24 && cb.substring(0,1).toUpperCase().equals("E") 
			&& cb.substring(1,2).toUpperCase().equals("S")) {

			do {
				caracterASCII = cb.codePointAt(i);
				esValido = (caracterASCII > 47 && caracterASCII < 58);
				i++;
			}
			while(i < cb.length() && esValido); 
		
		
			if(esValido) {
				cbNumero = new BigInteger(cb.substring(4,24) + "142800");
				resto = cbNumero.mod(modo).intValue();
				dc = 98 - resto;
				cadenaDc = String.valueOf(dc);
			}	
			
			if(dc < 10) {
				cadenaDc = "0" + cadenaDc;
			} 

			// Comparamos los caracteres 2 y 3 de la cuenta (dígito de control IBAN) con cadenaDc
			if(cb.substring(2,4).equals(cadenaDc)) {
				esValido = true;
			} else {
				esValido = false;
			}
		}

		return esValido;
	}

    public CompteBancari(String compte) {
        this.compte = compte;
    }

    public CompteBancari() {
        this.compte = compte;
    }

	public String getCompte() {
		return compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}
}

import  java.time.LocalDate;

public class Accidente {

    private final String numExpediente;
    private final LocalDate fecha;
    private final String tipoAccidente;
    private final String sexo;
    private final String positivaAlcohol;
    private final String positivaDroga;

    public Accidente(
            String numExpediente,
            LocalDate fecha,
            String tipoAccidente,
            String sexo,
            String positivaAlcohol,
            String positivaDroga
    ) {
        this.numExpediente = numExpediente;
        this.fecha = fecha;
        this.tipoAccidente = tipoAccidente;
        this.sexo = sexo;
        this.positivaAlcohol = positivaAlcohol;
        this.positivaDroga = positivaDroga;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipoAccidente() {
        return tipoAccidente;
    }

    public String getSexo() {
        return sexo;
    }

    public String getPositivaAlcohol() {
        return positivaAlcohol;
    }

    public String getPositivaDroga() {
        return positivaDroga;
    }

    @Override
    public String toString() {
        return "Accidente[" +
                "numExpediente='" + numExpediente + '\'' +
                ", fecha=" + fecha +
                ", tipoAccidente='" + tipoAccidente + '\'' +
                ", sexo='" + sexo + '\'' +
                ", positivaAlcohol='" + positivaAlcohol + '\'' +
                ", positivaDroga='" + positivaDroga + '\'' +
                ']';
    }
}

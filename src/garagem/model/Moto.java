package garagem.model;

/**
 * Representa uma Motocicleta na garagem.
 * Aplica tarifa reduzida de permanência: R$ 5,00 por hora.
 */
public class Moto extends Veiculo {
    private int cilindradas;
    public static final double TARIFA_HORA = 5.00;

    public Moto(String placa, String marca, String modelo, int ano, TipoCombustivel tipoCombustivel, int cilindradas) {
        super(placa, marca, modelo, ano, tipoCombustivel);
        this.cilindradas = cilindradas;
    }

    @Override
    public double calcularTarifa(long horasPermanencia) {
        long horasCobradas = Math.max(1, horasPermanencia);
        return horasCobradas * TARIFA_HORA;
    }

    @Override
    public double getTarifaHora() {
        return TARIFA_HORA;
    }

    @Override
    public String getTipoVeiculo() {
        return "Moto";
    }

    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Cilindradas: %d cc | Tarifa/h: R$ %.2f", cilindradas, TARIFA_HORA);
    }
}

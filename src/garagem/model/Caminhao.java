package garagem.model;

/**
 * Representa um Caminhão / veículo pesado na garagem.
 * Aplica tarifa maior de permanência: R$ 20,00 por hora.
 */
public class Caminhao extends Veiculo {
    private int numeroEixos;
    public static final double TARIFA_HORA = 20.00;

    public Caminhao(String placa, String marca, String modelo, int ano, TipoCombustivel tipoCombustivel, int numeroEixos) {
        super(placa, marca, modelo, ano, tipoCombustivel);
        this.numeroEixos = numeroEixos;
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
        return "Caminhão";
    }

    public int getNumeroEixos() {
        return numeroEixos;
    }

    public void setNumeroEixos(int numeroEixos) {
        this.numeroEixos = numeroEixos;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Eixos: %d | Tarifa/h: R$ %.2f", numeroEixos, TARIFA_HORA);
    }
}

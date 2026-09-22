package garagem.model;

/**
 * Representa um Carro de passeio na garagem.
 * Aplica tarifa padrão de permanência: R$ 10,00 por hora.
 */
public class Carro extends Veiculo {
    private int quantidadePortas;
    public static final double TARIFA_HORA = 10.00;

    public Carro(String placa, String marca, String modelo, int ano, TipoCombustivel tipoCombustivel, int quantidadePortas) {
        super(placa, marca, modelo, ano, tipoCombustivel);
        this.quantidadePortas = quantidadePortas;
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
        return "Carro";
    }

    public int getQuantidadePortas() {
        return quantidadePortas;
    }

    public void setQuantidadePortas(int quantidadePortas) {
        this.quantidadePortas = quantidadePortas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Portas: %d | Tarifa/h: R$ %.2f", quantidadePortas, TARIFA_HORA);
    }
}

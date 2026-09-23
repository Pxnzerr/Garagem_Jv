package garagem.model;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Classe abstrata base para todos os veículos da garagem.
 * Demonstra os conceitos de Abstração e Encapsulamento.
 */
public abstract class Veiculo {
    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private TipoCombustivel tipoCombustivel;
    private LocalDateTime horarioEntrada;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Veiculo(String placa, String marca, String modelo, int ano, TipoCombustivel tipoCombustivel) {
        setPlaca(placa);
        this.marca = marca;
        this.modelo = modelo;
        setAno(ano);
        this.tipoCombustivel = tipoCombustivel;
        this.horarioEntrada = null;
    }

    /**
     * Método abstrato para cálculo de tarifa.
     * Cada subclasse implementará sua regra de cobrança específica (Polimorfismo).
     *
     * @param horasPermanencia tempo de permanência calculado em horas
     * @return valor total da tarifa a ser cobrada
     */
    public abstract double calcularTarifa(long horasPermanencia);

    /**
     * Retorna a tarifa por hora do veículo (Polimorfismo).
     */
    public abstract double getTarifaHora();

    /**
     * Retorna a descrição legível do tipo específico do veículo (ex: Carro, Moto, Caminhão).
     */
    public abstract String getTipoVeiculo();

    public boolean estaEstacionado() {
        return this.horarioEntrada != null;
    }

    public void registrarEntrada(LocalDateTime horario) {
        this.horarioEntrada = horario;
    }

    public void registrarSaida() {
        this.horarioEntrada = null;
    }

    // Getters e Setters (Encapsulamento)

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("A placa não pode ser vazia.");
        }
        this.placa = placa.trim().toUpperCase();
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        int anoMaximo = Year.now().getValue() + 1;
        if (ano < 1886 || ano > anoMaximo) {
            throw new IllegalArgumentException("Ano do veículo inválido (deve ser entre 1886 e " + anoMaximo + ").");
        }
        this.ano = ano;
    }

    public TipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public LocalDateTime getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(LocalDateTime horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(placa, veiculo.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(placa);
    }

    @Override
    public String toString() {
        String status = estaEstacionado() 
                ? "Estacionado desde " + horarioEntrada.format(FORMATTER) 
                : "Fora da garagem";
        return String.format("[%s] Placa: %-8s | %s %s (%d) | Combustível: %-8s | Status: %s",
                getTipoVeiculo(), placa, marca, modelo, ano, tipoCombustivel.getDescricao(), status);
    }
}

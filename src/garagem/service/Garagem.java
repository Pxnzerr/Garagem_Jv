package garagem.service;

import garagem.model.Veiculo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Classe responsável por gerenciar os veículos, vagas, entradas e saídas da garagem.
 */
public class Garagem {

    private final Map<String, Veiculo> veiculosPorPlaca;
    private int veiculosEstacionados;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Registro imutável (record) contendo os dados do recibo de saída.
     */
    public record ReciboSaida(
            Veiculo veiculo,
            LocalDateTime entrada,
            LocalDateTime saida,
            long minutosPermanencia,
            long horasCobradas,
            double valorTotal
    ) {
        public String gerarComprovante() {
            return String.format(
                """
                ------------------------------------------------------------
                                     RECIBO DE PAGAMENTO
                ------------------------------------------------------------
                Veículo:      %s %s (%s)
                Placa:        %s
                Entrada:      %s
                Saída:        %s
                Tempo Total:  %d hora(s) e %d minuto(s) (Total: %d min)
                Horas Cobr.:  %d h (mínimo de 1h, frações viram hora cheia)
                Valor Total:  R$ %.2f
                ------------------------------------------------------------""",
                veiculo.getMarca(), veiculo.getModelo(), veiculo.getTipoVeiculo(),
                veiculo.getPlaca(),
                entrada.format(FORMATTER),
                saida.format(FORMATTER),
                minutosPermanencia / 60, minutosPermanencia % 60, minutosPermanencia,
                horasCobradas,
                valorTotal
            );
        }
    }

    public Garagem() {
        this.veiculosPorPlaca = new LinkedHashMap<>();
        this.veiculosEstacionados = 0;
    }

    /**
     * Adiciona um novo veículo ao sistema da garagem em O(1).
     * Não permite placas duplicadas.
     *
     * @param veiculo instância de Veiculo (Carro, Moto ou Caminhão)
     * @return true se adicionado com sucesso, false se placa já existir
     */
    public boolean adicionarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo.");
        }
        String placaFormatada = veiculo.getPlaca().toUpperCase();
        if (veiculosPorPlaca.containsKey(placaFormatada)) {
            return false;
        }
        veiculosPorPlaca.put(placaFormatada, veiculo);
        if (veiculo.estaEstacionado()) {
            veiculosEstacionados++;
        }
        return true;
    }

    /**
     * Retorna a lista imutável de veículos cadastrados em ordem de cadastro.
     */
    public List<Veiculo> listarVeiculos() {
        return Collections.unmodifiableList(new ArrayList<>(veiculosPorPlaca.values()));
    }

    /**
     * Busca um veículo cadastrado pela placa em O(1) (ignora maiúsculas/minúsculas).
     */
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        if (placa == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(veiculosPorPlaca.get(placa.trim().toUpperCase()));
    }

    /**
     * Registra a entrada de um veículo no momento atual.
     */
    public boolean registrarEntrada(String placa) {
        return registrarEntrada(placa, LocalDateTime.now());
    }

    /**
     * Registra a entrada de um veículo com um horário específico (útil para testes/simulações).
     */
    public boolean registrarEntrada(String placa, LocalDateTime horarioEntrada) {
        Optional<Veiculo> opt = buscarPorPlaca(placa);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Veículo com a placa " + placa + " não encontrado no cadastro.");
        }
        Veiculo v = opt.get();
        if (v.estaEstacionado()) {
            throw new IllegalStateException("O veículo com a placa " + placa + " já está estacionado na garagem.");
        }
        v.registrarEntrada(horarioEntrada);
        veiculosEstacionados++;
        return true;
    }

    /**
     * Registra a saída de um veículo no momento atual, calculando a tarifa polimorficamente.
     */
    public ReciboSaida registrarSaida(String placa) {
        return registrarSaida(placa, LocalDateTime.now());
    }

    /**
     * Registra a saída de um veículo com um horário específico, calculando a tarifa polimorficamente.
     */
    public ReciboSaida registrarSaida(String placa, LocalDateTime horarioSaida) {
        Optional<Veiculo> opt = buscarPorPlaca(placa);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Veículo com a placa " + placa + " não encontrado no cadastro.");
        }
        Veiculo v = opt.get();
        if (!v.estaEstacionado()) {
            throw new IllegalStateException("O veículo com a placa " + placa + " não está estacionado no momento.");
        }

        LocalDateTime entrada = v.getHorarioEntrada();
        if (horarioSaida.isBefore(entrada)) {
            throw new IllegalArgumentException("Horário de saída não pode ser anterior ao horário de entrada.");
        }

        Duration duracao = Duration.between(entrada, horarioSaida);
        long minutos = duracao.toMinutes();

        // Regra de arredondamento: mínimo 1 hora; qualquer fração de hora adicional conta como hora cheia
        long horasCobradas = (long) Math.ceil(Math.max(1.0, minutos / 60.0));

        // Invocação polimórfica: cada subclasse (Carro, Moto, Caminhao) calcula sua tarifa
        double valorTotal = v.calcularTarifa(horasCobradas);

        // Desocupa a vaga
        v.registrarSaida();
        veiculosEstacionados--;

        return new ReciboSaida(v, entrada, horarioSaida, minutos, horasCobradas, valorTotal);
    }

    /**
     * Remove um veículo do cadastro da garagem em O(1).
     * Não permite remoção se o veículo estiver estacionado no momento.
     */
    public boolean removerVeiculo(String placa) {
        if (placa == null) {
            return false;
        }
        String placaFormatada = placa.trim().toUpperCase();
        Veiculo v = veiculosPorPlaca.get(placaFormatada);
        if (v == null) {
            return false;
        }
        if (v.estaEstacionado()) {
            throw new IllegalStateException("Não é possível remover o veículo " + placa + " pois ele está atualmente estacionado.");
        }
        veiculosPorPlaca.remove(placaFormatada);
        return true;
    }

    /**
     * Retorna a quantidade total de veículos cadastrados em O(1).
     */
    public int contarTotalVeiculos() {
        return veiculosPorPlaca.size();
    }

    /**
     * Retorna a quantidade de veículos atualmente estacionados na garagem em O(1).
     */
    public int contarVeiculosEstacionados() {
        return veiculosEstacionados;
    }
}

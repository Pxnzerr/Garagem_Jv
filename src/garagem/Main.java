package garagem;

import garagem.model.*;
import garagem.service.Garagem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Ponto de entrada do sistema de gerenciamento de garagem.
 * Apresenta o menu interativo via console.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Garagem garagem = new Garagem();

    public static void main(String[] args) {
        // Carrega alguns veículos de exemplo para demonstrar a aplicação de imediato
        inicializarDadosExemplo();

        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opção: ");
            System.out.println();

            switch (opcao) {
                case 1 -> cadastrarVeiculo();
                case 2 -> listarVeiculos();
                case 3 -> buscarVeiculoPorPlaca();
                case 4 -> registrarEntrada();
                case 5 -> registrarSaida();
                case 6 -> removerVeiculo();
                case 7 -> exibirQuantidadeVeiculos();
                case 8 -> {
                    System.out.println("============================================================");
                    System.out.println(" Encerrando o sistema de garagem. Até logo!");
                    System.out.println("============================================================");
                    executando = false;
                }
                default -> System.out.println("⚠️ Opção inválida. Por favor, escolha um número de 1 a 8.");
            }

            if (executando) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("            SISTEMA DE GERENCIAMENTO DE GARAGEM             ");
        System.out.println("============================================================");
        System.out.println(" 1. Cadastrar um veículo");
        System.out.println(" 2. Listar os veículos cadastrados");
        System.out.println(" 3. Buscar um veículo pela placa");
        System.out.println(" 4. Registrar entrada de um veículo");
        System.out.println(" 5. Registrar saída de um veículo");
        System.out.println(" 6. Remover um veículo");
        System.out.println(" 7. Exibir a quantidade de veículos na garagem");
        System.out.println(" 8. Encerrar o programa");
        System.out.println("============================================================");
    }

    // 1. Cadastrar um veículo
    private static void cadastrarVeiculo() {
        System.out.println("--- [1] CADASTRO DE VEÍCULO ---");
        System.out.println("Selecione o tipo de veículo:");
        System.out.println(" 1. Carro     (Tarifa padrão: R$ 10,00/h)");
        System.out.println(" 2. Moto      (Tarifa reduzida: R$ 5,00/h)");
        System.out.println(" 3. Caminhão  (Tarifa maior: R$ 20,00/h)");
        int tipo = lerInteiro("Opção: ");

        if (tipo < 1 || tipo > 3) {
            System.out.println("⚠️ Tipo de veículo inválido.");
            return;
        }

        String placa = lerTexto("Informe a placa: ").toUpperCase();
        if (garagem.buscarPorPlaca(placa).isPresent()) {
            System.out.println("⚠️ Já existe um veículo cadastrado com a placa " + placa + ".");
            return;
        }

        String marca = lerTexto("Informe a marca: ");
        String modelo = lerTexto("Informe o modelo: ");
        int ano = lerInteiro("Informe o ano de fabricação: ");

        TipoCombustivel combustivel = selecionarCombustivel();

        Veiculo novoVeiculo;
        try {
            switch (tipo) {
                case 1 -> {
                    int portas = lerInteiro("Quantidade de portas: ");
                    novoVeiculo = new Carro(placa, marca, modelo, ano, combustivel, portas);
                }
                case 2 -> {
                    int cilindradas = lerInteiro("Cilindradas (cc): ");
                    novoVeiculo = new Moto(placa, marca, modelo, ano, combustivel, cilindradas);
                }
                case 3 -> {
                    int eixos = lerInteiro("Número de eixos: ");
                    novoVeiculo = new Caminhao(placa, marca, modelo, ano, combustivel, eixos);
                }
                default -> throw new IllegalStateException();
            }

            garagem.adicionarVeiculo(novoVeiculo);
            System.out.println("✅ " + novoVeiculo.getTipoVeiculo() + " cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Erro ao cadastrar: " + e.getMessage());
        }
    }

    // 2. Listar os veículos cadastrados
    private static void listarVeiculos() {
        System.out.println("--- [2] VEÍCULOS CADASTRADOS ---");
        List<Veiculo> veiculos = garagem.listarVeiculos();

        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado no momento.");
            return;
        }

        System.out.printf("Total de veículos cadastrados: %d\n\n", veiculos.size());
        for (int i = 0; i < veiculos.size(); i++) {
            System.out.printf("%2d. %s\n", (i + 1), veiculos.get(i));
        }
    }

    // 3. Buscar um veículo pela placa
    private static void buscarVeiculoPorPlaca() {
        System.out.println("--- [3] BUSCAR VEÍCULO PELA PLACA ---");
        String placa = lerTexto("Digite a placa desejada: ");
        Optional<Veiculo> resultado = garagem.buscarPorPlaca(placa);

        if (resultado.isPresent()) {
            System.out.println("✅ Veículo encontrado:");
            System.out.println(resultado.get());
        } else {
            System.out.println("⚠️ Nenhum veículo cadastrado com a placa '" + placa.toUpperCase() + "'.");
        }
    }

    // 4. Registrar entrada de um veículo
    private static void registrarEntrada() {
        System.out.println("--- [4] REGISTRAR ENTRADA DE VEÍCULO ---");
        String placa = lerTexto("Informe a placa do veículo: ");

        try {
            System.out.println("Deseja simular entrada no passado para testar a cobrança de horas?");
            System.out.println(" 1. Registrar agora (Horário Atual)");
            System.out.println(" 2. Simular entrada há algumas horas atrás");
            int modo = lerInteiro("Escolha: ");

            if (modo == 2) {
                int horasAtras = lerInteiro("Quantas horas atrás o veículo entrou? ");
                LocalDateTime horarioSimulado = LocalDateTime.now().minusHours(horasAtras);
                garagem.registrarEntrada(placa, horarioSimulado);
                System.out.printf("✅ Entrada do veículo [%s] registrada com sucesso (simulado: %d hora(s) atrás)!\n",
                        placa.toUpperCase(), horasAtras);
            } else {
                garagem.registrarEntrada(placa);
                System.out.printf("✅ Entrada do veículo [%s] registrada com sucesso agora!\n", placa.toUpperCase());
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 5. Registrar saída de um veículo
    private static void registrarSaida() {
        System.out.println("--- [5] REGISTRAR SAÍDA DE VEÍCULO ---");
        String placa = lerTexto("Informe a placa do veículo que está saindo: ");

        try {
            Garagem.ReciboSaida recibo = garagem.registrarSaida(placa);
            System.out.println("✅ Saída registrada com sucesso!");
            System.out.println(recibo.gerarComprovante());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 6. Remover um veículo
    private static void removerVeiculo() {
        System.out.println("--- [6] REMOVER VEÍCULO DO CADASTRO ---");
        String placa = lerTexto("Informe a placa do veículo a ser removido: ");

        try {
            boolean removido = garagem.removerVeiculo(placa);
            if (removido) {
                System.out.printf("✅ Veículo com a placa [%s] removido com sucesso!\n", placa.toUpperCase());
            } else {
                System.out.printf("⚠️ Veículo com a placa [%s] não encontrado.\n", placa.toUpperCase());
            }
        } catch (IllegalStateException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    // 7. Exibir a quantidade de veículos na garagem
    private static void exibirQuantidadeVeiculos() {
        System.out.println("--- [7] QUANTIDADE DE VEÍCULOS ---");
        int total = garagem.contarTotalVeiculos();
        int estacionados = garagem.contarVeiculosEstacionados();
        int fora = total - estacionados;

        System.out.println("---------------------------------------------");
        System.out.printf(" Total cadastrado no sistema:   %d veículo(s)\n", total);
        System.out.printf(" Atualmente na garagem:         %d veículo(s)\n", estacionados);
        System.out.printf(" Fora da garagem no momento:    %d veículo(s)\n", fora);
        System.out.println("---------------------------------------------");
    }

    // Helpers de entrada de dados

    private static TipoCombustivel selecionarCombustivel() {
        while (true) {
            System.out.println("Selecione o tipo de combustível:");
            System.out.println(" 1. Gasolina");
            System.out.println(" 2. Etanol");
            System.out.println(" 3. Diesel");
            System.out.println(" 4. Elétrico");
            int opc = lerInteiro("Opção: ");
            switch (opc) {
                case 1 -> { return TipoCombustivel.GASOLINA; }
                case 2 -> { return TipoCombustivel.ETANOL; }
                case 3 -> { return TipoCombustivel.DIESEL; }
                case 4 -> { return TipoCombustivel.ELETRICO; }
                default -> System.out.println("⚠️ Opção de combustível inválida.");
            }
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Entrada inválida. Por favor, digite um número inteiro.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            if (!linha.isEmpty()) {
                return linha;
            }
            System.out.println("⚠️ Este campo não pode ficar em branco.");
        }
    }

    private static void inicializarDadosExemplo() {
        // Pré-cadastra alguns veículos para agilizar os testes iniciais
        Carro carroExemplo = new Carro("ABC1D23", "Toyota", "Corolla", 2022, TipoCombustivel.GASOLINA, 4);
        Moto motoExemplo = new Moto("XYZ9K88", "Honda", "CB 500F", 2023, TipoCombustivel.GASOLINA, 500);
        Caminhao caminhaoExemplo = new Caminhao("MNO4T55", "Volvo", "FH 540", 2021, TipoCombustivel.DIESEL, 6);

        garagem.adicionarVeiculo(carroExemplo);
        garagem.adicionarVeiculo(motoExemplo);
        garagem.adicionarVeiculo(caminhaoExemplo);

        // Deixa o carro pré-estacionado há 2 horas para demonstrar a saída
        carroExemplo.registrarEntrada(LocalDateTime.now().minusHours(2).minusMinutes(15));
    }
}

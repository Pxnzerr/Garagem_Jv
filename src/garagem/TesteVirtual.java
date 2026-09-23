package garagem;

import garagem.model.*;
import garagem.service.Garagem;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Classe de testes automatizados para validar todas as regras de negócio e POO.
 */
public class TesteVirtual {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTES AUTOMATIZADOS DO SISTEMA DE GARAGEM ===");
        int testesPassados = 0;
        int totalTestes = 0;

        totalTestes++;
        if (testarPolimorfismoTarifas()) testesPassados++;

        totalTestes++;
        if (testarCadastroEPlacaDuplicada()) testesPassados++;

        totalTestes++;
        if (testarBuscarPorPlaca()) testesPassados++;

        totalTestes++;
        if (testarEntradaESaida()) testesPassados++;

        totalTestes++;
        if (testarRemoverVeiculo()) testesPassados++;

        totalTestes++;
        if (testarContagemVeiculos()) testesPassados++;

        totalTestes++;
        if (testarDesempenhoGrandeVolume()) testesPassados++;

        System.out.println("\n============================================================");
        System.out.printf("RESULTADO FINAL: %d de %d testes passaram com sucesso!\n", testesPassados, totalTestes);
        System.out.println("============================================================");

        if (testesPassados != totalTestes) {
            System.exit(1);
        }
    }

    private static boolean testarPolimorfismoTarifas() {
        System.out.print("[TESTE 1] Polimorfismo no cálculo de tarifas... ");
        Veiculo c = new Carro("CAR1111", "VW", "Gol", 2020, TipoCombustivel.ETANOL, 4);
        Veiculo m = new Moto("MOT2222", "Yamaha", "Fazer", 2021, TipoCombustivel.GASOLINA, 250);
        Veiculo t = new Caminhao("CAM3333", "Scania", "R450", 2019, TipoCombustivel.DIESEL, 4);

        // 3 horas de permanência
        // Carro: 3 * 10 = 30
        // Moto: 3 * 5 = 15
        // Caminhão: 3 * 20 = 60
        boolean ok = (c.calcularTarifa(3) == 30.00)
                  && (m.calcularTarifa(3) == 15.00)
                  && (t.calcularTarifa(3) == 60.00);

        if (ok) {
            System.out.println("PASSOU (Carro: R$30, Moto: R$15, Caminhão: R$60)");
            return true;
        } else {
            System.out.println("FALHOU");
            return false;
        }
    }

    private static boolean testarCadastroEPlacaDuplicada() {
        System.out.print("[TESTE 2] Cadastro e prevenção de placa duplicada... ");
        Garagem g = new Garagem();
        Veiculo v1 = new Carro("AAA1234", "Fiat", "Uno", 2015, TipoCombustivel.GASOLINA, 2);
        Veiculo v2 = new Carro("aaa1234", "Fiat", "Palio", 2018, TipoCombustivel.ETANOL, 4);

        boolean add1 = g.adicionarVeiculo(v1);
        boolean add2 = g.adicionarVeiculo(v2); // deve falhar por placa duplicada (case-insensitive)

        if (add1 && !add2 && g.contarTotalVeiculos() == 1) {
            System.out.println("PASSOU");
            return true;
        } else {
            System.out.println("FALHOU");
            return false;
        }
    }

    private static boolean testarBuscarPorPlaca() {
        System.out.print("[TESTE 3] Busca de veículo por placa... ");
        Garagem g = new Garagem();
        Veiculo v = new Moto("MOT9999", "Honda", "Hornet", 2014, TipoCombustivel.GASOLINA, 600);
        g.adicionarVeiculo(v);

        Optional<Veiculo> achado = g.buscarPorPlaca("mot9999");
        Optional<Veiculo> naoAchado = g.buscarPorPlaca("ZZZ0000");

        if (achado.isPresent() && achado.get().getPlaca().equals("MOT9999") && naoAchado.isEmpty()) {
            System.out.println("PASSOU");
            return true;
        } else {
            System.out.println("FALHOU");
            return false;
        }
    }

    private static boolean testarEntradaESaida() {
        System.out.print("[TESTE 4] Registro de entrada, saída e cobrança... ");
        Garagem g = new Garagem();
        Veiculo c = new Carro("XYZ8888", "Honda", "Civic", 2021, TipoCombustivel.GASOLINA, 4);
        g.adicionarVeiculo(c);

        LocalDateTime entrada = LocalDateTime.now().minusHours(2);
        LocalDateTime saida = LocalDateTime.now();

        g.registrarEntrada("XYZ8888", entrada);
        if (!c.estaEstacionado()) {
            System.out.println("FALHOU (veículo deveria estar estacionado)");
            return false;
        }

        Garagem.ReciboSaida recibo = g.registrarSaida("XYZ8888", saida);

        // 2 horas de carro = R$ 20,00
        boolean ok = (recibo.valorTotal() == 20.00)
                  && !c.estaEstacionado()
                  && recibo.horasCobradas() == 2;

        if (ok) {
            System.out.println("PASSOU (Tarifa calculada: R$ 20,00 para 2h)");
            return true;
        } else {
            System.out.println("FALHOU");
            return false;
        }
    }

    private static boolean testarRemoverVeiculo() {
        System.out.print("[TESTE 5] Remoção de veículo e proteção contra remover estacionado... ");
        Garagem g = new Garagem();
        Veiculo m = new Moto("REM1111", "Suzuki", "Bandit", 2016, TipoCombustivel.GASOLINA, 650);
        g.adicionarVeiculo(m);
        g.registrarEntrada("REM1111");

        boolean impediuRemocaoEstacionado = false;
        try {
            g.removerVeiculo("REM1111");
        } catch (IllegalStateException e) {
            impediuRemocaoEstacionado = true;
        }

        g.registrarSaida("REM1111");
        boolean removeuAposSaida = g.removerVeiculo("REM1111");

        if (impediuRemocaoEstacionado && removeuAposSaida && g.contarTotalVeiculos() == 0) {
            System.out.println("PASSOU");
            return true;
        } else {
            System.out.println("FALHOU");
            return false;
        }
    }

    private static boolean testarContagemVeiculos() {
        System.out.print("[TESTE 6] Contagem de veículos totais vs. estacionados... ");
        Garagem g = new Garagem();
        g.adicionarVeiculo(new Carro("C1", "Ford", "Ka", 2019, TipoCombustivel.ETANOL, 4));
        g.adicionarVeiculo(new Moto("M1", "Yamaha", "NMAX", 2022, TipoCombustivel.GASOLINA, 160));
        g.adicionarVeiculo(new Caminhao("T1", "Mercedes", "Actros", 2020, TipoCombustivel.DIESEL, 6));

        g.registrarEntrada("C1");
        g.registrarEntrada("T1");

        boolean ok = (g.contarTotalVeiculos() == 3) && (g.contarVeiculosEstacionados() == 2);
        if (ok) {
            System.out.println("PASSOU (Total: 3, Estacionados: 2)");
            return true;
        } else {
            System.out.println("FALHOU");
            return false;
        }
    }

    private static boolean testarDesempenhoGrandeVolume() {
        System.out.print("[TESTE 7] Desempenho em grande volume (10.000 operações em O(1))... ");
        Garagem g = new Garagem();
        long inicio = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            g.adicionarVeiculo(new Carro("PLK" + i, "Marca" + i, "Modelo" + i, 2020, TipoCombustivel.GASOLINA, 4));
        }

        g.registrarEntrada("PLK5000");
        g.registrarEntrada("PLK9999");

        Optional<Veiculo> vMeio = g.buscarPorPlaca("plk5000");
        Optional<Veiculo> vFim = g.buscarPorPlaca("plk9999");
        int total = g.contarTotalVeiculos();
        int estacionados = g.contarVeiculosEstacionados();

        long duracaoMs = System.currentTimeMillis() - inicio;

        boolean ok = vMeio.isPresent()
                  && vFim.isPresent()
                  && total == 10000
                  && estacionados == 2
                  && duracaoMs < 500;

        if (ok) {
            System.out.printf("PASSOU (10.000 veículos cadastrados/buscados em %d ms)\n", duracaoMs);
            return true;
        } else {
            System.out.printf("FALHOU (Duração: %d ms)\n", duracaoMs);
            return false;
        }
    }
}

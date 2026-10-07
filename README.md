# Sistema de Gerenciamento de Garagem em Java (Console & POO)

Projeto simples, organizado e funcional de gerenciamento de garagem em Java, desenvolvido demonstrando os pilares de **Programação Orientada a Objetos (POO)**: Abstração, Encapsulamento, Herança e Polimorfismo.

---

## 🏛️ Estrutura e Conceitos de POO

- **Abstração (`Veiculo`)**: Classe abstrata contendo os atributos essenciais de qualquer veículo (`placa`, `marca`, `modelo`, `ano`, `tipoCombustivel`, `horarioEntrada`) e a assinatura do método abstrato `calcularTarifa(long horas)`.
- **Encapsulamento**: Todos os atributos são privados (`private`), acessados e modificados via métodos getters e setters com validação de dados.
- **Herança**:
  - `Carro extends Veiculo`
  - `Moto extends Veiculo`
  - `Caminhao extends Veiculo`
- **Polimorfismo**: Cada classe filha sobrescreve o método `calcularTarifa(long horas)` com a sua regra de cobrança:
  - **Carro**: R$ 10,00 por hora (tarifa padrão)
  - **Moto**: R$ 5,00 por hora (tarifa reduzida)
  - **Caminhão**: R$ 20,00 por hora (tarifa maior para veículos pesados)
- **Enum (`TipoCombustivel`)**: `GASOLINA`, `ETANOL`, `DIESEL`, `ELETRICO`.
- **Gerenciador de Vagas (`Garagem`)**: Encapsula as regras de negócio de entrada, saída, listagem, busca por placa, contagem e emissão de recibos (`ReciboSaida`).

---

## 📋 Funcionalidades Implementadas

1. **Cadastrar um veículo** (Carro, Moto ou Caminhão) com validação de placa única.
2. **Listar os veículos cadastrados** (com indicação se está dentro ou fora da garagem).
3. **Buscar um veículo pela placa** (busca insensível a maiúsculas/minúsculas).
4. **Registrar entrada de um veículo** (horário atual ou simulação retroativa de horas para testes).
5. **Registrar saída de um veículo** (calcula o tempo decorrido, valor da tarifa polimórfica e emite recibo).
6. **Remover um veículo** (com proteção que impede a remoção de veículos que estejam estacionados no momento).
7. **Exibir a quantidade de veículos na garagem** (total cadastrados, atualmente estacionados, fora da garagem e receita total acumulada).
8. **Encerrar o programa**.

---

## 🚀 Como Compilar e Executar

### 1. Compilar o projeto
No terminal (PowerShell ou Bash), execute:
```powershell
javac -encoding UTF-8 -d bin src/garagem/model/*.java src/garagem/service/*.java src/garagem/*.java
```

### 2. Executar o sistema interativo
```powershell
java -cp bin garagem.Main
```

### 3. Executar os testes automatizados
```powershell
java -cp bin garagem.TesteVirtual
```

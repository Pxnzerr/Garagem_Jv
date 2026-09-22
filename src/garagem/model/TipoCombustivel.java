package garagem.model;

/**
 * Enum representando os tipos de combustíveis aceitos pelos veículos.
 */
public enum TipoCombustivel {
    GASOLINA("Gasolina"),
    ETANOL("Etanol"),
    DIESEL("Diesel"),
    ELETRICO("Elétrico");

    private final String descricao;

    TipoCombustivel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}

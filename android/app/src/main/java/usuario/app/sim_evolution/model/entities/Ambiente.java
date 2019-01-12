package usuario.app.sim_evolution.model.entities;

/**
 * Habitat das aves, cenário onde ocorrem os eventos
 * Atributos:
 * - tipoAmbiente: o tipo de cenário entre três possíveis:
 * -> 1: Floresta
 * -> 2: Savana
 * -> 3: Genérico (uso em simulação customizada)
 * - corAmbiente: cor predominante do ambiente, entre três possíveis:
 * -- correlacionadas com as cores das aves
 * -> 1: Verde (Floresta)
 * -> 3: Amarelo (Savana)
 * -- uso em simulação customizada
 * -> 2: Genérica
 */
public class Ambiente {

    private int tipoAmbiente;
    private int corAmbiente;

    public Ambiente(int tipoAmbiente, int corAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
        this.corAmbiente = corAmbiente;
    }

    public int getTipoAmbiente() {
        return tipoAmbiente;
    }

    public void setTipoAmbiente(int tipoAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
    }

    public int getCorAmbiente() {
        return corAmbiente;
    }

    public void setCorAmbiente(int corAmbiente) {
        this.corAmbiente = corAmbiente;
    }
}

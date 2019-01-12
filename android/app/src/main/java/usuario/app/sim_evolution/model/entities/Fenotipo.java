package usuario.app.sim_evolution.model.entities;

/**
 * Fenotipo: superclasse para os fenotipos de cada ave
 * Atributos:
 * - tipoFenotipo: identifica o fenotipo (cor = 1, bico = 2)
 * - variacaoFenotipo: identifica as variações dos tipos de fenótipo
 * -> cor: 1 (verde), 2 (mesclado), 3 (amarelo)
 * -> bico: 1 (grande), 2 (fino), 3 (alicate)
 * -- array genotipo: registra os genes do genótipo, transliterado para números inteiros
 * -> "A" = 4, "a" = 3, "B" = 2, "b" = 1
 *
 * CORES                |   BICOS
 * Verde: 44    (AA)    |   Grande: 22  (BB)
 * Mesclado: 43 (Aa)    |   Médio: 21   (Bb)
 * Amarelo: 33  (aa)    |   Pequeno: 11 (bb)
 *
 * - chanceAdaptabilidade: define o valor de probabilidade de adaptação de um determinado fenótipo em um ambiente
 * -> Se for COR: informa as chances de a ave ser predada. Assim, quanto MENOR o valor, MAIOR será a probabilidade de sobrevivência
 * -> Se for formato de bico: informa as chances de alimentação da ave. Aves que se alimentam MELHOR se reproduzem MAIS.
 *    Desse modo, quanto MENOR o valor, MENOR será a probabilidade de reprodução.
 */

public abstract class Fenotipo {

    private int tipoFenotipo;
    private int variacaoFenotipo;
    private int[] genotipo;
    private double valorAptidao;

    public int getTipo() {
        return tipoFenotipo;
    }

    public void setTipo(int tipo) {
        this.tipoFenotipo = tipo;
    }

    public int getVariacao() {
        return variacaoFenotipo;
    }

    public void setVariacao(int variacao) {
        this.variacaoFenotipo = variacao;
    }

    public int[] getGenotipo() {
        return genotipo;
    }

    public void setGenotipo(int[] genotipo) {
        this.genotipo = genotipo;
    }

    public double getValorAptidao() {
        return valorAptidao;
    }

    public void setValorAptidao(double valorAptidao) {
        this.valorAptidao = valorAptidao;
    }
}

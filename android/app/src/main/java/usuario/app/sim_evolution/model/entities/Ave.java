package usuario.app.sim_evolution.model.entities;

/**
 * Atributos:
 * - variacaoEspecie: variação de espécie da ave, decorrente de sua combinação de genótipos. Há 9 combinações possíveis (1 - 9)
 * -> 1 (A): Verde/Bico Grande (AABB)
 * -> 2 (B): Verde/Bico Médio (AABb)
 * -> 3 (C): Verde/Bico Pequeno (AAbb)
 * -> 4 (D): Mesclado/Bico Grande (AaBB)
 * -> 5 (E): Mesclado/Bico Médio (AaBb)
 * -> 6 (F): Mesclado/Bico Pequeno (Aabb)
 * -> 7 (G): Amarelo/Bico Grande (aaBB)
 * -> 8 (H): Amarelo/Bico Médio (aaBb)
 * -> 9 (I): Amarelo/Bico Pequeno (aabb)
 * - cor e bico: fenótipos da ave
 * - nivelEnergia: armazena o nível de energia da ave. Inicia com 100.
 * - nivelCamuflagem: Capacidade de se camuflar no ambiente, o que definiria níveis de exposição aos predadores.
 *                    Tem a ver com as chances de a ave se reproduzir no ambiente.
 * -> 3 = cor igual à do ambiente
 * -> 2 = cor parecida com a do ambiente
 * -> 1 = cor diferente da do ambiente
 * - tempoEventoNascimento: momento em que a ave foi criada
 * - tempoEventoMorte: momento em que a ave foi predada (nivelEnergia <= 0)
 */
public class Ave {

    private int id_ave;
    private int variacaoEspecie;
    private Cor cor;
    private Bico bico;
    private int tempoEventoNascimento;
    private int tempoEventoMorte;
    private int nivelEnergia;
    //private int nivelCamuflagem;
    private double valorAptidaoCor;
    private double valorAptidaoBico;

    private int imageID;

    public Ave(int variacaoEspecie,
               Cor cor, Bico bico, int nivelEnergia) { //, int nivelCamuflagem
        this.variacaoEspecie = variacaoEspecie;
        this.cor = cor;
        this.bico = bico;
        this.nivelEnergia = nivelEnergia;
       // this.nivelCamuflagem = nivelCamuflagem;
    }

    public Ave(int id_ave, int variacaoEspecie,
               Cor cor, Bico bico, int nivelEnergia,
               int tempoEventoNascimento, int tempoEventoMorte,
               double valorAptidaoCor, double valorAptidaoBico) { //, int nivelCamuflagem
        this.id_ave = id_ave;
        this.variacaoEspecie = variacaoEspecie;
        this.cor = cor;
        this.bico = bico;
        this.nivelEnergia = nivelEnergia;
        //this.nivelCamuflagem = nivelCamuflagem;
        this.tempoEventoNascimento = tempoEventoNascimento;
        this.tempoEventoMorte = tempoEventoMorte;
        this.valorAptidaoCor = valorAptidaoCor;
        this.valorAptidaoBico = valorAptidaoBico;
    }

    public int getId_ave() {
        return id_ave;
    }

    public int getTempoEventoNascimento() {
        return tempoEventoNascimento;
    }

    public void setTempoEventoNascimento(int tempoEventoNascimento) {
        this.tempoEventoNascimento = tempoEventoNascimento;
    }

    public int getTempoEventoMorte() {
        return tempoEventoMorte;
    }

    public void setTempoEventoMorte(int tempoEventoMorte) {
        this.tempoEventoMorte = tempoEventoMorte;
    }

    public void setId_ave(int id_ave) {
        this.id_ave = id_ave;
    }

    public int getVariacaoEspecie() {
        return variacaoEspecie;
    }

    public void setVariacaoEspecie(int variacaoEspecie) {
        this.variacaoEspecie = variacaoEspecie;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Bico getBico() {
        return bico;
    }

    public void setBico(Bico bico) {
        this.bico = bico;
    }

    public int getNivelEnergia() {
        return nivelEnergia;
    }

    public void setNivelEnergia(int nivelEnergia) {
        this.nivelEnergia = nivelEnergia;
    }

    //public int getNivelCamuflagem() {
    //    return nivelCamuflagem;
    //}

    //public void setNivelCamuflagem(int nivelCamuflagem) {
    //    this.nivelCamuflagem = nivelCamuflagem;
    //}

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public double getValorAptidaoCor() {
        return valorAptidaoCor;
    }

    public void setValorAptidaoCor(double valorAptidaoCor) {
        this.valorAptidaoCor = valorAptidaoCor;
    }

    public double getValorAptidaoBico() {
        return valorAptidaoBico;
    }

    public void setValorAptidaoBico(double valorAptidaoBico) {
        this.valorAptidaoBico = valorAptidaoBico;
    }

}

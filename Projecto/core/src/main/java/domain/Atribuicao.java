package domain;

import java.util.List;
import java.util.Objects;

public class Atribuicao {

    private Data dataInicio;
    private Data dataFim;
    private Data dataAtribuicao;
    private Classificacao classificacao;
    //private String nrUnico;
    //private int counter = 0;


    /**
     * Instantiates a new rating.
     * @param classificacao
     */
    public Atribuicao(Classificacao classificacao, Data dataInicio) {
        setClassificacao(classificacao);
        setDataAtribuicao(Data.dataAtual());
        setDataInicio(dataInicio);
        setDataFim(new Data (dataInicio.getAno(), dataInicio.getMes(), dataInicio.getDia()+classificacao.getCandidatura().getNrDias()));
        //++counter;
        //nrUnico = getClassificacao().getCandidatura().getDataCandidatura().getAno()+"-"+ counter;
    }

    public Data getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Data dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Data getDataAtribuicao() {
        return dataAtribuicao;
    }

    public void setDataAtribuicao(Data dataAtribuicao) {
        this.dataAtribuicao = dataAtribuicao;
    }

    public Data getDataFim() {
        return dataFim;
    }

    public void setDataFim(Data dataFim) {
        this.dataFim = dataFim;
    }

    public Classificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Classificacao classificacao) {
        if (classificacao != null)
            this.classificacao = classificacao;
        else
            throw new IllegalArgumentException("Classificacao Inválida");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atribuicao)) return false;
        Atribuicao that = (Atribuicao) o;
        return getDataInicio().equals(that.getDataInicio()) && getDataFim().equals(that.getDataFim()) &&
                getClassificacao().equals(that.getClassificacao()) && getDataAtribuicao().equals(that.dataAtribuicao);
    }

    @Override
    public String toString() {
        return "Atribuicao{" +
                "data de atribuicao= " + dataAtribuicao +
                "data de inicio=" + dataInicio +
                ", data fim=" + dataFim +
                ", tarefa=" + classificacao.getCandidatura().getAnuncio().getTarefa().getDesignacao() +
                '}';
    }
}

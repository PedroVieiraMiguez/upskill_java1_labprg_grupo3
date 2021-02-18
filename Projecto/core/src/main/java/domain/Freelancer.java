package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Current class represents the tool for creating new collaborators from an organization. This ones may have different
 * roles within their organization (which are listed in the Funcao enum class). Each of them is also described by their name,
 * mobile phone and email.
 */

public class Freelancer implements Serializable {

    private String nome;
    private Telefone telefone;
    private Email email;
    private NIF nif;
    private List<ReconhecimentoCT> reconhecimento;

    /**
     * Instantiates a new Freelancer.
     *
     * @param nome     as name
     * @param telefone as telephone
     * @param email    as email
     * @param nif      as tax identification nr
     */
    public Freelancer(String nome, Telefone telefone, Email email, NIF nif, List<ReconhecimentoCT> reconhecimento) {
        setNome(nome);
        setTelefone(telefone);
        setEmail(email);
        setNif(nif);
        this.reconhecimento = new ArrayList<>(reconhecimento);
    }

    private void setNome(String nome) {
        if (nome.length() < 2 || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do freelancer inválido");
        }
        this.nome = nome;
    }

    private void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    private void setEmail(Email email) {
        this.email = email;
    }

    private void setNif(NIF nif) {
        this.nif = nif;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getNome() {
        return nome;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public Email getEmail() {
        return email;
    }

    public Telefone getTelefone() {
        return this.telefone;
    }

    public NIF getNif() { return this.nif; }

    public ArrayList<ReconhecimentoCT> getReconhecimento() {
        return new ArrayList<>(reconhecimento);
    }

    private void setReconhecimento(List<ReconhecimentoCT> reconhecimento) {
        this.reconhecimento = reconhecimento;
    }

    @Override
    public String toString() {
        return String.format("Nome: %s%nTelefone: %s%nE-mail: %s%nNIF: %d",
                this.nome, this.telefone, this.email, this.nif);
    }

    public void adicionaReconhecimentoCT(ArrayList<ReconhecimentoCT> reconhecimentoCTS){
        for (ReconhecimentoCT reconhecimentoCT: reconhecimentoCTS) {
            if (!this.reconhecimento.contains(reconhecimentoCT)){
                this.reconhecimento.add(reconhecimentoCT);
            }
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Freelancer)) return false;
        Freelancer that = (Freelancer) o;
        return getNome().equals(that.getNome()) && getTelefone().equals(that.getTelefone()) &&
                getEmail().equals(that.getEmail()) && getNif().equals(that.getNif()) &&
                getReconhecimento().equals(that.getReconhecimento());
    }

}






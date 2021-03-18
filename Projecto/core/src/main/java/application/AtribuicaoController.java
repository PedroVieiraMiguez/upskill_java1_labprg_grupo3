/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.*;
import exceptions.FetchingProblemException;
import persistence.RepositorioCandidatura;
import persistence.RepositorioColaborador;
import persistence.RepositorioAtribuicao;
import persistence.RepositorioProcessoSeriacao;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is the one responsible to connect the GUI with the methods 
 * responsible for the process of assignment of the task to the freelancer.
 *
 * @author Grupo 3
 */
public class AtribuicaoController {
    
    /**
     * Boolean method which checks if the assignment process was created with success.
     * 
     * @param classificacao
     * @param dataInicio
     * @return boolean
     * @throws SQLException 
     */
    public boolean criarAtribuicao (Classificacao classificacao,
                                            LocalDate dataInicio)
                                                        throws SQLException {

        Atribuicao atribuicao = RepositorioAtribuicao.getInstance().criarAtribuicao(classificacao,
                new Data(dataInicio.getYear(), dataInicio.getMonth().getValue(),
                        dataInicio.getDayOfMonth()));
        
        return RepositorioAtribuicao.getInstance().insertAtribuicao(atribuicao);
        
    }

    
    /**
     * Gets a list of serialization processes by manager.
     * 
     * @param emailGestor
     * @return RepositorioProcessoSeriacao
     * @throws FetchingProblemException 
     */
    public ArrayList<ProcessoSeriacao> getProcessosSeriacaoByGestor(String emailGestor) throws FetchingProblemException {
        return RepositorioProcessoSeriacao.getInstance().getProcessosSeriacaoByGestor(emailGestor);
    }
    
}

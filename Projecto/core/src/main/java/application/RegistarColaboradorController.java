/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.Colaborador;
import persistence.RepositorioColaborador;

/**
 *
 * @author Grupo 3
 */
public class RegistarColaboradorController {

    /*public boolean Colaborador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    public boolean Colaborador(String nomeColaborador, String funcaoColaborador, String contactoColaborador, String emailColaborador) {
        
        Colaborador colaborador = new Colaborador(nomeColaborador, funcaoColaborador, contactoColaborador, emailColaborador);
        
        if (!Plataforma.getInstance().getRepoColab().addColaborador(colaborador)) {
            return false;
        } else return Plataforma.getInstance().getRepoOrg().registarColaboradorComoUtilizador(colaborador);
        
    }
    
}

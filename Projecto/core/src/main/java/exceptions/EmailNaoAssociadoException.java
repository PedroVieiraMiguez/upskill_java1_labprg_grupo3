/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * This class will be called if the email is not associated with anything.
 *
 * @author Grupo 3 - Turma 1
 */
public class EmailNaoAssociadoException extends IllegalArgumentException {
    public EmailNaoAssociadoException(String s) {
        super(s);
    }
}

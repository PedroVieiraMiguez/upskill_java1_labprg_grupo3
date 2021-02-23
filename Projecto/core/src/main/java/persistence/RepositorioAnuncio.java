package persistence;

import domain.*;
import exceptions.CodigoNaoAssociadoException;
import exceptions.FetchingProblemException;
import jdk.nashorn.internal.codegen.CompilerConstants;
import network.ConnectionHandler;
import oracle.jdbc.proxy.annotation.Pre;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class responsible for creating a repository to store information about
 * advertisements.
 * 
 * @author Grupo 3
 */
public class RepositorioAnuncio {

    private static RepositorioAnuncio instance;


    
    /**
     * Static method that returns a unique reference to the class object, which
     * implements a singleton.
     * @return instance
     */
    public static RepositorioAnuncio getInstance() {
        if (instance == null) {
            instance = new RepositorioAnuncio();
        }
        return instance;
    }

    private RepositorioAnuncio() {

    }

    /**
     * Boolean method that checks if a advertisement exists in the repository,
     * otherwise it is added to it.
     * @param anuncio
     * @return boolean
     * @throws SQLException 
     */
    public boolean insertAnuncio(Anuncio anuncio) throws SQLException {
        Connection conn = Plataforma.getInstance().getConnectionHandler().getConnection();

        try {

            CallableStatement cs1 = conn.prepareCall("SELECT idTarefa FROM Tarefa WHERE referenciaTarefa = ?");
            cs1.setString(1, anuncio.getTarefa().getCodigoUnico().toString());
            ResultSet rSetidTarefa = cs1.executeQuery();
            rSetidTarefa.next();

            int idTarefa = rSetidTarefa.getInt(1);

            CallableStatement cs2 = conn.prepareCall("SELECT idTipoRegimento FROM TipoRegimento where designacao = ?");
            cs2.setString(1, anuncio.getTipoRegimento().getDesignacao());
            ResultSet rSetidTipoRegimento = cs2.executeQuery();
            rSetidTipoRegimento.next();

            int idTipoRegimento = rSetidTipoRegimento.getInt(1);

            CallableStatement cs3 = conn.prepareCall("{CALL createAnuncio(?, ?, ?, ?, ?, ?, ?, ?)}");
            ResultSet rs = null;


            conn.setAutoCommit(false);

            cs3.setInt(1, idTarefa);
            cs3.setInt(2, idTipoRegimento);
            cs3.setDate(3, anuncio.getDataInicioPublicitacao().getDataSQL());
            cs3.setDate(4, anuncio.getDataFimPublicitacao().getDataSQL());
            cs3.setDate(5, anuncio.getDataInicioCandidatura().getDataSQL());
            cs3.setDate(6, anuncio.getDataFimCandidatura().getDataSQL());
            cs3.setDate(7, anuncio.getDataInicioSeriacao().getDataSQL());
            cs3.setDate(8, anuncio.getDataFimSeriacao().getDataSQL());


            cs3.executeQuery();

            conn.commit();
            cs1.close();
            cs2.close();
            cs3.close();

            return true;
        } catch (SQLException e) {
            e.getSQLState();
            e.printStackTrace();
            try {
                System.err.print("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException excep) {
                excep.getErrorCode();
            }
        }

        return false;
    }


    /**
     * Method to obtain a type of regiment from the list 'TipoRegimento'.
     * @return tiposRegimento
     * @throws SQLException 
     */
    public ArrayList<TipoRegimento> getTiposRegimento() throws SQLException {
        ArrayList<TipoRegimento> tiposRegimento = new ArrayList<>();

        Connection conn = Plataforma.getInstance().getConnectionHandler().getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM TipoRegimento");
        ResultSet rSetTipoRegimento = pstmt.executeQuery();
        while (rSetTipoRegimento.next()) {
            String designacao = rSetTipoRegimento.getString("designacao");
            String regras = rSetTipoRegimento.getString("descricaoRegras");

            tiposRegimento.add(new TipoRegimento(designacao, regras));
        }

        pstmt.close();
        return tiposRegimento;
    }

    /**
     * Method to obtain all the advertisements from the list 'Anuncio'.
     * @return montarListaAnuncios(rSetAnuncios)
     */
    public ArrayList<Anuncio> getAllAnuncios () {
        try {
            Connection conn = Plataforma.getInstance().getConnectionHandler().getConnection();

            PreparedStatement pstmtAnuncios = conn.prepareStatement("SELECT * FROM Anuncio");
            ResultSet rSetAnuncios = pstmtAnuncios.executeQuery();

            return montarListaAnuncios(rSetAnuncios);

        } catch (SQLException e) {
            e.getSQLState();
            e.printStackTrace();
            e.getErrorCode();
            throw new FetchingProblemException("Problemas ao montar a lista de anuncios");
        }
    }


    /**
     * Method to obtain a advertisement by a task.
     * @param tarefa
     * @return montarAnuncio(pstmt.executeQuery())
     */
    public Anuncio getAnuncioByTarefa(Tarefa tarefa) {

        try {
            Connection conn = Plataforma.getInstance().getConnectionHandler().getConnection();
            String refTarefa = tarefa.getCodigoUnico().toString();

            int nif = Integer.parseInt(tarefa.getOrganizacao().getNIF().toString());

            CallableStatement cs1 = conn.prepareCall("SELECT idOrganizacao FROM Organizacao WHERE NIF = ?");
            cs1.setInt(1, nif);
            ResultSet rSetIdOrg = cs1.executeQuery();
            rSetIdOrg.next();

            int idOrganizacao = rSetIdOrg.getInt("idOrganizacao");

            CallableStatement cs2 = conn.prepareCall("{? = call getAnunciobyRefTarefa_IdOrg(?, ?)}");
            cs2.registerOutParameter(1, Types.INTEGER);
            cs2.setString(2, refTarefa);
            cs2.setInt(3, idOrganizacao);
            cs2.executeUpdate();

            int idAnuncio = cs2.getInt(1);

            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Anuncio where idAnuncio = ?");
            pstmt.setInt(1, idAnuncio);


            return montarAnuncio(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new CodigoNaoAssociadoException("Não existe nenhum anuncio associado a esta tarefa.");
        }
    }




    public Anuncio montarAnuncio(ResultSet row) throws SQLException {
        Connection conn = Plataforma.getInstance().getConnectionHandler().getConnection();

        Anuncio anuncio = null;


        try {
            if (row.getRow() < 1) {
                row.next();
            }

            //montar tarefa
            PreparedStatement pstmt = conn.prepareStatement("SELECT idTarefa FROM Anuncio WHERE idAnuncio = ?");
            pstmt.setInt(1, row.getInt("idAnuncio"));
            ResultSet rSetTarefa = pstmt.executeQuery();
            rSetTarefa.next();
            int idTarefa = rSetTarefa.getInt("idTarefa");

            PreparedStatement pstmt2 = conn.prepareStatement("SELECT * FROM Tarefa WHERE idTarefa = ?");
            pstmt2.setInt(1, idTarefa);

            Tarefa tarefa = RepositorioTarefa.getInstance().montarTarefa(pstmt2.executeQuery());

            //montar tipoRegimento

            PreparedStatement pstmt3 = conn.prepareStatement("SELECT idTipoRegimento FROM Anuncio WHERE idAnuncio = ?");
            pstmt3.setInt(1, row.getInt("idAnuncio"));
            ResultSet rSetTipoRegimento = pstmt3.executeQuery();
            rSetTipoRegimento.next();
            int idTipoRegimento = rSetTipoRegimento.getInt("idTipoRegimento");

            PreparedStatement pstmt4 = conn.prepareStatement("SELECT * FROM TipoRegimento WHERE idTipoRegimento = ?");
            pstmt4.setInt(1, idTipoRegimento);
            ResultSet rSetTipoRegimento2 = pstmt4.executeQuery();
            rSetTipoRegimento2.next();

            String designacao = rSetTipoRegimento2.getString("designacao");
            String regras = rSetTipoRegimento2.getString("descricaoRegras");

            TipoRegimento tipoRegimento = new TipoRegimento(designacao, regras);

            //Datas Anúncio

            Date datasql = row.getDate("dataInicioPublicitacao");
            String[] dataString = datasql.toString().split("-");
            Data dataInicioPub = new Data(Integer.parseInt(dataString[0]), Integer.parseInt(dataString[1])
                    , Integer.parseInt(dataString[2]));
            Date datasql2 = row.getDate("dataFimPublicitacao");
            String[] dataString2 = datasql2.toString().split("-");
            Data dataFimPub = new Data(Integer.parseInt(dataString2[0]), Integer.parseInt(dataString2[1])
                    , Integer.parseInt(dataString2[2]));
            Date datasql3 = row.getDate("dataInicioCandidatura");
            String[] dataString3 = datasql3.toString().split("-");
            Data dataInicioCand = new Data(Integer.parseInt(dataString3[0]), Integer.parseInt(dataString3[1])
                    , Integer.parseInt(dataString3[2]));
            Date datasql4 = row.getDate("dataFimCandidatura");
            String[] dataString4 = datasql4.toString().split("-");
            Data dataFimCand = new Data(Integer.parseInt(dataString4[0]), Integer.parseInt(dataString4[1])
                    , Integer.parseInt(dataString4[2]));
            Date datasql5 = row.getDate("dataInicioSeriacao");
            String[] dataString5 = datasql5.toString().split("-");
            Data dataInicioSer = new Data(Integer.parseInt(dataString5[0]), Integer.parseInt(dataString5[1])
                    , Integer.parseInt(dataString5[2]));
            Date datasql6 = row.getDate("dataFimSeriacao");
            String[] dataString6 = datasql6.toString().split("-");
            Data dataFimSer = new Data(Integer.parseInt(dataString6[0]), Integer.parseInt(dataString6[1])
                    , Integer.parseInt(dataString6[2]));

            anuncio = new Anuncio(tarefa, tipoRegimento, dataInicioPub, dataFimPub, dataInicioCand, dataFimCand, dataInicioSer, dataFimSer);
        } catch (SQLException e) {
            e.getSQLState();
            e.printStackTrace();
        }
        if (anuncio != null) {
            return anuncio;
        } else {
            throw new FetchingProblemException("Problema ao montar anúncio");
        }

    }



    /**
     * Method that creates a list of advertisements, adding an advertisement to it. 
     * @param rows
     * @return listaAnuncios
     * @throws SQLException 
     */
    public ArrayList<Anuncio> montarListaAnuncios(ResultSet rows) throws SQLException {
        ArrayList<Anuncio> listaAnuncios = new ArrayList<>();

        try {
            while (rows.next()) {
                Anuncio anuncio = montarAnuncio(rows);
                listaAnuncios.add(anuncio);
            }

        } catch (SQLException e) {
            e.getSQLState();
            e.printStackTrace();
        }
        return listaAnuncios;
    }


    /**
     * Method that creates an advertisement.
     * 
     * @param tarefa
     * @param tipoRegimento
     * @param dataInicioPub
     * @param dataFimPub
     * @param dataInicioCand
     * @param dataFimCand
     * @param dataInicioSer
     * @param dataFimSer
     * @return Anuncio
     */
    public Anuncio criarAnuncio(Tarefa tarefa, TipoRegimento tipoRegimento, Data dataInicioPub,
                                LocalDate dataFimPub, LocalDate dataInicioCand, LocalDate dataFimCand,
                                LocalDate dataInicioSer, LocalDate dataFimSer) {

        return new Anuncio(tarefa, tipoRegimento, dataInicioPub, new Data(dataFimPub.getYear(), dataFimPub.getMonth().getValue(),
                dataFimPub.getDayOfMonth()), new Data(dataInicioCand.getYear(), dataInicioCand.getMonth().getValue(),
                dataInicioCand.getDayOfMonth()), new Data(dataFimCand.getYear(), dataFimCand.getMonth().getValue(),
                dataFimCand.getDayOfMonth()), new Data(dataInicioSer.getYear(), dataInicioSer.getMonth().getValue(),
                dataInicioSer.getDayOfMonth()), new Data(dataFimSer.getYear(), dataFimSer.getMonth().getValue(),
                dataFimSer.getDayOfMonth()));
    }

}

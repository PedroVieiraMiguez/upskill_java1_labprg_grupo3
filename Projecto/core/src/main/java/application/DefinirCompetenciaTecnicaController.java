package application;

import domain.AreaAtividade;
import domain.CompetenciaTecnica;
import domain.GrauProficiencia;
import domain.Plataforma;
import persistence.RepositorioCompetenciaTecnica;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Current class is the one responsible to connect the GUI with the methods responsible for setting new
 * technical competences.
 */
public class DefinirCompetenciaTecnicaController {

    /**
     * Setting new technical competence boolean.
     *
     * @param codigoUnico   as unique code
     * @param areaAtividade as activity area
     * @param descricao     as description
     * @param descDetalhada as detailed description
     * @return the boolean
     */
    public boolean definirCompetenciaTecnica(String codigoUnico, AreaAtividade areaAtividade,
                                             String descricao, String descDetalhada, List<GrauProficiencia> graus) throws SQLException {

        Plataforma plataforma = Plataforma.getInstance();
        RepositorioCompetenciaTecnica repo = RepositorioCompetenciaTecnica.getInstance();

        CompetenciaTecnica competenciaTecnica = repo.criarCompetenciaTecnica(codigoUnico, areaAtividade, descricao, descDetalhada, graus);

        return repo.createCompetenciaTecnica(competenciaTecnica);
    }

}
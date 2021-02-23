package ui;

import application.AuthenticationController;
import application.DefinirTarefaController;
import application.ServiceController;
import application.RegistarColaboradorController;
import domain.CategoriaTarefa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AreaGestorUI implements Initializable {

    //Registar Colaborador elements
    public Button btnRegistarColaborador;
    public Button btnLimparRegistarColaborador;

    public TextField txtNomeColaborador;
    public TextField txtContactoColaborador;
    public TextField txtEmailColaborador;
    public TextField txtFuncaoColaborador;


    //Criar Tarefa Elements
    public Button btnRegistarTarefa;
    public Button btnLimparTarefa;

    public TextField txtCodigoUnicoTarefa;
    public TextField txtCustoTarefa;
    public TextArea txtDescInfTarefa;
    public TextArea txtDescTecnicaTarefa;
    public TextField txtDuracaoTarefa;
    public ComboBox<CategoriaTarefa> comboCategoria;
    public TextField txtNomeTarefa;


    //General Elements
    public Button btnSelecionarRegistarColaborador;
    public Button btnSelecionarCriarTarefa;
    public Button btnLogout;

    public BorderPane criarTarefaPane;
    public BorderPane registarColaboradorPane;

    private RegistarColaboradorController registarColaboradorController;
    private AuthenticationController authController;
    private ServiceController serviceController;
    private DefinirTarefaController tarefaController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registarColaboradorController = new RegistarColaboradorController();
        authController = new AuthenticationController();
        serviceController = new ServiceController();
        try {
            comboCategoria.getItems().setAll(serviceController.getCategoriasTarefa());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tarefaController = new DefinirTarefaController();
    }

    //Registar Colaborador
    @FXML
    void registarColaboradorAction(ActionEvent event) {
       try {
            boolean registou = registarColaboradorController.registarColaborador(txtNomeColaborador.getText().trim(),
                    Integer.parseInt(txtContactoColaborador.getText().trim()),
                    txtEmailColaborador.getText().trim(), txtFuncaoColaborador.getText().trim(), authController.getEmail());

            AlertaUI.criarAlerta(Alert.AlertType.INFORMATION, MainApp.TITULO_APLICACAO, "Registar novo colaborador.",
                    registou ? "Colaborador criado com sucesso! \n\n" +
                            serviceController.getColaboradorToStringCompletoByEmail(txtEmailColaborador.getText().trim())
                            : "Não foi possível registar o colaborador.").show();

            if (registou) {
                limparDadosRegistarColaboradorPane();
            }

        } catch (NumberFormatException nfe) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO, "Erro nos dados.",
                    "Letras em campos de valores numéricos (NIF, Contacto Gestor ou Telefone Organização) ou campos em vazio.").show();
        } catch (IllegalArgumentException iae) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO, "Erro nos dados.",
                    iae.getMessage()).show();
        } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
    }

    //Criar Tarefa
    public void criarTarefaActionTarefa(ActionEvent actionEvent) {
        try {
            boolean criou = tarefaController.definirTarefa(
                    txtCodigoUnicoTarefa.getText().trim(),
                    txtNomeTarefa.getText().trim(),
                    txtDescInfTarefa.getText().trim(),
                    txtDescTecnicaTarefa.getText().trim(),
                    Integer.parseInt(txtDuracaoTarefa.getText().trim()),
                    Float.parseFloat(txtCustoTarefa.getText().trim()),
                    comboCategoria.getValue(),
                    authController.getEmail());

            AlertaUI.criarAlerta(Alert.AlertType.INFORMATION, MainApp.TITULO_APLICACAO, "Criar nova tarefa.",
                    criou ? "Tarefa criada com sucesso! \n\n" +
                            serviceController.getTarefaToStringCompletoByCodigoUnico(txtCodigoUnicoTarefa.getText().trim(),
                                    authController.getEmail())
                            : "Não foi possível criar a tarefa.").show();

            if (criou) {
                limparDadosRegistarTarefaPane();
            }

        } catch (NumberFormatException nfe) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO, "Erro nos dados.",
                    "Letras em campos de valores numéricos (NIF, Contacto Gestor ou Telefone Organização) ou campos em vazio.").show();
        } catch (IllegalArgumentException iae) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO, "Erro nos dados.",
                    iae.getMessage()).show();
        } catch (SQLException throwables) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro de SQL.",
                    throwables.getMessage()).show();
            throwables.printStackTrace();
        }
    }

    //limpar campos do registo colaborador
    public void limparRegistarColaboradorAction(ActionEvent actionEvent) {
        limparDadosRegistarColaboradorPane();
    }

    public void limparDadosRegistarColaboradorPane() {
        txtNomeColaborador.clear();
        txtContactoColaborador.clear();
        txtEmailColaborador.clear();
    }

    //Limpar campos do registo tarefa
    public void limparActionTarefa(ActionEvent actionEvent) {
        limparDadosRegistarTarefaPane();
    }

    public void limparDadosRegistarTarefaPane() {
        txtCodigoUnicoTarefa.clear();
        txtCustoTarefa.clear();
        txtDescInfTarefa.clear();
        txtDescTecnicaTarefa.clear();
        txtDuracaoTarefa.clear();
        txtNomeTarefa.clear();
    }

    //selecionar menu registo colaborador
    public void btnSelecionarRegistarColaboradorAction(ActionEvent actionEvent) {
        registarColaboradorPane.setVisible(true);
        registarColaboradorPane.setDisable(false);
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);

    }

    //selecionar menu criação de tarefa
    public void btnSelecionarCriarTarefaAction(ActionEvent actionEvent) {
        registarColaboradorPane.setVisible(false);
        registarColaboradorPane.setDisable(true);
        criarTarefaPane.setVisible(true);
        criarTarefaPane.setDisable(false);

        //popular elementos
        try {
            comboCategoria.getItems().setAll(serviceController.getCategoriasTarefa());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //fazer logout
    public void btnLogoutAction(ActionEvent actionEvent) {
        Alert alerta = AlertaUI.criarAlerta(Alert.AlertType.CONFIRMATION, "Logout",
                "Irá voltar à pagina inicial após confirmação.", "Deseja mesmo fazer logout?");
        if (alerta.showAndWait().get() == ButtonType.CANCEL) {
            actionEvent.consume();
        } else {
            try {
                limparTodosOsCampos();
                authController.logout();
                serviceController.resetUserAPI();
                voltarJanelaInicial();
            } catch (SQLException e) {
                AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                        "Erro de SQL.",
                        e.getMessage()).show();
            }

        }
    }

    //limpar todos os campos
    public void limparTodosOsCampos() {
        txtNomeColaborador.clear();
        txtContactoColaborador.clear();
        txtEmailColaborador.clear();

        txtCodigoUnicoTarefa.clear();
        txtCustoTarefa.clear();
        txtDescInfTarefa.clear();
        txtDescTecnicaTarefa.clear();
        txtDuracaoTarefa.clear();
        txtNomeTarefa.clear();
    }

    //volta à janela inicial
    public void voltarJanelaInicial() {
        MainApp.screenController.activate("JanelaInicial");
    }


}

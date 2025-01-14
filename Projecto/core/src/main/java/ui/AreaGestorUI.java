package ui;

import application.*;
import domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class responsible for the UI of the manager area.
 * 
 * @author Grupo 3
 */
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
    public BorderPane homePane;
    
    //Serialization process:
    public BorderPane seriacaoManualPane;
    public ListView<Candidatura> listViewCandidaturasPorSelecionarSeriacaoManual;
    public ListView<Colaborador> listViewColaboradoresPorSelecionarSeriacaoManual;
    public ListView<Candidatura> listViewCandidaturasSelecionadasSeriacaoManual;
    public ListView<Colaborador> listViewColaboradoresSelecionadosSeriacaoManual;
    public Button btnRemoverUltimaCandidatura;
    public Button btnRemoverUltimoColaborador;
    public BorderPane IniciarSeriacaoPane;
    public ListView<Anuncio> listViewAnunciosSeriarAnuncio;
    public BorderPane seriacaoAutomaticaPane;
    public ListView<Candidatura> listViewCandidaturasSeriarAnuncioSeriacaoAutomatica;
    public BorderPane publicarTarefaPane;
    public Button btnPublicarTarefa;
    public Button btnLimparDadosPublicarTarefa;
    public ListView<Tarefa> listViewTarefasMatchedPublicarTarefa;
    public DatePicker btnDataFimPub;
    public DatePicker btnDataInicioCand;
    public DatePicker btnDataFimCand;
    public DatePicker btnDataInicioSeriacao;
    public DatePicker btnDataFimSeriacao;
    public ComboBox<TipoRegimento> btnTipoRegimento;
    
    //Tasks assignments...
    public BorderPane iniciarAtribuicaoPane;
    public ListView<Classificacao> listViewManualOpcional;
    public DatePicker datePickerDataInicioManualOpcional;
    public Button btnVoltarAtribuicaoManualOpcional;
    public Button btnConfirmarManualOpcional;
    public BorderPane paneManualOpcional;
    public Button btnAtribuirTarefaObrigatorio;
    public Button btnVoltarObrigatorioAtribuicao;
    public ListView<Classificacao> listViewAutomaticoObrigatorio;
    public DatePicker datePickerDataInicioObrigatorio;
    public BorderPane paneAutomaticoObrigatorio;
    public ListView<ProcessoSeriacao> listViewProcessoSeriacaoAtribuicao;
    public Button btnVoltarAtribuicao;
    public Button btnAtribuirTarefa;

    //Controllers
    private RegistarColaboradorController registarColaboradorController;
    private AuthenticationController authController;
    private ServiceController serviceController;
    private DefinirTarefaController tarefaController;
    private PublicarTarefaController publicarTarefaController;
    private SeriarCandidaturaController seriarCandidaturaController;
    private AtribuicaoController atribuicaoController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registarColaboradorController = new RegistarColaboradorController();
        authController = new AuthenticationController();
        serviceController = new ServiceController();
        tarefaController = new DefinirTarefaController();
        publicarTarefaController = new PublicarTarefaController();
        seriarCandidaturaController = new SeriarCandidaturaController();
        atribuicaoController = new AtribuicaoController();


        //popular combo boxes do painel Publicar Tarefa
        try {
            btnTipoRegimento.getItems().setAll(serviceController.getTiposRegimento());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
                            : "Não foi possível registar o colaborador.").showAndWait();

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
                            : "Não foi possível criar a tarefa.").showAndWait();

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
        txtFuncaoColaborador.clear();
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
        //desligar
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);
        homePane.setVisible(false);
        homePane.setDisable(true);
        publicarTarefaPane.setDisable(true);
        publicarTarefaPane.setVisible(false);
        IniciarSeriacaoPane.setVisible(false);
        IniciarSeriacaoPane.setDisable(true);
        seriacaoAutomaticaPane.setVisible(false);
        seriacaoAutomaticaPane.setDisable(true);
        seriacaoManualPane.setVisible(false);
        seriacaoManualPane.setDisable(true);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);
        iniciarAtribuicaoPane.setDisable(true);
        iniciarAtribuicaoPane.setVisible(false);

        //ligar
        registarColaboradorPane.setVisible(true);
        registarColaboradorPane.setDisable(false);

    }

    //selecionar menu criação de tarefa
    public void btnSelecionarCriarTarefaAction(ActionEvent actionEvent) {
        //desligar
        registarColaboradorPane.setVisible(false);
        registarColaboradorPane.setDisable(true);
        homePane.setVisible(false);
        homePane.setDisable(true);
        IniciarSeriacaoPane.setVisible(false);
        IniciarSeriacaoPane.setDisable(true);
        seriacaoAutomaticaPane.setVisible(false);
        seriacaoAutomaticaPane.setDisable(true);
        seriacaoManualPane.setVisible(false);
        seriacaoManualPane.setDisable(true);
        publicarTarefaPane.setDisable(true);
        publicarTarefaPane.setVisible(false);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);
        iniciarAtribuicaoPane.setDisable(true);
        iniciarAtribuicaoPane.setVisible(false);

        //ligarr
        criarTarefaPane.setVisible(true);
        criarTarefaPane.setDisable(false);

        //popular elementos
        try {
            comboCategoria.getItems().setAll(serviceController.getCategoriasTarefa());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //selecionar menu Home
    public void goHomeSelectAction(ActionEvent actionEvent) {
        //desligar
        registarColaboradorPane.setVisible(false);
        registarColaboradorPane.setDisable(true);
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);
        IniciarSeriacaoPane.setVisible(false);
        IniciarSeriacaoPane.setDisable(true);
        seriacaoAutomaticaPane.setVisible(false);
        seriacaoAutomaticaPane.setDisable(true);
        seriacaoManualPane.setVisible(false);
        seriacaoManualPane.setDisable(true);
        publicarTarefaPane.setDisable(true);
        publicarTarefaPane.setVisible(false);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);
        iniciarAtribuicaoPane.setDisable(true);
        iniciarAtribuicaoPane.setVisible(false);

        //ligar
        homePane.setVisible(true);
        homePane.setDisable(false);
    }

    //Publicar tarefa
    public void btnPublicarTarefaSelectAction(ActionEvent actionEvent) {
        //desligar
        homePane.setVisible(false);
        homePane.setDisable(true);
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);
        seriacaoAutomaticaPane.setDisable(true);
        seriacaoAutomaticaPane.setVisible(false);
        seriacaoManualPane.setDisable(true);
        seriacaoManualPane.setVisible(false);
        IniciarSeriacaoPane.setDisable(true);
        IniciarSeriacaoPane.setVisible(false);
        registarColaboradorPane.setDisable(true);
        registarColaboradorPane.setVisible(false);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);
        iniciarAtribuicaoPane.setDisable(true);
        iniciarAtribuicaoPane.setVisible(false);

        //ligar
        publicarTarefaPane.setVisible(true);
        publicarTarefaPane.setDisable(false);


        //popular elementos
        try {
            listViewTarefasMatchedPublicarTarefa.getItems().setAll(serviceController.getTarefasOrganizacao(authController.getEmail()));
        } catch (Exception e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Problema preencher lista de tarefas.",
                    e.getMessage()).show();
        }
    }

    //Seriar anúncio
    public void btnSeriarAnuncioSelectAction(ActionEvent actionEvent) {
        //desligar
        homePane.setVisible(false);
        homePane.setDisable(true);
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);
        publicarTarefaPane.setVisible(false);
        publicarTarefaPane.setDisable(true);
        seriacaoAutomaticaPane.setDisable(true);
        seriacaoAutomaticaPane.setVisible(false);
        seriacaoManualPane.setDisable(true);
        seriacaoManualPane.setVisible(false);
        registarColaboradorPane.setDisable(true);
        registarColaboradorPane.setVisible(false);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);
        iniciarAtribuicaoPane.setDisable(true);
        iniciarAtribuicaoPane.setVisible(false);

        //ligar
        IniciarSeriacaoPane.setDisable(false);
        IniciarSeriacaoPane.setVisible(true);
        try {
            listViewAnunciosSeriarAnuncio.getItems().setAll(serviceController.getAllAnunciosSeriacao(authController.getEmail()));
        } catch (SQLException e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Problema preencher lista de tarefas.",
                    e.getMessage()).show();
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
        comboCategoria.setValue(null);
        btnDataFimPub.getEditor().clear();
        btnDataFimCand.getEditor().clear();
        btnDataFimSeriacao.getEditor().clear();
        btnDataInicioCand.getEditor().clear();
        btnDataInicioSeriacao.getEditor().clear();
        btnTipoRegimento.setValue(null);
    }

    //volta à janela inicial
    public void voltarJanelaInicial() {
        MainApp.screenController.activate("JanelaInicial");
    }

    //Conclui o processo de seriação manual.
    public void finalizarSeriacaoManualAction(ActionEvent actionEvent) {
        try {
            boolean criou = seriarCandidaturaController.criarProcessoSeriacao(
                    listViewCandidaturasSelecionadasSeriacaoManual.getItems(),
                    listViewColaboradoresSelecionadosSeriacaoManual.getItems(),
                    authController.getEmail());

            AlertaUI.criarAlerta(Alert.AlertType.INFORMATION, MainApp.TITULO_APLICACAO, "Processo de seriação.",
                    criou ? "Processo de seriação realizado com sucesso!" :
                            "Não foi possível realizar o processo de seriação").showAndWait();

            if (criou) {
                listViewCandidaturasSelecionadasSeriacaoManual.getItems().clear();
                listViewCandidaturasPorSelecionarSeriacaoManual.getItems().clear();
                listViewColaboradoresPorSelecionarSeriacaoManual.getItems().clear();
                listViewColaboradoresSelecionadosSeriacaoManual.getItems().clear();
                seriacaoManualPane.setDisable(true);
                seriacaoManualPane.setVisible(false);
                IniciarSeriacaoPane.setVisible(true);
                IniciarSeriacaoPane.setDisable(false);
                try {
                    listViewAnunciosSeriarAnuncio.getItems().setAll(serviceController.getAllAnunciosSeriacao(authController.getEmail()));
                } catch (SQLException e) {
                    AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                            "Problema preencher lista de tarefas.",
                            e.getMessage()).show();
                }

            }

        } catch (IllegalArgumentException e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    e.getMessage()).show();
        } catch (SQLException throwables) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro de SQL.",
                    throwables.getMessage()).show();
            throwables.printStackTrace();
        }
    }

    public void voltarSeriacaoManualAction(ActionEvent actionEvent) {
        Alert alerta = AlertaUI.criarAlerta(Alert.AlertType.CONFIRMATION, "Logout",
                "Irá voltar ao menu de seriação.", "Deseja mesmo fazer voltar?");
        if (alerta.showAndWait().get() == ButtonType.CANCEL) {
            actionEvent.consume();
        } else {
            try {
                listViewCandidaturasSelecionadasSeriacaoManual.getItems().clear();
                listViewCandidaturasPorSelecionarSeriacaoManual.getItems().clear();
                listViewColaboradoresPorSelecionarSeriacaoManual.getItems().clear();
                listViewColaboradoresSelecionadosSeriacaoManual.getItems().clear();
                seriacaoManualPane.setDisable(true);
                seriacaoManualPane.setVisible(false);
                IniciarSeriacaoPane.setVisible(true);
                IniciarSeriacaoPane.setDisable(false);
            } catch (Exception e) {
                AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                        "Problema ao voltar ao menu anterior.",
                        e.getMessage()).show();
            }
        }
    }

    //Classifica candidatura(s) para seriação manual.
    public void classificarCandidaturaSeriacaoManualAction(ActionEvent actionEvent) {
        if (listViewCandidaturasPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem() != null) {
            if (!listViewCandidaturasSelecionadasSeriacaoManual.getItems().contains(
                    listViewCandidaturasPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem())) {
                listViewCandidaturasSelecionadasSeriacaoManual.getItems().add(
                        listViewCandidaturasPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem());
                if (btnRemoverUltimaCandidatura.isDisable()) {
                    btnRemoverUltimaCandidatura.setDisable(false);
                }
                listViewCandidaturasPorSelecionarSeriacaoManual.getItems().remove(
                        listViewCandidaturasPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem());
            }
        } else {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO, "Erro ao adicionar candidatura",
                    "É obrigatório escolher uma candidatura para adicionar!").show();
        }
    }

    public void removerUltimaCandidaturaSeriacaoManualAction(ActionEvent actionEvent) {
        listViewCandidaturasPorSelecionarSeriacaoManual.getItems().add(
                listViewCandidaturasSelecionadasSeriacaoManual.getItems().get(listViewCandidaturasSelecionadasSeriacaoManual.getItems().size() - 1));

        listViewCandidaturasSelecionadasSeriacaoManual.getItems().remove(listViewCandidaturasSelecionadasSeriacaoManual.getItems().size() - 1);

        if (listViewCandidaturasSelecionadasSeriacaoManual.getItems().size() == 0) {
            btnRemoverUltimaCandidatura.setDisable(true);
        }
    }

    public void adicionarColaboradorSeriacaoManualAction(ActionEvent actionEvent) {
        if (listViewColaboradoresPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem() != null) {
            if (!listViewColaboradoresSelecionadosSeriacaoManual.getItems().contains(
                    listViewColaboradoresPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem())) {

                listViewColaboradoresSelecionadosSeriacaoManual.getItems().add(
                        listViewColaboradoresPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem());
                if (btnRemoverUltimoColaborador.isDisable()) {
                    btnRemoverUltimoColaborador.setDisable(false);
                }
                listViewColaboradoresPorSelecionarSeriacaoManual.getItems().remove(
                        listViewColaboradoresPorSelecionarSeriacaoManual.getSelectionModel().getSelectedItem());
            }
        } else {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO, "Erro ao adicionar candidatura",
                    "É obrigatório escolher uma candidatura para adicionar!").show();
        }
    }

    public void removerUltimoColaboradorSeriacaoManualAction(ActionEvent actionEvent) {
        listViewColaboradoresPorSelecionarSeriacaoManual.getItems().add(
                listViewColaboradoresSelecionadosSeriacaoManual.getItems().get(listViewColaboradoresSelecionadosSeriacaoManual.getItems().size() - 1));
        listViewColaboradoresSelecionadosSeriacaoManual.getItems().remove(listViewColaboradoresSelecionadosSeriacaoManual.getItems().size() - 1);

        if (listViewColaboradoresSelecionadosSeriacaoManual.getItems().size() == 0) {
            btnRemoverUltimoColaborador.setDisable(true);
        }
    }

    //Inicia o processo de seriação.
    public void iniciarSeriacaoAction(ActionEvent actionEvent) {
        //desligar
        homePane.setVisible(false);
        homePane.setDisable(true);
        IniciarSeriacaoPane.setDisable(true);
        IniciarSeriacaoPane.setVisible(false);
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);
        publicarTarefaPane.setVisible(false);
        publicarTarefaPane.setDisable(true);
        registarColaboradorPane.setDisable(true);
        registarColaboradorPane.setVisible(false);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);
        iniciarAtribuicaoPane.setDisable(true);
        iniciarAtribuicaoPane.setVisible(false);
        btnRemoverUltimaCandidatura.setDisable(true);
        btnRemoverUltimoColaborador.setDisable(true);
        listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems().clear();
        listViewColaboradoresPorSelecionarSeriacaoManual.getItems().clear();

        try {
            if (seriarCandidaturaController.isSeriacaoAutomatica(listViewAnunciosSeriarAnuncio.getSelectionModel().getSelectedItem())) {
                //ligar
                seriacaoAutomaticaPane.setDisable(false);
                seriacaoAutomaticaPane.setVisible(true);
                seriacaoManualPane.setDisable(true);
                seriacaoManualPane.setVisible(false);
                listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems().setAll(seriarCandidaturaController.
                        candidaturasSeriadasPorValor(listViewAnunciosSeriarAnuncio.getSelectionModel().getSelectedItem()));
            } else {
                //ligar
                seriacaoManualPane.setDisable(false);
                seriacaoManualPane.setVisible(true);
                seriacaoAutomaticaPane.setDisable(true);
                seriacaoAutomaticaPane.setVisible(false);
                listViewCandidaturasPorSelecionarSeriacaoManual.getItems().setAll(seriarCandidaturaController.
                        getAllCandidaturasPorSelecionar(listViewAnunciosSeriarAnuncio.getSelectionModel().getSelectedItem()));
                listViewColaboradoresPorSelecionarSeriacaoManual.getItems().setAll(seriarCandidaturaController.
                        getAllColaboradoresOrganizacao(authController.getEmail()));
            }

        } catch (Exception e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Problema ao iniciar seriação.",
                    e.getMessage()).show();
        }
    }

    //Confirma o processo de seriação automática.
    public void confirmarSeriacaoAutomaticaAction(ActionEvent actionEvent) {
        try {

            boolean seriou = seriarCandidaturaController.criarProcessoSeriacao(listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems(),
                    new ArrayList<Colaborador>(),
                    authController.getEmail());

            AlertaUI.criarAlerta(Alert.AlertType.INFORMATION, MainApp.TITULO_APLICACAO, "Seriação de Candidaturas.",
                    seriou ? "Seriação Automática realizada com sucesso! \n\n"
                            : "Não foi possível seriar automáticamente as candidaturas.").showAndWait();
            if (seriou) {
                listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems().clear();
                seriacaoAutomaticaPane.setDisable(true);
                seriacaoAutomaticaPane.setVisible(false);
                IniciarSeriacaoPane.setVisible(true);
                IniciarSeriacaoPane.setDisable(false);
                try {
                    listViewAnunciosSeriarAnuncio.getItems().setAll(serviceController.getAllAnunciosSeriacao(authController.getEmail()));
                } catch (SQLException e) {
                    AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                            "Problema preencher lista de tarefas.",
                            e.getMessage()).show();
                }
            }

        } catch (IllegalArgumentException e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    e.getMessage()).show();
        } catch (Exception e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    "Datas inválidas ou campos em falta");
        }

    }

    public void voltarSeriacaoAutomaticaAction(ActionEvent actionEvent) {
        Alert alerta = AlertaUI.criarAlerta(Alert.AlertType.CONFIRMATION, "Logout",
                "Irá voltar ao menu de seriação.", "Deseja mesmo fazer voltar?");
        if (alerta.showAndWait().get() == ButtonType.CANCEL) {
            actionEvent.consume();
        } else {
            try {
                listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems().clear();
                seriacaoAutomaticaPane.setDisable(true);
                seriacaoAutomaticaPane.setVisible(false);
                IniciarSeriacaoPane.setVisible(true);
                IniciarSeriacaoPane.setDisable(false);
            } catch (Exception e) {
                AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                        "Problema ao voltar ao menu anterior.",
                        e.getMessage()).show();
            }
        }
    }

    //Publica a tarefa.
    public void publicarTarefaAction(ActionEvent actionEvent) {
        try {

            boolean publicou = publicarTarefaController.publicarTarefa(
                    listViewTarefasMatchedPublicarTarefa.getSelectionModel().getSelectedItem(),
                    btnTipoRegimento.getValue(),
                    Data.dataAtual(),
                    btnDataFimPub.getValue(),
                    btnDataInicioCand.getValue(),
                    btnDataFimCand.getValue(),
                    btnDataInicioSeriacao.getValue(),
                    btnDataFimSeriacao.getValue(),
                    authController.getEmail());

            AlertaUI.criarAlerta(Alert.AlertType.INFORMATION, MainApp.TITULO_APLICACAO, "Criar novo Anúncio.",
                    publicou ? "Anúncio criado com sucesso! \n\n" +
                            serviceController.getAnunciotoStringCompletoByTarefa(listViewTarefasMatchedPublicarTarefa.
                                    getSelectionModel().getSelectedItem())
                            : "Não foi possível criar o anúncio.").showAndWait();

            if (publicou) {
                limparTodosOsCampos();
                try {
                    listViewTarefasMatchedPublicarTarefa.getItems().setAll(serviceController.getTarefasOrganizacao(authController.getEmail()));
                } catch (Exception e) {
                    AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                            "Problema preencher lista de tarefas.",
                            e.getMessage()).show();
                }
            }

        } catch (IllegalArgumentException e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    e.getMessage()).show();
        } catch (Exception e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    "Datas inválidas ou campos em falta");
        }
    }


    public void btnLimparDadosPublicarTarefaAction(ActionEvent actionEvent) {
        limparTodosOsCampos();
    }
    
    
    //Inicia o processo de atribuição.
    public void btnIniciarAtribuicaoSelectAction(ActionEvent actionEvent) {
        
        //desligar
        homePane.setVisible(false);
        homePane.setDisable(true);
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);
        publicarTarefaPane.setVisible(false);
        publicarTarefaPane.setDisable(true);
        seriacaoAutomaticaPane.setDisable(true);
        seriacaoAutomaticaPane.setVisible(false);
        seriacaoManualPane.setDisable(true);
        seriacaoManualPane.setVisible(false);
        registarColaboradorPane.setDisable(true);
        registarColaboradorPane.setVisible(false);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);
        iniciarAtribuicaoPane.setDisable(true);
        iniciarAtribuicaoPane.setVisible(false);
        IniciarSeriacaoPane.setVisible(false);
        IniciarSeriacaoPane.setDisable(true);

        //ligar
        iniciarAtribuicaoPane.setDisable(false);
        iniciarAtribuicaoPane.setVisible(true);
        
        try {
            listViewProcessoSeriacaoAtribuicao.getItems().setAll(atribuicaoController.getProcessosSeriacaoByGestor(authController.getEmail()));
        } catch (SQLException e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Problema preencher lista de processos de seriação.",
                    e.getMessage()).show();
        }

    }

    //Confirma o processo de atribuição manual/opcional.
    public void ConfirmarManualOpcionalAction(ActionEvent actionEvent) {
        
        try {

            boolean manop = atribuicaoController.criarAtribuicao(listViewManualOpcional.getSelectionModel().getSelectedItem(),
                                                                 datePickerDataInicioManualOpcional.getValue());

            AlertaUI.criarAlerta(Alert.AlertType.INFORMATION, MainApp.TITULO_APLICACAO, 
                    "Atribuição de Candidaturas.",
                    manop ? "Atribuição Manual/Opcional realizada com sucesso! \n\n"
                            : "Não foi possível fazer a atribuição.").showAndWait();
            
            if (manop) {
                listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems().clear();
                paneManualOpcional.setDisable(true);
                paneManualOpcional.setVisible(false);
                iniciarAtribuicaoPane.setVisible(true);
                iniciarAtribuicaoPane.setDisable(false);
                
                try {
                    listViewProcessoSeriacaoAtribuicao.getItems().setAll(atribuicaoController.getProcessosSeriacaoByGestor(authController.getEmail()));
                } catch (SQLException e) {
                    AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                            "Problema preencher lista de tarefas.",
                            e.getMessage()).show();
                }
            }

        } catch (IllegalArgumentException e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    e.getMessage()).show();    
        } catch (Exception e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    "Datas inválidas ou campos em falta");
        }
        
    }

    //volta para o menu da atribuição
    public void btnVoltarManualOpcionalAction(ActionEvent actionEvent) {
        
        Alert alerta = AlertaUI.criarAlerta(Alert.AlertType.CONFIRMATION, "Logout",
                "Irá voltar ao menu de atribuição.", "Deseja mesmo fazer voltar?");
        if (alerta.showAndWait().get() == ButtonType.CANCEL) {
            actionEvent.consume();
        } else {
            try {
                listViewProcessoSeriacaoAtribuicao.getItems().clear();
                paneManualOpcional.setDisable(true);
                paneManualOpcional.setVisible(false);
                iniciarAtribuicaoPane.setVisible(true);
                iniciarAtribuicaoPane.setDisable(false);
                listViewProcessoSeriacaoAtribuicao.getItems().setAll(atribuicaoController.getProcessosSeriacaoByGestor(authController.getEmail()));
            } catch (Exception e) {
                AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                        "Problema ao voltar ao menu anterior.",
                        e.getMessage()).showAndWait();
            }
        }
        
    }

    //Confirma o processo de atribuição obrigatória (automática).
    public void atribuirTarefaObrigatorioAction(ActionEvent actionEvent) {
        
        try {

            boolean obrig = atribuicaoController.criarAtribuicao(listViewAutomaticoObrigatorio.getItems().get(0),
                                                                 datePickerDataInicioObrigatorio.getValue());

            AlertaUI.criarAlerta(Alert.AlertType.INFORMATION, MainApp.TITULO_APLICACAO, 
                    "Atribuição de Tarefas.",
                    obrig ? "Atribuição Obrigatória realizada com sucesso! \n\n"
                            : "Não foi possível fazer a atribuição.").showAndWait();
            
            if (obrig) {
                listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems().clear();
                paneAutomaticoObrigatorio.setDisable(true);
                paneAutomaticoObrigatorio.setVisible(false);
                iniciarAtribuicaoPane.setVisible(true);
                iniciarAtribuicaoPane.setDisable(false);
                
                try {
                    listViewProcessoSeriacaoAtribuicao.getItems().setAll(atribuicaoController.getProcessosSeriacaoByGestor(authController.getEmail()));
                } catch (SQLException e) {
                    AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                            "Problema preencher lista de tarefas.",
                            e.getMessage()).showAndWait();
                }
            }

        } catch (IllegalArgumentException e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    e.getMessage()).showAndWait();
        } catch (Exception e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Erro nos dados.",
                    "Datas inválidas ou campos em falta").showAndWait();
        }
        
    }

    //volta para o menu da atribuição
    public void btnVoltarObrigatorioAction(ActionEvent actionEvent) {
        
        Alert alerta = AlertaUI.criarAlerta(Alert.AlertType.CONFIRMATION, "Logout",
                "Irá voltar ao menu de atribuição.", "Deseja mesmo voltar?");
        if (alerta.showAndWait().get() == ButtonType.CANCEL) {
            actionEvent.consume();
        } else {
            try {
                listViewProcessoSeriacaoAtribuicao.getItems().clear();
                paneAutomaticoObrigatorio.setDisable(true);
                paneAutomaticoObrigatorio.setVisible(false);
                iniciarAtribuicaoPane.setVisible(true);
                iniciarAtribuicaoPane.setDisable(false);
                listViewProcessoSeriacaoAtribuicao.getItems().setAll(atribuicaoController.getProcessosSeriacaoByGestor(authController.getEmail()));

            } catch (Exception e) {
                AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                        "Problema ao voltar ao menu anterior.",
                        e.getMessage()).show();
            }
        }
        
    }

    //atribuir tarefa
    public void atribuirTarefaAction(ActionEvent actionEvent) {

        //desligar
        homePane.setVisible(false);
        homePane.setDisable(true);
        IniciarSeriacaoPane.setDisable(true);
        IniciarSeriacaoPane.setVisible(false);
        criarTarefaPane.setVisible(false);
        criarTarefaPane.setDisable(true);
        publicarTarefaPane.setVisible(false);
        publicarTarefaPane.setDisable(true);
        registarColaboradorPane.setDisable(true);
        registarColaboradorPane.setVisible(false);
        paneManualOpcional.setVisible(false);
        paneManualOpcional.setDisable(true);
        paneAutomaticoObrigatorio.setDisable(true);
        paneAutomaticoObrigatorio.setVisible(false);

        iniciarAtribuicaoPane.setDisable(false);
        iniciarAtribuicaoPane.setVisible(true);
        btnRemoverUltimaCandidatura.setDisable(true);
        btnRemoverUltimoColaborador.setDisable(true);
        listViewCandidaturasSeriarAnuncioSeriacaoAutomatica.getItems().clear();
        listViewColaboradoresPorSelecionarSeriacaoManual.getItems().clear();

        try {
            if (atribuicaoController.isObrigatorio(listViewProcessoSeriacaoAtribuicao.getSelectionModel().getSelectedItem())){
                //ligar
                paneAutomaticoObrigatorio.setDisable(false);
                paneAutomaticoObrigatorio.setVisible(true);
                paneManualOpcional.setDisable(true);
                paneManualOpcional.setVisible(false);
                iniciarAtribuicaoPane.setDisable(true);
                iniciarAtribuicaoPane.setVisible(false);
                listViewAutomaticoObrigatorio.getItems().setAll(atribuicaoController.listarClassificacoesAtribuicao
                        (listViewProcessoSeriacaoAtribuicao.getSelectionModel().getSelectedItem()));
            } else {
                //ligar
                paneAutomaticoObrigatorio.setDisable(true);
                paneAutomaticoObrigatorio.setVisible(false);
                paneManualOpcional.setDisable(false);
                paneManualOpcional.setVisible(true);
                iniciarAtribuicaoPane.setDisable(true);
                iniciarAtribuicaoPane.setVisible(false);
                listViewManualOpcional.getItems().setAll(atribuicaoController.listarClassificacoesAtribuicao
                        (listViewProcessoSeriacaoAtribuicao.getSelectionModel().getSelectedItem()));
            }

        } catch (Exception e) {
            AlertaUI.criarAlerta(Alert.AlertType.ERROR, MainApp.TITULO_APLICACAO,
                    "Problema ao iniciar seriação.",
                    e.getMessage()).show();
                    e.printStackTrace();
        }
    }

    }



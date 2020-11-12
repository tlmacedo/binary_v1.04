package br.com.tlmacedo.binary.controller;

import br.com.tlmacedo.binary.controller.estrategias.RoboEstrategia;
import br.com.tlmacedo.binary.model.dao.ContaTokenDAO;
import br.com.tlmacedo.binary.model.dao.SymbolDAO;
import br.com.tlmacedo.binary.model.dao.TransacoesDAO;
import br.com.tlmacedo.binary.model.dao.TransactionDAO;
import br.com.tlmacedo.binary.model.enums.ROBOS;
import br.com.tlmacedo.binary.model.vo.*;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Operacoes implements Initializable {

    /**
     * Objetos DAO conecta com banco de dados
     */
    //** Banco de Dados **
    static SymbolDAO symbolDAO = new SymbolDAO();
    static ContaTokenDAO contaTokenDAO = new ContaTokenDAO();
    static TransacoesDAO transacoesDAO = new TransacoesDAO();
    static TransactionDAO transactionDAO = new TransactionDAO();


    /**
     * Identificação de volatilidades
     */
    //** Variaveis de identificacoes das volatilidades
    static final ObservableList<Symbol> symbolObservableList = FXCollections.observableArrayList(getSymbolDAO().getAll(Symbol.class, "ativa=true", null));
    static final Integer VOL_10 = 0;
    static final Integer VOL_25 = 1;
    static final Integer VOL_50 = 2;
    static final Integer VOL_75 = 3;
    static final Integer VOL_100 = 4;
    static final Integer VOL_HZ10 = 5;
    static final Integer VOL_HZ25 = 6;
    static final Integer VOL_HZ50 = 7;
    static final Integer VOL_HZ75 = 8;
    static final Integer VOL_HZ100 = 9;
    static final String[] VOL_NAME = getSymbolObservableList().stream().map(Symbol::getName).collect(Collectors.toList()).toArray(String[]::new);
    static BooleanProperty VOL_1S = new SimpleBooleanProperty(false);

    /**
     * Contas corretora
     */
    static ObjectProperty<ContaToken> contaToken = new SimpleObjectProperty<>();
    static ObjectProperty<Authorize> authorize = new SimpleObjectProperty<>();


    /**
     * Conexão e operação com WebService
     */
    static final ObjectProperty<WSClient> wsClient = new SimpleObjectProperty<>(new WSClient());
    static BooleanProperty wsConectado = new SimpleBooleanProperty(false);

    /**
     * Graficos
     */
    //** informações para graficos **
    static IntegerProperty[] graficoMaiorQtdDigito = new IntegerProperty[getSymbolObservableList().size()];
    static IntegerProperty[] graficoMenorQtdDigito = new IntegerProperty[getSymbolObservableList().size()];
    static IntegerProperty graficoQtdTicksAnalise = new SimpleIntegerProperty(1000);
    static IntegerProperty graficoQtdTicks = new SimpleIntegerProperty(100);

    Text[][] graficoTxtLegendaDigito_R = new Text[getSymbolObservableList().size()][10];

    //** gráficos em barras **
    XYChart.Series<String, Number>[] graficoBarasVolatilidade_R = new XYChart.Series[getSymbolObservableList().size()];
    ObservableList<Data<String, Number>>[] graficoBarrasListDataDigitos_R = new ObservableList[getSymbolObservableList().size()];
    static ObservableList<LongProperty>[] graficoBarrasListQtdDigito_R = new ObservableList[getSymbolObservableList().size()];


    //** graficos em linha **
    XYChart.Series<String, Number>[] graficoLinhasVolatilidade_R = new XYChart.Series[getSymbolObservableList().size()];
    ObservableList<Data<String, Number>>[] graficoLinhasListDataDigitos_R = new ObservableList[getSymbolObservableList().size()];
    static ObservableList<HistoricoDeTicks>[] graficoLinhasListQtdDigito_R = new ObservableList[getSymbolObservableList().size()];

    //** gráficos MACD **
    XYChart.Series<String, Number>[] graficoMACDVolatilidade_R = new XYChart.Series[getSymbolObservableList().size()];
    ObservableList<Data<String, Number>>[] graficoMACDListDataDigitos_R = new ObservableList[getSymbolObservableList().size()];
    static ObservableList<HistoricoDeTicks>[] graficoMACDListQtdDigito_R = new ObservableList[getSymbolObservableList().size()];


    /**
     * Robos
     */
    static ObjectProperty<ROBOS> roboSelecionado = new SimpleObjectProperty<>();
    static ObjectProperty<RoboEstrategia> roboEstrategia = new SimpleObjectProperty<>();


    /**
     * Variaveis de controle do sistema
     */

    Timeline roboRelogio;
    LongProperty roboHoraInicial = new SimpleLongProperty();
    LongProperty roboCronometro = new SimpleLongProperty();
    BooleanProperty roboCronometroAtivado = new SimpleBooleanProperty(false);


    /**
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     */

    /**
     * Objetos do formulario
     */

    // Detalhes e informações da conta
    public AnchorPane painelViewBinary;

    public TitledPane tpn_DetalhesConta;
    public ComboBox<ContaToken> cboConta;
    public Label lblLegendaNExecucoes;
    public Label lblTotalExecucoes;
    public Label lblTotalVitorias;
    public Label lblTotalDerrotas;
    public Label lblTotalLucro;
    public Label lblTotalLucroPorc;
    public Label lblProprietarioConta;
    public Label lblIdConta;
    public Label lblSaldoConta;
    public Label lblMoedaSaldo;
    public Label lblSaldoInicial;
    public Label lblTotalInvestido;
    public Label lblTotalPremiacao;
    public Label lblSaldoFinal;

    // Negociação
    public TitledPane tpn_negociacao;
    public ComboBox<ROBOS> cboRobos;
    public Button btnContratos;
    public Button btnIniciar;
    public Button btnPausar;
    public Button btnStop;
    public ComboBox<SimNao> cboVelocidadeTicksGrafico;
    public ComboBox<Integer> cboQtdTicksGrafico;
    public Label lblRoboHoraInicial;
    public Label lblRoboHoraAtual;
    public Label lblRoboCronometro;


    // Volatilidade R10
    public TitledPane tpn_R10;
    public BarChart<String, Number> grafBar_R10;
    public NumberAxis yAxisBar_R10;
    public BarChart<String, Number> grafBar_HZ10;
    public NumberAxis yAxisBar_HZ10;
    public LineChart grafLine_R10;
    public NumberAxis yAxisLine_R10;
    public LineChart grafLine_HZ10;
    public NumberAxis yAxisLine_HZ10;
    public Label lblInf01_R10;
    public Label lblVlrInf01_R10;
    public Label lblPorcInf01_R10;
    public Label lblInf02_R10;
    public Label lblVlrInf02_R10;
    public Label lblPorcInf02_R10;
    public Label lblTickUltimo_R10;
    public Label lblLegendaTickUltimo_R10;
    public Button btnContratos_R10;
    public Button btnComprar_R10;
    public Button btnPausar_R10;
    public Button btnStop_R10;
    public Label lblInvestido_R10;
    public Label lblInvestidoPorc_R10;
    public Label lblPremiacao_R10;
    public Label lblPremiacaoPorc_R10;
    public Label lblLucro_R10;
    public Label lblLucroPorc_R10;
    public TableView<Transacoes> tbvTransacoes_R10;
    public CheckBox chkAtivo_R10;
    public Label tpnLblLegendaExecucoes_R10;
    public Label tpnLblExecucoes_R10;
    public Label tpnLblVitorias_R10;
    public Label tpnLblDerrotas_R10;
    public Label tpnLblLucro_R10;

    // Volatilidade R25
    public TitledPane tpn_R25;
    public BarChart<String, Number> grafBar_R25;
    public NumberAxis yAxisBar_R25;
    public BarChart<String, Number> grafBar_HZ25;
    public NumberAxis yAxisBar_HZ25;
    public LineChart grafLine_R25;
    public NumberAxis yAxisLine_R25;
    public LineChart grafLine_HZ25;
    public NumberAxis yAxisLine_HZ25;
    public Label lblInf01_R25;
    public Label lblVlrInf01_R25;
    public Label lblPorcInf01_R25;
    public Label lblInf02_R25;
    public Label lblVlrInf02_R25;
    public Label lblPorcInf02_R25;
    public Label lblTickUltimo_R25;
    public Label lblLegendaTickUltimo_R25;
    public Button btnContratos_R25;
    public Button btnComprar_R25;
    public Button btnPausar_R25;
    public Button btnStop_R25;
    public Label lblInvestido_R25;
    public Label lblInvestidoPorc_R25;
    public Label lblPremiacao_R25;
    public Label lblPremiacaoPorc_R25;
    public Label lblLucro_R25;
    public Label lblLucroPorc_R25;
    public TableView<Transacoes> tbvTransacoes_R25;
    public CheckBox chkAtivo_R25;
    public Label tpnLblLegendaExecucoes_R25;
    public Label tpnLblExecucoes_R25;
    public Label tpnLblVitorias_R25;
    public Label tpnLblDerrotas_R25;
    public Label tpnLblLucro_R25;

    // Volatilidade R50
    public TitledPane tpn_R50;
    public BarChart<String, Number> grafBar_R50;
    public NumberAxis yAxisBar_R50;
    public BarChart<String, Number> grafBar_HZ50;
    public NumberAxis yAxisBar_HZ50;
    public LineChart grafLine_R50;
    public NumberAxis yAxisLine_R50;
    public LineChart grafLine_HZ50;
    public NumberAxis yAxisLine_HZ50;
    public Label lblInf01_R50;
    public Label lblVlrInf01_R50;
    public Label lblPorcInf01_R50;
    public Label lblInf02_R50;
    public Label lblVlrInf02_R50;
    public Label lblPorcInf02_R50;
    public Label lblTickUltimo_R50;
    public Label lblLegendaTickUltimo_R50;
    public Button btnContratos_R50;
    public Button btnComprar_R50;
    public Button btnPausar_R50;
    public Button btnStop_R50;
    public Label lblInvestido_R50;
    public Label lblInvestidoPorc_R50;
    public Label lblPremiacao_R50;
    public Label lblPremiacaoPorc_R50;
    public Label lblLucro_R50;
    public Label lblLucroPorc_R50;
    public TableView<Transacoes> tbvTransacoes_R50;
    public CheckBox chkAtivo_R50;
    public Label tpnLblLegendaExecucoes_R50;
    public Label tpnLblExecucoes_R50;
    public Label tpnLblVitorias_R50;
    public Label tpnLblDerrotas_R50;
    public Label tpnLblLucro_R50;

    // Volatilidade R75
    public TitledPane tpn_R75;
    public BarChart<String, Number> grafBar_R75;
    public NumberAxis yAxisBar_R75;
    public BarChart<String, Number> grafBar_HZ75;
    public NumberAxis yAxisBar_HZ75;
    public LineChart grafLine_R75;
    public NumberAxis yAxisLine_R75;
    public LineChart grafLine_HZ75;
    public NumberAxis yAxisLine_HZ75;
    public Label lblInf01_R75;
    public Label lblVlrInf01_R75;
    public Label lblPorcInf01_R75;
    public Label lblInf02_R75;
    public Label lblVlrInf02_R75;
    public Label lblPorcInf02_R75;
    public Label lblTickUltimo_R75;
    public Label lblLegendaTickUltimo_R75;
    public Button btnContratos_R75;
    public Button btnComprar_R75;
    public Button btnPausar_R75;
    public Button btnStop_R75;
    public Label lblInvestido_R75;
    public Label lblInvestidoPorc_R75;
    public Label lblPremiacao_R75;
    public Label lblPremiacaoPorc_R75;
    public Label lblLucro_R75;
    public Label lblLucroPorc_R75;
    public TableView<Transacoes> tbvTransacoes_R75;
    public CheckBox chkAtivo_R75;
    public Label tpnLblLegendaExecucoes_R75;
    public Label tpnLblExecucoes_R75;
    public Label tpnLblVitorias_R75;
    public Label tpnLblDerrotas_R75;
    public Label tpnLblLucro_R75;

    // Volatilidade R100
    public TitledPane tpn_R100;
    public BarChart<String, Number> grafBar_R100;
    public NumberAxis yAxisBar_R100;
    public BarChart<String, Number> grafBar_HZ100;
    public NumberAxis yAxisBar_HZ100;
    public LineChart grafLine_R100;
    public NumberAxis yAxisLine_R100;
    public LineChart grafLine_HZ100;
    public NumberAxis yAxisLine_HZ100;
    public Label lblInf01_R100;
    public Label lblVlrInf01_R100;
    public Label lblPorcInf01_R100;
    public Label lblInf02_R100;
    public Label lblVlrInf02_R100;
    public Label lblPorcInf02_R100;
    public Label lblTickUltimo_R100;
    public Label lblLegendaTickUltimo_R100;
    public Button btnContratos_R100;
    public Button btnComprar_R100;
    public Button btnPausar_R100;
    public Button btnStop_R100;
    public Label lblInvestido_R100;
    public Label lblInvestidoPorc_R100;
    public Label lblPremiacao_R100;
    public Label lblPremiacaoPorc_R100;
    public Label lblLucro_R100;
    public Label lblLucroPorc_R100;
    public TableView<Transacoes> tbvTransacoes_R100;
    public CheckBox chkAtivo_R100;
    public Label tpnLblLegendaExecucoes_R100;
    public Label tpnLblExecucoes_R100;
    public Label tpnLblVitorias_R100;
    public Label tpnLblDerrotas_R100;
    public Label tpnLblLucro_R100;

    /**
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    /**
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     */


    public static SymbolDAO getSymbolDAO() {
        return symbolDAO;
    }

    public static void setSymbolDAO(SymbolDAO symbolDAO) {
        Operacoes.symbolDAO = symbolDAO;
    }

    public static ContaTokenDAO getContaTokenDAO() {
        return contaTokenDAO;
    }

    public static void setContaTokenDAO(ContaTokenDAO contaTokenDAO) {
        Operacoes.contaTokenDAO = contaTokenDAO;
    }

    public static TransacoesDAO getTransacoesDAO() {
        return transacoesDAO;
    }

    public static void setTransacoesDAO(TransacoesDAO transacoesDAO) {
        Operacoes.transacoesDAO = transacoesDAO;
    }

    public static TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }

    public static void setTransactionDAO(TransactionDAO transactionDAO) {
        Operacoes.transactionDAO = transactionDAO;
    }

    public static ObservableList<Symbol> getSymbolObservableList() {
        return symbolObservableList;
    }

    public static Integer getVol10() {
        return VOL_10;
    }

    public static Integer getVol25() {
        return VOL_25;
    }

    public static Integer getVol50() {
        return VOL_50;
    }

    public static Integer getVol75() {
        return VOL_75;
    }

    public static Integer getVol100() {
        return VOL_100;
    }

    public static Integer getVolHz10() {
        return VOL_HZ10;
    }

    public static Integer getVolHz25() {
        return VOL_HZ25;
    }

    public static Integer getVolHz50() {
        return VOL_HZ50;
    }

    public static Integer getVolHz75() {
        return VOL_HZ75;
    }

    public static Integer getVolHz100() {
        return VOL_HZ100;
    }

    public static String[] getVolName() {
        return VOL_NAME;
    }

    public static boolean isVol1s() {
        return VOL_1S.get();
    }

    public static BooleanProperty VOL_1SProperty() {
        return VOL_1S;
    }

    public static void setVol1s(boolean vol1s) {
        VOL_1S.set(vol1s);
    }

    public static ContaToken getContaToken() {
        return contaToken.get();
    }

    public static ObjectProperty<ContaToken> contaTokenProperty() {
        return contaToken;
    }

    public static void setContaToken(ContaToken contaToken) {
        Operacoes.contaToken.set(contaToken);
    }

    public static Authorize getAuthorize() {
        return authorize.get();
    }

    public static ObjectProperty<Authorize> authorizeProperty() {
        return authorize;
    }

    public static void setAuthorize(Authorize authorize) {
        Operacoes.authorize.set(authorize);
    }

    public static WSClient getWsClient() {
        return wsClient.get();
    }

    public static ObjectProperty<WSClient> wsClientProperty() {
        return wsClient;
    }

    public static void setWsClient(WSClient wsClient) {
        Operacoes.wsClient.set(wsClient);
    }

    public static boolean isWsConectado() {
        return wsConectado.get();
    }

    public static BooleanProperty wsConectadoProperty() {
        return wsConectado;
    }

    public static void setWsConectado(boolean wsConectado) {
        Operacoes.wsConectado.set(wsConectado);
    }

    public static IntegerProperty[] getGraficoMaiorQtdDigito() {
        return graficoMaiorQtdDigito;
    }

    public static void setGraficoMaiorQtdDigito(IntegerProperty[] graficoMaiorQtdDigito) {
        Operacoes.graficoMaiorQtdDigito = graficoMaiorQtdDigito;
    }

    public static IntegerProperty[] getGraficoMenorQtdDigito() {
        return graficoMenorQtdDigito;
    }

    public static void setGraficoMenorQtdDigito(IntegerProperty[] graficoMenorQtdDigito) {
        Operacoes.graficoMenorQtdDigito = graficoMenorQtdDigito;
    }

    public static int getGraficoQtdTicksAnalise() {
        return graficoQtdTicksAnalise.get();
    }

    public static IntegerProperty graficoQtdTicksAnaliseProperty() {
        return graficoQtdTicksAnalise;
    }

    public static void setGraficoQtdTicksAnalise(int graficoQtdTicksAnalise) {
        Operacoes.graficoQtdTicksAnalise.set(graficoQtdTicksAnalise);
    }

    public static int getGraficoQtdTicks() {
        return graficoQtdTicks.get();
    }

    public static IntegerProperty graficoQtdTicksProperty() {
        return graficoQtdTicks;
    }

    public static void setGraficoQtdTicks(int graficoQtdTicks) {
        Operacoes.graficoQtdTicks.set(graficoQtdTicks);
    }

    public Text[][] getGraficoTxtLegendaDigito_R() {
        return graficoTxtLegendaDigito_R;
    }

    public void setGraficoTxtLegendaDigito_R(Text[][] graficoTxtLegendaDigito_R) {
        this.graficoTxtLegendaDigito_R = graficoTxtLegendaDigito_R;
    }

    public XYChart.Series<String, Number>[] getGraficoBarasVolatilidade_R() {
        return graficoBarasVolatilidade_R;
    }

    public void setGraficoBarasVolatilidade_R(XYChart.Series<String, Number>[] graficoBarasVolatilidade_R) {
        this.graficoBarasVolatilidade_R = graficoBarasVolatilidade_R;
    }

    public ObservableList<Data<String, Number>>[] getGraficoBarrasListDataDigitos_R() {
        return graficoBarrasListDataDigitos_R;
    }

    public void setGraficoBarrasListDataDigitos_R(ObservableList<Data<String, Number>>[] graficoBarrasListDataDigitos_R) {
        this.graficoBarrasListDataDigitos_R = graficoBarrasListDataDigitos_R;
    }

    public static ObservableList<LongProperty>[] getGraficoBarrasListQtdDigito_R() {
        return graficoBarrasListQtdDigito_R;
    }

    public static void setGraficoBarrasListQtdDigito_R(ObservableList<LongProperty>[] graficoBarrasListQtdDigito_R) {
        Operacoes.graficoBarrasListQtdDigito_R = graficoBarrasListQtdDigito_R;
    }

    public XYChart.Series<String, Number>[] getGraficoLinhasVolatilidade_R() {
        return graficoLinhasVolatilidade_R;
    }

    public void setGraficoLinhasVolatilidade_R(XYChart.Series<String, Number>[] graficoLinhasVolatilidade_R) {
        this.graficoLinhasVolatilidade_R = graficoLinhasVolatilidade_R;
    }

    public ObservableList<Data<String, Number>>[] getGraficoLinhasListDataDigitos_R() {
        return graficoLinhasListDataDigitos_R;
    }

    public void setGraficoLinhasListDataDigitos_R(ObservableList<Data<String, Number>>[] graficoLinhasListDataDigitos_R) {
        this.graficoLinhasListDataDigitos_R = graficoLinhasListDataDigitos_R;
    }

    public static ObservableList<HistoricoDeTicks>[] getGraficoLinhasListQtdDigito_R() {
        return graficoLinhasListQtdDigito_R;
    }

    public static void setGraficoLinhasListQtdDigito_R(ObservableList<HistoricoDeTicks>[] graficoLinhasListQtdDigito_R) {
        Operacoes.graficoLinhasListQtdDigito_R = graficoLinhasListQtdDigito_R;
    }

    public XYChart.Series<String, Number>[] getGraficoMACDVolatilidade_R() {
        return graficoMACDVolatilidade_R;
    }

    public void setGraficoMACDVolatilidade_R(XYChart.Series<String, Number>[] graficoMACDVolatilidade_R) {
        this.graficoMACDVolatilidade_R = graficoMACDVolatilidade_R;
    }

    public ObservableList<Data<String, Number>>[] getGraficoMACDListDataDigitos_R() {
        return graficoMACDListDataDigitos_R;
    }

    public void setGraficoMACDListDataDigitos_R(ObservableList<Data<String, Number>>[] graficoMACDListDataDigitos_R) {
        this.graficoMACDListDataDigitos_R = graficoMACDListDataDigitos_R;
    }

    public static ObservableList<HistoricoDeTicks>[] getGraficoMACDListQtdDigito_R() {
        return graficoMACDListQtdDigito_R;
    }

    public static void setGraficoMACDListQtdDigito_R(ObservableList<HistoricoDeTicks>[] graficoMACDListQtdDigito_R) {
        Operacoes.graficoMACDListQtdDigito_R = graficoMACDListQtdDigito_R;
    }

    public static ROBOS getRoboSelecionado() {
        return roboSelecionado.get();
    }

    public static ObjectProperty<ROBOS> roboSelecionadoProperty() {
        return roboSelecionado;
    }

    public static void setRoboSelecionado(ROBOS roboSelecionado) {
        Operacoes.roboSelecionado.set(roboSelecionado);
    }

    public static RoboEstrategia getRoboEstrategia() {
        return roboEstrategia.get();
    }

    public static ObjectProperty<RoboEstrategia> roboEstrategiaProperty() {
        return roboEstrategia;
    }

    public static void setRoboEstrategia(RoboEstrategia roboEstrategia) {
        Operacoes.roboEstrategia.set(roboEstrategia);
    }

    public Timeline getRoboRelogio() {
        return roboRelogio;
    }

    public void setRoboRelogio(Timeline roboRelogio) {
        this.roboRelogio = roboRelogio;
    }

    public long getRoboHoraInicial() {
        return roboHoraInicial.get();
    }

    public LongProperty roboHoraInicialProperty() {
        return roboHoraInicial;
    }

    public void setRoboHoraInicial(long roboHoraInicial) {
        this.roboHoraInicial.set(roboHoraInicial);
    }

    public long getRoboCronometro() {
        return roboCronometro.get();
    }

    public LongProperty roboCronometroProperty() {
        return roboCronometro;
    }

    public void setRoboCronometro(long roboCronometro) {
        this.roboCronometro.set(roboCronometro);
    }

    public boolean isRoboCronometroAtivado() {
        return roboCronometroAtivado.get();
    }

    public BooleanProperty roboCronometroAtivadoProperty() {
        return roboCronometroAtivado;
    }

    public void setRoboCronometroAtivado(boolean roboCronometroAtivado) {
        this.roboCronometroAtivado.set(roboCronometroAtivado);
    }

    public AnchorPane getPainelViewBinary() {
        return painelViewBinary;
    }

    public void setPainelViewBinary(AnchorPane painelViewBinary) {
        this.painelViewBinary = painelViewBinary;
    }

    public TitledPane getTpn_DetalhesConta() {
        return tpn_DetalhesConta;
    }

    public void setTpn_DetalhesConta(TitledPane tpn_DetalhesConta) {
        this.tpn_DetalhesConta = tpn_DetalhesConta;
    }

    public ComboBox<ContaToken> getCboConta() {
        return cboConta;
    }

    public void setCboConta(ComboBox<ContaToken> cboConta) {
        this.cboConta = cboConta;
    }

    public Label getLblLegendaNExecucoes() {
        return lblLegendaNExecucoes;
    }

    public void setLblLegendaNExecucoes(Label lblLegendaNExecucoes) {
        this.lblLegendaNExecucoes = lblLegendaNExecucoes;
    }

    public Label getLblTotalExecucoes() {
        return lblTotalExecucoes;
    }

    public void setLblTotalExecucoes(Label lblTotalExecucoes) {
        this.lblTotalExecucoes = lblTotalExecucoes;
    }

    public Label getLblTotalVitorias() {
        return lblTotalVitorias;
    }

    public void setLblTotalVitorias(Label lblTotalVitorias) {
        this.lblTotalVitorias = lblTotalVitorias;
    }

    public Label getLblTotalDerrotas() {
        return lblTotalDerrotas;
    }

    public void setLblTotalDerrotas(Label lblTotalDerrotas) {
        this.lblTotalDerrotas = lblTotalDerrotas;
    }

    public Label getLblTotalLucro() {
        return lblTotalLucro;
    }

    public void setLblTotalLucro(Label lblTotalLucro) {
        this.lblTotalLucro = lblTotalLucro;
    }

    public Label getLblTotalLucroPorc() {
        return lblTotalLucroPorc;
    }

    public void setLblTotalLucroPorc(Label lblTotalLucroPorc) {
        this.lblTotalLucroPorc = lblTotalLucroPorc;
    }

    public Label getLblProprietarioConta() {
        return lblProprietarioConta;
    }

    public void setLblProprietarioConta(Label lblProprietarioConta) {
        this.lblProprietarioConta = lblProprietarioConta;
    }

    public Label getLblIdConta() {
        return lblIdConta;
    }

    public void setLblIdConta(Label lblIdConta) {
        this.lblIdConta = lblIdConta;
    }

    public Label getLblSaldoConta() {
        return lblSaldoConta;
    }

    public void setLblSaldoConta(Label lblSaldoConta) {
        this.lblSaldoConta = lblSaldoConta;
    }

    public Label getLblMoedaSaldo() {
        return lblMoedaSaldo;
    }

    public void setLblMoedaSaldo(Label lblMoedaSaldo) {
        this.lblMoedaSaldo = lblMoedaSaldo;
    }

    public Label getLblSaldoInicial() {
        return lblSaldoInicial;
    }

    public void setLblSaldoInicial(Label lblSaldoInicial) {
        this.lblSaldoInicial = lblSaldoInicial;
    }

    public Label getLblTotalInvestido() {
        return lblTotalInvestido;
    }

    public void setLblTotalInvestido(Label lblTotalInvestido) {
        this.lblTotalInvestido = lblTotalInvestido;
    }

    public Label getLblTotalPremiacao() {
        return lblTotalPremiacao;
    }

    public void setLblTotalPremiacao(Label lblTotalPremiacao) {
        this.lblTotalPremiacao = lblTotalPremiacao;
    }

    public Label getLblSaldoFinal() {
        return lblSaldoFinal;
    }

    public void setLblSaldoFinal(Label lblSaldoFinal) {
        this.lblSaldoFinal = lblSaldoFinal;
    }

    public TitledPane getTpn_negociacao() {
        return tpn_negociacao;
    }

    public void setTpn_negociacao(TitledPane tpn_negociacao) {
        this.tpn_negociacao = tpn_negociacao;
    }

    public ComboBox<ROBOS> getCboRobos() {
        return cboRobos;
    }

    public void setCboRobos(ComboBox<ROBOS> cboRobos) {
        this.cboRobos = cboRobos;
    }

    public Button getBtnContratos() {
        return btnContratos;
    }

    public void setBtnContratos(Button btnContratos) {
        this.btnContratos = btnContratos;
    }

    public Button getBtnIniciar() {
        return btnIniciar;
    }

    public void setBtnIniciar(Button btnIniciar) {
        this.btnIniciar = btnIniciar;
    }

    public Button getBtnPausar() {
        return btnPausar;
    }

    public void setBtnPausar(Button btnPausar) {
        this.btnPausar = btnPausar;
    }

    public Button getBtnStop() {
        return btnStop;
    }

    public void setBtnStop(Button btnStop) {
        this.btnStop = btnStop;
    }

    public ComboBox<SimNao> getCboVelocidadeTicksGrafico() {
        return cboVelocidadeTicksGrafico;
    }

    public void setCboVelocidadeTicksGrafico(ComboBox<SimNao> cboVelocidadeTicksGrafico) {
        this.cboVelocidadeTicksGrafico = cboVelocidadeTicksGrafico;
    }

    public ComboBox<Integer> getCboQtdTicksGrafico() {
        return cboQtdTicksGrafico;
    }

    public void setCboQtdTicksGrafico(ComboBox<Integer> cboQtdTicksGrafico) {
        this.cboQtdTicksGrafico = cboQtdTicksGrafico;
    }

    public Label getLblRoboHoraInicial() {
        return lblRoboHoraInicial;
    }

    public void setLblRoboHoraInicial(Label lblRoboHoraInicial) {
        this.lblRoboHoraInicial = lblRoboHoraInicial;
    }

    public Label getLblRoboHoraAtual() {
        return lblRoboHoraAtual;
    }

    public void setLblRoboHoraAtual(Label lblRoboHoraAtual) {
        this.lblRoboHoraAtual = lblRoboHoraAtual;
    }

    public Label getLblRoboCronometro() {
        return lblRoboCronometro;
    }

    public void setLblRoboCronometro(Label lblRoboCronometro) {
        this.lblRoboCronometro = lblRoboCronometro;
    }

    public TitledPane getTpn_R10() {
        return tpn_R10;
    }

    public void setTpn_R10(TitledPane tpn_R10) {
        this.tpn_R10 = tpn_R10;
    }

    public BarChart<String, Number> getGrafBar_R10() {
        return grafBar_R10;
    }

    public void setGrafBar_R10(BarChart<String, Number> grafBar_R10) {
        this.grafBar_R10 = grafBar_R10;
    }

    public NumberAxis getyAxisBar_R10() {
        return yAxisBar_R10;
    }

    public void setyAxisBar_R10(NumberAxis yAxisBar_R10) {
        this.yAxisBar_R10 = yAxisBar_R10;
    }

    public BarChart<String, Number> getGrafBar_HZ10() {
        return grafBar_HZ10;
    }

    public void setGrafBar_HZ10(BarChart<String, Number> grafBar_HZ10) {
        this.grafBar_HZ10 = grafBar_HZ10;
    }

    public NumberAxis getyAxisBar_HZ10() {
        return yAxisBar_HZ10;
    }

    public void setyAxisBar_HZ10(NumberAxis yAxisBar_HZ10) {
        this.yAxisBar_HZ10 = yAxisBar_HZ10;
    }

    public LineChart getGrafLine_R10() {
        return grafLine_R10;
    }

    public void setGrafLine_R10(LineChart grafLine_R10) {
        this.grafLine_R10 = grafLine_R10;
    }

    public NumberAxis getyAxisLine_R10() {
        return yAxisLine_R10;
    }

    public void setyAxisLine_R10(NumberAxis yAxisLine_R10) {
        this.yAxisLine_R10 = yAxisLine_R10;
    }

    public LineChart getGrafLine_HZ10() {
        return grafLine_HZ10;
    }

    public void setGrafLine_HZ10(LineChart grafLine_HZ10) {
        this.grafLine_HZ10 = grafLine_HZ10;
    }

    public NumberAxis getyAxisLine_HZ10() {
        return yAxisLine_HZ10;
    }

    public void setyAxisLine_HZ10(NumberAxis yAxisLine_HZ10) {
        this.yAxisLine_HZ10 = yAxisLine_HZ10;
    }

    public Label getLblInf01_R10() {
        return lblInf01_R10;
    }

    public void setLblInf01_R10(Label lblInf01_R10) {
        this.lblInf01_R10 = lblInf01_R10;
    }

    public Label getLblVlrInf01_R10() {
        return lblVlrInf01_R10;
    }

    public void setLblVlrInf01_R10(Label lblVlrInf01_R10) {
        this.lblVlrInf01_R10 = lblVlrInf01_R10;
    }

    public Label getLblPorcInf01_R10() {
        return lblPorcInf01_R10;
    }

    public void setLblPorcInf01_R10(Label lblPorcInf01_R10) {
        this.lblPorcInf01_R10 = lblPorcInf01_R10;
    }

    public Label getLblInf02_R10() {
        return lblInf02_R10;
    }

    public void setLblInf02_R10(Label lblInf02_R10) {
        this.lblInf02_R10 = lblInf02_R10;
    }

    public Label getLblVlrInf02_R10() {
        return lblVlrInf02_R10;
    }

    public void setLblVlrInf02_R10(Label lblVlrInf02_R10) {
        this.lblVlrInf02_R10 = lblVlrInf02_R10;
    }

    public Label getLblPorcInf02_R10() {
        return lblPorcInf02_R10;
    }

    public void setLblPorcInf02_R10(Label lblPorcInf02_R10) {
        this.lblPorcInf02_R10 = lblPorcInf02_R10;
    }

    public Label getLblTickUltimo_R10() {
        return lblTickUltimo_R10;
    }

    public void setLblTickUltimo_R10(Label lblTickUltimo_R10) {
        this.lblTickUltimo_R10 = lblTickUltimo_R10;
    }

    public Label getLblLegendaTickUltimo_R10() {
        return lblLegendaTickUltimo_R10;
    }

    public void setLblLegendaTickUltimo_R10(Label lblLegendaTickUltimo_R10) {
        this.lblLegendaTickUltimo_R10 = lblLegendaTickUltimo_R10;
    }

    public Button getBtnContratos_R10() {
        return btnContratos_R10;
    }

    public void setBtnContratos_R10(Button btnContratos_R10) {
        this.btnContratos_R10 = btnContratos_R10;
    }

    public Button getBtnComprar_R10() {
        return btnComprar_R10;
    }

    public void setBtnComprar_R10(Button btnComprar_R10) {
        this.btnComprar_R10 = btnComprar_R10;
    }

    public Button getBtnPausar_R10() {
        return btnPausar_R10;
    }

    public void setBtnPausar_R10(Button btnPausar_R10) {
        this.btnPausar_R10 = btnPausar_R10;
    }

    public Button getBtnStop_R10() {
        return btnStop_R10;
    }

    public void setBtnStop_R10(Button btnStop_R10) {
        this.btnStop_R10 = btnStop_R10;
    }

    public Label getLblInvestido_R10() {
        return lblInvestido_R10;
    }

    public void setLblInvestido_R10(Label lblInvestido_R10) {
        this.lblInvestido_R10 = lblInvestido_R10;
    }

    public Label getLblInvestidoPorc_R10() {
        return lblInvestidoPorc_R10;
    }

    public void setLblInvestidoPorc_R10(Label lblInvestidoPorc_R10) {
        this.lblInvestidoPorc_R10 = lblInvestidoPorc_R10;
    }

    public Label getLblPremiacao_R10() {
        return lblPremiacao_R10;
    }

    public void setLblPremiacao_R10(Label lblPremiacao_R10) {
        this.lblPremiacao_R10 = lblPremiacao_R10;
    }

    public Label getLblPremiacaoPorc_R10() {
        return lblPremiacaoPorc_R10;
    }

    public void setLblPremiacaoPorc_R10(Label lblPremiacaoPorc_R10) {
        this.lblPremiacaoPorc_R10 = lblPremiacaoPorc_R10;
    }

    public Label getLblLucro_R10() {
        return lblLucro_R10;
    }

    public void setLblLucro_R10(Label lblLucro_R10) {
        this.lblLucro_R10 = lblLucro_R10;
    }

    public Label getLblLucroPorc_R10() {
        return lblLucroPorc_R10;
    }

    public void setLblLucroPorc_R10(Label lblLucroPorc_R10) {
        this.lblLucroPorc_R10 = lblLucroPorc_R10;
    }

    public TableView<Transacoes> getTbvTransacoes_R10() {
        return tbvTransacoes_R10;
    }

    public void setTbvTransacoes_R10(TableView<Transacoes> tbvTransacoes_R10) {
        this.tbvTransacoes_R10 = tbvTransacoes_R10;
    }

    public CheckBox getChkAtivo_R10() {
        return chkAtivo_R10;
    }

    public void setChkAtivo_R10(CheckBox chkAtivo_R10) {
        this.chkAtivo_R10 = chkAtivo_R10;
    }

    public Label getTpnLblLegendaExecucoes_R10() {
        return tpnLblLegendaExecucoes_R10;
    }

    public void setTpnLblLegendaExecucoes_R10(Label tpnLblLegendaExecucoes_R10) {
        this.tpnLblLegendaExecucoes_R10 = tpnLblLegendaExecucoes_R10;
    }

    public Label getTpnLblExecucoes_R10() {
        return tpnLblExecucoes_R10;
    }

    public void setTpnLblExecucoes_R10(Label tpnLblExecucoes_R10) {
        this.tpnLblExecucoes_R10 = tpnLblExecucoes_R10;
    }

    public Label getTpnLblVitorias_R10() {
        return tpnLblVitorias_R10;
    }

    public void setTpnLblVitorias_R10(Label tpnLblVitorias_R10) {
        this.tpnLblVitorias_R10 = tpnLblVitorias_R10;
    }

    public Label getTpnLblDerrotas_R10() {
        return tpnLblDerrotas_R10;
    }

    public void setTpnLblDerrotas_R10(Label tpnLblDerrotas_R10) {
        this.tpnLblDerrotas_R10 = tpnLblDerrotas_R10;
    }

    public Label getTpnLblLucro_R10() {
        return tpnLblLucro_R10;
    }

    public void setTpnLblLucro_R10(Label tpnLblLucro_R10) {
        this.tpnLblLucro_R10 = tpnLblLucro_R10;
    }

    public TitledPane getTpn_R25() {
        return tpn_R25;
    }

    public void setTpn_R25(TitledPane tpn_R25) {
        this.tpn_R25 = tpn_R25;
    }

    public BarChart<String, Number> getGrafBar_R25() {
        return grafBar_R25;
    }

    public void setGrafBar_R25(BarChart<String, Number> grafBar_R25) {
        this.grafBar_R25 = grafBar_R25;
    }

    public NumberAxis getyAxisBar_R25() {
        return yAxisBar_R25;
    }

    public void setyAxisBar_R25(NumberAxis yAxisBar_R25) {
        this.yAxisBar_R25 = yAxisBar_R25;
    }

    public BarChart<String, Number> getGrafBar_HZ25() {
        return grafBar_HZ25;
    }

    public void setGrafBar_HZ25(BarChart<String, Number> grafBar_HZ25) {
        this.grafBar_HZ25 = grafBar_HZ25;
    }

    public NumberAxis getyAxisBar_HZ25() {
        return yAxisBar_HZ25;
    }

    public void setyAxisBar_HZ25(NumberAxis yAxisBar_HZ25) {
        this.yAxisBar_HZ25 = yAxisBar_HZ25;
    }

    public LineChart getGrafLine_R25() {
        return grafLine_R25;
    }

    public void setGrafLine_R25(LineChart grafLine_R25) {
        this.grafLine_R25 = grafLine_R25;
    }

    public NumberAxis getyAxisLine_R25() {
        return yAxisLine_R25;
    }

    public void setyAxisLine_R25(NumberAxis yAxisLine_R25) {
        this.yAxisLine_R25 = yAxisLine_R25;
    }

    public LineChart getGrafLine_HZ25() {
        return grafLine_HZ25;
    }

    public void setGrafLine_HZ25(LineChart grafLine_HZ25) {
        this.grafLine_HZ25 = grafLine_HZ25;
    }

    public NumberAxis getyAxisLine_HZ25() {
        return yAxisLine_HZ25;
    }

    public void setyAxisLine_HZ25(NumberAxis yAxisLine_HZ25) {
        this.yAxisLine_HZ25 = yAxisLine_HZ25;
    }

    public Label getLblInf01_R25() {
        return lblInf01_R25;
    }

    public void setLblInf01_R25(Label lblInf01_R25) {
        this.lblInf01_R25 = lblInf01_R25;
    }

    public Label getLblVlrInf01_R25() {
        return lblVlrInf01_R25;
    }

    public void setLblVlrInf01_R25(Label lblVlrInf01_R25) {
        this.lblVlrInf01_R25 = lblVlrInf01_R25;
    }

    public Label getLblPorcInf01_R25() {
        return lblPorcInf01_R25;
    }

    public void setLblPorcInf01_R25(Label lblPorcInf01_R25) {
        this.lblPorcInf01_R25 = lblPorcInf01_R25;
    }

    public Label getLblInf02_R25() {
        return lblInf02_R25;
    }

    public void setLblInf02_R25(Label lblInf02_R25) {
        this.lblInf02_R25 = lblInf02_R25;
    }

    public Label getLblVlrInf02_R25() {
        return lblVlrInf02_R25;
    }

    public void setLblVlrInf02_R25(Label lblVlrInf02_R25) {
        this.lblVlrInf02_R25 = lblVlrInf02_R25;
    }

    public Label getLblPorcInf02_R25() {
        return lblPorcInf02_R25;
    }

    public void setLblPorcInf02_R25(Label lblPorcInf02_R25) {
        this.lblPorcInf02_R25 = lblPorcInf02_R25;
    }

    public Label getLblTickUltimo_R25() {
        return lblTickUltimo_R25;
    }

    public void setLblTickUltimo_R25(Label lblTickUltimo_R25) {
        this.lblTickUltimo_R25 = lblTickUltimo_R25;
    }

    public Label getLblLegendaTickUltimo_R25() {
        return lblLegendaTickUltimo_R25;
    }

    public void setLblLegendaTickUltimo_R25(Label lblLegendaTickUltimo_R25) {
        this.lblLegendaTickUltimo_R25 = lblLegendaTickUltimo_R25;
    }

    public Button getBtnContratos_R25() {
        return btnContratos_R25;
    }

    public void setBtnContratos_R25(Button btnContratos_R25) {
        this.btnContratos_R25 = btnContratos_R25;
    }

    public Button getBtnComprar_R25() {
        return btnComprar_R25;
    }

    public void setBtnComprar_R25(Button btnComprar_R25) {
        this.btnComprar_R25 = btnComprar_R25;
    }

    public Button getBtnPausar_R25() {
        return btnPausar_R25;
    }

    public void setBtnPausar_R25(Button btnPausar_R25) {
        this.btnPausar_R25 = btnPausar_R25;
    }

    public Button getBtnStop_R25() {
        return btnStop_R25;
    }

    public void setBtnStop_R25(Button btnStop_R25) {
        this.btnStop_R25 = btnStop_R25;
    }

    public Label getLblInvestido_R25() {
        return lblInvestido_R25;
    }

    public void setLblInvestido_R25(Label lblInvestido_R25) {
        this.lblInvestido_R25 = lblInvestido_R25;
    }

    public Label getLblInvestidoPorc_R25() {
        return lblInvestidoPorc_R25;
    }

    public void setLblInvestidoPorc_R25(Label lblInvestidoPorc_R25) {
        this.lblInvestidoPorc_R25 = lblInvestidoPorc_R25;
    }

    public Label getLblPremiacao_R25() {
        return lblPremiacao_R25;
    }

    public void setLblPremiacao_R25(Label lblPremiacao_R25) {
        this.lblPremiacao_R25 = lblPremiacao_R25;
    }

    public Label getLblPremiacaoPorc_R25() {
        return lblPremiacaoPorc_R25;
    }

    public void setLblPremiacaoPorc_R25(Label lblPremiacaoPorc_R25) {
        this.lblPremiacaoPorc_R25 = lblPremiacaoPorc_R25;
    }

    public Label getLblLucro_R25() {
        return lblLucro_R25;
    }

    public void setLblLucro_R25(Label lblLucro_R25) {
        this.lblLucro_R25 = lblLucro_R25;
    }

    public Label getLblLucroPorc_R25() {
        return lblLucroPorc_R25;
    }

    public void setLblLucroPorc_R25(Label lblLucroPorc_R25) {
        this.lblLucroPorc_R25 = lblLucroPorc_R25;
    }

    public TableView<Transacoes> getTbvTransacoes_R25() {
        return tbvTransacoes_R25;
    }

    public void setTbvTransacoes_R25(TableView<Transacoes> tbvTransacoes_R25) {
        this.tbvTransacoes_R25 = tbvTransacoes_R25;
    }

    public CheckBox getChkAtivo_R25() {
        return chkAtivo_R25;
    }

    public void setChkAtivo_R25(CheckBox chkAtivo_R25) {
        this.chkAtivo_R25 = chkAtivo_R25;
    }

    public Label getTpnLblLegendaExecucoes_R25() {
        return tpnLblLegendaExecucoes_R25;
    }

    public void setTpnLblLegendaExecucoes_R25(Label tpnLblLegendaExecucoes_R25) {
        this.tpnLblLegendaExecucoes_R25 = tpnLblLegendaExecucoes_R25;
    }

    public Label getTpnLblExecucoes_R25() {
        return tpnLblExecucoes_R25;
    }

    public void setTpnLblExecucoes_R25(Label tpnLblExecucoes_R25) {
        this.tpnLblExecucoes_R25 = tpnLblExecucoes_R25;
    }

    public Label getTpnLblVitorias_R25() {
        return tpnLblVitorias_R25;
    }

    public void setTpnLblVitorias_R25(Label tpnLblVitorias_R25) {
        this.tpnLblVitorias_R25 = tpnLblVitorias_R25;
    }

    public Label getTpnLblDerrotas_R25() {
        return tpnLblDerrotas_R25;
    }

    public void setTpnLblDerrotas_R25(Label tpnLblDerrotas_R25) {
        this.tpnLblDerrotas_R25 = tpnLblDerrotas_R25;
    }

    public Label getTpnLblLucro_R25() {
        return tpnLblLucro_R25;
    }

    public void setTpnLblLucro_R25(Label tpnLblLucro_R25) {
        this.tpnLblLucro_R25 = tpnLblLucro_R25;
    }

    public TitledPane getTpn_R50() {
        return tpn_R50;
    }

    public void setTpn_R50(TitledPane tpn_R50) {
        this.tpn_R50 = tpn_R50;
    }

    public BarChart<String, Number> getGrafBar_R50() {
        return grafBar_R50;
    }

    public void setGrafBar_R50(BarChart<String, Number> grafBar_R50) {
        this.grafBar_R50 = grafBar_R50;
    }

    public NumberAxis getyAxisBar_R50() {
        return yAxisBar_R50;
    }

    public void setyAxisBar_R50(NumberAxis yAxisBar_R50) {
        this.yAxisBar_R50 = yAxisBar_R50;
    }

    public BarChart<String, Number> getGrafBar_HZ50() {
        return grafBar_HZ50;
    }

    public void setGrafBar_HZ50(BarChart<String, Number> grafBar_HZ50) {
        this.grafBar_HZ50 = grafBar_HZ50;
    }

    public NumberAxis getyAxisBar_HZ50() {
        return yAxisBar_HZ50;
    }

    public void setyAxisBar_HZ50(NumberAxis yAxisBar_HZ50) {
        this.yAxisBar_HZ50 = yAxisBar_HZ50;
    }

    public LineChart getGrafLine_R50() {
        return grafLine_R50;
    }

    public void setGrafLine_R50(LineChart grafLine_R50) {
        this.grafLine_R50 = grafLine_R50;
    }

    public NumberAxis getyAxisLine_R50() {
        return yAxisLine_R50;
    }

    public void setyAxisLine_R50(NumberAxis yAxisLine_R50) {
        this.yAxisLine_R50 = yAxisLine_R50;
    }

    public LineChart getGrafLine_HZ50() {
        return grafLine_HZ50;
    }

    public void setGrafLine_HZ50(LineChart grafLine_HZ50) {
        this.grafLine_HZ50 = grafLine_HZ50;
    }

    public NumberAxis getyAxisLine_HZ50() {
        return yAxisLine_HZ50;
    }

    public void setyAxisLine_HZ50(NumberAxis yAxisLine_HZ50) {
        this.yAxisLine_HZ50 = yAxisLine_HZ50;
    }

    public Label getLblInf01_R50() {
        return lblInf01_R50;
    }

    public void setLblInf01_R50(Label lblInf01_R50) {
        this.lblInf01_R50 = lblInf01_R50;
    }

    public Label getLblVlrInf01_R50() {
        return lblVlrInf01_R50;
    }

    public void setLblVlrInf01_R50(Label lblVlrInf01_R50) {
        this.lblVlrInf01_R50 = lblVlrInf01_R50;
    }

    public Label getLblPorcInf01_R50() {
        return lblPorcInf01_R50;
    }

    public void setLblPorcInf01_R50(Label lblPorcInf01_R50) {
        this.lblPorcInf01_R50 = lblPorcInf01_R50;
    }

    public Label getLblInf02_R50() {
        return lblInf02_R50;
    }

    public void setLblInf02_R50(Label lblInf02_R50) {
        this.lblInf02_R50 = lblInf02_R50;
    }

    public Label getLblVlrInf02_R50() {
        return lblVlrInf02_R50;
    }

    public void setLblVlrInf02_R50(Label lblVlrInf02_R50) {
        this.lblVlrInf02_R50 = lblVlrInf02_R50;
    }

    public Label getLblPorcInf02_R50() {
        return lblPorcInf02_R50;
    }

    public void setLblPorcInf02_R50(Label lblPorcInf02_R50) {
        this.lblPorcInf02_R50 = lblPorcInf02_R50;
    }

    public Label getLblTickUltimo_R50() {
        return lblTickUltimo_R50;
    }

    public void setLblTickUltimo_R50(Label lblTickUltimo_R50) {
        this.lblTickUltimo_R50 = lblTickUltimo_R50;
    }

    public Label getLblLegendaTickUltimo_R50() {
        return lblLegendaTickUltimo_R50;
    }

    public void setLblLegendaTickUltimo_R50(Label lblLegendaTickUltimo_R50) {
        this.lblLegendaTickUltimo_R50 = lblLegendaTickUltimo_R50;
    }

    public Button getBtnContratos_R50() {
        return btnContratos_R50;
    }

    public void setBtnContratos_R50(Button btnContratos_R50) {
        this.btnContratos_R50 = btnContratos_R50;
    }

    public Button getBtnComprar_R50() {
        return btnComprar_R50;
    }

    public void setBtnComprar_R50(Button btnComprar_R50) {
        this.btnComprar_R50 = btnComprar_R50;
    }

    public Button getBtnPausar_R50() {
        return btnPausar_R50;
    }

    public void setBtnPausar_R50(Button btnPausar_R50) {
        this.btnPausar_R50 = btnPausar_R50;
    }

    public Button getBtnStop_R50() {
        return btnStop_R50;
    }

    public void setBtnStop_R50(Button btnStop_R50) {
        this.btnStop_R50 = btnStop_R50;
    }

    public Label getLblInvestido_R50() {
        return lblInvestido_R50;
    }

    public void setLblInvestido_R50(Label lblInvestido_R50) {
        this.lblInvestido_R50 = lblInvestido_R50;
    }

    public Label getLblInvestidoPorc_R50() {
        return lblInvestidoPorc_R50;
    }

    public void setLblInvestidoPorc_R50(Label lblInvestidoPorc_R50) {
        this.lblInvestidoPorc_R50 = lblInvestidoPorc_R50;
    }

    public Label getLblPremiacao_R50() {
        return lblPremiacao_R50;
    }

    public void setLblPremiacao_R50(Label lblPremiacao_R50) {
        this.lblPremiacao_R50 = lblPremiacao_R50;
    }

    public Label getLblPremiacaoPorc_R50() {
        return lblPremiacaoPorc_R50;
    }

    public void setLblPremiacaoPorc_R50(Label lblPremiacaoPorc_R50) {
        this.lblPremiacaoPorc_R50 = lblPremiacaoPorc_R50;
    }

    public Label getLblLucro_R50() {
        return lblLucro_R50;
    }

    public void setLblLucro_R50(Label lblLucro_R50) {
        this.lblLucro_R50 = lblLucro_R50;
    }

    public Label getLblLucroPorc_R50() {
        return lblLucroPorc_R50;
    }

    public void setLblLucroPorc_R50(Label lblLucroPorc_R50) {
        this.lblLucroPorc_R50 = lblLucroPorc_R50;
    }

    public TableView<Transacoes> getTbvTransacoes_R50() {
        return tbvTransacoes_R50;
    }

    public void setTbvTransacoes_R50(TableView<Transacoes> tbvTransacoes_R50) {
        this.tbvTransacoes_R50 = tbvTransacoes_R50;
    }

    public CheckBox getChkAtivo_R50() {
        return chkAtivo_R50;
    }

    public void setChkAtivo_R50(CheckBox chkAtivo_R50) {
        this.chkAtivo_R50 = chkAtivo_R50;
    }

    public Label getTpnLblLegendaExecucoes_R50() {
        return tpnLblLegendaExecucoes_R50;
    }

    public void setTpnLblLegendaExecucoes_R50(Label tpnLblLegendaExecucoes_R50) {
        this.tpnLblLegendaExecucoes_R50 = tpnLblLegendaExecucoes_R50;
    }

    public Label getTpnLblExecucoes_R50() {
        return tpnLblExecucoes_R50;
    }

    public void setTpnLblExecucoes_R50(Label tpnLblExecucoes_R50) {
        this.tpnLblExecucoes_R50 = tpnLblExecucoes_R50;
    }

    public Label getTpnLblVitorias_R50() {
        return tpnLblVitorias_R50;
    }

    public void setTpnLblVitorias_R50(Label tpnLblVitorias_R50) {
        this.tpnLblVitorias_R50 = tpnLblVitorias_R50;
    }

    public Label getTpnLblDerrotas_R50() {
        return tpnLblDerrotas_R50;
    }

    public void setTpnLblDerrotas_R50(Label tpnLblDerrotas_R50) {
        this.tpnLblDerrotas_R50 = tpnLblDerrotas_R50;
    }

    public Label getTpnLblLucro_R50() {
        return tpnLblLucro_R50;
    }

    public void setTpnLblLucro_R50(Label tpnLblLucro_R50) {
        this.tpnLblLucro_R50 = tpnLblLucro_R50;
    }

    public TitledPane getTpn_R75() {
        return tpn_R75;
    }

    public void setTpn_R75(TitledPane tpn_R75) {
        this.tpn_R75 = tpn_R75;
    }

    public BarChart<String, Number> getGrafBar_R75() {
        return grafBar_R75;
    }

    public void setGrafBar_R75(BarChart<String, Number> grafBar_R75) {
        this.grafBar_R75 = grafBar_R75;
    }

    public NumberAxis getyAxisBar_R75() {
        return yAxisBar_R75;
    }

    public void setyAxisBar_R75(NumberAxis yAxisBar_R75) {
        this.yAxisBar_R75 = yAxisBar_R75;
    }

    public BarChart<String, Number> getGrafBar_HZ75() {
        return grafBar_HZ75;
    }

    public void setGrafBar_HZ75(BarChart<String, Number> grafBar_HZ75) {
        this.grafBar_HZ75 = grafBar_HZ75;
    }

    public NumberAxis getyAxisBar_HZ75() {
        return yAxisBar_HZ75;
    }

    public void setyAxisBar_HZ75(NumberAxis yAxisBar_HZ75) {
        this.yAxisBar_HZ75 = yAxisBar_HZ75;
    }

    public LineChart getGrafLine_R75() {
        return grafLine_R75;
    }

    public void setGrafLine_R75(LineChart grafLine_R75) {
        this.grafLine_R75 = grafLine_R75;
    }

    public NumberAxis getyAxisLine_R75() {
        return yAxisLine_R75;
    }

    public void setyAxisLine_R75(NumberAxis yAxisLine_R75) {
        this.yAxisLine_R75 = yAxisLine_R75;
    }

    public LineChart getGrafLine_HZ75() {
        return grafLine_HZ75;
    }

    public void setGrafLine_HZ75(LineChart grafLine_HZ75) {
        this.grafLine_HZ75 = grafLine_HZ75;
    }

    public NumberAxis getyAxisLine_HZ75() {
        return yAxisLine_HZ75;
    }

    public void setyAxisLine_HZ75(NumberAxis yAxisLine_HZ75) {
        this.yAxisLine_HZ75 = yAxisLine_HZ75;
    }

    public Label getLblInf01_R75() {
        return lblInf01_R75;
    }

    public void setLblInf01_R75(Label lblInf01_R75) {
        this.lblInf01_R75 = lblInf01_R75;
    }

    public Label getLblVlrInf01_R75() {
        return lblVlrInf01_R75;
    }

    public void setLblVlrInf01_R75(Label lblVlrInf01_R75) {
        this.lblVlrInf01_R75 = lblVlrInf01_R75;
    }

    public Label getLblPorcInf01_R75() {
        return lblPorcInf01_R75;
    }

    public void setLblPorcInf01_R75(Label lblPorcInf01_R75) {
        this.lblPorcInf01_R75 = lblPorcInf01_R75;
    }

    public Label getLblInf02_R75() {
        return lblInf02_R75;
    }

    public void setLblInf02_R75(Label lblInf02_R75) {
        this.lblInf02_R75 = lblInf02_R75;
    }

    public Label getLblVlrInf02_R75() {
        return lblVlrInf02_R75;
    }

    public void setLblVlrInf02_R75(Label lblVlrInf02_R75) {
        this.lblVlrInf02_R75 = lblVlrInf02_R75;
    }

    public Label getLblPorcInf02_R75() {
        return lblPorcInf02_R75;
    }

    public void setLblPorcInf02_R75(Label lblPorcInf02_R75) {
        this.lblPorcInf02_R75 = lblPorcInf02_R75;
    }

    public Label getLblTickUltimo_R75() {
        return lblTickUltimo_R75;
    }

    public void setLblTickUltimo_R75(Label lblTickUltimo_R75) {
        this.lblTickUltimo_R75 = lblTickUltimo_R75;
    }

    public Label getLblLegendaTickUltimo_R75() {
        return lblLegendaTickUltimo_R75;
    }

    public void setLblLegendaTickUltimo_R75(Label lblLegendaTickUltimo_R75) {
        this.lblLegendaTickUltimo_R75 = lblLegendaTickUltimo_R75;
    }

    public Button getBtnContratos_R75() {
        return btnContratos_R75;
    }

    public void setBtnContratos_R75(Button btnContratos_R75) {
        this.btnContratos_R75 = btnContratos_R75;
    }

    public Button getBtnComprar_R75() {
        return btnComprar_R75;
    }

    public void setBtnComprar_R75(Button btnComprar_R75) {
        this.btnComprar_R75 = btnComprar_R75;
    }

    public Button getBtnPausar_R75() {
        return btnPausar_R75;
    }

    public void setBtnPausar_R75(Button btnPausar_R75) {
        this.btnPausar_R75 = btnPausar_R75;
    }

    public Button getBtnStop_R75() {
        return btnStop_R75;
    }

    public void setBtnStop_R75(Button btnStop_R75) {
        this.btnStop_R75 = btnStop_R75;
    }

    public Label getLblInvestido_R75() {
        return lblInvestido_R75;
    }

    public void setLblInvestido_R75(Label lblInvestido_R75) {
        this.lblInvestido_R75 = lblInvestido_R75;
    }

    public Label getLblInvestidoPorc_R75() {
        return lblInvestidoPorc_R75;
    }

    public void setLblInvestidoPorc_R75(Label lblInvestidoPorc_R75) {
        this.lblInvestidoPorc_R75 = lblInvestidoPorc_R75;
    }

    public Label getLblPremiacao_R75() {
        return lblPremiacao_R75;
    }

    public void setLblPremiacao_R75(Label lblPremiacao_R75) {
        this.lblPremiacao_R75 = lblPremiacao_R75;
    }

    public Label getLblPremiacaoPorc_R75() {
        return lblPremiacaoPorc_R75;
    }

    public void setLblPremiacaoPorc_R75(Label lblPremiacaoPorc_R75) {
        this.lblPremiacaoPorc_R75 = lblPremiacaoPorc_R75;
    }

    public Label getLblLucro_R75() {
        return lblLucro_R75;
    }

    public void setLblLucro_R75(Label lblLucro_R75) {
        this.lblLucro_R75 = lblLucro_R75;
    }

    public Label getLblLucroPorc_R75() {
        return lblLucroPorc_R75;
    }

    public void setLblLucroPorc_R75(Label lblLucroPorc_R75) {
        this.lblLucroPorc_R75 = lblLucroPorc_R75;
    }

    public TableView<Transacoes> getTbvTransacoes_R75() {
        return tbvTransacoes_R75;
    }

    public void setTbvTransacoes_R75(TableView<Transacoes> tbvTransacoes_R75) {
        this.tbvTransacoes_R75 = tbvTransacoes_R75;
    }

    public CheckBox getChkAtivo_R75() {
        return chkAtivo_R75;
    }

    public void setChkAtivo_R75(CheckBox chkAtivo_R75) {
        this.chkAtivo_R75 = chkAtivo_R75;
    }

    public Label getTpnLblLegendaExecucoes_R75() {
        return tpnLblLegendaExecucoes_R75;
    }

    public void setTpnLblLegendaExecucoes_R75(Label tpnLblLegendaExecucoes_R75) {
        this.tpnLblLegendaExecucoes_R75 = tpnLblLegendaExecucoes_R75;
    }

    public Label getTpnLblExecucoes_R75() {
        return tpnLblExecucoes_R75;
    }

    public void setTpnLblExecucoes_R75(Label tpnLblExecucoes_R75) {
        this.tpnLblExecucoes_R75 = tpnLblExecucoes_R75;
    }

    public Label getTpnLblVitorias_R75() {
        return tpnLblVitorias_R75;
    }

    public void setTpnLblVitorias_R75(Label tpnLblVitorias_R75) {
        this.tpnLblVitorias_R75 = tpnLblVitorias_R75;
    }

    public Label getTpnLblDerrotas_R75() {
        return tpnLblDerrotas_R75;
    }

    public void setTpnLblDerrotas_R75(Label tpnLblDerrotas_R75) {
        this.tpnLblDerrotas_R75 = tpnLblDerrotas_R75;
    }

    public Label getTpnLblLucro_R75() {
        return tpnLblLucro_R75;
    }

    public void setTpnLblLucro_R75(Label tpnLblLucro_R75) {
        this.tpnLblLucro_R75 = tpnLblLucro_R75;
    }

    public TitledPane getTpn_R100() {
        return tpn_R100;
    }

    public void setTpn_R100(TitledPane tpn_R100) {
        this.tpn_R100 = tpn_R100;
    }

    public BarChart<String, Number> getGrafBar_R100() {
        return grafBar_R100;
    }

    public void setGrafBar_R100(BarChart<String, Number> grafBar_R100) {
        this.grafBar_R100 = grafBar_R100;
    }

    public NumberAxis getyAxisBar_R100() {
        return yAxisBar_R100;
    }

    public void setyAxisBar_R100(NumberAxis yAxisBar_R100) {
        this.yAxisBar_R100 = yAxisBar_R100;
    }

    public BarChart<String, Number> getGrafBar_HZ100() {
        return grafBar_HZ100;
    }

    public void setGrafBar_HZ100(BarChart<String, Number> grafBar_HZ100) {
        this.grafBar_HZ100 = grafBar_HZ100;
    }

    public NumberAxis getyAxisBar_HZ100() {
        return yAxisBar_HZ100;
    }

    public void setyAxisBar_HZ100(NumberAxis yAxisBar_HZ100) {
        this.yAxisBar_HZ100 = yAxisBar_HZ100;
    }

    public LineChart getGrafLine_R100() {
        return grafLine_R100;
    }

    public void setGrafLine_R100(LineChart grafLine_R100) {
        this.grafLine_R100 = grafLine_R100;
    }

    public NumberAxis getyAxisLine_R100() {
        return yAxisLine_R100;
    }

    public void setyAxisLine_R100(NumberAxis yAxisLine_R100) {
        this.yAxisLine_R100 = yAxisLine_R100;
    }

    public LineChart getGrafLine_HZ100() {
        return grafLine_HZ100;
    }

    public void setGrafLine_HZ100(LineChart grafLine_HZ100) {
        this.grafLine_HZ100 = grafLine_HZ100;
    }

    public NumberAxis getyAxisLine_HZ100() {
        return yAxisLine_HZ100;
    }

    public void setyAxisLine_HZ100(NumberAxis yAxisLine_HZ100) {
        this.yAxisLine_HZ100 = yAxisLine_HZ100;
    }

    public Label getLblInf01_R100() {
        return lblInf01_R100;
    }

    public void setLblInf01_R100(Label lblInf01_R100) {
        this.lblInf01_R100 = lblInf01_R100;
    }

    public Label getLblVlrInf01_R100() {
        return lblVlrInf01_R100;
    }

    public void setLblVlrInf01_R100(Label lblVlrInf01_R100) {
        this.lblVlrInf01_R100 = lblVlrInf01_R100;
    }

    public Label getLblPorcInf01_R100() {
        return lblPorcInf01_R100;
    }

    public void setLblPorcInf01_R100(Label lblPorcInf01_R100) {
        this.lblPorcInf01_R100 = lblPorcInf01_R100;
    }

    public Label getLblInf02_R100() {
        return lblInf02_R100;
    }

    public void setLblInf02_R100(Label lblInf02_R100) {
        this.lblInf02_R100 = lblInf02_R100;
    }

    public Label getLblVlrInf02_R100() {
        return lblVlrInf02_R100;
    }

    public void setLblVlrInf02_R100(Label lblVlrInf02_R100) {
        this.lblVlrInf02_R100 = lblVlrInf02_R100;
    }

    public Label getLblPorcInf02_R100() {
        return lblPorcInf02_R100;
    }

    public void setLblPorcInf02_R100(Label lblPorcInf02_R100) {
        this.lblPorcInf02_R100 = lblPorcInf02_R100;
    }

    public Label getLblTickUltimo_R100() {
        return lblTickUltimo_R100;
    }

    public void setLblTickUltimo_R100(Label lblTickUltimo_R100) {
        this.lblTickUltimo_R100 = lblTickUltimo_R100;
    }

    public Label getLblLegendaTickUltimo_R100() {
        return lblLegendaTickUltimo_R100;
    }

    public void setLblLegendaTickUltimo_R100(Label lblLegendaTickUltimo_R100) {
        this.lblLegendaTickUltimo_R100 = lblLegendaTickUltimo_R100;
    }

    public Button getBtnContratos_R100() {
        return btnContratos_R100;
    }

    public void setBtnContratos_R100(Button btnContratos_R100) {
        this.btnContratos_R100 = btnContratos_R100;
    }

    public Button getBtnComprar_R100() {
        return btnComprar_R100;
    }

    public void setBtnComprar_R100(Button btnComprar_R100) {
        this.btnComprar_R100 = btnComprar_R100;
    }

    public Button getBtnPausar_R100() {
        return btnPausar_R100;
    }

    public void setBtnPausar_R100(Button btnPausar_R100) {
        this.btnPausar_R100 = btnPausar_R100;
    }

    public Button getBtnStop_R100() {
        return btnStop_R100;
    }

    public void setBtnStop_R100(Button btnStop_R100) {
        this.btnStop_R100 = btnStop_R100;
    }

    public Label getLblInvestido_R100() {
        return lblInvestido_R100;
    }

    public void setLblInvestido_R100(Label lblInvestido_R100) {
        this.lblInvestido_R100 = lblInvestido_R100;
    }

    public Label getLblInvestidoPorc_R100() {
        return lblInvestidoPorc_R100;
    }

    public void setLblInvestidoPorc_R100(Label lblInvestidoPorc_R100) {
        this.lblInvestidoPorc_R100 = lblInvestidoPorc_R100;
    }

    public Label getLblPremiacao_R100() {
        return lblPremiacao_R100;
    }

    public void setLblPremiacao_R100(Label lblPremiacao_R100) {
        this.lblPremiacao_R100 = lblPremiacao_R100;
    }

    public Label getLblPremiacaoPorc_R100() {
        return lblPremiacaoPorc_R100;
    }

    public void setLblPremiacaoPorc_R100(Label lblPremiacaoPorc_R100) {
        this.lblPremiacaoPorc_R100 = lblPremiacaoPorc_R100;
    }

    public Label getLblLucro_R100() {
        return lblLucro_R100;
    }

    public void setLblLucro_R100(Label lblLucro_R100) {
        this.lblLucro_R100 = lblLucro_R100;
    }

    public Label getLblLucroPorc_R100() {
        return lblLucroPorc_R100;
    }

    public void setLblLucroPorc_R100(Label lblLucroPorc_R100) {
        this.lblLucroPorc_R100 = lblLucroPorc_R100;
    }

    public TableView<Transacoes> getTbvTransacoes_R100() {
        return tbvTransacoes_R100;
    }

    public void setTbvTransacoes_R100(TableView<Transacoes> tbvTransacoes_R100) {
        this.tbvTransacoes_R100 = tbvTransacoes_R100;
    }

    public CheckBox getChkAtivo_R100() {
        return chkAtivo_R100;
    }

    public void setChkAtivo_R100(CheckBox chkAtivo_R100) {
        this.chkAtivo_R100 = chkAtivo_R100;
    }

    public Label getTpnLblLegendaExecucoes_R100() {
        return tpnLblLegendaExecucoes_R100;
    }

    public void setTpnLblLegendaExecucoes_R100(Label tpnLblLegendaExecucoes_R100) {
        this.tpnLblLegendaExecucoes_R100 = tpnLblLegendaExecucoes_R100;
    }

    public Label getTpnLblExecucoes_R100() {
        return tpnLblExecucoes_R100;
    }

    public void setTpnLblExecucoes_R100(Label tpnLblExecucoes_R100) {
        this.tpnLblExecucoes_R100 = tpnLblExecucoes_R100;
    }

    public Label getTpnLblVitorias_R100() {
        return tpnLblVitorias_R100;
    }

    public void setTpnLblVitorias_R100(Label tpnLblVitorias_R100) {
        this.tpnLblVitorias_R100 = tpnLblVitorias_R100;
    }

    public Label getTpnLblDerrotas_R100() {
        return tpnLblDerrotas_R100;
    }

    public void setTpnLblDerrotas_R100(Label tpnLblDerrotas_R100) {
        this.tpnLblDerrotas_R100 = tpnLblDerrotas_R100;
    }

    public Label getTpnLblLucro_R100() {
        return tpnLblLucro_R100;
    }

    public void setTpnLblLucro_R100(Label tpnLblLucro_R100) {
        this.tpnLblLucro_R100 = tpnLblLucro_R100;
    }
}

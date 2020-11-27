package br.com.tlmacedo.binary.controller;


import br.com.tlmacedo.binary.model.enums.CONTRACT_TYPE;
import br.com.tlmacedo.binary.model.enums.MSG_TYPE;
import br.com.tlmacedo.binary.model.vo.*;
import br.com.tlmacedo.binary.model.vo.Error;
import br.com.tlmacedo.binary.services.Util_Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.tlmacedo.binary.interfaces.Constants.*;

public class WSClient extends WebSocketListener {

    private static ObjectMapper mapper = new ObjectMapper();
    private WebSocket myWebSocket;
    private Msg_type msgType;
    private Symbols symbols;
    private Error error;
    private History history;
    private Tick tick;
    private Candle candle;
    private Passthrough passthrough;
    private Proposal proposal;
    private Authorize authorize;
    private Buy buy;
    private Transaction transaction;

    public WSClient() {
    }

    public void connect() {

        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(CONECT_URL_BINARY).build();
        setMyWebSocket(client.newWebSocket(request, this));//synthetic_index

    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        openOrClosedSocket(true);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        openOrClosedSocket(false);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        System.out.printf("***%s\n", text);
        setMsgType((Msg_type) Util_Json.getMsg_Type(text));
        imprime(text, getMsgType().getMsgType());

        if (getMsgType().getMsgType() != null) {
            switch (getMsgType().getMsgType()) {
                case ACTIVE_SYMBOLS -> {
                    System.out.printf("será que vai????\n");
                    setSymbols((Symbols) Util_Json.getObject_from_String(text, Symbols.class));
                    System.out.printf("result: %s\n", getSymbols());
                    refreshActiveSymbols();
                }
                case HISTORY -> {
                    
//                    Integer symbolId = Operacoes.getSymbolId(Util_Json.getValue_from_EchoReq(text, "ticks_history"));
//                    if (symbolId == null) return;
                    setPassthrough((Passthrough) Util_Json.getObject_from_String(text, Passthrough.class));
                    setHistory((History) Util_Json.getObject_from_String(text, History.class));
                    refreshHistoryTick(getPassthrough(), getHistory());
                }
            }
        }

    }

    /**
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     */

    private void imprime(String text, MSG_TYPE msgType) {
        if (CONSOLE_BINARY_ALL || CONSOLE_BINARY_ALL_SEM_TICKS) {
            if (CONSOLE_BINARY_ALL_SEM_TICKS) {
                if (msgType.equals(MSG_TYPE.TICK))
                    return;
            }
            System.out.printf("..0..%s\n", text);
        } else {
            boolean print = false;
            switch (msgType) {
                case ACTIVE_SYMBOLS -> print = CONSOLE_BINARY_ACTIVE_SYMBOL;
                case AUTHORIZE -> print = CONSOLE_BINARY_AUTHORIZE;
                case ERROR -> print = CONSOLE_BINARY_ERROR;
                case TICK -> print = CONSOLE_BINARY_TICK;
                case PROPOSAL -> print = CONSOLE_BINARY_PROPOSAL;
                case BUY -> print = CONSOLE_BINARY_BUY;
                case TRANSACTION -> print = CONSOLE_BINARY_TRANSACTION;
                case HISTORY -> print = CONSOLE_BINARY_HISTORY;
                default -> print = (CONSOLE_BINARY_ALL || CONSOLE_BINARY_ALL_SEM_TICKS);
            }
            if (print)
                System.out.printf("..1..%s\n", text);
        }
    }

    private void openOrClosedSocket(boolean conectado) {
        if (CONSOLE_BINARY_CONECTADO)
            System.out.printf("servidorConectado: [%s]\n", conectado);
        Operacoes.setWsConectado(conectado);
    }

    private void refreshActiveSymbols() {
        Symbol symbol;
        List<Symbol> symbolList =
                getSymbols().getActive_symbols().stream()
                        .filter(activeSymbol -> activeSymbol.getMarket().equals("synthetic_index"))
                        .collect(Collectors.toList());
        for (int i = 0; i < symbolList.size(); i++) {
            symbol = symbolList.get(i);
            Operacoes.getSymbolDAO().merger(symbol);
        }
    }

    private void refreshError() {
        System.out.printf("deu erro!!!!!\n%s\n", getError().toString());
    }

    private void refreshAuthorize(Authorize authorize) {

    }

    private void refreshTick(Tick tick) {

    }

    private void refreshProposal(Integer symbolId, Proposal proposal, CONTRACT_TYPE contractType) {

    }

    private void refreshTransactionAutorizacao(Transaction transaction) {

        System.out.printf("transaction: %s\n", transaction);

    }

    private void refreshTransaction(Transaction transaction) {

    }

    private void refreshHistoryTick(Passthrough passthrough, History history) {
        Platform.runLater(() -> {
            for (int digito = 0; digito < 10; digito++) {
                Operacoes.getGraficoBarrasListQtdDigito_R()[passthrough.getOperador()].get(digito).setValue(0);
            }

            Operacoes.getHistoricoDeTicksObservableList()[passthrough.getOperador()].clear();
            Operacoes.getHistoricoDeTicksAnaliseObservableList()[passthrough.getOperador()].clear();

            HistoricoDeTicks ticks;
            for (int i = 0; i < getHistory().getTimes().size(); i++) {
                int finalI = i;
                if (Operacoes.getHistoricoDeTicksAnaliseObservableList()[passthrough.getOperador()].stream()
                        .anyMatch(historicoDeTicks -> historicoDeTicks.getTime() == getHistory().getTimes().get(finalI)))
                    continue;
                ticks = new HistoricoDeTicks(passthrough.getOperador(),
                        getHistory().getPrices().get(i), getHistory().getTimes().get(i));
                Operacoes.getHistoricoDeTicksAnaliseObservableList()[passthrough.getOperador()].add(0, ticks);
                Operacoes.getHistoricoDeTicksAnaliseObservableList()[passthrough.getOperador()].sort(Comparator.comparing(HistoricoDeTicks::getTime).reversed());
            }
            for (HistoricoDeTicks tick : Operacoes.getHistoricoDeTicksAnaliseObservableList()[passthrough.getOperador()])
                if (Operacoes.getHistoricoDeTicksObservableList()[passthrough.getOperador()].size() < Operacoes.getGraficoQtdTicks())
                    Operacoes.getHistoricoDeTicksObservableList()[passthrough.getOperador()].add(tick);
        });
    }

    /**
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     */

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static void setMapper(ObjectMapper mapper) {
        WSClient.mapper = mapper;
    }

    public WebSocket getMyWebSocket() {
        return myWebSocket;
    }

    public void setMyWebSocket(WebSocket myWebSocket) {
        this.myWebSocket = myWebSocket;
    }

    public Msg_type getMsgType() {
        return msgType;
    }

    public void setMsgType(Msg_type msgType) {
        this.msgType = msgType;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public Tick getTick() {
        return tick;
    }

    public void setTick(Tick tick) {
        this.tick = tick;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public Authorize getAuthorize() {
        return authorize;
    }

    public void setAuthorize(Authorize authorize) {
        this.authorize = authorize;
    }

    public Buy getBuy() {
        return buy;
    }

    public void setBuy(Buy buy) {
        this.buy = buy;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Symbols getSymbols() {
        return symbols;
    }

    public void setSymbols(Symbols symbols) {
        this.symbols = symbols;
    }

    public Candle getCandle() {
        return candle;
    }

    public void setCandle(Candle candle) {
        this.candle = candle;
    }

    public Passthrough getPassthrough() {
        return passthrough;
    }

    public void setPassthrough(Passthrough passthrough) {
        this.passthrough = passthrough;
    }
}

package br.com.tlmacedo.binary.controller;


import br.com.tlmacedo.binary.model.enums.CONTRACT_TYPE;
import br.com.tlmacedo.binary.model.enums.MSG_TYPE;
import br.com.tlmacedo.binary.model.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.lang.Error;

import static br.com.tlmacedo.binary.interfaces.Constants.*;

public class WSClient extends WebSocketListener {

    private static ObjectMapper mapper = new ObjectMapper();
    private WebSocket myWebSocket;
    private Msg_type msgType;
    private Error error;
    private History history;
    private Tick tick;
    private Proposal proposal;
    private Authorize authorize;
    private Buy buy;
    private Transaction transaction;

    public WSClient() {
    }

    public void connect() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(CONECT_URL_BINARY).build();
        setMyWebSocket(client.newWebSocket(request, this));
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
        super.onMessage(webSocket, text);
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
            System.out.printf("...%s\n", text);
        } else {
            boolean print = false;
            switch (msgType) {
                case AUTHORIZE -> print = CONSOLE_BINARY_AUTHORIZE;
                case ERROR -> print = CONSOLE_BINARY_ERROR;
                case TICK -> print = CONSOLE_BINARY_TICK;
                case PROPOSAL -> print = CONSOLE_BINARY_PROPOSAL;
                case BUY -> print = CONSOLE_BINARY_BUY;
                case TRANSACTION -> print = CONSOLE_BINARY_TRANSACTION;
                case HISTORY -> print = CONSOLE_BINARY_HISTORY;
            }
            if (print)
                System.out.printf("...%s\n", text);
        }
    }

    private void openOrClosedSocket(boolean conectado) {
        Operacoes.setWsConectado(conectado);
        if (CONSOLE_BINARY_CONECTADO)
            System.out.printf("servidorConectado: [%s]\n", conectado);
    }

    private void refreshError(Error error) {

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

    private void refreshHistoryTick(Integer symbolId, History history) {

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
}

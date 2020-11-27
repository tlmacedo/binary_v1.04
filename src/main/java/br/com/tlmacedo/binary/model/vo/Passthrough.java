package br.com.tlmacedo.binary.model.vo;

import java.io.Serializable;

public class Passthrough implements Serializable {
    public static final long serialVersionUID = 1L;

    Integer operador;
    Symbol symbol;
    String operacao;
    String typer;
    String mensagem;

    public Passthrough() {
    }

    public Integer getOperador() {
        return operador;
    }

    public void setOperador(Integer operador) {
        this.operador = operador;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getTyper() {
        return typer;
    }

    public void setTyper(String typer) {
        this.typer = typer;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "Passthrough{" +
                "operador=" + operador +
                ", symbol=" + symbol +
                ", operacao='" + operacao + '\'' +
                ", typer='" + typer + '\'' +
                ", mensagem='" + mensagem + '\'' +
                '}';
    }

}

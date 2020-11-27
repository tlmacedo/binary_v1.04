package br.com.tlmacedo.binary.model.vo;

import java.io.Serializable;

public class TransactionsStream  implements Serializable {
    public static final long serialVersionUID = 1L;

    Integer transaction = 1;
    Integer subscribe = 1;

    public TransactionsStream() {
    }

    public Integer getTransaction() {
        return transaction;
    }

    public void setTransaction(Integer transaction) {
        this.transaction = transaction;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    @Override
    public String toString() {
        return "TransactionsStream{" +
                "transaction=" + transaction +
                ", subscribe=" + subscribe +
                '}';
    }
}

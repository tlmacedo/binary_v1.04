package br.com.tlmacedo.binary.model.vo;


import javafx.beans.property.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "HistoricoDeCandles")
@Table(name = "historico_de_candles")
public class HistoricoDeCandles implements Serializable {
    public static final long serialVersionUID = 1L;

    LongProperty id = new SimpleLongProperty();
    ObjectProperty<Symbol> activeSymbol = new SimpleObjectProperty<>();
    ObjectProperty<BigDecimal> open = new SimpleObjectProperty<>();
    ObjectProperty<BigDecimal> high = new SimpleObjectProperty<>();
    ObjectProperty<BigDecimal> low = new SimpleObjectProperty<>();
    ObjectProperty<BigDecimal> close = new SimpleObjectProperty<>();
    IntegerProperty pip_size = new SimpleIntegerProperty();
    IntegerProperty time = new SimpleIntegerProperty();

    public HistoricoDeCandles() {
    }

    public HistoricoDeCandles(Integer symbolId, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Integer time) {
////        this.activeSymbol = new SimpleObjectProperty<>(Operacoes.getSymbolObservableList().get(symbolId));
//        this.price = new SimpleObjectProperty<>(price);
//        if (getSymbol().getName().contains("1HZ"))
//            this.pip_size = new SimpleIntegerProperty(2);
//        else
//            this.pip_size = new SimpleIntegerProperty(getSymbol().getPip_size());
//        this.time = new SimpleIntegerProperty(time);
//        setUltimoDigito();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Symbol getActiveSymbol() {
        return activeSymbol.get();
    }

    public ObjectProperty<Symbol> activeSymbolProperty() {
        return activeSymbol;
    }

    public void setActiveSymbol(Symbol symbol) {
        this.activeSymbol.set(symbol);
    }

    @Column(length = 19, nullable = false, scale = 4)
    public BigDecimal getOpen() {
        return open.get();
    }

    public ObjectProperty<BigDecimal> openProperty() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open.set(open);
    }

    @Column(length = 19, nullable = false, scale = 4)
    public BigDecimal getHigh() {
        return high.get();
    }

    public ObjectProperty<BigDecimal> highProperty() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high.set(high);
    }

    @Column(length = 19, nullable = false, scale = 4)
    public BigDecimal getLow() {
        return low.get();
    }

    public ObjectProperty<BigDecimal> lowProperty() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low.set(low);
    }

    @Column(length = 19, nullable = false, scale = 4)
    public BigDecimal getClose() {
        return close.get();
    }

    public ObjectProperty<BigDecimal> closeProperty() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close.set(close);
    }

    @Column(length = 1, nullable = false)
    public int getPip_size() {
        return pip_size.get();
    }

    public IntegerProperty pip_sizeProperty() {
        return pip_size;
    }

    public void setPip_size(int pip_size) {
        this.pip_size.set(pip_size);
    }

    @Column(nullable = false)
    public int getTime() {
        return time.get();
    }

    public IntegerProperty timeProperty() {
        return time;
    }

    public void setTime(int time) {
        this.time.set(time);
    }

//    @Transient
//    public String getQuoteCompreto() {
//        return Service_Mascara.getValorFormatado(getPip_size(), getPrice());
//    }


    @Override
    public String toString() {
        return "HistoricoDeCandles{" +
                "id=" + id +
                ", activeSymbol=" + activeSymbol +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", pip_size=" + pip_size +
                ", time=" + time +
                '}';
    }
}

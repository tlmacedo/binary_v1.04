package br.com.tlmacedo.binary.model.vo;

import br.com.tlmacedo.binary.controller.estrategias.RoboEstrategia;
import javafx.beans.property.*;

import java.io.Serializable;

public class Operador implements Serializable {
    public static final long serialVersionUID = 1L;

    LongProperty id = new SimpleLongProperty();
    ObjectProperty<Symbol> mercado = new SimpleObjectProperty<>();
    ObjectProperty<RoboEstrategia> estrategia = new SimpleObjectProperty<>();

    public Operador() {
    }

    public Operador(Long id) {
        this.id = new SimpleLongProperty(id);
    }

    public Operador(Long id, Symbol mercado, RoboEstrategia estrategia) {
        this.id = new SimpleLongProperty(id);
        this.mercado = new SimpleObjectProperty<>(mercado);
        this.estrategia = new SimpleObjectProperty<>(estrategia);
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public Symbol getMercado() {
        return mercado.get();
    }

    public ObjectProperty<Symbol> mercadoProperty() {
        return mercado;
    }

    public void setMercado(Symbol mercado) {
        this.mercado.set(mercado);
    }

    public RoboEstrategia getEstrategia() {
        return estrategia.get();
    }

    public ObjectProperty<RoboEstrategia> estrategiaProperty() {
        return estrategia;
    }

    public void setEstrategia(RoboEstrategia estrategia) {
        this.estrategia.set(estrategia);
    }

    @Override
    public String toString() {
        return "Operador{" +
                "id=" + id +
                ", mercado=" + mercado +
                ", estrategia=" + estrategia +
                '}';
    }

}

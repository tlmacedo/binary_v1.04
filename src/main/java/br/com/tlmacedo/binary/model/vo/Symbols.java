package br.com.tlmacedo.binary.model.vo;

import java.util.ArrayList;
import java.util.List;

public class Symbols {

    List<ActiveSymbol> active_symbols = new ArrayList<>();

    public Symbols() {
    }

    public List<ActiveSymbol> getActive_symbols() {
        return active_symbols;
    }

    public void setActive_symbols(List<ActiveSymbol> active_symbols) {
        this.active_symbols = active_symbols;
    }

    @Override
    public String toString() {
        return "Symbols{" +
                "active_symbols=" + active_symbols +
                '}';
    }
}

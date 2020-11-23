package br.com.tlmacedo.binary.model.vo;

import javafx.beans.property.BooleanProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity(name = "ActiveSymbol")
@Table(name = "active_symbol")
public class ActiveSymbol {

    Boolean allow_forward_startingenum;
    String display_name;
    Boolean exchange_is_openenum;
    Boolean is_trading_suspendedenum;
    String market;
    String market_display_name;
    BigDecimal pip;
    String submarket;
    String submarket_display_name;
    String symbol;
    String symbol_type;

    public ActiveSymbol() {
    }

    public Boolean getAllow_forward_startingenum() {
        return allow_forward_startingenum;
    }

    public void setAllow_forward_startingenum(Boolean allow_forward_startingenum) {
        this.allow_forward_startingenum = allow_forward_startingenum;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Boolean getExchange_is_openenum() {
        return exchange_is_openenum;
    }

    public void setExchange_is_openenum(Boolean exchange_is_openenum) {
        this.exchange_is_openenum = exchange_is_openenum;
    }

    public Boolean getIs_trading_suspendedenum() {
        return is_trading_suspendedenum;
    }

    public void setIs_trading_suspendedenum(Boolean is_trading_suspendedenum) {
        this.is_trading_suspendedenum = is_trading_suspendedenum;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getMarket_display_name() {
        return market_display_name;
    }

    public void setMarket_display_name(String market_display_name) {
        this.market_display_name = market_display_name;
    }

    public BigDecimal getPip() {
        return pip;
    }

    public void setPip(BigDecimal pip) {
        this.pip = pip;
    }

    public String getSubmarket() {
        return submarket;
    }

    public void setSubmarket(String submarket) {
        this.submarket = submarket;
    }

    public String getSubmarket_display_name() {
        return submarket_display_name;
    }

    public void setSubmarket_display_name(String submarket_display_name) {
        this.submarket_display_name = submarket_display_name;
    }

    @Id
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(String symbol_type) {
        this.symbol_type = symbol_type;
    }

    @Override
    public String toString() {
        return "Mercado{" +
                "allow_forward_startingenum=" + allow_forward_startingenum +
                ", display_name='" + display_name + '\'' +
                ", exchange_is_openenum=" + exchange_is_openenum +
                ", is_trading_suspendedenum=" + is_trading_suspendedenum +
                ", market='" + market + '\'' +
                ", market_display_name='" + market_display_name + '\'' +
                ", pip=" + pip +
                ", submarket='" + submarket + '\'' +
                ", submarket_display_name='" + submarket_display_name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", symbol_type='" + symbol_type + '\'' +
                '}';
    }
}

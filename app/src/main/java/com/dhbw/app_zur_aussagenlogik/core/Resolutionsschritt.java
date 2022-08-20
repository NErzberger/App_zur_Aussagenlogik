package com.dhbw.app_zur_aussagenlogik.core;

public class Resolutionsschritt {

    private Formel blockEins;
    private Formel blockZwei;
    private Formel ergebnis;

    public Formel getBlockEins() {
        return blockEins;
    }

    public Formel getBlockZwei() {
        return blockZwei;
    }

    public Formel getErgebnis() {
        return ergebnis;
    }

    public void setBlockEins(Formel blockEins) {
        this.blockEins = blockEins;
    }

    public void setBlockZwei(Formel blockZwei) {
        this.blockZwei = blockZwei;
    }

    public void setErgebnis(Formel ergebnis) {
        this.ergebnis = ergebnis;
    }
}

package com.dhbw.app_zur_aussagenlogik.core;

/**
 * Die Klasse <b>Resolutionsschritt</b> ist dafür da, einzelne Schritte der Resolution darzustellen,
 * damit diese später für einen Rechenweg angezeigt werden können
 *
 *    !!!WICHTIG!!!
 * Diese Klasse ist noch nicht fertig implementiert oder komplett getestet worden!!!
 *    !!!WICHTIG!!!
 *
 * @author Nico Erzberg
 * @version 1.0
 */
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

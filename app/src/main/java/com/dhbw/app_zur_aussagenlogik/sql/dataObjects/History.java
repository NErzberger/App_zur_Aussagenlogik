package com.dhbw.app_zur_aussagenlogik.sql.dataObjects;

public class History {

    private int id;
    private String modi;
    private String formula;
    private String secondFormula;

    public History(int id, String modi, String formula, String secondFormula){
        this.id = id;
        this.modi = modi;
        this.formula = formula;
        this.secondFormula = secondFormula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getSecondFormula() {
        return secondFormula;
    }

    public void setSecondFormula(String secondFormula) {
        this.secondFormula = secondFormula;
    }

    public String getModi() {
        return modi;
    }

    public void setModi(String modi) {
        this.modi = modi;
    }

    @Override
    public String toString(){
        return "ID " + this.id + ", Modus: "+this.modi+ ", Formel: " + this.formula + ", zweite Formel: " + this.secondFormula;
    }
}

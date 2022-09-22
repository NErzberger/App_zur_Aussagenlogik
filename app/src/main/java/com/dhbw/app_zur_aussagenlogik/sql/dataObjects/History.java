package com.dhbw.app_zur_aussagenlogik.sql.dataObjects;

/**
 * Die Klasse <b>History</b> wird dazu verwendet, um Formeln und deren Berechnungen in einem Verlauf zu speichern.
 * Die Klasse erfüllt hierfür zwei Funktionen:
 * <ol>
 *     <li>Sie dient als Entity für die Datenbank</li>
 *     <li>Objekt in den Fragments zum Laden der Formeln und des zugehörigen Modus</li>
 * </ol>
 * <br>
 * Es werden insgesamt 4 Felder verwendet:
 * <table>
 *     <tr>
 *         <th>Datentyp</th>
 *         <th>Bezeichnung</th>
 *     </tr>
 *     <tr>
 *         <td>int</td>
 *         <td>id</td>
 *     </tr>
 *     <tr>
 *         <td>String</td>
 *         <td>modi</td>
 *     </tr>
 *     <tr>
 *         <td>String</td>
 *         <td>formula</td>
 *     </tr>
 *     <tr>
 *         <td>String</td>
 *         <td>secondFormula</td>
 *     </tr>
 * </table>
 * @author Nico Erzberger
 * @version 1.0
 */
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

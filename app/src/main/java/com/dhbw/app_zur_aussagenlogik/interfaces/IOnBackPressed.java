package com.dhbw.app_zur_aussagenlogik.interfaces;

/**
 * Das Interface <b>IOnBackPressed</b> wird dazu verwendet, um Fragments zu typisieren und die Methode <i>goBackToMainFragment</i> implementieren zu lassen.
 * Mittles der Methode goBackToMainFragment soll eine Schnittstelle geschaffen werden, welche von der mainAcitvity aus aufgerufen werden kann,
 * um zum mainFragment zurück zu wechseln. Dies ist erforderlich, falls der Android Zurück-Button betätigt wird.
 * <br>
 * <h2>Verwendungen</h2>
 * <table>
 *     <tr>
 *         <th>Fragment Name</th>
 *         <th>Aufgerufen von</th>
 *         <th>Rückkehr zu</th>
 *     </tr>
 *     <tr>
 *         <td>MainFragment</td>
 *         <td>MainActivity</td>
 *         <td>System Ende</td>
 *     </tr>
 *     <tr>
 *         <td>AboutUsFragment</td>
 *         <td>MainActivity - Menü</td>
 *         <td>MainFragment</td>
 *     </tr>
 *     <tr>
 *          <td>InstructionFragment</td>
 *          <td>MainActivity - Menü</td>
 *          <td>MainFragment</td>
 *     </tr>
 *     <tr>
 *         <td>HistoryFragment</td>
 *         <td>MainActivity - Menü</td>
 *         <td>MainFragment</td>
 *     </tr>
 *     <tr>
 *         <td>NormalformFragment</td>
 *         <td>MainFragment - Modus KNF/DNF</td>
 *         <td>MainFragment</td>
 *     </tr>
 *     <tr>
 *         <td>ResolutionFragment</td>
 *         <td>MainFragment - Modus Resolution</td>
 *         <td>MainFragment</td>
 *     </tr>
 *     <tr>
 *         <td>TruthTableFragment</td>
 *         <td>MainFragment - Modus Wertetabelle</td>
 *         <td>MainFragment</td>
 *     </tr>
 *     <tr>
 *         <td>ZweiFormelFragment</td>
 *         <td>MainFragment - Modus Formel</td>
 *         <td>MainFragment</td>
 *     </tr>
 * </table>
 * @author Nico Erzberger
 * @version 1.0
 */
public interface IOnBackPressed {
    void goBackToMainFragment();
}

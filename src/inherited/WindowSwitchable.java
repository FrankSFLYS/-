/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package inherited;

/**
 *
 * @author FrankFLY
 */
public interface WindowSwitchable extends Switchable {

    /**
     * 切换到窗口，并且在切换回原窗口时调用callback方法
     * 
     * @param callback
     *            要回调的方法
     */
    void switchTo(WindowCallback callback);

    void switchBack();

}

/**
 * 允许两个窗口进行互相切换
 * 
 */
package inherited;

/**
 * @author FrankFLY
 *
 */
public interface DialogSwitchable extends Switchable {

    /**
     * 切换到此窗口
     */
    void switchIn();

    /**
     * 切换到此窗口，并且在窗口关闭的时候调用DialogCallback的实例。
     * 
     * @param callable
     *            {@link base.DialogCallback DialogCallback }的实例。
     */
    void switchIn(DialogCallback callable);

    /**
     * 切换出此窗口
     */
    void switchOut();

}

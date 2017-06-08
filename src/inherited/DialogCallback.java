package inherited;

import base.KeyCode;

/**
 * 回调接口，用于对话框（{@link base.Dialog Dialog}）的回调事件。
 * 
 * @author FrankFLY
 *
 *         用法：调用者通过 {@link inherited.Switchable Switchable} 打开一个对话框时，
 *         使用{@code switchIn(DialogCallback callable)} 进行切换，即可在对话框
 *         关闭时调用{@code callable}，例： <blockquote>
 * 
 *         <pre>
 *         Dialog dialog = new Dialog("Title", "Message");
 *         dialog.switchIn(keyCode -&#62; {
 *             switch (keyCode) {
 *                 case CONFIRM:
 *                     // do something when "Confirm" has been clicked
 *                     break;
 *                 case CANCEL:
 *                 default:
 *                     // do something else
 *                     break;
 *             }
 *         });
 *         </pre>
 * 
 *         </blockquote> 上述例子会在对话框按“确认”关闭后进入CONFIRM中执行。
 */
public interface DialogCallback extends Callback {

    /**
     * 对话框结束时回调的方法。
     * 
     * @param keyCode
     *            对话框结束时点击的按钮代号
     */
    void call(KeyCode keyCode);

}

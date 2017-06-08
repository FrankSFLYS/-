package inherited;

import base.KeyCode;

/**
 * �ص��ӿڣ����ڶԻ���{@link base.Dialog Dialog}���Ļص��¼���
 * 
 * @author FrankFLY
 *
 *         �÷���������ͨ�� {@link inherited.Switchable Switchable} ��һ���Ի���ʱ��
 *         ʹ��{@code switchIn(DialogCallback callable)} �����л��������ڶԻ���
 *         �ر�ʱ����{@code callable}������ <blockquote>
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
 *         </blockquote> �������ӻ��ڶԻ��򰴡�ȷ�ϡ��رպ����CONFIRM��ִ�С�
 */
public interface DialogCallback extends Callback {

    /**
     * �Ի������ʱ�ص��ķ�����
     * 
     * @param keyCode
     *            �Ի������ʱ����İ�ť����
     */
    void call(KeyCode keyCode);

}

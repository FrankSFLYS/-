/**
 * �����������ڽ��л����л�
 * 
 */
package inherited;

/**
 * @author FrankFLY
 *
 */
public interface DialogSwitchable extends Switchable {

    /**
     * �л����˴���
     */
    void switchIn();

    /**
     * �л����˴��ڣ������ڴ��ڹرյ�ʱ�����DialogCallback��ʵ����
     * 
     * @param callable
     *            {@link base.DialogCallback DialogCallback }��ʵ����
     */
    void switchIn(DialogCallback callable);

    /**
     * �л����˴���
     */
    void switchOut();

}

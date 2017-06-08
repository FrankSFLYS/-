package base;

import inherited.WindowCallback;
import inherited.WindowSwitchable;

public class Window extends BaseFrame implements WindowSwitchable {

    /**
     * 
     */
    private static final long serialVersionUID = -1769775057512788849L;

    public Window(String title) {
        super(title);
    }

    private WindowCallback callback;

    @Override
    public void switchTo(WindowCallback callback) {
        this.callback = callback;
        setVisible(true);
    }

    @Override
    public void switchBack() {
        if (callback != null) {
            callback.call();
        }
    }

    @Override
    protected void onFinish() {
        switchBack();
        super.onFinish();
    }

}

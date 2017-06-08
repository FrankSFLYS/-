package base;

import inherited.DialogSwitchable;
import inherited.DialogCallback;
import javax.swing.BoxLayout;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import control.ScreenUtils;
import static base.KeyCode.*;

public class Dialog extends BaseFrame implements DialogSwitchable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4119686647665831083L;

    KeyCode keyCode = KeyCode.NULL;
    DialogCallback callable;

    private MultilineLabel msgArea = new MultilineLabel();

    public enum DialogType {
        CONFIRM_ONLY, CONFIRM_CANCEL, YES_NO, YES_NO_CANCEL, RETRY_CANCEL, T_R_I,
    }

    private DialogType type;
    private JButton aButton, bButton, cButton;

    public Dialog() {
        this(DialogType.CONFIRM_CANCEL);
    }

    public Dialog(DialogType type) {
        super();
        setDialogType(type);
        refreshView();
    }

    public Dialog(String title) {
        this(title, DialogType.CONFIRM_CANCEL);
    }

    public Dialog(String title, DialogType type) {
        super(title);
        setDialogType(type);
        refreshView();
    }

    public Dialog(String title, String message) {
        this(title, message, DialogType.CONFIRM_CANCEL);
    }

    public Dialog(String title, String message, DialogType type) {
        super(title);
        setMessage(message);
        setDialogType(type);
        refreshView();
    }

    public void setMessage(String message) {
        msgArea.setText(message);
    }

    public String getMessage() {
        return msgArea.getText();
    }

    public void setDialogType(DialogType type) {
        if (this.type != type) {
            this.type = type;
            refreshView();
        }
    }

    @Override
    public void switchIn() {
        this.callable = null;
        setVisible(true);
    }

    @Override
    public void switchIn(DialogCallback callable) {
        this.callable = callable;
        setVisible(true);
    }

    @Override
    public void switchOut() {
        if (callable != null) {
            callable.call(keyCode);
        }
    }

    @Override
    protected void onFinish() {
        switchOut();
        super.onFinish();
    }

    private void refreshView() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        // Place the window in the middle of screen
        setLocation((int) ScreenUtils.getWidth() / 2 - 150,
            (int) ScreenUtils.getHeight() / 2 - 100);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        setResizable(false);

        initButtons();
        initView();
    }

    private void initButtons() {
        switch (type) {
            case CONFIRM_CANCEL:
                cButton = new JButton(CANCEL);
            case CONFIRM_ONLY:
                aButton = new JButton(CONFIRM);
                break;
            case YES_NO_CANCEL:
                cButton = new JButton(CANCEL);
            case YES_NO:
                aButton = new JButton(YES);
                bButton = new JButton(NO);
                break;
            case RETRY_CANCEL:
                bButton = new JButton(RETRY);
                cButton = new JButton(CANCEL);
                break;
            case T_R_I:
                aButton = new JButton(TERMINATE);
                bButton = new JButton(RETRY);
                cButton = new JButton(IGNORE);
                break;
            default:
                aButton = new JButton(CONFIRM);
                cButton = new JButton(CANCEL);
                break;
        }
        if (aButton != null) {
            aButton.addActionListener(l -> {
                keyCode = KeyCode.CONFIRM_YES_TERMINATE;
                dispose();
            });
        }
        if (bButton != null) {
            bButton.addActionListener(l -> {
                keyCode = KeyCode.NO_RETRY;
                dispose();
            });
        }
        if (cButton != null) {
            cButton.addActionListener(l -> {
                keyCode = KeyCode.CANCEL_IGNORE;
                dispose();
            });
        }
    }

    private void initView() {
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        msgArea.setBackground(topPanel.getBackground());

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(msgArea);
        topPanel.add(Box.createHorizontalStrut(5));

        rootPanel.add(Box.createVerticalStrut(7));
        rootPanel.add(topPanel);

        JPanel butPanel = new JPanel();
        butPanel.setLayout(new BoxLayout(butPanel, BoxLayout.X_AXIS));
        switch (type) {
            // aButton: Confirm / Yes / Terminate
            // bButton: No / Retry
            // cButton: Cancel / Ignore
            case CONFIRM_ONLY:
                // One Button, aButton only
                butPanel.add(Box.createHorizontalGlue());
                butPanel.add(aButton);
                butPanel.add(Box.createHorizontalGlue());
                break;
            case CONFIRM_CANCEL:
                // Two Buttons, a & c Buttons
                butPanel.add(Box.createHorizontalGlue());
                butPanel.add(aButton);
                butPanel.add(Box.createHorizontalStrut(13));
                butPanel.add(cButton);
                butPanel.add(Box.createHorizontalGlue());
                break;
            case RETRY_CANCEL:
                // Two Buttons, b & c Buttons
                butPanel.add(Box.createHorizontalGlue());
                butPanel.add(bButton);
                butPanel.add(Box.createHorizontalStrut(13));
                butPanel.add(cButton);
                butPanel.add(Box.createHorizontalGlue());
                break;
            case YES_NO:
                // Two Buttons, a & b Buttons
                butPanel.add(Box.createHorizontalGlue());
                butPanel.add(aButton);
                butPanel.add(Box.createHorizontalStrut(13));
                butPanel.add(bButton);
                butPanel.add(Box.createHorizontalGlue());
                break;
            case T_R_I:
            case YES_NO_CANCEL:
                // Three Buttons
                butPanel.add(Box.createHorizontalGlue());
                butPanel.add(aButton);
                butPanel.add(Box.createHorizontalStrut(12));
                butPanel.add(bButton);
                butPanel.add(Box.createHorizontalStrut(12));
                butPanel.add(cButton);
                butPanel.add(Box.createHorizontalGlue());
                break;
        }

        rootPanel.add(butPanel);
        rootPanel.add(Box.createVerticalStrut(4));

        add(rootPanel, BorderLayout.CENTER);
    }

}

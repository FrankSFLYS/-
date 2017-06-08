package control;

import base.Dialog;
import base.Dialog.DialogType;
import exception.FailedToResetException;
import exception.KeyNotFoundException;

public class ConfigEditor {

    private String cName;
    private FileEditor fEditor;

    public ConfigEditor(String cName) {
        this.cName = cName;
        try {
            fEditor = new FileEditor(cName + ".config");
        } catch (FailedToResetException e) {
            Dialog dialog = new Dialog("错误", "无法读取配置文件，某些设置可能已经重置了。", DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }

    public String getConfigureName() {
        return cName;
    }

    public void put(String key, String value) {
        fEditor.put(key, value);
    }

    public void put(String key, Integer value) {
        put(key, value.toString());
    }

    public void put(String key, Float value) {
        put(key, value.toString());
    }

    public void put(String key, Boolean value) {
        put(key, value.toString());
    }

    public void put(String key, Long value) {
        put(key, value.toString());
    }

    public String get(String key, String defaultValue) {
        try {
            return fEditor.find(key);
        } catch (KeyNotFoundException e) {
            put(key, defaultValue);
        }
        return defaultValue;
    }

    public Integer get(String key, Integer defaultValue) {
        try {
            return Integer.parseInt(fEditor.find(key));
        } catch (KeyNotFoundException e) {
            put(key, defaultValue);
        } catch (NumberFormatException e) {
            put(key, defaultValue);
        }
        return defaultValue;
    }

    public Boolean get(String key, Boolean defaultValue) {
        try {
            return Boolean.parseBoolean(fEditor.find(key));
        } catch (KeyNotFoundException e) {
            put(key, defaultValue);
        }
        return defaultValue;
    }

    public Float get(String key, Float defaultValue) {
        try {
            return Float.parseFloat(fEditor.find(key));
        } catch (KeyNotFoundException e) {
            put(key, defaultValue);
        } catch (NumberFormatException e) {
            put(key, defaultValue);
        }
        return defaultValue;

    }

    public Long get(String key, Long defaultValue) {
        try {
            return Long.parseLong(fEditor.find(key));
        } catch (KeyNotFoundException e) {
            put(key, defaultValue);
        } catch (NumberFormatException e) {
            put(key, defaultValue);
        }
        return defaultValue;
    }
}

package control;

public class Configure {

	public static final String WINDOW_WIDTH = "width";
	public static final String WINDOW_HEIGHT = "height";
	
	private String id;
	private ConfigEditor editor;
	 
	public Configure(String id) {
		this.id = id;
		editor = new ConfigEditor(id);
	}
	
	public String getId() {
		return id;
	}
	
	/**
     * Returns a configuration of this application
     * The configuration is saved as SharedPreferences, and its file name
     * is "config.xml"
     *
     * @see #getSharedPref(String, Object, String)
     *
     * @param key The tag of an item to save, it is also the tag of xml tag
     * @param defaultVal If the item has not been saved yet or something wrong happened,
     *                   defaultVal will be returned
     * @param <T> Type of defaultVal
     * @return Saved value, defaultVal will be returned if key has not been saved
     *  Notice: You have to cast return value
     */
    public <T> Object getConfig(String key, T defaultVal) {
        return getStoredValue(key, defaultVal);
    }
    
    /**
     * Save the configuration refered by key
     * @param key The name / flag of configuration.
     * @param value The value to save
     */
    public <T> void saveConfig(String key, T value) {
    	editor.put(key, value.toString());
    }
    
	/**
     * Get SharedPreference settings
     *
     * @param key Tag of an item
     * @param defaultVal Default value of the item, if key cannot be found or other
     *                   error occurs, defaultVal will be the return value
     * @param valueType To show which pref file is the item from
     * @param <T> The type of defaultVal
     * @return Saved value, or defaultVal if the item has not been saved
     */
    private <T> Object getStoredValue(String key, T defaultVal) {
    	
        String className = defaultVal.getClass().getSimpleName();
        Class<? extends Object> classOfT = defaultVal.getClass();
        
        switch (className) {
            case "String":
                String aStr;
                aStr = editor.get(key, (String) defaultVal);
                try {
                    return classOfT.cast(aStr);
                } catch (ClassCastException e) {
                    return defaultVal;
                }
            case "Integer":
                Integer anInt;
                anInt = editor.get(key, (Integer) defaultVal);
                try {
                    return classOfT.cast(anInt);
                } catch (ClassCastException e) {
                    return defaultVal;
                }
            case "Float":
                Float aFloat;
                aFloat = editor.get(key, (Float) defaultVal);
                try {
                    return classOfT.cast(aFloat);
                } catch (ClassCastException e) {
                    return defaultVal;
                }
            case "Boolean":
                Boolean aBoolean;
                aBoolean = editor.get(key, (Boolean) defaultVal);
                try {
                    return classOfT.cast(aBoolean);
                } catch (ClassCastException e) {
                    return defaultVal;
                }
            case "Long":
                Long aLong;
                aLong = editor.get(key, (Long) defaultVal);
                try {
                    return classOfT.cast(aLong);
                } catch (ClassCastException e) {
                    return defaultVal;
                }
            default:
                return defaultVal;
        }
    }
}

package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.FailedToResetException;
import exception.KeyNotFoundException;

public class FileEditor {
	
	File file;
	HashMap<String, String> map;
	
	public FileEditor(String fileName) throws FailedToResetException {
		file = new File(fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				try {
					resetToDefault(fileName);
				} catch (FailedToResetException fe) {
					throw fe;
				}
			}
		}
		map = new HashMap<String, String>();
		try {
			load();
		} catch (IOException e) {
			try {
				resetToDefault(fileName);
			} catch (FailedToResetException fe) {
				throw fe;
			}
		}
	}
	
	private void load() throws IOException {
		FileInputStream fStream = new FileInputStream(file);
		InputStreamReader iReader = new InputStreamReader(fStream, "UTF-8");
		BufferedReader bReader = new BufferedReader(iReader);
		Pattern pattern = Pattern.compile("([a-zA-Z_0-9]+),([a-zA-Z_0-9:\\.\\\\]+)");
		Matcher m;
		String line;
		while((line = bReader.readLine()) != null) {
			if(line.length() > 0) {
				m = pattern.matcher(line);
				if(m.find()) {
					map.put(m.group(1), m.group(2));
				}
			}
		}
		bReader.close();
	}
	
	private void save() {
		try {
			FileOutputStream fStream = new FileOutputStream(file);
			OutputStreamWriter oWriter = new OutputStreamWriter(fStream, "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(oWriter);
			for(Entry<String, String> entry : map.entrySet()) {
				bWriter.write(entry.getKey() + "," + entry.getValue() + "\n");
			}
			bWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String find(String key) throws KeyNotFoundException {
		if(map.containsKey(key)) {
			return map.get(key);
		}
		save();
		throw new KeyNotFoundException();
	}
	
	public void put(String key, String value) {
		map.put(key, value);
		save();
	}
	
	private void resetToDefault(String fileName) throws FailedToResetException {
		file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new FailedToResetException();
		}
	}
	
}
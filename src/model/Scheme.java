package model;

import java.util.ArrayList;
import java.util.Objects;

public class Scheme {

	private long index;
	private String name;
	private ArrayList<String> origins;
	private ArrayList<Strategy> strategies;
	
	public Scheme() {
	}
	
	public Scheme(long index) {
		this.index = index;
	}
	
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public String getName() {
        if(name == null) {
            return "";
        }
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getOrigins() {
		return origins;
	}
	public void setOrigins(ArrayList<String> origins) {
		this.origins = origins;
	}
	public ArrayList<Strategy> getStrategies() {
		return strategies;
	}
	public void setStrategies(ArrayList<Strategy> strategies) {
		this.strategies = strategies;
	}
    
    @Override
    public String toString() {
        return name;
    }
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() == this.getClass()) {
			Scheme s = (Scheme)obj;
			return s.getIndex() == index;
		} else {
			return false;
		}
	}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (this.index ^ (this.index >>> 32));
        hash = 41 * hash + Objects.hashCode(this.name);
        return hash;
    }
	
}

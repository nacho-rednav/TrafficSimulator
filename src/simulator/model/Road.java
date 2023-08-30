package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject{

	protected Junction srcJunc, destJunc;
	protected int maxSpeed;
	protected int length;
	protected int currentMaxSpeed;
	protected int contLimit;
	protected int totalCO2;
	protected Weather weather;
	protected List<Vehicle> vehicles;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) {
		super(id);
		if(maxSpeed <= 0 || contLimit < 0 || length <= 0
				|| srcJunc == null || destJunc == null || weather == null)
			throw new IllegalArgumentException("Wrong arguments for road");
		else {
			this.maxSpeed = maxSpeed;
			currentMaxSpeed = maxSpeed;
			this.contLimit = contLimit;
			totalCO2 = 0;
			this.weather = weather;
			this.srcJunc = srcJunc;
			this.destJunc = destJunc;
			this.length = length;
			vehicles = new ArrayList<Vehicle>();
			srcJunc.addOutGoingRoad(this);
			destJunc.addIncomingRoad(this);
		}
	}
	
	void enter(Vehicle v) {
		if(v.getCurrentSpeed() != 0 || v.getLocation() != 0)
			throw new IllegalArgumentException("Invalid car to enter the road");
		else {
			vehicles.add(v);
		}
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather w) {
		if(w == null)
			throw new IllegalArgumentException("Illegal weather");
		else {
			weather = w;
		}
	}
	
	void addContamination(int c) {
		if(c < 0)
			throw new IllegalArgumentException("Illegal contamination");
		else {
			totalCO2 += c;
		}
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v : vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		vehicles.sort(null);
	}

	@Override
	public JSONObject report() {
		JSONObject j = new JSONObject();
		j.put("id", _id);
		j.put("speedlimit", currentMaxSpeed);
		j.put("weather", weather);
		j.put("co2", totalCO2);
		JSONArray ja = new JSONArray();
		for(Vehicle v: vehicles)
			ja.put(v.getId());
		j.put("vehicles", ja);
		
		return j;
	}

	public Junction getSrcJunc() {
		return srcJunc;
	}

	public Junction getDestJunc() {
		return destJunc;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getLength() {
		return length;
	}

	public int getCurrentMaxSpeed() {
		return currentMaxSpeed;
	}

	public int getContLimit() {
		return contLimit;
	}

	public int getTotalCO2() {
		return totalCO2;
	}

	public Weather getWeather() {
		return weather;
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}

}

package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class RoadMap {
	List<Junction> junctions;
	List<Road> roads;
	List<Vehicle> vehicles;
	Map<String,Junction> idJunctions;
	Map<String,Road> idRoads;
	Map<String,Vehicle> idVehicles;
	
	RoadMap(){
		List<Junction> junctions = new ArrayList<Junction>();
		List<Road> roads = new ArrayList<Road>();
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		Map<String,Junction> idJunctions = new HashMap<String,Junction>();
		Map<String,Road> idRoads = new HashMap<String,Road>();
		Map<String,Vehicle> idVehicles = new HashMap<String,Vehicle>();
	}
	
	void addJunction(Junction j) {
		if (!junctions.contains(j)) {
			junctions.add(j);
			//Acabar
		}
	}
	
	void addRoad(Road r) {
		
	}
	
	void addVehicle(Vehicle v) {
		
	}
	
	public Junction getJunction(String id) {
		for (Junction j : junctions)
			if(j.getId() == id)
				return j;
		return null;
	}
	
	public Road getRoad(String id) {
		for (Road j : roads)
			if(j.getId() == id)
				return j;
		return null;
	}
	
	public Vehicle getVehicle(String id) {
		for (Vehicle j : vehicles)
			if(j.getId() == id)
				return j;
		return null;
	}
	
	public List<Junction>getJunctions(){
		return Collections.unmodifiableList(junctions);
	}
	
	public List<Road>getRoads(){
		return Collections.unmodifiableList(roads);
	}
	
	public List<Vehicle>getVehicles(){
		return Collections.unmodifiableList(vehicles);
	}
	
	void reset() {
		junctions.clear();
		vehicles.clear();
		roads.clear();
		idJunctions.clear();
		idRoads.clear();
	    idVehicles.clear();
	}
	
	public JSONObject report() {
		return null;
	}
}

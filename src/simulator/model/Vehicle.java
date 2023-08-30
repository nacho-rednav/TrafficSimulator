package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.json.JSONObject;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle>{

	private List<Junction> itinerary;
	private int maxSpeed;
	private int currentSpeed;
	private VehicleStatus state;
	private Road road;
	private int location;
	private int contClass;
	private int totalCO2;
	private int totalDistance;
	private int junctionIndex;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		if(maxSpeed <= 0 || itinerary.size() < 2)
			throw new IllegalArgumentException("Error al crear vehículo");
		this.maxSpeed = maxSpeed;
		setSpeed(maxSpeed);
		setContaminationClass(contClass);
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		currentSpeed = 0;
		state = VehicleStatus.PENDING;
		location = 0;
		totalCO2 = 0;
		totalDistance = 0;
		junctionIndex = 0;
	}
	
	void setContaminationClass(int c) {
		if(c < 0 || c > 10) {
			throw new IllegalArgumentException("Incorrect contamination class");
		}
		else {
			contClass = c;
		}	
	}
	
	void setSpeed(int s) {
		if(s <= 0) {
			throw new IllegalArgumentException("Incorrect max speed");
		}
		else {
			currentSpeed = min(s, maxSpeed);
		}	
	}
	
	void moveToNextRoad() {
		if(VehicleStatus.PENDING.equals(state) || VehicleStatus.WAITING.equals(state)) {
			if(junctionIndex == itinerary.size() - 1) {
				state = VehicleStatus.ARRIVED;
				currentSpeed = 0;
			}
			else {
				prepareRoadEnter();
				if(VehicleStatus.PENDING.equals(state)) {
					enterNextRoad();
				}
				else {
					road.exit(this);
					enterNextRoad();
				}
			}
			junctionIndex++;
		}
		else {
			throw new IllegalArgumentException("Vehicle can´t move to next road");
		}
	}
	
	private void enterNextRoad() {
		road = itinerary.get(junctionIndex).roadTo(itinerary.get(junctionIndex + 1));
		road.enter(this);
	}
	
	private void prepareRoadEnter() {
		location = 0;
		currentSpeed = 0;
		state = VehicleStatus.TRAVELING;
	}
	
	@Override
	void advance(int time) {
		int distanceAdvanced;
		if(VehicleStatus.TRAVELING.equals(state)) {
			distanceAdvanced = min(currentSpeed, road.getLength() - location);
			location += distanceAdvanced;
			totalDistance += distanceAdvanced;
			totalCO2 += contClass * distanceAdvanced;
			road.addContamination(contClass * distanceAdvanced);
			if(location >= road.getLength()) {
				state = VehicleStatus.WAITING;
				currentSpeed = 0;
				itinerary.get(junctionIndex).enter(this);
			}
		}
	}

	@Override
	public JSONObject report() {
		JSONObject j = new JSONObject();
		j.put("id", _id);
		j.put("speed", currentSpeed);
		j.put("distance", totalDistance);
		j.put("co2", totalCO2);
		j.put("class", contClass);
		j.put("status", state.toString());
		if(VehicleStatus.WAITING.equals(state) || VehicleStatus.TRAVELING.equals(state)) {
			j.put("road", road.getId());
			j.put("location", location);
		}

		return j;
	}
	
	private int min(int n1, int n2) {
		if(n1 < n2)
			return n1;
		else
			return n2;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getCurrentSpeed() {
		return currentSpeed;
	}

	public VehicleStatus getState() {
		return state;
	}

	public Road getRoad() {
		return road;
	}

	public int getLocation() {
		return location;
	}

	public int getContClass() {
		return contClass;
	}

	public int getTotalCO2() {
		return totalCO2;
	}

	@Override
	public int compareTo(Vehicle v) {
		if(location == v.getLocation()) return 0;
		else if(location < v.getLocation())return -1;
		else return 1;
	}

}

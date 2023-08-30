package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{
	
	List<Road> incomingRoads;
	Map<Junction, Road> outgoingRoads;
	List<List<Vehicle>> queuesList;
	Map <Road, List<Vehicle>> roadQueueMap;
	int currGreen;
	int lastSwitchingTime;
	LightSwitchStrategy lsStrategy;
	DequeingStrategy dqStrategy;
	int xCoor;
	int yCoor;
	
	Junction(String id, LightSwitchStrategy lsStrategy, DequeingStrategy
			dqStrategy, int xCoor, int yCoor) {
			super(id);
			if( xCoor < 0 || yCoor < 0 || dqStrategy == null ||lsStrategy == null) {
				throw new IllegalArgumentException("Wrong arguments for Junction");
			}
			else {
				this.xCoor = xCoor;
				this.yCoor = yCoor;
				this.dqStrategy = dqStrategy;
				this.lsStrategy = lsStrategy;
				incomingRoads = new ArrayList<Road>();
				outgoingRoads = new HashMap<Junction, Road>();
				queuesList = new ArrayList<List<Vehicle>>();
				roadQueueMap = new HashMap<Road, List<Vehicle>>();
				currGreen = -1;
				lastSwitchingTime = 0;
			}
		}
	
	void addIncomingRoad(Road r) {
		if(r.getDestJunc() == this) {
			incomingRoads.add(r);
			List<Vehicle> queue = new LinkedList<Vehicle>();
			queuesList.add(queue);
			roadQueueMap.put(r, queue);
		}
		else {
			throw new IllegalArgumentException("Road does not end in this junction");
		}
	}
	
	void addOutGoingRoad(Road r) {
		if(r.getSrcJunc() == this && !outgoingRoads.containsKey(r.getDestJunc())) {
			outgoingRoads.put(r.getDestJunc(), r);
		}
		else {
			throw new IllegalArgumentException("Error in outgoins roads");
		}
	}
	
	void enter(Vehicle v) {
		roadQueueMap.get(v.getRoad()).add(v);
	}
	
	Road roadTo(Junction j) {
		return outgoingRoads.get(j);
	}
	
	@Override
	void advance(int time) {
		Road greenLightRoad = incomingRoads.get(currGreen);
		List<Vehicle> dq = dqStrategy.dequeue(roadQueueMap.get(greenLightRoad));
		for(Vehicle v : dq) {
			v.moveToNextRoad();
			roadQueueMap.get(greenLightRoad).remove(v);
			//con esto ya se quita de la List<Vehicle>?
		}
		int nextGreen = lsStrategy.chooseNextGreen(incomingRoads, queuesList, 
				currGreen, lastSwitchingTime, time);
		if(nextGreen != currGreen) {
			currGreen = nextGreen;
			lastSwitchingTime = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject j = new JSONObject();
		JSONObject jSub;
		JSONArray ja = new JSONArray();
		JSONArray jaSub = new JSONArray();

		j.put("id", _id);
		if(currGreen == -1)
			j.put("green", "none");
		else
			j.put("green", incomingRoads.get(currGreen).getId());
		for(int i = 0; i < incomingRoads.size(); i++) {
			jSub = new JSONObject();
			jSub.put("road", incomingRoads.get(i).getId());
			for(Vehicle v: queuesList.get(i)) {
				jaSub = new JSONArray();
				jaSub.put(v.getId());
			}
			jSub.put("vehicles", jaSub);
			ja.put(jSub);
		}
		j.put("queues", ja);
		return j;
	}
}

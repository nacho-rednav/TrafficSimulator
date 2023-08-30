package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchStrategy{
	
	int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		
		if(roads.isEmpty())
			return -1;
		else if(currGreen == -1)
			return longestQueue(qs, 0);
		else if(currTime-lastSwitchingTime <timeSlot)
			return currGreen;
		else
			return longestQueue(qs, ((currGreen+1) % roads.size()));
	}
	
	private int longestQueue(List<List<Vehicle>> qs, int start) {
		int index = 0;
		int longest = qs.get(index).size();
		
		for(int i = start; i < qs.size(); i++) {
			if(qs.get(i).size() > longest) {
				longest = qs.get(i).size();
				index = i;
			}
		}
		if(start != 0) {
			for(int i = 0; i < start; i++) {
				if(qs.get(i).size() > longest) {
					longest = qs.get(i).size();
					index = i;
				}
			}
		}
		
		return index;
	}

}

package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeingStrategy{

	private int initial = 0;
	
	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> r = new ArrayList<Vehicle>();
		r.add(q.get(initial));
		return r;
	}

}

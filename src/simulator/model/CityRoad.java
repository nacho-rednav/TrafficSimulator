package simulator.model;

public class CityRoad extends Road{
	
	private static int windyStormX = 10;
	private static int defaultX = 2;
	
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, 
			int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		totalCO2 = totalCO2 - calculateX();
		if(totalCO2 < 0)
			totalCO2 = 0;
	}

	@Override
	void updateSpeedLimit() {
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((11 - v.getContClass()) * currentMaxSpeed) / 11;
	}
	
	private int calculateX() {
		if(Weather.WINDY.equals(weather) || Weather.STORM.equals(weather))
			return windyStormX;
		else {
			return defaultX;
		}
	}
	
}

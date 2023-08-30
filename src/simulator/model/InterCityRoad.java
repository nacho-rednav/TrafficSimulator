package simulator.model;

public class InterCityRoad extends Road{
	
	private static int sunnyX = 2;
	private static int cloudyX = 3;
	private static int rainyX = 10;
	private static int windyX = 15;
	private static int stormX = 20;
	
	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		totalCO2 = ((100 - calculateX())*totalCO2)/100;
	}

	@Override
	void updateSpeedLimit() {
		if(totalCO2 > contLimit)
			currentMaxSpeed = maxSpeed / 2;
		else {
			currentMaxSpeed = maxSpeed;
		}
		
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(Weather.STORM.equals(weather)) {
			return (currentMaxSpeed*8)/10;
		}
		else {
			return currentMaxSpeed;
		}
	}
	
	private int calculateX() {
		int tc = 0;
		if(Weather.SUNNY.equals(weather))tc = sunnyX;
		else if(Weather.CLOUDY.equals(weather))tc = cloudyX;
		else if(Weather.RAINY.equals(weather))tc = rainyX;
		else if(Weather.WINDY.equals(weather))tc = windyX;
		else if(Weather.STORM.equals(weather))tc = stormX;
		return tc;
	}
}

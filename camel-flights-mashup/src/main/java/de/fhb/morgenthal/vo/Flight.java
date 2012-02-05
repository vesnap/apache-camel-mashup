package de.fhb.morgenthal.vo;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("flight")
@XmlRootElement
public class Flight {
	
	private String number;
	private ArrayList<Object> payload;
	
	private String hexcode;
	private String latidude;
	private String longitude; 
	private String country;
	private String unixTimestamp;
	private String weather;
	private String weather_Degree;
	private String weatherIconUrl, weatherDesc, windspeedKmph, winddir16Point, cloudcover, pressure, visibility, humidity, precipMM;
	private String places;
	
	public void setPlaces(String places){
		this.places=places;
	}
	public String getPlaces(){
		return places;
	}
	public String getWeather_Degree() {
		return weather_Degree;
	}
	public void setWeather_Degree(String weather_Degree) {
		this.weather_Degree = weather_Degree;
	}

	public String getWeatherIconUrl() {
		return weatherIconUrl;
	}
	public void setWeatherIconUrl(String weatherIconUrl) {
		this.weatherIconUrl = weatherIconUrl;
	}
	public String getWeatherDesc() {
		return weatherDesc;
	}
	public void setWeatherDesc(String weatherDesc) {
		this.weatherDesc = weatherDesc;
	}
	public String getWindspeedKmph() {
		return windspeedKmph;
	}
	public void setWindspeedKmph(String windspeedKmph) {
		this.windspeedKmph = windspeedKmph;
	}
	public String getWinddir16Point() {
		return winddir16Point;
	}
	public void setWinddir16Point(String winddir16Point) {
		this.winddir16Point = winddir16Point;
	}
	public String getCloudcover() {
		return cloudcover;
	}
	public void setCloudcover(String cloudcover) {
		this.cloudcover = cloudcover;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getPrecipMM() {
		return precipMM;
	}
	public void setPrecipMM(String precipMM) {
		this.precipMM = precipMM;
	}

	private String airport;
	public String getAirport() {
		return airport;
	}
	public void setAirport(String airport) {
		this.airport = airport;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}

	private String Track,Altitude,Speed,Squawk,RadarType,PlaneType, FlugzeugID;	
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getNumber() {
		return number;
	}
	public String getHexcode() {
		return hexcode;
	}
	public void setHexcode(String hexcode) {
		this.hexcode = hexcode;
	}
	public String getLatidude() {
		return latidude;
	}
	public void setLatidude(String latidude) {
		this.latidude = latidude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getTrack() {
		return Track;
	}
	public void setTrack(String track) {
		Track = track;
	}
	public String getAltitude() {
		return Altitude;
	}
	public void setAltitude(String altitude) {
		Altitude = altitude;
	}
	public String getSpeed() {
		return Speed;
	}
	public void setSpeed(String speed) {
		Speed = speed;
	}
	public String getSquawk() {
		return Squawk;
	}
	public void setSquawk(String squawk) {
		Squawk = squawk;
	}
	public String getRadarType() {
		return RadarType;
	}
	public void setRadarType(String radarType) {
		RadarType = radarType;
	}
	public String getPlaneType() {
		return PlaneType;
	}
	public void setPlaneType(String planeType) {
		PlaneType = planeType;
	}
	public String getFlugzeugID() {
		return FlugzeugID;
	}
	public void setFlugzeugID(String flugzeugID) {
		FlugzeugID = flugzeugID;
	}
	public String getUnixTimestamp() {
		return unixTimestamp;
	}
	public void setUnixTimestamp(String unixTimestamp) {
		this.unixTimestamp = unixTimestamp;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(ArrayList<Object> payload) {
		this.payload = payload;
		
		hexcode = payload.get(0).toString();
		latidude = payload.get(1).toString();
		longitude = payload.get(2).toString();
		Track= payload.get(3).toString();
		Altitude = payload.get(4).toString();
		Speed = payload.get(5).toString();
		Squawk = payload.get(6).toString();
		RadarType = payload.get(7).toString();
		PlaneType = payload.get(8).toString();
		FlugzeugID = payload.get(9).toString();
		unixTimestamp = payload.get(10).toString();
		number = payload.get(11).toString();
	}
	
	  @Override
	  public String toString() {
	    return "Flight - " 
	      + " Number: " + number
	      + " Hexcode: " + hexcode 
	      + " Latidude: " + latidude
	      + " Longitude: " + longitude
	      + " Track: " + Track
	      + " Altitude: " + Altitude
	      + " Speed: " + Speed
	      + " Squawk: " + Squawk
	      + " RadarType: " + RadarType
	      + " PlaneType: " + PlaneType
	      + " FlugzeugID: " + FlugzeugID
	      + " UnixTimestamp: " + unixTimestamp;
	  }
}

package it.polito.tdp.flight.model;

import java.util.HashMap;

public class AirlineIdMap extends HashMap<Integer,Airline>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Airline get(Airline airline) {
		Airline old = super.get(airline.getAirlineId());
		
		if(old!=null) {
			return old;
		}
		super.put(airline.getAirlineId(), airline);
		
		return airline;
	}

}
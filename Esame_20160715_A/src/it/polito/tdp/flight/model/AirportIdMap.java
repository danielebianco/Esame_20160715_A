package it.polito.tdp.flight.model;

import java.util.HashMap;

public class AirportIdMap extends HashMap<Integer,Airport>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Airport get(Airport airport) {
		Airport old = super.get(airport.getAirportId());
		
		if(old!=null) {
			return old;
		}
		super.put(airport.getAirportId(), airport);
		
		return airport;
	}

}

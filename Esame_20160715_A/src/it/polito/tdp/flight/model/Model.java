package it.polito.tdp.flight.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {
	private List<Airline> airlines;
	private List<Airport> airports;
	private FlightDAO dao;
	private SimpleDirectedWeightedGraph<Airport,DefaultWeightedEdge> grafo;
	private AirportIdMap airportIdMap;
	private AirlineIdMap airlineIdMap;
		
	public Model() {
		this.airportIdMap = new AirportIdMap();
		this.airlineIdMap = new AirlineIdMap();
		this.dao = new FlightDAO();
		this.airlines = dao.getAllAirlines(airlineIdMap);
		this.airports = dao.getAllAirports(airportIdMap);
		
	}

	public void creaGrafo(int distanzaMax) { // VERSIONE B: int distanzaMin
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, airports);
		for (Route f : dao.getAllRoutes(airlineIdMap)) {
			Airline ar = this.airlineIdMap.get(f.getAirlineId());
			Airport a1 = this.airportIdMap.get(f.getSourceAirportId());
			Airport a2 = this.airportIdMap.get(f.getDestinationAirportId());
			if(ar != null && a1 != null && a2 != null && !a1.equals(a2)) {
				double distanza = LatLngTool.distance(new LatLng(a1.getLatitude(), a1.getLongitude()),
						new LatLng(a2.getLatitude(), a2.getLongitude()), LengthUnit.KILOMETER);
				if(distanza < distanzaMax) { // VERSIONE B: distanza > distanzaMin
					double peso = distanza/800; // VERSIONE B: peso = distanza/900 
					Graphs.addEdge(grafo, a1, a2, peso);
				}
			}
		}
		
		
	}
	
	public List<Airport> mostraRaggiungibili(Airport airport) {
		
		Airport start = airportIdMap.get(airport); // trova il vertice di partenza
		
		List<Airport> visitati = new ArrayList<>(); // visita il grafo
		
		BreadthFirstIterator<Airport, DefaultWeightedEdge> dfv = new BreadthFirstIterator<>(this.grafo, start);
		
		dfv.next(); // salto il primo stato (quello di partenza)
		
		while (dfv.hasNext())
			visitati.add(dfv.next());

		return visitati;
	}
	
	// VERSIONE B: mostraNonRaggPiuVicino
	public Airport mostraPiuLontano() { 
		Airport fiumicino = this.airportIdMap.get(1555); // VERSIONE B: LA = airportIdMap.get(3484)
		Airport result = null;
		double distanzaMax = Double.MIN_VALUE; // VERSIONE B: distanzaMin = Double.MAX_VALUE;
		// VERSIONE B: creo List<Airport> nonRagg e rimuovo gli oggetti della lista raggiungibili
		//             e sostituisco LA a Fiumicino 
		for (Airport a : this.mostraRaggiungibili(fiumicino)) {
			double distanzaDaFM = LatLngTool.distance(new LatLng(fiumicino.getLatitude(), fiumicino.getLongitude()),
					new LatLng(a.getLatitude(), a.getLongitude()), LengthUnit.KILOMETER);
			if(distanzaDaFM > distanzaMax) { // VERSIONE B: distanzaDaLA < distanzaMin
				distanzaMax = distanzaDaFM;  // VERSIONE B: distanzaMin = distanzaDaLA
				result = a;
			}			
		}
		return result;
	}
	
	public List<Airline> getAirlines() {
		return airlines;
	}

	public List<Airport> getAirports() {
		return airports;
	}

	public SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public AirportIdMap getAirportIdMap() {
		return airportIdMap;
	}
	
	
}

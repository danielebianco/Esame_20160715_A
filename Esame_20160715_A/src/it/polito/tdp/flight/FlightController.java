package it.polito.tdp.flight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtDistanzaInput;

	@FXML
	private TextField txtPasseggeriInput;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCreaGrafo(ActionEvent event) {
		
		txtResult.clear();
		
		try {
			
			boolean rag = false;
			boolean route = false;
			
			int distanzaMax = Integer.parseInt(this.txtDistanzaInput.getText());
			this.model.creaGrafo(distanzaMax);
			
			this.txtResult.appendText(String.format("GOOD!!\n\nGrafo creato: %d vertici %d archi\n",
					model.getGrafo().vertexSet().size(), model.getGrafo().edgeSet().size()));
			
			for(Airport airport : model.getGrafo().vertexSet()) {
				List<Airport> prova = model.mostraRaggiungibili(airport);
					if(prova.size() == model.getGrafo().edgeSet().size()) {
						if(model.getGrafo().edgeSet().size()==0) {
							route = true;
						}
						else {
							rag = true;
						}
						
					}
			}
			
			if(rag == true) {
				this.txtResult.appendText("\nNel grafo ottenuto è possibile da ogni aeroporto raggiungere ogni altro aeroporto.\n");
			} else {
				this.txtResult.appendText("\nNel grafo ottenuto NON è possibile da ogni aeroporto raggiungere ogni altro aeroporto.\n");
			}
			
			if (route == true) {
				this.txtResult.appendText("\nQuesto grafo contiene aeroporti con 0 rotte.\n");
			} else {
				Airport fiumicino = model.getAirportIdMap().get(1555);
				List<Airport> raggiungibili = model.mostraRaggiungibili(fiumicino);
				if(raggiungibili.isEmpty()) {
		    		this.txtResult.appendText("\nLe rotte per Fiumicino non sono presenti in questo grafo.\n");
		    	} else {
			    	txtResult.appendText("\nL'aeroporto più lontano raggiungibile da " + fiumicino +
			    			" è: " + raggiungibili.get(raggiungibili.size()-1));
		    	}
			}
									
		} catch (RuntimeException e) {
			this.txtResult.setText("\n@!?@!?@!?@!?@!?@!? -> Errore: input non valido.\n");
		}
	}

	@FXML
	void doSimula(ActionEvent event) {
		
	}

	@FXML
	void initialize() {
		assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Untitled'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	public Model getModel() {
		return model;
	}
}

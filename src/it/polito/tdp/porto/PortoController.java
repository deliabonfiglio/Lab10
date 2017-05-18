package it.polito.tdp.porto;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	Author author = boxPrimo.getValue();
    	if(author == null){
    		txtResult.setText("Scegliere un autore!\n");
    		return;
    	}
    	
    	model.createGraph();
    	
    	txtResult.clear();
    	boxSecondo.getItems().clear();
		boxSecondo.getItems().addAll(model.getAuthorsNotCoauthors(author));

    	txtResult.appendText("Coautori dell'autore selezionato: \n");
    	for(Author a : model.getNeighbours(author))
    		txtResult.appendText(a.toString());
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	Author a1 = boxPrimo.getValue();
    	Author a2 = boxSecondo.getValue();
    	
    	if(a1== null || a2== null){
    		txtResult.setText("Selezionare entrambi gli autori\n");
    		return;
    	}
    	if(model.getShortestPath(a1, a2)!=null){
    		Set<Paper> paperstemp= model.getShortestPath(a1, a2);
    		txtResult.appendText("\nSequenza di articoli tra i due autori selezionati: \n");
		    	for (Paper ptemp : paperstemp)
		    		txtResult.appendText(ptemp.toString()+"\n");
    	} else{
    		txtResult.appendText("Non esiste un cammino tra gli autori selezionati\n");
    		return;
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model=model;
		boxPrimo.getItems().addAll(model.getAuthors());
	}
}

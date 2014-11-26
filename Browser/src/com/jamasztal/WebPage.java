package com.jamasztal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebPage extends Tab{
	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();
	private String webPage;
	VBox vb = new VBox();
	
	public WebPage(String w){
		webPage = w;
		setText(webPage);
		
		vb.setPadding(new Insets(0));
		vb.setSpacing(0);
		vb.setAlignment(Pos.CENTER);
		
		setContent(vb);
		
		vb.getChildren().add( browser );
		browser.prefWidthProperty().bind(vb.widthProperty());
		webEngine.load(webPage);	
	}
	
	public void goToPage(String p){
		webPage = p;
		webEngine.load(webPage);
		setText(webPage);
	}
	
	public void goBack(){webEngine.executeScript( "history.back()" );}
	
	public void goForward(){webEngine.executeScript( "history.forward()" );}
}

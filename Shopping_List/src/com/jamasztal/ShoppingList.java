package com.jamasztal;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/*
 * This class defines a ShoppingList application
 * 
 * @author Jan Masztal
 */
public class ShoppingList extends Application{

	/**
	 *   Run the application.
	 *   Note: Application is a Process, only one allowed per VM.
	 */
	public static void main(String[] args) 
	{
		launch(args);
	}


	@Override
	public void start(Stage theStage) throws Exception {
		theStage.setTitle("Shopping List");

		Group root = new Group();
		Scene scene = new Scene(root);

		VBox mainBox = new VBox();
		mainBox.setPadding(new Insets(40,10,10,10));
		mainBox.setSpacing(10);
		mainBox.setAlignment(Pos.CENTER);

		root.getChildren().add(mainBox);

		final ObservableList<Item> itemList = FXCollections.observableArrayList();

		itemList.add( new Item("Soda",5.00,9));
		itemList.add( new Item("Turkey",1.99,9) );

		// VIEW 
		TableView<Item> itemTable = new TableView<Item>();

		itemTable.setItems(itemList);
		itemTable.setEditable(true);
		mainBox.getChildren().add( itemTable );

		// First column!

		TableColumn<Item, String> firstColumn = new TableColumn<Item,String>("Item");

		firstColumn.setPrefWidth(128);

		firstColumn.setCellValueFactory( 
				new PropertyValueFactory<Item, String>("name")
			);

		// new functionality!

		firstColumn.setCellFactory( TextFieldTableCell.forTableColumn() );

		firstColumn.setOnEditCommit(
				new EventHandler< CellEditEvent<Item, String> >() 
				{
					public void handle( CellEditEvent<Item, String> t ) 
					{
						int rowNum = t.getTablePosition().getRow();
						Item i = (Item) t.getTableView().getItems().get(rowNum);
						String s = t.getNewValue();
						i.setName( s );
					}
				}
				);
	
		itemTable.getColumns().add(firstColumn);

		// Second column!

		TableColumn<Item,Double> priceColumn = new TableColumn<Item, Double>("Price");

		priceColumn.setPrefWidth(128);

		priceColumn.setCellValueFactory( 
				new PropertyValueFactory<Item, Double>("price")
				);

		// new functionality!

		priceColumn.setCellFactory(TextFieldTableCell.<Item,Double>forTableColumn(new DoubleStringConverter()) );

		priceColumn.setOnEditCommit(
				new EventHandler< CellEditEvent<Item, Double> >() 
				{
					public void handle( CellEditEvent<Item, Double> t ) 
					{
						int rowNum = t.getTablePosition().getRow();
						Item i = (Item) t.getTableView().getItems().get(rowNum);
						double p = t.getNewValue();
						i.setPrice(p);
					}

			
				}
				);

		itemTable.getColumns().add(priceColumn);

		TableColumn<Item,Integer> qtyColumn = new TableColumn<Item, Integer>("QTY");

		qtyColumn.setPrefWidth(128);

		qtyColumn.setCellValueFactory( 
				new PropertyValueFactory<Item, Integer>("qty")
				);

		// new functionality!

		qtyColumn.setCellFactory(TextFieldTableCell.<Item,Integer>forTableColumn(new IntegerStringConverter()) );

		qtyColumn.setOnEditCommit(
				new EventHandler< CellEditEvent<Item, Integer> >() 
				{
					public void handle( CellEditEvent<Item, Integer> t ) 
					{
						int rowNum = t.getTablePosition().getRow();
						Item i = (Item) t.getTableView().getItems().get(rowNum);
						int q = t.getNewValue();
						i.setPrice(q);
					}

			
				}
				);

		itemTable.getColumns().add(qtyColumn);

		// manual entry

		final TextField itemNameField = new TextField();
		itemNameField.setPromptText("Item Name");

		final TextField priceField = new TextField();
		priceField.setPromptText("Price");

		final TextField qtyField = new TextField();
		qtyField.setPromptText("qty");
		
		Button itemButton = new Button();
		itemButton.setText("add item");
		itemButton.setPrefWidth( 100 );
		itemButton.setOnAction( new EventHandler<ActionEvent>()
						{
					public void handle(ActionEvent ae)
					{
						String n = itemNameField.getText();
						double p = Double.parseDouble(priceField.getText());
						int q = Integer.parseInt(qtyField.getText());
						Item i = new Item(n,p,q);
						itemList.add( i );
					}
						}
						);
						

		mainBox.getChildren().addAll( itemNameField, priceField, qtyField, itemButton );



		theStage.setScene(scene);
		theStage.show();


	}

}

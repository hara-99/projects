package application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ChuiceBox extends Application {

    public static void main(String[] args) {
        launch(args);  // Needed to run JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // TODO Auto-generated method stub
    	ChoiceBox<String> choiceBox = new ChoiceBox<>();
    	ObservableList<String> list = choiceBox.getItems();
    	list.addAll("C","C++","Java","Python","Scala");
    	BorderPane root = new BorderPane();
    	root.setCenter(choiceBox);
    	choiceBox.setOnAction(e->{
    		System.out.println(choiceBox.getValue().toString()); // here the choice box items are printed on the console
    	});		
    	Scene scene = new Scene(root,300,300);		
    	primaryStage.setScene(scene);
    	primaryStage.setTitle("ChoiceBox Examples");
    	primaryStage.show();
    }
}

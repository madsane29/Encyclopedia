package encyclopedia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="@FXML items">
    @FXML
    Pane tablePane;
    @FXML
    Pane firstPane;
    @FXML
    Pane secondPane;
    @FXML
    Pane thirdPane;

    @FXML
    TableView table;

    @FXML
    TextField inputSearch;
    @FXML
    TextField inputName;
    @FXML
    TextField inputLifespan;
    @FXML
    TextField inputColor;
    @FXML
    TextField inputSound;
    @FXML
    TextField inputEditName;
    @FXML
    TextField inputEditLifespan;
    @FXML
    TextField inputEditColor;
    @FXML
    TextField inputEditSound;

    @FXML
    Button addButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Button searchButton;
    @FXML
    Button lastAddButton;
    @FXML
    Button lastEditButton;
    @FXML
    Button exitButton;
//</editor-fold>

    DB db = new DB();
    String tempID;
    private final ObservableList<Animal> data = FXCollections.observableArrayList();

    @FXML
    private void addButton(ActionEvent event) {
        transition(firstPane, 1, 0.3, 200);
        secondPane.setVisible(true);
        transition(secondPane, 0.1, 1, 200);
    }

    @FXML
    private void exitButton(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void editButton(ActionEvent event) {
        transition(firstPane, 1, 0.3, 200);

        if (table.getSelectionModel().getSelectedIndex() != -1) {
            thirdPane.setVisible(true);
            transition(thirdPane, 0.3, 1, 200);

            Animal tmp = (Animal) table.getSelectionModel().getSelectedItem();
            inputEditName.setText(tmp.getName());
            inputEditLifespan.setText(tmp.getLifespan());
            inputEditColor.setText(tmp.getColor());
            inputEditSound.setText(tmp.getSound());
            tempID = tmp.getID();

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("You did not select any data!");
            alert.setTitle("Error");
            alert.showAndWait();
            transition(firstPane, 0.3, 1, 200);
        }
    }

    @FXML
    private void lastEditButton(ActionEvent event) {
        String name = inputEditName.getText();
        String lifespan = inputEditLifespan.getText();
        String color = inputEditColor.getText();
        String sound = inputEditSound.getText();
        Animal tmp = new Animal(name, lifespan, color, sound, tempID);

        /*db.updateAnimal(tmp);
        data.clear();
        data.addAll(db.getAllAnimals());
        table.setItems(data);*/
        db.updateAnimal(tmp);
        data.removeIf((Animal dataAnimal) -> dataAnimal.getID().equals(tmp.getID()));
        data.add(tmp);
        table.setItems(data);

        thirdPane.setVisible(false);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Informations edited!");
        alert.setHeaderText(null);
        alert.setTitle("Edited");
        alert.showAndWait();

        transition(firstPane, 0.3, 1, 200);

    }

    @FXML
    private void deleteButton(ActionEvent event) {

        Animal tmp = (Animal) table.getSelectionModel().getSelectedItem();
        transition(firstPane, 1, 0.3, 200);
        if (table.getSelectionModel().getSelectedIndex() != -1) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("You are about to delete a record!");
            alert.setContentText("Are you sure about deleting the selected record?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                db.deleteAnimal(Integer.parseInt(tmp.getID()));
                table.getItems().remove(table.getSelectionModel().getSelectedItem());
                data.remove(tmp);
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("You did not choose any data!");
            alert.showAndWait();
        }

        transition(firstPane, 0.3, 1, 200);
    }

    @FXML
    private void searchButton(ActionEvent event) {
        ObservableList<Animal> temp_data = FXCollections.observableArrayList();
        String input = inputSearch.getText().toLowerCase();

        if (!input.equals("")) {
            for (Animal tmp : data) {
                if (tmp.getName().toLowerCase().contains(input)) {
                    temp_data.add(tmp);
                }
            }
            table.setItems(temp_data);
        } else {
            temp_data.addAll(db.getAllAnimals());
            table.setItems(temp_data);
        }
    }

    @FXML
    private void lastAddButton(ActionEvent event) {
        String name = inputName.getText();
        String lifespan = inputLifespan.getText();
        String color = inputColor.getText();
        String sound = inputSound.getText();
        Animal tmp = new Animal(name, lifespan, color, sound);

        db.addAnimal(tmp);
        data.clear();
        data.addAll(db.getAllAnimals());
        table.setItems(data);

        secondPane.setVisible(false);

        inputName.setText("");
        inputLifespan.setText("");
        inputColor.setText("");
        inputSound.setText("");

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Informations added to database!");
        alert.setHeaderText(null);
        alert.setTitle("Record added");
        alert.showAndWait();

        transition(firstPane, 0.3, 1, 200);

    }

    @FXML
    private void cancelButton(ActionEvent event) {
        secondPane.setVisible(false);
        thirdPane.setVisible(false);
        transition(firstPane, 0.3, 1, 200);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableColumns();

    }

    public void tableColumns() {
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setMaxWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn lifespanCol = new TableColumn("Lifespan");
        lifespanCol.setMinWidth(100);
        lifespanCol.setMaxWidth(200);
        lifespanCol.setCellValueFactory(new PropertyValueFactory<>("lifespan"));

        TableColumn colorCol = new TableColumn("Color");
        colorCol.setMinWidth(100);
        colorCol.setMaxWidth(200);
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));

        TableColumn soundCol = new TableColumn("Sound");
        soundCol.setMinWidth(100);
        soundCol.setMaxWidth(200);
        soundCol.setCellValueFactory(new PropertyValueFactory<>("sound"));

        TableColumn IDCol = new TableColumn("ID");
        IDCol.setMinWidth(50);
        IDCol.setMaxWidth(200);
        IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));

        table.getColumns().addAll(IDCol, nameCol, lifespanCol, colorCol, soundCol);
        addButtonToTable();
        data.addAll(db.getAllAnimals());
        table.setItems(data);
    }

    public void transition(Node whatToTransition, double from, double to, int millisDuration) {
        FadeTransition tmp = new FadeTransition();
        tmp.setDuration(Duration.millis(millisDuration));
        tmp.setNode(whatToTransition);
        tmp.setFromValue(from);
        tmp.setToValue(to);
        tmp.play();
    }

    private void addButtonToTable() {
        TableColumn<Animal, Void> playSoundCol = new TableColumn("Play sound");
        double prefWidth = playSoundCol.getPrefWidth();
        playSoundCol.setMinWidth(prefWidth);
        playSoundCol.setMaxWidth(prefWidth);
        playSoundCol.setStyle("-fx-alignment: CENTER");
        
        
        Callback<TableColumn<Animal, Void>, TableCell<Animal, Void>> cellFactory = new Callback<TableColumn<Animal, Void>, TableCell<Animal, Void>>() {
            @Override
            public TableCell<Animal, Void> call(final TableColumn<Animal, Void> param) {
                final TableCell<Animal, Void> cell = new TableCell<Animal, Void>() {

                    private final Button btn = new Button("Play");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            String fileName = "C:\\Users\\Petkovics\\Desktop\\newproject\\Encyclopedia\\src\\encyclopedia\\waterdrop.mp3";
                            Sound sound = new Sound();
                            sound.play(fileName);
                            
                           
                            
                            

                            Animal data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                        });
                    }

                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        playSoundCol.setCellFactory(cellFactory);

        table.getColumns().add(playSoundCol);

    }

}

package com.example;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrimaryController {
    private Stage stage;
    private ObservableList<String> lista;
    private List<String> nombresPlantillas;
    private String plantillaSeleccionada;
    
    @FXML
    private TabPane tabpane;
    @FXML
    private Button borrarButton;

    @FXML
    private Button borrarPlantillaButton;

    @FXML
    private Button plantillaButton;

    @FXML
    private Button borrarButton1;

    @FXML
    private Button plantillaButton1;

    @FXML
    private Button directoryChooser;

    @FXML
    private Button borrarMetadatoButton;

    @FXML
    private Button metadatoPersButton;

    @FXML
    private Button anadirButton;

    @FXML
    private Label carpetaLabel;

    @FXML
    private Label archivoLabel;

    @FXML
    private Label numArchivosLabel;

    @FXML
    private Label errorAnadir;
    
    @FXML
    private CheckBox incluirBox;

    @FXML
    private ListView<String> list;

    @FXML
    private TableView<PlantillaMetadata> tabla;

    @FXML
    private TableView<PlantillaMetadata> tabla1;

    @FXML
    private TableColumn<PlantillaMetadata, String> nombreColumn;

    @FXML
    private TableColumn<PlantillaMetadata, String> valorColumn;

    @FXML
    private TableColumn<PlantillaMetadata, String> nombreColumn1;

    @FXML
    private TableColumn<PlantillaMetadata, String> valorColumn1;

    @FXML
    private TextField nombrePlantilla;

    @FXML
    private Label labelCompleto;

    private Stage popupWindow;

    
    private File carpeta;

    private File archivo;

    @FXML
    public void mostrarVentanaMetadato(){
        String[] valores = anadirMetadatoPersonalizado();
        if(valores[0] != "" && valores[1] != ""){
            PlantillasManager.anadirCustomMetadato(plantillaSeleccionada, valores[0], valores[1]);
            tabla1.getItems().add(new PlantillaMetadata((String) valores[0], (String) valores[1]));
        }
    }

    public String[] anadirMetadatoPersonalizado(){
        String[] valores = new String[] {"", ""};
        Stage popupStage = new Stage();
        TextField nombreField = new TextField();
        TextField valorField = new TextField();
        Label n = new Label("Nombre del metadato");
        Label v = new Label("Valor del metadato");
        
        Button confirmButton = new Button("Confirmar");
        confirmButton.setOnAction(e -> {
            String inputText = nombreField.getText();
            valores[0] = nombreField.getText();
            valores[1] = valorField.getText(); 
        });
        VBox popupLayout = new VBox(10, n, nombreField, v, valorField, confirmButton);
        popupLayout.setPadding(new javafx.geometry.Insets(10));
        Scene popupScene = new Scene(popupLayout, 600, 210);
        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL); //Bloquea la ventana principal hasta que se cierre la emergente
        popupStage.setTitle("Ventana Emergente");
        popupStage.showAndWait();

        return valores;
    }

    @FXML
    private void borrarMetadatosTodo() {
        try {
            new MetadataManager().deleteAll(carpeta, incluirBox.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarSeleccion(){
        String plantillaSeleccion = seleccionarPlantillaCarpeta();
        System.out.println(plantillaSeleccion);
        if(plantillaSeleccion != null){
            new MetadataManager().aplicarPlantilla(carpeta, plantillaSeleccion, incluirBox.isSelected());
        }
        labelCompleto.setVisible(true);
    }

    public String seleccionarPlantillaCarpeta(){
        popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Seleccionar plantilla");
        ListView<String> listView = new ListView<>();
        listView.setItems(lista);

        Button selectButton = new Button("Seleccionar");
        Button cancelButton = new Button("Cancelar");
        Button goPlantillasButton = new Button("Gestionar plantillas");
        final String[] selectedItem = new String[1];

        selectButton.setOnAction(event -> {
            selectedItem[0] = listView.getSelectionModel().getSelectedItem();
            popupWindow.close();
        });

        cancelButton.setOnAction(event -> {popupWindow.close();});
        goPlantillasButton.setOnAction(event -> {popupWindow.close();tabpane.getSelectionModel().select(2);});
        HBox container = new HBox(10, selectButton, goPlantillasButton, cancelButton);
        VBox dialogVBox = new VBox(10, listView, container);
        dialogVBox.setPadding(new Insets(0, 0, 20, 0));
        container.setPadding(new Insets( 0,0, 0, 10));
        Scene dialogScene = new Scene(dialogVBox, 600, 400);
        popupWindow.setScene(dialogScene);
        popupWindow.showAndWait();

        return selectedItem[0];
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void initialize(){
        try {
            nombresPlantillas = PlantillasManager.getNombresPlantillas();

            lista = FXCollections.observableList(nombresPlantillas);
            list.setItems(lista);
            list.setEditable(true);
            list.setCellFactory(TextFieldListCell.forListView());
            list.setOnEditCommit(event -> {
                PlantillasManager.editarNombrePlantilla(plantillaSeleccionada, event.getNewValue());
                int indice = list.getSelectionModel().getSelectedIndex();
                list.getItems().set(indice, event.getNewValue());
            });
            list.setOnMouseClicked(event -> {selectPlantilla(list.getSelectionModel().getSelectedItem());});

            nombreColumn.setCellValueFactory(new PropertyValueFactory<PlantillaMetadata, String>("name"));
            valorColumn.setCellValueFactory(new PropertyValueFactory<PlantillaMetadata, String>("value"));
            nombreColumn1.setCellValueFactory(new PropertyValueFactory<PlantillaMetadata, String>("name"));
            valorColumn1.setCellValueFactory(new PropertyValueFactory<PlantillaMetadata, String>("value"));
            valorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            valorColumn.setOnEditCommit(event -> {
                PlantillasManager.modificarValor(plantillaSeleccionada, event.getRowValue().getName(), event.getNewValue());
                event.getRowValue().setValue(event.getNewValue());
                System.out.println("A");
            
            });
            nombreColumn1.setCellFactory(TextFieldTableCell.forTableColumn());
            nombreColumn1.setOnEditCommit(event -> {
                PlantillasManager.modificarNombreMeta(plantillaSeleccionada, event.getOldValue(), event.getNewValue());
                System.out.println(event.getOldValue() + "  " + event.getNewValue());
                event.getRowValue().setName(event.getNewValue());
            
            });
            valorColumn1.setCellFactory(TextFieldTableCell.forTableColumn());
            valorColumn1.setOnEditCommit(event -> {
                PlantillasManager.modificarValorCustom(plantillaSeleccionada, event.getRowValue().getName(), event.getNewValue());
                event.getRowValue().setValue(event.getNewValue());
                System.out.println("A");
            
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    public void selectPlantilla(String nombre){
        tabla.getItems().clear();
        tabla1.getItems().clear();
        plantillaSeleccionada = nombre;
        JSONObject metadatos = PlantillasManager.getMetadatosPlantilla(plantillaSeleccionada);
        JSONObject custom = PlantillasManager.getCustomMetadatosPlantilla(plantillaSeleccionada);
        for (Object nombreMetadato : metadatos.keySet()) {
            System.out.println((String) nombreMetadato + " " + metadatos.get(nombreMetadato));
            tabla.getItems().add(new PlantillaMetadata((String) nombreMetadato, (String) metadatos.get(nombreMetadato)));
        }
        for (Object nombreMetadato : custom.keySet()) {
            System.out.println((String) nombreMetadato + " " + custom.get(nombreMetadato));
            tabla1.getItems().add(new PlantillaMetadata((String) nombreMetadato, (String) custom.get(nombreMetadato)));
        }
    }

    @FXML
    private void selectDirectory() throws IOException{
        DirectoryChooser dc = new DirectoryChooser();
        File selectedDirectory = dc.showDialog(stage);
        String path = selectedDirectory.getAbsolutePath();
        carpeta = new File(path);
        borrarButton.setDisable(false);
        plantillaButton.setDisable(false);
        borrarButton.setOpacity(1);
        plantillaButton.setOpacity(1);
        carpetaLabel.setText("Has seleccionado la carpeta: " + carpeta.getAbsolutePath());
        numArchivosLabel.setText("Hay " + contarArchivos(carpeta) + " archivos en la carpeta y subcarpetas");
        numArchivosLabel.setOpacity(1);
    }

    @FXML
    private void selectFile() {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(stage);
        String path = selectedFile.getAbsolutePath();
        archivo = new File(path);
        archivoLabel.setText("Has seleccionado el archivo: "+path);
        borrarButton1.setDisable(false);
        plantillaButton1.setDisable(false);
        borrarButton1.setOpacity(1);
        plantillaButton1.setOpacity(1);
    }

    @FXML
    private void anadirPlantilla(){
        try {
            System.out.println("ANADIENDO");
            if(nombresPlantillas.contains(nombrePlantilla.getText()) || nombrePlantilla.getText().isBlank()){
                errorAnadir.setVisible(true);
            }
            else{
                errorAnadir.setVisible(false);
                nombresPlantillas.add(nombrePlantilla.getText());
                list.setItems(FXCollections.observableList(nombresPlantillas));
                PlantillasManager.anadirPlantilla(nombrePlantilla.getText());
                nombrePlantilla.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void borrarPlantilla(){
        PlantillasManager.eliminarPlantilla(plantillaSeleccionada);
        list.getItems().remove(plantillaSeleccionada);
        tabla.getItems().clear();
    }

    private int contarArchivos(File carpeta){
        int num = 0;
        File[] archivos = carpeta.listFiles();
        for(File file: archivos){
            String[] partespath = file.getAbsolutePath().split("\\.");
            if(partespath.length == 1){num += contarArchivos(new File(partespath[0]));}
            else{num ++;}
        }
        return num;

    }
}

package edu.pucmm.smarthit.controller;

import edu.pucmm.smarthit.binding.MainViewBinding;
import edu.pucmm.smarthit.binding.PlayerModelBinding;
import edu.pucmm.smarthit.model.PlayerModel;
import edu.pucmm.smarthit.util.FileUtil;
import edu.pucmm.smarthit.util.Utilities;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static edu.pucmm.smarthit.util.FileUtil.PLAYERS_PATH;

public class PlayerViewController implements Initializable {

    private ObjectProperty<PlayerModel> playerModel = new SimpleObjectProperty<>(this, "playerModel", new PlayerModel());

    @FXML
    private VBox vBox;

    @FXML
    private Label lblTitle;

    @FXML
    private Button btnNewCode;

    @FXML
    private ComboBox<PlayerModel> comboBox;

    @FXML
    private TextField txtFieldCode;

    @FXML
    private TextField txtFieldName;

    @FXML
    private TextField txtFieldLastName;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField txtFieldWeight;

    @FXML
    private TextField txtFieldHeight;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnUpdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();

        txtFieldCode.setEditable(false);
        comboBox.itemsProperty().bindBidirectional(PlayerModelBinding.getInstance().listPropertyProperty());

        btnNewCode.setOnAction(e -> generateNewCode());

        comboBox.setOnAction(e -> playerSelected());

        btnUpdate.setOnAction(e -> updatePlayer());

        btnRegister.setOnAction(e -> registerPlayer());
    }

    private void generateNewCode() {
        txtFieldCode.setText(new Utilities().generateCode());
        txtFieldName.setText("");
        txtFieldLastName.setText("");
        datePicker.setValue(LocalDate.now());
        txtFieldHeight.setText("");
        txtFieldWeight.setText("");

        comboBox.selectionModelProperty().get().clearSelection();

        playerModel.set(new PlayerModel());

        PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().set(playerModel.get());
        MainViewBinding.getInstance().labelTitleProperty().set("NO EXISTE UN JUGADOR SELECCIONADO. SELECCIONE O REGISTRE UNO.");
        MainViewBinding.getInstance().playerSelectedProperty().setValue(Boolean.FALSE);
    }

    private void init() {
        comboBox.setConverter(new StringConverter<PlayerModel>() {
            Map<String, PlayerModel> map = new HashMap<>();

            @Override
            public String toString(PlayerModel object) {
                map.put(object.getCode(), object);
                return object.getNames() + " " + object.getLastNames();
            }

            @Override
            public PlayerModel fromString(String string) {
                return map.get(string);
            }
        });

        datePicker.setValue(LocalDate.of(1995, 02, 17));

        txtFieldCode.setText(new Utilities().generateCode());

        try {
            JSONArray jsonArray = readPlayers();

            for (int i = 0; i < jsonArray.length(); i++) {
                PlayerModel playerModel = new PlayerModel();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                playerModel.setCode(jsonObject.getString("code"));
                playerModel.setNames(jsonObject.getString("names"));
                playerModel.setLastNames(jsonObject.getString("lastNames"));
                playerModel.setBirthdate(LocalDate.parse(jsonObject.getString("birthdate")));
                playerModel.setWeight(jsonObject.getString("weight"));
                playerModel.setHeight(jsonObject.getString("height"));

                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + playerModel.getCode()));

                PlayerModelBinding.getInstance().listPropertyProperty().add(playerModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playerSelected() {
        playerModel.setValue(comboBox.valueProperty().get());

        if (playerModel.get() != null) {
            txtFieldCode.setText(playerModel.get().getCode());
            txtFieldName.setText(playerModel.get().getNames());
            txtFieldLastName.setText(playerModel.get().getLastNames());
            datePicker.setValue(playerModel.get().getBirthdate());
            txtFieldWeight.setText(playerModel.get().getWeight());
            txtFieldHeight.setText(playerModel.get().getHeight());

            PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().set(playerModel.get());
            MainViewBinding.getInstance().labelTitleProperty().set(playerModel.get().getNames() + " " + playerModel.get().getLastNames() + "(" + playerModel.get().getCode() + ")");

            new FileUtil().fetchFiles(new File(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode()), e -> {
                if (e.isFile() && FilenameUtils.getExtension(e.getAbsolutePath()).equals("json")) {
                    try {
                        JSONObject jsonObject = new JSONObject(new JSONTokener(new FileInputStream(new File(e.getAbsolutePath()))));

                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            MainViewBinding.getInstance().playerSelectedProperty().setValue(Boolean.TRUE);
        }
    }

    private void registerPlayer() {
        if (!PlayerModelBinding.getInstance().listPropertyProperty().contains(playerModel.get())) {
            txtFieldCode.setText(new Utilities().generateCode());

            PlayerModel playerModel = new PlayerModel();
            playerModel.setCode(txtFieldCode.getText());
            playerModel.setNames(txtFieldName.getText());
            playerModel.setLastNames(txtFieldLastName.getText());
            playerModel.setBirthdate(datePicker.getValue());
            playerModel.setWeight(txtFieldWeight.getText());
            playerModel.setHeight(txtFieldHeight.getText());

            try {
                Files.createDirectories(Paths.get(FileUtil.ROOT_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }

            PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().setValue(playerModel);

            JSONArray data = null;

            try {
                data = readPlayers();
            } catch (IOException e) {
                e.printStackTrace();

                data = new JSONArray();
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", playerModel.getCode());
            jsonObject.put("names", playerModel.getNames());
            jsonObject.put("lastNames", playerModel.getLastNames());
            jsonObject.put("birthdate", playerModel.getBirthdate());
            jsonObject.put("weight", playerModel.getWeight());
            jsonObject.put("height", playerModel.getHeight());

            data.put(jsonObject);

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(FileUtil.ROOT_PATH + "players" + ".json"));
                writer.append(data.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PlayerModelBinding.getInstance().listPropertyProperty().get().add(playerModel);
        } else {
            new Alert(Alert.AlertType.ERROR, "El jugador ya esta registrado.").show();
        }
    }

    private JSONArray readPlayers() throws IOException {
        return new JSONArray(new String(Files.readAllBytes(Paths.get(FileUtil.ROOT_PATH + "players" + ".json"))));
    }

    private void updatePlayer() {



        System.out.println(playerModel.toString());
    }
}

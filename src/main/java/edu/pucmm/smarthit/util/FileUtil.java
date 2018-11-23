package edu.pucmm.smarthit.util;

import edu.pucmm.smarthit.binding.DataModelBinding;
import edu.pucmm.smarthit.binding.PlayerModelBinding;
import edu.pucmm.smarthit.binding.SensorModelBinding;
import edu.pucmm.smarthit.model.SensorModel;
import javafx.collections.FXCollections;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

public class FileUtil {

    public final static String ROOT_PATH = "data/";
    public final static String PLAYERS_PATH = "data/players";

    public void saveDataToFile() {
        JSONArray data = new JSONArray();

        if (DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().get().size() > 10) {
            //Save data with no date and no time

            for (SensorModel sensorModel : DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().get()) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("accX", Double.parseDouble(sensorModel.getAcceleration().split(",")[0].trim()));
                jsonObject.put("accY", Double.parseDouble(sensorModel.getAcceleration().split(",")[1].trim()));
                jsonObject.put("accZ", Double.parseDouble(sensorModel.getAcceleration().split(",")[2].trim()));

                jsonObject.put("velX", Double.parseDouble(sensorModel.getAngularVelocity().split(",")[0].trim()));
                jsonObject.put("velY", Double.parseDouble(sensorModel.getAngularVelocity().split(",")[1].trim()));
                jsonObject.put("velZ", Double.parseDouble(sensorModel.getAngularVelocity().split(",")[2].trim()));

                jsonObject.put("angX", Double.parseDouble(sensorModel.getAngle().split(",")[0].trim()));
                jsonObject.put("angY", Double.parseDouble(sensorModel.getAngle().split(",")[1].trim()));
                jsonObject.put("angZ", Double.parseDouble(sensorModel.getAngle().split(",")[2].trim()));

                data.put(jsonObject);
            }

            BufferedWriter writer = null;
            try {
                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear()));
                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue()));
                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth()));
                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth() + "/r/"));
                writer = new BufferedWriter(new FileWriter(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth() +"/r/session_" + LocalTime.now().format(DateTimeFormatter.ofPattern("hhmmss")) + ".json"));
                writer.write(data.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Data saved ready for R...");

            //Save data with date and time

            data = new JSONArray();

            //Calculating estimated impact time
            float max = 0.0f;
            String minTime = DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().get(0).getTime();
            String maxTime = "";

            for (SensorModel sensorModel : DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().get()) {
                if (Float.parseFloat(sensorModel.accelerationProperty().get().split(",")[0]) > max) {
                    maxTime = sensorModel.getTime();
                    max = Float.parseFloat(sensorModel.accelerationProperty().get().split(",")[0]);
                }
            }

            JSONObject json = new JSONObject();

            json.put("impact", String.format(String.valueOf(LocalTime.parse(minTime, DateTimeFormatter.ofPattern("HH:mm:ss.SSS")).until(LocalTime.parse(maxTime, DateTimeFormatter.ofPattern("HH:mm:ss.SSS")), ChronoUnit.MILLIS)), "%10.2f"));

            //Calculating velocity before saving it to the file
            float velocity = 0.0f;
            float time = (float) json.getDouble("impact") / 1000.0f;
            float acceleration = max;

            velocity = 0 + (acceleration * time);

            json.put("velocity", velocity);

            for (SensorModel sensorModel : DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().get()) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("time", sensorModel.getTime());
                jsonObject.put("date", sensorModel.getDate());

                jsonObject.put("accX", Double.parseDouble(sensorModel.getAcceleration().split(",")[0].trim()));
                jsonObject.put("accY", Double.parseDouble(sensorModel.getAcceleration().split(",")[1].trim()));
                jsonObject.put("accZ", Double.parseDouble(sensorModel.getAcceleration().split(",")[2].trim()));

                jsonObject.put("velX", Double.parseDouble(sensorModel.getAngularVelocity().split(",")[0].trim()));
                jsonObject.put("velY", Double.parseDouble(sensorModel.getAngularVelocity().split(",")[1].trim()));
                jsonObject.put("velZ", Double.parseDouble(sensorModel.getAngularVelocity().split(",")[2].trim()));

                jsonObject.put("angX", Double.parseDouble(sensorModel.getAngle().split(",")[0].trim()));
                jsonObject.put("angY", Double.parseDouble(sensorModel.getAngle().split(",")[1].trim()));
                jsonObject.put("angZ", Double.parseDouble(sensorModel.getAngle().split(",")[2].trim()));

                data.put(jsonObject);
            }

            json.put("metrics", data);

            writer = null;
            try {
                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear()));
                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue()));
                Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth()));
                writer = new BufferedWriter(new FileWriter(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth() +"/session_" + LocalTime.now().format(DateTimeFormatter.ofPattern("hhmmss")) + ".json"));
                writer.write(json.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Data saved...");

            DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().clear();
            DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().set(FXCollections.observableArrayList());
            SensorModelBinding.getInstance().sensorModelPropertyProperty().set(new SensorModel());
        }
    }

    public void initDataDirectory() {
        try {
            Files.createDirectories(Paths.get(ROOT_PATH));
            Files.createDirectories(Paths.get(PLAYERS_PATH));
            Files.createDirectories(Paths.get(PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchFiles(File dir, Consumer<File> fileConsumer) {

        if (dir.isDirectory() && !dir.getName().contains("r")) {
            for (File file1 : dir.listFiles()) {
                fetchFiles(file1, fileConsumer);
            }
        } else {
            fileConsumer.accept(dir);
        }
    }
}

package edu.pucmm.smarthit.sensor;

import edu.pucmm.smarthit.binding.DataModelBinding;
import edu.pucmm.smarthit.binding.SensorModelBinding;
import edu.pucmm.smarthit.model.SensorModel;
import edu.pucmm.smarthit.util.FileUtil;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Sensor {
    CommPortIdentifier portId;
    Enumeration portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    private Queue<Byte> queueBuffer = new LinkedList<Byte>();
    private byte[] packBuffer = new byte[11];
    private float [] fData=new float[31];

    public Sensor() {
        System.out.println("Starting...");

        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();

            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("/dev/tty.HC-06-DevB")) {
                    try {
                        serialPort = (SerialPort) portId.open("SimpleReadApp", 3000);
                    } catch (PortInUseException e) {
                        e.printStackTrace();
                        serialPort.close();
                    }

                    try {
                        serialPort.notifyOnDataAvailable(true);
                    } catch (NullPointerException e) {
                        serialPort.close();
                        e.printStackTrace();
                    }

                    try {
                        serialPort.setSerialPortParams(115200,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        serialPort.close();
                        e.printStackTrace();
                    }

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        byte[] readBuffer = new byte[1024];
                        byte sHead;
                        int numBytes;
                        boolean loaded = false;

                        Date date;
                        boolean timeSetted = false;

                        ObservableList<SensorModel> list = FXCollections.observableArrayList();

                        @Override
                        public void run() {
                            try {
                                inputStream = serialPort.getInputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            SensorModel sensorModel = new SensorModel();

                            try {
                                numBytes = Objects.requireNonNull(inputStream).read(readBuffer);

                                for (int i = 0; i < numBytes; i++) {
                                    queueBuffer.add(readBuffer[i]);
                                }

                                while (queueBuffer.size() >= 11 && SensorModelBinding.getInstance().canReadPropertyProperty().get()) {
                                    if (queueBuffer.poll() != 0x55) continue;

                                    if (!timeSetted) {
                                        date = new Date();
                                        timeSetted = true;
                                    }

                                    sHead = queueBuffer.poll();

                                    for (int j = 0; j < 9; j++) packBuffer[j] = queueBuffer.poll();

                                    sensorModel.timeProperty().setValue(LocalTime.now().toString());
                                    sensorModel.dateProperty().setValue(LocalDate.now().toString());

                                    switch (sHead) {
                                        case 0x51:
                                            fData = getAcceleration(packBuffer);

                                            try {
                                                sensorModel.accelerationProperty().set(String.format("%10.2f", fData[0]) + "," + String.format("%10.2f", fData[1]) + "," + String.format("%10.2f", fData[2]));
                                            } catch (NullPointerException e) {
                                                sensorModel.accelerationProperty().set("0.00,0.00,0.00");
                                            }
                                            break;

                                        case 0x52:
                                            fData = getAngularVelocity(packBuffer);

                                            try {
                                                sensorModel.angularVelocityProperty().set(String.format("%10.2f", fData[0]) + "," + String.format("%10.2f", fData[1]) + "," + String.format("%10.2f", fData[2]));
                                            } catch (NullPointerException e) {
                                                sensorModel.angularVelocityProperty().set("0.00,0.00,0.00");
                                            }
                                            break;

                                        case 0x53:
                                            fData = getAngle(packBuffer);

                                            try {
                                                sensorModel.angleProperty().set(String.format("%10.2f", fData[0]) + "," + String.format("%10.2f", fData[1]) + "," + String.format("%10.2f", fData[2]));
                                            } catch (NullPointerException e) {
                                                sensorModel.angleProperty().set("0.00,0.00,0.00");
                                            }

                                            if (sensorModel.canSend()) {
                                                loaded = true;
                                                SensorModelBinding.getInstance().sensorModelPropertyProperty().set(sensorModel);
                                                DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().add(sensorModel);
                                                sensorModel = new SensorModel();
                                                date = new Date();
                                            } else if (new Date().getTime() - date.getTime() >= 2500) {
                                                timeSetted = false;
                                                new FileUtil().saveDataToFile();
                                            }
                                            break;
                                    }
                                }
                            } catch (IOException e) {e.printStackTrace();}
                        }
                    }, 0, 100);
                }
            }
        }
    }

    public float[] getAcceleration(byte[] buffer) {
        float [] fData = new float[31];

        fData[0] = Math.abs(((((short) buffer[1]) << 8) | ((short) buffer[0] & 0xff)) / 32768.0f * (16 * 9.8f));
        fData[1] = Math.abs(((((short) buffer[3]) << 8) | ((short) buffer[2] & 0xff)) / 32768.0f * (16 * 9.8f));
        fData[2] = Math.abs(((((short) buffer[5]) << 8) | ((short) buffer[4] & 0xff)) / 32768.0f * (16 * 9.8f));

        return fData;
    }

    public float[] getAngularVelocity(byte[] buffer) {
        float [] fData = new float[31];
        fData[0] = Math.abs(((((short) buffer[1]) << 8) | ((short) buffer[0] & 0xff)) / 32768.0f * 2000);
        fData[1] = Math.abs(((((short) buffer[3]) << 8) | ((short) buffer[2] & 0xff)) / 32768.0f * 2000);
        fData[2] = Math.abs(((((short) buffer[5]) << 8) | ((short) buffer[4] & 0xff)) / 32768.0f * 2000);

        return fData;
    }

    public float[] getAngle(byte[] buffer) {
        float [] fData = new float[31];
        fData[0] = Math.abs(((((short) buffer[1]) << 8) | ((short) buffer[0] & 0xff)) / 32768.0f * 180);
        fData[1] = Math.abs(((((short) buffer[3]) << 8) | ((short) buffer[2] & 0xff)) / 32768.0f * 180);
        fData[2] = Math.abs(((((short) buffer[5]) << 8) | ((short) buffer[4] & 0xff)) / 32768.0f * 180);

        return fData;
    }
}

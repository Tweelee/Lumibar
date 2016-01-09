package edu.tsp.asr.lumibar;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * Created by mps on 09/01/16.
 * Ces fonctions sont issues des exemples fournis ici :
 * https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples
 */
public class SerialCommJssc {

    public void listPorts() {
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }

    public void writeData(String data) {
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            serialPort.writeBytes(data.getBytes());//Write data to port
            serialPort.closePort();//Close serial port
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    public void readData() {
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
            byte[] buffer = serialPort.readBytes(10);//Read 10 bytes from serial port
            // added
            System.out.println(buffer.toString());
            serialPort.closePort();//Close serial port
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }



}


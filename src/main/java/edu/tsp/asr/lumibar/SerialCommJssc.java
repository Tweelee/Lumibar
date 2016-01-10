package edu.tsp.asr.lumibar;

import jssc.*;

import java.nio.charset.Charset;

/**
 * Created by mps on 09/01/16.
 * Ces fonctions sont issues des exemples fournis ici :
 * https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples
 */
public class SerialCommJssc {

    static SerialPort serialPort;

    public void listPorts() {
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }

    public void writeData(String data) {
        //  SerialPort serialPort = new SerialPort("/dev/ttyUSB3");
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

    public String readData(int nbBytes) {
//      SerialPort serialPort = new SerialPort("/dev/ttyUSB3");
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
            byte[] buffer = serialPort.readBytes(nbBytes);//Read nbBytes bytes from serial port (1byte for 1 char send)
            String v = new String( buffer, Charset.forName("UTF-8") );

            // added
           // System.out.println(buffer.toString());
           // System.out.println("string résultante"+v);

            serialPort.closePort();//Close serial port
            return v;
        } catch (SerialPortException ex) {
            System.out.println(ex);
            return null;
        }
    }

    public void readString() {
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
            String data = serialPort.readString();
            System.out.println(data);
            serialPort.closePort();//Close serial port
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
  /*  public void readInt() {
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
            int data = serialPort.readIntArray();
            System.out.println(data);
            serialPort.closePort();//Close serial port
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }*/


    public void readEventListener() {
        SerialPort serialPort = new SerialPort("/dev/ttyACM0");
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(9600, 8, 1, 0);//Set params
            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }

    }

    static class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {//If data is available
              /*  if (event.getEventValue() == 8) {//Check bytes count in the input buffer
                    //Read data, if 10 bytes available*/
                    try {
                        byte buffer[] = serialPort.readBytes(8);
                        System.out.println(buffer);
                    } catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                        //}
            }
        }


    }

}

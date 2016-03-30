package connection;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by oscar on 27/02/16.
 */
public class BTConnection implements DiscoveryListener {
    private static final Object lock = new Object();
    private static final List<RemoteDevice> btDevices = new Vector<>();
    private OutputStream connection;
    private PrintWriter pw;

    public BTConnection() {
        super();
    }

    public List<Device> getBtDevices() {
        List<Device> devices = new ArrayList<>();
        for(RemoteDevice device: btDevices) {
            try {
                devices.add(new Device(device.getFriendlyName(false), device.getBluetoothAddress()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return devices;
    }

    public void bluetoothDiscovery() {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.GIAC, this);
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
    }

    public String getAdrress(int index) {
        try{
            return btDevices.get(index).getBluetoothAddress();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "No device";
        }
    }

    public String getFriendlyName(int index) {
        try {
            return btDevices.get(index).getFriendlyName(false);
        } catch (IOException e) {
            return "An error retrieving the name ocurred";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "No device";
        }
    }

    public String connect(int index) {
        RemoteDevice remoteDevice = btDevices.get(index);
        String address = "btspp://" + remoteDevice.getBluetoothAddress() + ":1";
        try {
            connection = Connector.openOutputStream(address);
            pw = new PrintWriter(new OutputStreamWriter(connection));
            return "Success";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }

    }

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        if (!btDevices.contains(remoteDevice)) {
            btDevices.add(remoteDevice);
        }
    }

    @Override
    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

    }

    @Override
    public void serviceSearchCompleted(int i, int i1) {

    }

    @Override
    public void inquiryCompleted(int i) {
        synchronized (lock) {
            lock.notify();
        }
    }

    public Device getBtDevice(int index) {
        try {
            return new Device(btDevices.get(index).getFriendlyName(false), btDevices.get(index).getBluetoothAddress());
        } catch (IOException e) {
            return new Device();
        }
    }

    public String disconnect(int index) {
        try {
            pw.close();
            connection.close();
            return "Success";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public void sendMessage(String body) {
        pw.print(body);
        pw.flush();
    }

    public void sendByte(byte dato) {
        try {
            connection.write(dato);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String commandLine) {
        char[] letras = new char[2];
        letras[0] = (char)Integer.parseInt(commandLine.split(",")[0]);
        letras[1] = (char)Integer.parseInt(commandLine.split(",")[1]);
        pw.write(letras);
        pw.flush();
    }

    public void sendTest(String data) {
        StringBuffer sb = new StringBuffer();
        int max = 5;
        if(Integer.parseInt(data) < 0) {
            max = 4;
            sb.append('-');
        }
        for(int i = 0; i < max-data.length(); i++) {
            sb.append(0);
        }
        sb.append(Math.abs(Integer.parseInt(data)));
        System.out.println(sb.toString());
        if(pw != null) {
            pw.write(sb.toString());
            pw.flush();
        }
    }

    public class Device {
        public String name;
        public String address;

        public Device() {
            super();
            name = address = "Not found";
        }

        public Device(String name, String address) {
            this.name = name;
            this.address = address;
        }
    }
}

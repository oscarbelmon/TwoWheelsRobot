package pruebas;

/**
 * Created by oscar on 27/02/16.
 */


import java.io.*;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;

/**
 * Class that discovers all bluetooth devices in the neighbourhood,
 * <p>
 * Connects to the chosen device and checks for the presence of OBEX push service in it.
 * and displays their name and bluetooth address.
 */
public class BluetoothServiceDiscovery implements DiscoveryListener {

    //object used for waiting
    private static final Object lock = new Object();

    //vector containing the devices discovered
    private static Vector vecDevices = new Vector();

    private static String connectionURL = null;

    /**
     * Entry point.
     */
    public static void main(String[] args) throws IOException {

        BluetoothServiceDiscovery bluetoothServiceDiscovery = new BluetoothServiceDiscovery();

        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());

        //find devices
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();

        System.out.println("Starting device inquiry…");
        agent.startInquiry(DiscoveryAgent.GIAC, bluetoothServiceDiscovery);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Device Inquiry Completed. ");

        //print all devices in vecDevices
        int deviceCount = vecDevices.size();

        if (deviceCount <= 0) {
            System.out.println("No Devices Found .");
        } else {
            //print bluetooth device addresses and names in the format [ No. address (name) ]
            System.out.println("Bluetooth Devices: ");
            for (int i = 0; i < deviceCount; i++) {
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                System.out.println((i + 1)
                        + " " + remoteDevice.getBluetoothAddress()
                        + " (" + remoteDevice.getFriendlyName(true) + ")");

                connect(remoteDevice);

                System.out.print("Choose the device to search for Obex Push service : ");
                BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
                String chosenIndex = bReader.readLine();
                int index = Integer.parseInt(chosenIndex.trim());
                //check for obex service RemoteDevice
                remoteDevice = (RemoteDevice) vecDevices.elementAt(index - 1);
                UUID[] uuidSet = new UUID[1];
//                uuidSet[0] = new UUID("1105", true); // OBEX
                uuidSet[0] = new UUID("1101", true); // Serial port
                System.out.println("\nSearching for service...");
                agent.searchServices(null, uuidSet, remoteDevice, bluetoothServiceDiscovery);
                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (connectionURL == null) {
                    System.out.println("Device does not support Object Push.");
                } else {
                    System.out.println("Device supports Object Push.");
                }
            }
        }
    }

    private static void connect(RemoteDevice remoteDevice) {
        String address = "btspp://201309241075:1;authenticate=false;encrypt=false;master=false";
        try {
            OutputStream connection = Connector.openOutputStream(address);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(connection));
            System.out.println("Enviando cadena");
            pw.println("Hola");
            System.out.println("Cerrando conexiones");
            pw.flush();
            pw.close();
            connection.close();
//            InputStream connection = Connector.openInputStream(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a bluetooth device is discovered. * Used for device search.
     */
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        //add the device to the vector
        if (!vecDevices.contains(btDevice)) {
            vecDevices.addElement(btDevice);
        }
    }

    /**
     * Called when a bluetooth service is discovered. * Used for service search.
     */
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if (servRecord != null && servRecord.length > 0) {
            connectionURL = servRecord[0].getConnectionURL(0, false);
            System.out.println(connectionURL);
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    /**
     * Called when the service search is over.
     */
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (lock) {
            lock.notify();
        }
    }

    /**
     * Called when the device search is over.
     */
    public void inquiryCompleted(int discType) {
        synchronized (lock) {
            lock.notify();
        }

    }
}
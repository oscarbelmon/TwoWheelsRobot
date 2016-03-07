package service;

import bt.BTConnection;
import com.google.gson.Gson;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.*;

/**
 * Created by oscar on 27/02/16.
 */
public class Server {
    private BTConnection btConnection;
    private Gson gson = new Gson();

    private Server() {
        super();
        btConnection = new BTConnection();
//        btConnection.bluetoothDiscovery();
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {
        before(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.header("Access-Control-Allow-Origin", origin);
                response.header("Access-Control-Request-Method", methods);
                response.header("Access-Control-Allow-Headers", headers);
            }
        });
    }


    private void setUp() {
        staticFileLocation("/public");
        get("/discover", this::discoverDevices);
        get("/devices", this::getAllDevices);
        get("/devices/:id", this::getDevice);
        get("/devices/:id/address", this::getDeviceAddress);
        get("/devices/:id/name", this::getDeviceName);
        get("/devices/:id/connect", this::deviceConnect);
        get("/devices/:id/disconnect", this::deviceDisconnect);
        post("/devices/command", this::sendCommad);
        Server.enableCORS("*", "*", "*"); // No sé si esto es necesario
    }

    private String sendCommad(Request request, Response response) {
        String commandLine = request.body();
        btConnection.sendCommand(commandLine);
        return "Success";
    }

    private String deviceDisconnect(Request request, Response response) {
        int index = new Integer(request.params(":id")).intValue();
        return btConnection.disconnect(index);
    }

    private String deviceConnect(Request request, Response response) {
        int index = new Integer(request.params(":id")).intValue();
        return btConnection.connect(index);
    }

    private String discoverDevices(Request request, Response response) {
        btConnection.bluetoothDiscovery();
        return getAllDevices(request, response);
    }

    private String getDevice(Request request, Response response) {
        int index = new Integer(request.params(":id")).intValue();
        return  gson.toJson(btConnection.getBtDevice(index));
    }

    public static void main(String[] args) {
        new Server().setUp();
    }

    private String getAllDevices(Request request, Response response) {
        List<BTConnection.Device> devices = btConnection.getBtDevices();
        Gson gson = new Gson();
        return gson.toJson(devices);
    }

    private String getDeviceName(Request request, Response response) {
        int id = new Integer(request.params(":id")).intValue();
        return btConnection.getFriendlyName(id);
    }

    private String getDeviceAddress(Request request, Response response) {
        Integer id = new Integer(request.params(":id"));
        return btConnection.getAdrress(id.intValue());
    }
}

package pruebas;

import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

/**
 * Created by oscar on 27/02/16.
 */
public class Servidor {
    public static void main(String[] args) {
        Servidor servidor = new Servidor();

        get("/hola", servidor::ruta);
    }

    private String ruta(Request request, Response response) {
        return "Hola";
    }
}

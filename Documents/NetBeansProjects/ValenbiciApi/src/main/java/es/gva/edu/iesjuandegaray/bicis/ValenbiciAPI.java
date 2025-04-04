package es.gva.edu.iesjuandegaray.bicis;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ValenbiciAPI {
     private static final String API_URL = "https://valencia.opendatasoft.com/api/explore/v2.1/catalog/datasets/valenbisi-disponibilitat-valenbisi-dsiponibilidad/records?f=json&location=39.46447,-0.39308&distance=10&limit=3";

    public static void main(String[] args) {
        if (API_URL.isEmpty()) {
            System.err.println("La URL de la API no está especificada.");
            return;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println("Respuesta de la API:");
                System.out.println(result);

                // Intentamos procesar la respuesta como JSON
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");

                    //  Recorre el vector resultsArray mostrando los datos solicitados    
                    for (int i = 0; i < resultsArray.length(); i++) {
                        
                        JSONObject record = resultsArray.getJSONObject(i);
                        // Extraer datos relevantes
                         String id = record.getString("id"); // Ejemplo: clave "id"
                         JSONObject fields = record.getJSONObject("fields");
                         String stationName = fields.getString("station_name"); // Ejemplo: clave "station_name"
                         int bikesAvailable = fields.getInt("bikes_available"); // Ejemplo: clave "bikes_available"

                         // Mostrar los datos
                         System.out.println("Estación: " + stationName);
                         System.out.println("ID: " + id);
                         System.out.println("Bicis disponibles: " + bikesAvailable);
                         System.out.println("--------------------------");
                    }
                    

                 } catch (org.json.JSONException e) {
                    // Si la respuesta no es un array JSON, imprimimos el mensaje de error
                    System.err.println("Error al procesar los datos JSON: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
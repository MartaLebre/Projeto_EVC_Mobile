package amsi.dei.estg.ipleiria.projetoevc.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.modelo.Encomenda;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;

public class EncomendaJsonParser {

    public static ArrayList<Encomenda>parserJsonEncomendas(JSONArray response){
        ArrayList<Encomenda> encomendas = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject encomenda = (JSONObject) response.get(i);

                int Id = encomenda.getInt("id");
                String Estado = encomenda.getString("estado");
                String Data = encomenda.getString("data");
                int Id_user = encomenda.getInt("id_user");

                Encomenda auxEncomenda = new Encomenda(Id, Estado, Data, Id_user);
                encomendas.add(auxEncomenda);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
            return encomendas;
    }

   public static Encomenda parserJsonEncomenda(String response){

       Encomenda auxEncomenda = null;
       try {
           JSONObject encomenda = new JSONObject(response);

           int Id = encomenda.getInt("id");
           String Estado = encomenda.getString("estado");
           String Data = encomenda.getString("data");
           int Id_user = encomenda.getInt("id_user");

           auxEncomenda = new Encomenda(Id, Estado, Data, Id_user);

       } catch (JSONException e) {
           e.printStackTrace();
       }

       return  auxEncomenda;
   }
}

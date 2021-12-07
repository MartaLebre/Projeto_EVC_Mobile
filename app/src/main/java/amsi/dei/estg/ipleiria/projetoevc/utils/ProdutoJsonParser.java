package amsi.dei.estg.ipleiria.projetoevc.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;

public class ProdutoJsonParser {

    public static ArrayList<Produto>parserJsonProdutos(JSONArray response){
        ArrayList<Produto> produtos = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject produto = (JSONObject) response.get(i);

                int CodigoProduto = produto.getInt("codigo_produto");
                String Nome = produto.getString("nome");
                String Genero = produto.getString("genero");
                String Descricao = produto.getString("descricao");
                String Tamanho = produto.getString("tamanho");
                double Preco = produto.getDouble("preco");

                Produto auxProduto = new Produto(CodigoProduto, Nome, Genero, Descricao, Tamanho, (float) Preco);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
            return produtos;
    }

   public static Produto parserJsonProduto(String response){

       Produto auxProduto = null;
       try {
           JSONObject produto = new JSONObject(response);

           int CodigoProduto = produto.getInt("codigo_produto");
           String Nome = produto.getString("nome");
           String Genero = produto.getString("genero");
           String Descricao = produto.getString("descricao");
           String Tamanho = produto.getString("tamanho");
           double Preco = produto.getDouble("preco");

           auxProduto = new Produto(CodigoProduto, Nome, Genero, Descricao, Tamanho, (float) Preco);

       } catch (JSONException e) {
           e.printStackTrace();
       }

       return  auxProduto;
   }
}

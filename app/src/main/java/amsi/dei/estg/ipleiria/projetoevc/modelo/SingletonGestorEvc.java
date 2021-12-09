package amsi.dei.estg.ipleiria.projetoevc.modelo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.listeners.UserListener;
import amsi.dei.estg.ipleiria.projetoevc.utils.ProdutoJsonParser;
import amsi.dei.estg.ipleiria.projetoevc.utils.UtilizadoresParserJson;

public class SingletonGestorEvc {

    //192.168.1.189
    private static SingletonGestorEvc instance = null;
    private Utilizador utilizador;
    private ArrayList<Produto> produtos;
    private ProdutoDBHelper produtosDB=null;
    private static RequestQueue volleyQueue = null; //static para ser fila unica
    private static final String mUrlAPIRegistarUser = "http://192.168.1.177:8080/v1/user/registo";
    private static final String mUrlAPIUserLogin = "http://192.168.1.177:8080/v1/user/login";
    private static final String mUrlAPIEditarRegistoUser = "http://192.168.1.177:8080/v1/user/editar";
    private static final String mUrlAPIApagarUser = "http://192.168.1.177:8080/v1/user/apagar";
    private static final String mUrlAPIUserDetalhes = "http://192.168.1.177:8080/v1/user/detalhes";
    private static final String mUrlAPIProdutos = "http://192.168.1.177:8080/v1/produto";

    private UserListener userListener;
    protected ProdutosListener produtosListener;

    public static synchronized SingletonGestorEvc getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorEvc(context);
            volleyQueue = Volley.newRequestQueue(context); //Cria apenas uma fila de pedidos
        }
        return instance;
    }

    private SingletonGestorEvc(Context context) {

    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }

    public void setProdutosListener(ProdutosListener produtosListener) {
        this.produtosListener = produtosListener;
    }

    public Produto getProduto(int codigo_produto){
        for(Produto p: produtos){
            if(p.getCodigo_produto() == codigo_produto)
                return p;
        }
        return null;
    }

    /*********** Metodos para aceder a BD local ************/

    public ArrayList<Produto> getProdutosDB() {
        produtos = produtosDB.getAllProdutosBD();

        return produtos;
        // return new ArrayList<>(livros);
    }

    public void adicionarProdutoBD(Produto produto){
        produtosDB.adicionarProdutoBD(produto);
        //Livro auxLivro = livrosBD.adicionarLivroBD(livro);
        // if(auxLivro!= null)
        //livros.add(auxLivro);
        //livros.add(livro);
    }

    public void adicionarProdutosBD(ArrayList<Produto> produtos){
        produtosDB.removerAllProdutosBD();
        for(Produto p : produtos)
            adicionarProdutoBD(p);
    }

    public void editarProdutoBD(Produto produto){
        Produto produtoAux = getProduto(produto.getCodigo_produto());
        if(produtoAux != null)
            produtosDB.editarProdutoBD(produtoAux);
            /*
            if(livrosBD.editarLivroBD(livroAux)) {
                livroAux.setTitulo(livro.getTitulo());
                livroAux.setAutor(livro.getAutor());
                livroAux.setCapa(livro.getCapa());
                livroAux.setSerie(livro.getSerie());
                livroAux.setAno(livro.getAno());
              */
    }

    public void removerProdutoBD(int codigo_produto){
        Produto produto = getProduto(codigo_produto);
        if(produto!=null)
            //if(livrosBD.removerLivroBD(id))
            //livros.remove(livro);
            produtosDB.removerProdutoBD(codigo_produto);
    }

    public static boolean isConnectedInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /********* Métodos de acesso à API - Utilizador****/
    /**
     * Registar User API
     */

    public void registarUserAPI(final Utilizador utilizador, final Context context) {
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIRegistarUser, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (userListener != null) {
                    userListener.onUserRegistado(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", utilizador.getUsername());
                params.put("email", utilizador.getEmail());
                params.put("password", utilizador.getPassword());
                params.put("primeiro_nome", utilizador.getPrimeiroNome());
                params.put("ultimo_nome", utilizador.getUltimoNome());
                params.put("telemovel", utilizador.getNumeroTelemovel());

                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void getUserAPI(final Context context, String token) {

            StringRequest req = new StringRequest(Request.Method.GET, mUrlAPIUserDetalhes + "/" + token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    utilizador = UtilizadoresParserJson.parserJsonUtilizador(response);

                    if (userListener != null)
                        userListener.onLoadEditarRegisto(utilizador);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
    }


    public void editarUtilizadorAPI(final Utilizador utilizador, final Context context, final String username) {
        StringRequest req = new StringRequest(Request.Method.PUT, mUrlAPIEditarRegistoUser + "/" + username, new Response.Listener<String>() {

            public void onResponse(String response) {
                if (userListener != null) {
                    userListener.onRefreshDetalhes(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", utilizador.getUsername());
                params.put("email", utilizador.getEmail());
                params.put("primeiro_nome", utilizador.getPrimeiroNome());
                params.put("ultimo_nome", utilizador.getUltimoNome());
                params.put("telemovel", utilizador.getNumeroTelemovel());
                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void loginUserAPI(final String username, final String password, final Context context) {
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIUserLogin, new Response.Listener<String>() {

            public void onResponse(String response) {
                String token = UtilizadoresParserJson.parserJsonLogin(response);
                if (userListener != null) {
                    userListener.onValidateLogin(token, username);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (userListener != null) {
                    userListener.onErroLogin();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        volleyQueue.add(req);
    }


    public void apagarContaAPI(String username, final Context context) {
        StringRequest req = new StringRequest(Request.Method.PATCH, mUrlAPIApagarUser + "/" + username, new Response.Listener<String>() {

            public void onResponse(String response) {
                if (userListener != null) {
                    userListener.onApagarConta(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    public void getAllProdutosAPI(final Context context) {
        /*if(!Produto.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à Internet", Toast.LENGTH_LONG).show();

            if(livrosListener != null) {
                livrosListener.onRefreshListaLivros(livrosBD.getAllLivrosBD());
            }
        }
        else {*/
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIProdutos, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    produtos = ProdutoJsonParser.parserJsonProdutos(response);
                    //adicionarLivrosBD(livros);

                    if(produtosListener != null) {
                        produtosListener.onRefreshListaProdutos(produtos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(request);
    }



}

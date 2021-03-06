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

import amsi.dei.estg.ipleiria.projetoevc.listeners.EncomendasListener;
import amsi.dei.estg.ipleiria.projetoevc.listeners.FavoritosListener;
import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.listeners.UserListener;
import amsi.dei.estg.ipleiria.projetoevc.utils.EncomendaJsonParser;
import amsi.dei.estg.ipleiria.projetoevc.utils.ProdutoJsonParser;
import amsi.dei.estg.ipleiria.projetoevc.utils.UtilizadoresParserJson;

public class SingletonGestorEvc {

    private static final int ADICIONAR_BD = 1;

    private static final int REMOVER_BD = 3;

    private static final String IP = "http://192.168.1.189:8080";

    private static SingletonGestorEvc instance = null;
    private Utilizador utilizador;
    private ArrayList<Produto> produtos;
    private ArrayList<Encomenda> encomendas;
    private Produto produto;
    private ProdutosFavoritosDBHelper produtosFavoritosBD;
    private static RequestQueue volleyQueue = null; //static para ser fila unica
    private static final String mUrlAPIRegistarUser = IP + "/v1/user/registo";
    private static final String mUrlAPIUserLogin = IP + "/v1/user/login";
    private static final String mUrlAPIEditarRegistoUser = IP + "/v1/user/editar";
    private static final String mUrlAPIApagarUser = IP + "/v1/user/apagar";
    private static final String mUrlAPIUserDetalhes = IP + "/v1/user/detalhes";
    private static final String mUrlAPIProdutos = IP + "/v1/produto/all";
    private static final String mUrlAPIProdutoPesquisa = IP + "/v1/produto/pesquisa";
    private static final String mUrlAPIProdutosFavoritos = IP + "/v1/favorito/info";
    private static final String mUrlAPIProdutosFavoritosAdicionar = IP + "/v1/favorito/adicionar";
    private static final String mUrlAPIProdutosFavoritosEliminar = IP + "/v1/favorito/remover";
    private static final String mUrlAPIProdutosFavoritosCheck = IP + "/v1/favorito/check";
    private static final String mUrlAPIEncomendas = IP + "/v1/encomenda/all";


    private UserListener userListener;
    protected ProdutosListener produtosListener;
    public FavoritosListener favoritosListener;
    private EncomendasListener encomendasListener;

    public static synchronized SingletonGestorEvc getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorEvc(context);
            volleyQueue = Volley.newRequestQueue(context); //Cria apenas uma fila de pedidos
        }
        return instance;
    }

    private SingletonGestorEvc(Context context) {

        produtos = new ArrayList<>();
        produtosFavoritosBD = new ProdutosFavoritosDBHelper(context);

    }

    public void setFavoritosListener(FavoritosListener favoritosListener) {
        this.favoritosListener = favoritosListener;
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }

    public void setProdutosListener(ProdutosListener produtosListener) {
        this.produtosListener = produtosListener;
    }

    public void setEncomendasListener(EncomendasListener encomendasListener) {
        this.encomendasListener = encomendasListener;
    }

    public Produto getProduto(int id) {
        for (Produto p : produtos) {
            if (p.getCodigo_produto() == id) {
                return p;
            }
        }
        return null;
    }

    /*********** Metodos para aceder a BD local ************/

    public ArrayList<Produto> getProdutosFavoritosDB() {
        produtos = produtosFavoritosBD.getAllProdutosFavoritosBD();

        return produtos;
    }

    public void adicionarProdutoFavoritoBD(Produto produtoFavorito) {
        produtosFavoritosBD.adicionarProdutoFavoritoBD(produtoFavorito);
    }

    public void adicionarProdutosFavoritosBD(ArrayList<Produto> produtos) {
        produtosFavoritosBD.removerAllProdutosFavoritosBD();
        for (Produto p : produtos)
            adicionarProdutoFavoritoBD(p);
    }


    public void removerProdutoFavoritoBD(int codigo_produto) {
        Produto produto = getProduto(codigo_produto);
        if (produto != null) {
            if (produtosFavoritosBD.removerProdutoFavoritoBD(codigo_produto)) {
                produtos.remove(codigo_produto);
            }
        }
    }

    public static boolean isConnectedInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /********* M??todos de acesso ?? API - Utilizador****/
    /**
     * Registar User API
     */

    public void registarUserAPI(final Utilizador utilizador, final Context context) {
        if (!isConnectedInternet(context)) {
            Toast.makeText(context, "N??o tem liga????o ?? internet!", Toast.LENGTH_SHORT).show();
        } else {
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
    }

    public void getUserAPI(final Context context, String token) {
        if (!isConnectedInternet(context)) {
            Toast.makeText(context, "N??o tem liga????o ?? internet!", Toast.LENGTH_SHORT).show();
        } else {

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
        if (!isConnectedInternet(context)) {
            Toast.makeText(context, "N??o tem liga????o ?? internet!", Toast.LENGTH_SHORT).show();
        } else {
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
        if (!isConnectedInternet(context)) {
            Toast.makeText(context, "N??o tem liga????o ?? internet!", Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIProdutos, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    produtos = ProdutoJsonParser.parserJsonProdutos(response);

                    if (produtosListener != null) {
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

    public void getProdutoPesquisa(String pesquisa, final Context context) {
            StringRequest request = new StringRequest(Request.Method.GET, mUrlAPIProdutoPesquisa + "/" + pesquisa, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    produto = ProdutoJsonParser.parserJsonProduto(response);

                    if (produtosListener != null) {
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

    /********* M??todos de acesso ?? API - Favoritos****/
    /**
     *
     */

    public void getAllProdutosFavoritosAPI(final Context context, String token) {
        if (!isConnectedInternet(context)) {
            Toast.makeText(context, "N??o tem liga????o ?? internet!", Toast.LENGTH_SHORT).show();
            adicionarProdutosFavoritosBD(produtos);
            if (favoritosListener != null) {
                favoritosListener.onRefreshListaFavoritosProdutos(produtos);
            }
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIProdutosFavoritos + "/" + token, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    produtos = ProdutoJsonParser.parserJsonProdutos(response);
                    adicionarProdutosFavoritosBD(produtos);
                    if (favoritosListener != null) {
                        favoritosListener.onRefreshListaFavoritosProdutos(produtos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "N??o tem nenhum produto adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void adicionarProdutoFavoritoAPI(final Context context, final Produto produto, final String token) {
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIProdutosFavoritosAdicionar + "/" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                adicionarProdutoFavoritoBD(produto);
                if (favoritosListener != null) {
                    favoritosListener.onAddProdutosFavoritos();
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
                params.put("codigo_produto", produto.getCodigo_produto() + "");
                params.put("token", token);
                    /*
                    JSONObject param = new JSONObject(params);
                    Log.e("MAP:", param+"");*/

                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void removerProdutoFavoritoAPI(final Context applicationContext, Produto produto, String token) {

        StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPIProdutosFavoritosEliminar + "/" + produto.getCodigo_produto() + "/" + token, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (favoritosListener != null) {
                    favoritosListener.onRemoverProdutosFavoritos();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    public void checkFavoritoAPI(final Context applicationContext, final Produto produto, String token) {
        StringRequest req = new StringRequest(Request.Method.GET, mUrlAPIProdutosFavoritosCheck + "/" + produto.getCodigo_produto() + "/" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean favorito = response.equals("true") ? true : false;

                if (favoritosListener != null)
                    favoritosListener.oncheckProdutoFavorito(favorito);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    public void getAllEncomendasAPI(final Context context, String token) {
        if (!isConnectedInternet(context)) {
            Toast.makeText(context, "N??o tem liga????o ?? internet!", Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIEncomendas + "/" + token, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    encomendas = EncomendaJsonParser.parserJsonEncomendas(response);
                    if (encomendasListener != null) {
                        encomendasListener.onRefreshListaEncomendas(encomendas);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "N??o tem nenhuma encomenda!", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }


    public void OnUpdateListaFavoritosBD(Produto produto, int operacao) {
        switch (operacao) {
            case ADICIONAR_BD:
                adicionarProdutoFavoritoBD(produto);
                break;
            case REMOVER_BD:
                removerProdutoFavoritoBD(produto.getCodigo_produto());
                break;
        }
    }
}

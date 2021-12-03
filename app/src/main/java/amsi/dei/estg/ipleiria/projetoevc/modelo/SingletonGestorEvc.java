package amsi.dei.estg.ipleiria.projetoevc.modelo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projetoevc.listeners.UserListener;
import amsi.dei.estg.ipleiria.projetoevc.utils.UtilizadoresParserJson;

public class SingletonGestorEvc {

    //192.168.1.189
    private static SingletonGestorEvc instance = null;
    private Utilizador utilizadores;
    private static RequestQueue volleyQueue = null; //static para ser fila unica
    private static final String mUrlAPIRegistarUser = "http://192.168.1.68:8080/v1/user/registo";
    private static final String mUrlAPIUserLogin = "http://192.168.1.68:8080/v1/user/login";
    private static final String mUrlAPIEditarRegistoUser = "http://192.168.1.68:8080/v1/user/editar";
    private static final String mUrlAPIApagarUser = "http://192.168.1.68:8080/v1/user/apagar";
    private static final String mUrlAPIUserInfo = "http://192.168.1.68:8080/v1/user/detalhes";

    public UserListener userListener;

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

    public void getUserAPI(final Context context, String username) {

            StringRequest req = new StringRequest(Request.Method.GET, mUrlAPIUserInfo + "/" + username, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    utilizadores = UtilizadoresParserJson.parserJsonUtilizador(response);

                    if (userListener != null)
                        userListener.onLoadEditarRegisto(utilizadores);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
    }


    public void editarUtilizadorAPI(final Utilizador utilizador, final Context context, final String oldPassword, final String username) {
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
                if (userListener != null) {
                    userListener.onValidateLogin(UtilizadoresParserJson.parserJsonLogin(response), username);
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
        Log.e("12", req.toString());
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



}

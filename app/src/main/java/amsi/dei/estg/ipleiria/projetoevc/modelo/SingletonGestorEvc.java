package amsi.dei.estg.ipleiria.projetoevc.modelo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projetoevc.listeners.UserListener;
import amsi.dei.estg.ipleiria.projetoevc.utils.UtilizadoresParserJson;

public class SingletonGestorEvc {

    private static SingletonGestorEvc instance = null;
    private static RequestQueue volleyQueue;
    private static final String mUrlAPIRegistarUser = "http://192.168.1.68:8080/v1/user/registo";
    private static final String mUrlAPIUserLogin = "http://192.168.1.68:8080/v1/user/login/";
    private static final String mUrlAPIEditarRegistoUser = "http://192.168.1.68:8080/v1/user/editar";
    private static final String mUrlAPIApagarUser = "http://192.168.1.68:8080/v1/user/apagaruser";

    public UserListener userListener;

    private static final int ADICIONAR_BD = 1;
    private static  final int  EDITAR_BD = 2;


    public void setUserListener(UserListener userListener){
        this.userListener = userListener;
    }


    public static synchronized SingletonGestorEvc getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestorEvc(context);
            volleyQueue = Volley.newRequestQueue(context);
        }

        return instance;
    }

    private SingletonGestorEvc(Context context) {

    }

    public static boolean isConnectedInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /*********** Métodos de acesso à API - Utilizador ***********/

    public void registarUserAPI(final Utilizador utilizador, final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mUrlAPIRegistarUser,
                new Response.Listener<String>() {
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
                Map<String, String> params = new HashMap<>();
                params.put("username", utilizador.getUsername());
                params.put("email", utilizador.getEmail());
                params.put("password", utilizador.getPassword());
                params.put("primeiro_nome", utilizador.getPrimeiro_nome());
                params.put("ultimo_nome", utilizador.getUltimo_nome());
                params.put("telemovel", utilizador.getTelemovel());

                return params;
            }
        };
        volleyQueue.add(stringRequest);
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
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void editarUtilizadorAPI(final Utilizador utilizador, final Context context, final String password, final String username) {
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
                params.put("primeiro_nome", utilizador.getPrimeiro_nome());
                params.put("ultimo_nome", utilizador.getUltimo_nome());
                params.put("telemovel", utilizador.getTelemovel());

                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void apagarContaAPI(String username, final Context context) {
        StringRequest req = new StringRequest(Request.Method.PATCH, mUrlAPIApagarUser + "/" +  username, new Response.Listener<String>() {

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
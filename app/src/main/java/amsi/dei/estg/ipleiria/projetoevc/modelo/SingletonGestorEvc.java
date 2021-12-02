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

    private static final String mUrlAPIRegistarUser = "http://192.168.1.68:8080/v1/user/registo";
    private static final String mUrlAPIUserLogin = "http://192.168.1.68:8080/v1/user/login";
    private static final String mUrlAPIEditarRegistoUser = "http://192.168.1.68:8080/v1/user/editar";
    private static final String mUrlAPIApagarUser = "http://192.168.1.68:8080/v1/user/apagar";
    private static final String mUrlAPIUserInfo = "http://192.168.1.68:8080/v1/user/detalhes";



}

package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.listeners.UserListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Utilizador;
import amsi.dei.estg.ipleiria.projetoevc.utils.UtilizadoresParserJson;



public class LoginFragment extends Fragment implements UserListener {

    private EditText mUsername, mPassword;

    private FragmentManager fragmentManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        SingletonGestorEvc.getInstance(getContext()).setUserListener(this);

        fragmentManager = getFragmentManager();

        mUsername = view.findViewById(R.id.etUsername);
        mPassword = view.findViewById(R.id.etPassword);

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonGestorEvc.isConnectedInternet(getContext())) {
                    String username = mUsername.getText().toString();
                    String password = mPassword.getText().toString();

                    if (!isUsernameValido(username)) {
                        mUsername.setError("Username Inválido");
                        return;
                    }

                    if (!isPasswordValida(password)) {
                        mPassword.setError("Palavra Passe Inválida");
                        return;
                    }

                    SingletonGestorEvc.getInstance(getContext()).loginUserAPI(username, password, getContext());
                }
            }
        });

        Button btnRegistar = view.findViewById(R.id.btnSignup);
        btnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignupFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
    private boolean isUsernameValido(String username) {
        if (username == null) {
            return true;
        }
        return username.length() > 0;

    }
    private boolean isPasswordValida(String password) {
        if (password == null) {
            return true;
        }
        return password.length() > 0;

    }

    @Override
    public void onUserRegistado(String response) {

    }

    @Override
    public void onValidateLogin(String username, String token) {
        if (username != null) {
            SharedPreferences sharedPrefUser = getActivity().getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefUser.edit();
            editor.putString(MenuMainActivity.USERNAME, username);
            //editor.putString(MenuMainActivity.TOKEN, token);
            editor.apply();

            Fragment fragment = new MainFragment();
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
            Toast.makeText(getContext(), "Bem Vindo!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Login Inválido", Toast.LENGTH_LONG).show();
        }

}


    @Override
    public void onRefreshDetalhes(String response) {

    }

    @Override
    public void onApagarConta(String response) {

    }

    @Override
    public void onErroLogin() {
        Toast.makeText(getContext(), "A sua conta não cumpre os requisitos para que seja possivel iniciar sessão! Para mais informações contacto o suporte.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadEditarRegisto(Utilizador utilizador) {

    }


    private void guardarInfoSharedPref(String token, String username) {
        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();

        editor.putString(MenuMainActivity.USERNAME, username);
        editor.putString(MenuMainActivity.TOKEN, token);

        editor.apply();
    }

}
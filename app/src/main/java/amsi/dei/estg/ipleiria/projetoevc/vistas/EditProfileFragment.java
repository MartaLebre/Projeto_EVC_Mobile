package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.listeners.UserListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Utilizador;


public class EditProfileFragment extends Fragment implements UserListener {



    EditText username;
    EditText email;
    EditText numeroTelemovel;
    EditText password;
    EditText primeiroNome;
    EditText ultimoNome;


    private FragmentManager fragmentManager;
    private Utilizador utilizador;

    private Pattern pattern;
    private Matcher matcher;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        fragmentManager = getFragmentManager();

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        SingletonGestorEvc.getInstance(getContext()).setUserListener(this);


        numeroTelemovel = view.findViewById(R.id.etTelemovel);
        password = view.findViewById(R.id.etPassword);
        username = view.findViewById(R.id.etUsername);
        email = view.findViewById(R.id.etEmail);
        ultimoNome = view.findViewById(R.id.etultimoNome);
        primeiroNome = view.findViewById(R.id.etprimeiroNome);


        //SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
        //String token = sharedPreferencesUser.getString(MenuMainActivity.USERNAME, null);


        Button button = view.findViewById(R.id.btnUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SingletonGestorEvc.isConnectedInternet(getContext())) {


                    String mUsername = username.getText().toString();
                    String mEmail = email.getText().toString();
                    String mPassword = password.getText().toString();
                    String mPrimeiroNome = primeiroNome.getText().toString();
                    String mUltimoNome = ultimoNome.getText().toString();
                    String mNumeroTelemovel = numeroTelemovel.getText().toString();

                    utilizador = new Utilizador(mUsername, mEmail,  mPassword, mPrimeiroNome, mUltimoNome, mNumeroTelemovel);
                    SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
                    String token = sharedPreferencesUser.getString(MenuMainActivity.USERNAME, null);
                    SingletonGestorEvc.getInstance(getContext()).editarUtilizadorAPI(utilizador, getContext(), mPassword,token);
                }
            }
        });

        Button buttonApagar = view.findViewById(R.id.btnDelete);
        buttonApagar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SingletonGestorEvc.isConnectedInternet(getContext())) {


                    String mUsername = username.getText().toString();
                    String mEmail = email.getText().toString();
                    String mPassword = password.getText().toString();
                    String mPrimeiroNome = primeiroNome.getText().toString();
                    String mUltimoNome = ultimoNome.getText().toString();
                    String mNumeroTelemovel = numeroTelemovel.getText().toString();


                    utilizador = new Utilizador(mUsername, mEmail,  mPassword, mPrimeiroNome, mUltimoNome , mNumeroTelemovel);
                    SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
                    String token = sharedPreferencesUser.getString(MenuMainActivity.USERNAME, null);
                    SingletonGestorEvc.getInstance(getContext()).apagarContaAPI(token, getContext());

                    SharedPreferences.Editor editor = sharedPreferencesUser.edit();

                    editor.clear().apply();

                }
            }
        });

        return  view;
    }

    @Override
    public void onUserRegistado(String response) {
        Log.e("resposta", response);

        switch (response) {
            case "0":
                Log.e("eee", "1111");
                email.setError("Este email já se encontra registado!");
                break;
            case "1":
                username.setError("Este nome de utilizador já se encontra registado!");
                break;
            case "2":
                numeroTelemovel.setError("Este numero de telemovel já se encontra registado!");
                break;
            case "false":
                Fragment fragment = new LoginFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
                Toast.makeText(getContext(), "Bem Vindo(a), a sua conta foi registada com sucesso!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onRefreshDetalhes(String resposta) {
        switch (resposta) {
            case "true":
                Fragment fragment = new MainFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
                Toast.makeText(getContext(), "A sua conta foi atualizada com sucesso!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onApagarConta(String resposta) {

        switch (resposta) {
            case "null":
                Fragment fragment = new SignupFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
                Toast.makeText(getContext(), "A sua conta foi apagada com sucesso!", Toast.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    public void onValidateLogin(String token, String username) {

    }
}
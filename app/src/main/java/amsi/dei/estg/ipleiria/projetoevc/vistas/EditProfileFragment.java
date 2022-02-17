package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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

    private EditText username, email, password, primeiroNome, ultimoNome, numeroTelemovel;
    private FragmentManager fragmentManager;
    private Utilizador utilizador;

    private Pattern pattern;
    private Matcher matcher;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        fragmentManager = getFragmentManager();

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        SingletonGestorEvc.getInstance(getContext()).setUserListener(this);

        username = view.findViewById(R.id.etUsername);
        email = view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPassword);
        ultimoNome = view.findViewById(R.id.etultimoNome);
        primeiroNome = view.findViewById(R.id.etprimeiroNome);
        numeroTelemovel = view.findViewById(R.id.etTelemovel);

        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);

        String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
        SingletonGestorEvc.getInstance(getContext()).getUserAPI(getContext(), token);

        Button button = view.findViewById(R.id.btnUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SingletonGestorEvc.isConnectedInternet(getContext())) {
                    String Username = username.getText().toString();
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();
                    String PrimeiroNome = primeiroNome.getText().toString();
                    String UltimoNome = ultimoNome.getText().toString();
                    String NumeroTelemovel = numeroTelemovel.getText().toString();

                    utilizador = new Utilizador(Username, Email, Password, PrimeiroNome, UltimoNome, NumeroTelemovel);

                    SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
                    String user = sharedPreferencesUser.getString(MenuMainActivity.USERNAME, null);
                    SingletonGestorEvc.getInstance(getContext()).editarUtilizadorAPI(utilizador, getContext(), user);
                    SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                    editor.apply();
                }
            }
        });

        Button buttonApagar = view.findViewById(R.id.btnDelete);
        buttonApagar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SingletonGestorEvc.isConnectedInternet(getContext())) {
                    SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
                    String user = sharedPreferencesUser.getString(MenuMainActivity.USERNAME, null);

                    apagar(user);
                }
            }
        });

        return  view;
    }

    private void apagar(final String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Apagar Conta")
                .setMessage("Tem a certeza que pretende apagar a conta?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonGestorEvc.getInstance(getContext()).apagarContaAPI(username, getContext());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    @Override
    public void onUserRegistado(String response) {
        switch (response) {
            case "true":
                Fragment fragment = new LoginFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
                Toast.makeText(getContext(), "Bem Vindo(a), a sua conta foi registada com sucesso!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onValidateLogin(String token, String username) {

    }

    @Override
    public void onRefreshDetalhes(String response) {
        switch (response) {
            case "true":
                Fragment fragment = new MainFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
                Toast.makeText(getContext(), "A sua conta foi atualizada com sucesso!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onApagarConta(String response) {
        switch (response) {
            case "true":
                SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                editor.clear();
                editor.commit();
                Fragment fragment = new LoginFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
                Toast.makeText(getContext(), "A sua conta foi apagada com sucesso!", Toast.LENGTH_LONG).show();

                break;
        }
    }

    @Override
    public void onErroLogin() {

    }

    @Override
    public void onLoadEditarRegisto(Utilizador utilizador) {
        username.setText(utilizador.getUsername());
        email.setText(utilizador.getEmail());
        password.setText(utilizador.getPassword());
        primeiroNome.setText(utilizador.getPrimeiroNome());
        ultimoNome.setText(utilizador.getUltimoNome());
        numeroTelemovel.setText(utilizador.getNumeroTelemovel());
    }
}
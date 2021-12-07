package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.listeners.UserListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Utilizador;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment implements UserListener {

    private EditText username, email, password, primeiroNome, ultimoNome, numeroTelemovel;
    Button btn_signUp;
    public UserListener userListener;

    private Utilizador utilizador;
    private Pattern pattern;
    private Matcher matcher;
    private FragmentManager fragmentManager;


    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        fragmentManager = getFragmentManager();
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_signup, container, false);

       SingletonGestorEvc.getInstance(getContext()).setUserListener(this);

        username = view.findViewById(R.id.etUsername);
        email = view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPassword);
        primeiroNome = view.findViewById(R.id.etprimeiroNome);
        ultimoNome = view.findViewById(R.id.etultimoNome);
        numeroTelemovel = view.findViewById(R.id.etTelemovel);


        btn_signUp = view.findViewById(R.id.btnSignup);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonGestorEvc.isConnectedInternet(getContext())) {
                    String mUsername = username.getText().toString();
                    String mEmail = email.getText().toString();
                    String mPassword = password.getText().toString();
                    String mPrimeiroNome = primeiroNome.getText().toString();
                    String mUltimoNome = ultimoNome.getText().toString();
                    String mNumeroTelemovel = numeroTelemovel.getText().toString();


                    if (!isPrimeiroNomeValido(mPrimeiroNome)) {
                        primeiroNome.setError("Primeiro nome inválido");
                        return;
                    }

                    if (!isUltimoNomeValido(mUltimoNome)) {
                        ultimoNome.setError("Ultimo nome inválido");
                        return;
                    }

                    if (!isUsernameValido(mUsername)) {
                        username.setError("Nome de Utilizador Inválido");
                        return;
                    }

                    if (!isEmailValido(mEmail)) {
                        email.setError("Email inválido");
                        return;
                    }

                    if (!isPasswordValida(mPassword)) {
                        password.setError("Palavra Passe Inválida, é necessário ter mais de 8 caractéres");
                        return;
                    }
                    if (!isNumeroTelemovelValido(mNumeroTelemovel)) {
                        numeroTelemovel.setError("Número de telemóvel inválido");
                        return;
                    }


                    utilizador = new Utilizador(mUsername, mEmail, mPassword, mPrimeiroNome, mUltimoNome, mNumeroTelemovel);
                    SingletonGestorEvc.getInstance(getContext()).registarUserAPI(utilizador, getContext());

                } else {
                    Toast.makeText(getContext(), "Sem ligação à Internet!", Toast.LENGTH_LONG).show();

                }
            }

        });

        return view;
    }

    private boolean isPrimeiroNomeValido(String primeiroNome) {
        if (primeiroNome == null) {
            return false;
        }
        return primeiroNome.length() > 0;

    }

    private boolean isUltimoNomeValido(String ultimoNome) {
        if (ultimoNome == null) {
            return false;
        }
        return ultimoNome.length() > 0;

    }

    private boolean isUsernameValido(String username) {
        if (username == null) {
            return true;
        }
        return username.length() > 0;

    }

    private boolean isEmailValido(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String password) {
        if (password == null) {
            return true;
        }
        return password.length() > 7;

    }

    public boolean isNumeroTelemovelValido(String numeroTelemovel) {
        if (numeroTelemovel == null) {
            return false;
        } else if (numeroTelemovel.length() != 9) {
            return false;
        }
        return true;
    }

    @Override
    public void onUserRegistado(String response) {
        Log.e("resposta", response);
        switch (response) {
            case "true":
                Fragment fragment = new LoginFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
                Toast.makeText(getContext(), "Bem Vindo(a), a sua conta foi registada com sucesso!", Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public void onRefreshDetalhes(String response) {

    }

    @Override
    public void onApagarConta(String response) {

    }

    @Override
    public void onValidateLogin(String token, String username) {

    }

    @Override
    public void onLoadEditarRegisto(Utilizador utilizador) {

    }

    @Override
    public void onErroLogin() {

    }
}
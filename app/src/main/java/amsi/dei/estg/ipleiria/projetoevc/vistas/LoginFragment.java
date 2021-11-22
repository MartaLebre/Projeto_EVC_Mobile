package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements UserListener {

    Button mLoginButton;
    EditText mUsername;
    EditText mPassword;

    private FragmentManager fragmentManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        SingletonGestorEvc.getInstance(getContext()).setUserListener(this);

        fragmentManager = getFragmentManager();
        mLoginButton = view.findViewById(R.id.btnLogin);
        mUsername = view.findViewById(R.id.etUsername);
        mPassword = view.findViewById(R.id.etPassword);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonGestorEvc.isConnectedInternet(getContext())) {
                    String username = mUsername.getText().toString();
                    String password = mPassword.getText().toString();

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


    private void guardarInfoSharedPref(String token, String username) {
        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();

        editor.putString(MenuMainActivity.USERNAME, username);
        editor.putString(
                MenuMainActivity.TOKEN, token);

        editor.apply();
    }

    @Override
    public void onUserRegistado(String response) {

    }

    @Override
    public void onRefreshDetalhes(String response) {

    }

    @Override
    public void onApagarConta(String response) {

    }

    @Override
    public void onValidateLogin(String token, String username) {
        if (token != null) {
            guardarInfoSharedPref(token, username);
            Fragment fragment = new MainFragment();
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
            Toast.makeText(getContext(), "Bem Vindo!", Toast.LENGTH_LONG).show();
        } else {
            mPassword.setError("Utilizador ou Palavra-Passe Incorretos!");
        }
    }
}
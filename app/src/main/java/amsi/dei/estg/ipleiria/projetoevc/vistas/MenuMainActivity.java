package amsi.dei.estg.ipleiria.projetoevc.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.projetoevc.R;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    public String token = "";
    public static final String PREF_INFO_USER = "PREF_INFO_USER";
    public static final String USERNAME = "USERNAME";
    public static final String TOKEN = "TOKEN";


    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);


        navigationView = findViewById(R.id.nav_view);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar,  R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        fragmentManager = getSupportFragmentManager();

        navigationView.setNavigationItemSelectedListener(this);
        carregarFragmento();
    }
    private void carregarFragmento(){
        fragment = new MainFragment();
        setTitle("Inicial");
        if(fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
    }

    public void carregarCabecalho(){
        SharedPreferences sharedPrefInfoUser = getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
        token = sharedPrefInfoUser.getString(USERNAME, "teste");


        View hView = navigationView.getHeaderView(0);
        TextView txtEmailHeader = hView.findViewById(R.id.tvUsername);
        txtEmailHeader.setText(token);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        carregarCabecalho();
        switch (menuItem .getItemId()) {
            case R.id.nav_inicial:
                setTitle(menuItem.getTitle());
                fragment = new MainFragment();
                break;
            case R.id.nav_produtos:
                //ragment = new ListaAnuncioFragment();
                setTitle(menuItem.getTitle());
                break;
            case R.id.nav_favoritos:
                //fragment = new ListaAnuncioFragment();
                setTitle(menuItem.getTitle());
                break;
            case R.id.nav_perfil:
                if (token != null) {
                    fragment = new EditProfileFragment();
                    setTitle(menuItem.getTitle());
                    break;
                } else {
                    fragment = new LoginFragment();
                    setTitle(menuItem.getTitle());
                    break;
                }
            case R.id.nav_TerminarSessao:
                if (token != null) {

                    SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                    editor.clear().apply();

                    fragment = new MainFragment();
                    fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();

                    Toast.makeText(getApplicationContext(), "Terminou a sessão com sucesso!", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Não tem login efectuado para terminar sessão!", Toast.LENGTH_LONG).show();

                }
            default:
                fragment = new MainFragment();
                setTitle(menuItem.getTitle());
        }
        if(fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        carregarFragmento();
        return;
    }
}
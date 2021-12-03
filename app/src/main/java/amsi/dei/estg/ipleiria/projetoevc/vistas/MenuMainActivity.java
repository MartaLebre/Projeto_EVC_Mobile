package amsi.dei.estg.ipleiria.projetoevc.vistas;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.utils.UtilizadoresParserJson;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String USERNAME = "USERNAME";
    public static final String TOKEN = "TOKEN";
    public static final String PREF_INFO_USER = "PREF_INFO_USER";
    private FragmentManager fragmentManager;
    private String username;
    private String token;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;

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
        username = sharedPrefInfoUser.getString(USERNAME, "");
        View hView = navigationView.getHeaderView(0);
        TextView txtEmailHeader = hView.findViewById(R.id.tvUsername);
        txtEmailHeader.setText(username);
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
                fragment = new EditProfileFragment();
                setTitle(menuItem.getTitle());
                break;
            case R.id.nav_perfil:
                fragment = new LoginFragment();
                setTitle(menuItem.getTitle());
                break;

            case R.id.nav_TerminarSessao:
                if(username != null){
                    SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.PREF_INFO_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesUser.edit();

                    editor.clear().apply();

                    fragment = new MainFragment();
                    fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();

                    Toast.makeText(getApplicationContext(), "Terminou a sessão com sucesso!", Toast.LENGTH_LONG).show();
                }else{
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
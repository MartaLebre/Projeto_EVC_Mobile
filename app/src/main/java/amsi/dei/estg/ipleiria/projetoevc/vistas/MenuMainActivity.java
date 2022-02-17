package amsi.dei.estg.ipleiria.projetoevc.vistas;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    private static final Integer REQUEST_CODE = 1;
    public static final String USERNAME = "USERNAME";
    public static final String TOKEN = "TOKEN";
    public static final String INFO_USER = "INFO_USER";
    private FragmentManager fragmentManager;
    private String username = "";
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
        SharedPreferences sharedPrefInfoUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        username = sharedPrefInfoUser.getString(USERNAME, "");
        View hView = navigationView.getHeaderView(0);
        TextView txtEmailHeader = hView.findViewById(R.id.tvUsername);
        txtEmailHeader.setText(username);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        carregarCabecalho();

        SharedPreferences sharedPrefInfoUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        token = sharedPrefInfoUser.getString(TOKEN, null);

        switch (menuItem .getItemId()) {
            case R.id.nav_inicial:
                setTitle(menuItem.getTitle());
                fragment = new MainFragment();
                break;
            case R.id.nav_produtos:
                fragment = new ListaProdutosFragment();
                setTitle(menuItem.getTitle());
                break;
            case R.id.nav_favoritos:
                if(token != null){
                    fragment = new FavoritoFragment();
                    setTitle(menuItem.getTitle());
                    break;
                }else{
                    Toast.makeText(getApplicationContext(), "Não tem sessão iniciada", Toast.LENGTH_LONG).show();
                    fragment = new LoginFragment();
                    setTitle(menuItem.getTitle());
                    break;
                }
            case R.id.nav_encomendas:
                if(token != null){
                    fragment = new EncomendasFragment();
                    setTitle(menuItem.getTitle());
                    break;
                }else{
                    Toast.makeText(getApplicationContext(), "Não tem sessão iniciada", Toast.LENGTH_LONG).show();
                    fragment = new LoginFragment();
                    setTitle(menuItem.getTitle());
                    break;
                }

            case R.id.nav_contacto:
                Intent myIntent = new Intent(Intent.ACTION_CALL);
                String phNum = "tel:" + "911035352";
                myIntent.setData(Uri.parse(phNum));
                if (ActivityCompat.checkSelfPermission( MenuMainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(myIntent);
                }
                else {
                    /* Exibe o ecra para o usuário dar a permissão. */
                    ActivityCompat.requestPermissions(
                            MenuMainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE);
                }
                break;
            case R.id.nav_editarPerfil:
                if(token != null){
                    fragment = new EditProfileFragment();
                    setTitle(menuItem.getTitle());
                    break;
                }else{
                    Toast.makeText(getApplicationContext(), "Não tem sessão iniciada", Toast.LENGTH_LONG).show();
                    fragment = new LoginFragment();
                    setTitle(menuItem.getTitle());
                    break;
                }
            case R.id.nav_terminarSessao:
                if(token != null){
                    SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                    editor.clear();
                    editor.commit();
                    fragment = new MainFragment();
                    fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();

                    Toast.makeText(getApplicationContext(), "Terminou a sessão com sucesso!", Toast.LENGTH_LONG).show();
                    break;
                }else{
                    Toast.makeText(getApplicationContext(), "Não tem login efectuado para terminar sessão!", Toast.LENGTH_LONG).show();
                    fragment = new LoginFragment();
                    setTitle(menuItem.getTitle());
                    break;
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
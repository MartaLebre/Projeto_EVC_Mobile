package amsi.dei.estg.ipleiria.projetoevc.vistas;

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

import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.projetoevc.R;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private String token;
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
        token = sharedPrefInfoUser.getString(TOKEN, null);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        carregarCabecalho();
        switch (menuItem .getItemId()) {
            case R.id.nav_inicial:
                setTitle(menuItem.getTitle());
                fragment = new MainFragment();
                break;
            case R.id.nav_perfil:
                setTitle(menuItem.getTitle());
                fragment = new EditProfileFragment();
                break;
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
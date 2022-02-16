package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.adaptadores.ListaEncomendaAdaptador;
import amsi.dei.estg.ipleiria.projetoevc.adaptadores.ListaFavoritoAdaptador;
import amsi.dei.estg.ipleiria.projetoevc.listeners.EncomendasListener;
import amsi.dei.estg.ipleiria.projetoevc.listeners.FavoritosListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Encomenda;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;

public class EncomendasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, EncomendasListener {

    private ListView lvListaEncomendas;
    private ArrayList<Encomenda> encomenda;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentManager fragmentManager;

    public EncomendasFragment(){

    }
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_encomenda, container, false);
        fragmentManager = getFragmentManager();

        lvListaEncomendas = view.findViewById(R.id.lvListaEncomenda);

        SingletonGestorEvc.getInstance(getContext()).setEncomendasListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPrefInfoUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        String token = sharedPrefInfoUser.getString(MenuMainActivity.TOKEN, null);


        SingletonGestorEvc.getInstance(getContext()).getAllEncomendasAPI(getContext(), token);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        SingletonGestorEvc.getInstance(getContext()).setEncomendasListener(this);
    }

    @Override
    public void onRefreshListaEncomendas(ArrayList<Encomenda> encomendas) {

        if(encomendas != null) {
            lvListaEncomendas.setAdapter(new ListaEncomendaAdaptador(getContext(), encomendas));
        }
    }

    @Override
    public void onRefresh() {
        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
        SingletonGestorEvc.getInstance(getContext()).getAllEncomendasAPI(getContext(), token);
        swipeRefreshLayout.setRefreshing(false);
    }
}
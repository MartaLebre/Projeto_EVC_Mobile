package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.adaptadores.ListaFavoritoAdaptador;
import amsi.dei.estg.ipleiria.projetoevc.listeners.FavoritosListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;

public class FavoritoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FavoritosListener {
    private ListView lvListaFavoritos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentManager fragmentManager;

    public FavoritoFragment(){

    }
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        fragmentManager = getFragmentManager();

        lvListaFavoritos = view.findViewById(R.id.lvListaFavoritos);

        SingletonGestorEvc.getInstance(getContext()).setFavoritosListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPrefInfoUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        String token = sharedPrefInfoUser.getString(MenuMainActivity.TOKEN, null);

        SingletonGestorEvc.getInstance(getContext()).getAllProdutosFavoritosAPI(getContext(), token);

        lvListaFavoritos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getContext(), DetalhesProdutoActivity.class);
                intent.putExtra(DetalhesProdutoActivity.ID, (int) id);
                startActivity(intent);
            }

        });

        return view;
    }



    @Override
    public void onAddProdutosFavoritos() {

    }

    @Override
    public void onRemoverProdutosFavoritos() {

        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
        SingletonGestorEvc.getInstance(getContext()).getAllProdutosFavoritosAPI(getContext(), token);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void oncheckProdutoFavorito(Boolean favorito) {

    }

    @Override
    public void onRefreshListaFavoritosProdutos(ArrayList<Produto> listaFavoritos) {

        if(listaFavoritos != null) {
            lvListaFavoritos.setAdapter(new ListaFavoritoAdaptador(getContext(), listaFavoritos));
        }

    }

    @Override
    public void onNoFavoritos() {
        Fragment fragment = new FavoritoFragment();
        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
        Toast.makeText(getContext(), "Não tem favoritos :(!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRefresh() {

        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
        SingletonGestorEvc.getInstance(getContext()).getAllProdutosFavoritosAPI(getContext(), token);
        swipeRefreshLayout.setRefreshing(false);
    }
}

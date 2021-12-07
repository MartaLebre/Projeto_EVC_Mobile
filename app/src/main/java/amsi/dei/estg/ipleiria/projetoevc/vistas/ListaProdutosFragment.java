package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.adaptadores.ListaProdutoAdaptador;
import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.utils.ProdutoJsonParser;

public class ListaProdutosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ProdutosListener {

    private ListView lvListaProdutos;
    private ArrayList<Produto> listaProdutos;
    private SearchView searchView;
    private static final int EDITAR = 2;
    private static final int ADICIONAR = 3;
    private SwipeRefreshLayout swipeRefreshLayout;


    public ListaProdutosFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_lista_produtos, container, false);

        lvListaProdutos = view.findViewById(R.id.lvListaProdutos);

        SingletonGestorEvc.getInstance(getContext()).getAllProdutosAPI(getContext());
        //SingletonGestorEvc.getInstance(getContext()).setProdutosListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SingletonGestorEvc.getInstance(getContext()).setProdutosListener(this);
    }

    @Override
    public void onRefresh() {
        SingletonGestorEvc.getInstance(getContext()).getAllProdutosAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefreshListaLivros(ArrayList<Produto> listaProdutos) {
        if(listaProdutos != null) {
            lvListaProdutos.setAdapter(new ListaProdutoAdaptador(getContext(), listaProdutos));
        }
    }

    @Override
    public void onRefreshDetalhes() {
        //empty
    }
}
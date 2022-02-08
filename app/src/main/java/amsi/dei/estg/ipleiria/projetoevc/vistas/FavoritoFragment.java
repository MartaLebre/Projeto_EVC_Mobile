package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.app.Activity;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.adaptadores.ListaFavoritoAdaptador;
import amsi.dei.estg.ipleiria.projetoevc.listeners.FavoritosListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;

public class FavoritoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FavoritosListener {
    private ListView lvListaFavoritos;
    private ArrayList<Produto> produto;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int ADICIONAR = 3;
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


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(FavoritoFragment.this);
                integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Produto Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADICIONAR:
                    produto = SingletonGestorEvc.getInstance(getContext()).getProdutosFavoritosDB();
                    lvListaFavoritos.setAdapter(new ListaFavoritoAdaptador(getContext(), produto));
                    Toast.makeText(getContext(), "Favorito Adicionado com sucesso", Toast.LENGTH_LONG).show();
                    //Snackbar.make(getView(),"Livro Adicionado com sucesso", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
        //atualizar a lista
        //apresentar um toast
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        SingletonGestorEvc.getInstance(getContext()).setFavoritosListener(this);
    }


    @Override
    public void onAddProdutosFavoritos() {

    }

    @Override
    public void onRemoverProdutosFavoritos() {
        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
        SingletonGestorEvc.getInstance(getContext()).getAllProdutosFavoritosAPI(getContext(), token);
        Toast.makeText(getContext(), "Removido dos favoritos!", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void oncheckProdutoFavorito(Boolean favorito) {

    }

    @Override
    public void onRefreshListaFavoritosProdutos(ArrayList<Produto> produtos) {

        if(produtos != null) {
            lvListaFavoritos.setAdapter(new ListaFavoritoAdaptador(getContext(), produtos));
        }

    }

    @Override
    public void onRefresh() {
        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
        SingletonGestorEvc.getInstance(getContext()).getAllProdutosFavoritosAPI(getContext(), token);
        swipeRefreshLayout.setRefreshing(false);
    }
}

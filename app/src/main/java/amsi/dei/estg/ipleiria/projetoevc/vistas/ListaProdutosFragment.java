package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.adaptadores.ListaProdutoAdaptador;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;

public class ListaProdutosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ProdutosListener {

    private ListView lvListaProdutos;
    //private ArrayList<Produto> listaProdutos;
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
        lvListaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesProdutoActivity.class);
                intent.putExtra("ID" , (int)id);
                //startActivity(intent);
                startActivityForResult(intent,EDITAR);
            }
        });

        SingletonGestorEvc.getInstance(getContext()).getAllProdutosAPI(getContext());
        //SingletonGestorEvc.getInstance(getContext()).setProdutosListener(this);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ListaProdutosFragment.this);
                integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Produto Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        return view;
    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if (result.getContents() != null) {
                SingletonGestorEvc.getInstance(getContext()).getProdutoPesquisa(result.getContents(), getContext());
                produto(result.getContents());

            } else {
                Toast.makeText(getContext(), "Scan cancelado", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }
        //atualizar a lista
        //apresentar um toast
    }

    private void produto(String id){
        Intent intent = new Intent(getContext(), DetalhesProdutoActivity.class);
        intent.putExtra("ID" , Integer.parseInt(id));
        startActivity(intent);
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
    public void onRefreshListaProdutos(ArrayList<Produto> produtos) {
        if(produtos != null) {
            lvListaProdutos.setAdapter(new ListaProdutoAdaptador(getContext(), produtos));
        }
    }

    @Override
    public void onRefreshDetalhes() {
        //empty
    }

    @Override
    public void onLoadDetalhes(Produto produto) {

    }
}
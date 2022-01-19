package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.listeners.FavoritosListener;
import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;

public class DetalhesProdutoActivity extends AppCompatActivity implements FavoritosListener, ProdutosListener{

    public static final String ID = "ID";
    private static final String DEFAULT_IMAGE = "http://amsi.dei.estg.ipleiria.pt/img/ipl_semfundo.png";
    private Produto produto;
    private TextView tvCodigo_Produto, tvNome, tvGenero, tvDescricao, tvTamanho, tvPreco;
    private ImageView imgCapa;
    private String token;
    private Button btnAdicionarFavoritos, btnRemoverFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);


        int id = getIntent().getIntExtra(ID, -1);
        produto = SingletonGestorEvc.getInstance(getApplicationContext()).getProduto(id);

        tvCodigo_Produto = findViewById(R.id.tvCodigo_produto);
        tvNome = findViewById(R.id.tvNome);
        tvGenero = findViewById(R.id.tvGenero);
        tvDescricao = findViewById(R.id.tvDescricao);
        tvTamanho = findViewById(R.id.tvTamanho);
        tvPreco = findViewById(R.id.tvPreco);
        btnAdicionarFavoritos = findViewById(R.id.btnAddFav);
        btnRemoverFavoritos = findViewById(R.id.btnRemoveFav);

        SharedPreferences sharedPrefUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        token = sharedPrefUser.getString(MenuMainActivity.TOKEN, null);

        SingletonGestorEvc.getInstance(getApplicationContext()).setProdutosListener(this);

        if(produto != null) {
            Toolbar toolbar = findViewById(R.id.myToolBar);
            setSupportActionBar(toolbar);
            setTitle("Detalhes " + produto.getNome());
            carregarDetalhesProduto();
        }

        if (token != null){
            btnAdicionarFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
                    String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
                    SingletonGestorEvc.getInstance(getApplicationContext()).adicionarProdutoFavoritoAPI(getApplicationContext(), produto, token);
                }
            });

            btnRemoverFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
                    String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);

                    SingletonGestorEvc.getInstance(getApplicationContext()).removerProdutoFavoritoAPI(getApplicationContext(), produto, token);
                }
            });
        }else{
            btnAdicionarFavoritos.setVisibility(View.INVISIBLE);
            btnRemoverFavoritos.setVisibility(View.GONE);
        }

    }

    private void carregarDetalhesProduto() {
        tvCodigo_Produto.setText(produto.getCodigo_produto() + "");
        tvNome.setText(produto.getNome());
        tvGenero.setText(produto.getGenero());
        tvDescricao.setText(produto.getDescricao());
        tvTamanho.setText(produto.getTamanho());
        tvPreco.setText((int) produto.getPreco() + "");
    }


    @Override
    public void onAddProdutosFavoritos() {

        Toast.makeText(getApplicationContext(), "Ponto Turístico Removido dos favoritos!", Toast.LENGTH_SHORT).show();
        btnAdicionarFavoritos.setVisibility(View.VISIBLE);
        btnRemoverFavoritos.setVisibility(View.GONE);
    }

    @Override
    public void onRemoverProdutosFavoritos() {
        Toast.makeText(getApplicationContext(), "Ponto Turístico Removido dos favoritos!", Toast.LENGTH_SHORT).show();
        btnAdicionarFavoritos.setVisibility(View.VISIBLE);
        btnRemoverFavoritos.setVisibility(View.GONE);
    }

    @Override
    public void oncheckProdutoFavorito(Boolean favorito) {
        if(favorito){
            btnAdicionarFavoritos.setVisibility(View.GONE);
            btnRemoverFavoritos.setVisibility(View.VISIBLE);
        }else{
            btnAdicionarFavoritos.setVisibility(View.VISIBLE);
            btnRemoverFavoritos.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefreshListaFavoritosProdutos(ArrayList<Produto> produtos) {

    }

    @Override
    public void onNoFavoritos() {

    }

    @Override
    public void onRefreshListaProdutos(ArrayList<Produto> listaProdutos) {

    }

    @Override
    public void onRefreshDetalhes() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoadDetalhes(Produto produto) {

    }
}
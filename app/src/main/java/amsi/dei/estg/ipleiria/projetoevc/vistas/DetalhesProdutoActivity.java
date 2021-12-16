package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;

public class DetalhesProdutoActivity extends AppCompatActivity implements ProdutosListener {

    public static final String ID = "ID";
    private static final String DEFAULT_IMAGE = "http://amsi.dei.estg.ipleiria.pt/img/ipl_semfundo.png";
    private Produto produto;
    private TextView tvCodigo_Produto, tvNome, tvGenero, tvDescricao, tvTamanho, tvPreco;
    private ImageView imgCapa;
    private String token;

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

        SharedPreferences sharedPrefUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        token = sharedPrefUser.getString(MenuMainActivity.TOKEN, null);

        SingletonGestorEvc.getInstance(getApplicationContext()).setProdutosListener(this);

        if(produto != null) {
            Toolbar toolbar = findViewById(R.id.myToolBar);
            setSupportActionBar(toolbar);
            setTitle("Detalhes " + produto.getNome());
            carregarDetalhesProduto();
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
    public void onRefreshListaProdutos(ArrayList<Produto> produtos) {

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
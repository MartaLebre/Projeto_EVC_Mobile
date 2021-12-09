package amsi.dei.estg.ipleiria.projetoevc.vistas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.listeners.ProdutosListener;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.utils.ProdutoJsonParser;

public class DetalhesProdutoFragment extends AppCompatActivity implements ProdutosListener {

    public static final String ID = "ID";
    private static final String DEFAULT_IMAGE = "http://amsi.dei.estg.ipleiria.pt/img/ipl_semfundo.png";
    private Produto produto;
    private TextView etCodigo_Produto, etNome, etGenero, etDescricao, etTamanho, etPreco;
    private ImageView imgCapa;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detalhes_produto);

        int id = getIntent().getIntExtra(ID, -1);
        produto = SingletonGestorEvc.getInstance(getApplicationContext()).getProduto(id);

        etCodigo_Produto = findViewById(R.id.etCodigo_Produto);
        etNome = findViewById(R.id.etNome);
        etGenero = findViewById(R.id.etGenero);
        etDescricao = findViewById(R.id.etDescricao);
        etTamanho = findViewById(R.id.etTamanho);
        etPreco = findViewById(R.id.etPreco);
        //FloatingActionButton fab = findViewById(R.id.fab);

        SharedPreferences sharedPrefUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        token = sharedPrefUser.getString(MenuMainActivity.TOKEN, null);

        SingletonGestorEvc.getInstance(getApplicationContext()).setProdutosListener(this);

        if(produto != null) {
            setTitle("Detalhes " + produto.getNome());
            carregarDetalhesProduto();
            //ab.setImageResource(R.drawable.ic_action_guardar);
        }
    }

    private void carregarDetalhesProduto() {
        etCodigo_Produto.setText(produto.getCodigo_produto() + "");
        etNome.setText(produto.getNome());
        etGenero.setText(produto.getGenero());
        etDescricao.setText(produto.getDescricao());
        etTamanho.setText(produto.getTamanho());
        etPreco.setText((int) produto.getPreco() + "");
        //imgCapa.setImageResource(livro.getCapa());
    }

    @Override
    public void onRefreshListaProdutos(ArrayList<Produto> produtos) {

    }

    @Override
    public void onRefreshDetalhes() {
        setResult(RESULT_OK);
        finish();
    }
}
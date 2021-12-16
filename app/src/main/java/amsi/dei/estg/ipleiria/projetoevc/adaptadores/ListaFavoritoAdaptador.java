package amsi.dei.estg.ipleiria.projetoevc.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.vistas.MenuMainActivity;

public class ListaFavoritoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Produto> produtos;

    public ListaFavoritoAdaptador(Context context, ArrayList<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return produtos.get(position).getCodigo_produto();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_lista_favorito, null);
        }

        //otimização

        ViewHolderLista viewHolderLista = (ViewHolderLista)convertView.getTag();
        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(convertView);
            convertView.setTag(viewHolderLista);
        }
        viewHolderLista.update(produtos.get(position));

        viewHolderLista.btnRemoveFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferencesUser = context.getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
                String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
                SingletonGestorEvc.getInstance(context).removerProdutoFavoritoAPI(context, produtos.get(position), token);
            }
        });


        return convertView;
    }

    private class ViewHolderLista{
        private TextView tvNome, tvTamanho;
        private Button btnRemoveFav;

        public ViewHolderLista(View view){
            tvNome = view.findViewById(R.id.tvNome);
            tvTamanho = view.findViewById(R.id.tvTamanho);
            btnRemoveFav = view.findViewById(R.id.btn_removerFavoritos);
        }

        public void update(Produto produto){
            tvNome.setText(produto.getNome());
            tvTamanho.setText(produto.getTamanho());
        }
    }
}

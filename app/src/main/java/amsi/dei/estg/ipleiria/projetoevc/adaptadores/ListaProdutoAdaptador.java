package amsi.dei.estg.ipleiria.projetoevc.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;

public class ListaProdutoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Produto> produtos;

    public ListaProdutoAdaptador(Context context, ArrayList<Produto> produtos) {
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
            convertView = inflater.inflate(R.layout.item_lista_produto, null);
        }

        //otimização

        ViewHolderLista viewHolderLista = (ViewHolderLista)convertView.getTag();
        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(convertView);
            convertView.setTag(viewHolderLista);
        }
        viewHolderLista.update(produtos.get(position));


        return convertView;
    }

    private class ViewHolderLista{
        private TextView tvNome, tvTamanho;
        private ImageView logo;

        public ViewHolderLista(View view){
            tvNome = view.findViewById(R.id.tvNome);
            tvTamanho = view.findViewById(R.id.tvTamanho);
            logo = view.findViewById(R.id.logo);
        }

        public void update(Produto produto){
            tvNome.setText(produto.getNome());
            tvTamanho.setText(produto.getTamanho());
            Glide.with(context)
                    .load(produto.getFoto())
                    .placeholder(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(logo);
        }
    }
}

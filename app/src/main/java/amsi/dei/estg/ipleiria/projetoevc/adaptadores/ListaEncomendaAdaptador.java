package amsi.dei.estg.ipleiria.projetoevc.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.R;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Encomenda;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.vistas.MenuMainActivity;

public class ListaEncomendaAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Encomenda> encomendas;

    public ListaEncomendaAdaptador(Context context, ArrayList<Encomenda> encomendas) {
        this.context = context;
        this.encomendas = encomendas;
    }

    @Override
    public int getCount() {
        return encomendas.size();
    }

    @Override
    public Object getItem(int position) {
        return encomendas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return encomendas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_lista_encomenda, null);
        }

        //otimização

        ViewHolderLista viewHolderLista = (ViewHolderLista)convertView.getTag();
        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(convertView);
            convertView.setTag(viewHolderLista);
        }
        viewHolderLista.update(encomendas.get(position));


        return convertView;
    }

    private class ViewHolderLista{
        private TextView tvId, tvEstado, tvData;

        public ViewHolderLista(View view){
            tvId = view.findViewById(R.id.tvId);
            tvEstado = view.findViewById(R.id.tvEstado);
            tvData = view.findViewById(R.id.tvData);

        }

        public void update(Encomenda encomenda){
           tvId.setText(String.valueOf(encomenda.getId()));
            tvEstado.setText(encomenda.getEstado());
            tvData.setText(encomenda.getData());


        }
    }
}

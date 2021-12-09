package amsi.dei.estg.ipleiria.projetoevc.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;

public interface ProdutosListener {

        void onRefreshListaProdutos (ArrayList<Produto> listaProdutos);

        void onRefreshDetalhes();
}

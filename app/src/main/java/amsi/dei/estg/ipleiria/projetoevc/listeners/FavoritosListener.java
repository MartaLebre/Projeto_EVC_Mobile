package amsi.dei.estg.ipleiria.projetoevc.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.modelo.Produto;

public interface FavoritosListener {

    void onAddProdutosFavoritos();

    void onRemoverProdutosFavoritos();

    void oncheckProdutoFavorito(Boolean favorito);

    void onRefreshListaFavoritosProdutos(ArrayList<Produto> produtos);

    void onNoFavoritos();
}

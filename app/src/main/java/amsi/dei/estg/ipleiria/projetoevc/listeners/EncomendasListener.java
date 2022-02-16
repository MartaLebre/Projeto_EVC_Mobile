package amsi.dei.estg.ipleiria.projetoevc.listeners;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projetoevc.adaptadores.ListaFavoritoAdaptador;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Encomenda;
import amsi.dei.estg.ipleiria.projetoevc.modelo.SingletonGestorEvc;
import amsi.dei.estg.ipleiria.projetoevc.modelo.Utilizador;
import amsi.dei.estg.ipleiria.projetoevc.vistas.MenuMainActivity;

public interface EncomendasListener {


    void onResume();

    void onRefreshListaEncomendas(ArrayList<Encomenda> encomendas);


    void onRefresh();

}


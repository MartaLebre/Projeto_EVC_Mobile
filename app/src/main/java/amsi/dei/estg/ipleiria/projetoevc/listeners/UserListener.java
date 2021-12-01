package amsi.dei.estg.ipleiria.projetoevc.listeners;

import amsi.dei.estg.ipleiria.projetoevc.modelo.Utilizador;

public interface UserListener {
    void onUserRegistado(String response);

    void onValidateLogin(String token, String username);

    void onRefreshDetalhes(String response);

    void onApagarConta(String response);

    void onErroLogin();

    void onLoadEditarRegisto(Utilizador utilizador);


}

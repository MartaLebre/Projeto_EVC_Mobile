package amsi.dei.estg.ipleiria.projetoevc.listeners;

import amsi.dei.estg.ipleiria.projetoevc.modelo.Utilizador;

public interface UserListener {
    public void onUserRegistado(String response);

    void onRefreshDetalhes(String response);

    void onApagarConta(String response);

    void onValidateLogin(String token, String username);

    void onLoadEditarRegisto(Utilizador utilizador);

    void onErroLogin();
}

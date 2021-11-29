package amsi.dei.estg.ipleiria.projetoevc.listeners;

public interface UserListener {
    public void onUserRegistado(String response);

    void onRefreshDetalhes(String response);

    void onApagarConta(String response);

    void onValidateLogin(String token, String username);
}

package amsi.dei.estg.ipleiria.projetoevc.modelo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilizador {

        private int id;
        private String username, email, password, primeiroNome, ultimoNome, numeroTelemovel;

        private Pattern pattern;
        private Matcher matcher;


    public Utilizador(String username, String email, String password, String primeiro_nome, String ultimo_nome, String telemovel) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.primeiroNome = primeiro_nome;
        this.ultimoNome = ultimo_nome;
        this.numeroTelemovel = telemovel;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public String getNumeroTelemovel() {
        return numeroTelemovel;
    }

    public void setNumeroTelemovel(String numeroTelemovel) {
        this.numeroTelemovel = numeroTelemovel;
    }




}
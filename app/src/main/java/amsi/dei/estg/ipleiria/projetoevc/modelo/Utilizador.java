package amsi.dei.estg.ipleiria.projetoevc.modelo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilizador {

        private int id;
        private String username, email, password, primeiro_nome, ultimo_nome, telemovel;

        private Pattern pattern;
        private Matcher matcher;

    public Utilizador(String username, String email, String password, String primeiro_nome, String ultimo_nome, String telemovel) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.primeiro_nome = primeiro_nome;
        this.ultimo_nome = ultimo_nome;
        this.telemovel = telemovel;
        
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

    public String getPrimeiro_nome() {
        return primeiro_nome;
    }

    public void setPrimeiro_nome(String primeiro_nome) {
        this.primeiro_nome = primeiro_nome;
    }

    public String getUltimo_nome() {
        return ultimo_nome;
    }

    public void setUltimo_nome(String ultimo_nome) {
        this.ultimo_nome = ultimo_nome;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }




}
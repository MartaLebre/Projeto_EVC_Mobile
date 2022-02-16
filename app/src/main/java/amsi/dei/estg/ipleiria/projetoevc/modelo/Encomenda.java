package amsi.dei.estg.ipleiria.projetoevc.modelo;

public class Encomenda {

    private int id, id_user;
    private String data, estado;
    //o atributo autoIncrementedId é static: comum a todas as instâncias/objetos da classe
    //private static int autoIncrementedId = 1;

    public Encomenda(int id, String estado, String data, int id_user) {

        this.id = id;
        this.estado = estado;
        this.data = data;
        this.id_user = id_user;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }





}

package amsi.dei.estg.ipleiria.projetoevc.modelo;

public class Produto {
    private int codigo_produto, quantidade, id_modelo;
    private float preco;
    private String nome, genero, descricao, tamanho, data, foto;
    //o atributo autoIncrementedId é static: comum a todas as instâncias/objetos da classe
    //private static int autoIncrementedId = 1;

    public Produto(int codigo_produto, String nome, String genero, String descricao, String tamanho, float preco, int quantidade, String data, int id_modelo, String foto) {

        this.codigo_produto = codigo_produto;
        this.nome = nome;
        this.genero = genero;
        this.descricao = descricao;
        this.tamanho = tamanho;
        this.preco = preco;
        this.quantidade = quantidade;
        this.data = data;
        this.id_modelo = id_modelo;
        this.foto = foto;
    }

    public int getCodigo_produto() {
        return codigo_produto;
    }

    public void setCodigo_produto(int codigo_produto) {
        this.codigo_produto = codigo_produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getId_modelo() {
        return id_modelo;
    }

    public void setId_modelo(int id_modelo) {
        this.id_modelo = id_modelo;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

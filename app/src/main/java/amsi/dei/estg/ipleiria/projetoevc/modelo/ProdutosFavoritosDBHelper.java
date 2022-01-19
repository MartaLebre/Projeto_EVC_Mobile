package amsi.dei.estg.ipleiria.projetoevc.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ProdutosFavoritosDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="yii2_evc";
    private static final int DB_VERSION=1;

    private static final String TABLE_NAME = "favorito";

    private static final String CODIGO_PRODUTO="codigo_produto";
    private static final String NOME_PRODUTO = "nome_produto";
    private static final String GENERO_PRODUTO = "genero_produto";
    private static final String DESCRICAO_PRODUTO = "descricao_produto";
    private static final String TAMANHO_PRODUTO = "tamanho_produto";
    private static final String PRECO_PRODUTO = "preco_produto";
    private static final String QUANTIDADE_PRODUTO = "quantidade_produto";
    private static final String DATA_PRODUTO = "data_produto";
    private static final String ID_MODELO = "id_modelo";
    private static final String FOTO_PRODUTO = "foto_produto";

    private final SQLiteDatabase db;

    public ProdutosFavoritosDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableProdutoFavorito="CREATE TABLE "+TABLE_NAME+" ("+
                CODIGO_PRODUTO+" INTEGER PRIMARY KEY, "+
                NOME_PRODUTO+" TEXT NOT NULL , "+
                GENERO_PRODUTO + " TEXT NOT NULL, " +
                DESCRICAO_PRODUTO + " TEXT NOT NULL, " +
                TAMANHO_PRODUTO + " TEXT NOT NULL, " +
                PRECO_PRODUTO + " FLOAT NOT NULL, " +
                QUANTIDADE_PRODUTO + " INTEGER NOT NULL, " +
                DATA_PRODUTO + " DATETIME NOT NULL, " +
                ID_MODELO + " INTEGER, " +
                FOTO_PRODUTO + " TEXT);";
        db.execSQL(sqlCreateTableProdutoFavorito);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDropTableProdutoFavorito="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(sqlDropTableProdutoFavorito);
        this.onCreate(db);
    }

    /**
     * INSERT
     * o método insert() -> return idlivro (long), se houver erro devolve -1
     * @param produto
     * @return
     */
    public void adicionarProdutoFavoritoBD(Produto produto) {
        ContentValues values = new ContentValues();
        values.put(CODIGO_PRODUTO, produto.getCodigo_produto());
        values.put(NOME_PRODUTO, produto.getNome());
        values.put(GENERO_PRODUTO, produto.getGenero());
        values.put(DESCRICAO_PRODUTO, produto.getDescricao());
        values.put(TAMANHO_PRODUTO, produto.getTamanho());
        values.put(PRECO_PRODUTO, produto.getPreco());
        values.put(QUANTIDADE_PRODUTO, produto.getQuantidade());
        values.put(DATA_PRODUTO, produto.getData());
        values.put(ID_MODELO, produto.getId_modelo());
        values.put(FOTO_PRODUTO, produto.getFoto());

        this.db.insert(TABLE_NAME, null, values);
    }


    /**
     * DELETE
     * @param codigo_produto
     * @return
     */
    public boolean removerProdutoFavoritoBD(int codigo_produto){
        return this.db.delete(TABLE_NAME, "codigo_produto=?", new String[]{codigo_produto + ""}) > 0;
    }

    public void removerAllProdutosFavoritosBD(){
        this.db.delete(TABLE_NAME,  null, null);
    }

    /**
     * SELECT
     * this.db.rawQuery("codigo sql", null) -> suscetivel de SQLINJECTION
     * @return
     */
    public ArrayList<Produto> getAllProdutosFavoritosBD(){
        ArrayList<Produto> produtos = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME, new String[]{CODIGO_PRODUTO, NOME_PRODUTO, GENERO_PRODUTO, DESCRICAO_PRODUTO, TAMANHO_PRODUTO, PRECO_PRODUTO, QUANTIDADE_PRODUTO, DATA_PRODUTO, ID_MODELO, FOTO_PRODUTO},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                Produto auxProdutoFavorito = new Produto(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getFloat(5), cursor.getInt(6), cursor.getString(7),
                        cursor.getInt(8), cursor.getString(9));
                produtos.add(auxProdutoFavorito);
            }while(cursor.moveToNext());
        }
        return produtos;
    }
}

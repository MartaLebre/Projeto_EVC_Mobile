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
    private static final String ID="ID";
    private static final String CODIGO_PRODUTO="codigo_produto";
    private static final String ID_USER="id_user";

    private final SQLiteDatabase db;

    public ProdutosFavoritosDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableProduto="CREATE TABLE "+TABLE_NAME+" ("+
                ID+" INTEGER PRIMARY KEY, "+
                CODIGO_PRODUTO+" INTEGER , "+
                ID_USER+" INTEGER );";
        db.execSQL(sqlCreateTableProduto);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDropTableProduto="DROP TABLE IF EXISTS "+TABLE_PRODUTOS;
        db.execSQL(sqlDropTableProduto);
        this.onCreate(db);
    }

    /**
     * INSERT
     * o método insert() -> return idlivro (long), se houver erro devolve -1
     * @param produto
     * @return
     */
    public void adicionarProdutoBD(Produto produto) {
        ContentValues values = new ContentValues();
        values.put(CODIGO_PRODUTO, produto.getCodigo_produto());
        values.put(NOME_PRODUTO, produto.getNome());
        values.put(GENERO_PRODUTO, produto.getGenero());
        values.put(DESCRICAO_PRODUTO, produto.getDescricao());
        values.put(TAMANHO_PRODUTO, produto.getTamanho());
        values.put(PRECO_PRODUTO, produto.getPreco());

        this.db.insert(TABLE_PRODUTOS, null, values);
    }
    /**
     * UPDATE
     * o método update() -> return o nº de linhas alteradas
     * @param produto
     * @return
     */
    public boolean editarProdutoBD(Produto produto){
        ContentValues values = new ContentValues();
        values.put(CODIGO_PRODUTO, produto.getCodigo_produto());
        values.put(NOME_PRODUTO, produto.getNome());
        values.put(GENERO_PRODUTO, produto.getGenero());
        values.put(DESCRICAO_PRODUTO, produto.getDescricao());
        values.put(TAMANHO_PRODUTO, produto.getTamanho());
        values.put(PRECO_PRODUTO, produto.getPreco());

        return this.db.update(TABLE_PRODUTOS, values, "codigo_produto=?", new String[]{produto.getCodigo_produto() + ""}) > 0;
    }

    /**
     * DELETE
     * @param codigo_produto
     * @return
     */
    public boolean removerProdutoBD(int codigo_produto){
        return this.db.delete(TABLE_PRODUTOS, "codigo_produto=?", new String[]{codigo_produto + ""}) > 0;
    }

    public void removerAllProdutosBD(){
        this.db.delete(TABLE_PRODUTOS,  null, null);
    }

    /**
     * SELECT
     * this.db.rawQuery("codigo sql", null) -> suscetivel de SQLINJECTION
     * @return
     */
    public ArrayList<Produto> getAllProdutosBD(){
        ArrayList<Produto> produtos = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_PRODUTOS, new String[]{CODIGO_PRODUTO, NOME_PRODUTO, GENERO_PRODUTO, DESCRICAO_PRODUTO, TAMANHO_PRODUTO, PRECO_PRODUTO},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                Produto auxProduto = new Produto(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getFloat(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8), cursor.getString(9));
                produtos.add(auxProduto);
            }while(cursor.moveToNext());
        }
        return produtos;
    }
}

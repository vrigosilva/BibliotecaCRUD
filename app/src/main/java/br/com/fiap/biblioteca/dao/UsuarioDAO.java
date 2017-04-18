package br.com.fiap.biblioteca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.fiap.biblioteca.Modelo.Usuario;

public class UsuarioDAO {
    private DBOpenHelper banco;

    public UsuarioDAO(Context context) {
        banco = new DBOpenHelper(context);
    }

    public static final String TABELA_USUARIO     = "USUARIO";
    public static final String COLUNA_USUARIO     = "USUARIO";
    public static final String COLUNA_SENHA       = "SENHA";



    public Usuario getBy(String usuario, String senha) {
        SQLiteDatabase db = banco.getReadableDatabase();

        String colunas[] = {COLUNA_USUARIO,COLUNA_SENHA};
        String where = COLUNA_USUARIO + " =? and " + COLUNA_SENHA +" =? ";
        String[] values = new String[]{usuario,senha};
        Cursor cursor = db.query(true, TABELA_USUARIO, colunas, where, values, null, null, null, null);
        Usuario user = null;

        if (cursor != null) {
            cursor.moveToFirst();
            user = new Usuario();
            user.setUsuario(cursor.getString(cursor.getColumnIndex(COLUNA_USUARIO)));
            user.setSenha(cursor.getString(cursor.getColumnIndex(COLUNA_SENHA)));
        }
        return user;
    }

    public boolean persist(Usuario usuario) {
        long resultado;
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUNA_USUARIO, usuario.getUsuario());
        values.put(COLUNA_SENHA, usuario.getSenha());

        resultado = db.insert(TABELA_USUARIO, null, values);

        db.close();
        return (resultado != -1);
    }


    public boolean remove(){
        SQLiteDatabase db = banco.getWritableDatabase();
        long resultado = db.delete(TABELA_USUARIO,null,null);
        db.close();
        return (resultado != -1);
    }
}

package br.com.fiap.biblioteca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.com.fiap.biblioteca.Modelo.Livro;

public class LivroDAO {
    private DBOpenHelper banco;

    public LivroDAO(Context context) {
        banco = new DBOpenHelper(context);
    }

    public static final String TABELA_LIVRO     = "LIVRO";
    public static final String COLUNA_ID        = "ID";
    public static final String COLUNA_TITULO    = "TITULO";
    public static final String COLUNA_AUTOR     = "AUTOR";
    public static final String COLUNA_PAGINAS   = "PAGINAS";
    public static final String COLUNA_ISBN      = "ISBN";
    public static final String COLUNA_NACIONAL  = "NACIONAL";

    public List<Livro> getAll() {
        List<Livro> livros = new LinkedList<>();
        String colunas[] = {COLUNA_ID,COLUNA_ID,COLUNA_TITULO,COLUNA_AUTOR,COLUNA_PAGINAS,COLUNA_ISBN,COLUNA_NACIONAL};
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor cursor = db.query(true, TABELA_LIVRO, colunas, null, null, null, null, null, null);
        Livro livro = null;

        if (cursor.moveToFirst()) {
            do {
                livro = new Livro();
                livro.setId(cursor.getInt(cursor.getColumnIndex(COLUNA_ID)));
                livro.setTitulo(cursor.getString(cursor.getColumnIndex(COLUNA_TITULO)));
                livro.setAutor(cursor.getString(cursor.getColumnIndex(COLUNA_AUTOR)));
                livro.setPaginas(cursor.getInt(cursor.getColumnIndex(COLUNA_PAGINAS)));
                livro.setISBN(cursor.getLong(cursor.getColumnIndex(COLUNA_ISBN)));
                livro.setNacional(cursor.getInt(cursor.getColumnIndex(COLUNA_NACIONAL))>0);
                livros.add(livro);
            } while (cursor.moveToNext());
        }
        return livros;
    }

    public Livro getBy(int id) {
        SQLiteDatabase db = banco.getReadableDatabase();
        String colunas[] = {COLUNA_ID,COLUNA_ID,COLUNA_TITULO,COLUNA_AUTOR,COLUNA_PAGINAS,COLUNA_ISBN,COLUNA_NACIONAL};
        String where = "id = " + id;
        Cursor cursor = db.query(true, TABELA_LIVRO, colunas, where, null, null, null, null, null);
        Livro livro = null;

        if (cursor != null) {
            cursor.moveToFirst();
            livro = new Livro();
            livro.setId(cursor.getInt(cursor.getColumnIndex(COLUNA_ID)));
            livro.setTitulo(cursor.getString(cursor.getColumnIndex(COLUNA_TITULO)));
            livro.setAutor(cursor.getString(cursor.getColumnIndex(COLUNA_AUTOR)));
            livro.setPaginas(cursor.getInt(cursor.getColumnIndex(COLUNA_PAGINAS)));
            livro.setISBN(cursor.getLong(cursor.getColumnIndex(COLUNA_ISBN)));
            livro.setNacional(cursor.getInt(cursor.getColumnIndex(COLUNA_NACIONAL))>0);
        }
        return livro;
    }

    public boolean persist(Livro livro) {
        long resultado;
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUNA_TITULO, livro.getTitulo());
        values.put(COLUNA_AUTOR, livro.getAutor());
        values.put(COLUNA_PAGINAS, livro.getPaginas());
        values.put(COLUNA_ISBN, livro.getISBN());
        values.put(COLUNA_NACIONAL, livro.isNacional() ? 1:0);

        resultado = db.insert(TABELA_LIVRO, null, values);

        db.close();
        return (resultado != -1);
    }

    public boolean update(Livro livro) {
        long resultado;
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUNA_TITULO, livro.getTitulo());
        values.put(COLUNA_AUTOR, livro.getAutor());
        values.put(COLUNA_PAGINAS, livro.getPaginas());
        values.put(COLUNA_ISBN, livro.getISBN());
        values.put(COLUNA_NACIONAL, livro.isNacional() ? 1:0);

        String where = COLUNA_ID +"=?";

        resultado = db.update(TABELA_LIVRO, values, where, new String[] {livro.getId().toString()});

        db.close();
        return (resultado != -1);
    }

    public boolean remove(int id){
        SQLiteDatabase db = banco.getWritableDatabase();
        String where = "id = ?";
        String[] values = new String[]{String.valueOf(id)};

        return db.delete(TABELA_LIVRO, where, values) != -1;
    }
}

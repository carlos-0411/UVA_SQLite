package com.example.catalogodelivros;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LivroDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "livros.db"; // Nome do banco de dados
    private static final int DB_VERSION = 1; // Versão do banco de dados

    public LivroDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION); // Construtor da classe
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE livros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "autor TEXT NOT NULL," +
                "ano INTEGER NOT NULL)"); // Cria a tabela "livros" no banco de dados
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS livros"); // Remove a tabela "livros" do banco de dados
        onCreate(db); // Cria a tabela "livros" novamente
    }
}

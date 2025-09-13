package com.example.catalogodelivros;
public class Livro {
    private long id;
    private String titulo;
    private String autor;
    private int ano;

    public Livro(long id, String titulo, String autor, int ano) {
        this.id = id; // Parâmetro de ID
        this.titulo = titulo; // Parâmetro de título
        this.autor = autor; // Parâmetro de autor
        this.ano = ano; // Parâmetro de ano
    }

    public long getId() {
        return id; // Retorna o ID
    }

    public String getTitulo() {
        return titulo; // Retorna o título
    }

    public String getAutor() {
        return autor; // Retorna o autor
    }

    public int getAno() {
        return ano; // Retorna o ano
    }

    @Override
    public String toString() {
        return titulo + " - " + autor + " (" + ano + ")"; // Retorna uma representação em string do livro
    }
}


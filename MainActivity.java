package com.example.catalogodelivros;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LivroDBHelper dbHelper; // Classe de banco de dados
    private ListView listView; // ListView para exibir a lista de livros
    private List<Livro> livros; // Lista de livros
    private LivroAdapter adapter; // Adaptador para a lista
    private EditText edtTitulo, edtAutor, edtAno; // Campos de entrada de texto
    private Button btnSalvar; // Botão para salvar
    private Long livroSelecionadoId = null; // ID do livro selecionado para edição

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new LivroDBHelper(this); // Inicializa a classe de banco de dados
        edtTitulo = findViewById(R.id.edtTitulo); // Encontra o campo de entrada de texto para título
        edtAutor = findViewById(R.id.edtAutor); // Encontra o campo de entrada de texto para autor
        edtAno = findViewById(R.id.edtAno); // Encontra o campo de entrada de texto para ano
        btnSalvar = findViewById(R.id.btnSalvar); // Encontra o botão para salvar
        listView = findViewById(R.id.listViewLivros); // Encontra a ListView para exibir a lista de livros

        btnSalvar.setOnClickListener(v -> salvarLivro()); // Define o listener para o botão de salvar
        carregarLivros(); // Carrega a lista de livros ao iniciar a atividade
    }

    private void salvarLivro() {
        String titulo = edtTitulo.getText().toString().trim(); // Obtém o texto do campo de entrada de texto para título
        String autor = edtAutor.getText().toString().trim(); // Obtém o texto do campo de entrada de texto para autor
        String anoStr = edtAno.getText().toString().trim(); // Obtém o texto do campo de entrada de texto para ano

        if (titulo.isEmpty() || autor.isEmpty() || edtAno.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show(); // Exibe uma mensagem de erro se algum campo estiver vazio
            return; // Retorna se algum campo estiver vazio
        }

        int ano; // Variável para armazenar o ano
        try {
            ano = Integer.parseInt(anoStr); // Converte a string para um inteiro)
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ano inválido", Toast.LENGTH_SHORT).show(); // Exibe uma mensagem de erro se o ano for inválido
            return; // Retorna se o ano for inválido
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Obtém a instância do banco de dados para escrita
        ContentValues valores = new ContentValues(); // Cria um objeto ContentValues para armazenar os valores a serem inseridos no banco de dados
        valores.put("titulo", titulo); // Insere os valores no ContentValues
        valores.put("autor", autor); // Insere os valores no ContentValues
        valores.put("ano", ano); // Insere os valores no ContentValues

        if (livroSelecionadoId == null) {
            db.insert("livros", null, valores); // Insere os valores no banco de dados
            Toast.makeText(this, "Livro inserido com sucesso", Toast.LENGTH_SHORT).show(); // Exibe uma mensagem de sucesso
        } else {
            db.update("livros", valores, "id = ?", new String[]{String.valueOf(livroSelecionadoId)}); // Atualiza os valores no banco de dados
            Toast.makeText(this, "Livro atualizado com sucesso", Toast.LENGTH_SHORT).show(); // Exibe uma mensagem de sucesso
            livroSelecionadoId = null; // Reseta o ID do livro selecionado
        }

        edtTitulo.setText(""); // Limpa os campos de entrada de texto
        edtAutor.setText(""); // Limpa os campos de entrada de texto
        edtAno.setText(""); // Limpa os campos de entrada de texto
        carregarLivros(); // Carrega a lista de livros após a operação de salvar
    }

    private void carregarLivros() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("livros", null, null, null, null, null, null);
        livros = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
            String autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"));
            int ano = cursor.getInt(cursor.getColumnIndexOrThrow("ano"));
            livros.add(new Livro(id, titulo, autor, ano));
        }
        cursor.close();

        adapter = new LivroAdapter(this, livros, new LivroAdapter.OnLivroActionListener() {
            @Override
            public void onEditar(Livro livro) {
                edtTitulo.setText(livro.getTitulo());
                edtAutor.setText(livro.getAutor());
                edtAno.setText(String.valueOf(livro.getAno()));
                livroSelecionadoId = livro.getId();
            }

            @Override
            public void onExcluir(Livro livro) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Excluir Livro")
                        .setMessage("Deseja excluir \"" + livro.getTitulo() + "\"?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.delete("livros", "id = ?", new String[]{String.valueOf(livro.getId())});
                            carregarLivros();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        listView.setAdapter(adapter);

    }
}
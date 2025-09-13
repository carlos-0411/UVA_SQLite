package com.example.catalogodelivros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LivroAdapter extends ArrayAdapter<Livro> {
    private Context context;
    private List<Livro> livros;
    private OnLivroActionListener listener;

    public interface OnLivroActionListener {
        void onEditar(Livro livro);

        void onExcluir(Livro livro);
    }

    public LivroAdapter(Context context, List<Livro> livros, OnLivroActionListener listener) {
        super(context, 0, livros);
        this.context = context;
        this.livros = livros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Livro livro = livros.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_livro, parent, false);
        }

        TextView txtTitulo = convertView.findViewById(R.id.txtTitulo);
        TextView txtAutorAno = convertView.findViewById(R.id.txtAutorAno);
        ImageButton btnEditar = convertView.findViewById(R.id.btnEditar);
        ImageButton btnExcluir = convertView.findViewById(R.id.btnExcluir);

        txtTitulo.setText(livro.getTitulo());
        txtAutorAno.setText(livro.getAutor() + " - " + livro.getAno());

        btnEditar.setOnClickListener(v -> listener.onEditar(livro));
        btnExcluir.setOnClickListener(v -> listener.onExcluir(livro));

        return convertView;
    }
}
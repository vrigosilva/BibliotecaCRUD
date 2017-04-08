package br.com.fiap.biblioteca.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.fiap.biblioteca.Modelo.Livro;
import br.com.fiap.biblioteca.R;
import br.com.fiap.biblioteca.interfaces.RecyclerViewOnClickListener;

/**
 * Created by helena on 23/02/2017.
 */

public class LivroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Livro> livros;
    private RecyclerViewOnClickListener recyclerViewOnClickListener;

    public LivroAdapter(Context context, List<Livro> livros) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.livros = livros;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.livro_item_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.tvTitulo.setText(livros.get(position).getTitulo());
        itemHolder.tvAutor.setText(livros.get(position).getAutor());
        itemHolder.tvPaginas.setText(livros.get(position).getPaginas().toString());

    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener r){
        this.recyclerViewOnClickListener = r;
    }

    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivIcone;
        TextView tvTitulo, tvAutor, tvPaginas;

        public ItemHolder(View itemView) {
            super(itemView);

            ivIcone = (ImageView) itemView.findViewById(R.id.ivItemCapa);
            tvTitulo = (TextView) itemView.findViewById(R.id.tvItemTitulo);
            tvPaginas = (TextView) itemView.findViewById(R.id.tvItemPaginas);
            tvAutor = (TextView) itemView.findViewById(R.id.tvItemAutor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListener != null){
                recyclerViewOnClickListener.onClickListener(v, getLayoutPosition());
            }

        }
    }
}

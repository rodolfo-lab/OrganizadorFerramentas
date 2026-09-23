package com.example.organizadorferramentas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class FerramentaAdapter extends BaseAdapter {

    private final Context context;
    private final List<Ferramenta> ferramentas;
    private final LayoutInflater inflater;

    public FerramentaAdapter(Context context, List<Ferramenta> ferramentas) {
        this.context = context;
        this.ferramentas = ferramentas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ferramentas.size();
    }

    @Override
    public Ferramenta getItem(int position) {
        return ferramentas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_ferramenta, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ferramenta ferramenta = getItem(position);

        holder.nome.setText(ferramenta.getNome());
        holder.codigoCategoria.setText(context.getString(R.string.item_codigo_categoria,
                ferramenta.getCodigo(), ferramenta.getCategoria()));
        holder.localizacao.setText(context.getString(R.string.item_localizacao,
                ferramenta.getLocalizacao()));
        holder.estado.setText(context.getString(R.string.item_estado, ferramenta.getEstado()));

        if (ferramenta.isDisponivel()) {
            holder.disponibilidade.setText(R.string.item_disponivel);
            holder.disponibilidade.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.disponivel));
        } else {
            holder.disponibilidade.setText(R.string.item_indisponivel);
            holder.disponibilidade.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.indisponivel));
        }

        return convertView;
    }

    private static class ViewHolder {

        final TextView nome;
        final TextView codigoCategoria;
        final TextView localizacao;
        final TextView estado;
        final TextView disponibilidade;

        ViewHolder(View linha) {
            nome = linha.findViewById(R.id.tvItemNome);
            codigoCategoria = linha.findViewById(R.id.tvItemCodigoCategoria);
            localizacao = linha.findViewById(R.id.tvItemLocalizacao);
            estado = linha.findViewById(R.id.tvItemEstado);
            disponibilidade = linha.findViewById(R.id.tvItemDisponibilidade);
        }
    }
}

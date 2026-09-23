package com.example.organizadorferramentas;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaFerramentasActivity extends AppCompatActivity {

    private ArrayList<Ferramenta> ferramentas;
    private ListView listaFerramentas;
    private FerramentaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ferramentas);

        ferramentas = carregarFerramentas();

        adapter = new FerramentaAdapter(this, ferramentas);

        listaFerramentas = findViewById(R.id.listaFerramentas);
        listaFerramentas.setAdapter(adapter);

        listaFerramentas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ferramenta ferramenta = ferramentas.get(position);

                String disponibilidade = getString(ferramenta.isDisponivel()
                        ? R.string.status_disponivel
                        : R.string.status_emprestada);

                String mensagem = getString(R.string.item_clicado,
                        ferramenta.getCodigo(),
                        ferramenta.getNome(),
                        ferramenta.getCategoria(),
                        ferramenta.getLocalizacao(),
                        disponibilidade);

                Toast.makeText(ListaFerramentasActivity.this, mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<Ferramenta> carregarFerramentas() {
        String[] nomes = getResources().getStringArray(R.array.ferramentas_nomes);
        String[] codigos = getResources().getStringArray(R.array.ferramentas_codigos);
        String[] categorias = getResources().getStringArray(R.array.ferramentas_categorias);
        String[] localizacoes = getResources().getStringArray(R.array.ferramentas_localizacoes);
        String[] estados = getResources().getStringArray(R.array.ferramentas_estados);
        String[] disponibilidades = getResources().getStringArray(R.array.ferramentas_disponibilidade);

        ArrayList<Ferramenta> lista = new ArrayList<>();

        for (int i = 0; i < nomes.length; i++) {
            boolean disponivel = getString(R.string.disponivel_sim)
                    .equalsIgnoreCase(disponibilidades[i]);

            lista.add(new Ferramenta(
                    nomes[i],
                    codigos[i],
                    categorias[i],
                    localizacoes[i],
                    estados[i],
                    disponivel));
        }

        return lista;
    }
}

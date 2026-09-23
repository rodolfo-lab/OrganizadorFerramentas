package com.example.organizadorferramentas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListaFerramentasActivity extends BaseActivity {

    private static final int REQUEST_CADASTRO = 1;

    private ArrayList<Ferramenta> ferramentas;
    private ListView listaFerramentas;
    private FerramentaAdapter adapter;
    private Button btnAdicionar;
    private Button btnSobre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ferramentas);

        ferramentas = new ArrayList<>();
        adapter = new FerramentaAdapter(this, ferramentas);

        listaFerramentas = findViewById(R.id.listaFerramentas);
        listaFerramentas.setAdapter(adapter);
        listaFerramentas.setEmptyView(findViewById(R.id.tvListaVazia));

        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnSobre = findViewById(R.id.btnSobre);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaFerramentasActivity.this, MainActivity.class);
                startActivityForResult(intent, REQUEST_CADASTRO);
            }
        });

        btnSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaFerramentasActivity.this, SobreActivity.class));
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CADASTRO) {
            return;
        }

        if (resultCode != RESULT_OK || data == null) {
            Toast.makeText(this, R.string.cadastro_cancelado, Toast.LENGTH_SHORT).show();
            return;
        }

        Ferramenta ferramenta = new Ferramenta(
                data.getStringExtra(MainActivity.EXTRA_NOME),
                data.getStringExtra(MainActivity.EXTRA_CODIGO),
                data.getStringExtra(MainActivity.EXTRA_CATEGORIA),
                data.getStringExtra(MainActivity.EXTRA_LOCALIZACAO),
                data.getStringExtra(MainActivity.EXTRA_ESTADO),
                data.getBooleanExtra(MainActivity.EXTRA_DISPONIVEL, false));

        ferramentas.add(ferramenta);
        adapter.notifyDataSetChanged();

        Toast.makeText(this,
                getString(R.string.ferramenta_adicionada, ferramenta.getNome()),
                Toast.LENGTH_SHORT).show();
    }
}

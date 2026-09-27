package com.example.organizadorferramentas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListaFerramentasActivity extends BaseActivity {

    private static final int REQUEST_CADASTRO = 1;

    private ArrayList<Ferramenta> ferramentas;
    private ListView listaFerramentas;
    private FerramentaAdapter adapter;

    private ActionMode menuContextual;
    private int posicaoSelecionada = ListView.INVALID_POSITION;

    private FerramentaDao ferramentaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ferramentas);

        ferramentaDao = FerramentasDatabase.getInstance(this).getFerramentaDao();

        ferramentas = new ArrayList<>();
        adapter = new FerramentaAdapter(this, ferramentas);

        listaFerramentas = findViewById(R.id.listaFerramentas);
        listaFerramentas.setAdapter(adapter);
        listaFerramentas.setEmptyView(findViewById(R.id.tvListaVazia));

        listaFerramentas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuContextual != null) {
                    menuContextual.finish();
                    return;
                }

                Ferramenta ferramenta = ferramentas.get(position);

                String disponibilidade = getString(ferramenta.isDisponivel()
                        ? R.string.status_disponivel
                        : R.string.status_emprestada);

                String mensagem = getString(R.string.item_clicado,
                        ferramenta.getCodigo(),
                        ferramenta.getNome(),
                        ferramenta.getDescricaoCategoria(ListaFerramentasActivity.this),
                        ferramenta.getLocalizacao(),
                        disponibilidade);

                Toast.makeText(ListaFerramentasActivity.this, mensagem, Toast.LENGTH_LONG).show();
            }
        });

        listaFerramentas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                abrirMenuContextual(position);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarFerramentas();
    }

    private void carregarFerramentas() {
        ferramentas.clear();
        ferramentas.addAll(ferramentaDao.listarTodas());
        ordenarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAdicionar) {
            abrirCadastro(ListView.INVALID_POSITION);
            return true;
        }

        if (id == R.id.menuConfiguracoes) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
            return true;
        }

        if (id == R.id.menuSobre) {
            startActivity(new Intent(this, SobreActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ordenarLista() {
        final int ordenacao = Preferencias.getOrdenacao(this);

        final Collator comparadorDeTexto = Collator.getInstance();
        comparadorDeTexto.setStrength(Collator.PRIMARY);

        Collections.sort(ferramentas, new Comparator<Ferramenta>() {
            @Override
            public int compare(Ferramenta uma, Ferramenta outra) {
                if (ordenacao == Preferencias.ORDENACAO_NOME) {
                    return comparadorDeTexto.compare(uma.getNome(), outra.getNome());
                }

                if (ordenacao == Preferencias.ORDENACAO_CODIGO) {
                    return comparadorDeTexto.compare(uma.getCodigo(), outra.getCodigo());
                }

                return Long.compare(uma.getId(), outra.getId());
            }
        });

        adapter.notifyDataSetChanged();
    }

    private void abrirMenuContextual(int posicao) {
        if (posicaoSelecionada != ListView.INVALID_POSITION) {
            listaFerramentas.setItemChecked(posicaoSelecionada, false);
        }

        posicaoSelecionada = posicao;
        listaFerramentas.setItemChecked(posicao, true);

        if (menuContextual == null) {
            menuContextual = startSupportActionMode(callbackMenuContextual);
        }

        if (menuContextual != null) {
            menuContextual.setTitle(R.string.item_selecionado);
        }
    }

    private void fecharMenuContextual() {
        if (menuContextual != null) {
            menuContextual.finish();
        }
    }

    private final ActionMode.Callback callbackMenuContextual = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_contextual_lista, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (posicaoSelecionada == ListView.INVALID_POSITION) {
                mode.finish();
                return true;
            }

            int id = item.getItemId();

            if (id == R.id.menuEditar) {
                abrirCadastro(posicaoSelecionada);
                mode.finish();
                return true;
            }

            if (id == R.id.menuExcluir) {
                confirmarExclusao(ferramentas.get(posicaoSelecionada));
                mode.finish();
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            menuContextual = null;

            if (posicaoSelecionada != ListView.INVALID_POSITION) {
                listaFerramentas.setItemChecked(posicaoSelecionada, false);
            }

            posicaoSelecionada = ListView.INVALID_POSITION;
        }
    };

    private void abrirCadastro(int posicao) {
        Intent intent = new Intent(this, MainActivity.class);

        if (posicao != ListView.INVALID_POSITION) {
            intent.putExtra(MainActivity.EXTRA_ID, ferramentas.get(posicao).getId());
        }

        startActivityForResult(intent, REQUEST_CADASTRO);
    }

    private void confirmarExclusao(final Ferramenta ferramenta) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmar_exclusao_titulo)
                .setIcon(R.drawable.ic_excluir)
                .setMessage(getString(R.string.confirmar_exclusao_mensagem, ferramenta.getNome()))
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogo, int botao) {
                        excluirFerramenta(ferramenta);
                    }
                })
                .setNegativeButton(R.string.nao, null)
                .show();
    }

    private void excluirFerramenta(Ferramenta ferramenta) {
        ferramentaDao.excluir(ferramenta);

        ferramentas.remove(ferramenta);
        adapter.notifyDataSetChanged();

        Toast.makeText(this,
                getString(R.string.ferramenta_excluida, ferramenta.getNome()),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CADASTRO) {
            return;
        }

        fecharMenuContextual();

        if (resultCode != RESULT_OK || data == null) {
            Toast.makeText(this, R.string.cadastro_cancelado, Toast.LENGTH_SHORT).show();
            return;
        }

        String nome = data.getStringExtra(MainActivity.EXTRA_NOME);
        boolean edicao = data.getBooleanExtra(MainActivity.EXTRA_EDICAO, false);

        Toast.makeText(this,
                getString(edicao ? R.string.ferramenta_editada : R.string.ferramenta_adicionada,
                        nome),
                Toast.LENGTH_SHORT).show();
    }
}

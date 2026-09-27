package com.example.organizadorferramentas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_ID = "com.example.organizadorferramentas.ID";
    public static final String EXTRA_NOME = "com.example.organizadorferramentas.NOME";
    public static final String EXTRA_EDICAO = "com.example.organizadorferramentas.EDICAO";

    private static final long ID_NOVO = -1;

    private EditText textNome;
    private EditText textCodigo;
    private EditText textLocalizacao;
    private Spinner categoria;
    private RadioGroup estado;
    private CheckBox disponivel;

    private FerramentaDao ferramentaDao;
    private Ferramenta ferramentaEmEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar barra = getSupportActionBar();
        if (barra != null) {
            barra.setDisplayHomeAsUpEnabled(true);
        }

        textNome = findViewById(R.id.textNome);
        textCodigo = findViewById(R.id.textCodigo);
        textLocalizacao = findViewById(R.id.textLocalizacao);
        categoria = findViewById(R.id.categoria);
        estado = findViewById(R.id.estado);
        disponivel = findViewById(R.id.disponivel);

        ferramentaDao = FerramentasDatabase.getInstance(this).getFerramentaDao();

        long id = getIntent().getLongExtra(EXTRA_ID, ID_NOVO);

        if (id != ID_NOVO) {
            ferramentaEmEdicao = ferramentaDao.buscarPorId(id);

            if (ferramentaEmEdicao == null) {
                Toast.makeText(this, R.string.erro_ferramenta_nao_encontrada,
                        Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                return;
            }

            setTitle(R.string.titulo_edicao);

            if (savedInstanceState == null) {
                preencherFormulario(ferramentaEmEdicao);
            }
        } else if (savedInstanceState == null && Preferencias.isSugestoesAtivas(this)) {
            aplicarSugestoes();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuSalvar) {
            salvarFormulario();
            return true;
        }

        if (id == R.id.menuLimpar) {
            limparFormulario();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    private void aplicarSugestoes() {
        textLocalizacao.setText(Preferencias.getLocalizacaoPadrao(this));
        disponivel.setChecked(Preferencias.isDisponivelPadrao(this));
        selecionarCategoria(Preferencias.getCategoriaPadrao(this));
    }

    private void preencherFormulario(Ferramenta ferramenta) {
        textNome.setText(ferramenta.getNome());
        textCodigo.setText(ferramenta.getCodigo());
        textLocalizacao.setText(ferramenta.getLocalizacao());
        disponivel.setChecked(ferramenta.isDisponivel());

        selecionarCategoria(ferramenta.getCategoria());
        selecionarEstado(ferramenta.getEstado());
    }

    private void selecionarCategoria(int posicao) {
        if (categoria.getAdapter() != null
                && posicao >= 0
                && posicao < categoria.getAdapter().getCount()) {
            categoria.setSelection(posicao);
        }
    }

    private void selecionarEstado(int posicao) {
        if (posicao >= 0 && posicao < estado.getChildCount()) {
            ((RadioButton) estado.getChildAt(posicao)).setChecked(true);
        }
    }

    private void limparFormulario() {
        textNome.setText("");
        textCodigo.setText("");
        textLocalizacao.setText("");

        estado.clearCheck();
        disponivel.setChecked(false);

        if (categoria.getAdapter() != null && categoria.getAdapter().getCount() > 0) {
            categoria.setSelection(0);
        }

        textNome.requestFocus();

        Toast.makeText(this, R.string.formulario_limpo, Toast.LENGTH_SHORT).show();
    }

    private void salvarFormulario() {
        String nome = textNome.getText().toString().trim();
        String codigo = textCodigo.getText().toString().trim();
        String localizacao = textLocalizacao.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, R.string.erro_nome, Toast.LENGTH_SHORT).show();
            textNome.requestFocus();
            return;
        }

        if (codigo.isEmpty()) {
            Toast.makeText(this, R.string.erro_codigo, Toast.LENGTH_SHORT).show();
            textCodigo.requestFocus();
            return;
        }

        if (localizacao.isEmpty()) {
            Toast.makeText(this, R.string.erro_localizacao, Toast.LENGTH_SHORT).show();
            textLocalizacao.requestFocus();
            return;
        }

        int categoriaSelecionada = categoria.getSelectedItemPosition();
        if (categoriaSelecionada <= 0) {
            Toast.makeText(this, R.string.erro_categoria, Toast.LENGTH_SHORT).show();
            categoria.requestFocus();
            return;
        }

        int radioSelecionadoId = estado.getCheckedRadioButtonId();
        if (radioSelecionadoId == -1) {
            Toast.makeText(this, R.string.erro_estado, Toast.LENGTH_SHORT).show();
            estado.requestFocus();
            return;
        }

        View rbSelecionado = findViewById(radioSelecionadoId);
        int estadoSelecionado = estado.indexOfChild(rbSelecionado);
        boolean estaDisponivel = disponivel.isChecked();

        boolean edicao = ferramentaEmEdicao != null;

        if (edicao) {
            ferramentaEmEdicao.setNome(nome);
            ferramentaEmEdicao.setCodigo(codigo);
            ferramentaEmEdicao.setCategoria(categoriaSelecionada);
            ferramentaEmEdicao.setLocalizacao(localizacao);
            ferramentaEmEdicao.setEstado(estadoSelecionado);
            ferramentaEmEdicao.setDisponivel(estaDisponivel);

            ferramentaDao.alterar(ferramentaEmEdicao);
        } else {
            Ferramenta nova = new Ferramenta(nome, codigo, categoriaSelecionada, localizacao,
                    estadoSelecionado, estaDisponivel);

            ferramentaDao.inserir(nova);
        }

        Intent resultado = new Intent();
        resultado.putExtra(EXTRA_NOME, nome);
        resultado.putExtra(EXTRA_EDICAO, edicao);

        setResult(RESULT_OK, resultado);
        finish();
    }
}

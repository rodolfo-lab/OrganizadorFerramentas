package com.example.organizadorferramentas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_NOME = "com.example.organizadorferramentas.NOME";
    public static final String EXTRA_CODIGO = "com.example.organizadorferramentas.CODIGO";
    public static final String EXTRA_CATEGORIA = "com.example.organizadorferramentas.CATEGORIA";
    public static final String EXTRA_LOCALIZACAO = "com.example.organizadorferramentas.LOCALIZACAO";
    public static final String EXTRA_ESTADO = "com.example.organizadorferramentas.ESTADO";
    public static final String EXTRA_DISPONIVEL = "com.example.organizadorferramentas.DISPONIVEL";
    public static final String EXTRA_POSICAO = "com.example.organizadorferramentas.POSICAO";

    private static final int POSICAO_NOVA = -1;

    private EditText textNome;
    private EditText textCodigo;
    private EditText textLocalizacao;
    private Spinner categoria;
    private RadioGroup estado;
    private CheckBox disponivel;

    private int posicao = POSICAO_NOVA;

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

        posicao = getIntent().getIntExtra(EXTRA_POSICAO, POSICAO_NOVA);

        if (posicao != POSICAO_NOVA) {
            setTitle(R.string.titulo_edicao);
            preencherFormulario(getIntent());
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

    private void preencherFormulario(Intent dados) {
        textNome.setText(dados.getStringExtra(EXTRA_NOME));
        textCodigo.setText(dados.getStringExtra(EXTRA_CODIGO));
        textLocalizacao.setText(dados.getStringExtra(EXTRA_LOCALIZACAO));
        disponivel.setChecked(dados.getBooleanExtra(EXTRA_DISPONIVEL, false));

        selecionarCategoria(dados.getStringExtra(EXTRA_CATEGORIA));
        selecionarEstado(dados.getStringExtra(EXTRA_ESTADO));
    }

    private void selecionarCategoria(String categoriaSalva) {
        if (categoriaSalva == null || categoria.getAdapter() == null) {
            return;
        }

        for (int i = 0; i < categoria.getAdapter().getCount(); i++) {
            if (categoriaSalva.equals(categoria.getAdapter().getItem(i).toString())) {
                categoria.setSelection(i);
                return;
            }
        }
    }

    private void selecionarEstado(String estadoSalvo) {
        if (estadoSalvo == null) {
            return;
        }

        for (int i = 0; i < estado.getChildCount(); i++) {
            RadioButton opcao = (RadioButton) estado.getChildAt(i);

            if (estadoSalvo.equals(opcao.getText().toString())) {
                opcao.setChecked(true);
                return;
            }
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

        if (categoria.getSelectedItemPosition() <= 0) {
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

        RadioButton rbSelecionado = findViewById(radioSelecionadoId);
        String estadoSelecionado = rbSelecionado.getText().toString();
        String categoriaSelecionada = categoria.getSelectedItem().toString();
        boolean estaDisponivel = disponivel.isChecked();

        Intent resultado = new Intent();
        resultado.putExtra(EXTRA_NOME, nome);
        resultado.putExtra(EXTRA_CODIGO, codigo);
        resultado.putExtra(EXTRA_CATEGORIA, categoriaSelecionada);
        resultado.putExtra(EXTRA_LOCALIZACAO, localizacao);
        resultado.putExtra(EXTRA_ESTADO, estadoSelecionado);
        resultado.putExtra(EXTRA_DISPONIVEL, estaDisponivel);
        resultado.putExtra(EXTRA_POSICAO, posicao);

        setResult(RESULT_OK, resultado);
        finish();
    }
}

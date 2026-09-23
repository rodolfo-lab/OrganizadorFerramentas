package com.example.organizadorferramentas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_NOME = "com.example.organizadorferramentas.NOME";
    public static final String EXTRA_CODIGO = "com.example.organizadorferramentas.CODIGO";
    public static final String EXTRA_CATEGORIA = "com.example.organizadorferramentas.CATEGORIA";
    public static final String EXTRA_LOCALIZACAO = "com.example.organizadorferramentas.LOCALIZACAO";
    public static final String EXTRA_ESTADO = "com.example.organizadorferramentas.ESTADO";
    public static final String EXTRA_DISPONIVEL = "com.example.organizadorferramentas.DISPONIVEL";

    private EditText textNome;
    private EditText textCodigo;
    private EditText textLocalizacao;
    private Spinner categoria;
    private RadioGroup estado;
    private CheckBox disponivel;
    private Button btnLimpar;
    private Button btnSalvar;

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
        btnLimpar = findViewById(R.id.btnLimpar);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparFormulario();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarFormulario();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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

        setResult(RESULT_OK, resultado);
        finish();
    }
}

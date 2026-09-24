package com.example.organizadorferramentas;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

public class ConfiguracoesActivity extends BaseActivity {

    private RadioGroup ordenacao;
    private CheckBox sugestoes;
    private TextView rotuloLocalizacaoPadrao;
    private EditText localizacaoPadrao;
    private TextView rotuloCategoriaPadrao;
    private Spinner categoriaPadrao;
    private CheckBox disponivelPadrao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        ActionBar barra = getSupportActionBar();
        if (barra != null) {
            barra.setDisplayHomeAsUpEnabled(true);
        }

        ordenacao = findViewById(R.id.ordenacao);
        sugestoes = findViewById(R.id.sugestoes);
        rotuloLocalizacaoPadrao = findViewById(R.id.tvLabelLocalizacaoPadrao);
        localizacaoPadrao = findViewById(R.id.textLocalizacaoPadrao);
        rotuloCategoriaPadrao = findViewById(R.id.tvLabelCategoriaPadrao);
        categoriaPadrao = findViewById(R.id.categoriaPadrao);
        disponivelPadrao = findViewById(R.id.disponivelPadrao);

        sugestoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton botao, boolean marcado) {
                habilitarCamposDeSugestao(marcado);
            }
        });

        carregarConfiguracoes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuracoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSalvar) {
            salvarConfiguracoes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void carregarConfiguracoes() {
        switch (Preferencias.getOrdenacao(this)) {
            case Preferencias.ORDENACAO_NOME:
                ordenacao.check(R.id.rbOrdenacaoNome);
                break;
            case Preferencias.ORDENACAO_CODIGO:
                ordenacao.check(R.id.rbOrdenacaoCodigo);
                break;
            default:
                ordenacao.check(R.id.rbOrdenacaoCadastro);
                break;
        }

        boolean sugestoesAtivas = Preferencias.isSugestoesAtivas(this);

        sugestoes.setChecked(sugestoesAtivas);
        localizacaoPadrao.setText(Preferencias.getLocalizacaoPadrao(this));
        disponivelPadrao.setChecked(Preferencias.isDisponivelPadrao(this));

        int categoriaSalva = Preferencias.getCategoriaPadrao(this);
        if (categoriaPadrao.getAdapter() != null
                && categoriaSalva < categoriaPadrao.getAdapter().getCount()) {
            categoriaPadrao.setSelection(categoriaSalva);
        }

        habilitarCamposDeSugestao(sugestoesAtivas);
    }

    private void habilitarCamposDeSugestao(boolean habilitado) {
        rotuloLocalizacaoPadrao.setEnabled(habilitado);
        localizacaoPadrao.setEnabled(habilitado);
        rotuloCategoriaPadrao.setEnabled(habilitado);
        categoriaPadrao.setEnabled(habilitado);
        disponivelPadrao.setEnabled(habilitado);

        float transparencia = habilitado ? 1f : 0.4f;

        rotuloLocalizacaoPadrao.setAlpha(transparencia);
        localizacaoPadrao.setAlpha(transparencia);
        rotuloCategoriaPadrao.setAlpha(transparencia);
        categoriaPadrao.setAlpha(transparencia);
        disponivelPadrao.setAlpha(transparencia);
    }

    private void salvarConfiguracoes() {
        int ordenacaoEscolhida;

        int opcaoMarcada = ordenacao.getCheckedRadioButtonId();
        if (opcaoMarcada == R.id.rbOrdenacaoNome) {
            ordenacaoEscolhida = Preferencias.ORDENACAO_NOME;
        } else if (opcaoMarcada == R.id.rbOrdenacaoCodigo) {
            ordenacaoEscolhida = Preferencias.ORDENACAO_CODIGO;
        } else {
            ordenacaoEscolhida = Preferencias.ORDENACAO_CADASTRO;
        }

        Preferencias.salvar(this,
                ordenacaoEscolhida,
                sugestoes.isChecked(),
                localizacaoPadrao.getText().toString().trim(),
                categoriaPadrao.getSelectedItemPosition(),
                disponivelPadrao.isChecked());

        Toast.makeText(this, R.string.configuracoes_salvas, Toast.LENGTH_SHORT).show();
        finish();
    }
}

package com.example.organizadorferramentas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        int radioSelecionadoId = estado.getCheckedRadioButtonId();
        if (radioSelecionadoId == -1) {
            Toast.makeText(this, R.string.erro_estado, Toast.LENGTH_SHORT).show();
            estado.requestFocus();
            return;
        }

        RadioButton rbSelecionado = findViewById(radioSelecionadoId);
        String estadoSelecionado = rbSelecionado.getText().toString();

        Object itemSelecionado = categoria.getSelectedItem();
        String categoriaSelecionada = itemSelecionado != null ? itemSelecionado.toString() : "";

        boolean estaDisponivel = disponivel.isChecked();
        String disponibilidadeTexto = getString(estaDisponivel ? R.string.disponivel_sim : R.string.disponivel_nao);

        String mensagem = getString(
                R.string.ferramenta_salva,
                nome,
                codigo,
                localizacao,
                categoriaSelecionada,
                estadoSelecionado,
                disponibilidadeTexto);

        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }

}
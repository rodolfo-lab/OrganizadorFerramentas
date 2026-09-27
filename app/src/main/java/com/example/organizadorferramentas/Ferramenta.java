package com.example.organizadorferramentas;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ferramentas")
public class Ferramenta {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String nome;

    @NonNull
    private String codigo;

    private int categoria;

    @NonNull
    private String localizacao;

    private int estado;

    private boolean disponivel;

    public Ferramenta() {
        this.nome = "";
        this.codigo = "";
        this.localizacao = "";
    }

    @Ignore
    public Ferramenta(@NonNull String nome, @NonNull String codigo, int categoria,
                      @NonNull String localizacao, int estado, boolean disponivel) {
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.localizacao = localizacao;
        this.estado = estado;
        this.disponivel = disponivel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    @NonNull
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull String codigo) {
        this.codigo = codigo;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    @NonNull
    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(@NonNull String localizacao) {
        this.localizacao = localizacao;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getDescricaoCategoria(Context contexto) {
        return descricao(contexto.getResources().getStringArray(R.array.categorias_ferramentas),
                categoria);
    }

    public String getDescricaoEstado(Context contexto) {
        return descricao(contexto.getResources().getStringArray(R.array.estados_ferramentas),
                estado);
    }

    private static String descricao(String[] opcoes, int posicao) {
        if (posicao < 0 || posicao >= opcoes.length) {
            return "";
        }

        return opcoes[posicao];
    }

    @NonNull
    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}

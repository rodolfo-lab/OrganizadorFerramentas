package com.example.organizadorferramentas;

public class Ferramenta {

    private String nome;
    private String codigo;
    private String categoria;
    private String localizacao;
    private String estado;
    private boolean disponivel;
    private long sequencia;

    public Ferramenta(String nome, String codigo, String categoria, String localizacao,
                      String estado, boolean disponivel) {
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.localizacao = localizacao;
        this.estado = estado;
        this.disponivel = disponivel;
    }

    public long getSequencia() {
        return sequencia;
    }

    public void setSequencia(long sequencia) {
        this.sequencia = sequencia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}

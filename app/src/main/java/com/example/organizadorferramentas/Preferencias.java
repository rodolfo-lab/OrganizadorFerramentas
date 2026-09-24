package com.example.organizadorferramentas;

import android.content.Context;
import android.content.SharedPreferences;

public final class Preferencias {

    public static final int ORDENACAO_CADASTRO = 0;
    public static final int ORDENACAO_NOME = 1;
    public static final int ORDENACAO_CODIGO = 2;

    private static final String ARQUIVO = "com.example.organizadorferramentas.CONFIGURACOES";

    private static final String CHAVE_ORDENACAO = "ordenacao";
    private static final String CHAVE_SUGESTOES = "sugestoes_ativas";
    private static final String CHAVE_LOCALIZACAO_PADRAO = "localizacao_padrao";
    private static final String CHAVE_CATEGORIA_PADRAO = "categoria_padrao";
    private static final String CHAVE_DISPONIVEL_PADRAO = "disponivel_padrao";

    private Preferencias() {
    }

    private static SharedPreferences arquivo(Context contexto) {
        return contexto.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
    }

    public static int getOrdenacao(Context contexto) {
        return arquivo(contexto).getInt(CHAVE_ORDENACAO, ORDENACAO_CADASTRO);
    }

    public static boolean isSugestoesAtivas(Context contexto) {
        return arquivo(contexto).getBoolean(CHAVE_SUGESTOES, false);
    }

    public static String getLocalizacaoPadrao(Context contexto) {
        return arquivo(contexto).getString(CHAVE_LOCALIZACAO_PADRAO, "");
    }

    public static int getCategoriaPadrao(Context contexto) {
        return arquivo(contexto).getInt(CHAVE_CATEGORIA_PADRAO, 0);
    }

    public static boolean isDisponivelPadrao(Context contexto) {
        return arquivo(contexto).getBoolean(CHAVE_DISPONIVEL_PADRAO, false);
    }

    public static void salvar(Context contexto,
                             int ordenacao,
                             boolean sugestoesAtivas,
                             String localizacaoPadrao,
                             int categoriaPadrao,
                             boolean disponivelPadrao) {

        arquivo(contexto).edit()
                .putInt(CHAVE_ORDENACAO, ordenacao)
                .putBoolean(CHAVE_SUGESTOES, sugestoesAtivas)
                .putString(CHAVE_LOCALIZACAO_PADRAO, localizacaoPadrao)
                .putInt(CHAVE_CATEGORIA_PADRAO, categoriaPadrao)
                .putBoolean(CHAVE_DISPONIVEL_PADRAO, disponivelPadrao)
                .apply();
    }
}

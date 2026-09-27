package com.example.organizadorferramentas;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Ferramenta.class}, version = 1, exportSchema = false)
public abstract class FerramentasDatabase extends RoomDatabase {

    private static final String NOME_BANCO = "ferramentas.db";

    private static volatile FerramentasDatabase instancia;

    public abstract FerramentaDao getFerramentaDao();

    public static FerramentasDatabase getInstance(Context contexto) {
        if (instancia == null) {
            synchronized (FerramentasDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(contexto.getApplicationContext(),
                                    FerramentasDatabase.class,
                                    NOME_BANCO)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return instancia;
    }
}

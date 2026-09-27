package com.example.organizadorferramentas;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FerramentaDao {

    @Insert
    long inserir(Ferramenta ferramenta);

    @Update
    int alterar(Ferramenta ferramenta);

    @Delete
    int excluir(Ferramenta ferramenta);

    @Query("SELECT * FROM ferramentas WHERE id = :id")
    Ferramenta buscarPorId(long id);

    @Query("SELECT * FROM ferramentas ORDER BY id ASC")
    List<Ferramenta> listarTodas();
}

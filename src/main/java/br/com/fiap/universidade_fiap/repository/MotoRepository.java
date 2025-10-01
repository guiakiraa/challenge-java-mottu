package br.com.fiap.universidade_fiap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.universidade_fiap.model.Moto;

public interface MotoRepository extends JpaRepository<Moto, Long> {

    @Query("SELECT m FROM Moto m WHERE "
        + "(:placa IS NULL OR LOWER(m.placa) LIKE LOWER(CONCAT('%', :placa, '%'))) AND "
        + "(:modelo IS NULL OR LOWER(m.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) AND "
        + "(:ano IS NULL OR m.ano = :ano) AND "
        + "(:tipoCombustivel IS NULL OR LOWER(m.tipoCombustivel) = LOWER(:tipoCombustivel)) AND "
        + "(:filialId IS NULL OR m.filial.id = :filialId)")
    List<Moto> buscarAvancado(
        @Param("placa") String placa,
        @Param("modelo") String modelo,
        @Param("ano") Integer ano,
        @Param("tipoCombustivel") String tipoCombustivel,
        @Param("filialId") Long filialId
    );

    long countByFilial_Id(Long filialId);
}



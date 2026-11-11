package com.IchafLab.backend.repository;

import com.IchafLab.backend.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository
        extends JpaRepository<Produto, Long> {
}
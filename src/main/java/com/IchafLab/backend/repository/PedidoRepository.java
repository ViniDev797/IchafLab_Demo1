package com.IchafLab.backend.repository;

import com.IchafLab.backend.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}

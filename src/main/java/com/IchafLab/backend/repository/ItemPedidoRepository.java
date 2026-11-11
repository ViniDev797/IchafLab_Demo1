package com.IchafLab.backend.repository;

import com.IchafLab.backend.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // Consulta derivada: o Spring Data lê o nome do método e monta o SQL.
    // "findBy" + "Pedido" (campo pedido) + "Id" (campo id do pedido)
    // equivale a: SELECT ... FROM itens_pedido WHERE pedido_id = ?
    List<ItemPedido> findByPedidoId(Long pedidoId);
}
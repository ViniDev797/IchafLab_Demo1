package com.IchafLab.backend.service;

import com.IchafLab.backend.dto.AtualizarStatusPedidoRequest;
import com.IchafLab.backend.dto.PedidoRequest;
import com.IchafLab.backend.dto.PedidoResponse;
import com.IchafLab.backend.entity.Pedido;
import com.IchafLab.backend.enums.ModalidadePedido;
import com.IchafLab.backend.enums.StatusPedido;
import com.IchafLab.backend.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    // Injeção de dependência por construtor:
    // o Spring entrega o repository pronto quando cria o service.
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // Retorna todos os pedidos já convertidos para PedidoResponse.
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(PedidoResponse::deEntidade)
                .toList();
    }

    // Busca um pedido pelo ID. Se não existir, lança NoSuchElementException (404).
    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Pedido não encontrado com o id: " + id));
        return PedidoResponse.deEntidade(pedido);
    }

    // Cria um novo pedido a partir dos dados recebidos.
    // O status inicial é sempre RASCUNHO, e o valorTotal começa em zero
    // (valor inicial definido na entidade). O valor total é atualizado
    // quando itens são adicionados ao pedido.
    @Transactional
    public PedidoResponse salvar(PedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("O pedido não pode ser nulo.");
        }

        Pedido pedido = new Pedido(
                request.modalidade(),
                StatusPedido.RASCUNHO,
                request.canalOrigem(),
                request.observacao());

        return PedidoResponse.deEntidade(pedidoRepository.save(pedido));
    }

    // Atualiza o status de um pedido, respeitando as transições permitidas.
    // O pedido é carregado dentro da transação e fica gerenciado pelo JPA:
    // a alteração do status é gravada automaticamente ao final dela.
    @Transactional
    public PedidoResponse atualizarStatus(Long pedidoId, AtualizarStatusPedidoRequest request) {
        if (request == null || request.status() == null) {
            throw new IllegalArgumentException("O status do pedido é obrigatório.");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Pedido não encontrado com o id: " + pedidoId));

        StatusPedido statusAtual = pedido.getStatus();
        StatusPedido novoStatus = request.status();

        // Nesta fase, a modalidade SALAO ainda não tem fluxo de status.
        if (pedido.getModalidade() == ModalidadePedido.SALAO) {
            throw new IllegalArgumentException(
                    "A atualização de status ainda não está disponível para pedidos do tipo SALAO.");
        }

        // Pedidos finalizados não podem mais ser alterados.
        if (statusAtual == StatusPedido.CONCLUIDO || statusAtual == StatusPedido.CANCELADO) {
            throw new IllegalArgumentException(
                    "Pedido com status " + statusAtual + " não pode mais ser alterado. "
                            + "Status solicitado: " + novoStatus + ".");
        }

        if (!transicaoPermitida(statusAtual, novoStatus, pedido.getModalidade())) {
            throw new IllegalArgumentException(
                    "Transição de status inválida: de " + statusAtual
                            + " para " + novoStatus + ".");
        }

        pedido.setStatus(novoStatus);

        return PedidoResponse.deEntidade(pedido);
    }

    // Exclui um pedido pelo ID. Se não existir, lança NoSuchElementException (404).
    @Transactional
    public void excluir(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "Pedido não encontrado com o id: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    // Define, para cada status atual, qual é o único próximo passo permitido
    // (ou o cancelamento, nos status em que ele é possível).
    // A modalidade só importa depois que o pedido está PRONTO.
    private boolean transicaoPermitida(StatusPedido atual,
                                       StatusPedido novo,
                                       ModalidadePedido modalidade) {
        return switch (atual) {
            case RASCUNHO ->
                    novo == StatusPedido.AGUARDANDO_CONFIRMACAO
                            || novo == StatusPedido.CANCELADO;
            case AGUARDANDO_CONFIRMACAO ->
                    novo == StatusPedido.CONFIRMADO
                            || novo == StatusPedido.CANCELADO;
            case CONFIRMADO ->
                    novo == StatusPedido.EM_PREPARO
                            || novo == StatusPedido.CANCELADO;
            case EM_PREPARO ->
                    novo == StatusPedido.PRONTO;
            case PRONTO ->
                    (modalidade == ModalidadePedido.DELIVERY && novo == StatusPedido.EM_ENTREGA)
                            || (modalidade == ModalidadePedido.RETIRADA && novo == StatusPedido.CONCLUIDO);
            case EM_ENTREGA ->
                    modalidade == ModalidadePedido.DELIVERY && novo == StatusPedido.CONCLUIDO;
            case CONCLUIDO, CANCELADO ->
                    false;
        };
    }
}
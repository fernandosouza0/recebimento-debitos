package br.com.fadesp.service.impl;

import br.com.fadesp.domain.dto.RecebimentoDebitosRequest;
import br.com.fadesp.domain.dto.RecebimentoDebitosResponse;
import br.com.fadesp.domain.enums.MetodoPagamento;
import br.com.fadesp.domain.enums.StatusPagamento;
import br.com.fadesp.domain.mapper.RecebimentoDebitosMapper;
import br.com.fadesp.domain.model.RecebimentoDebitos;
import br.com.fadesp.exception.NotFoundException;
import br.com.fadesp.repository.RecebimentoDebitosRepository;
import br.com.fadesp.service.RecebimentoDebitosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecebimentoDebitosServiceImpl implements RecebimentoDebitosService {

    private final RecebimentoDebitosRepository repository;
    private final RecebimentoDebitosMapper mapper;

    public RecebimentoDebitosServiceImpl(RecebimentoDebitosRepository repository, RecebimentoDebitosMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<RecebimentoDebitosResponse> listarPagamentos(Long cpfCnpj, Long codigoDebito, String status) {
        if (cpfCnpj != null && codigoDebito == null && status == null) {
            return repository.findByCpfCnpjPagador(cpfCnpj)
                    .stream()
                    .map(mapper::recebimentoDebitosToDTOConfiguracao)
                    .collect(Collectors.toList());
        } else if (codigoDebito != null && cpfCnpj == null && status == null) {
            return repository.findFirstByCodigoDebito(codigoDebito)
                    .stream()
                    .map(mapper::recebimentoDebitosToDTOConfiguracao)
                    .collect(Collectors.toList());
        } else if (status != null && cpfCnpj == null && codigoDebito == null) {
            return repository.findByStatus(status)
                    .stream()
                    .map(mapper::recebimentoDebitosToDTOConfiguracao)
                    .collect(Collectors.toList());
        } else {
            return repository.findAll()
                    .stream()
                    .map(mapper::recebimentoDebitosToDTOConfiguracao)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public RecebimentoDebitosResponse receberPagamento(RecebimentoDebitosRequest recebimentoDebitosRequest) {
        this.validarNumeroCartao(recebimentoDebitosRequest);

        RecebimentoDebitos forma = mapper.requestToModelRecebimentoDebitos(recebimentoDebitosRequest);
        forma.setStatus(StatusPagamento.PENDENTE_DE_PROCESSAMENTO);
        return mapper.recebimentoDebitosToDTOConfiguracao(repository.save(forma));
    }


    @Override
    public RecebimentoDebitosResponse alterarPagamento(Long codigoDebito, RecebimentoDebitosRequest request) {
        this.validarNumeroCartao(request);

        RecebimentoDebitos pagamento = repository.findByCodigoDebito(codigoDebito)
                .orElseThrow(() -> new NotFoundException("Código de débito não encontrado"));

        // Verificar o status atual do pagamento
        if (pagamento.getStatus() == StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
            // Se estiver pendente de processamento, pode ser alterado para Processado com Sucesso ou Processado com Falha
            if (request.getStatus() == StatusPagamento.PROCESSADO_COM_SUCESSO ||
                    request.getStatus() == StatusPagamento.PROCESSADO_COM_FALHA) {
                pagamento.setStatus(request.getStatus());
            } else {
                throw new IllegalArgumentException("O status do pagamento só pode ser alterado para 'PROCESSADO_COM_SUCESSO' ou 'PROCESSADO_COM_FALHA'");
            }
        } else if (pagamento.getStatus() == StatusPagamento.PROCESSADO_COM_FALHA) {
            // Se estiver processado com falha, só pode ser alterado para Pendente de Processamento
            if (request.getStatus() == StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
                pagamento.setStatus(request.getStatus());
            } else {
                throw new IllegalArgumentException("O status do pagamento em falha só pode ser alterado para 'PENDENTE_DE_PROCESSAMENTO'");
            }
        } else {
            // Se estiver processado com sucesso, não pode ser alterado
            throw new IllegalArgumentException("O status do pagamento em sucesso não pode ser alterado");
        }

        // Salvar o pagamento atualizado no banco de dados
        RecebimentoDebitos pagamentoAtualizado = repository.save(pagamento);
        return mapper.recebimentoDebitosToDTOConfiguracao(pagamentoAtualizado);
    }

    @Override
    public boolean excluirPagamento(Long codigoDebito) {
        Optional<RecebimentoDebitos> pagamentoOptional = repository.findById(codigoDebito);
        if (pagamentoOptional.isPresent()) {
            RecebimentoDebitos pagamento = pagamentoOptional.get();
            if (pagamento.getStatus() == StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
                repository.deleteById(codigoDebito);
                return true;
            } else {
                return false;
            }
        } else {
            throw new NotFoundException("Pagamento não encontrado com o código " + codigoDebito);
        }
    }

    private void validarNumeroCartao(RecebimentoDebitosRequest request) {
        MetodoPagamento metodoPagamento = request.getMetodoPagamento();
        Long numeroCartao = request.getNumeroCartao();

        // Verificar se o número do cartão é obrigatório apenas para CARTAO_CREDITO ou CARTAO_DEBITO
        if ((metodoPagamento == MetodoPagamento.CARTAO_CREDITO || metodoPagamento == MetodoPagamento.CARTAO_DEBITO) &&
                numeroCartao == null) {
            throw new IllegalArgumentException("O número do cartão é obrigatório para pagamentos com cartão de crédito ou débito.");
        } else if (metodoPagamento != MetodoPagamento.CARTAO_CREDITO && metodoPagamento != MetodoPagamento.CARTAO_DEBITO &&
                numeroCartao != null) {
            // Se o método de pagamento não for CARTAO_CREDITO ou CARTAO_DEBITO, o número do cartão não deve ser fornecido
            throw new IllegalArgumentException("O número do cartão só pode ser enviado se o método de pagamento for CARTAO_CREDITO ou CARTAO_DEBITO.");
        }
    }

}

package br.com.fadesp.service;

import br.com.fadesp.domain.dto.RecebimentoDebitosRequest;
import br.com.fadesp.domain.dto.RecebimentoDebitosResponse;

import java.util.List;

public interface RecebimentoDebitosService {
    List<RecebimentoDebitosResponse> listarPagamentos(Long cpfCnpj, Long codigoDebito, String status);
    RecebimentoDebitosResponse receberPagamento(RecebimentoDebitosRequest recebimentoDebitosRequest);
    RecebimentoDebitosResponse alterarPagamento(Long codigoDebito, RecebimentoDebitosRequest request);
    boolean excluirPagamento(Long codigoDebito);

}

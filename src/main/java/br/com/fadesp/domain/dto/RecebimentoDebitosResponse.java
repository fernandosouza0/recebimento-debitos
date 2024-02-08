package br.com.fadesp.domain.dto;


import br.com.fadesp.domain.enums.MetodoPagamento;
import br.com.fadesp.domain.enums.StatusPagamento;

public class RecebimentoDebitosResponse {

    private Long codigoDebito;
    private Long cpfCnpjPagador;
    private MetodoPagamento metodoPagamento;
    private Long numeroCartao;
    private double valorPagamento;
    private StatusPagamento status;

    public Long getCodigoDebito() {
        return codigoDebito;
    }

    public void setCodigoDebito(Long codigoDebito) {
        this.codigoDebito = codigoDebito;
    }

    public Long getCpfCnpjPagador() {
        return cpfCnpjPagador;
    }

    public void setCpfCnpjPagador(Long cpfCnpjPagador) {
        this.cpfCnpjPagador = cpfCnpjPagador;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public Long getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(Long numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public double getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }
}

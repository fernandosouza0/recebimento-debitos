package br.com.fadesp.controller;

import br.com.fadesp.domain.Code;
import br.com.fadesp.domain.dto.RecebimentoDebitosRequest;
import br.com.fadesp.domain.dto.RecebimentoDebitosResponse;
import br.com.fadesp.exception.NotFoundException;
import br.com.fadesp.service.RecebimentoDebitosService;
import br.com.fadesp.utils.ResponseRest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Recebimento de pagamentos de débitos de pessoas físicas e jurídicas")
@RequestMapping("v1/api/recebimento-debitos")
public class RecebimentoDebitosController {

    private final RecebimentoDebitosService service;

    public RecebimentoDebitosController(RecebimentoDebitosService service) {
        this.service = service;
    }

    @GetMapping("")
    @Operation(description = "Pesquisar Debito")
    public ResponseEntity<ResponseRest<List<RecebimentoDebitosResponse>>> listarPagamentos(
            @RequestParam(required = false) Long cpfCnpj,
            @RequestParam(required = false) Long codigoDebito,
            @RequestParam(required = false) String status) {
        List<RecebimentoDebitosResponse> pagamentos = service.listarPagamentos(cpfCnpj, codigoDebito, status);
        if (pagamentos.isEmpty()) {
            return ResponseEntity
                    .status(Code.BUSCA_GERAL_NOT_FOUND.getProperties().getStatus())
                    .body(new ResponseRest<>(null, Code.BUSCA_GERAL_NOT_FOUND.getProperties(), null));
        }
        return ResponseEntity.ok(new ResponseRest<>(pagamentos, Code.SUCESSO.getProperties(), null));
    }



    @PostMapping
    @Operation(description = "Receber Pagamento")
    public ResponseEntity<ResponseRest<RecebimentoDebitosResponse>> receberPagamento(@RequestBody @Valid RecebimentoDebitosRequest recebimentoDebitosRequest) {
        try {
            RecebimentoDebitosResponse registroSalvo = service.receberPagamento(recebimentoDebitosRequest);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri().path("/{codigoDebito}")
                    .buildAndExpand(registroSalvo.getCodigoDebito())
                    .toUri();
            return ResponseEntity
                    .created(uri)
                    .body(new ResponseRest<>(registroSalvo, Code.CAD_SERVICO_SUCESSO_I.getProperties(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseRest<>(null, Code.CAD_SERVICO_ERROR_I.getProperties(), e.getMessage()));
        }
    }


    @PutMapping("{codigoDebito}")
    @Operation(description = "Alterar Status de Pagamento")
    public ResponseEntity<ResponseRest<RecebimentoDebitosResponse>> alterarPagamento(@PathVariable Long codigoDebito,
                                                                        @RequestBody @Valid RecebimentoDebitosRequest request) {
        try {
            RecebimentoDebitosResponse registroAlterado = service.alterarPagamento(codigoDebito, request);
            return ResponseEntity
                    .ok(new ResponseRest<>(registroAlterado, Code.CAD_SERVICO_SUCESSO_A.getProperties(), null));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRest<>(null, Code.CAD_SERVICO_ERROR_A.getProperties(),
                    e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{codigoDebito}")
    @Operation(description = "Deletar Pagamento")
    public ResponseEntity<ResponseRest<Code>> excluir(@PathVariable Long codigoDebito) {
        try {
            boolean excluido = service.excluirPagamento(codigoDebito);
            if (excluido) {
                return ResponseEntity.ok(new ResponseRest<>(null, Code.CAD_SERVICO_SUCESSO_E.getProperties(), null));
            } else {
                return new ResponseEntity<>(new ResponseRest<>(null, Code.CAD_SERVICO_ERROR_E.getProperties(), "Não é possível excluir um pagamento com status diferente de 'Pendente de Processamento'"), HttpStatus.BAD_REQUEST);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseRest<>(null, Code.CAD_SERVICO_ERROR_E.getProperties(), e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


}

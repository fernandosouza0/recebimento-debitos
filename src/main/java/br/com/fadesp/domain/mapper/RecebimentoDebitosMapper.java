package br.com.fadesp.domain.mapper;

import br.com.fadesp.domain.dto.RecebimentoDebitosRequest;
import br.com.fadesp.domain.dto.RecebimentoDebitosResponse;
import br.com.fadesp.domain.model.RecebimentoDebitos;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RecebimentoDebitosMapper {

    RecebimentoDebitosResponse recebimentoDebitosToDTOConfiguracao(RecebimentoDebitos recebimentoDebitos);

    RecebimentoDebitos requestToModelRecebimentoDebitos(RecebimentoDebitosRequest recebimentoDebitosRequest);
}

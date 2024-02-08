package br.com.fadesp.repository;


import br.com.fadesp.domain.model.RecebimentoDebitos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecebimentoDebitosRepository extends JpaRepository<RecebimentoDebitos, Long> {
    List<RecebimentoDebitos> findByCpfCnpjPagador(Long cpfCnpj);

    List<RecebimentoDebitos> findByStatus( String status);

    List<RecebimentoDebitos> findFirstByCodigoDebito(Long codigoDebito);

    Optional<RecebimentoDebitos> findByCodigoDebito(Long codigoDebito);

}

package br.com.fadesp.domain;

import br.com.fadesp.utils.Properties;
import org.springframework.http.HttpStatus;

public enum Code {

    SUCESSO(0, HttpStatus.OK, "Consulta realizada com sucesso."),
    BUSCA_GERAL_NOT_FOUND(0, HttpStatus.NOT_FOUND, "Nenhum Registro Encontrado."),
    CAD_SERVICO_SUCESSO_I(0, HttpStatus.OK, "Incluído Com Sucesso"),
    CAD_SERVICO_SUCESSO_E(0, HttpStatus.OK, "Excluído Com Sucesso"),
    CAD_SERVICO_SUCESSO_A(0, HttpStatus.OK, "Atualizado Com Sucesso"),
    CAD_SERVICO_ERROR_I(1, HttpStatus.BAD_REQUEST, "Erro ao incluir"),
    CAD_SERVICO_ERROR_E(1, HttpStatus.BAD_REQUEST, "Erro ao Excluir"),
    CAD_SERVICO_ERROR_A(1, HttpStatus.BAD_REQUEST, "Erro ao Atualizar");
    private final Properties properties;

    Code(int code, HttpStatus status, String message) {
        this.properties = new Properties(code, status, message);
    }

    public Properties getProperties() {
        return properties;
    }

}

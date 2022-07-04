package br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString()
public class Conta {
    private String agencia;
    private String numeroConta;
    private String saldo;
    private String status;
    private Boolean statusEnvioReceita;
}
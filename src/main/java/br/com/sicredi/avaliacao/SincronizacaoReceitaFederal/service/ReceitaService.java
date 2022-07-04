package br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.service;

import br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.SincronizacaoReceitaFederal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceitaService {

    // Esta é a implementação interna do "servico" do banco central. Veja o código fonte abaixo para ver os formatos esperados pelo Banco Central neste cenário.
    private static Logger logger = LoggerFactory.getLogger(SincronizacaoReceitaFederal.class);

    public boolean atualizarConta(String agencia, String conta, double saldo, String status)
            throws RuntimeException, InterruptedException {

        long duracaoInicial = ZonedDateTime.now().toInstant().toEpochMilli();
        // Formato agencia: 0000
        if (agencia == null || agencia.length() != 4) {
            return false;
        }
        // Formato conta: 000000
        if (conta == null || conta.length() != 6) {
            return false;
        }
        // Tipos de status validos:
        List tipos = new ArrayList();
        tipos.add("A");
        tipos.add("I");
        tipos.add("B");
        tipos.add("P");

        if (status == null || !tipos.contains(status)) {
            return false;
        }
        // Simula tempo de resposta do serviço (entre 1 e 5 segundos)
        long wait = Math.round(Math.random() * 4000) + 1000;
        Thread.sleep(wait);

        // Simula cenario de erro no serviço (0,1% de erro)
        long randomError = Math.round(Math.random() * 1000);
        if (randomError == 500) {
            throw new RuntimeException("Error");
        }
        long duracaoFinal =ZonedDateTime.now().toInstant().toEpochMilli();
        logger.info((duracaoFinal-duracaoInicial)+"ms -> tempo de resposta do service");
        return true;
    }
}

package br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.domain;

import br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.Utils.Utils;
import br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.dto.Conta;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CSV {
    private  String PATH_SAIDA_CSV;
    private static Logger logger = LoggerFactory.getLogger(CSV.class);

    public List<Conta> lerCsv(String pathEntrada, String pathSaida) {
        PATH_SAIDA_CSV = pathSaida;
        long duracaoInicial = ZonedDateTime.now().toInstant().toEpochMilli();
        List<Conta> contas = new ArrayList<Conta>();
        logger.info("Validando extensão de arquivo...");
        if(FilenameUtils.getExtension(pathEntrada).equalsIgnoreCase("csv")) {
            try {
                logger.info("Leitura CSV de Entrada iniciada.");
                var caminhoArquivo = new FileInputStream(pathEntrada);
                var buffer = new BufferedReader(new InputStreamReader(caminhoArquivo));
                var lines = buffer.lines().collect(Collectors.toList());
                lines.remove(0);
                for (String line : lines) {
                    line = Utils.formatarLineCsv(line);
                    String[] itens = line.split(";");
                    Conta conta = new Conta();
                    conta.setAgencia(itens[0]);
                    conta.setNumeroConta(Utils.formatarNumeroConta(itens[1]));
                    conta.setSaldo(Utils.formatarSaldo(itens[2]));
                    conta.setStatus(itens[3]);

                    contas.add(conta);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            long duracaoFinal =ZonedDateTime.now().toInstant().toEpochMilli();
            logger.info((duracaoFinal-duracaoInicial)+"ms:");
        }else {
            logger.error("Invalido a extenção do arquivo.");
        }
        return contas;
    }
    public void criaCsv(List<Conta> dadosCsv)  {
        List<String[]> linhas = new ArrayList<>();
        logger.info("Criando CSV de Saida...");
        for(Conta linha: dadosCsv ) {
            linhas.add(new String[] {linha.getAgencia(),Utils.reverterFormatarNumeroConta(linha.getNumeroConta()),linha.getSaldo(),linha.getStatus(),Utils.formatarResposta(linha.getStatusEnvioReceita())});
            logger.info(linha.toString());
        }
        logger.info("Formatando dados para CSV...");
        String[] cabecalho = {"agencia", "conta", "saldo", "status", "resposta_receita"};
        try {
            FileWriter escrever = new FileWriter(new File(PATH_SAIDA_CSV));
            logger.info("destino do CSV:"+ PATH_SAIDA_CSV);

            CSVWriter csv = new CSVWriter(escrever,';',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
            csv.writeNext(cabecalho);
            csv.writeAll(linhas);
            csv.flush();
            escrever.close();
            logger.info("finalizado.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
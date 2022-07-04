package br.com.sicredi.avaliacao.SincronizacaoReceitaFederal;

import br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.domain.CSV;
import br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.dto.Conta;
import br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.service.ReceitaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/*
Cenário de Negócio:
Todo dia útil por volta das 6 horas da manhã um colaborador da retaguarda do Sicredi recebe e organiza as informações de
contas para enviar ao Banco Central. Todas agencias e cooperativas enviam arquivos Excel à Retaguarda. Hoje o Sicredi
já possiu mais de 4 milhões de contas ativas.
Esse usuário da retaguarda exporta manualmente os dados em um arquivo CSV para ser enviada para a Receita Federal,
antes as 10:00 da manhã na abertura das agências.

Requisito:
Usar o "serviço da receita" (fake) para processamento automático do arquivo.

Funcionalidade:
0. Criar uma aplicação SprintBoot standalone. Exemplo: java -jar SincronizacaoReceita <input-file>
1. Processa um arquivo CSV de entrada com o formato abaixo.
2. Envia a atualização para a Receita através do serviço (SIMULADO pela classe ReceitaService).
3. Retorna um arquivo com o resultado do envio da atualização da Receita. Mesmo formato adicionando o resultado em uma
nova coluna.

Formato CSV:
agencia;conta;saldo;status
0101;12225-6;100,00;A
0101;12226-8;3200,50;A
3202;40011-1;-35,12;I
3202;54001-2;0,00;P
3202;00321-2;34500,00;B
...

*/
@SpringBootApplication
public class SincronizacaoReceitaFederal {
	private static Logger logger = LoggerFactory.getLogger(SincronizacaoReceitaFederal.class);

	public static void main(String[] args) {

		SpringApplication.run(SincronizacaoReceitaFederal.class, args);
		String[] path;
		if(args.length > 0) {
			path = args[0].toString().split(",");
			logger.info("Caminho entrada:"+ path[0]);
			logger.info("Caminho saida:"+ path[1]);

			CSV csv = new CSV();
			List<Conta> dadosEnviados = new ArrayList<>();
			try {
				long duracaoInicial = ZonedDateTime.now().toInstant().toEpochMilli();
				for(Conta conta : csv.lerCsv(path[0],path[1])) {
					ReceitaService receitaService = new ReceitaService();
					conta.setStatusEnvioReceita(receitaService.atualizarConta(conta.getAgencia(), conta.getNumeroConta(), Double.valueOf(conta.getSaldo()), conta.getStatus()));
					dadosEnviados.add(conta);
				}
				long duracaoFinal =ZonedDateTime.now().toInstant().toEpochMilli();
				logger.info((duracaoFinal-duracaoInicial)+"ms: total");
				csv.criaCsv(dadosEnviados);
			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else {
			logger.info(">>>>>>Caminho do .csv não informado.<<<<<<");
			logger.info("java -jar SincronizacaoReceitaFederal-0.0.1-SNAPSHOT.jar  C:\\\\Users\\\\thenr\\\\Downloads\\\\dados.csv,C:\\\\Users\\\\thenr\\\\Downloads\\\\dadosAtualizado.csv\n" );
		}
	}
}


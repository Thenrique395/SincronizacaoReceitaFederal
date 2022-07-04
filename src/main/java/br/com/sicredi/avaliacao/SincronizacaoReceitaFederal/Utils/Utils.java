package br.com.sicredi.avaliacao.SincronizacaoReceitaFederal.Utils;

import java.text.DecimalFormat;

public class Utils {

    public static  String formatarNumeroConta(String numeroConta) {
        String numeroContaFormatado = numeroConta.replace("-", "");
        return numeroContaFormatado;
    }

    public static  String reverterFormatarNumeroConta(String numeroConta) {
        String numeroContaFormatado = numeroConta.substring(0, 5)+"-"+numeroConta.substring(5,numeroConta.length());
        return numeroContaFormatado;
    }

    public static  String formatarSaldo(String saldo) {
        String saldoFormatado = saldo.replace(",", ".");
        return saldoFormatado;
    }

    public static  String reverterFormatarSaldo(String saldo) {
        Double val = Double.parseDouble(saldo);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String saldoFormatado =  decimalFormat.format(val);
        return saldoFormatado;
    }

    public static String formatarLineCsv(String linha) {
        return linha.replace("\"", "");

    }

    public static String formatarResposta(Boolean isBoolean) {
        return (isBoolean.toString().equalsIgnoreCase("TRUE") ? "SUCESSO" : "FALHOU");
    }

}

package com.github.limiares;

import java.util.ArrayList;

import com.github.ck.CKNumber;

public class GerarLimiares {

	public static void main(String[] args) {
		final String PASTA_RESULTADO = "C:\\eclipse\\configuration\\smelldetector\\";
		GerenciadorLimiares gLimiares = new GerenciadorLimiares();

		System.out.println("Gerando Planila de Design Roles...");
		ArrayList<String> projetosAnalisar = gLimiares.lerProjetos("analisar.txt");
		// ArrayList<CKNumber> metricasProjetosAnalisar =
		// getMetricasProjetos(projetosAnalisar);
		// gLimiares.gerarDesignRoles(metricasProjetosAnalisar, "C:\\DR\\DR-DEH.CSV");

		System.out.println("Iniciando a coleta de m�tricas do projeto referencia...");
		ArrayList<String> projetosReferencia = gLimiares.lerProjetos("referencia.txt");
		ArrayList<CKNumber> metricasProjetosReferencia = gLimiares.getMetricasProjetos(projetosReferencia);

		System.out.println("Iniciando a coleta de m�tricas dos projetos WEB...");
		ArrayList<String> projetosBenchmark = gLimiares.lerProjetos("benchmark.txt");
		// ArrayList<CKNumber> metricasProjetosBenchmark =
		// getMetricasProjetos(projetosBenchmark);

		System.out.println("Gerando Limiares por Alves...");
		// gLimiares.gerarLimiarAlves(metricasProjetosBenchmark, PASTA_RESULTADO +
		// "A.csv");

		System.out.println("Gerando Limiares por Aniche...");
		// gLimiares.gerarLimiarAniche(metricasProjetosBenchmark, PASTA_RESULTADO +
		// "X.csv");

		System.out.println("Gerando Limiares por Dosea Referencia...");
		gLimiares.gerarLimiarDoseaPercentil(metricasProjetosReferencia, PASTA_RESULTADO + "R.csv");

		System.out.println("Gerando Limiares por Dosea Referencia por Design Role...");
		gLimiares.gerarLimiarDoseaDesignRolePercentil(metricasProjetosReferencia, PASTA_RESULTADO + "D.csv");

		System.out.println("Limiares gravados na pasta " + PASTA_RESULTADO + " com sucesso!");
	}

}

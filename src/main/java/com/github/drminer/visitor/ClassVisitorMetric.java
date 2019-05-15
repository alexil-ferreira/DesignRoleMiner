package com.github.drminer.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import com.github.drminer.ClassMetricResult;
import com.github.drminer.MetricReport;
import com.github.mauricioaniche.ck.CK;

public class ClassVisitorMetric implements CommitVisitor {

	List<String> listaConcerns = new ArrayList<String>();

	public ClassVisitorMetric(List<String> listaConcerns) {
		this.listaConcerns = listaConcerns;
	}

	public ClassVisitorMetric() {
		super();
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		try {
			repo.getScm().checkout(commit.getHash());
			CK ck = new CK();

			MetricReport report = ck.calculate(repo.getPath());

			writer.write("Commit", "Class", "Design Role", "SuperClass", "interfaces", "DIT", "NOM", "CBO", "LCOM",
					"NOC", "RFC", "WMC");

			// desconsiderarConcerns(commit, writer, report, 1);

			if (listaConcerns.size() == 0) {
				for (ClassMetricResult classMetrics : report.all()) {
					String superClass = classMetrics.getSuperClassNameLevel1();
					writer.write(commit.getHash(), classMetrics.getClassName(),
							"\"" + classMetrics.getDesignRole() + "\"", "\"" + superClass + "\"",
							"\"" + classMetrics.getInterfaces() + "\"", classMetrics.getDit(), classMetrics.getNom(),
							classMetrics.getCbo(), classMetrics.getLcom(), classMetrics.getNoc(), classMetrics.getRfc(),
							classMetrics.getWmc());
				}
			} else {
				for (ClassMetricResult classMetrics : report.all()) {
					String superClass = classMetrics.getSuperClassNameLevel1();
					if (listaConcerns.contains(classMetrics.getDesignRole())) {
						writer.write(commit.getHash(), classMetrics.getClassName(),
								"\"" + classMetrics.getDesignRole() + "\"", "\"" + superClass + "\"",
								"\"" + classMetrics.getInterfaces() + "\"", classMetrics.getDit(),
								classMetrics.getNom(), classMetrics.getCbo(), classMetrics.getLcom(),
								classMetrics.getNoc(), classMetrics.getRfc(), classMetrics.getWmc());
					}
				}
			}
		} finally {
			repo.getScm().reset();
		}
	}

	private void desconsiderarConcerns(Commit commit, PersistenceMechanism writer, MetricReport report,
			int quantidadeCorte) {

		// primeiro conta quantas classes associadas ao conecern
		Map<String, Integer> mapConcerns = new HashMap<String, Integer>();
		for (ClassMetricResult classMetrics : report.all()) {
			Integer quantidade = mapConcerns.get(classMetrics.getDesignRole());
			if (quantidade != null)
				mapConcerns.put(classMetrics.getDesignRole(), ++quantidade);
			else
				mapConcerns.put(classMetrics.getDesignRole(), 1);
		}
		// Verifica se conncer deve ser desconsiderado
		for (ClassMetricResult classMetrics : report.all()) {
			Integer quantidade = mapConcerns.get(classMetrics.getDesignRole());
			if (quantidade <= quantidadeCorte)
				classMetrics.setConcern("UNDEFINED");
		}

	}

	@Override
	public String name() {
		return "ck";
	}

}
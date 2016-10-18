package com.github.mauricioaniche.ck;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import com.github.mauricioaniche.ck.metric.ClassInfo;
import com.github.mauricioaniche.ck.metric.Metric;

public class MetricsExecutor extends FileASTRequestor {

	private CKReport report;
	private Callable<List<Metric>> metrics;
	
	private static Logger log = Logger.getLogger(MetricsExecutor.class);
	
	public MetricsExecutor(Callable<List<Metric>> metrics) {
		this.metrics = metrics;
		this.report = new CKReport();
	}


	@Override
	public void acceptAST(String sourceFilePath, 
			CompilationUnit cu) {
		
		try {
			ClassInfo info = new ClassInfo();
			cu.accept(info);
			if(info.getClassName()==null) return;
		
			CKNumber result = new CKNumber(sourceFilePath, info.getClassName(), info.getType());
			for(Metric visitor : metrics.call()) {
				visitor.execute(cu, result, report);
				visitor.setResult(result);
			}
			log.info(result);
			report.add(result);
		} catch(Exception e) {
			// just ignore... sorry!
			log.error("error in " + sourceFilePath, e);
		}
	}
	
	public CKReport getReport() {
		return report;
	}
	
}

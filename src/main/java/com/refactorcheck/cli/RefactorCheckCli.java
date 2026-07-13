package com.refactorcheck.cli;

import com.refactorcheck.core.AnalysisRequest;
import com.refactorcheck.core.RefactorCheckEngine;
import com.refactorcheck.core.report.RefactorCheckReport;

public final class RefactorCheckCli {
    private RefactorCheckCli() {
    }

    public static void main(String[] args) {
        try {
            AnalysisRequest request = CliArguments.parse(args);
            RefactorCheckEngine engine = new RefactorCheckEngine();
            RefactorCheckReport report = engine.run(request);

            System.out.println("Analysis finished.");
            System.out.println("consistent=" + report.consistent);
            System.out.println("comparedMethodPairs=" + report.summary.comparedMethodPairs);
            System.out.println("inconsistentMethodPairs=" + report.summary.inconsistentMethodPairs);
            System.out.println("impactProvider=" + report.tooling.impactProvider);
            System.out.println("output=" + request.output().toAbsolutePath().normalize());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (!(e instanceof IllegalArgumentException)) {
                e.printStackTrace(System.err);
            }
            System.exit(1);
        }
    }
}

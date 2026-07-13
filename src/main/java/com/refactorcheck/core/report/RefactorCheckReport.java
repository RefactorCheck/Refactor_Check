package com.refactorcheck.core.report;

import java.util.ArrayList;
import java.util.List;

public final class RefactorCheckReport {
    public boolean consistent;
    public Summary summary;
    public Tooling tooling;
    public List<String> warnings = new ArrayList<>();
    public List<MethodFinding> methodFindings = new ArrayList<>();

    public static final class Summary {
        public int comparedMethodPairs;
        public int inconsistentMethodPairs;
        public int totalMatchedStatements;
        public long elapsedMillis;
    }

    public static final class Tooling {
        public String impactMode;
        public String impactProvider;
        public boolean fallbackUsed;
        public String refactoringMinerStatus;
    }

    public static final class MethodFinding {
        public MethodDescriptor before;
        public MethodDescriptor after;
        public boolean consistent;
        public double confidence;
        public int beforeNodes;
        public int afterNodes;
        public int matchedNodes;
        public List<String> reasons = new ArrayList<>();
    }

    public static final class MethodDescriptor {
        public String file;
        public String container;
        public String signature;
        public int startLine;
        public int endLine;
    }
}

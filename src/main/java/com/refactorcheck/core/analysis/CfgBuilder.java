package com.refactorcheck.core.analysis;

import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.model.MethodCfg;
import com.refactorcheck.core.model.MethodRef;

public interface CfgBuilder {
    MethodCfg build(SourceSnapshot snapshot, MethodRef method);
}

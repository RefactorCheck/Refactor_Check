package com.refactorcheck.core.impact;

import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.mapper.MapperSpec;
import com.refactorcheck.core.model.ImpactSource;
import com.refactorcheck.core.model.MethodModel;
import com.refactorcheck.core.model.MethodPair;

import java.util.ArrayList;
import java.util.List;

public final class MapperImpactAnalyzer implements ImpactAnalyzer {
    private final MapperSpec mapperSpec;

    public MapperImpactAnalyzer(MapperSpec mapperSpec) {
        this.mapperSpec = mapperSpec;
    }

    @Override
    public ImpactAnalysisResult analyze(SourceSnapshot before, SourceSnapshot after) {
        List<MethodPair> pairs = new ArrayList<>();

        for (MapperSpec.MapperPair pair : mapperSpec.pairs()) {
            MethodModel beforeMethod = ImpactUtils.resolveByMapperSpec(before, pair.before())
                    .orElseThrow(() -> new IllegalArgumentException("Mapper before method not found: "
                            + pair.before().file() + "#" + pair.before().signature()));
            MethodModel afterMethod = ImpactUtils.resolveByMapperSpec(after, pair.after())
                    .orElseThrow(() -> new IllegalArgumentException("Mapper after method not found: "
                            + pair.after().file() + "#" + pair.after().signature()));

            pairs.add(new MethodPair(beforeMethod.ref(), afterMethod.ref(), ImpactSource.MAPPER, 1.0));
        }

        List<MethodPair> expanded = ImpactUtils.expandWithCallers(before, after, pairs, ImpactSource.CALLER_EXPANSION);
        return new ImpactAnalysisResult(expanded, "MAPPER", false, List.of());
    }
}

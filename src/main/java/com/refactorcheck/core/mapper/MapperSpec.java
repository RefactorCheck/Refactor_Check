package com.refactorcheck.core.mapper;

import java.util.List;

public record MapperSpec(List<MapperPair> pairs) {
    public record MapperPair(MapperMethod before, MapperMethod after) {
    }

    public record MapperMethod(String file, String signature) {
    }
}

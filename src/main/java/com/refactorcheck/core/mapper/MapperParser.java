package com.refactorcheck.core.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class MapperParser {
    private final Gson gson = new Gson();

    public MapperSpec parse(Path mapperPath) {
        if (mapperPath == null) {
            throw new IllegalArgumentException("--mapper is required when --impact off");
        }

        String json;
        try {
            json = Files.readString(mapperPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read mapper file: " + mapperPath, e);
        }

        MapperSpec spec;
        try {
            spec = gson.fromJson(json, MapperSpec.class);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Invalid mapper JSON: " + mapperPath, e);
        }

        if (spec == null || spec.pairs() == null || spec.pairs().isEmpty()) {
            throw new IllegalArgumentException("Mapper JSON must include non-empty pairs[]");
        }

        for (MapperSpec.MapperPair pair : spec.pairs()) {
            validateMethod("before", pair.before());
            validateMethod("after", pair.after());
        }

        return new MapperSpec(List.copyOf(spec.pairs()));
    }

    private void validateMethod(String side, MapperSpec.MapperMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Mapper pair missing " + side + " object");
        }
        if (method.file() == null || method.file().isBlank()) {
            throw new IllegalArgumentException("Mapper method " + side + ".file is required");
        }
        if (method.signature() == null || method.signature().isBlank()) {
            throw new IllegalArgumentException("Mapper method " + side + ".signature is required");
        }
    }
}

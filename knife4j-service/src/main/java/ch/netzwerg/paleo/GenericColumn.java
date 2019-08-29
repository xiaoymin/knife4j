/*
 * Copyright 2016 Rahel Lüthy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.netzwerg.paleo;

import io.vavr.collection.Array;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;

import java.util.Objects;

public final class GenericColumn<V, I extends ColumnIds.GenericColumnId> extends AbstractColumn<V, I> {

    private GenericColumn(I id, Array<V> values, Map<String, String> metaData) {
        super(id, values, metaData);
    }

    public static <V, I extends ColumnIds.GenericColumnId> GenericColumn<V, I> of(I id, V value) {
        return of(id, value, LinkedHashMap.empty());
    }

    public static <V, I extends ColumnIds.GenericColumnId> GenericColumn<V, I> of(I id, V value, Map<String, String> metaData) {
        return new GenericColumn<>(id, Array.of(value), Objects.requireNonNull(metaData, "metaData is null"));
    }

    @SafeVarargs
    public static <V, I extends ColumnIds.GenericColumnId> GenericColumn<V, I> ofAll(I id, V... values) {
        return new GenericColumn<>(id, Array.of(values), LinkedHashMap.empty());
    }

    public static <V, I extends ColumnIds.GenericColumnId> GenericColumn<V, I> ofAll(I id, Iterable<V> values) {
        return ofAll(id, values, LinkedHashMap.empty());
    }

    public static <V, I extends ColumnIds.GenericColumnId> GenericColumn<V, I> ofAll(I id, Iterable<V> values, Map<String, String> metaData) {
        return new GenericColumn<>(id, Array.ofAll(values), Objects.requireNonNull(metaData, "metaData is null"));
    }

}
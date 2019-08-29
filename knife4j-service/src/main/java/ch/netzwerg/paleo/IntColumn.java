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

import ch.netzwerg.paleo.impl.MetaDataBuilder;
import io.vavr.collection.Map;

import java.util.Arrays;
import java.util.stream.IntStream;

import static ch.netzwerg.paleo.ColumnIds.IntColumnId;

public final class IntColumn implements Column<IntColumnId> {

    private final IntColumnId id;
    private final int[] values;
    private final Map<String, String> metaData;

    private IntColumn(IntColumnId id, IntStream values, Map<String, String> metaData) {
        this.id = id;
        this.values = values.toArray();
        this.metaData = metaData;
    }

    public static IntColumn of(IntColumnId id, int value) {
        return builder(id).add(value).build();
    }

    public static IntColumn ofAll(IntColumnId id, int... values) {
        return builder(id).addAll(values).build();
    }

    public static IntColumn ofAll(IntColumnId id, IntStream values) {
        return builder(id).addAll(values).build();
    }

    public static Builder builder(IntColumnId id) {
        return new Builder(id);
    }

    @Override
    public IntColumnId getId() {
        return id;
    }

    @Override
    public int getRowCount() {
        return values.length;
    }

    @Override
    public Map<String, String> getMetaData() {
        return metaData;
    }

    public int getValueAt(int index) {
        return values[index];
    }

    public IntStream valueStream() {
        return Arrays.stream(values);
    }

    public static final class Builder implements Column.Builder<Integer, IntColumn> {

        private final IntColumnId id;
        private final IntStream.Builder valueBuilder;
        private final MetaDataBuilder metaDataBuilder;

        private Builder(IntColumnId id) {
            this.id = id;
            this.valueBuilder = IntStream.builder();
            this.metaDataBuilder = new MetaDataBuilder();
        }

        @Override
        public Builder add(Integer value) {
            valueBuilder.add(value);
            return this;
        }

        public Builder addAll(int... values) {
            return addAll(Arrays.stream(values));
        }

        public Builder addAll(IntStream values) {
            values.forEachOrdered(this::add);
            return this;
        }

        @Override
        public Builder putMetaData(String key, String value) {
            metaDataBuilder.putMetaData(key, value);
            return this;
        }

        @Override
        public Builder putAllMetaData(Map<String, String> metaData) {
            metaDataBuilder.putAllMetaData(metaData);
            return this;
        }

        @Override
        public IntColumn build() {
            return new IntColumn(id, valueBuilder.build(), metaDataBuilder.build());
        }

    }

}
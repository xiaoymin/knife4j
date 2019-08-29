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

import ch.netzwerg.paleo.ColumnIds.LongColumnId;
import ch.netzwerg.paleo.impl.MetaDataBuilder;
import io.vavr.collection.Map;

import java.util.Arrays;
import java.util.stream.LongStream;

public class LongColumn implements Column<LongColumnId> {

    private final LongColumnId id;
    private final long[] values;
    private final Map<String, String> metaData;

    private LongColumn(LongColumnId id, LongStream values, Map<String, String> metaData) {
        this.id = id;
        this.values = values.toArray();
        this.metaData = metaData;
    }

    public static LongColumn of(LongColumnId id, long value) {
        return builder(id).add(value).build();
    }

    public static LongColumn ofAll(LongColumnId id, long... values) {
        return builder(id).addAll(values).build();
    }

    public static LongColumn ofAll(LongColumnId id, LongStream values) {
        return builder(id).addAll(values).build();
    }

    public static Builder builder(LongColumnId id) {
        return new Builder(id);
    }

    @Override
    public LongColumnId getId() {
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

    public long getValueAt(int index) {
        return values[index];
    }

    public LongStream valueStream() {
        return Arrays.stream(values);
    }

    public static final class Builder implements Column.Builder<Long, LongColumn> {

        private final LongColumnId id;
        private final LongStream.Builder valueBuilder;
        private final MetaDataBuilder metaDataBuilder;

        private Builder(LongColumnId id) {
            this.id = id;
            this.valueBuilder = LongStream.builder();
            this.metaDataBuilder = new MetaDataBuilder();
        }

        @Override
        public Builder add(Long value) {
            valueBuilder.add(value);
            return this;
        }

        public Builder add(Integer value) {
            valueBuilder.add(value);
            return this;
        }

        public Builder addAll(long... values) {
            return addAll(Arrays.stream(values));
        }

        public Builder addAll(Iterable<Long> values) {
            for (Long value : values) {
                this.valueBuilder.add(value);
            }
            return this;
        }

        public Builder addAll(LongStream values) {
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
        public LongColumn build() {
            return new LongColumn(id, valueBuilder.build(), metaDataBuilder.build());
        }

    }

}

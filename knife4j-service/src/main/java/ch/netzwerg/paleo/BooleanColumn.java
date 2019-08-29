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

import ch.netzwerg.paleo.ColumnIds.BooleanColumnId;
import ch.netzwerg.paleo.impl.MetaDataBuilder;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

public final class BooleanColumn implements Column<BooleanColumnId> {

    private final BooleanColumnId id;
    private final int rowCount;
    private final BitSet values;
    private final Map<String, String> metaData;

    private BooleanColumn(BooleanColumnId id, int rowCount, BitSet values, Map<String, String> metaData) {
        this.id = id;
        this.rowCount = rowCount;
        this.values = (BitSet) values.clone();
        this.metaData = metaData;
    }

    public static BooleanColumn of(BooleanColumnId id, boolean value) {
        return builder(id).add(value).build();
    }

    public static BooleanColumn ofAll(BooleanColumnId id, boolean... values) {
        return builder(id).addAll(values).build();
    }

    public static BooleanColumn ofAll(BooleanColumnId id, Iterable<Boolean> values) {
        return builder(id).addAll(values).build();
    }

    public static Builder builder(BooleanColumnId id) {
        return new Builder(id);
    }

    @Override
    public BooleanColumnId getId() {
        return id;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public Map<String, String> getMetaData() {
        return metaData;
    }

    public boolean getValueAt(int rowIndex) {
        return values.get(rowIndex);
    }

    public Stream<Boolean> valueStream() {
        return Stream.range(0, rowCount).map(values::get);
    }

    public static final class Builder implements Column.Builder<Boolean, BooleanColumn> {

        private final BooleanColumnId id;
        private final AtomicInteger rowIndex;
        private final BitSet values;
        private final MetaDataBuilder metaDataBuilder;

        private Builder(BooleanColumnId id) {
            this.id = id;
            this.rowIndex = new AtomicInteger();
            this.values = new BitSet();
            this.metaDataBuilder = new MetaDataBuilder();
        }

        @Override
        public Builder add(Boolean value) {
            values.set(rowIndex.getAndIncrement(), value);
            return this;
        }

        public Builder addAll(boolean... values) {
            return addAll(Stream.ofAll(values));
        }

        public Builder addAll(Iterable<Boolean> values) {
            return Stream.ofAll(values).foldLeft(this, Builder::add);
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
        public BooleanColumn build() {
            return new BooleanColumn(id, rowIndex.get(), values, metaDataBuilder.build());
        }

    }

}

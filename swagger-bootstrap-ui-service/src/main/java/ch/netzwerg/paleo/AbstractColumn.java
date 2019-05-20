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

import io.vavr.collection.IndexedSeq;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

import static ch.netzwerg.paleo.ColumnIds.GenericColumnId;

abstract class AbstractColumn<V, I extends GenericColumnId> implements Column<I> {

    private final I id;
    private final IndexedSeq<V> values;
    private final Map<String, String> metaData;

    protected AbstractColumn(I id, IndexedSeq<V> values, Map<String, String> metaData) {
        this.id = id;
        this.values = values;
        this.metaData = metaData;
    }

    @Override
    public final I getId() {
        return id;
    }

    @Override
    public final int getRowCount() {
        return values.length();
    }

    @Override
    public final Map<String, String> getMetaData() {
        return metaData;
    }

    public final IndexedSeq<V> getValues() {
        return values;
    }

    public final Stream<V> valueStream() {
        return values.toStream();
    }

    public final V getValueAt(int index) {
        return values.get(index);
    }

}
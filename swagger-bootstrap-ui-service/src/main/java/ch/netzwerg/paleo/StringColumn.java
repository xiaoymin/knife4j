/*
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
import io.vavr.collection.Array;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

import java.util.ArrayList;

import static ch.netzwerg.paleo.ColumnIds.StringColumnId;

public final class StringColumn extends AbstractColumn<String, StringColumnId> {

    private StringColumn(StringColumnId id, Array<String> values, Map<String, String> metaData) {
        super(id, values, metaData);
    }

    public static StringColumn of(StringColumnId id, String value) {
        return builder(id).add(value).build();
    }

    public static StringColumn ofAll(StringColumnId id, String... values) {
        return builder(id).addAll(values).build();
    }

    public static StringColumn ofAll(StringColumnId id, Iterable<String> values) {
        return builder(id).addAll(values).build();
    }

    public static Builder builder(StringColumnId id) {
        return new Builder(id);
    }

    public static final class Builder implements Column.Builder<String, StringColumn> {

        private final StringColumnId id;
        private final java.util.List<String> values;
        private final MetaDataBuilder metaDataBuilder;

        private Builder(StringColumnId id) {
            this.id = id;
            this.values = new ArrayList<>();
            this.metaDataBuilder = new MetaDataBuilder();
        }

        @Override
        public Builder add(String value) {
            values.add(value);
            return this;
        }

        public Builder addAll(String... values) {
            return addAll(Stream.of(values));
        }

        public Builder addAll(Iterable<String> values) {
            this.values.addAll(Stream.ofAll(values).toJavaList());
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
        public StringColumn build() {
            return new StringColumn(id, Array.ofAll(values), metaDataBuilder.build());
        }

    }

}
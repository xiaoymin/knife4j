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

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import static ch.netzwerg.paleo.ColumnIds.*;
import static io.vavr.Tuple.of;

public final class ColumnType<T extends ColumnId> {

    public static final ColumnType<IntColumnId> INT = new ColumnType<>("Int", IntColumnId.class);
    public static final ColumnType<LongColumnId> LONG = new ColumnType<>("Long", LongColumnId.class);
    public static final ColumnType<DoubleColumnId> DOUBLE = new ColumnType<>("Double", DoubleColumnId.class);
    public static final ColumnType<BooleanColumnId> BOOLEAN = new ColumnType<>("Boolean", BooleanColumnId.class);
    public static final ColumnType<StringColumnId> STRING = new ColumnType<>("String", StringColumnId.class);
    public static final ColumnType<TimestampColumnId> TIMESTAMP = new ColumnType<>("Timestamp", TimestampColumnId.class);
    public static final ColumnType<CategoryColumnId> CATEGORY = new ColumnType<>("Category", CategoryColumnId.class);

    private static final Map<String, ColumnType<?>> TYPES_BY_DESCRIPTION = HashMap.<String, ColumnType<?>>ofEntries(
            of(ColumnType.INT.getDescription(), ColumnType.INT),
            of(ColumnType.LONG.getDescription(), ColumnType.LONG),
            of(ColumnType.DOUBLE.getDescription(), ColumnType.DOUBLE),
            of(ColumnType.BOOLEAN.getDescription(), ColumnType.BOOLEAN),
            of(ColumnType.STRING.getDescription(), ColumnType.STRING),
            of(ColumnType.TIMESTAMP.getDescription(), ColumnType.TIMESTAMP),
            of(ColumnType.CATEGORY.getDescription(), ColumnType.CATEGORY));

    private final String description;
    private final Class<T> idType;

    public ColumnType(String description, Class<T> idType) {
        this.description = description;
        this.idType = idType;
    }

    public String getDescription() {
        return this.description;
    }

    public Class<T> getIdType() {
        return idType;
    }

    public static ColumnType<?> getByDescriptionOrDefault(String description, ColumnType<?> defaultType) {
        return TYPES_BY_DESCRIPTION.get(description).getOrElse(defaultType);
    }

    @Override
    public String toString() {
        return description;
    }

}
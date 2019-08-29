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

public interface ColumnIds {

    final class IntColumnId extends GenericColumnId {

        private IntColumnId(String name) {
            super(name, ColumnType.INT);
        }

        public static IntColumnId of(String name) {
            return new IntColumnId(name);
        }

    }

    final class LongColumnId extends GenericColumnId {

        private LongColumnId(String name) {
            super(name, ColumnType.LONG);
        }

        public static LongColumnId of(String name) {
            return new LongColumnId(name);
        }
    }

    final class DoubleColumnId extends GenericColumnId {

        private DoubleColumnId(String name) {
            super(name, ColumnType.DOUBLE);
        }

        public static DoubleColumnId of(String name) {
            return new DoubleColumnId(name);
        }

    }

    final class BooleanColumnId extends GenericColumnId {

        private BooleanColumnId(String name) {
            super(name, ColumnType.BOOLEAN);
        }

        public static BooleanColumnId of(String name) {
            return new BooleanColumnId(name);
        }

    }

    final class StringColumnId extends GenericColumnId {

        private StringColumnId(String name) {
            super(name, ColumnType.STRING);
        }

        public static StringColumnId of(String name) {
            return new StringColumnId(name);
        }

    }

    final class TimestampColumnId extends GenericColumnId {

        private TimestampColumnId(String name) {
            super(name, ColumnType.TIMESTAMP);
        }

        public static TimestampColumnId of(String name) {
            return new TimestampColumnId(name);
        }

    }

    final class CategoryColumnId extends GenericColumnId {

        private CategoryColumnId(String name) {
            super(name, ColumnType.CATEGORY);
        }

        public static CategoryColumnId of(String name) {
            return new CategoryColumnId(name);
        }

    }

    class GenericColumnId implements ColumnId {

        private final String name;
        private final ColumnType<?> type;

        public GenericColumnId(String name, ColumnType<?> type) {
            this.name = name;
            this.type = type;
        }

        public static GenericColumnId of(String name, ColumnType<?> type) {
            return new GenericColumnId(name, type);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ColumnType<?> getType() {
            return type;
        }

    }

}

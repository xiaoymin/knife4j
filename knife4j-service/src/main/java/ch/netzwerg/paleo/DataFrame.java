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

import io.vavr.collection.*;

import java.time.Instant;

import static ch.netzwerg.paleo.ColumnIds.*;

public final class DataFrame implements Iterable<Column<?>> {

    private final Array<Column<?>> columns;
    private final int rowCount;
    private final Map<String, String> metaData;

    private DataFrame(Array<Column<?>> columns) {
        this(columns, HashMap.empty());
    }

    private DataFrame(Array<Column<?>> columns, Map<String, String> metaData) {
        this.columns = columns;
        this.rowCount = inferRowCount(columns);
        this.metaData = metaData;
    }

    private static int inferRowCount(IndexedSeq<Column<?>> columns) {
        if (columns.isEmpty()) {
            return 0;
        } else {
            Set<Integer> rowCounts = columns.map(Column::getRowCount).distinct().toSet();
            if (rowCounts.length() > 1) {
                throw new IllegalArgumentException("Differing number of rows (i.e. column sizes)");
            } else {
                return rowCounts.iterator().next();
            }
        }
    }

    public static DataFrame empty() {
        return new DataFrame(Array.empty());
    }

    public static DataFrame of(Column<?> column) {
        return ofAll(Array.of(column));
    }

    public static DataFrame ofAll(Column<?>... columns) {
        return ofAll(Array.of(columns));
    }

    public static DataFrame ofAll(Iterable<? extends Column<?>> columns) {
        return new DataFrame(Array.ofAll(columns));
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columns.length();
    }

    public IndexedSeq<ColumnId> getColumnIds() {
        return columns.map(Column::getId);
    }

    public IndexedSeq<Column<?>> getColumns() {
        return columns;
    }

    public IndexedSeq<String> getColumnNames() {
        return getColumnIds().map(ColumnId::getName);
    }

    public <C extends ColumnId> C getColumnId(int columnIndex, ColumnType<C> type) {
        Class<C> idType = type.getIdType();
        return idType.cast(columns.get(columnIndex).getId());
    }

    public IntColumn getColumn(IntColumnId columnId) {
        return getTypedColumn(columnId);
    }

    public DoubleColumn getColumn(DoubleColumnId columnId) {
        return getTypedColumn(columnId);
    }

    public BooleanColumn getColumn(BooleanColumnId columnId) {
        return getTypedColumn(columnId);
    }

    public StringColumn getColumn(StringColumnId columnId) {
        return getTypedColumn(columnId);
    }

    public TimestampColumn getColumn(TimestampColumnId columnId) {
        return getTypedColumn(columnId);
    }

    public CategoryColumn getColumn(CategoryColumnId columnId) {
        return getTypedColumn(columnId);
    }

    public <V, I extends GenericColumnId> GenericColumn<V, I> getColumn(I columnId) {
        return getTypedColumn(columnId);
    }

    public Column<?> getColumn(ColumnId columnId) {
        return getTypedColumn(columnId);
    }

    public int getValueAt(int rowIndex, IntColumnId columnId) {
        IntColumn column = getTypedColumn(columnId);
        return column.getValueAt(rowIndex);
    }

    public double getValueAt(int rowIndex, DoubleColumnId columnId) {
        DoubleColumn column = getTypedColumn(columnId);
        return column.getValueAt(rowIndex);
    }

    public boolean getValueAt(int rowIndex, BooleanColumnId columnId) {
        BooleanColumn column = getTypedColumn(columnId);
        return column.getValueAt(rowIndex);
    }

    public String getValueAt(int rowIndex, StringColumnId columnId) {
        StringColumn column = getTypedColumn(columnId);
        return column.getValueAt(rowIndex);
    }

    public Instant getValueAt(int rowIndex, TimestampColumnId columnId) {
        TimestampColumn column = getTypedColumn(columnId);
        return column.getValueAt(rowIndex);
    }

    public String getValueAt(int rowIndex, CategoryColumnId columnId) {
        CategoryColumn column = getTypedColumn(columnId);
        return column.getValueAt(rowIndex);
    }

    public <V, I extends GenericColumnId> V getValueAt(int rowIndex, I columnId) {
        GenericColumn<V, I> column = getTypedColumn(columnId);
        return column.getValueAt(rowIndex);
    }

    @SuppressWarnings("unchecked")
    private <T extends Column<?>> T getTypedColumn(ColumnId columnId) {
        return (T) columns.find(c -> c.getId().equals(columnId)).getOrElseThrow(() -> {
            String msg = String.format("Unknown column id '%s'", columnId);
            throw new IllegalArgumentException(msg);
        });
    }

    @Override
    public Iterator<Column<?>> iterator() {
        return columns.iterator();
    }

    public DataFrame withMetaData(Map<String, String> metaData) {
        return new DataFrame(columns, metaData);
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

}
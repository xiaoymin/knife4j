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

import io.vavr.collection.Map;

public interface Column<T extends ColumnId> {

    T getId();

    int getRowCount();

    Map<String, String> getMetaData();

    interface Builder<V, C extends Column<?>> {

        Builder<V, C> add(V value);

        Builder<V, C> putMetaData(String key, String value);

        Builder<V, C> putAllMetaData(Map<String, String> metaData);

        C build();
    }

}
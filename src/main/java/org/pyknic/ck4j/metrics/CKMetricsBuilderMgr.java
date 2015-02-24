/**
 *
 * Copyright (c) 2015, Emil Forslund. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pyknic.ck4j.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.bcel.classfile.JavaClass;

/**
 * Instantiates builders for new classes encountered and holds them for future
 * use.
 * @author Emil Forslund
 */
public final class CKMetricsBuilderMgr {
    private final Map<String, CKMetricsBuilder> builders = new HashMap<>();
    
    public CKMetricsBuilder get(JavaClass clazz) {
        final String className = clazz.getClassName();
        return builders.putIfAbsent(className, new CKMetricsBuilder(clazz, this));
    }
    
    public CKMetricsBuilder get(String className) {
        final CKMetricsBuilder result = builders.get(className);
        
        if (result == null) {
            throw new UnsupportedOperationException("Attempting to access builder for " + className + " before initiated.");
        }
        
        return result;
    }
    
    public Stream<Map.Entry<String, CKMetrics>> stream() {
        return builders.entrySet().stream()
            .map(e -> new HashMap.SimpleEntry<>(
                e.getKey(), e.getValue().build()
            ));
    }
}
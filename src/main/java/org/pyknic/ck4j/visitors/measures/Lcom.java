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
package org.pyknic.ck4j.visitors.measures;

import org.pyknic.ck4j.visitors.measures.interfaces.Metric;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.MethodGen;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;
import org.pyknic.ck4j.visitors.measures.interfaces.OnMethod;

/**
 *
 * @author Emil Forslund
 */
public class Lcom extends Metric implements OnMethod {
    private final List<Set<String>> fieldsInMethods = new ArrayList<>();

    public Lcom(JavaClass visited, CKMetricsBuilderMgr mgr) {
        super(visited, mgr);
    }

    @Override
    public void onMethod(MethodGen method) {
        fieldsInMethods.add(Stream.of(method.getArgumentTypes())
            .map(t -> t.getSignature())
            .collect(Collectors.toCollection(
                () -> new TreeSet<String>()
            ))
        );
    }
    
    @Override
    public int getResult() {
        int lcom = 0;
        
        for (int i = 0; i < fieldsInMethods.size(); i++) {
            for (int j = i + 1; j < fieldsInMethods.size(); j++) {
                
            final TreeSet<?> intersection = new TreeSet<>(fieldsInMethods.get(i));
            intersection.retainAll(fieldsInMethods.get(j));
            
            if (intersection.isEmpty())
                lcom++;
            else
                lcom--;
            }
        }
        
        return lcom > 0 ? lcom : 0;
    }
}
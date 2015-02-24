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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.MethodGen;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;
import org.pyknic.ck4j.visitors.measures.listeners.OnField;
import org.pyknic.ck4j.visitors.measures.listeners.OnMethod;

/**
 *
 * @author Emil Forslund
 */
public class Lcom extends Metric implements OnMethod, OnField {
    private final List<Set<String>> fieldsInMethods = new ArrayList<>();

    public Lcom(JavaClass visited, CKMetricsBuilderMgr mgr) {
        super(visited, mgr);
    }

    @Override
    public void onMethod(MethodGen method) {
        fieldsInMethods.add(new TreeSet<>());
    }

    @Override
    public void onField(Field field) {
        fieldsInMethods.get(fieldsInMethods.size() - 1).add(field.getName());
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
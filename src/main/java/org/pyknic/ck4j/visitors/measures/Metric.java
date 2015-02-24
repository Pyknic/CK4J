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

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ConstantPoolGen;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;

/**
 *
 * @author Emil Forslund
 */
public abstract class Metric {
    private final JavaClass visited;
    private final CKMetricsBuilderMgr mgr;
    private final ConstantPoolGen constants;
    
    public Metric(JavaClass visited, CKMetricsBuilderMgr mgr) {
        this.visited = visited;
        this.mgr = mgr;
        this.constants = new ConstantPoolGen(visited.getConstantPool());
    }

    protected final JavaClass visited() {
        return visited;
    }

    protected final CKMetricsBuilderMgr mgr() {
        return mgr;
    }
    
    protected final ConstantPoolGen constants() {
        return constants;
    }
    
    public abstract int getResult();
}
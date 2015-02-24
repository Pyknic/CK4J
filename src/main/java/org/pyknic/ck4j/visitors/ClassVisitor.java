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
package org.pyknic.ck4j.visitors;

import java.util.stream.Stream;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;
import org.pyknic.ck4j.metrics.CKMetricsBuilder;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;

/**
 *
 * @author Emil Forslund
 */
public class ClassVisitor extends EmptyVisitor {
    private final JavaClass visited;
    private final ConstantPoolGen constants;
    private final CKMetricsBuilder builder;
    private final CKMetricsBuilderMgr mgr;

    /**
     * Instantiates the ClassVisitor for a particular class.
     * @param visited The class to be visited.
     * @param mgr The manager of all CKMetricsBuilders.
     */
    public ClassVisitor(JavaClass visited, CKMetricsBuilderMgr mgr) {
        this.visited    = visited;
        this.constants  = new ConstantPoolGen(visited.getConstantPool());
        this.builder    = mgr.get(visited);
        this.mgr        = mgr;
    }

    public void visit() {
        builder.getMetricsMgr().notifyClass(visited);
        
        try {
            Stream.of(visited.getAllInterfaces()).forEach(i -> builder.getMetricsMgr().notifyInterface(i));
        } catch (ClassNotFoundException ex) {
            throw new UnsupportedOperationException(ex);
        }
        
        Stream.of(visited.getFields()).forEach(f -> builder.getMetricsMgr().notifyField(f));
        Stream.of(visited.getMethods()).forEach(m -> processMethod(m));
    }
    
    private void processMethod(Method m) {
        final MethodGen gen = new MethodGen(m, visited.getClassName(), constants);
        
        builder.getMetricsMgr().notifyMethod(gen);
        
        if (!gen.isAbstract() && !gen.isNative()) {
            Stream.of(gen.getInstructionList().getInstructionHandles())
                .map(ih -> ih.getInstruction())
                .forEach(i -> builder.getMetricsMgr().notifyInstruction(i));
        }
    }
}
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
package org.pyknic.ck4j.visitors.measures.interfaces;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.MethodGen;
import org.pyknic.ck4j.metrics.CK;

/**
 *
 * @author Emil Forslund
 */
public abstract class MetricsMgr {
    
    protected final Map<CK, Metric> metrics = new EnumMap<>(CK.class);
    protected final List<OnClass> onClass = new ArrayList<>();
	protected final List<OnChild> onChild = new ArrayList<>();
    protected final List<OnField> onField = new ArrayList<>();
    protected final List<OnInstruction> onInstruction = new ArrayList<>();
    protected final List<OnInterface> onInterface = new ArrayList<>();
    protected final List<OnMethod> onMethod = new ArrayList<>();
    protected final List<OnCoupling> onCoupling = new ArrayList<>();
    
    private MetricsMgr() {}
    
    public static class Impl extends MetricsMgr {
        public Impl() {}
        
        public Impl add(CK name, Metric metric) {
            metrics.put(name, metric);

            if (metric instanceof OnClass)       onClass.add((OnClass) metric);
			if (metric instanceof OnChild)       onChild.add((OnChild) metric);
            if (metric instanceof OnField)       onField.add((OnField) metric);
            if (metric instanceof OnInstruction) onInstruction.add((OnInstruction) metric);
            if (metric instanceof OnInterface)   onInterface.add((OnInterface) metric);
            if (metric instanceof OnMethod)      onMethod.add((OnMethod) metric);
            if (metric instanceof OnCoupling)    onCoupling.add((OnCoupling) metric);

            return this;
        }
    }
    
    public void notifyClass(JavaClass clazz) {
        onClass.stream().forEach(c -> c.onClass(clazz));
    }
	
	public void notifyChild(JavaClass child) {
        onChild.stream().forEach(c -> c.onChild(child));
    }
    
    public void notifyField(Field field) {
        onField.stream().forEach(c -> c.onField(field));
    }
    
    public void notifyInstruction(Instruction instr) {
        onInstruction.stream().forEach(c -> c.onInstruction(instr));
    }
    
    public void notifyInterface(String interf) {
        onInterface.stream().forEach(c -> c.onInterface(interf));
    }
    
    public void notifyMethod(MethodGen method) {
        onMethod.stream().forEach(c -> c.onMethod(method));
    }
    
    public void notifyCoupled(String className) {
        onCoupling.stream().forEach(c -> c.onCoupled(className));
    }
    
    public int get(CK metric) {
        return metrics.get(metric).getResult();
    }
}

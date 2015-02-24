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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;
import org.pyknic.ck4j.visitors.Settings;
import static org.pyknic.ck4j.visitors.Settings.COUPLE_TO_INTERFACES;
import static org.pyknic.ck4j.visitors.Settings.COUPLE_TO_JAVA_SDK;
import org.pyknic.ck4j.visitors.measures.listeners.OnClass;
import org.pyknic.ck4j.visitors.measures.listeners.OnCoupling;
import org.pyknic.ck4j.visitors.measures.listeners.OnField;
import org.pyknic.ck4j.visitors.measures.listeners.OnInstruction;
import org.pyknic.ck4j.visitors.measures.listeners.OnInterface;
import org.pyknic.ck4j.visitors.measures.listeners.OnMethod;

/**
 *
 * @author Emil Forslund
 */
public class Cbo extends Metric implements
        OnClass, OnInterface, OnField, OnMethod, OnInstruction, OnCoupling {
    
    private final Set<String> efferentCouplings = new HashSet<>();

    public Cbo(JavaClass visited, CKMetricsBuilderMgr mgr) {
        super (visited, mgr);
    }
    
    @Override
    public int getResult() {
        return efferentCouplings.size();
    }

    @Override
    public void onClass(JavaClass clazz) {
        registerCoupling(clazz.getSuperclassName());
    }

    @Override
    public void onInterface(JavaClass interf) {
        if (COUPLE_TO_INTERFACES.isSet()) {
            registerCoupling(interf.getClassName());
        }
    }

    @Override
    public void onField(Field field) {
        registerCoupling(field.getType());
    }
    
    @Override
    public void onMethod(MethodGen gen) {
        registerCoupling(gen.getReturnType());
        
        Stream.of(gen.getArgumentTypes()).forEach(t -> registerCoupling(t));
        Stream.of(gen.getExceptions()).forEach(t -> registerCoupling(t));
        Stream.of(gen.getExceptionHandlers())
            .map(eh -> eh.getCatchType())
            .filter(ct -> ct != null)
            .forEach(ct -> registerCoupling(ct));
    }

    @Override
    public void onInstruction(Instruction i) {
        if (shouldVisit(i)) {
            if (i instanceof FieldInstruction) {
                registerCoupling(((FieldInstruction) i).getFieldType(constants()));

            } else if (i instanceof InvokeInstruction) {
                final InvokeInstruction ii = (InvokeInstruction) i;
                Stream.of(ii.getArgumentTypes(constants())).forEach(a -> registerCoupling(a));
                registerCoupling(ii.getReturnType(constants()));

            } else if (i instanceof TypedInstruction) {
                registerCoupling(((TypedInstruction) i).getType(constants()));
            }
        }
    }

    @Override
    public void onCoupling(String className) {
        efferentCouplings.add(className);
    }
    
    private boolean shouldVisit(Instruction i) {
        return ((InstructionConstants.INSTRUCTIONS[i.getOpcode()] != null)
            && !(i instanceof ConstantPushInstruction)
            && !(i instanceof ReturnInstruction)
        );
    }
    
    private void registerCoupling(Type type) {
        registerCoupling(className(type));
    }
    
    private void registerCoupling(String name) {
        if (COUPLE_TO_JAVA_SDK.isSet()
        || !Settings.isJavaSDK(name)) {
            onCoupling(name);
            mgr().get(name).getMetricsMgr().notifyCoupling(visited().getClassName());
        }
    }
    
    private static String className(Type type) {
        if (type.getType() <= Constants.T_VOID) {
            return "java.PRIMITIVE";
        } else if (type instanceof ArrayType) {
            return className(((ArrayType) type).getBasicType());
        } else {
            return type.toString();
        }
    }
}
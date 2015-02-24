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

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.generic.ArrayInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.TypedInstruction;

/**
 *
 * @author Emil Forslund
 */
public class CboMethodVisitor extends EmptyVisitor {
    private final MethodGen gen;
    private final ConstantPoolGen constants;
    private final Consumer<Type> registerCoupling;
    
    public CboMethodVisitor(MethodGen gen, Consumer<Type> registerCoupling) {
        this.gen = gen;
        this.constants = gen.getConstantPool();
        this.registerCoupling = registerCoupling;
    }
    
    public void visit() {
        if (!gen.isAbstract() && !gen.isNative()) {
            Stream.of(gen.getInstructionList().getInstructionHandles())    
            .map(ih -> ih.getInstruction()).forEach(i -> {
                if (visitInstruction(i)) {
                    if (i instanceof FieldInstruction) {
                        registerCoupling(((FieldInstruction) i).getFieldType(constants));
                        
                    } else if (i instanceof InvokeInstruction) {
                        final InvokeInstruction ii = (InvokeInstruction) i;
                        Stream.of(ii.getArgumentTypes(constants)).forEach(a -> registerCoupling(a));
                        registerCoupling(ii.getReturnType(constants));
                        
                    } else if (i instanceof TypedInstruction) {
                        registerCoupling(((TypedInstruction) i).getType(constants));
                    }
                }
                
                Stream.of(gen.getExceptionHandlers())
                    .map(eh -> eh.getCatchType())
                    .filter(ct -> ct != null)
                    .forEach(ct -> registerCoupling(ct));
            });
        }
    }
    
    private boolean visitInstruction(Instruction i) {
        return ((InstructionConstants.INSTRUCTIONS[i.getOpcode()] != null)
            && !(i instanceof ConstantPushInstruction)
            && !(i instanceof ReturnInstruction)
        );
    }
    
    private void registerCoupling(Type e) {
        registerCoupling.accept(e);
    }
}
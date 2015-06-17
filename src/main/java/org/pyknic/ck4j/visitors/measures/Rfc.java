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
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import org.pyknic.ck4j.visitors.measures.interfaces.Metric;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;
import org.pyknic.ck4j.visitors.measures.interfaces.OnInstruction;
import org.pyknic.ck4j.visitors.measures.interfaces.OnMethod;

/**
 *
 * @author Emil Forslund
 */
public class Rfc extends Metric implements OnMethod, OnInstruction {

	private final Set<MethodGen> methods;
	private final Set<RemoteMethod> remoteMethods;

	public Rfc(JavaClass visited, CKMetricsBuilderMgr mgr) {
		super(visited, mgr);
		methods = new HashSet<>();
		remoteMethods = new HashSet<>();
	}

	@Override
	public void onMethod(MethodGen method) {
		if (!method.getName().contains("$")) {
			remoteMethods.add(new RemoteMethod(
				method.getType(),
				method.getName(),
				method.getArgumentTypes()
			));
		}
	}

	@Override
	public void onInstruction(Instruction instr) {
		if (instr instanceof InvokeInstruction
		&&  instr instanceof LoadClass) {
			final InvokeInstruction ii = (InvokeInstruction) instr;

			final RemoteMethod rm = new RemoteMethod(
				ii.getReferenceType(constants()),
				ii.getMethodName(constants()),
				ii.getArgumentTypes(constants())
			);

			remoteMethods.add(rm);
//			
//			
//			
//			
//			// Instantiation calls should not be counted.
//			//if (!ii.getMethodName(constants()).equals("<init>")) {
//			// Make sure it is not a call to an internal method.
//			final String signature = "L" + visited().getClassName().replace(".", "/") + ";";
//
//			try {
//				
//				final Type type = Type.getType(signature);
//				
//				// Check if referenced type is an implemented interface.
//				if (!Stream.of(visited().getInterfaces())
//					.map(i -> "L" + i.getClassName().replace(".", "/") + ";")
//					.filter(i -> signature.equals(i))
//					.findAny().isPresent()) {
//				
//					//final Type ext = ii.getReferenceType(constants());
//
//					if (!ii.getReferenceType(constants()).isCastableTo(type)) {
//
//						//if (!ii.getReferenceType(constants()).getSignature().equals(signature)) {
//						System.err.println("RF: " + ii.getReferenceType(constants()).getSignature());
//						System.err.println("TH: " + signature);
//
//						final RemoteMethod rm = new RemoteMethod(
//								ii.getReferenceType(constants()),
//								ii.getMethodName(constants()),
//								ii.getArgumentTypes(constants())
//						);
//
//						remoteMethods.add(rm);
//						//}
//
//					}
//				}
//			} catch (ClassNotFoundException ex) {
//				Logger.getLogger(Rfc.class.getName()).log(Level.SEVERE, null, ex);
//			}
//			//}

		}
	}

	@Override
	public int getResult() {
//		System.out.println(
//				"-------------------------------------\n"
//				+ visited().getClassName() + "\n"
//				+ remoteMethods.stream().map(m -> "    " + m.toString()).collect(joining("\n"))
//		);
		return /*methods.size() + */remoteMethods.size();
	}

	public final class RemoteMethod {

		private final Type classType;
		private final String methodName;
		private final Type[] argumentTypes;

		private RemoteMethod(Type classType, String methodName, Type[] argumentTypes) {
			this.classType = classType;
			this.methodName = methodName;
			this.argumentTypes = argumentTypes;
		}

		public Type getClassType() {
			return classType;
		}

		public String getMethodName() {
			return methodName;
		}

		public Type[] getArgumentTypes() {
			return argumentTypes;
		}

		@Override
		public String toString() {
			return classType.getSignature() + "."
					+ methodName + "("
					+ Stream.of(argumentTypes)
					.map(at -> at.getSignature())
					.collect(joining(", "))
					+ ")";
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return Optional.ofNullable(obj)
					.filter(o -> o instanceof RemoteMethod)
					.map(o -> (RemoteMethod) o)
					.filter(o -> toString().equals(o.toString()))
					.isPresent();
		}
	}
}

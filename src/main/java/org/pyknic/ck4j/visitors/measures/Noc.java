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
import org.pyknic.ck4j.visitors.measures.interfaces.Metric;
import org.apache.bcel.classfile.JavaClass;
import org.pyknic.ck4j.metrics.CKMetricsBuilder;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;
import org.pyknic.ck4j.visitors.measures.interfaces.OnChild;
import org.pyknic.ck4j.visitors.measures.interfaces.OnClass;

/**
 *
 * @author Emil Forslund
 */
public class Noc extends Metric implements OnClass, OnChild {
	
	private final Set<JavaClass> children;
	
    public Noc(JavaClass visited, CKMetricsBuilderMgr mgr) {
        super(visited, mgr);
		this.children = new HashSet<>();
    }
	
	@Override
	public void onChild(JavaClass child) {
		children.add(child);
	}
	
	@Override
	public void onClass(JavaClass me) {
		try {
			final JavaClass parent = me.getSuperClass();
			if (parent != null) {
				final CKMetricsBuilder ck = mgr().get(parent);
				ck.getMetricsMgr().notifyChild(me);
			}
		} catch (ClassNotFoundException ex) {
			//Logger.getLogger(Noc.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
    
    @Override
    public int getResult() {
        return children.size();
    }
}
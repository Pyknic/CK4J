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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.pyknic.ck4j.visitors.measures.interfaces.Metric;
import org.apache.bcel.classfile.JavaClass;
import org.pyknic.ck4j.metrics.CKMetricsBuilderMgr;


/**
 *
 * @author Emil Forslund
 */
public class Dit extends Metric {

    public Dit(JavaClass visited, CKMetricsBuilderMgr mgr) {
        super(visited, mgr);
    }
    
    @Override
    public int getResult() {
        try {
            return visited().getSuperClasses().length;
        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Dit.class.getName()).log(Level.SEVERE, 
//                "Failed to load one or more super classes of '" + visited().getClassName() + "'."
//            , ex);
            return -1;
        }
    }
}
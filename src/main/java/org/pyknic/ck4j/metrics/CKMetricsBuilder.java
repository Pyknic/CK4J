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
package org.pyknic.ck4j.metrics;

import org.apache.bcel.classfile.JavaClass;
import static org.pyknic.ck4j.metrics.CK.CBO;
import static org.pyknic.ck4j.metrics.CK.DIT;
import static org.pyknic.ck4j.metrics.CK.LCOM;
import static org.pyknic.ck4j.metrics.CK.NOC;
import static org.pyknic.ck4j.metrics.CK.RFC;
import static org.pyknic.ck4j.metrics.CK.WMC;
import org.pyknic.ck4j.visitors.measures.Cbo;
import org.pyknic.ck4j.visitors.measures.Dit;
import org.pyknic.ck4j.visitors.measures.Lcom;
import org.pyknic.ck4j.visitors.measures.interfaces.MetricsMgr;
import org.pyknic.ck4j.visitors.measures.Noc;
import org.pyknic.ck4j.visitors.measures.Rfc;
import org.pyknic.ck4j.visitors.measures.Wmc;

/**
 *
 * @author Emil Forslund
 */
public class CKMetricsBuilder {
    private final String label;
    private final MetricsMgr metrics;
    
    CKMetricsBuilder() {
        label = "";
        metrics = new MetricsMgr.Impl() {
            @Override
            public int get(CK metric) {
                return 0;
            }
        };
    }
    
    CKMetricsBuilder(JavaClass clazz, CKMetricsBuilderMgr mgr) {
        label = clazz.getClassName();
        metrics = new MetricsMgr.Impl()
            .add(WMC,  new Wmc(clazz, mgr))
            .add(NOC,  new Noc(clazz, mgr))
            .add(RFC,  new Rfc(clazz, mgr))
            .add(CBO,  new Cbo(clazz, mgr))
            .add(DIT,  new Dit(clazz, mgr))
            .add(LCOM, new Lcom(clazz, mgr));
    }

    public MetricsMgr getMetricsMgr() {
        return metrics;
    }
    
    public CKMetrics build() {
        return new CKMetrics(label, 
            metrics.get(WMC), 
            metrics.get(NOC), 
            metrics.get(RFC), 
            metrics.get(CBO), 
            metrics.get(DIT), 
            metrics.get(LCOM)
        );
    }
}

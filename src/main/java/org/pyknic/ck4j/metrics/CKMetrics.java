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

import java.util.Arrays;
import java.util.List;

/**
 * A lightweight representation of the CK metrics of a particular class.
 * This class is an immutable.
 * 
 * @author Emil Forslund
 */
public final class CKMetrics {
    private final String label;
    private final int wmc, noc, rfc, cbo, dit, lcom;
    
    /**
     * Constructs this immutable representation of the CK metrics.
     * @param label The class that was calculated.
     * @param wmc Weighted Methods per Class
     * @param noc Number of Children
     * @param rfc Response For a Class
     * @param cbo Coupling Between Objects
     * @param dit Depth of Inheritance Tree
     * @param lcom Lack of Cohesion Of Methods
     */
    CKMetrics(String label, int wmc, int noc, int rfc, int cbo, int dit, int lcom) {
        this.label = label;
        this.wmc   = wmc;
        this.noc   = noc;
        this.rfc   = rfc;
        this.cbo   = cbo;
        this.dit   = dit;
        this.lcom  = lcom;
    }

    /**
     * Returns the label describing the class that was evaluated.
     * @return the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the Weighted Methods per Class metric.
     * @return WMC
     */
    public int getWmc() {
        return wmc;
    }

    /**
     * Returns the Number of Children metric. 
     * @return NOC
     */
    public int getNoc() {
        return noc;
    }

    /**
     * Returns the Response For a Class metric.
     * @return RFC
     */
    public int getRfc() {
        return rfc;
    }

    /**
     * Returns the Coupling Between Objects metric.
     * @return CBO
     */
    public int getCbo() {
        return cbo;
    }

    /**
     * Returns the Depth of Inheritance Tree metric.
     * @return DIT
     */
    public int getDit() {
        return dit;
    }

    /**
     * Returns the Lack of Cohesion Of Methods metric.
     * @return LCOM
     */
    public int getLcom() {
        return lcom;
    }
    
    /**
     * Returns a list with all metrics in the following order:
     *      wmc, noc, rfc, cbo, dit, lcom.
     * 
     * @return a list of all metrics.
     */
    public List<Integer> getAll() {
        return Arrays.asList(wmc, noc, rfc, cbo, dit, lcom);
    }

    /**
     * Transformes this object into a string.
     * @return 
     */
    @Override
    public String toString() {
        return new StringBuilder("(")
            .append("WMC: ").append(wmc).append(", ")
            .append("NOC: ").append(noc).append(", ")
            .append("RFC: ").append(rfc).append(", ")
            .append("CBO: ").append(cbo).append(", ")
            .append("DIT: ").append(dit).append(", ")
            .append("LCOM: ").append(lcom)
        .append(")").toString();
    }
}
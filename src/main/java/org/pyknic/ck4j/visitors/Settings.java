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

/**
 *
 * @author Emil Forslund
 */
public enum Settings {
    COUPLE_TO_JAVA_SDK, 
    COUPLE_TO_INTERFACES,
    COUNT_OWN_METHODS_IN_RFC;
    
    private boolean value = true;
    
    public void set(boolean value) {
        this.value = value;
    }
    
    public boolean isSet() {
        return value;
    }
    
    public static boolean isJavaSDK(String name) {
        return (name.startsWith("java.") ||
                name.startsWith("javax.") ||
                name.startsWith("org.omg.") ||
                name.startsWith("org.w3c.dom.") ||
                name.startsWith("org.xml.sax."));
    }
}

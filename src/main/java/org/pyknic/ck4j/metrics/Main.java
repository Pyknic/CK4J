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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Emil Forslund
 */
public class Main {
    public static void main(String... params) {
        System.out.println("Starting CK4J.");
        if (params.length > 0) {
            final CKMetricsBuilderMgr mgr = new CKMetricsBuilderMgr();
            
            System.out.println("Attempting to load '" + params[0] + "'.");
            try {
                loadFromDirectory(mgr, new File(params[0]).toPath());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "The path specified could not be parsed.", ex);
            }
            
            System.out.println("The following classes was evaluated:");
            mgr.stream().forEach(e -> {
                System.out.println("    " + e.getKey() + " : " + e.getValue());
            });
        } else {
            System.err.println("Missing parameter! You need to specifiy a .class file or a folder that can be traversed to find .class files.");
        }
        System.out.println("Closing down.");
    }
    
    public static void loadFromDirectory(CKMetricsBuilderMgr mgr, Path dir) throws IOException {
        mgr.load(Files.walk(dir)
            .filter(f -> f.toFile().isFile())
            .map(f -> f.toString())
            .filter(s -> s.endsWith(".class"))
        ).collect(Collectors.toList())
            .forEach(v -> v.visit());
    }
}
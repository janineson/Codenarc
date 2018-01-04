/*
 * Copyright 2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.convention

import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.rule.AstVisitor
import org.codenarc.rule.ClassReferenceAstVisitor

/**
 * Check for direct use of Vector or java.util.Vector.
 *
 * Known limitation: Does not catch references as Anonymous Inner class: def x = new java.util.Vector() { .. }
 *
 * @author Chris Mair
 */
class VectorIsObsoleteRule extends AbstractAstVisitorRule {
    String name = 'VectorIsObsolete'
    int priority = 2

    @Override
    AstVisitor getAstVisitor() {
        new ClassReferenceAstVisitor('Vector, java.util.Vector', 'The {0} class is obsolete. Use classes from the Java Collections Framework instead, including ArrayList or Collections.synchronizedList().')
    }
}

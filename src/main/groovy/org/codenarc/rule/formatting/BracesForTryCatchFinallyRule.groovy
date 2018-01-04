/*
 * Copyright 2011 the original author or authors.
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
package org.codenarc.rule.formatting

import org.codehaus.groovy.ast.stmt.TryCatchStatement
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Checks the location of the opening brace ({) for try statements. By default, requires them on the line, but the
 * sameLine property can be set to false to override this.
 *
 * @author <a href="mailto:geli.crick@osoco.es">Geli Crick</a>
 * @author Hamlet D'Arcy
  */
class BracesForTryCatchFinallyRule extends AbstractAstVisitorRule {
    String name = 'BracesForTryCatchFinally'
    int priority = 2
    Class astVisitorClass = BracesForTryCatchFinallyAstVisitor
    boolean sameLine = true
}

class BracesForTryCatchFinallyAstVisitor extends AbstractAstVisitor {

    //TODO Check catch and finally statements
    @Override
    void visitTryCatchFinally(TryCatchStatement node) {
        if (rule.sameLine) {
            if(!sourceLine(node)?.contains('{')) {
                addViolation(node, 'Braces should start on the same line')
            }
        } else {
            if(sourceLine(node)?.contains('{')) {
                addViolation(node, 'Braces should start on a new line')
            }
        }
        super.visitTryCatchFinally(node)
    }
}

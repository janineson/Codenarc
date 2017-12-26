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
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for UseConsistentReturnValueRule
 *
 * @author Janine Son
 */
class UseConsistentReturnValueRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'UseConsistentReturnValue'
    }

    void testSuccessScenario() {
        final SOURCE = '''
            def getSomeResult(input) {
                if (input == "1")   
                    return true //ok
                return false //ok
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testViolation() {
        final SOURCE = '''
            def getSomeResult(input) {
                if (input == "1")   
                    return true //ok
                if (input == "2") 
                    return false //ok
                return "hello" //BAD
            }
        '''
        assertSingleViolation(SOURCE, 7, 'hello', "Use consistent return values.")
    }

    void testSingleViolation() {
        final SOURCE = '''
            def getSomeResult(input) {
                if (input == "1")   
                    return true //ok
                if (input == "2") 
                    return false //ok
                return false //ok
            }
            def getAnotherResult(input) {
                if (input == "1")   
                    return 2.56 //ok
                if (input == "2") 
                    return false //BAD
                return 5.0 //ok
            }  
        '''
        assertSingleViolation(SOURCE, 13, 'return false', "Use consistent return values.")
    }


    protected Rule createRule() {
        new UseConsistentReturnValueRule()
    }
}
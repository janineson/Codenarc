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
 * Tests for NoGlobalVariableRule
 *
 * @author Janine Son
 */
class NoGlobalVariableRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'NoGlobalVariable'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	def getMyConstant() {
                return "some constant value"
            }
        '''
        assertNoViolations(SOURCE)
    }
    void testAnotherSuccessScenario() {
        final SOURCE = '''
        	def getVal() {
                def myval = 'some constant value\'
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testNewSuccessScenario() {
        final SOURCE = '''
        	 preferences {
                page(name: "selectButton")
                for (def i=1; i<=8; i++) {
                    page(name: "configureButton$i")
                }
            }
        '''
        assertNoViolations(SOURCE)
    }

    void test2SuccessScenario() {
        final SOURCE = '''
        	def getVal(def myval) {
                 myval = 'some constant value\'
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testViolation() {
        final SOURCE = '''
            def MY_CONSTANT = 'some constant value'
            def MY_CONSTANT2 = themotion
        '''

        assertTwoViolations(SOURCE,
            2, "def MY_CONSTANT = 'some constant value'", 'In SmartThings, defining global constant variables will not work. Use a getter method instead.',
            3, 'def MY_CONSTANT2 = themotion', 'In SmartThings, defining global constant variables will not work. Use a getter method instead.')

    }


    protected Rule createRule() {
        new NoGlobalVariableRule()
    }
}
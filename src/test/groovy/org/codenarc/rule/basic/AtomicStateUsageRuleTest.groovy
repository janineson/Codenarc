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
 * Tests for AtomicStateUsageRule
 *
 * @author Janine Son
 */
class AtomicStateUsageRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 1
        assert rule.name == 'AtomicStateUsage'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	state.key_one = "val one"
          state.key_two = "val two"
        '''
        assertNoViolations(SOURCE)
    }
    void testAnotherSuccessScenario() {
        final SOURCE = '''
        	atomicState.myMap = [key1: "val1"]
        	atomicState.hi = "hi"
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            atomicState.myMap = [key1: "val1"]
            state.key_one = "val one"
        '''
        assertSingleViolation(SOURCE, 3, 'state.key_one = "val one"',
            "Avoid using atomicState and state in the same SmartApp.")
    }


    protected Rule createRule() {
        new AtomicStateUsageRule()
    }
}
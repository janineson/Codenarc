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
 * Tests for AtomicStateUpdateUsageRule
 *
 * @author Janine Son
 */
class AtomicStateUpdateUsageRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'AtomicStateUpdateUsage'
    }

    void testSuccessScenario() {
        final SOURCE = '''
            def initialize() {
                atomicState.myMap = [key1: "val1"]
                log.debug "atomicState: $atomicState"
            
                // assign collection to local variable and update
                def temp = atomicState.myMap
                // update existing entry
                temp.key1 = "UPDATED"
                // add new entry
                temp.key2 = "val2"
            
                // assign collection back to atomicState
                atomicState.myMap = temp
                log.debug "atomicState: $atomicState"
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
              def someEventHandler(evt) {
                atomicState.someEvent = [name: evt.name, value: evt.value, id: evt.id]
                atomicState.someEvent = [name: updated, value: evt.value, id: evt.id]
            }
        '''
        assertSingleViolation(SOURCE, 4, 'atomicState.someEvent = [name: updated, value: evt.value, id: evt.id]',
            "Modifying collections in Atomic State does not work as it does with State. Read documentation for reference.")
    }

    protected Rule createRule() {
        new AtomicStateUpdateUsageRule()
    }
}
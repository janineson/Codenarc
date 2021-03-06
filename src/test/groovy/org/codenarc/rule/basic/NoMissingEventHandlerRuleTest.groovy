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
 * Tests for NoMissingEventHandlerRule
 *
 * @author Janine Son
 */
class NoMissingEventHandlerRuleTest extends AbstractRuleTestCase {


    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'NoMissingEventHandler'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	def initialize() {
                subscribe(themotion, 'motion.active', motionDetectedHandler)
            }
            
            def motionDetectedHandler(evt) {
                log.debug "motionDetectedHandler called: $evt"
            }
                    '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            def initialize() {
                subscribe(themotion, 'motion.active', motionDetectedHandler)
            }

        '''
        assertSingleViolation(SOURCE, 3, "subscribe(themotion, 'motion.active', motionDetectedHandler)", "Declare an event handler when subscribing to an event.")
    }



    protected Rule createRule() {
        new NoMissingEventHandlerRule()
    }
}
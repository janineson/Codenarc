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
 * Tests for SingleArgEventHandlerRule
 *
 * @author Janine Son
 */
class SingleArgEventHandlerRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'SingleArgEventHandler'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	  def initialize() {
                subscribe(themotion, "motion.active", motionDetectedHandler)
            }
            
            def motionDetectedHandler(evt) {
                theswitch.on()
             
                log.debug MY_CONSTANT.currentState("motion")
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            def initialize() {
                subscribe(themotion, "motion.active", motionDetectedHandler)
            }
            
            def motionDetectedHandler(evt, hi) {
                theswitch.on()
             
                log.debug MY_CONSTANT.currentState("motion")
            }
        '''
        assertSingleViolation(SOURCE, 6, "def motionDetectedHandler(evt, hi) {", "Event handler methods must have a single argument, which contains the event object.")
    }

    void testAnotherSingleViolation() {
        final SOURCE = '''
            def initialize() {
                subscribe(themotion, "motion.active", motionDetectedHandler)
            }
            
            def motionDetectedHandler() {
                theswitch.on()
             
                log.debug MY_CONSTANT.currentState("motion")
            }
        '''
        assertSingleViolation(SOURCE, 6, "def motionDetectedHandler() {", "Event handler methods must have a single argument, which contains the event object.")
    }

    protected Rule createRule() {
        new SingleArgEventHandlerRule()
    }
}
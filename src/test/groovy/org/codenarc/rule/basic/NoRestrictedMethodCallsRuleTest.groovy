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
 * Tests for NoRestrictedMethodCallsRule
 *
 * @author Janine Son
 */
class NoRestrictedMethodCallsRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'NoRestrictedMethodCalls'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	def motionDetectedHandler(evt) {
                log.debug "motionDetectedHandler called: $evt"
                theswitch.on()
                someMethod()
            }
        '''
        assertNoViolations(SOURCE)
    }
    void test2SuccessScenario() {
        final SOURCE = '''
            def jawboneHandler(evt) {
                log.debug "In Jawbone Event Handler, Event Name = ${evt.name}, Value = ${evt.value}"
                
                location.helloHome.execute(settings.wakePhrase)
             }
        
        '''
        assertNoViolations(SOURCE)
    }
    void testSingleViolation() {
        final SOURCE = '''
            def motionDetectedHandler(evt) {
                log.debug "motionDetectedHandler called: $evt"
                theswitch.on()
                someMethod()
                print()
            }

        '''
        assertSingleViolation(SOURCE, 6, "print()", "SmartThings restricted methods calls are not allowed.")
    }
    void test2SingleViolation() {
        final SOURCE = '''
            def jawboneHandler(evt) {
                log.debug "In Jawbone Event Handler, Event Name = ${evt.name}, Value = ${evt.value}"
                execute()
             }
        
        '''
        assertSingleViolation(SOURCE, 4, "execute()", "SmartThings restricted methods calls are not allowed.")
    }
    protected Rule createRule() {
        new NoRestrictedMethodCallsRule()
    }
}
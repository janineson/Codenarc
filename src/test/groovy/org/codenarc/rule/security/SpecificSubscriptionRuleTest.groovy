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
package org.codenarc.rule.security

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for SpecificSubscriptionRule
 *
 * @author Janine Son
 */
class SpecificSubscriptionRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'SpecificSubscription'
    }

    void testSuccessScenario() {
        final SOURCE = '''
            def install() {
                subscribe(theSwitch, "switch.on", switchOnHandler)
            }
            
            def switchOnHandler(evt) {
                log.debug "switch turned on!"
            }

        '''
        assertNoViolations(SOURCE)
    }


    void testSingleViolation() {
        final SOURCE = '''
            subscribe(theSwitch, "switch", switchHandler)
            def switchHandler(evt) {
                if (evt.value == "on") {
                    log.debug "switch turned on!"
                }
            }
        '''
        assertSingleViolation(SOURCE, 2, 'subscribe(theSwitch, "switch", switchHandler)',
            "Subscription must be specific to the Event you are interested in.")
    }


    protected Rule createRule() {
        new SpecificSubscriptionRule()
    }
}
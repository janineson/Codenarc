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

    void testSuccess2Scenario() {
        final SOURCE = '''
            preferences {
                section("Choose thermostat... ") {
                input "thermostat", "capability.thermostat"
                }
                section("Heat setting...") {
                input "heatingSetpoint", "number", title: "Degrees?"
                }
                section("Air conditioning setting..."){
                input "coolingSetpoint", "number", title: "Degrees?"
                }
                }
                
                def installed()
                {
                subscribe(thermostat, "heatingSetpoint", heatingSetpointHandler)
                subscribe(thermostat, "coolingSetpoint", coolingSetpointHandler)
                subscribe(location, changedLocationMode)
                subscribe(app, appTouch)
            }

        '''
        assertNoViolations(SOURCE)
    }
    void testSuccess3Scenario() {
        final SOURCE = '''
           def initialize() {
                subscribe(meter, "power", meterHandler)
            }

        '''
        assertNoViolations(SOURCE)
    }
    void testSuccess4Scenario() {
        final SOURCE = '''
           def initialize() {
                subscribe(buttonDevice, "button", buttonEvent)
            }

        '''
        assertNoViolations(SOURCE)
    }
    void testSuccess5Scenario() {
        final SOURCE = '''
           def initialize() {
                subscribe(location, "sunrise", setSunrise)
                subscribe(location, "sunset", setSunset)
            }

        '''
        assertNoViolations(SOURCE)
    }
    void testSingleViolation() {
        final SOURCE = '''
            preferences {
                section("Choose ... ") {
                    input "theSwitch", "capability.switch"
                 }
             }
            subscribe(theSwitch, "switch", switchHandler)
            def switchHandler(evt) {
                if (evt.value == "on") {
                    log.debug "switch turned on!"
                }
            }
        '''
        assertSingleViolation(SOURCE, 7, 'subscribe(theSwitch, "switch", switchHandler)',
            "Subscription must be specific to the Event you are interested in.")
    }


    protected Rule createRule() {
        new SpecificSubscriptionRule()
    }
}
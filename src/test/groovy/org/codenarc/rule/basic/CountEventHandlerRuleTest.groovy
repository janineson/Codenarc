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
 * Tests for CountEventHandlerRule
 *
 * @author Janine Son
 */
class CountEventHandlerRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'CountEventHandler'
    }


    void testSuccessScenario() {
        final SOURCE = '''
            def scheduledTimeHandler() {
                eventHandler(null)
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
                } else if (evt.value == "off") {
                    log.debug "switch turned off!"
                }
            }
        '''
        assertSingleViolation(SOURCE, 2, 'subscribe(theSwitch, "switch", switchHandler)')
    }
    void testSingleViolation2() {
        final SOURCE = '''
            subscribe(theSwitch, "switch", switchHandler)
            subscribe(theSwitch2, "switch", switchHandler)
            def switchHandler(evt) {
                if (evt.value == "on") {
                    log.debug "switch turned on!"
                } else if (evt.value == "off") {
                    log.debug "switch turned off!"
                }
            }
        '''
        assertSingleViolation(SOURCE, 2, 'subscribe(theSwitch, "switch", switchHandler)')
    }

    void testSingleViolation3() {
        final SOURCE = '''
            subscribe(plantlinksensors, "moisture_status", moistureHandler)
            def moistureHandler(event){
                def expected_plant_name = "SmartThings - ${event.displayName}"
                def device_serial = getDeviceSerialFromEvent(event)
                
                if (!atomicState.attached_sensors.containsKey(device_serial)){
                    dock_sensor(device_serial, expected_plant_name)
                }
            }
        '''
        assertSingleViolation(SOURCE, 2, 'subscribe(plantlinksensors, "moisture_status", moistureHandler)')
    }

    void testSingleViolation4() {
        final SOURCE = '''
            def initialize() {
                subscribe(contacts, "contact", "checkThings");
            }
            def checkThings(evt) {
                def outsideTemp = settings.outTemp.currentTemperature
            }
        '''
        assertSingleViolation(SOURCE, 3, 'subscribe(contacts, "contact", "checkThings");')
    }
    protected Rule createRule() {
        new CountEventHandlerRule()
    }
}
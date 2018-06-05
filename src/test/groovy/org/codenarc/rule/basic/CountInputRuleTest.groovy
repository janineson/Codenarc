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
 * Tests for CountInputRule
 *
 * @author Janine Son
 */
class CountInputRuleTest extends AbstractRuleTestCase {


    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'CountInput'
    }

    void testSuccessScenario() {
        final SOURCE = ''' 
        section("Select which Tesla to connect"){
        }'''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
          section("When there is no motion on any of these sensors") {
             input "motionSensors", "capability.motionSensor", title: "Where?", multiple: true
           }

        '''
        assertSingleViolation(SOURCE, 3, 'input "motionSensors", "capability.motionSensor", title: "Where?", multiple: true')
    }

    void testSingleViolation2() {
        final SOURCE = '''
          if (contacts)
            input(name: 'toggles', title: 'Select water delay switches', type: 'capability.switch', multiple: true, required: false, submitOnChange: true)
        '''
        assertSingleViolation(SOURCE, 3, "input(name: 'toggles', title: 'Select water delay switches', type: 'capability.switch', multiple: true, required: false, ")
    }

    void testSingleViolation3() {
        final SOURCE = '''
          section {
                input(name: "sensor", type: "capability.$sensorType", title: "If the $sensorType device")
            }
        '''
        assertSingleViolation(SOURCE, 3, "input(name: \"sensor\", type: \"capability.\$sensorType\", title: \"If the \$sensorType device\")")
    }
    void testSingleViolation4() {
        final SOURCE = '''
          section("When there is no motion on any of these sensors") {
             input "motionSensors", "capability.$sensorType", title: "Where?", multiple: true
           }
        '''
        assertSingleViolation(SOURCE, 3, "input \"motionSensors\", \"capability.\$sensorType\", title: \"Where?\", multiple: true")
    }

    void testSingleViolation5() {
        final SOURCE = '''
          section("Allow a web application to control these things...") {
                input name: "switches", type: "capability.switch", title: "Which Switches?", multiple: true, required: false
            }
        '''
        assertSingleViolation(SOURCE, 3, "input name: \"switches\", type: \"capability.switch\", title: \"Which Switches?\", multiple: true, required: false")
    }
    void testSingleViolation6() {
        final SOURCE = '''
          section {
			input(
				name: "linkedSmartBlock",
				type: "capability.switch",
				title: "Linked SmartBlock",
				required: true,
				multiple: false
			)
		}
        '''
        assertSingleViolation(SOURCE, 3, "input(")
    }

    void testSingleViolation7() {
        final SOURCE = '''
          def inputLightsA = [
                name:       "A_switches",
                type:       "capability.switch",
                title:      "Control the following switches...",
                multiple:   true,
                required:   false
            ]
        '''
        assertSingleViolation(SOURCE, 2, "def inputLightsA = [")
    }

    void testSingleViolation8() {
        final SOURCE = '''
          section("Set the lighting mood when..."){
				ifSet "motion", "capability.motionSensor", title: "Motion Here", required: false, multiple: true
			}
        '''
        assertSingleViolation(SOURCE, 3, "ifSet \"motion\", \"capability.motionSensor\", title: \"Motion Here\", required: false, multiple: true")
    }


    void testSingleViolation9() {
        final SOURCE = '''
             section("Monitor the humidity of:") {
                input "humiditySensor1", "capability.relativeHumidityMeasurement"
            }
            
        '''
        assertSingleViolation(SOURCE, 3, "input \"humiditySensor1\", \"capability.relativeHumidityMeasurement\"")
    }


    void testSingleViolation10() {
        final SOURCE = '''
          section {
			input(
				name: "switchUpdatesBlock",
				type: "bool",
				title: "Update this SmartBlock when the switch below changes state",
				description: "",
				defaultValue: "false"
			)
		}
        '''
        assertSingleViolation(SOURCE, 3, "input(")
    }

    void testSingleViolation11() {
        final SOURCE = '''
                input(
                    type: "boolean", 
                    name: "notification", 
                    title: state.languageString."${atomicState.language}".title1,
                    required: false,
                    default: true,
                    multiple: false
                )
        '''
        assertSingleViolation(SOURCE, 2, "input(")
    }

    protected Rule createRule() {
        new CountInputRule()
    }
}
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
 * Tests for CountSubscriptionRule
 *
 * @author Janine Son
 */
class CountSubscriptionRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'CountSubscription'
    }

    void testSuccessScenario() {
        final SOURCE = '''
            def updated() {
                unsubscribe()
                saveState()
            }
         '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            def createSubscriptions()
            {
                subscribe(motionSensors, "motion.active", motionActiveHandler)
            }
        '''
        assertSingleViolation(SOURCE, 4, 'subscribe(motionSensors, "motion.active", motionActiveHandler)')
    }

    void testSingleViolation2() {
        final SOURCE = '''
            def createSubscriptions()
            {
                subscribe(motionSensors, "motion.active", motionActiveHandler)
                subscribe(motionSensors, "motion.active", motionActiveHandler)
            }
        '''
        assertSingleViolation(SOURCE, 4, 'subscribe(motionSensors, "motion.active", motionActiveHandler)')
    }

    void testSingleViolation3() {
        final SOURCE = '''
            def installed() {
                subscribe(presence, "presence", presenceHandler)
            }
        
            def updated() {
                unsubscribe()
                subscribe(presence, "presence", presenceHandler)
            }
        '''
        assertSingleViolation(SOURCE, 3, 'subscribe(presence, "presence", presenceHandler)')
    }

    void testViolation() {
        final SOURCE = '''
            def createSubscriptions()
            {
                subscribe(motionSensors, "motion.active", motionActiveHandler)
                subscribe(motionSensors, "motion.inactive", motionInactiveHandler)
            }
        '''
        assertTwoViolations SOURCE,
            4, "subscribe(motionSensors, \"motion.active\", motionActiveHandler)",
            5, "subscribe(motionSensors, \"motion.inactive\", motionInactiveHandler)"
    }



    void testViolation2() {
        final SOURCE = '''
            def initialize() {
                if (blockUpdatesSwitch)
                {
                    subscribe(linkedSmartBlock, "level", updateSwitchLevel)
                }
            
                if (switchUpdatesBlock)
                {
                    subscribe(linkedSwitch, "level", updateBlockLevel)
                }
            
            }
        '''
        assertTwoViolations SOURCE,
            5, "subscribe(linkedSmartBlock, \"level\", updateSwitchLevel)",
            10, "subscribe(linkedSwitch, \"level\", updateBlockLevel)"
    }

    void testViolation3() {
        final SOURCE = '''
            if(D_motion) {
                subscribe(settings.D_motion, "motion", onEventD)
            }
            
            if(D_acceleration) {
                subscribe(settings.D_acceleration, "acceleration", onEventD)
            }

        '''
        assertTwoViolations SOURCE,
            3, "subscribe(settings.D_motion, \"motion\", onEventD)",
            7, "subscribe(settings.D_acceleration, \"acceleration\", onEventD)"
    }

    void testViolation4() {
        final SOURCE = '''
            def createSubscriptions()
            {
                subscribe(location, modeChangeHandler)
                subscribe(location, "position", positionChange)
            }
        '''
        assertTwoViolations SOURCE,
            4, "subscribe(location, modeChangeHandler)",
            5, "subscribe(location, \"position\", positionChange)"
    }

    void testSingleViolation5() {
        final SOURCE = '''
            def createSubscriptions()
            {
                subscribe(settings.switches, 'switch.off', cycleOff)
            }
            def createSubscriptions2()
            {
                subscribe(settings.switches, 'switch.off', cycleOff)
            }
        '''
        assertSingleViolation(SOURCE, 4, 'subscribe(settings.switches, \'switch.off\', cycleOff)')
    }

    protected Rule createRule() {
        new CountSubscriptionRule()
    }
}
/*
* Copyright 2008 the original author or authors.
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
package org.codenarc.results

import org.codenarc.rule.StubRule
import org.codenarc.rule.Violation
import org.codenarc.test.AbstractTestCase

/**
 * Tests for DirectoryResults 
 *
 * @author Chris Mair
 */
class DirectoryResultsTest extends AbstractTestCase {

    static final PATH = '/src/main'
    static final VIOLATION1 = new Violation(rule:new StubRule(1))
    static final VIOLATION2 = new Violation(rule:new StubRule(2))
    static final VIOLATION3 = new Violation(rule:new StubRule(3))
    static final VIOLATION4 = new Violation(rule:new StubRule(4))
    static final VIOLATION7 = new Violation(rule:new StubRule(7))

    void testNoChildren() {
        def results = new DirectoryResults(PATH)
        assert results.path == PATH
        assert results.children == []
        assert results.violations == []

        assert results.getNumberOfViolationsWithPriority(1) == 0
        assert results.getNumberOfViolationsWithPriority(2) == 0
        assert results.getNumberOfViolationsWithPriority(3) == 0

        assert results.totalNumberOfFiles == 0
        assert results.numberOfFilesWithViolations == 0
        assert results.getNumberOfFilesWithViolations(false) == 0
        assert !results.isFile()
    }

    void testWithOneChild() {
        def results = new DirectoryResults(PATH)
        assert results.path == PATH
        def fileResults = new FileResults('path', [VIOLATION1, VIOLATION3, VIOLATION3, VIOLATION1, VIOLATION2])
        results.addChild(fileResults)
        results.numberOfFilesInThisDirectory = 7
        assert results.children == [fileResults]
        assert results.violations.findAll { v -> v.rule.priority == 1 } == [VIOLATION1, VIOLATION1]
        assert results.violations.findAll { v -> v.rule.priority == 2 } == [VIOLATION2]
        assert results.violations.findAll { v -> v.rule.priority == 3 } == [VIOLATION3, VIOLATION3]

        assert results.getNumberOfViolationsWithPriority(1) == 2
        assert results.getNumberOfViolationsWithPriority(2) == 1
        assert results.getNumberOfViolationsWithPriority(3, false) == 2

        assert results.numberOfFilesWithViolations == 1
        assert results.getNumberOfFilesWithViolations(false) == 1

        assert results.getTotalNumberOfFiles() == 7
        assert results.getTotalNumberOfFiles(false) == 7
    }

    void testWithMultipleChildren() {
        def results = new DirectoryResults(PATH)
        assert results.path == PATH
        results.numberOfFilesInThisDirectory = 3
        def fileResults1 = new FileResults('path', [VIOLATION1, VIOLATION3, VIOLATION3, VIOLATION7, VIOLATION1, VIOLATION2])
        results.addChild(fileResults1)
        def subDirResults = new DirectoryResults('subdir')
        subDirResults.numberOfFilesInThisDirectory = 2
        def fileResults2 = new FileResults('path', [VIOLATION2, VIOLATION3, VIOLATION4])
        subDirResults.addChild(fileResults2)
        results.addChild(subDirResults)
        assert results.children == [fileResults1, subDirResults]
        assert results.getViolations().sort { v -> v.rule.priority } == [VIOLATION1, VIOLATION1, VIOLATION2, VIOLATION2, VIOLATION3, VIOLATION3, VIOLATION3, VIOLATION4, VIOLATION7]

        assert results.violations.findAll { v -> v.rule.priority == 1 } == [VIOLATION1, VIOLATION1]
        assert results.violations.findAll { v -> v.rule.priority == 2 } == [VIOLATION2, VIOLATION2]
        assert results.violations.findAll { v -> v.rule.priority == 3 } == [VIOLATION3, VIOLATION3, VIOLATION3]
        assert results.violations.findAll { v -> v.rule.priority == 4 } == [VIOLATION4]
        assert results.violations.findAll { v -> v.rule.priority == 7 } == [VIOLATION7]

        assert results.getNumberOfViolationsWithPriority(1) == 2
        assert results.getNumberOfViolationsWithPriority(2) == 2
        assert results.getNumberOfViolationsWithPriority(3) == 3
        assert results.getNumberOfViolationsWithPriority(4) == 1
        assert results.getNumberOfViolationsWithPriority(7) == 1

        assert results.getNumberOfViolationsWithPriority(1, false) == 2
        assert results.getNumberOfViolationsWithPriority(2, false) == 1
        assert results.getNumberOfViolationsWithPriority(3, false) == 2

        assert results.numberOfFilesWithViolations == 2
        assert results.getNumberOfFilesWithViolations(false) == 1

        assert results.getTotalNumberOfFiles() == 5
        assert results.getTotalNumberOfFiles(false) == 3
    }

    void testFindResultsForPath() {
        def results = new DirectoryResults(PATH)
        def fileResults1 = new FileResults('file1', [])
        def subDirResults = new DirectoryResults('subdir')
        def fileResults2 = new FileResults('file2', [])
        subDirResults.addChild(fileResults2)
        results.addChild(fileResults1)
        results.addChild(subDirResults)

        assert results.findResultsForPath(null) == null
        assert results.findResultsForPath('xx/yy') == null
        assert results.findResultsForPath(PATH) == results
        assert results.findResultsForPath('file1') == fileResults1
        assert results.findResultsForPath('subdir') == subDirResults
        assert results.findResultsForPath('file2') == fileResults2
    }

    void testGetViolations_ReturnsDefensiveCopy() {
        def results = new DirectoryResults(PATH)
        def fileResults = new FileResults('path', [VIOLATION1, VIOLATION3])
        results.addChild(fileResults)
        results.getViolations() << VIOLATION7
        assert results.getViolations() == [VIOLATION1, VIOLATION3]
    }

}
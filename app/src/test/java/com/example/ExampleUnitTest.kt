package com.example

import com.example.data.model.ShsTrack
import com.example.data.model.SubjectCategory
import com.example.data.repository.WassceDataBank
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testWassceSubjectsCoverage() {
        val subjects = WassceDataBank.subjects
        assertTrue("Subjects bank must not be empty", subjects.isNotEmpty())

        val coreSubjects = subjects.filter { it.category == SubjectCategory.CORE }
        assertEquals("Must have 4 Core subjects", 4, coreSubjects.size)

        val coreMath = subjects.find { it.id == "core_math" }
        assertNotNull("Core Math must exist", coreMath)
        assertTrue("Core Math must be present in all tracks", coreMath!!.tracks.contains(ShsTrack.ALL_ROUND))

        val elecMath = subjects.find { it.id == "elec_math" }
        assertNotNull("Elective Math must exist", elecMath)
        assertTrue("Elective Math must belong to General Science", elecMath!!.tracks.contains(ShsTrack.GENERAL_SCIENCE))
    }

    @Test
    fun testPastQuestionsIntegrity() {
        val questions = WassceDataBank.pastQuestions
        assertTrue("Past questions bank must not be empty", questions.isNotEmpty())
        for (q in questions) {
            assertEquals("Question must have exactly 4 options (A, B, C, D)", 4, q.options.size)
            assertTrue("Option index must be 0..3", q.correctOptionIndex in 0..3)
            assertTrue("Question must have an explanation", q.explanation.isNotBlank())
            assertTrue("Question must have an examiner tip", q.examinerTip.isNotBlank())
            assertTrue("Year must be valid WASSCE year", q.year in 2015..2025)
        }
    }

    @Test
    fun testBundledTextbooksForGemini() {
        val textbooks = WassceDataBank.bundledTextbooks
        assertTrue("Bundled textbooks must not be empty", textbooks.isNotEmpty())
        for (tb in textbooks) {
            assertTrue("Textbook excerpt must be rich for Gemini", tb.sampleChapterExcerpt.length > 100)
            assertTrue("Sample chapter must be named", tb.sampleChapterTitle.isNotBlank())
        }
    }

    @Test
    fun testStudyBuddiesAndRooms() {
        val buddies = WassceDataBank.studyBuddies
        assertTrue("Study buddies must not be empty", buddies.isNotEmpty())
        val rooms = WassceDataBank.studyRooms
        assertTrue("Study rooms must not be empty", rooms.isNotEmpty())
    }
}

package com.example.presentationtestproject

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun `activity is created`() {
        scenario.onActivity { activity ->
            assertNotNull(activity)
        }
    }

    @Test
    fun `activity is resumed`() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun `progress is not visible`() {
        scenario.onActivity { activity ->
            val progressbar = activity.findViewById<ProgressBar>(R.id.progressBar)
            assertEquals(View.GONE, progressbar.visibility)
        }
    }

    @Test
    fun `search editText text test`() {
        scenario.onActivity { activity ->
            val editText = activity.findViewById<EditText>(R.id.searchEditText)
            editText.setText("text", TextView.BufferType.EDITABLE)
            assertNotNull(editText.text)
            assertEquals("text", editText.text.toString())
        }
    }

    @Test
    fun `search editText text not changed after search test`() {
        scenario.onActivity { activity ->
            val editText = activity.findViewById<EditText>(R.id.searchEditText)
            editText.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            assertEquals("", editText.text.toString())
        }
    }
}

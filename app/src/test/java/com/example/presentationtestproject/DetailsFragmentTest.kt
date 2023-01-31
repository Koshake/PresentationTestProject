package com.example.presentationtestproject

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import com.example.presentationtestproject.details.DetailsFragment
import com.example.presentationtestproject.details.DetailsFragment.Companion.TOTAL_COUNT_EXTRA
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailsFragmentTest {

    private companion object {
        const val COUNT = 10
    }

    private lateinit var context: Context
    private lateinit var scenario: FragmentScenario<DetailsFragment>

    @Before
    fun setup() {
        scenario = FragmentScenario.launchInContainer(DetailsFragment::class.java, bundleOf(TOTAL_COUNT_EXTRA  to COUNT))
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `fragment is created`() {
        scenario.onFragment { fragment ->
            assertNotNull(fragment)
        }
    }

    @Test
    fun `test fragment bundle`() {
        scenario.moveToState(Lifecycle.State.RESUMED)
        val text = String.format("Number of results: %d", COUNT)
        scenario.onFragment { fragment ->
            val textView = fragment.view?.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals(text, textView?.text.toString())
        }
    }

    @Test
    fun `fragment buttons are visible`() {
        scenario.onFragment { fragment ->
            val decrementButton = fragment.view?.findViewById<Button>(R.id.decrementButton)
            TestCase.assertEquals(View.VISIBLE, decrementButton?.visibility)

            val incrementButton = fragment.view?.findViewById<Button>(R.id.incrementButton)
            TestCase.assertEquals(View.VISIBLE, incrementButton?.visibility)
        }
    }

    @After
    fun close() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }
}

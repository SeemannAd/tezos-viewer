package de.showcase.tezos_viewer.domains.shared.models

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import de.showcase.tezos_viewer.tester.Tester
import org.junit.Test

class BlocksModelTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val json = Tester.loadJsonFromAssets(context, "blocks.json")

    @Test
    @Tester.TestDescription(
        GIVEN = "A list of blocks as json",
        WHEN = "The json is loaded form assets",
        THEN = "the loaded json is not empty"
    )
    fun load_from_assets(){
        assert(json.isNotEmpty())
    }
}
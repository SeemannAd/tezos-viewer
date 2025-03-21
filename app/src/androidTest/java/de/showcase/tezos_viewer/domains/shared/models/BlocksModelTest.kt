package de.showcase.tezos_viewer.domains.shared.models

import Block
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import de.showcase.tezos_viewer.domains.shared.extensions.isNotNullOrEmpty
import de.showcase.tezos_viewer.tester.Tester
import kotlinx.serialization.json.Json
import org.junit.Test

class BlocksModelTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val json = Tester.loadJsonFromAssets(context, "blocks.json")
    private val blocks = Json.decodeFromString<List<Block>>(json)

    @Test
    @Tester.TestDescription(
        GIVEN = "A list of blocks as json",
        WHEN = "The json is loaded form assets",
        THEN = "the loaded json is not empty"
    )
    fun load_from_assets(){
        assert(json.isNotEmpty())
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    @Tester.TestDescription(
        GIVEN = "A list of blocks",
        WHEN = "The json is loaded form assets and deserialized",
        THEN = "the result is a list of [Block]"
    )
    fun deserialization_assert_type(){
        assert(blocks is List<Block>)
    }

    @Test
    @Tester.TestDescription(
        GIVEN = "A list of blocks",
        WHEN = "The json is loaded form assets and deserialized",
        THEN = "the list of [Block] is not empty"
    )
    fun deserialization_assert_size(){
        assert(blocks.isNotEmpty())
    }

    @Test
    @Tester.TestDescription(
        GIVEN = "A list of blocks",
        WHEN = "The json is loaded form assets and deserialized",
        THEN = "the result content is not empty"
    )
    fun deserialization_assert_content(){
        blocks.forEach { block ->
            assert(block.hash.isNotNullOrEmpty())
            assert(block.cycle != null)
            assert(block.priority != null)
            assert(block.transactions != null)
            assert(block.transactions!!.isNotEmpty())
        }
    }

    @Test
    @Tester.TestDescription(
        GIVEN = "A list of blocks",
        WHEN = "The json is loaded form assets and deserialized",
        THEN = "the result-content of transactions is not null or empty"
    )
    fun deserialization_assert_content_transactions(){
        blocks.forEach { block ->
            block.transactions!!.forEach { transaction ->
                assert(transaction.id != null)
                assert(transaction.block.isNotNullOrEmpty())
                assert(transaction.level != null)
                assert(transaction.amount != null)
                assert(transaction.status.isNotNullOrEmpty())
            }
        }
    }
}


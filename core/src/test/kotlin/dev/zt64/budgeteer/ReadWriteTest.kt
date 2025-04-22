package dev.zt64.budgeteer

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.zt64.budgeteer.domain.dao.TransactionDao
import dev.zt64.budgeteer.domain.database.AppDatabase
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Clock

class ReadWriteTest {
    private lateinit var transactionDao: TransactionDao
    private lateinit var db: AppDatabase

    @BeforeTest
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder<AppDatabase>()
            .setDriver(BundledSQLiteDriver())
            .build()
        transactionDao = db.transactionDao()
    }

    @AfterTest
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertTransaction() {
        val transaction = TransactionEntity(
            title = "Test Transaction",
            amount = 100.0,
            date = Clock.System.now(),
            description = "This is a test transaction",
            categoryId = null
        )

        runBlocking {
            transactionDao.insert(transaction)
        }
    }
}
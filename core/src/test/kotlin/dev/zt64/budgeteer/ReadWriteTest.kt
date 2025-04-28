package dev.zt64.budgeteer

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.zt64.budgeteer.domain.dao.TransactionDao
import dev.zt64.budgeteer.domain.database.AppDatabase
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.*
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

            assertNotNull(transactionDao.getAllTransactions(), "Transaction list should not be null")
        }
    }

    @Test
    fun testDeleteTransaction() {
        val transaction = TransactionEntity(
            title = "Test Transaction",
            amount = 100.0,
            date = Clock.System.now(),
            description = "This is a test transaction",
            categoryId = null
        )

        runBlocking {
            transactionDao.insert(transaction)
            transactionDao.delete(transaction)

            assertNull(
                transactionDao.getTransactionById(transaction.transactionId),
                "Transaction should be null after deletion"
            )
        }
    }

    @Test
    fun testUpdateTransaction() {
        val transaction = TransactionEntity(
            title = "Test Transaction",
            amount = 100.0,
            date = Clock.System.now(),
            description = "This is a test transaction",
            categoryId = null
        )

        runBlocking {
            val id = transactionDao.insert(transaction).toInt()
            transactionDao.update(transaction.copy(transactionId = id, title = "Updated Transaction"))

            val updatedTransaction = transactionDao.getTransactionById(id)

            assertNotNull(updatedTransaction, "Updated transaction should not be null")
            assertEquals(
                "Updated Transaction",
                updatedTransaction.transaction.title,
                "Updated transaction title should be 'Updated Transaction'"
            )
        }
    }

    @Test
    fun testGetAllTransactions() {
        val transaction1 = TransactionEntity(
            title = "Test Transaction 1",
            amount = 100.0,
            date = Clock.System.now(),
            description = "This is a test transaction",
            categoryId = null
        )
        val transaction2 = TransactionEntity(
            title = "Test Transaction 2",
            amount = 200.0,
            date = Clock.System.now(),
            description = "This is another test transaction",
            categoryId = null
        )

        runBlocking {
            transactionDao.insert(transaction1)
            transactionDao.insert(transaction2)

            val transactions = transactionDao.getAllTransactions().toList().flatten()

            assertNotNull(transactions, "Transaction list should not be null")
            assertEquals(2, transactions.size, "Transaction list size should be 2")
        }
    }
}
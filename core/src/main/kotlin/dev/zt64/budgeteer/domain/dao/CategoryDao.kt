package dev.zt64.budgeteer.domain.dao

import androidx.room.*
import dev.zt64.budgeteer.domain.entity.CategoryEntity
import dev.zt64.budgeteer.domain.entity.CategoryWithTransactions
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CategoryDao {
    @Insert
    suspend fun insert(category: CategoryEntity)

    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT * FROM CategoryEntity")
    fun getAllCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>>

    @Query("SELECT * FROM CategoryEntity WHERE name = :name")
    suspend fun getCategory(name: String): CategoryEntity?

    @Query("SELECT * FROM CategoryEntity WHERE categoryId = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?
}
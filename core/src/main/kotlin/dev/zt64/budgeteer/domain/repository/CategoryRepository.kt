package dev.zt64.budgeteer.domain.repository

import dev.zt64.budgeteer.domain.dao.CategoryDao
import dev.zt64.budgeteer.domain.entity.CategoryEntity
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.domain.toDatabaseEntity
import dev.zt64.budgeteer.domain.toDomainModel
import kotlinx.coroutines.flow.map

class CategoryRepository internal constructor(private val categoryDao: CategoryDao) {
    fun getCategories() = categoryDao.getAllCategories().map {
        it.map(CategoryEntity::toDomainModel)
    }

    suspend fun getCategory(name: String) = categoryDao.getCategoryById(name)?.toDomainModel()

    suspend fun addCategory(category: Category) = categoryDao.insert(category.toDatabaseEntity())

    suspend fun updateCategory(category: Category) = categoryDao.update(category.toDatabaseEntity())

    suspend fun deleteCategory(category: Category) = categoryDao.delete(category.toDatabaseEntity())
}
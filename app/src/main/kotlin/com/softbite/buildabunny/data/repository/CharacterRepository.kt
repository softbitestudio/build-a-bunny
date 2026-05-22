package com.softbite.buildabunny.data.repository

import com.softbite.buildabunny.data.model.CharacterConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CharacterRepository {

    private val _characters = MutableStateFlow<List<CharacterConfig>>(emptyList())
    val characters: Flow<List<CharacterConfig>> = _characters.asStateFlow()

    fun save(config: CharacterConfig) {
        _characters.update { current ->
            val existing = current.indexOfFirst { it.id == config.id }
            if (existing >= 0) {
                current.toMutableList().also { it[existing] = config }
            } else {
                current + config
            }
        }
    }

    fun delete(id: String) {
        _characters.update { current -> current.filter { it.id != id } }
    }

    fun find(id: String): CharacterConfig? =
        _characters.value.firstOrNull { it.id == id }
}

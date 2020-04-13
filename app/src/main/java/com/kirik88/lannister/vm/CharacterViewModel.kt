package com.kirik88.lannister.vm

import androidx.lifecycle.*
import com.kirik88.lannister.db.FullCharacter
import com.kirik88.lannister.di.DI
import com.kirik88.lannister.repository.CharactersRepository
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {

    private val repository = DI.get<CharactersRepository>()

    private val characterId = MutableLiveData<Long>()

    val character : LiveData<FullCharacter?> = Transformations.switchMap(characterId) {
        id -> repository.getCharacter(id)
    }

    val isLoaded : LiveData<Boolean> = Transformations.map(character) {
        it != null
    }

    val fetchFather: LiveData<UseCase<Unit>>
        get() = _fetchFather
    private val _fetchFather = LiveUseCase<Unit>()

    val fetchMother: LiveData<UseCase<Unit>>
        get() = _fetchMother
    private val _fetchMother = LiveUseCase<Unit>()

    val fetchSpouse: LiveData<UseCase<Unit>>
        get() = _fetchSpouse
    private val _fetchSpouse = LiveUseCase<Unit>()

    val refresher: LiveData<UseCase<Unit>>
        get() = _refresher
    private val _refresher = LiveUseCase<Unit>()

    val openCharacter: LiveData<UseCase<Long>>
        get() = _openCharacter
    private val _openCharacter = LiveUseCase<Long>()

    private val _characterObserver = Observer<FullCharacter?> { character ->
        character?.let {
            if (character.character.father != null && character.fatherName == null)
                fetchAnotherCharacter(character.character.father, _fetchFather)

            if (character.character.mother != null && character.motherName == null)
                fetchAnotherCharacter(character.character.mother, _fetchMother)

            if (character.character.spouse != null && character.spouseName == null)
                fetchAnotherCharacter(character.character.spouse, _fetchSpouse)
        }
    }

    init {
        character.observeForever(_characterObserver)
    }

    override fun onCleared() {
        super.onCleared()

        character.removeObserver(_characterObserver)
    }

    fun initCharacter(id: Long) {
        characterId.value = id

        viewModelScope.launch {
            _refresher.execute {
                repository.refreshCharacter(id)
            }
        }
    }

    fun onOpenFather() {
        character.value?.let {
            if (it.character.father != null)
                openCharacter(it.character.father)
        }
    }

    fun onOpenMother() {
        character.value?.let {
            if (it.character.mother != null)
                openCharacter(it.character.mother)
        }
    }

    fun onOpenSpouse() {
        character.value?.let {
            if (it.character.spouse != null)
                openCharacter(it.character.spouse)
        }
    }

    private fun openCharacter(id: Long) {
        _openCharacter.execute { id }
    }

    private fun fetchAnotherCharacter(id: Long, useCase: LiveUseCase<Unit>) {
        viewModelScope.launch {
            useCase.execute {
                repository.refreshCharacter(id)
            }
        }
    }
}
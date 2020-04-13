package com.kirik88.lannister.vm

import androidx.lifecycle.*
import com.kirik88.lannister.di.DI
import com.kirik88.lannister.model.db.Character
import com.kirik88.lannister.repository.CharactersRepository
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {

    private val repository = DI.get<CharactersRepository>()

    private val pageNumber = Transformations.map(repository.getLastCharactersPage() ) { it }

    val characters : LiveData<List<Character>> = Transformations.switchMap(pageNumber) {
        page -> repository.getCharacters(page)
    }

    val isLoaded : LiveData<Boolean> = Transformations.map(characters) {
        (it != null && it.isNotEmpty())
    }

    val refresher: LiveData<UseCase<Unit>>
        get() = _refresher
    private val _refresher = LiveUseCase<Unit>()

    val openCharacter: LiveData<UseCase<Long>>
        get() = _openCharacter
    private val _openCharacter = LiveUseCase<Long>()

    private val _firstLoadObserver = Observer<List<Character>> {
        if (it != null && it.isEmpty())
            loadPage(1)
    }

    init {
        characters.observeForever(_firstLoadObserver)
    }

    override fun onCleared() {
        super.onCleared()

        characters.removeObserver(_firstLoadObserver)
    }

    fun onNextPage() {
        pageNumber.value?.let { page ->
            loadPage(page.plus(1))
        }
    }

    fun onOpenCharacter(id: Long) {
        _openCharacter.execute { id }
    }

    private fun loadPage(page: Long) {
        viewModelScope.launch {
            _refresher.execute {
                repository.refreshCharacters(page)
            }
        }
    }
}
package com.example.reselover.note.presentance.screen.homeScreen.stateEvent

import com.example.reselover.note.data.NoteEntity

sealed class HomeEvent {

    data class Title(val title : String) : HomeEvent()

    data class Content(val content : String) : HomeEvent()

    data class GetNote(val id: String?): HomeEvent()
    data class DeleteNote(val noteEntity: NoteEntity?): HomeEvent()

    data object SaveBtn : HomeEvent()
    data object Refresh : HomeEvent()

}
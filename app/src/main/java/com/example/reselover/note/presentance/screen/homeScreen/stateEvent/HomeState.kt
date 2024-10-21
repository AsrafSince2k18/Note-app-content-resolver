package com.example.reselover.note.presentance.screen.homeScreen.stateEvent

import com.example.reselover.note.data.NoteEntity


data class HomeState(

    val title:String="",

    val content : String="",

    var noteEntity: NoteEntity?=null,

    val id : String?=null,

    val listNote : List<NoteEntity> = emptyList()

)

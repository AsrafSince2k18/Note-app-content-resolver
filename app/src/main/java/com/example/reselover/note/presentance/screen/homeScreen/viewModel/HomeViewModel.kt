package com.example.reselover.note.presentance.screen.homeScreen.viewModel

import android.app.Application
import android.content.ContentValues
import android.database.ContentObserver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reselover.note.data.NoteEntity
import com.example.reselover.note.presentance.screen.homeScreen.stateEvent.HomeEvent
import com.example.reselover.note.presentance.screen.homeScreen.stateEvent.HomeState
import com.example.reselover.note.presentance.utils.Utils.CONTENT_URI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {




    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        getAllNote()
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Title -> {
                _homeState.update {
                    it.copy(title = event.title)
                }
            }

            is HomeEvent.Content -> {
                _homeState.update {
                    it.copy(content = event.content)
                }
            }

            is HomeEvent.GetNote -> {
                fetchNote(id=event.id?.toInt()!!)
            }

            is HomeEvent.DeleteNote -> {
                deleteNote(id=event.noteEntity?.id!!)
            }

            HomeEvent.SaveBtn -> {
                insertNote()
            }

            HomeEvent.Refresh -> {
                getAllNote()
            }
        }
    }



    private fun getAllNote(){
        val noteItem = mutableListOf<NoteEntity>()

        viewModelScope.launch {
            val cursor = application.contentResolver?.query(CONTENT_URI,null,null,null,null)
            cursor?.let {
                while(it.moveToNext()){
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val title = it.getString(it.getColumnIndexOrThrow("title"))
                    val content = it.getString(it.getColumnIndexOrThrow("content"))
                    noteItem.add(
                        NoteEntity(
                            id=id,
                            title=title,
                            content=content
                        )
                    )
                    _homeState.update {state->
                        state.copy(listNote = noteItem)
                    }

                }
            }
            cursor?.close()
        }
    }

    private fun insertNote() {
        viewModelScope.launch {
            val contentResolver = application.contentResolver
            if (homeState.value.noteEntity!=null){
                Log.d("t1", "insertNote: ${homeState.value.noteEntity}")
                Log.d("t1", "insertNote: ${homeState.value.id}")
                val noteId = homeState.value.id
                val updateUri = Uri.parse("$CONTENT_URI/$noteId")
                val contentValue = ContentValues().apply {
                    put("id",noteId)
                    put("title", homeState.value.title)
                    put("content", homeState.value.content)
                }
                contentResolver.update(updateUri, contentValue,null,null)

                _homeState.update {
                    it.copy(title = "",
                        content = "",
                        noteEntity = null)
                }

            }else {
                val contentValue = ContentValues().apply {
                    put("title", homeState.value.title)
                    put("content", homeState.value.content)
                }
                contentResolver.insert(CONTENT_URI, contentValue)
                _homeState.update {
                    it.copy(title = "",
                        content = "",
                        noteEntity = null)
                }
            }

        }
    }

    private fun fetchNote(id:Int){
        viewModelScope.launch {
            val projection = arrayOf("id","title","content")
            val cursor = application.contentResolver.query(CONTENT_URI,projection,"id = ?",
                arrayOf(id.toString()),null
            )
            cursor?.let {
                when(it.moveToFirst()){
                    true -> {
                        val noteId = it.getInt(it.getColumnIndexOrThrow("id"))
                        val title = it.getString(it.getColumnIndexOrThrow("title"))
                        val content = it.getString(it.getColumnIndexOrThrow("content"))
                        _homeState.update { state->
                            state.copy(
                                title = title,
                                content = content,
                                noteEntity = NoteEntity(noteId, title, content),
                                id = noteId.toString()
                            )
                        }

                    }
                    false -> {
                        Log.d("t1", "fetchNote False")

                    }
                }
            }

        }
    }

    private fun deleteNote(id:Int){
        viewModelScope.launch {
            val uri = Uri.parse("$CONTENT_URI/$id")
            val contentResolver = application.contentResolver
            contentResolver.delete(uri,null,null)
        }
    }

     fun observeTheData()= callbackFlow<List<NoteEntity>> {
        val observe = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                val note = homeState.value.listNote
                trySend(note)
            }
        }
        application.contentResolver.registerContentObserver(CONTENT_URI,false,observe)
        awaitClose {
            application.contentResolver.unregisterContentObserver(observe)
        }


    }


}
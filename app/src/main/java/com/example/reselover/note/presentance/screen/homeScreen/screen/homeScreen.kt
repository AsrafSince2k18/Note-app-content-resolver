package com.example.reselover.note.presentance.screen.homeScreen.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reselover.note.presentance.component.HomeTopBar
import com.example.reselover.note.presentance.component.NoteComponent
import com.example.reselover.note.presentance.screen.homeScreen.stateEvent.HomeEvent
import com.example.reselover.note.presentance.screen.homeScreen.stateEvent.HomeState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeState: HomeState,
    homeEvent: (HomeEvent) -> Unit
) {

    val modelBottomSheet = rememberModalBottomSheetState()
    val focusManage = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HomeTopBar(onRefresh = { homeEvent(HomeEvent.Refresh) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    modelBottomSheet.expand()
                }
            }) {

                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (modelBottomSheet.isVisible) {
                ModalBottomSheet(
                    onDismissRequest = {
                        coroutineScope.launch {
                            modelBottomSheet.hide()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 5.dp,
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Text(
                            text = "Add note",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )

                        OutlinedTextField(
                            value = homeState.title,
                            onValueChange = {
                                homeEvent(HomeEvent.Title(it))
                            },
                            placeholder = {
                                Text(
                                    text = "Title",
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.32f)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManage.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Spacer(
                            modifier = Modifier
                                .height(4.dp)
                        )

                        OutlinedTextField(
                            value = homeState.content,
                            onValueChange = {
                                homeEvent(HomeEvent.Content(it))
                            },
                            placeholder = {
                                Text(
                                    text = "Content",
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.32f)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManage.clearFocus(true) }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                        )

                        Button(
                            onClick = { homeEvent(HomeEvent.SaveBtn) },
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 3.dp,
                                pressedElevation = 6.dp
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp)
                        ) {
                            Text(
                                text = if (homeState.noteEntity != null) "Update" else "Save",
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }
            }

            if (homeState.listNote.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add note",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(homeState.listNote) { note ->
                        NoteComponent(
                            noteEntity = note,
                            onClick = { noteE ->
                                coroutineScope.launch {
                                    modelBottomSheet.expand()
                                }
                                homeEvent(HomeEvent.GetNote(noteE.id.toString()))
                            },
                            onDelete = {
                                homeEvent(HomeEvent.DeleteNote(noteEntity = it))
                            }
                        )
                    }
                }
            }
        }
    }
}
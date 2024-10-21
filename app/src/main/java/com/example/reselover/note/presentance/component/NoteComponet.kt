package com.example.reselover.note.presentance.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reselover.note.data.NoteEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteComponent(
    noteEntity: NoteEntity,
    onClick: (NoteEntity) -> Unit,
    onDelete: (NoteEntity) -> Unit
) {

    Card(onClick = {
        onClick(noteEntity)
    },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 5.dp
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp
            )
    ) {
        Row (
            modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = noteEntity.title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp)

                Spacer(modifier = Modifier
                    .height(6.dp))

                Text(text = noteEntity.content,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 12.sp)
            }
            IconButton(onClick = { onDelete(noteEntity) }) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = null)
            }
        }

    }

}
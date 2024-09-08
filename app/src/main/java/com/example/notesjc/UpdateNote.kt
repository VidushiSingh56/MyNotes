package com.example.notesjc


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesjc.ui.theme.NOTESJCTheme
import com.example.notesjc.ui.theme.Purple40

class UpdateNote : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NOTESJCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Purple40,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Update Notes",
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                    )
                                })
                        },
                        content = { paddingValues ->
                            // Retrieve the intent data
                            val noteId = intent.getIntExtra("NoteId", -1)  // Use the note ID
                            val title = intent.getStringExtra("Title") ?: ""
                            val subject = intent.getStringExtra("Subject") ?: ""
                            val description = intent.getStringExtra("Description") ?: ""

                            Log.d("UpdateNote", "Received noteId: $noteId, title: $title, subject: $subject, description: $description")

                            if (noteId == -1) {
                                // Handle invalid noteId
                                Toast.makeText(this, "Invalid Note ID", Toast.LENGTH_SHORT).show()
                                finish()  // Close activity
                            } else {
                                Column(modifier = Modifier.padding(paddingValues)) {
                                    updateDataToDatabase(
                                        context = LocalContext.current,
                                        noteId = noteId,
                                        tName = title,
                                        sUbject = subject,
                                        dIscription = description
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun updateDataToDatabase(
    context: Context,
    noteId: Int, // Accept noteId to update the specific note
    tName: String,
    sUbject: String,
    dIscription: String
) {
    var title by remember { mutableStateOf(TextFieldValue(tName)) }
    var subject by remember { mutableStateOf(TextFieldValue(sUbject)) }
    var description by remember { mutableStateOf(TextFieldValue(dIscription)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 19.dp)
    ) {
        val dbHandler: DBHandler = DBHandler(context)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text(text = "Enter note title") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            placeholder = { Text(text = "Enter note subject") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text(text = "Enter note description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                // Check for empty fields
                if (title.text.isNotEmpty() && subject.text.isNotEmpty() && description.text.isNotEmpty()) {
                    // Update the note using its ID
                    val isUpdated = dbHandler.updateNote(
                        noteId = noteId,
                        newTitle = title.text,
                        newSubject = subject.text,
                        newDescription = description.text
                    )
                    if (isUpdated) {
                        Toast.makeText(context, "Note Updated", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Failed to Update Note", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Update", color = Color.White)
            }

            Button(onClick = {
                // View Notes action
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("View Notes", color = Color.White)
            }
        }
    }
}


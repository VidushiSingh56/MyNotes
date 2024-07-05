package com.example.notesjc



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notesjc.ui.theme.NOTESJCTheme
import com.example.notesjc.ui.theme.Purple40


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NOTESJCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                        // in scaffold we are specifying the top bar.
                        topBar = {

                            // inside top bar we are specifying background color.
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Purple40,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {

                                    // in the top bar we are specifying tile as a text
                                  Text(

                                        // on below line we are specifying
                                        // text to display in top app bar.
                                        text = "Your Notes",

                                        // on below line we are specifying
                                        // modifier to fill max width.
                                        modifier = Modifier.fillMaxWidth()
                                        ,

                                        // on below line we are specifying
                                        // text alignment.
                                        textAlign = TextAlign.Center,

                                        // on below line we are specifying
                                        // color for our text.
                                        color = Color.White,

                                        )
                                })
                        },
                        content = { paddingValues ->
                            Column(modifier = Modifier.padding(paddingValues)) {
                                readDataToDatabase(LocalContext.current, paddingValues)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun readDataToDatabase(context: Context, paddingValues: PaddingValues) {
    val dbHandler = DBHandler(context)
    val notesList = dbHandler.readNotes() ?: arrayListOf()
    val activity = context as Activity

    if (notesList.isNullOrEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "No notes now",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            FloatingActionButton(onClick = {
                val intent = Intent(context, AddNotes::class.java)
                context.startActivity(intent)
            }, containerColor = Purple40) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(notesList) { index, item ->
                        Card(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Title: ${item.title}",
                                        modifier = Modifier.padding(4.dp),
                                        color = Color.Black,
                                        textAlign = TextAlign.Start
                                    )
                                    Row {
                                        IconButton(onClick = {
                                            // Handle edit action
                                            val i = Intent(context, UpdateNote::class.java).apply {
                                                putExtra("Title", item.title)
                                                putExtra("Subject", item.subject)
                                                putExtra("Description", item.description)
                                            }
                                            context.startActivity(i)


                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = {
                                            dbHandler.deleteNote(item.id)
                                            Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(context, MainActivity::class.java)
                                            context.startActivity(intent)
                                            activity.finish()
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "Subject: ${item.subject}",
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "Description: ${item.description}",
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, AddNotes::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),containerColor = Purple40
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}

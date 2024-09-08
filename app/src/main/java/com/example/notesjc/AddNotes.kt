package com.example.notesjc

//import MainActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesjc.ui.theme.Brown
import com.example.notesjc.ui.theme.NOTESJCTheme


class AddNotes : ComponentActivity() {
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
                                    containerColor = Brown,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    //
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Add Notes",
                                        //

                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                    )
                                }
                            )
                        },
                        content = { paddingValues ->
                            Column(modifier = Modifier.padding(paddingValues)) {
                                addToDatabase(LocalContext.current)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun addToDatabase(context: Context) {
    val activity = context as Activity

    val tile = remember { mutableStateOf(TextFieldValue("")) }
    val subject = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 19.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
    ) {
        var dbHandler: DBHandler = DBHandler(context)

//        Text(
//            text = "Add Notes",
//            textAlign= TextAlign.Start,
//            color = Purple40, fontSize = 20.sp, fontWeight = FontWeight.Bold
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = tile.value,
            onValueChange = { tile.value = it },
            placeholder = { Text(text = "Enter note title") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = subject.value,
            onValueChange = { subject.value = it },
            placeholder = { Text(text = "Enter note subject") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            placeholder = { Text(text = "Enter note description")},
            modifier = Modifier.fillMaxWidth().height(200.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            maxLines = 5
        )
        Spacer(modifier = Modifier.height(15.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                dbHandler.addNotes(
                    tile.value.text,
                    subject.value.text,
                    description.value.text)

                val i = Intent(context, MainActivity::class.java)
                context.startActivity(i)

                Toast.makeText(context, "Note Added to Database", Toast.LENGTH_SHORT)
            }){
                Text(text = "Save")
            }

//            Spacer(modifier = Modifier.height(15.dp))

            Button(onClick = { val i = Intent(context, MainActivity::class.java)
                context.startActivity(i)
            },) {
                Text("View Notes", color = Color.White)
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun addToDatabasePreview() {
    val context = LocalContext.current
    NOTESJCTheme {
        addToDatabase(context = context)
    }
}

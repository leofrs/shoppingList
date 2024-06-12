package com.example.shoppinglist

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingItem(
    var id: Int,
    var name: String,
    var quantity: Byte,
    var isEditing: Boolean
)


@Composable
fun ShoppingListApp() {
    var shoppingList by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDialog = true }) {
            Text("Add Item")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(shoppingList) { item ->
                if (item.isEditing) {
                    ShoppingItemEdit(item = item) { name, quantity ->
                        shoppingList = shoppingList.map {
                            if (it.id == item.id) {
                                it.copy(name = name, quantity = quantity, isEditing = false)
                            } else {
                                it
                            }
                        }
                    }
                } else {
                    ItemList(
                        item = item,
                        onDeleteClick = {
                            shoppingList = shoppingList.filter { it.id != item.id }
                        },
                        onEditClick = {
                            shoppingList = shoppingList.map {
                                if (it.id == item.id) {
                                    it.copy(isEditing = true)
                                } else {
                                    it
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = {
                            if (itemName.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = shoppingList.size + 1,
                                    name = itemName,
                                    quantity = itemQuantity.toByte(),
                                    isEditing = false
                                )
                                shoppingList += newItem
                                showDialog = false
                                itemName = ""
                                itemQuantity = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text("Insira seu item e sua quantidade") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        placeholder = { Text("Insira o item") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        singleLine = true,
                        placeholder = { Text("Insira a quantidade") }
                    )
                }
            },
        )
    }
}

@Composable
fun ItemList(item: ShoppingItem, onDeleteClick: () -> Unit, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Blue, shape = RoundedCornerShape(20))
            .background(color = Color.LightGray, shape = RoundedCornerShape(20)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = item.name,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 5.dp)
        )
        Text(
            text = "Quant. ${item.quantity}",
            style = TextStyle(fontSize = 16.sp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Icone para editar um item da lista"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Icone para deletar um item da lista"
                )
            }
        }
    }
}

@Composable
fun ShoppingItemEdit(item: ShoppingItem, onEditItem: (String, Byte) -> Unit) {
    var nameItem by remember { mutableStateOf(item.name) }
    var quantityItem by remember { mutableStateOf(item.quantity.toString()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nameItem,
            onValueChange = { nameItem = it },
            singleLine = true,
            placeholder = { Text("Insira o item") }
        )
        OutlinedTextField(
            value = quantityItem,
            onValueChange = { quantityItem = it },
            singleLine = true,
            placeholder = { Text("Insira a quantidade") }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                onEditItem(nameItem, quantityItem.toByte())
            }) {
                Text(text = "Save")
            }
            Button(onClick = {
                onEditItem(item.name, item.quantity) // Cancel edit, revert to original
            }) {
                Text(text = "Cancel")
            }
        }
    }
}

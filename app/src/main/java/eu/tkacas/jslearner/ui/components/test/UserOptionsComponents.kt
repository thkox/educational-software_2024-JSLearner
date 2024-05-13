package eu.tkacas.jslearner.ui.components.test

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.tkacas.jslearner.ui.theme.PrussianBlue
import eu.tkacas.jslearner.ui.theme.SkyBlue
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun MultipleChoiceSingleCard(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val cardColor = if (isSelected) SkyBlue else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PrussianBlue
                )
            )
            Text(
                text = text,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun MultipleChoiceMultipleCard(
    text: String,
    isSelected: MutableState<Boolean>,
    onSelected: () -> Unit
) {
    val cardColor = if (isSelected.value) SkyBlue else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Checkbox(
                checked = isSelected.value,
                onCheckedChange = { isSelected.value = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = PrussianBlue
                )
            )
            Text(
                text = text,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun MultipleChoiceSingleAnswer(
    question: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(text = question)
        options.forEach { option ->
            MultipleChoiceSingleCard(
                text = option,
                isSelected = option == selectedOption,
                onSelected = { onOptionSelected(option) }
            )
        }
    }
}

@Composable
fun MultipleChoiceMultipleAnswers(
    question: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionSelected: (String, Boolean) -> Unit
) {
    Column {
        Text(text = question)
        options.forEach { option ->
            MultipleChoiceMultipleCard(
                text = option,
                isSelected = remember { mutableStateOf(option in selectedOptions) },
                onSelected = { onOptionSelected(option, option !in selectedOptions) }
            )
        }
    }
}

@Composable
fun TrueFalse(
    question: String,
    isTrue: Boolean?,
    onTrueFalseSelected: (Boolean) -> Unit
) {
    Column {
        Text(text = question)
        Row {
            RadioButton(
                selected = isTrue == true,
                onClick = { onTrueFalseSelected(true) }
            )
            Text(text = "True")
        }
        Row {
            RadioButton(
                selected = isTrue == false,
                onClick = { onTrueFalseSelected(false) }
            )
            Text(text = "False")
        }
    }
}

@Composable
fun FillInTheBlankMatching(
    question: String,
    blanks: List<String>,
    userAnswers: List<String>,
    onBlankFilled: (Int, String) -> Unit
) {
    Column {
        Text(text = question)
        blanks.forEachIndexed { index, blank ->
            TextField(
                value = userAnswers[index],
                onValueChange = { onBlankFilled(index, it) }
            )
        }
    }
}

@Composable
fun SequenceHotspot(
    question: String,
    items: List<String>,
    sequence: List<Int>,
    userSequence: List<Int>,
    onSequenceChanged: (Int, Int) -> Unit
) {
    Column {
        Text(text = question)
        items.forEachIndexed { index, item ->
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }

            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                            val newIndex = ((index + offsetX).roundToInt()).coerceIn(0, items.size - 1)
                            onSequenceChanged(index, newIndex)
                        }
                    }
            ) {
                Text(text = item)
            }
        }
    }
}

@Composable
fun DragTheWords(
    question: String,
    words: List<String>,
    targetWords: List<String>,
    userAnswers: List<String>,
    onWordDropped: (Int, String) -> Unit
) {
    Column {
        Text(text = question)
        words.forEach { word ->
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }

            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume() // Consume the gesture so it doesn't propagate to other views
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                            val newIndex = ((words.indexOf(word) + offsetX).roundToInt()).coerceIn(0, words.size - 1)
                            onWordDropped(newIndex, word)
                        }
                    }
            ) {
                Text(text = word)
            }
        }
        targetWords.forEachIndexed { index, targetWord ->
            Box(
                modifier = Modifier.background(Color.LightGray)
            ) {
                Text(text = userAnswers[index])
            }
        }
    }
}

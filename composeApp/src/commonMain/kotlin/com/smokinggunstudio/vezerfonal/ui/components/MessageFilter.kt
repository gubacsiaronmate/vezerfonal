package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.state.controller.MessageFilterStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.MessageFilterStateModel
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*
import kotlin.time.ExperimentalTime

@Composable
@OptIn(ExperimentalTime::class)
fun MessageFilter(
    snapshot: MessageFilterStateModel,
    tabOpenedClick: Function,
    modifier: Modifier = Modifier,
    scrollLockedBySliderCallback: CallbackFunction<Boolean>
) {
    val state = remember { MessageFilterStateController(snapshot) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = state.senderName,
            onValueChange = state::updateSenderName,
            label = {
                Text(
                    text = stringResource(Res.string.senders_name),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Column {
            Text(
                stringResource(Res.string.time_sent),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            val fullRange = state.earliestMessageUnixTime..state.latestMessageUnixTime
            val selectedRange = state.selectedStartDate.toFloat()..state.selectedEndDate.toFloat()
            val isFullRange = state.selectedStartDate == 0L && state.selectedEndDate == 0L
            MessageTimeRangeSlider(
                initialRange = if (isFullRange) fullRange else selectedRange,
                valueRange = fullRange,
                onRangeSelected = { range ->
                    state.updateSelectedStartDate(range.start.toLong())
                    state.updateSelectedEndDate(range.endInclusive.toLong())
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) { scrollLockedBySliderCallback(it) }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.important),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Switch(
                checked = state.isImportant,
                onCheckedChange = state::updateIsImportant,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.waiting_for_answer),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Switch(
                checked = state.isWaitingForAnswer,
                onCheckedChange = state::updateIsWaitingForAnswer,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = state::updateSearchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            singleLine = true,
            label = {
                Text(
                    text = stringResource(Res.string.search_title_or_in_message_content),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        HorizontallyScrollableTagSelect(
            tagList = state.tagSelectionState.allItems.map { it.name },
            tabOpenedCallback = tabOpenedClick
        ) { _ -> }
    }
}

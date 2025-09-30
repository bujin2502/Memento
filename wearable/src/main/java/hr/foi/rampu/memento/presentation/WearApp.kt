package hr.foi.rampu.memento.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import hr.foi.rampu.memento.models.Task
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import hr.foi.rampu.memento.R
import hr.foi.rampu.memento.presentation.theme.MementoBlue
import hr.foi.rampu.memento.presentation.theme.MementoDarkerBlue
import hr.foi.rampu.memento.presentation.theme.MementoTheme

@Composable
fun WearApp(tasks: List<Task>) {

    val listState = rememberScalingLazyListState()

    MementoTheme {
        Scaffold(
                    positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
                    vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
        ) {
            if (tasks.isEmpty()) {
                CircularProgressIndicator(
                            modifier = Modifier
                                . fillMaxWidth()
                        . fillMaxHeight()
                )
                Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                ) {
                    Text(
                                stringResource(R.string.msg_wait_tasks_syncing),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                    )
                }
            } else {
                ScalingLazyColumn {
                    items(tasks) { task ->
                        TaskCard(task)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task
) {
    Card(onClick = {}) {
        Column {
            Text(
                        task.name, style = MaterialTheme.typography.title3, color = MementoBlue
            )
            Text(
                        task.categoryName, style = MaterialTheme.typography.body2, color = MementoDarkerBlue
            )
        }
    }
}
/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package hr.foi.rampu.memento.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import hr.foi.rampu.memento.models.Task

class MainActivity : ComponentActivity(), DataClient.OnDataChangedListener {

    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val tasks = mutableStateListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp(tasks)
        }
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { dataEvent ->
            if (dataEvent.type == DataEvent.TYPE_CHANGED) {
                DataMapItem.fromDataItem(dataEvent.dataItem).apply {
                    val tasksCount = dataMap.getInt("tasks_count")

                    tasks.clear()
                    for (i in 0 until tasksCount) {
                        tasks.add(
                            Task(
                                dataMap.getInt("task_id_$i"),
                                dataMap.getString("task_name_$i")!!,
                                dataMap.getString("task_category_$i")!!
                            )
                        )
                    }

                    dataClient.deleteDataItems(dataEvent.dataItem.uri)
                }
            }
        }
    }
}
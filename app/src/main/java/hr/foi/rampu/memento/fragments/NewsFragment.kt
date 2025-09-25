package hr.foi.rampu.memento.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible // Added for isVisible property
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rampu.memento.R
import hr.foi.rampu.memento.adapters.NewsAdapter // Assuming this is your adapter
import hr.foi.rampu.memento.ws.WsNews
import hr.foi.rampu.memento.ws.WsNewsResponse // Assuming this is your response data class
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingCircle: ProgressBar
    private lateinit var btnRefresh: FloatingActionButton
    private val ws = WsNews.newsService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false) // Ensure you have this layout file

        loadingCircle = view.findViewById(R.id.tv_news_fragment_loading)
        btnRefresh = view.findViewById(R.id.fab_news_fragment_refresh_news)
        recyclerView = view.findViewById(R.id.rv_news) // Initialize recyclerView here

        btnRefresh.setOnClickListener {
            loadNews()
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadNews()

        return view
    }

    private fun loadNews() {
        changeDisplay(isLoading = true) // Pass a boolean or adjust changeDisplay
        ws.getNews().enqueue(
            object : Callback<WsNewsResponse> {
                override fun onResponse(call: Call<WsNewsResponse>, response: Response<WsNewsResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.results?.let { news ->
                            recyclerView.adapter = NewsAdapter(news)
                        } ?: displayWebServiceErrorMessage("Empty response or news list")
                    } else {
                        displayWebServiceErrorMessage("Error: ${response.code()}")
                    }
                    changeDisplay(isLoading = false)
                }

                override fun onFailure(call: Call<WsNewsResponse>, t: Throwable) {
                    displayWebServiceErrorMessage("Failure: ${t.message}")
                    changeDisplay(isLoading = false)
                }
            }
        )
    }

    private fun changeDisplay(isLoading: Boolean) {
        btnRefresh.isVisible = !isLoading
        recyclerView.isVisible = !isLoading
        loadingCircle.isVisible = isLoading
    }

    private fun displayWebServiceErrorMessage(message: String = "Error loading news") {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun displayWebServiceErrorMessage() {
        Toast.makeText(
                    context,
                    getString(R.string.news_ws_err_msg),
                    Toast.LENGTH_LONG
        ).show()
    }
}
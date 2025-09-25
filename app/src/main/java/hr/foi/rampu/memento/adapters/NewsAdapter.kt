package hr.foi.rampu.memento.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.foi.rampu.memento.R
import hr.foi.rampu.memento.ws.WsNews
import hr.foi.rampu.memento.ws.WsNewsResult

class NewsAdapter(private val newsList: List<WsNewsResult>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tv_news_list_item_title)
        private val text: TextView = view.findViewById(R.id.tv_news_list_item_text)
        private val dateTime: TextView = view.findViewById(R.id.tv_news_list_item_date)
        private val imageView: ImageView = view.findViewById(R.id.iv_news_list_item_image) // Iako se ne koristi u bind metodi, pretpostavljam da će se koristiti

        fun bind(newsItem: WsNewsResult) {
            title.text = newsItem.title
            text.text = newsItem.text
            dateTime.text = newsItem.date
            Picasso.get().load(WsNews.BASE_URL + newsItem.imagePath).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_list_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size
}
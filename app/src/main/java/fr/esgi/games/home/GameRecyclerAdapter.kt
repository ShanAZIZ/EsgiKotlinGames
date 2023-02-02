package fr.esgi.games.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esgi.games.R
import fr.esgi.games.service.GameApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameRecyclerAdapter(val games: ArrayList<Int>, val listener: CustomOnClickListener<Int>): RecyclerView.Adapter<GameRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val layout: ConstraintLayout = itemView.findViewById(R.id.game_card)
        val loading: View = itemView.findViewById(R.id.loading)
        val bgLayout: ImageView = itemView.findViewById(R.id.bg_image)
        val cardImage: ImageView = itemView.findViewById(R.id.card_image)
        val gameTitle: TextView = itemView.findViewById(R.id.game_title)
        val gameEditor: TextView = itemView.findViewById(R.id.game_editor)
        val gamePrice: TextView = itemView.findViewById(R.id.game_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_card, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = games[position]
        holder.layout.visibility = View.INVISIBLE
        holder.loading.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val game = GameApiService.fetchGameDetail(id)
            if(game != null) {
                withContext(Dispatchers.Main) {
                    holder.gameTitle.text = game.title
                    holder.gameEditor.text = game.editor
                    holder.gamePrice.text = game.price
                    Glide.with(holder.cardImage.context).load(game.getSmallImage()).centerCrop().into(holder.cardImage)
                    Glide.with(holder.bgLayout.context).load(game.bgImage).centerCrop().into(holder.bgLayout)
                    holder.itemView.setOnClickListener {
                        listener.onClick(game.appId)
                    }
                    holder.layout.visibility = View.VISIBLE
                    holder.loading.visibility = View.GONE
                }
            }
            else {
                withContext(Dispatchers.Main) {
                    holder.itemView.visibility = View.GONE
                    holder.loading.visibility = View.GONE
                }
                // TODO: manage when null
            }

        }
    }
}
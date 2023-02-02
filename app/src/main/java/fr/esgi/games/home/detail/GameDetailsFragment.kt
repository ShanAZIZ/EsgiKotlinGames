package fr.esgi.games.home.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.esgi.games.R
import fr.esgi.games.service.FirebaseLikesService
import fr.esgi.games.service.GameApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var firebaseLikesService: FirebaseLikesService
    private lateinit var auth: FirebaseAuth
    private var isLiked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        firebaseLikesService = FirebaseLikesService()
        auth = Firebase.auth
        val args: GameDetailsFragmentArgs by navArgs()

        val like = view.findViewById<View>(R.id.toolbar_like)

        isLiked(args.appId, like)

        view.findViewById<View>(R.id.toolbar_cross).setOnClickListener {
            navController.popBackStack()
        }

        like.setOnClickListener {
            if(isLiked) {
                // TODO: dislike
            }
            else {
                // TODO: like
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            val game = GameApiService.fetchGameDetail(args.appId)
            if(game != null) {
                withContext(Dispatchers.Main) {
                    val bgImage = view.findViewById<ImageView>(R.id.game_details_background)
                    val cardBgImage = view.findViewById<ImageView>(R.id.card_bg_image)
                    val cardImage = view.findViewById<ImageView>(R.id.card_image)
                    Glide.with(bgImage.context).load(game.getBgrImage()).centerCrop().into(bgImage) // TODO loader
                    Glide.with(cardBgImage.context).load(game.bgImage).centerCrop().into(cardBgImage) // TODO loader
                    Glide.with(cardImage.context).load(game.getSmallImage()).centerCrop().into(cardImage) // TODO loader
                    view.findViewById<TextView>(R.id.game_title).text= game.title
                    view.findViewById<TextView>(R.id.game_editor).text= game.editor
                }
            }
        }

        // TODO: init is in whishlist and is in liked
        // TODO: add to like / remove
        // TODO: add to whishlist / remove
        // TODO: retrieve game info
        // TODO: description / avis
    }

    private fun isLiked(id: Int, like: View) {
        println(id)
        val uid = auth.currentUser?.uid ?: return
        firebaseLikesService.isInLikes(uid, id) { liked ->
            println(liked)
            if(liked) {
                like.background = context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.like_full) }
                isLiked = true
            }
        }
    }
}
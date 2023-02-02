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
import fr.esgi.games.service.FirebaseWishlistService
import fr.esgi.games.service.GameApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var firebaseLikesService: FirebaseLikesService
    private lateinit var firebaseWishlistService: FirebaseWishlistService
    private lateinit var auth: FirebaseAuth
    private var liked = false
    private var wished = false

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
        firebaseWishlistService = FirebaseWishlistService()
        auth = Firebase.auth
        val args: GameDetailsFragmentArgs by navArgs()

        val like = view.findViewById<View>(R.id.toolbar_like)
        val wishV = view.findViewById<View>(R.id.toolbar_whishlist)

        isLiked(args.appId, like)
        isWished(args.appId, wishV)

        view.findViewById<View>(R.id.toolbar_cross).setOnClickListener {
            navController.popBackStack()
        }

        like.setOnClickListener {
            if(liked) {
                dislike(args.appId, like)
            }
            else {
                like(args.appId, like)

            }
        }

        wishV.setOnClickListener {
            if (wished){
                diswish(args.appId, wishV)
            }
            else {
                wish(args.appId, wishV)
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

        // TODO: add to like / remove
        // TODO: add to whishlist / remove
        // TODO: description / avis
    }

    private fun isLiked(id: Int, like: View) {
        val uid = auth.currentUser?.uid ?: return
        firebaseLikesService.isInLikes(uid, id) { l ->
            if(l) {
                like.background = context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.like_full) }
                liked = true
            }
        }
    }

    private fun isWished(id: Int, wish: View) {
        val uid = auth.currentUser?.uid ?: return
        firebaseWishlistService.isInWished(uid, id) { w ->
            if(w) {
                wish.background = context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.whishlist_full) }
                wished = true
            }
        }
    }

    private fun dislike(id: Int, like: View) {
        val uid = auth.currentUser?.uid ?: return
        firebaseLikesService.removeFromLikes(uid, id) {
            if(it) {
                like.background = context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.like) }
                liked = false
            }
        }
    }

    private fun like(id: Int, like: View) {
        val uid = auth.currentUser?.uid ?: return
        firebaseLikesService.addToLikes(uid, id) {
            if(it) {
                like.background = context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.like_full) }
                liked = true
            }
        }
    }

    private fun diswish(id: Int, wish: View) {
        val uid = auth.currentUser?.uid ?: return
        firebaseWishlistService.removeFromWishlist(uid, id) {
            if(it) {
                wish.background = context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.whishlist) }
                wished = false
            }
        }
    }

    private fun wish(id: Int, wish: View) {
        val uid = auth.currentUser?.uid ?: return
        firebaseWishlistService.addToWishlist(uid, id) {
            if(it) {
                wish.background = context?.let { it1 -> ContextCompat.getDrawable(it1, R.drawable.whishlist_full) }
                wished = true
            }
        }
    }
}

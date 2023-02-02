package fr.esgi.games.home.like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.esgi.games.R
import fr.esgi.games.home.CustomOnClickListener
import fr.esgi.games.home.GameRecyclerAdapter

class LikeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private var gameIds : ArrayList<Int> = ArrayList(730)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        navController = Navigation.findNavController(view)

        val exitButton = view.findViewById<View>(R.id.toolbar_cross)
        val recyclerView =view.findViewById<RecyclerView>(R.id.like_list)

        exitButton.setOnClickListener {
            navController.navigate(R.id.action_likeFragment_to_homeFragment)
        }
        if(gameIds.isEmpty()){
            view.findViewById<View>(R.id.no_likes).visibility = View.VISIBLE
        }
        else{
            recyclerView.visibility = View.VISIBLE
            view.findViewById<View>(R.id.no_likes).visibility = View.GONE
            view.findViewById<View>(R.id.click).visibility = View.GONE
            view.findViewById<View>(R.id.likes_bg_svg).visibility = View.GONE

        }
        val adapter = GameRecyclerAdapter(gameIds, object: CustomOnClickListener<Int> {
            override fun onClick(item: Int) {
                navController.navigate(R.id.action_likeFragment_to_gameDetailsFragment)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


}
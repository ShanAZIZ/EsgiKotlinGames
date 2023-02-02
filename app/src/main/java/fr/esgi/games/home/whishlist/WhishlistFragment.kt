package fr.esgi.games.home.whishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.games.R
import fr.esgi.games.home.CustomOnClickListener
import fr.esgi.games.home.GameRecyclerAdapter
import fr.esgi.games.model.GameDetail

class WhishlistFragment : Fragment() {

    private lateinit var navController: NavController
    private var gameIds = listOf<Int>(730,730)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_whishlist, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val exitButton = view.findViewById<View>(R.id.toolbar_cross)
        val recyclerView =view.findViewById<RecyclerView>(R.id.wishlist)


        exitButton.setOnClickListener {
            navController.navigate(R.id.action_whishlistFragment_to_homeFragment)
        }
        if(gameIds.isEmpty()){
            view.findViewById<View>(R.id.no_wish).visibility = View.VISIBLE
            view.findViewById<View>(R.id.click).visibility = View.VISIBLE
            view.findViewById<View>(R.id.whishlist_bg_svg).visibility = View.VISIBLE        }
        else{
            recyclerView.visibility = View.VISIBLE
            view.findViewById<View>(R.id.no_wish).visibility = View.GONE
            view.findViewById<View>(R.id.click).visibility = View.GONE
            view.findViewById<View>(R.id.whishlist_bg_svg).visibility = View.GONE

        }
        val adapter = GameRecyclerAdapter(gameIds, object: CustomOnClickListener<GameDetail> {
            override fun onClick(item: GameDetail) {
                navController.navigate(R.id.action_whishlistFragment_to_gameDetailsFragment)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
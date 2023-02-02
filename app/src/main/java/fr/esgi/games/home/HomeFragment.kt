package fr.esgi.games.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.games.R
import fr.esgi.games.service.GameApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO retrieve all datas - steam API
// https://store.steampowered.com/appreviews/730?json=1
class HomeFragment : Fragment(){

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        view.findViewById<View>(R.id.toolbar_like).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_likeFragment)
        }
        view.findViewById<View>(R.id.toolbar_whishlist).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_whishlistFragment)
        }
        view.findViewById<EditText>(R.id.search_bar).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_searchFragment)
        }

        val recyclerView =view.findViewById<RecyclerView>(R.id.game_list)

        GlobalScope.launch(Dispatchers.IO) {
            val ids = GameApiService.fetchGameIds()
            if(ids != null) {
                withContext(Dispatchers.Main) {
                    val adapter = GameRecyclerAdapter(
                        ids,
                        object : CustomOnClickListener<Int> {
                            override fun onClick(item: Int) {
                                val action = HomeFragmentDirections.actionHomeFragmentToGameDetailsFragment(item)
                                navController.navigate(action)
                            }
                        },
                    )
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                }
            }
            // TODO: else
        }
    }
}
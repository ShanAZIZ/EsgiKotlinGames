package fr.esgi.games.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class HomeFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        view.findViewById<View>(R.id.toolbar_like).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_likeFragment)
        }
        view.findViewById<View>(R.id.toolbar_whishlist)
        val recyclerView =view.findViewById<RecyclerView>(R.id.game_list)

        GlobalScope.launch(Dispatchers.IO) {
            val ids = GameApiService.fetchGameIds()
            withContext(Dispatchers.Main) {
                val adapter = GameRecyclerAdapter(ids)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(context)
            }
        }
    }
}
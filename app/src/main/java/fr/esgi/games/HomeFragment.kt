package fr.esgi.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
// TODO retrieve all datas - steam API
// https://store.steampowered.com/api/appdetails?appids=730&l=french
// https://api.steampowered.com/ISteamChartsService/GetMostPlayedGames/v1/
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

        val goToLikesButton = view.findViewById<View>(R.id.toolbar_like)

        goToLikesButton.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_likeFragment)
        }
    }
}
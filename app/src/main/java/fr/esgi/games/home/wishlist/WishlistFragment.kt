package fr.esgi.games.home.wishlist

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
import fr.esgi.games.service.FirebaseWishlistService

class WishlistFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var firebaseWishlistService: FirebaseWishlistService
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        navController = Navigation.findNavController(view)
        firebaseWishlistService = FirebaseWishlistService()

        val exitButton = view.findViewById<View>(R.id.toolbar_cross)
        val recyclerView =view.findViewById<RecyclerView>(R.id.wishlist)

        exitButton.setOnClickListener {
            navController.navigate(R.id.action_wishlistFragment_to_homeFragment)
        }


        val uid = auth.uid

        if(uid != null){
            firebaseWishlistService.getWish(uid).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    if(task.result.isEmpty()){
                        view.findViewById<View>(R.id.no_wish).visibility = View.VISIBLE
                    }
                    else{
                        recyclerView.visibility = View.VISIBLE
                        view.findViewById<View>(R.id.no_wish).visibility = View.GONE
                        view.findViewById<View>(R.id.click).visibility = View.GONE
                        view.findViewById<View>(R.id.whishlist_bg_svg).visibility = View.GONE
                        val adapter = GameRecyclerAdapter(task.result, object: CustomOnClickListener<Int> {
                            override fun onClick(item: Int) {
                                val action = WishlistFragmentDirections.actionWishlistFragmentToGameDetailsFragment(item)
                                navController.navigate(action)
                            }
                        })
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }
    }
}
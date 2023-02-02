package fr.esgi.games.home.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.games.R
import fr.esgi.games.home.CustomOnClickListener
import fr.esgi.games.service.GameApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        view.findViewById<View>(R.id.toolbar_cross).setOnClickListener {
            navController.popBackStack()
        }
        val editText = view.findViewById<EditText>(R.id.search_bar)
        val recyclerView =view.findViewById<RecyclerView>(R.id.game_list)
        val loading = view.findViewById<ProgressBar>(R.id.loading)
        val noResult = view.findViewById<TextView>(R.id.no_results)

        editText.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        editText.setOnKeyListener { _, keyCode, event ->
            if((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                recyclerView.visibility = View.GONE
                noResult.visibility = View.GONE
                loading.visibility = View.VISIBLE
                val text = editText.text
                GlobalScope.launch(Dispatchers.IO) {
                    val ids = GameApiService.fetchGameSearch(text.toString())
                    if(ids != null) {
                        withContext(Dispatchers.Main) {
                            if(ids.size == 0) {
                                noResult.visibility = View.VISIBLE
                                loading.visibility = View.GONE
                                recyclerView.visibility = View.GONE
                            }
                            else {
                                val adapter = fr.esgi.games.home.GameRecyclerAdapter(
                                    ids,
                                    object : CustomOnClickListener<Int> {
                                        override fun onClick(item: Int) {
                                            val action =
                                                SearchFragmentDirections.actionSearchFragmentToGameDetailsFragment(
                                                    item
                                                )
                                            navController.navigate(action)
                                        }
                                    },
                                )
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(context)
                                recyclerView.visibility = View.VISIBLE
                                loading.visibility = View.GONE
                                noResult.visibility = View.GONE
                            }
                        }
                    }
                    else {
                        withContext(Dispatchers.Main) {
                            noResult.visibility = View.VISIBLE
                            loading.visibility = View.GONE
                            recyclerView.visibility = View.GONE
                        }
                    }
                }
            }
            false
        }
    }
}
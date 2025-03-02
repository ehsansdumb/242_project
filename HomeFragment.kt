package com.example.myauth4

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myauth4.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentItem = 0
    private val images = listOf(R.drawable.img, R.drawable.img_4, R.drawable.img_5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val featuredProducts = listOf(
            Product("Product 1", 10.99, R.drawable.download),
            Product("Product 2", 12.99, R.drawable.img_2),
            Product("Product 3", 14.99, R.drawable.img)
        )

        val bestSellers = listOf(
            Product("Best Seller 1", 15.99, R.drawable.img_3),
            Product("Best Seller 2", 17.99, R.drawable.img_4),
            Product("Best Seller 3", 19.99, R.drawable.img_5)
        )

        val WorstSellers = listOf(
            Product("Worst Seller 1", 15.99, R.drawable.img_3),
            Product("Worst Seller 2", 17.99, R.drawable.img_4),
            Product("Worst Seller 3", 19.99, R.drawable.img_5)
        )

        val featuredAdapter = ProductAdapter(featuredProducts)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = featuredAdapter

        val bestSellersAdapter = ProductAdapter(bestSellers)
        binding.bestSellersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.bestSellersRecyclerView.adapter = bestSellersAdapter

        val WorstSellersAdapter = ProductAdapter(WorstSellers)
        binding.WorstSellersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.WorstSellersRecyclerView.adapter = WorstSellersAdapter

        val adapter = ViewPagerAdapter(images)
        binding.viewPager.adapter = adapter

        startAutoSwipe()

        return binding.root
    }


    private fun startAutoSwipe() {
        // Runnable that will be executed every 2 seconds to swipe the ViewPager
        val runnable = object : Runnable {
            override fun run() {
                // Swiping to the next item
                currentItem = (currentItem + 1) % images.size
                binding.viewPager.setCurrentItem(currentItem, true)
                handler.postDelayed(this, 2000) // Re-run this every 2 seconds
            }
        }

        handler.postDelayed(runnable, 2000) // Start the auto-swiping after 2 seconds
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacksAndMessages(null)
    }
}

data class Product(val name: String, val price: Double, val imageRes: Int)

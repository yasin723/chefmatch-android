
package com.chefmatch.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private val orange = Color.rgb(255, 105, 55)
    private val recipes = listOf(
        Triple("Chicken Burger", "chicken, tomato, onion", "🍔"),
        Triple("Tomato Soup", "tomato, onion, garlic", "🍅"),
        Triple("Chicken Wrap", "chicken, tomato, lettuce", "🌯"),
        Triple("Vegetable Omelette", "egg, onion, tomato", "🍳"),
        Triple("Simple Pasta", "pasta, tomato, cheese", "🍝")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(255, 245, 235)
        window.navigationBarColor = Color.rgb(255, 245, 235)
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
            android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(255, 249, 242))
        }

        val header = TextView(this).apply {
            text = "🍳  ChefMatch"
            textSize = 25f
            setTextColor(Color.WHITE)
            setTypeface(null, android.graphics.Typeface.BOLD)
            setBackgroundColor(orange)
            setPadding(22, 22, 22, 22)
        }
        root.addView(header)

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 28, 20, 24)
        }

        val title = TextView(this).apply {
            text = "Cook with what you have."
            textSize = 29f
            gravity = Gravity.CENTER
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(Color.rgb(45, 45, 45))
        }
        content.addView(title)

        val subtitle = TextView(this).apply {
            text = "Find delicious recipes using your ingredients."
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(Color.DKGRAY)
            setPadding(0, 12, 0, 24)
        }
        content.addView(subtitle)

        val input = EditText(this).apply {
            hint = "chicken, tomato, rice, onion"
            textSize = 16f
            setSingleLine(true)
            setPadding(14, 12, 14, 12)
            setBackgroundColor(Color.WHITE)
        }
        content.addView(input)

        val searchButton = Button(this).apply {
            text = "Find Recipes"
            setTextColor(Color.WHITE)
            setBackgroundColor(orange)
        }
        content.addView(searchButton)

        val resultTitle = TextView(this).apply {
            text = "Popular Recipes"
            textSize = 23f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(Color.rgb(45, 45, 45))
            setPadding(0, 28, 0, 12)
        }
        content.addView(resultTitle)

        val results = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        content.addView(results)

        fun displayRecipes(ingredients: String) {
            results.removeAllViews()
            val words = ingredients.lowercase()
                .split(",", " ", ";")
                .filter { it.isNotBlank() }

            val matches = recipes.map { recipe ->
                val score = words.count {
                    recipe.second.contains(it, ignoreCase = true)
                }
                recipe to score
            }.filter { it.second > 0 }
                .sortedByDescending { it.second }

            if (matches.isEmpty()) {
                results.addView(TextView(this).apply {
                    text = "Enter ingredients such as chicken, tomato or egg."
                    textSize = 16f
                    setPadding(0, 12, 0, 12)
                })
                return
            }

            matches.forEach { (recipe, score) ->
                val card = TextView(this).apply {
                    text = "${recipe.third}  ${recipe.first}\n\n" +
                        "Ingredients: ${recipe.second}\n" +
                        "Matching ingredients: $score"
                    textSize = 17f
                    setTextColor(Color.rgb(45, 45, 45))
                    setBackgroundColor(Color.WHITE)
                    setPadding(18, 20, 18, 20)
                }

                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 14)
                results.addView(card, params)
            }
        }

        searchButton.setOnClickListener {
            val ingredients = input.text.toString().trim()
            if (ingredients.isEmpty()) {
                input.error = "Enter your ingredients first"
            } else {
                resultTitle.text = "Matching Recipes"
                displayRecipes(ingredients)
            }
        }

        displayRecipes("")
        scroll.addView(content)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }
}

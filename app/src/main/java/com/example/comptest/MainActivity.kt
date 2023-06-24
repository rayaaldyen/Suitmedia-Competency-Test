package com.example.comptest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import com.example.comptest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonCheckEnabling()
        buttonNextEnabling()
        binding.buttonNext.setOnClickListener{
            buttonNextHandler()
        }
        binding.buttonCheck.setOnClickListener {
            buttonCheckHandler()
        }

    }

    private fun buttonNextHandler(){
        val name = binding.inputName.text.toString()
        startActivity(Intent(this@MainActivity, SecondScreenActivity::class.java).apply {
            putExtra(SecondScreenActivity.EXTRA_NAME, name)
        })
    }
    private fun buttonNextEnabling() {
        binding.inputName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonNext.isEnabled = true
            }

            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun buttonCheckEnabling() {
        binding.inputPalindrome.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonCheck.isEnabled = true
            }

            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun buttonCheckHandler(){
        val inputPalindrome = binding.inputPalindrome.text.toString().lowercase()

        if (isPalindrome(inputPalindrome)) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.is_palindrome))
                setPositiveButton(getString(R.string.back)) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.not_palindrome))
                setPositiveButton(getString(R.string.back)) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    private fun isPalindrome(word: String): Boolean {
        val cleanWord = word.replace("\\W".toRegex(), "")
        val reverse = cleanWord.reversed()
        return cleanWord == reverse
    }
}
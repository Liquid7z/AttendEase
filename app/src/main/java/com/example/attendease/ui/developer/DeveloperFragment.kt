package com.example.attendease.ui.developer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.attendease.R
import com.google.android.material.button.MaterialButton

class DeveloperFragment :
    Fragment(R.layout.fragment_developer) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(
            R.id.btnGithub
        ).setOnClickListener {

            openUrl(
                "https://github.com/Liquid7z"
            )
        }

        view.findViewById<MaterialButton>(
            R.id.btnLinkedIn
        ).setOnClickListener {

            openUrl(
                "https://www.linkedin.com/in/iftiar-karim/"
            )
        }

        view.findViewById<MaterialButton>(
            R.id.btnDonate
        ).setOnClickListener {

            openUrl(
                "https://buymeacoffee.com/liquidd"
            )
        }
    }

    private fun openUrl(url: String) {

        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }
}
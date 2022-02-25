package com.example.a99password.presenter.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a99password.R
import com.example.a99password.databinding.CardviewUserPictureBinding

class PictureAdapter(
    private val context : Context,
    private val pictureInterface: PictureInterface
    ) : RecyclerView.Adapter<PictureViewHolder>() {

    private var pictureSelected = -1
    private var positionSelected = -1

    private val allPicturesId = arrayListOf<Int>(
        R.drawable.user_icon_01,
        R.drawable.user_icon_02,
        R.drawable.user_icon_03,
        R.drawable.user_icon_04,
        R.drawable.user_icon_05,
        R.drawable.user_icon_06,
        R.drawable.user_icon_07,
        R.drawable.user_icon_08,
        R.drawable.user_icon_09,
        R.drawable.user_icon_10,
        R.drawable.user_icon_11,
        R.drawable.user_icon_12,
    )

    fun setPicture(pictureID : Int) {
        pictureSelected = pictureID
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(CardviewUserPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val binding = holder.binding
        val picture = allPicturesId[position]

        binding.civUserIcon.setImageDrawable(ContextCompat.getDrawable(context, picture))

        if(pictureSelected == picture) binding.civUserIcon.borderColor = Color.parseColor("#00FF00")
        else binding.civUserIcon.borderColor = Color.parseColor("#555555")

        binding.civUserIcon.setOnClickListener {
            if(positionSelected == -1) notifyDataSetChanged() else notifyItemChanged(positionSelected)

            positionSelected = position
            pictureSelected = picture
            notifyItemChanged(positionSelected)

            pictureInterface.selectPicture(pictureSelected)
        }
    }

    override fun getItemCount(): Int = allPicturesId.size
}

class PictureViewHolder(val binding : CardviewUserPictureBinding) : RecyclerView.ViewHolder(binding.root)


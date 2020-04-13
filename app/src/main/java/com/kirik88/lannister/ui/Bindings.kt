package com.kirik88.lannister.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kirik88.lannister.R
import com.kirik88.lannister.model.db.Character

@BindingAdapter("characterName")
fun TextView.bindCharacterName(character: Character?) {
    character?.let {
        text = when {
            character.name.isNotEmpty() -> character.name
            character.aliases.isNotEmpty() && character.aliases.first().isNotEmpty() -> character.aliases.first()
            character.titles.isNotEmpty() && character.titles.first().isNotEmpty() -> character.titles.first()
            else -> ""
        }
    }
}

@BindingAdapter("list")
fun TextView.bindList(list: List<String>?) {
    text = if (list != null && list.isNotEmpty() && list.first().isNotEmpty())
        list.joinToString("\n")
    else
        "-"
}

@BindingAdapter("possibleBlank")
fun TextView.bindPossibleBlank(txt: String?) {
    text = if (txt != null && txt.isNotEmpty()) txt else "-"
}

@BindingAdapter("linkId", "linkText")
fun TextView.bindLink(linkId: Long?, linkText: String?) {
    if (linkId != null) {
        text = linkText ?: "..."
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_forward_black_24dp, 0, 0, 0)
    } else {
        text = "-"
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }
}
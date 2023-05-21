package com.dicoding.storyapp.util

import com.dicoding.storyapp.data.ListStoryItem

object DataDummy {
    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val story = ListStoryItem(
                "story-_xIGHIfdiNDGFRw_",
                "https://story-api.dicoding.dev/images/stories/photos-1684693703078_VpYZBdHe.jpg",
                "2023-05-21T18:28:23.079Z",
                "christinee",
                "tes",
                107.3853817,
                -6.3457783
            )
            storyList.add(story)
        }
        return storyList
    }
}
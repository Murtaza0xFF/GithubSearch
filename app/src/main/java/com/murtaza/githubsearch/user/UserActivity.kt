package com.murtaza.githubsearch.user

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.murtaza.githubsearch.common.PicassoHelper
import com.murtaza.githubsearch.R
import com.murtaza.githubsearch.user.data.User
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private val picassoHelper: PicassoHelper =
        PicassoHelper()
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        playAnimationFile("hourglass.json")
        retrieveUserDetails(intent.extras!!.getString("username"))
    }

    private fun retrieveUserDetails(username: String?) {
        userViewModel.getUser(username!!)
            .observe(this, Observer {
                if (it.error == null) {
                    val user: User? = it.data
                    stopAnimation()
                    setName(user!!.name)
                    setAvatar(user.avatarUrl)
                    setBlog(user.blog)
                    setPublicRepos(user.publicRepos)
                    setPublicGists(user.publicGists)
                    setLocation(user.location)
                    setFollowers(user.followers)
                    setFollowing(user.following)
                    setBio(user.bio)
                } else {
                    playAnimationFile("error_404.json")
                    setErrorMessage("Error retrieving user: " + it.error)
                }
            })
    }

    private fun setName(name: String) {
        this.name.text = name
    }

    private fun setLocation(location: String?) {
        if (location == null) {
            this.location.visibility = View.GONE
            return
        } else {
            this.location.visibility = View.VISIBLE
        }
        this.location.text = location
    }

    private fun setFollowers(followers: Int?) {
        this.followers.text = String.format("%d followers", followers)
    }

    private fun setFollowing(following: Int?) {
        this.following.text = String.format("%d following", following)
    }

    private fun setBio(bio: String?) {
        if (bio == null) {
            this.description.visibility = View.GONE
            return
        } else {
            this.description.visibility = View.VISIBLE
        }
        this.description.text = bio
    }

    private fun setAvatar(avatarUrl: String) {
        picassoHelper.setImageView(R.drawable.ic_person_outline_black_24dp, avatarUrl, user_avatar)
    }

    private fun setBlog(blog: String?) {
        if (blog == null) {
            this.blog.visibility = View.GONE
            return
        } else {
            this.blog.visibility = View.VISIBLE
        }
        this.blog.text = blog
    }

    private fun setPublicRepos(publicRepos: Int?) {
        this.public_repos.text = String.format("%d Public Repos", publicRepos)
    }

    private fun setPublicGists(publicGists: Int?) {
        this.public_gists.text = String.format("%d Public Gists", publicGists)
    }

    private fun playAnimationFile(fileName: String) {
        animation_view.visibility = View.VISIBLE
        animation_view.setAnimation(fileName)
        animation_view.playAnimation()
    }

    private fun stopAnimation() {
        animation_view.cancelAnimation()
        animation_view.visibility = View.GONE
    }

    private fun setErrorMessage(errorMessage: String) {
        this.error.visibility = View.VISIBLE
        this.error.text = errorMessage
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

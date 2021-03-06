package com.adnd.xyzreader;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adnd.xyzreader.databinding.ArticleDetailsActivityBinding;
import com.adnd.xyzreader.models.Article;
import com.adnd.xyzreader.view_models.ArticleDetailsViewModel;

public class ArticleDetailsActivity extends AppCompatActivity {

    public static final String ARTICLE_ID_EXTRA_KEY = "article_id_extra_key";

    ArticleDetailsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.article_details_activity);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(ARTICLE_ID_EXTRA_KEY)) {
            ArticleDetailsViewModel model =
                    ViewModelProviders.of(this).get(ArticleDetailsViewModel.class);

            int articleId =
                    intentThatStartedThisActivity.getIntExtra(ARTICLE_ID_EXTRA_KEY, 0);
            model.setArticleId(articleId);

            model.getArticleLiveData().observe(this, new Observer<Article>() {
                @Override
                public void onChanged(@Nullable Article article) {
                    if (article != null) {
                        binding.setArticle(article);
                    }
                }
            });

            binding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(ArticleDetailsActivity.this)
                            .setType("text/plain")
                            .setText("Some sample text")
                            .getIntent(), getString(R.string.action_share)));
                }
            });

        } else {
            finish();
        }
    }
}

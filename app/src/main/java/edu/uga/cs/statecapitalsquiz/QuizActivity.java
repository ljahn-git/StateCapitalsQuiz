package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewPager2 pager = findViewById(R.id.viewpager);
        StateQuizPagerAdapter sqpAdapter = new StateQuizPagerAdapter(getSupportFragmentManager(), getLifecycle());

        //Pass info from database here (will need to update StateQuizFragment and QuizResultsFragment to accept more info in newInstance method)

        for (int i = 0; i < 6; i++) {
            StateQuizFragment sqf = StateQuizFragment.newInstance(i);
            sqpAdapter.addFragment(sqf);
        }
        QuizResultsFragment qrf = QuizResultsFragment.newInstance();
        sqpAdapter.addFragment(qrf);

        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setAdapter(sqpAdapter);
    }
}
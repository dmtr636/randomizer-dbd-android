package com.kodimstudio.myapplication.ui.skillcheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.kodimstudio.myapplication.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SkillCheckFragment extends Fragment {

    public static final int ANGLE_INCREMENT_HARD = 2;
    static final float ANGLE_START_ROTATION = -100;
    static final float ARROW_END_ROTATION = 120;

    static final int BG_RANDOM_RANGE = 100;
    static final float BG_START_ROTATION = -50;

    static final long DELAY_BETWEEN_SKILLCHECKS = 1000;
    static final long DELAY_AFTER_START_CLICKED = 500;
    static final long SLEEP_TIME = 6;
    public static final float ANGLE_INCREMENT_EASY = 0.5f;
    public static final int ANGLE_INCREMENT_MEDIUM = 1;

    SharedPreferences sp;

    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_skill_check, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(root.findViewById(R.id.top_bar), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.topMargin = insets.top;
            v.setLayoutParams(mlp);

            return WindowInsetsCompat.CONSUMED;
        });

        ImageView skillCheckArrow = root.findViewById(R.id.skill_check_arrow);
        ImageView skillCheckBg = root.findViewById(R.id.skill_check_bg);
        ImageView startButton = root.findViewById(R.id.startButton);
        ImageView stopButton = root.findViewById(R.id.stopButton);
        ImageView life1 = root.findViewById(R.id.life1);
        ImageView life2 = root.findViewById(R.id.life2);
        ImageView life3 = root.findViewById(R.id.life3);
        ImageView life4 = root.findViewById(R.id.life4);

        TextView notification = root.findViewById(R.id.notification);

        ImageView clickButton = root.findViewById(R.id.skill_check_click);
        ChipGroup chipGroup = root.findViewById(R.id.chipGroup);
        Chip chipEasy = root.findViewById(R.id.chipEasy);
        Chip chipMedium = root.findViewById(R.id.chipMedium);
        Chip chipHard = root.findViewById(R.id.chipHard);
        TextView points = root.findViewById(R.id.points);
        TextView record = root.findViewById(R.id.record);

        ViewCompat.setOnApplyWindowInsetsListener(clickButton, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.bottomMargin = insets.bottom + mlp.bottomMargin;
            v.setLayoutParams(mlp);

            return WindowInsetsCompat.CONSUMED;
        });

        AtomicInteger pointsCount = new AtomicInteger(0);

        sp = requireActivity().getSharedPreferences("skill_check", Context.MODE_PRIVATE);
        AtomicInteger recordCount = new AtomicInteger(sp.getInt("record_easy", 0));
        record.setText(String.valueOf(recordCount.get()));

        // Кол-во жизней
        final int[] lifes = {4};

        Random random = new Random();

        final float[] currentPos = new float[1];
        final float[] bgAngle = new float[1];

        AtomicReference<Thread> thread = new AtomicReference<>();

        final boolean[] buttonClicked = {false};

        final float[] angleIncrement = {0.5f};

        final int[] difficulty = {1};

        startButton.setOnClickListener(v -> {
            if (startButton.getAlpha() == 0.2f) {
                return;
            }

            stopButton.setAlpha(1.0f);
            startButton.setAlpha(0.2f);

            pointsCount.set(0);
            points.setText(String.valueOf(pointsCount.get()));

            life1.setColorFilter(null);
            life2.setColorFilter(null);
            life3.setColorFilter(null);
            life4.setColorFilter(null);

            lifes[0] = 4;

            chipEasy.setEnabled(false);chipMedium.setEnabled(false);chipHard.setEnabled(false);

            thread.set(new Thread(() -> {
                while (!thread.get().isInterrupted()) {

                    buttonClicked[0] = false;
                    bgAngle[0] = -(float)random.nextInt(BG_RANDOM_RANGE) + BG_START_ROTATION;

                    try {
                        requireActivity().runOnUiThread(() -> {
                            // Начальное положение
                            skillCheckBg.setRotation(bgAngle[0]);
                            skillCheckArrow.setVisibility(View.VISIBLE);
                            skillCheckBg.setVisibility(View.VISIBLE);
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    // Цикл анимации
                    float i = ANGLE_START_ROTATION;
                    while (i <= ARROW_END_ROTATION + bgAngle[0] - BG_START_ROTATION) {
                        float finalI1 = i;
                        try {
                            requireActivity().runOnUiThread(() -> skillCheckArrow.setRotation(finalI1));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        currentPos[0] = i;

                        try {
                            Thread.sleep(SLEEP_TIME);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }

                        i += angleIncrement[0];

                        if (buttonClicked[0]) {
                            break;
                        }
                    }

                    // Кнопка не нажата, реакция провалена
                    if (!buttonClicked[0]) {
                        buttonClicked[0] = true;
                        try {
                            requireActivity().runOnUiThread(() -> {
                                switch (lifes[0]) {
                                    case 4:
                                        life1.setColorFilter(Color.RED);
                                        break;
                                    case 3:
                                        life2.setColorFilter(Color.RED);
                                        break;
                                    case 2:
                                        life3.setColorFilter(Color.RED);
                                        break;
                                    case 1:
                                        life4.setColorFilter(Color.RED);
                                        stopButton.setAlpha(0.2f);
                                        startButton.setAlpha(1.0f);
                                        thread.get().interrupt();
                                        chipEasy.setEnabled(true);chipMedium.setEnabled(true);chipHard.setEnabled(true);

                                        notification.setText(R.string.end_game);
                                        notification.setTextColor(Color.RED);
                                        notification.setVisibility(View.VISIBLE);
                                        new Thread(() -> {
                                            try {
                                                Thread.sleep(DELAY_BETWEEN_SKILLCHECKS);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                requireActivity().runOnUiThread(() -> notification.setVisibility(View.INVISIBLE));
                                            }
                                            catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }).start();

                                        break;
                                }
                                lifes[0]--;
                            });
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    try {
                        requireActivity().runOnUiThread(() -> {
                            skillCheckArrow.setVisibility(View.INVISIBLE);
                            skillCheckBg.setVisibility(View.INVISIBLE);
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Интервал
                    try {
                        Thread.sleep(DELAY_BETWEEN_SKILLCHECKS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }));

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    thread.get().start();
                }
            };
            timer.schedule(timerTask, DELAY_AFTER_START_CLICKED);
        });

        // Кнопка СТОП
        stopButton.setOnClickListener(v -> {
            if (stopButton.getAlpha() == 0.2f) {
                return;
            }

            stopButton.setAlpha(0.2f);
            startButton.setAlpha(1.0f);
            skillCheckArrow.setVisibility(View.INVISIBLE);
            skillCheckBg.setVisibility(View.INVISIBLE);
            thread.get().interrupt();
            chipEasy.setEnabled(true);chipMedium.setEnabled(true);chipHard.setEnabled(true);
        });

        // Кнопка КЛИК
        clickButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !buttonClicked[0]) {
                clickButton.setImageResource(R.drawable.button_active_2);

                buttonClicked[0] = true;

                skillCheckArrow.setVisibility(View.INVISIBLE);
                skillCheckBg.setVisibility(View.INVISIBLE);

                currentPos[0] -= bgAngle[0] + 50;

                if (currentPos[0] >= 37 && currentPos[0] <= 47) {
                    notification.setText(R.string.great_skill_check);
                    notification.setTextColor(Color.GREEN);
                    notification.setVisibility(View.VISIBLE);
                    new Thread(() -> {
                        try {
                            Thread.sleep(DELAY_BETWEEN_SKILLCHECKS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            requireActivity().runOnUiThread(() -> notification.setVisibility(View.INVISIBLE));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                    int currentPoints = pointsCount.addAndGet(5);
                    points.setText(String.valueOf(currentPoints));
                    if (currentPoints > recordCount.get()) {
                        recordCount.set(currentPoints);
                        record.setText(String.valueOf(recordCount));

                        switch (difficulty[0]) {
                            case 1:
                                sp.edit().putInt("record_easy", currentPoints).apply();
                                break;

                            case 2:
                                sp.edit().putInt("record_medium", currentPoints).apply();
                                break;

                            case 3:
                                sp.edit().putInt("record_hard", currentPoints).apply();
                                break;
                        }
                    }
                    return true;
                }
                else if (currentPos[0] > 47 && currentPos[0] <= 80) {
                    notification.setText(R.string.good_skill_check);
                    notification.setTextColor(Color.WHITE);
                    notification.setVisibility(View.VISIBLE);
                    new Thread(() -> {
                        try {
                            Thread.sleep(DELAY_BETWEEN_SKILLCHECKS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            requireActivity().runOnUiThread(() -> notification.setVisibility(View.INVISIBLE));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                    int currentPoints = pointsCount.addAndGet(1);
                    points.setText(String.valueOf(currentPoints));
                    if (currentPoints > recordCount.get()) {
                        recordCount.set(currentPoints);
                        record.setText(String.valueOf(recordCount));

                        switch (difficulty[0]) {
                            case 1:
                                sp.edit().putInt("record_easy", currentPoints).apply();
                                break;

                            case 2:
                                sp.edit().putInt("record_medium", currentPoints).apply();
                                break;

                            case 3:
                                sp.edit().putInt("record_hard", currentPoints).apply();
                                break;
                        }
                    }
                    return true;
                }

                switch (lifes[0]) {
                    case 4:
                        life1.setColorFilter(Color.RED);
                        break;
                    case 3:
                        life2.setColorFilter(Color.RED);
                        break;
                    case 2:
                        life3.setColorFilter(Color.RED);
                        break;
                    case 1:
                        thread.get().interrupt();
                        life4.setColorFilter(Color.RED);
                        stopButton.setAlpha(0.2f);
                        startButton.setAlpha(1.0f);
                        chipEasy.setEnabled(true);chipMedium.setEnabled(true);chipHard.setEnabled(true);

                        notification.setText(R.string.end_game);
                        notification.setTextColor(Color.RED);
                        notification.setVisibility(View.VISIBLE);
                        new Thread(() -> {
                            try {
                                Thread.sleep(DELAY_BETWEEN_SKILLCHECKS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                requireActivity().runOnUiThread(() -> notification.setVisibility(View.INVISIBLE));
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();

                        break;
                }
                lifes[0]--;

                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                clickButton.setImageResource(R.drawable.button_active);
            }
            return false;
        });

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.chipEasy:
                    angleIncrement[0] = ANGLE_INCREMENT_EASY;
                    difficulty[0] = 1;
                    points.setText("0");
                    pointsCount.set(0);
                    recordCount.set(sp.getInt("record_easy", 0));
                    record.setText(String.valueOf(recordCount.get()));
                    break;

                case R.id.chipMedium:
                    angleIncrement[0] = ANGLE_INCREMENT_MEDIUM;
                    difficulty[0] = 2;
                    points.setText("0");
                    pointsCount.set(0);
                    recordCount.set(sp.getInt("record_medium", 0));
                    record.setText(String.valueOf(recordCount.get()));
                    break;

                case R.id.chipHard:
                    angleIncrement[0] = ANGLE_INCREMENT_HARD;
                    difficulty[0] = 3;
                    points.setText("0");
                    pointsCount.set(0);
                    recordCount.set(sp.getInt("record_hard", 0));
                    record.setText(String.valueOf(recordCount.get()));
                    break;
            }
        });

        return root;
    }
}
package com.example.whatsfortoday;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    private static final String PREFS = "food_preferences";
    private static final String KEY_FOODS = "foods";
    private static final String KEY_LAST = "last_meal";
    private final int forest = Color.rgb(24, 44, 41);
    private final int cream = Color.rgb(245, 240, 228);
    private final int gold = Color.rgb(217, 185, 120);
    private final ArrayList<String> foods = new ArrayList<>();
    private SharedPreferences prefs;
    private LinearLayout listContainer;
    private TextView resultText, countText;
    private EditText input;
    private String lastMeal = "";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        loadSavedData();
        buildScreen();
        renderFoods();
        if (!lastMeal.isEmpty()) resultText.setText("🍽️  " + lastMeal);
    }

    private void loadSavedData() {
        String raw = prefs.getString(KEY_FOODS, null);
        if (raw != null) {
            try {
                JSONArray arr = new JSONArray(raw);
                for (int i = 0; i < arr.length(); i++) foods.add(arr.getString(i));
            } catch (Exception ignored) {}
        } else {
            foods.add("Pasta");
            foods.add("Fried chicken");
            foods.add("Pizza");
            foods.add("Rice and chicken");
            foods.add("Sandwich");
            saveFoods();
        }
        lastMeal = prefs.getString(KEY_LAST, "");
    }

    private void saveFoods() {
        JSONArray arr = new JSONArray();
        for (String food : foods) arr.put(food);
        prefs.edit().putString(KEY_FOODS, arr.toString()).apply();
    }

    private void saveLastMeal() {
        prefs.edit().putString(KEY_LAST, lastMeal).apply();
    }

    private void buildScreen() {
        getWindow().setStatusBarColor(forest);
        getWindow().setNavigationBarColor(forest);
        getWindow().getDecorView().setSystemUiVisibility(0);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(cream);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(24), dp(22), dp(28));
        scroll.addView(page);
        setContentView(scroll);

        TextView title = text("What's for Today?", 27, Color.WHITE, true);
        TextView subtitle = text("Can't decide what to eat? Let fate decide.", 14, 0xFFD2DFD9, false);
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(20), dp(22), dp(20), dp(20));
        header.setBackground(round(forest, 22));
        page.addView(header, matchWrap());
        header.addView(title);
        addGap(header, 7);
        header.addView(subtitle);

        LinearLayout resultCard = new LinearLayout(this);
        resultCard.setOrientation(LinearLayout.VERTICAL);
        resultCard.setPadding(dp(18), dp(18), dp(18), dp(18));
        resultCard.setBackground(round(0xFF29443D, 17));
        LinearLayout.LayoutParams rc = matchWrap();
        rc.topMargin = dp(20);
        header.addView(resultCard, rc);

        TextView today = text("TODAY'S MEAL", 11, 0xFFD2DFD9, true);
        resultCard.addView(today);
        resultText = text("Tap randomize to decide", 23, Color.WHITE, true);
        LinearLayout.LayoutParams rp = matchWrap();
        rp.topMargin = dp(12);
        resultCard.addView(resultText, rp);

        Button random = button("✦   RANDOMIZE", gold, forest);
        LinearLayout.LayoutParams bp = matchWrap();
        bp.topMargin = dp(18);
        resultCard.addView(random, bp);
        random.setOnClickListener(v -> pickRandom());

        TextView listTitle = text("My food list", 21, forest, true);
        LinearLayout.LayoutParams lt = matchWrap();
        lt.topMargin = dp(27);
        page.addView(listTitle, lt);
        countText = text("", 12, 0xFF66736D, false);
        LinearLayout.LayoutParams cp = matchWrap();
        cp.topMargin = dp(4);
        page.addView(countText, cp);

        LinearLayout addRow = new LinearLayout(this);
        addRow.setOrientation(LinearLayout.HORIZONTAL);
        addRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams ar = matchWrap();
        ar.topMargin = dp(15);
        page.addView(addRow, ar);
        input = new EditText(this);
        input.setSingleLine(true);
        input.setTextSize(15);
        input.setHint("Add a meal...");
        input.setHintTextColor(0xFF89928D);
        input.setTextColor(forest);
        input.setPadding(dp(14), 0, dp(12), 0);
        input.setBackground(round(Color.WHITE, 13));
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        addRow.addView(input, new LinearLayout.LayoutParams(0, dp(50), 1));
        Button add = button("+", forest, Color.WHITE);
        LinearLayout.LayoutParams addp = new LinearLayout.LayoutParams(dp(54), dp(50));
        addp.leftMargin = dp(9);
        addRow.addView(add, addp);
        add.setOnClickListener(v -> addFood());
        input.setOnEditorActionListener((v, action, event) -> {
            if (action == EditorInfo.IME_ACTION_DONE) { addFood(); return true; }
            return false;
        });

        listContainer = new LinearLayout(this);
        listContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = matchWrap();
        lp.topMargin = dp(12);
        page.addView(listContainer, lp);

        TextView footer = text("Your list is saved automatically on this phone.", 12, 0xFF77817B, false);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams fp = matchWrap();
        fp.topMargin = dp(20);
        page.addView(footer, fp);
    }

    private void addFood() {
        String value = input.getText().toString().trim();
        if (value.isEmpty()) return;
        for (String item : foods) {
            if (item.equalsIgnoreCase(value)) {
                Toast.makeText(this, "That meal is already on your list.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        foods.add(value);
        input.setText("");
        saveFoods();
        renderFoods();
    }

    private void removeFood(String food) {
        foods.remove(food);
        saveFoods();
        renderFoods();
        if (food.equals(lastMeal)) {
            lastMeal = "";
            saveLastMeal();
            resultText.setText("Tap randomize to decide");
        }
    }

    private void pickRandom() {
        if (foods.isEmpty()) {
            Toast.makeText(this, "Add some meals to your list first.", Toast.LENGTH_SHORT).show();
            return;
        }
        String choice = foods.get(new Random().nextInt(foods.size()));
        if (foods.size() > 1 && choice.equals(lastMeal)) {
            int index = foods.indexOf(choice);
            choice = foods.get((index + 1 + new Random().nextInt(foods.size() - 1)) % foods.size());
        }
        lastMeal = choice;
        saveLastMeal();
        resultText.setText("🍽️  " + choice);
    }

    private void renderFoods() {
        if (listContainer == null) return;
        listContainer.removeAllViews();
        countText.setText(foods.size() + (foods.size() == 1 ? " meal" : " meals"));
        if (foods.isEmpty()) {
            TextView empty = text("Your list is empty. Add your first meal above.", 14, 0xFF66736D, false);
            empty.setPadding(dp(4), dp(18), dp(4), dp(18));
            listContainer.addView(empty);
            return;
        }
        for (String food : foods) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(dp(15), dp(5), dp(8), dp(5));
            row.setBackground(round(Color.WHITE, 13));
            LinearLayout.LayoutParams rowp = matchWrap();
            rowp.bottomMargin = dp(8);
            listContainer.addView(row, rowp);
            TextView name = text("🍴  " + food, 15, forest, true);
            row.addView(name, new LinearLayout.LayoutParams(0, dp(48), 1));
            TextView delete = text("✕", 18, 0xFF9B6259, true);
            delete.setGravity(Gravity.CENTER);
            row.addView(delete, new LinearLayout.LayoutParams(dp(42), dp(42)));
            delete.setOnClickListener(v -> removeFood(food));
        }
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private Button button(String label, int bg, int fg) {
        Button b = new Button(this);
        b.setText(label); b.setTextSize(14); b.setTextColor(fg);
        b.setAllCaps(false); b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(round(bg, 13));
        return b;
    }

    private GradientDrawable round(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(dp(radius));
        return d;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(-1, -2);
    }
    private void addGap(LinearLayout parent, int h) {
        View gap = new View(this);
        parent.addView(gap, new LinearLayout.LayoutParams(1, dp(h)));
    }
    private int dp(float v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }
}

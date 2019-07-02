package sg.edu.rp.c346.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tvLastCalaculation, tvLastBmi, tvRemarks;
    EditText etWeight, etHeight;
    Button btnCalculate, btnReset;

    float bmiCalculation = 0;
    String datetime = "", bmiRemarks = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLastCalaculation = findViewById(R.id.tvLastDate);
        tvLastBmi = findViewById(R.id.tvLastBmi);
        tvRemarks = findViewById(R.id.tvRemarks);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etWeight.getText().toString().trim().isEmpty() && etHeight.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Weight/Height cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    float weight = Float.parseFloat(etWeight.getText().toString());
                    float height = Float.parseFloat(etHeight.getText().toString());
                    bmiCalculation = weight / (height * height);

                    clearFields();

                    Calendar now = Calendar.getInstance();
                    datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                            (now.get(Calendar.MONTH) + 1) + "/" +
                            now.get(Calendar.YEAR) + " " +
                            now.get(Calendar.HOUR_OF_DAY) + ":" +
                            now.get(Calendar.MINUTE);

                    if(bmiCalculation < 18.5){
                        bmiRemarks = "You are underweight";
                    }else if(bmiCalculation < 24.9){
                        bmiRemarks = "Your BMI is normal";
                    }else if(bmiCalculation < 29.9){
                        bmiRemarks = "You are overweight";
                    }else if(bmiCalculation > 30){
                        bmiRemarks = "You are obese";
                    }

                    tvLastCalaculation.setText(getResources().getString(R.string.last_calc) + " " + datetime + "");
                    tvLastBmi.setText(getResources().getString(R.string.last_bmi) + " " + bmiCalculation + "");
                    tvRemarks.setText(bmiRemarks);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLastCalaculation.setText(getResources().getString(R.string.last_calc));
                tvLastBmi.setText(getResources().getString(R.string.last_bmi));
                tvRemarks.setText("");
                clearSavedData();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lastCalcDate = prefs.getString("lastCalc", "");
        float lastBmi = prefs.getFloat("lastBmi", bmiCalculation);
        String remarks = prefs.getString("remarks", "");

        tvLastCalaculation.setText(getResources().getString(R.string.last_calc) + " " + lastCalcDate + "");
        tvLastBmi.setText(getResources().getString(R.string.last_bmi) + " " + lastBmi + "");
        tvRemarks.setText(remarks);

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void saveData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString("lastCalc", datetime + "").putString("remarks", bmiRemarks).putFloat("lastBmi", bmiCalculation  );
        prefsEdit.commit();
    }

    private void clearSavedData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString("lastCalc",  " ").putString("remarks", " ").putFloat("lastBmi", 0);
        prefsEdit.commit();
    }

    private void clearFields() {
        etHeight.setText("");
        etWeight.setText("");
    }
}

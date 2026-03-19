package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // Array Initialisations
    final String[] conversionTypes = {"Currency", "Temperature", "Fuel"};
    final String[] temperatures = {"Fahrenheit", "Celsius", "Kelvin"};
    final String[] fuel = {"mpg", "gal", "nm", "L", "km", "km/L"};
    final String[] currencies = {"AUD", "EUR", "JPY", "GBP"};

    // Initialisation variables
    Spinner fromUnitSpinner;
    Spinner toDestinationUnitSpinner;

    Spinner conversionTypeSpinner;

    EditText inputValue;
    Button convertButton;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner Initialisation
        fromUnitSpinner = findViewById(R.id.fromUnit);
        toDestinationUnitSpinner = findViewById(R.id.destinationUnit);
        conversionTypeSpinner = findViewById(R.id.conversionType);

        // Button and EditText Initialisation
        inputValue = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);

        // ResultText Initialisation
        resultText = findViewById(R.id.resultText);

        // Setting an onClick listener for the button
        convertButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String inputStr = inputValue.getText().toString();

                String type = conversionTypeSpinner.getSelectedItem().toString();
                String from = fromUnitSpinner.getSelectedItem().toString();
                String to = toDestinationUnitSpinner.getSelectedItem().toString();
                double amount;

                // Conversion Logic
                if (inputStr.isEmpty()) {
                    resultText.setText("Please enter a value");
                    return;
                }

                if (type.equals("Currency")) {
                    double amountInUSD = 0;
                    double finalAmount = 0;

                    try {
                        amount = Double.parseDouble(inputStr);
                        switch (from) {
                            case "AUD":
                                amountInUSD = amount / 1.55;
                                break;
                            case "EUR":
                                amountInUSD = amount / 0.92;
                                break;
                            case "JPY":
                                amountInUSD = amount / 148.50;
                                break;
                            case "GBP":
                                amountInUSD = amount / 0.78;
                                break;
                        }

                        switch (to) {
                            case "AUD":
                                finalAmount = amountInUSD * 1.55;
                                break;
                            case "EUR":
                                finalAmount = amountInUSD * 0.92;
                                break;
                            case "JPY":
                                finalAmount = amountInUSD * 148.50;
                                break;
                            case "GBP":
                                finalAmount = amountInUSD * 0.78;
                                break;
                        }

                        // Display final amount + formatted result
                        resultText.setText("Result: " + String.format("%.2f", finalAmount) + " " + to);
                    } catch (NumberFormatException e) {
                        resultText.setText("Invalid Input");
                    }
                }
                else if (type.equals("Temperature")) {
                    double celsius = 0;
                    double finalTemp = 0;

                    try {
                        amount = Double.parseDouble(inputStr);
                        switch (from) {
                            case "Fahrenheit":
                                celsius = (amount - 32) / 1.8;
                                break;
                            case "Kelvin":
                                celsius = amount - 273.15;
                                break;
                            case "Celsius":
                                celsius = amount;
                                break;
                        }

                        switch (to) {
                            case "Fahrenheit":
                                finalTemp = (celsius * 1.8) + 32;
                                break;
                            case "Kelvin":
                                finalTemp = celsius + 273.15;
                                break;
                            case "Celsius":
                                finalTemp = celsius;
                                break;
                        }

                        // Display final temp + formatted result
                        resultText.setText("Result: " + String.format("%.2f %s", finalTemp, to));

                    } catch (NumberFormatException e) {
                        resultText.setText("Invalid Input");
                    }
                }
                else if (type.equals("Fuel")) {
                    double finalValue = 0;

                    try {
                        amount = Double.parseDouble(inputStr);

                        if (from.equals("mpg") && to.equals("km/L")) {
                            finalValue = amount * 0.425;
                        }
                        else if (from.equals("km/L") && to.equals("mpg")) {
                            finalValue = amount / 0.425;
                        }
                        else if (from.equals("gal") && to.equals("L")) {
                            finalValue = amount * 3.785;
                        }
                        else if (from.equals("L") && to.equals("gal")) {
                            finalValue = amount / 3.785;
                        }
                        else if (from.equals("nm") && to.equals("km")) {
                            finalValue = amount * 1.852;
                        }
                        else if (from.equals("km") && to.equals("nm")) {
                            finalValue = amount / 1.852;
                        }
                        else if (from.equals(to)) {
                            finalValue = amount;
                        }
                        else {
                            resultText.setText("Invalid Conversion");
                            return;
                        }

                        resultText.setText("Result: " + String.format("%.2f %s", finalValue, to));

                    } catch (NumberFormatException e) {
                        resultText.setText("Invalid Input");
                    }
                }

            }
        });


        // ArrayAdapter Initialisation
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                currencies
        );

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                conversionTypes
        );

        // Specifies what layout is being used when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Connecting adapter to the spinners
        fromUnitSpinner.setAdapter(adapter);
        toDestinationUnitSpinner.setAdapter(adapter);
        conversionTypeSpinner.setAdapter(typeAdapter);

        conversionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = conversionTypes[position];
                ArrayAdapter<String> unitAdapter;

                switch (selectedType) {
                    case "Temperature":
                        unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, temperatures);
                        break;
                    case "Fuel":
                        unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, fuel);
                        break;
                    default: // Currency
                        unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, currencies);
                        break;
                }

                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fromUnitSpinner.setAdapter(unitAdapter);
                toDestinationUnitSpinner.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}

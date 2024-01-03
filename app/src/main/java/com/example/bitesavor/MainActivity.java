package com.example.bitesavor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;

public class MainActivity extends AppCompatActivity {
    Button button;
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DBHelper(this);
        button = findViewById(R.id.start_button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.insertDefaultAdmin();

                String itemName = "Chicken Pizza";
                if (!myDB.isItemExists(itemName)) {

                    myDB.insertItem(getApplicationContext(), 1, "Chicken Tikka Pizza", 4.5, R.drawable.pizza_1,"Pizza Sauce, Mozzarella, Chicken Tikka, Onions, Green Peppers with Mint Sauce");
                    myDB.insertItem(getApplicationContext(), 2, "Pizza Chicken Ranch", 5.5, R.drawable.pizza_2,"Grilled Chicken with mozzarella cheese, fresh mushrooms, fresh tomatoes and Jalepenos.");
                    myDB.insertItem(getApplicationContext(), 3, "Cheese Burger", 2.5, R.drawable.burger_1,"Grilled chicken topped with melted mozzarella cheese, sautéed fresh mushrooms, ripe tomatoes, and a kick of jalapeños for a flavorful and satisfying combination.");
                    myDB.insertItem(getApplicationContext(), 4, "Double Burger", 3.5, R.drawable.burger_2,
                            " two juicy beef patties, layered with melted cheese, crisp lettuce, ripe tomatoes all nestled between soft buns .");
                    myDB.insertItem(getApplicationContext(), 5, "Chicken Wrap", 1.01, R.drawable.wrap_1,"Savor the perfection of our Chicken Wrap — grilled chicken, crisp veggies, and zesty salsa embraced in a soft tortilla. A burst of flavors in every bite!");
                    myDB.insertItem(getApplicationContext(), 6, "Shrimp Wrap", 1.01, R.drawable.wrap_1,"Dive into our Shrimp Wrap — succulent shrimp, fresh veggies, and a tangy sauce wrapped in a soft tortilla. A taste of the sea in every delicious bite!");
                    myDB.insertItem(getApplicationContext(), 7, "Coca-Cola", 1.00, R.drawable.drinks_1,"The original coca-cola");
                    myDB.insertItem(getApplicationContext(), 8, "Sprite", 1.00, R.drawable.drinks_2,"The quickest lemonade");
                    myDB.insertItem(getApplicationContext(), 9, "Water", 0.10, R.drawable.water,"300ml");
                    myDB.insertItem(getApplicationContext(), 10, "Water Big", 0.20, R.drawable.water,"500ml");

                }
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.organizadorferramentas;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        posicionarBarraAplicativo();
    }

    private void posicionarBarraAplicativo() {
        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(),
                new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        Insets barras = insets.getInsets(
                                WindowInsetsCompat.Type.systemBars()
                                        | WindowInsetsCompat.Type.ime());
                        v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
                        return WindowInsetsCompat.CONSUMED;
                    }
                });
    }
}

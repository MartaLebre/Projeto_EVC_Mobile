package amsi.dei.estg.ipleiria.projetoevc.vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import amsi.dei.estg.ipleiria.projetoevc.R;

public class MenuMainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        toolbar.findViewById(R.id.myToolBar);

        setSupportActionBar(toolbar);
    }
}
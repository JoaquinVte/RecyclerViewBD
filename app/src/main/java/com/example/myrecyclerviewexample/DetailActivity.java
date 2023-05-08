package com.example.myrecyclerviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.base.ImageDownloader;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;

public class DetailActivity extends BaseActivity {

    public static enum MODE implements Serializable {
        UPDATE,CREATE
    }

    private Usuario usuario;
    private Button btnSave;
    private Button btnCreate;
    private Button btnCancel;
    private Spinner spinnerOficio;
    private ImageView imageViewOficio;
    private TextInputEditText tietApellidos;

    private TextInputEditText tietNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MODE mode = MODE.valueOf(getIntent().getExtras().getString("mode"));

        btnSave = findViewById(R.id.btnSave);
        btnCreate = findViewById(R.id.btnCreate);
        btnCancel = findViewById(R.id.btnCancel);
        tietNombre = findViewById(R.id.tietNombre);
        tietApellidos = findViewById(R.id.tietApellidos);
        spinnerOficio = findViewById(R.id.spinnerOficio);
        imageViewOficio = findViewById(R.id.imageViewOficio);

        ArrayAdapter<Oficio> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Model.getInstance(getApplicationContext()).getOficios());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOficio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Oficio oficio = adapter.getItem(i);
                setImage(oficio.getIdOficio());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerOficio.setAdapter(adapter);

        switch (mode){
            case UPDATE:
                usuario = (Usuario) getIntent().getExtras().getSerializable("user");
                tietNombre.setText(usuario.getNombre());
                tietApellidos.setText(usuario.getApellidos());
                setImage(usuario.getIdUsuario());
                spinnerOficio.setSelection(Model.getInstance(getApplicationContext()).getOficios().indexOf(new Oficio(usuario.getOficio(),"")));
                btnCreate.setVisibility(View.GONE);
                break;
            case CREATE:
                btnSave.setVisibility(View.GONE);
                break;

        }


        btnCancel.setOnClickListener(
                view -> finish()
        );

        btnSave.setOnClickListener(
                v-> {
                    showProgress();
                    executeCall(new CallInterface() {
                        boolean actualizado;
                        @Override
                        public void doInBackground() {
                            actualizado = Model.getInstance(getApplicationContext()).updateUsuario(new Usuario(usuario.getIdUsuario(), tietNombre.getText().toString(),tietApellidos.getText().toString(),((Oficio)spinnerOficio.getSelectedItem()).getIdOficio()));
                        }

                        @Override
                        public void doInUI() {
                            hideProgress();
                            if (actualizado) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(DetailActivity.this, "Algo ha salido mal, usuario no actualizdo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        );

        btnCreate.setOnClickListener(
                v -> {
                    showProgress();
                   executeCall(new CallInterface() {

                       Usuario u;

                       @Override
                       public void doInBackground() {
                           String nombre = tietNombre.getText().toString();
                           String apellidos = tietApellidos.getText().toString();
                           Oficio oficio = (Oficio) spinnerOficio.getSelectedItem();

                           u = new Usuario(nombre,apellidos,oficio.getIdOficio());
                           u = Model.getInstance(getApplicationContext()).insertUsuario(u);

                       }

                       @Override
                       public void doInUI() {
                            if (u!=null) {

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                setResult(RESULT_OK);
                                finish();

                            }else
                                Toast.makeText(getApplicationContext(),"Algo ha ido mal. Revisa los campos",Toast.LENGTH_LONG).show();
                       }
                   });
                }
        );
    }



    private void setImage(int idOficio) {
        switch (idOficio) {
            case 1:
                imageViewOficio.setImageResource(R.mipmap.ic_1_foreground);
                break;
            case 2:
                imageViewOficio.setImageResource(R.mipmap.ic_2_foreground);
                break;
            case 3:
                imageViewOficio.setImageResource(R.mipmap.ic_3_foreground);
                break;
            case 4:
                imageViewOficio.setImageResource(R.mipmap.ic_4_foreground);
                break;
            case 5:
                imageViewOficio.setImageResource(R.mipmap.ic_5_foreground);
                break;
            case 6:
                imageViewOficio.setImageResource(R.mipmap.ic_6_foreground);
                break;
            case 7:
                imageViewOficio.setImageResource(R.mipmap.ic_7_foreground);
                break;
            case 8:
                imageViewOficio.setImageResource(R.mipmap.ic_8_foreground);
                break;
            case 9:
                imageViewOficio.setImageResource(R.mipmap.ic_9_foreground);
                break;
            case 10:
                imageViewOficio.setImageResource(R.mipmap.ic_10_foreground);
                break;
            case 11:
                imageViewOficio.setImageResource(R.mipmap.ic_11_foreground);
                break;
            case 12:
                imageViewOficio.setImageResource(R.mipmap.ic_12_foreground);
                break;
        }
    }
}
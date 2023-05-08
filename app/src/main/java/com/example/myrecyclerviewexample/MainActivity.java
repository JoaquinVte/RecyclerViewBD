package com.example.myrecyclerviewexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, CallInterface {

    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> detailActivityLauncherForUpdate;
    private ActivityResultLauncher<Intent> detailActivityLauncherForInsert;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    private FloatingActionButton fabAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        fabAdd = findViewById(R.id.fabAdd);

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerViewAdapter.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                Usuario u = Model.getInstance(getApplicationContext()).getUsuarios().get(viewHolder.getAdapterPosition());

                executeCall(new CallInterface() {
                    @Override
                    public void doInBackground() {
                        Model.getInstance(getApplicationContext()).removeUser(u);
                    }

                    @Override
                    public void doInUI() {
                        myRecyclerViewAdapter.notifyItemRemoved(position);

                        Snackbar.make(recyclerView, "Deleted " + u.getNombre(), Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        executeCall(new CallInterface() {
                                            @Override
                                            public void doInBackground() {
                                                Model.getInstance(getApplicationContext()).insertUsuario(u);
                                            }

                                            @Override
                                            public void doInUI() {
                                                myRecyclerViewAdapter.notifyItemInserted(position);
                                            }
                                        });

                                    }
                                })
                                .show();
                    }
                });

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);


        detailActivityLauncherForUpdate = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        myRecyclerViewAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"Usuario actualizdo.",Toast.LENGTH_LONG).show();
                    }

                }
        );

        detailActivityLauncherForInsert = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        myRecyclerViewAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"Usuario creado.",Toast.LENGTH_LONG).show();
                    }
                });

        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
            intent.putExtra("mode",DetailActivity.MODE.CREATE.toString());
            detailActivityLauncherForInsert.launch(intent);
        });

        showProgress();
        executeCall(this);
    }

    @Override
    public void onClick(View view) {
        Usuario u = Model.getInstance(getApplicationContext()).getUsuarios().get(recyclerView.getChildAdapterPosition(view));

        Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
        intent.putExtra("mode",DetailActivity.MODE.UPDATE.toString());
        intent.putExtra("user",u);
        detailActivityLauncherForUpdate.launch(intent);

//        Toast.makeText(this,"Clic en " + u.getOficio(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doInBackground() {
        Model.getInstance(getApplicationContext()).loadDataFromDB();
    }

    @Override
    public void doInUI() {
        hideProgress();
        List<Usuario> usuarioList = Model.getInstance(getApplicationContext()).getUsuarios();
        myRecyclerViewAdapter.setUsuarios(usuarioList);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.configuracion):
                Intent intentPreferenciasActivity = new Intent(this, PreferenceActivity.class);
                startActivity(intentPreferenciasActivity);
                return true;
            case (R.id.exit):
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
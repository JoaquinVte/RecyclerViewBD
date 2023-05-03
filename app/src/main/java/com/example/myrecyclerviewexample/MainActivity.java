package com.example.myrecyclerviewexample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
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
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, CallInterface {

    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> detailActivityLauncher;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);

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

                Usuario u = Model.getInstance().getUsuarios().get(viewHolder.getAdapterPosition());

                myRecyclerViewAdapter.notifyItemRemoved(position);

                Snackbar.make(recyclerView, "Deleted " + u.getNombre(), Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                myRecyclerViewAdapter.notifyItemInserted(position);
                            }
                        })
                        .show();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);


        detailActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== Activity.RESULT_OK)
                        myRecyclerViewAdapter.notifyDataSetChanged();
                }
        );

        showProgress();
        executeCall(this);
    }

    @Override
    public void onClick(View view) {
        Usuario u = Model.getInstance().getUsuarios().get(recyclerView.getChildAdapterPosition(view));

        Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
        intent.putExtra("mode",DetailActivity.MODE.UPDATE.toString());
        intent.putExtra("user",u);
        detailActivityLauncher.launch(intent);

//        Toast.makeText(this,"Clic en " + u.getOficio(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doInBackground() {

        Model.getInstance().getUsuarios();
        Model.getInstance().getOficios();

    }

    @Override
    public void doInUI() {
        hideProgress();
        List<Usuario> usuarioList = Model.getInstance().getUsuarios();
        myRecyclerViewAdapter.setUsuarios(usuarioList);
    }
}
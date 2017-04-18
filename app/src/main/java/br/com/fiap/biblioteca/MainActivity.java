package br.com.fiap.biblioteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.com.fiap.biblioteca.Modelo.Livro;
import br.com.fiap.biblioteca.adapter.LivroAdapter;
import br.com.fiap.biblioteca.dao.LivroDAO;
import br.com.fiap.biblioteca.interfaces.RecyclerViewOnClickListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvLivros;
    private LivroAdapter adapter;
    private List<Livro> livros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rvLivros = (RecyclerView) findViewById(R.id.rvLivros);
        carregaLivros();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cadastroLivro) {
            novoLivro();
        } else if (id == R.id.nav_sair) {
            sair();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            carregaLivros();
        }
    }


    private void carregaLivros() {
        this.livros =  new LivroDAO(this).getAll();
        this.rvLivros.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new LivroAdapter(this, this.livros);

        this.adapter.setRecyclerViewOnClickListener(new RecyclerViewOnClickListener() {
            @Override
            public void onClickListener(View v, int position) {
                onClickLivro(livros, position);
            }
        });

        this.rvLivros.setAdapter(this.adapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback());
        helper.attachToRecyclerView(this.rvLivros);
    }

    private void novoLivro() {
        startActivityForResult(new Intent(MainActivity.this,  NovoLivroActivity.class),  C.NOVO_LIVRO);
    }

    private void removeLivro(RecyclerView.ViewHolder viewHolder){
        LivroDAO dao = new LivroDAO(MainActivity.this);
        if(dao.remove(this.livros.get(viewHolder.getAdapterPosition()).getId())) {
            this.livros.remove(viewHolder.getAdapterPosition());
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
            Toast.makeText(this,"Livro removido com sucesso.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Livro não removido!", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickLivro(List<Livro> livros, int position){
        Intent i = new Intent(MainActivity.this,  NovoLivroActivity.class);
        i.putExtra("livro",  livros.get(position));
        startActivityForResult(i,  C.EDITA_LIVRO);
    }

    private void sair() {
        SharedPreferences pref = getSharedPreferences(C.KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(C.KEY_LOGIN, "");
        editor.apply();
        finish();
    }








    class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        public ItemTouchHelperCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
           removeLivro(viewHolder);
        }
    }


}

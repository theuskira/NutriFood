package br.com.nutrifood.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.nutrifood.R;
import br.com.nutrifood.fragment.Adicionar_Alimento_Tipo_Fragment;
import br.com.nutrifood.fragment.ListarAlimentosFragment;
import br.com.nutrifood.fragment.CalculoFragment;
import br.com.nutrifood.fragment.CalculoProFragment;
import br.com.nutrifood.fragment.Editar_Conta_Fragment;
import br.com.nutrifood.fragment.PerfilFragment;
import br.com.nutrifood.fragment.PrincipalFragment;
import br.com.nutrifood.helper.UsuarioFirebase;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth autenticacao;

    private  View view;
    private Menu menu;
    private CircleImageView foto;
    private ImageView fotoPadrao;
    private FrameLayout frameLayout;
    private TextView nome, email;

    //Fragments
    private PrincipalFragment principalFragment = new PrincipalFragment();
    private CalculoFragment calculoFragment = new CalculoFragment();
    private Editar_Conta_Fragment editar_conta_fragment = new Editar_Conta_Fragment();
    private PerfilFragment perfilFragment = new PerfilFragment();
    private Adicionar_Alimento_Tipo_Fragment adicionar_alimento_fragment = new Adicionar_Alimento_Tipo_Fragment();
    private CalculoProFragment calculoProFragment = new CalculoProFragment();
    private ListarAlimentosFragment listarAlimentosFragment = new ListarAlimentosFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = findViewById(R.id.frameLayout);

        iniciarFragment(principalFragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu  = navigationView.getMenu();

        view = navigationView.getHeaderView(0);

        nome = view.findViewById(R.id.txtMainNome);
        email = view.findViewById(R.id.txtMainEmail);

        foto = view.findViewById(R.id.imgMainFoto);
        fotoPadrao = view.findViewById(R.id.imgMain);

        //verificarUsuario();
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        //verificarUsuario();
    }

    private void verificarUsuario() {
        try {
            autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

            FirebaseUser usuarioAtual = null;
            if (autenticacao.getCurrentUser() != null) {
                usuarioAtual = autenticacao.getCurrentUser();
            }

            if (usuarioAtual != null) {
                //Esta Logado
                menu.getItem(6).setVisible(true);
                menu.getItem(7).setVisible(true);
                menu.getItem(8).setVisible(false);
                menu.getItem(9).setVisible(true);
                menu.getItem(10).setVisible(true);

                nome.setText(usuarioAtual.getDisplayName());
                email.setText(usuarioAtual.getEmail());
                email.setVisibility(View.VISIBLE);

                alterarImagem();
            } else {
                //Nao Esta Logado
                menu.getItem(6).setVisible(false);
                menu.getItem(7).setVisible(false);
                menu.getItem(8).setVisible(true);
                menu.getItem(9).setVisible(false);
                menu.getItem(10).setVisible(false);
                email.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else if (!frameLayout.getContext().equals(principalFragment.getActivity())){

            iniciarFragment(principalFragment);

        }else{
            alertaSair();

        }

    }

    private void alertaSair(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair do aplicativo");
        builder.setMessage("Deseja sair do aplicativo?");
        builder.setCancelable(true);
        builder.setPositiveButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            getSupportActionBar().setTitle(R.string.calculo_imc);

            iniciarFragment(calculoFragment);
        }
        else if (id == R.id.nav_profi) {

            getSupportActionBar().setTitle(R.string.calculo_avancado);

            iniciarFragment(calculoProFragment);
        }
        else if (id == R.id.nav_home) {

            getSupportActionBar().setTitle(R.string.app_name);

            iniciarFragment(principalFragment);
        }
        else if (id == R.id.nav_orientacao) {
            Toast.makeText(MainActivity.this, "Orientação Nutricional", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_alarme) {
            Toast.makeText(MainActivity.this, "Alarmes", Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.nav_alimentos) {

            getSupportActionBar().setTitle(R.string.alimentos);

            iniciarFragment(listarAlimentosFragment);
        }
        else if (id == R.id.nav_manage) {

            iniciarFragment(editar_conta_fragment);
        }
        else if (id == R.id.nav_Add_fruta) {
            getSupportActionBar().setTitle(R.string.adicionar_alimento);

            iniciarFragment(adicionar_alimento_fragment);
        }
        else if (id == R.id.nav_entrar) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else if (id == R.id.nav_perfil) {

            iniciarFragment(perfilFragment);
        }
        else if (id == R.id.nav_deslogar) {
            deslogarUsuario();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deslogarUsuario(){
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deslogar");
        builder.setMessage("Deseja sair da conta " + usuario.getDisplayName() + "?");
        builder.setCancelable(true);
        builder.setPositiveButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    autenticacao.signOut();
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),R.string.erro + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alterarImagem(){
        try {
            FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
            Uri url = usuario.getPhotoUrl();

            if(url != null){
                Glide.with(MainActivity.this)
                        .load(url)
                        .into(foto);
                foto.setVisibility(View.VISIBLE);
                fotoPadrao.setVisibility(View.GONE);
            }else {
                foto.setVisibility(View.GONE);
                fotoPadrao.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            foto.setVisibility(View.GONE);
            fotoPadrao.setVisibility(View.VISIBLE);
            Toast.makeText(this,R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}

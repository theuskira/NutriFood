package br.com.nutrifood.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.nutrifood.R;
import br.com.nutrifood.activity.LoginActivity;
import br.com.nutrifood.activity.SplashActivity;
import br.com.nutrifood.config.ConfiguracaoFirebase;
import br.com.nutrifood.helper.UsuarioFirebase;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private TextView email;
    private TextView nome;

    private FirebaseAuth autenticacao;

    private CircleImageView circleImageViewPerfil;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        email = view.findViewById(R.id.txtEmailPerfil);
        nome = view.findViewById(R.id.txtNomePerfil);

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        circleImageViewPerfil = view.findViewById(R.id.circleImageViewFotoPerfil);

        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if(url != null){
            Glide.with(getActivity())
                    .load(url)
                    .into(circleImageViewPerfil);
        }else {
            circleImageViewPerfil.setImageResource(R.drawable.logo);
        }

        verificarDados();

        return view;
    }


    private void verificarDados(){
        try{
            //Toast.makeText(MainActivity.this, "" + autenticacao.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();

            FirebaseUser usuarioAtual = null;
            if(autenticacao.getCurrentUser() != null){
                usuarioAtual = autenticacao.getCurrentUser();
            }

            if(usuarioAtual != null){
                email.setText(usuarioAtual.getEmail());
                nome.setText(usuarioAtual.getDisplayName());
            }else{
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deslogarUsuario(){
        try{
            autenticacao.signOut();
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
        }catch (Exception e){
            Toast.makeText(getActivity(),R.string.erro + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}

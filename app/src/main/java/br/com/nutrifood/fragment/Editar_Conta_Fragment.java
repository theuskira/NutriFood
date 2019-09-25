package br.com.nutrifood.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.nutrifood.R;
import br.com.nutrifood.config.ConfiguracaoFirebase;
import br.com.nutrifood.helper.Base64Custom;
import br.com.nutrifood.helper.UsuarioFirebase;
import br.com.nutrifood.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Editar_Conta_Fragment extends Fragment {

    private CircleImageView circleImageViewPerfil;

    private FirebaseUser firebaseUser;

    private EditText nome;
    private EditText email;
    private EditText telefone;
    private EditText peso;
    private EditText altura;


    public Editar_Conta_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editar_conta, container, false);

        firebaseUser = UsuarioFirebase.getUsuarioAtual();

        nome = view.findViewById(R.id.editarNome);
        email = view.findViewById(R.id.editarEmail);
        telefone = view.findViewById(R.id.editarTelefone);
        peso = view.findViewById(R.id.editarPesoAtual);
        altura = view.findViewById(R.id.editarAlturaAtual);

        circleImageViewPerfil = view.findViewById(R.id.circleImageViewEditFotoPerfil);

        retornarValores();

        return view;
    }

    private void retornarValores(){

        try{
            email.setText(firebaseUser.getEmail());
            nome.setText(firebaseUser.getDisplayName());

            Uri url = firebaseUser.getPhotoUrl();

            if(url != null){
                Glide.with(getActivity())
                        .load(url)
                        .into(circleImageViewPerfil);
            }else {
                circleImageViewPerfil.setImageResource(R.drawable.logo);
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}

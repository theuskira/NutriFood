package br.com.nutrifood.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.nutrifood.R;
import br.com.nutrifood.config.ConfiguracaoFirebase;
import br.com.nutrifood.helper.Permissao;
import br.com.nutrifood.helper.UsuarioFirebase;
import br.com.nutrifood.model.Alimentos;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdicionarFrutasFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private Alimentos frutas = new Alimentos();

    private EditText frutaNome, frutaUmidade, frutaEnergia, frutaProteina, frutaLipideos,
            frutaColesterol, frutaCarboidrato, frutaFibraAlimentar, frutaCinzas, frutaCalcio,
            frutaMagnesio, frutaManganes, frutaFosforo, frutaFerro, frutaSodio, frutaPotassio,
            frutaCobre, frutaZinco, frutaRetinol, frutaTiamina, frutaRiboflavina, frutaPiridoxina,
            frutaNiacina, frutaVitaminaC;
    private Button btnCadastrar, btnVoltar;
    private ProgressBar progressBar;
    private ScrollView scrollView;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private Bitmap imagem = null;
    private ImageButton imageButtonCamera, imageButtonGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView circleImageViewPerfil;
    private StorageReference storageReference;


    public AdicionarFrutasFragment() {
        // Required empty public constructor
    }
/*

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adicionar_frutas, container, false);

        //Configuracao Inicial
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase().child("alimentos").child("frutas");
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        //Validar Permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        pegarCampos(view);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCampos();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adicionar_Alimento_Tipo_Fragment fragment = new Adicionar_Alimento_Tipo_Fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamera();
            }
        });

        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        return view;
    }

    private void pegarCampos(View view){
        frutaNome = view.findViewById(R.id.txtAddFrutaNome);
        frutaUmidade = view.findViewById(R.id.txtAddFrutaUmidade);
        frutaEnergia = view.findViewById(R.id.txtAddFrutaEnergia);
        frutaProteina = view.findViewById(R.id.txtAddFrutaProteina);
        frutaLipideos = view.findViewById(R.id.txtAddFrutaLipideos);
        frutaColesterol = view.findViewById(R.id.txtAddFrutaColesterol);
        frutaCarboidrato = view.findViewById(R.id.txtAddFrutaCarboidrato);
        frutaFibraAlimentar = view.findViewById(R.id.txtAddFrutaFribraAlimentar);
        frutaCinzas = view.findViewById(R.id.txtAddFrutaCinzas);
        frutaCalcio = view.findViewById(R.id.txtAddFrutaCalcio);
        frutaMagnesio = view.findViewById(R.id.txtAddFrutaMagnesio);
        frutaManganes = view.findViewById(R.id.txtAddFrutaManganes);
        frutaFosforo = view.findViewById(R.id.txtAddFrutaFosforo);
        frutaFerro = view.findViewById(R.id.txtAddFrutaFerro);
        frutaSodio = view.findViewById(R.id.txtAddFrutaSodio);
        frutaPotassio = view.findViewById(R.id.txtAddFrutaPotassio);
        frutaCobre = view.findViewById(R.id.txtAddFrutaCobre);
        frutaZinco = view.findViewById(R.id.txtAddFrutaZinco);
        frutaRetinol = view.findViewById(R.id.txtAddFrutaRetinol);
        frutaTiamina = view.findViewById(R.id.txtAddFrutaTiamina);
        frutaRiboflavina = view.findViewById(R.id.txtAddFrutaRiboflavina);
        frutaPiridoxina = view.findViewById(R.id.txtAddFrutaPiridoxina);
        frutaNiacina = view.findViewById(R.id.txtAddFrutaNiacina);
        frutaVitaminaC = view.findViewById(R.id.txtAddFrutaVitaminaC);
        btnCadastrar = view.findViewById(R.id.btnAddFrutas);
        btnVoltar = view.findViewById(R.id.btnVoltarTipo);
        progressBar = view.findViewById(R.id.progressBarAddFruta);
        scrollView = view.findViewById(R.id.scrollViewAddFrutas);

        imageButtonCamera = view.findViewById(R.id.imageButtonCameraCadastrarFruta);
        imageButtonGaleria = view.findViewById(R.id.imageButtonGaleriaCadastrarFruta);
        circleImageViewPerfil = view.findViewById(R.id.circleImageViewAddFotoFruta);
    }

    private void verificarCampos(){
        try{
            boolean erro = false;

            if(frutaNome.getText().toString().equals("")){
                frutaNome.setError("Campo obrigatório!");
                erro = true;
            }
            if(frutaEnergia.getText().toString().equals("")){
                frutaEnergia.setError("Campo obrigatório!");
                erro = true;
            }
            if(frutaProteina.getText().toString().equals("")){
                frutaProteina.setError("Campo obrigatório!");
                erro = true;
            }
            if(frutaLipideos.getText().toString().equals("")){
                frutaLipideos.setError("Campo obrigatório!");
                erro = true;
            }
            if(frutaCarboidrato.getText().toString().equals("")){
                frutaCarboidrato.setError("Campo obrigatório!");
                erro = true;
            }
            if(frutaFibraAlimentar.getText().toString().equals("")){
                frutaFibraAlimentar.setError("Campo obrigatório!");
                erro = true;
            }

            if(!erro){
                try{

                    frutas.setNome_Alimento(frutaNome.getText().toString());
                    frutas.setNome_Pesquisa(frutaNome.getText().toString());

                    frutas.setKcal(Integer.parseInt(frutaEnergia.getText().toString()));
                    frutas.setProteina(Float.parseFloat(frutaProteina.getText().toString()));
                    frutas.setLipideos(Float.parseFloat(frutaLipideos.getText().toString()));
                    frutas.setCarboidrato(Float.parseFloat(frutaCarboidrato.getText().toString()));
                    frutas.setFibra_Alimentar(Float.parseFloat(frutaFibraAlimentar.getText().toString()));

                    enviarDados();

                }catch (Exception e){
                    Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e){
            Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarDados(){
        try {
            scrollView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            databaseReference.child(frutas.getNome_Alimento()).child("nome_pesquisa").setValue(frutas.getNome_Pesquisa()).addOnCompleteListener(
                    getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                inserirvalores();

                                Toast.makeText(getActivity(), frutaNome.getText().toString() + " cadastrado!", Toast.LENGTH_LONG).show();
                                scrollView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                //limparCampos();
                            }else {
                                String excessao = "";
                                try{
                                    throw task.getException();
                                }catch (FirebaseNetworkException e){
                                    excessao = "Verifique sua conexão!";
                                }catch (Exception e){
                                    excessao = "Erro: " + e.getMessage();
                                    e.printStackTrace();
                                }
                                scrollView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), excessao, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }catch (Exception e){
            Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inserirvalores(){

        databaseReference.child(frutas.getNome_Alimento()).child("adm_email").setValue(firebaseAuth.getCurrentUser().getEmail());
        databaseReference.child(frutas.getNome_Alimento()).child("adm_nome").setValue(firebaseAuth.getCurrentUser().getDisplayName());
        databaseReference.child(frutas.getNome_Alimento()).child("adm_data").setValue(getDateTime());

        databaseReference.child(frutas.getNome_Alimento()).child("kcal").setValue(frutas.getKcal());
        databaseReference.child(frutas.getNome_Alimento()).child("proteina").setValue(frutas.getProteina());
        databaseReference.child(frutas.getNome_Alimento()).child("lipedios").setValue(frutas.getLipideos());
        databaseReference.child(frutas.getNome_Alimento()).child("carboidrato").setValue(frutas.getCarboidrato());
        databaseReference.child(frutas.getNome_Alimento()).child("fibra_alimentar").setValue(frutas.getFibra_Alimentar());

        if(!frutaUmidade.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("umidade").setValue(Float.parseFloat(frutaUmidade.getText().toString()));
        }
        if(!frutaColesterol.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("colesterol").setValue(Integer.parseInt(frutaColesterol.getText().toString()));
        }
        if(!frutaCinzas.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("cinzas").setValue(Float.parseFloat(frutaCinzas.getText().toString()));
        }
        if(!frutaCalcio.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("calcio").setValue(Integer.parseInt(frutaCalcio.getText().toString()));
        }
        if(!frutaMagnesio.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("magnesio").setValue(Integer.parseInt(frutaMagnesio.getText().toString()));
        }
        if(!frutaManganes.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("manganes").setValue(Float.parseFloat(frutaManganes.getText().toString()));
        }
        if(!frutaFosforo.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("fosforo").setValue(Integer.parseInt(frutaFosforo.getText().toString()));
        }
        if(!frutaFerro.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("ferro").setValue(Float.parseFloat(frutaFerro.getText().toString()));
        }
        if(!frutaSodio.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("sodio").setValue(Integer.parseInt(frutaSodio.getText().toString()));
        }
        if(!frutaPotassio.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("potassio").setValue(Integer.parseInt(frutaPotassio.getText().toString()));
        }
        if(!frutaCobre.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("cobre").setValue(Float.parseFloat(frutaCobre.getText().toString()));
        }
        if(!frutaZinco.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("zinco").setValue(Float.parseFloat(frutaZinco.getText().toString()));
        }
        if(!frutaRetinol.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("retinol").setValue(Integer.parseInt(frutaRetinol.getText().toString()));
        }
        if(!frutaTiamina.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("tiamina").setValue(Float.parseFloat(frutaTiamina.getText().toString()));
        }
        if(!frutaRiboflavina.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("riboflavina").setValue(Float.parseFloat(frutaRiboflavina.getText().toString()));
        }
        if(!frutaPiridoxina.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("piridoxina").setValue(Float.parseFloat(frutaPiridoxina.getText().toString()));
        }
        if(!frutaVitaminaC.getText().toString().equals("")){
            databaseReference.child(frutas.getNome_Alimento()).child("vitamina_c").setValue(Float.parseFloat(frutaVitaminaC.getText().toString()));
        }

        if(circleImageViewPerfil != null){
            try{
                salvarImagemFirebase(frutaNome.getText().toString());
            }catch (Exception e){
                Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void abrirCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, SELECAO_CAMERA);
        }
    }

    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, SELECAO_GALERIA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == -1){

            try{

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }

                if(imagem != null){
                    circleImageViewPerfil.setImageBitmap(imagem);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Salvar Imagem
    private void salvarImagemFirebase(String nomeFruta){
        if(imagem != null){
            try {
                //Recuperar Dados da Imagem para o Firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dadosImagem = baos.toByteArray();

                //Salvar Imagem no Firebase
                final StorageReference imageRef = storageReference.child("imagens")
                        .child("alimentos")
                        .child("frutas")
                        .child(nomeFruta + ".jpeg");

                UploadTask uploadTask = imageRef.putBytes(dadosImagem);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(getActivity(), "Sucesso ao alterar a foto", Toast.LENGTH_SHORT).show();

                            try{
                                Uri downloadUri = task.getResult();

                                //atualizaFotoUsuario(downloadUri);
                                frutas.setCaminho_Foto(downloadUri.toString());
                                databaseReference.child(frutas.getNome_Alimento()).child("foto_caminho").setValue(downloadUri.toString());

                            }catch (Exception e){
                                Toast.makeText(getActivity(), "Erro ao pegar URL", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Erro ao alterar a foto", Toast.LENGTH_SHORT).show();
                        }
                        //Chamar Tela Principal
                        //chamarMain();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void atualizaFotoUsuario(Uri url){
        try {
            UsuarioFirebase.atualizarFotoUsuario(url);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
*/
}

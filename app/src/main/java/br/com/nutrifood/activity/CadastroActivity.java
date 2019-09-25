package br.com.nutrifood.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.com.nutrifood.R;
import br.com.nutrifood.config.ConfiguracaoFirebase;
import br.com.nutrifood.helper.Permissao;
import br.com.nutrifood.helper.UsuarioFirebase;
import br.com.nutrifood.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private Bitmap imagem = null;

    private boolean imagem_Vazia = true;

    private FirebaseAuth autenticacao;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
    private StorageReference storageReference;

    private ImageButton imageButtonCamera, imageButtonGaleria;

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private CircleImageView circleImageViewPerfil;

    private EditText cadastroNome, cadastroEmail, cadastroNumero, cadastroSenha, cadastroPeso, cadastroAltura;
    private RadioButton radioButtonMasculino, radioButtonFeminino;
    private ProgressBar progressBar;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Configurações Iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();

        //Validar Permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        cadastroNome = findViewById(R.id.cadastroNome);
        cadastroEmail = findViewById(R.id.cadastroEmail);
        cadastroNumero = findViewById(R.id.cadastroTelefone);
        cadastroSenha = findViewById(R.id.cadastroSenha);
        cadastroPeso = findViewById(R.id.cadastroPesoAtual);
        cadastroAltura = findViewById(R.id.cadastroAlturaAtual);
        radioButtonMasculino = findViewById(R.id.radioButtonCadMasculino);
        radioButtonFeminino = findViewById(R.id.radioButtonCadFeminino);

        progressBar = findViewById(R.id.progressBarCadastro);
        scrollView = findViewById(R.id.scrollViewCad_Usuario);

        imageButtonCamera = findViewById(R.id.imageButtonCameraCadastrar);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleriaCadastrar);

        circleImageViewPerfil = findViewById(R.id.circleImageViewAddFotoPerfil);
    }

    public void abrirCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, SELECAO_CAMERA);
        }
    }

    public void abrirGaleria(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, SELECAO_GALERIA);
        }
    }

    public void cadastrarUsuario(final Usuario usuario){

        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Bem vindo " + usuario.getNome() + "!", Toast.LENGTH_LONG).show();
                            UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                            try{

                                usuario.setId(UsuarioFirebase.getIndentificadorUsuario());

                                databaseReference.child(usuario.getId()).child("Email").setValue(usuario.getEmail());
                                databaseReference.child(usuario.getId()).child("Nome").setValue(usuario.getNome());
                                databaseReference.child(usuario.getId()).child("Numero").setValue(usuario.getNumero());

                                if(!usuario.getSexo().equals("")){
                                    databaseReference.child(usuario.getId()).child("Sexo").setValue(usuario.getSexo());
                                }
                                if(!cadastroAltura.getText().toString().equals("")){
                                    databaseReference.child(usuario.getId()).child("Altura").setValue(usuario.getAltura());
                                }
                                if(!cadastroPeso.getText().toString().equals("")){
                                    databaseReference.child(usuario.getId()).child("Peso").setValue(usuario.getPeso());
                                }

                                if(imagem_Vazia){
                                    chamarMain();
                                }else{
                                    salvarImagemFirebase(usuario.getId());
                                }
                            }catch (Exception e){
                                Toast.makeText(CadastroActivity.this, "Erro ao salvar informações: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }else {
                            scrollView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            String excessao = "";
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                excessao = "Digite uma senha mais forte!";
                                cadastroSenha.setError(excessao);
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excessao = "Por favor, digite um e-mail válido!";
                                cadastroEmail.setError(excessao);
                            }catch (FirebaseAuthUserCollisionException e){
                                excessao = "Esta conta já foi cadastrada!";
                                cadastroEmail.setError(excessao);
                            }catch (FirebaseNetworkException e){
                                excessao = "Verifique sua conexão!";
                            }catch (Exception e){
                                excessao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroActivity.this, excessao, Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    public void validarCadastroUsuario( View view){

        boolean erro = false;
        Usuario usuario = new Usuario();

        usuario.setNome("");
        usuario.setEmail("");
        usuario.setSenha("");
        usuario.setSexo("");

        if(cadastroNome.getText().toString().equals("")){
            this.cadastroNome.setError("Digite o Nome!");
            erro = true;
        }else{
            usuario.setNome(cadastroNome.getText().toString());
        }
        if(cadastroEmail.getText().toString().equals("")){
            this.cadastroEmail.setError("Digite o Email!");
            erro = true;
        }else{
            usuario.setEmail(cadastroEmail.getText().toString());
        }
        if(cadastroNumero.getText().toString().equals("")){
            this.cadastroNumero.setError("Digite o Numero!");
            erro = true;
        }else{
            usuario.setNumero(Long.parseLong(cadastroNumero.getText().toString()));
        }
        if(cadastroSenha.getText().toString().equals("")){
            this.cadastroSenha.setError("Digite a Senha!");
            erro = true;
        }else {
            usuario.setSenha(cadastroSenha.getText().toString());
        }

        if(!cadastroAltura.getText().toString().equals("")){
            if(Float.parseFloat(cadastroAltura.getText().toString()) >= 50){
                cadastroAltura.setText(String.valueOf(Float.parseFloat(cadastroAltura.getText().toString()) * 0.01));
            }
            else if(Float.parseFloat(cadastroAltura.getText().toString()) > 4 || Float.parseFloat(cadastroAltura.getText().toString()) < 0){
                cadastroAltura.setError("Altura inválida!");
                erro = true;
            }
        }
        if(!cadastroPeso.getText().toString().equals("")){
            if(Float.parseFloat(cadastroPeso.getText().toString()) > 600 || Float.parseFloat(cadastroPeso.getText().toString()) < 0){
                cadastroPeso.setError("Peso inválido!");
                erro = true;
            }
        }

        if(!erro){
            if(!cadastroPeso.getText().toString().equals("")){
                usuario.setPeso(Float.parseFloat(cadastroPeso.getText().toString()));
            }
            if(!cadastroAltura.getText().toString().equals("")){
                usuario.setAltura(Float.parseFloat(cadastroAltura.getText().toString()));
            }
            if(radioButtonMasculino.isChecked()){
                usuario.setSexo("Masculino");
            }
            if(radioButtonFeminino.isChecked()){
                usuario.setSexo("Feminino");
            }

            try {
                cadastrarUsuario(usuario);
            }catch (Exception e){
                Toast.makeText(CadastroActivity.this, getString(R.string.erro) + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void chamarMain(){
        Intent intent = new Intent(CadastroActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            try{

                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if(imagem != null){
                    circleImageViewPerfil.setImageBitmap(imagem);
                    imagem_Vazia = false;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //Salvar Imagem
    private void salvarImagemFirebase(String identificadorUsuario){
        if(imagem != null){
            try {
                //Recuperar Dados da Imagem para o Firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] dadosImagem = baos.toByteArray();

                //Salvar Imagem no Firebase
                final StorageReference imageRef = storageReference.child("imagens")
                        .child("perfil")
                        //.child(identificadorUsuario)
                        .child(identificadorUsuario + ".jpeg");

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

                            Toast.makeText(CadastroActivity.this, "Sucesso ao alterar a foto de perfil", Toast.LENGTH_SHORT).show();

                            try{
                                Uri downloadUri = task.getResult();

                                atualizaFotoUsuario(downloadUri);
                            }catch (Exception e){
                                Toast.makeText(CadastroActivity.this, "Erro ao pegar URL", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(CadastroActivity.this, "Erro ao alterar a foto de perfil", Toast.LENGTH_SHORT).show();
                        }
                        //Chamar Tela Principal
                        chamarMain();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(CadastroActivity.this, getString(R.string.erro) + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void atualizaFotoUsuario(Uri url){
        try {
            UsuarioFirebase.atualizarFotoUsuario(url);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(CadastroActivity.this, getString(R.string.erro) + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Perfil", "Erro ao atualizar foto de perfil. " + e.getMessage());
        }
    }
}


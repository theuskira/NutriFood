package br.com.nutrifood.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.nutrifood.R;
import br.com.nutrifood.config.ConfiguracaoFirebase;
import br.com.nutrifood.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    //Tela Entrar
    private EditText txtEmail, txtSenha;
    private ScrollView scrollViewEntrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponentes();

    }

    private void iniciarComponentes(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        txtEmail = findViewById(R.id.txtCadLogin_Email);
        txtSenha = findViewById(R.id.txtCadLogin_Senha);

        progressBar = findViewById(R.id.login_progress);
        scrollViewEntrar = findViewById(R.id.login_form);
    }

    public void chamarCadastro(View view){
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        finish();
    }

    public void redefinirSenha(View view){
        //
    }

    //Entrar
    public void logarUsuario(Usuario usuario){

        scrollViewEntrar.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(LoginActivity.this, "Bem vindo " + autenticacao.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();

                            abrirTelaPrincipal();
                            finish();
                        }else {
                            String excessao = "";
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                excessao = "Usuário não está cadastrado!";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                excessao = "E-mail e senha não correspondem a um usuário cadastrado!";
                            }catch (FirebaseNetworkException e){
                                excessao = "Verifique sua conexão!";
                            }catch (Exception e){
                                excessao = "Erro ao cadastrar usário: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this, excessao, Toast.LENGTH_LONG).show();
                            scrollViewEntrar.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }

    public void validarAutenticacaoUsuario(View view){

        if(!validarCampos()){
            Usuario usuario = new Usuario();
            usuario.setEmail(txtEmail.getText().toString());
            usuario.setSenha(txtSenha.getText().toString());

            logarUsuario(usuario);
        }

    }

    public boolean validarCampos(){
        boolean erro = false;

        if(txtEmail.getText().toString().equals("")){
            txtEmail.setError("Digite um e-mail!");
            erro = true;
        }
        if(txtSenha.getText().toString().equals("")){
            txtSenha.setError("Digite uma senha!");
            erro = true;
        }
        return erro;
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

}

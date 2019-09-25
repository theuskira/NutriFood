package br.com.nutrifood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import br.com.nutrifood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculoFragment extends Fragment {

    private EditText altura, peso;
    private Button calcular;
    private TextView infoIMC, infoPeso;
    private ImageView imagem;
    private CheckBox sexoMasculino, sexoFeminino;

    private DecimalFormat df = new DecimalFormat("###.##");
    private float imc;


    public CalculoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculo, container, false);

        inicializarComponentes(view);

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sexoFeminino.isChecked() && sexoMasculino.isChecked()){
                    Toast.makeText(getActivity(), "Selecione apenas um dos sexos!", Toast.LENGTH_SHORT).show();
                    infoPeso.setVisibility(View.GONE);
                }else {
                    try {
                        calcularSimples();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getContext(),R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private void inicializarComponentes(View view){

        altura = view.findViewById(R.id.editTextAlturaFragment);
        peso = view.findViewById(R.id.editTextPesoFragment);
        calcular = view.findViewById(R.id.btnCalcularFragment);
        infoIMC = view.findViewById(R.id.textoInformacaoIMCFragment);
        infoPeso = view.findViewById(R.id.textoInformacaoPesoIdealFragment);
        imagem = view.findViewById(R.id.imagemIMCFragment);
        sexoMasculino = view.findViewById(R.id.checkBoxCalculoSexM);
        sexoFeminino = view.findViewById(R.id.checkBoxCalculoSexF);

    }

    private void calcularSimples(){
        try{
            boolean erro = false;

            if(altura.getText().toString().equals("")){
                altura.setError("Digite a altura!");
                imagem.setVisibility(View.VISIBLE);
                imagem.setImageResource(R.drawable.invalido);
                infoPeso.setVisibility(View.GONE);
                infoIMC.setVisibility(View.GONE);
                erro = true;
            }
            else if(Float.parseFloat(altura.getText().toString()) >= 50){
                altura.setText(String.valueOf(Float.parseFloat(altura.getText().toString())  * 0.01));
            }
            else if(Float.parseFloat(altura.getText().toString()) > 4 || Float.parseFloat(altura.getText().toString()) < 0){
                altura.setError("Altura inválida!");
                imagem.setVisibility(View.VISIBLE);
                imagem.setImageResource(R.drawable.invalido);
                infoPeso.setVisibility(View.GONE);
                infoIMC.setVisibility(View.GONE);
                erro = true;
            }

            if(peso.getText().toString().equals("")){
                peso.setError("Digite o peso!");
                imagem.setVisibility(View.VISIBLE);
                imagem.setImageResource(R.drawable.invalido);
                infoPeso.setVisibility(View.GONE);
                infoIMC.setVisibility(View.GONE);
                erro = true;
            }
            else if(Float.parseFloat(peso.getText().toString()) > 600 || Float.parseFloat(peso.getText().toString()) < 0){
                peso.setError("Peso inválido!");
                imagem.setVisibility(View.VISIBLE);
                imagem.setImageResource(R.drawable.invalido);
                infoPeso.setVisibility(View.GONE);
                infoIMC.setVisibility(View.GONE);
                erro = true;
            }

            if(!erro){
                imc = (Float.parseFloat(peso.getText().toString()) / (Float.parseFloat(altura.getText().toString()) * Float.parseFloat(altura.getText().toString())));
                infoIMC.setText("IMC = " + df.format(imc) + " " + calculoIMC(imc));
                infoIMC.setVisibility(View.VISIBLE);
                AlterarImagem(imc);

                if(calculoPesoIdeal(Float.parseFloat(altura.getText().toString()), sexoMasculino, sexoFeminino).equals("")){
                    infoPeso.setVisibility(View.GONE);
                }else{
                    infoPeso.setText(calculoPesoIdeal(Float.parseFloat(altura.getText().toString()), sexoMasculino, sexoFeminino));
                    infoPeso.setVisibility(View.VISIBLE);
                }
            }

        }catch (Exception e){
            Toast.makeText(getActivity(), R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String calculoIMC(float imc){
        if(imc < 16){
            return "Desnutrição Severa";
        }
        else if(imc >= 16 && imc <= 16.99){
            return "Desnutrição Moderada";
        }
        else if(imc >= 17 && imc <= 18.49){
            return "Desnutrição Leve";
        }
        else if(imc >= 18.5 && imc <= 24.99){
            return "Peso Normal!";
        }
        else if(imc >= 25 && imc <= 26.99){
            return "Sobrepeso Grau I";
        }
        else if(imc >= 27 && imc <= 29.99){
            return "Sobrepeso Grau II (Pré-obeso)";
        }
        else if(imc >= 30 && imc <= 34.99){
            return "Obesidade I";
        }
        else if(imc >= 35 && imc <= 39.99){
            return "Obesidade II";
        }
        else if(imc >= 40 && imc <= 49.99){
            return "Obesidade III (Mórbida)";
        }
        else if(imc >= 50){
            return "Obesidade IV (Extrema)";
        }
        else{
            return "Valor Inválido!";
        }
    }

    private String calculoPesoIdeal(Float altura, CheckBox homem, CheckBox mulher){

        if(homem.isChecked()){
            return "Seu peso ideal é " + df.format(22.0 * (altura * altura)) + " Kg";
        }else if(mulher.isChecked()){
            return "Seu peso ideal é " + df.format(20.8 * (altura * altura)) + " Kg";
        }else {
            return "";
        }
    }

    private void AlterarImagem(float imc){
        if(imc != 0){
            imagem.setVisibility(View.VISIBLE);
        }
        if(imc < 17){
            // Muito abaixo do peso
            imagem.setImageResource(R.drawable.sad);
        }
        else if(imc >= 17 && imc <= 18.49){
            // Abaixo do Peso
            imagem.setImageResource(R.drawable.neutro);
        }
        else if(imc >= 18.5 && imc <= 24.99){
            // Peso Normal
            imagem.setImageResource(R.drawable.lol);
        }
        else if(imc >= 25 && imc <= 29.99){
            // Acima do Peso
            imagem.setImageResource(R.drawable.neutro);
        }
        else if(imc >= 30 && imc <= 34.99){
            // Obesidade I
            imagem.setImageResource(R.drawable.sad);
        }
        else if(imc >= 35 && imc <= 39.99){
            // Obesidade II (Severa)
            imagem.setImageResource(R.drawable.sad);
        }
        else if(imc >= 40){
            // Obesidade III (Mórbida)
            imagem.setImageResource(R.drawable.sad);
        }
        else{
            // Valor Inválido!
            imagem.setImageResource(R.drawable.invalido);
        }
    }

}

package br.com.nutrifood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import br.com.nutrifood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculoProFragment extends Fragment {

    private DecimalFormat df = new DecimalFormat("###.##");

    private ScrollView scrollViewEnviar, scrollViewMostrar;

    //ScrollView Pegar valores
    private Button calcular;
    private EditText altura, peso, idade;
    private CheckBox sexoMasculino, sexoFeminino;

    //ScrollView Mostrar Resultado
    private Button btnCalcularNovamente;
    private TextView faixaEtaria, idadeResultado, sexoResultado,
            pesoResultado, alturaResultado, imcResultado, classificacaoResultado,
            necessidadeHidricaResultado, pesoIdealResultado, pesoAjustadoResultado;


    public CalculoProFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_calculo_pro, container, false);

        inicializarComponentes(view);


        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sexoFeminino.isChecked() && sexoMasculino.isChecked()){
                    Toast.makeText(getActivity(), "Selecione apenas um dos sexos!", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        calcular();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getContext(),R.string.erro + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCalcularNovamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewEnviar.setVisibility(View.VISIBLE);
                scrollViewMostrar.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void inicializarComponentes(View view){

        //ScrollView Pegar Valoresx
        altura = view.findViewById(R.id.editTextProfAltura);
        peso = view.findViewById(R.id.editTextProfPeso);
        idade = view.findViewById(R.id.editTextProfIdade);
        calcular = view.findViewById(R.id.btnCalcularProf);
        scrollViewEnviar = view.findViewById(R.id.scrollViewEnviarDados);
        sexoMasculino = view.findViewById(R.id.checkBoxCalculoProSexM);
        sexoFeminino = view.findViewById(R.id.checkBoxCalculoProSexF);

        //ScrollView Mostrar Resultado
        btnCalcularNovamente = view.findViewById(R.id.btnCalcularNovamente);
        faixaEtaria = view.findViewById(R.id.faixaEtariaResultado);
        idadeResultado = view.findViewById(R.id.idadeResultado);
        sexoResultado = view.findViewById(R.id.sexoResultado);
        pesoResultado = view.findViewById(R.id.pesoAtualResultado);
        alturaResultado = view.findViewById(R.id.alturaResultado);
        imcResultado = view.findViewById(R.id.imcResultado);
        classificacaoResultado = view.findViewById(R.id.classificacaoResultado);
        necessidadeHidricaResultado = view.findViewById(R.id.necessidadesHidricasResultado);
        pesoIdealResultado = view.findViewById(R.id.pesoIdealResultado);
        pesoAjustadoResultado = view.findViewById(R.id.pesoAjustadoResultado);
        scrollViewMostrar = view.findViewById(R.id.scrollViewReceberDados);

    }

    private void calcular(){
        try{
            boolean erro = false;
            if(altura.getText().toString().equals("")){
                altura.setError("Digite a altura!");
                erro = true;
            }
            else if(Float.parseFloat(altura.getText().toString()) >= 50){
                altura.setText(String.valueOf(Float.parseFloat(altura.getText().toString())  * 0.01));
            }
            else if(Float.parseFloat(altura.getText().toString()) > 4 || Float.parseFloat(altura.getText().toString()) < 0){
                altura.setError("Altura inválida!");
                erro = true;
            }
            if(peso.getText().toString().equals("")){
                peso.setError("Digite o peso!");
                erro = true;
            }
            else if(Float.parseFloat(peso.getText().toString()) > 600 || Float.parseFloat(peso.getText().toString()) < 0){
                peso.setError("Peso inválido!");
                erro = true;
            }
            if(idade.getText().toString().equals("")){
                idade.setError("Digite a idade!");
                erro = true;
            }
            else if(Float.parseFloat(idade.getText().toString()) > 150 || Float.parseFloat(idade.getText().toString()) < 0){
                idade.setError("Idade inválida!");
                erro = true;
            }

            if(!erro){
                //Chamar novo Scroll View
                mostrarResultado(Float.parseFloat(altura.getText().toString()), Float.parseFloat(peso.getText().toString()),
                        Integer.parseInt(idade.getText().toString()));
            }

        }catch (Exception e){
            Toast.makeText(getContext(), R.string.erro + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarResultado(float altura, float peso, int idade){


        scrollViewEnviar.setVisibility(View.GONE);
        scrollViewMostrar.setVisibility(View.VISIBLE);

        imcResultado.setText(String.valueOf(df.format(peso/(altura*altura))));
        classificacaoResultado.setText(calculoIMC(peso/(altura*altura)));
        faixaEtaria.setText(faixaEtaria(idade));
        alturaResultado.setText(df.format(altura));
        pesoResultado.setText(df.format(peso));
        idadeResultado.setText(String.valueOf(idade));
        sexoResultado.setText(verificarSexo(sexoMasculino, sexoMasculino));
        necessidadeHidricaResultado.setText(df.format(calculoHidrico(peso, idade) * 0.001));
        pesoIdealResultado.setText(df.format(pesoIdeal(verificarSexo(sexoMasculino, sexoFeminino), altura)));
        pesoAjustadoResultado.setText(df.format((pesoIdeal(verificarSexo(sexoMasculino, sexoFeminino), altura) - peso) * 0.25 + peso));
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

    private String faixaEtaria(int idade){
        if(idade < 18){
            return "Jovem";
        }
        else if(idade >= 18 && idade < 55){
            return "Adulto";
        }
        else if(idade >= 55){
            return "Idoso";
        }
        else{
            return "Valor Inválido!";
        }
    }

    private float calculoHidrico(float peso, int idade){
        if(idade < 18){
            return peso * 40;
        }
        else if(idade >= 18 && idade < 55){
            return peso * 35;
        }
        else if(idade >= 55 && idade <= 75){
            return peso * 30;
        }
        else {
            return peso * 25;
        }
    }

    private double pesoIdeal(String sexo, float altura){
        if(sexo.equals("Masculino")){
            return 22 * (altura * altura);
        }
        else if(sexo.equals("Feminino")){
            return 20.8 * (altura * altura);
        }
        else{
            return 21.7 * (altura * altura);
        }
    }

    private String verificarSexo(CheckBox masculino, CheckBox feminino){
        if(masculino.isChecked()){
            return "Masculino";
        }
        else if(feminino.isChecked()){
            return "Feminino";
        }
        else {
            return "Indefinido";
        }
    }

}

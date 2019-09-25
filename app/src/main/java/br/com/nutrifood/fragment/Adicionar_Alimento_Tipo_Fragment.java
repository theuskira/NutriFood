package br.com.nutrifood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.nutrifood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Adicionar_Alimento_Tipo_Fragment extends Fragment {

    private Button btnFrutas;


    public Adicionar_Alimento_Tipo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adicionar_alimento_tipo, container, false);

        btnFrutas = view.findViewById(R.id.btnAlimento_Frutas);


        btnFrutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdicionarFrutasFragment adicionarFrutasFragment = new AdicionarFrutasFragment();

                iniciarFragment(adicionarFrutasFragment);
            }
        });

        return view;
    }

    private void iniciarFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}
